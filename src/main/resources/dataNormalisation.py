import pandas as pd
from sklearn.preprocessing import StandardScaler, RobustScaler, Normalizer, MinMaxScaler
import mysql.connector

# import mysql.connector
# Define the database connection parameters
db_params = {
    'host': 'localhost',
    'database': 'darkstore_data_2',
    'user': 'shipping_stage_user',
    'password': '1tBCNb4Dya',
    'port': 3307
}

def dbconectConnection():
	return mysql.connector.connect(**db_params).cursor


def getDarkstoreData():
	conn = mysql.connector.connect(**db_params)
	cur = conn.cursor()
	cur.execute('SELECT * FROM city_darkstore_mappings')
	rows = cur.fetchall()
	# for row in rows:
	#      print(row)
	cur.close()
	conn.close()
	return rows

def insert_data_in_darkstoredata(df):
	conn = mysql.connector.connect(**db_params)
	cursor = conn.cursor()
	sql = """
	INSERT IGNORE INTO `darkstore_level_data` (
		`darkstore_id`, `order_value`, `sla`, `picklist_creation_to_dispatch`,
		`handover_time`, `ship_to_delivered`, `unit_cont`, `unit_fsn_count`,
		`normalized_picklist_creation_to_dispatch`, `normalized_handover_time`,
		`normalized_ship_to_delivered`,`date`,`points`
		) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
	"""
	data=[(row['id'], row['order_original_billing_amount'], None , row['picklist_creation_to_dispatch'],
			row['handover_time'], row['ship_to_deliver'], row['unit_count'], None,
			row['picklist_creation_to_dispatch_per_fsn_normalised'], row['handover_time_normalised'],
			row['ship_to_deliver_normalised'], row['date_column'], None)
	for index, row in df.iterrows()]
	cursor.executemany(sql, data)
	conn.commit()

def insert_data_in_pickerdata(df):
	conn = mysql.connector.connect(**db_params)
	cursor = conn.cursor()
	sql = """
	INSERT IGNORE INTO picker_data (picker_id, picker_name, darkstore_id, p2d_minutes, date)
	VALUES (%s, %s, %s, %s, %s)
	"""
	data = [
	(row['picklist_assigned_to'], None, row['id'], row['picklist_creation_to_dispatch_minutes_per_fsn'], row['date_column'])
	for index, row in df.iterrows()
	]
	cursor.executemany(sql, data)
	conn.commit()



def convert_to_int(x):
    try:
        return int(x)
    except ValueError:
        raise TypeError(f"Could not convert string '{x}' to numeric")

def convert_to_float(x):
    try:
        return float(x)
    except ValueError:
        raise TypeError(f"Could not convert string '{x}' to numeric")

def cleanupData(df):
	print(f"Number of rows: {len(df)}")
	df = df.dropna(subset=['fulfill_item_unit_dispatch_expected_date_max_key','sla_minutes_max','picklist_creation_to_dispatch_minutes_max','ship_to_deliver_minutes_max','handover_time_minutes_max','order_to_dispatch_minutes_max'])
	print(f"Number of rows: {len(df)}")
	columns_to_convert = ['unit_count','order_original_billing_amount','unique_fsn_count']
	for column in columns_to_convert:
	    df[column] = df[column].apply(convert_to_int)

	columns_to_convert = ['picklist_creation_to_dispatch_minutes_max','handover_time_minutes_max','ship_to_deliver_minutes_max']
	for column in columns_to_convert:
		df[column] = df[column].apply(convert_to_float)
	return df

def renamecolumns(df):
	df.rename(columns={'fulfill_item_unit_dispatch_expected_date_max_key': 'date'}, inplace=True)
	df.rename(columns={'picklist_creation_to_dispatch_minutes_max': 'picklist_creation_to_dispatch'}, inplace=True)
	df.rename(columns={'handover_time_minutes_max': 'handover_time'}, inplace=True)
	df.rename(columns={'ship_to_deliver_minutes_max': 'ship_to_deliver'}, inplace=True)
	return df

def replaceZeros(df,list):
	# Identify the second lowest non-zero value in the 'picklist_creation_to_dispatch_minutes_per_fsn' column
	for column in list:
		non_zero_values = df[column][df[column] > 0]
		second_lowest_value = sorted(non_zero_values.unique())[0]
		df[column] = df[column].replace(0, second_lowest_value)
	return df

def processPickerData(df,date,darkstoredatadf):
	selected_columns = ['darkstore_id','unit_count', 'warehouse_name', 'date','picklist_assigned_to',
	'picklist_creation_to_dispatch','picklist_creation_to_dispatch_minutes_per_fsn']
	df=df[selected_columns]

	aggregated_df = df.groupby(['picklist_assigned_to','darkstore_id']).agg({
	    'picklist_creation_to_dispatch_minutes_per_fsn':'mean',
	    'picklist_creation_to_dispatch': 'mean',
	    'unit_count': 'sum'
	}).reset_index()

	aggregated_df['date_column'] = pd.to_datetime(date, format='%Y%m%d')
	result_df = pd.merge(aggregated_df, darkstoredatadf, on='darkstore_id')
	print(date)
	print (result_df[['darkstore_id','date_column','picklist_creation_to_dispatch_minutes_per_fsn','picklist_creation_to_dispatch','unit_count']])
	insert_data_in_pickerdata(result_df)




def processDarkStoreData(df,date,darkstoredatadf):
	selected_columns = ['darkstore_id','order_original_billing_amount','unit_count', 'warehouse_name', 'date','sla_minutes_max',
	'picklist_creation_to_dispatch','ship_to_deliver','handover_time','picklist_creation_to_dispatch_minutes_per_fsn','order_to_dispatch_minutes_max']
	df=df[selected_columns]



	aggregated_df = df.groupby('darkstore_id').agg({
	    'order_original_billing_amount': 'mean',
	    'picklist_creation_to_dispatch_minutes_per_fsn':'mean',
	    'picklist_creation_to_dispatch': 'mean',
	    'handover_time': 'mean',
	    'ship_to_deliver': 'mean',
	    'unit_count': 'sum'
	}).reset_index()


	print(df['sla_minutes_max'].head())
	print(df['ship_to_deliver'].head())
	print(df['order_to_dispatch_minutes_max'].head())
	print(df['handover_time'].head())
	df['ship_to_deliver']=df['ship_to_deliver']/(df['sla_minutes_max']-df['order_to_dispatch_minutes_max']-df['handover_time'])


	print(df['ship_to_deliver'].head())



	minmax_scaler = MinMaxScaler()
	aggregated_df['picklist_creation_to_dispatch_per_fsn_normalised'] = minmax_scaler.fit_transform(aggregated_df[['picklist_creation_to_dispatch_minutes_per_fsn']])
	aggregated_df['handover_time_normalised'] = minmax_scaler.fit_transform(aggregated_df[['handover_time']])
	aggregated_df['ship_to_deliver_normalised'] = minmax_scaler.fit_transform(aggregated_df[['ship_to_deliver']])


	# since its inverse relationship
	aggregated_df['picklist_creation_to_dispatch_per_fsn_normalised'] = 1 - aggregated_df['picklist_creation_to_dispatch_per_fsn_normalised']
	aggregated_df['handover_time_normalised'] =  1 - aggregated_df['handover_time_normalised']
	aggregated_df['ship_to_deliver_normalised'] =  1 - aggregated_df['ship_to_deliver_normalised']
	aggregated_df = replaceZeros(aggregated_df,['picklist_creation_to_dispatch_per_fsn_normalised','handover_time_normalised','ship_to_deliver_normalised'])

	aggregated_df['date_column'] = pd.to_datetime(date, format='%Y%m%d')
	result_df = pd.merge(aggregated_df, darkstoredatadf, on='darkstore_id')
	print(date)
	print (result_df[['darkstore_id','date_column','unit_count','picklist_creation_to_dispatch_per_fsn_normalised']])
	insert_data_in_darkstoredata(result_df)



def processData():
	darkstoredata = getDarkstoreData()
	columns = [  'id','city_id' ,'darkstore_id' ,'darkstore_name' ]
	darkstoredatadf = pd.DataFrame(darkstoredata,columns=columns)
	df = pd.read_csv('/Users/hardik.kothari/Downloads/972304b65c0fe30cfbb2a027feb0c5f5_csv.csv')
	df = df.iloc[:-1]
	print(df['handover_time_minutes_max'].describe)
	df = cleanupData(df)
	df['picklist_creation_to_dispatch_minutes_per_fsn'] = df['picklist_creation_to_dispatch_minutes_max']/df['unique_fsn_count']
	df = renamecolumns(df)
	df['date_column'] = pd.to_datetime(df['date'], format='%Y%m%d')
	unique_dates = df['date'].unique()
	for date in unique_dates:
		df_day= df[df['date'] == date]
		processDarkStoreData(df_day,date,darkstoredatadf)
		processPickerData(df_day,date,darkstoredatadf)




if __name__ == "__main__":
    processData()
