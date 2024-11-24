//package com.Hackathon.minutesGamification.database.service;
//
//import com.Hackathon.minutesGamification.database.entities.*;
//import com.Hackathon.minutesGamification.database.repository.*;
//import jakarta.annotation.PostConstruct;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Testing {
//
//    private final CityDarkStoreMappingRepository cityDarkStoreMappingRepository;
//
//    private final OrderCountRepository orderCountRepository;
//
//    private final P2DO2DRepository p2DO2DRepository;
//
//    private final CitiesRepository cities;
//
//    private final UsersRepository usersRepository;
//
//    public Testing(CityDarkStoreMappingRepository cityDarkStoreMappingRepository, OrderCountRepository orderCountRepository, P2DO2DRepository p2DO2DRepository, CitiesRepository cities, UsersRepository usersRepository) {
//        this.cityDarkStoreMappingRepository = cityDarkStoreMappingRepository;
//        this.orderCountRepository = orderCountRepository;
//        this.p2DO2DRepository = p2DO2DRepository;
//        this.cities = cities;
//        this.usersRepository = usersRepository;
//    }
//
////    @PostConstruct
////    public void init() {
//////        for (CityDarkStoreMapping mapping : cityDarkStoreMappingRepository.findAll()) {
//////            System.out.println(mapping.toString());
//////        }
//////
//////        for (OrderCount orderCount : orderCountRepository.findAll()) {
//////            System.out.println(orderCount.toString());
//////        }
//////
//////        for (P2DO2D p2DO2D : p2DO2DRepository.findAll()) {
//////            System.out.println(p2DO2D.toString());
//////        }
//////
//////        for (Cities cities1 : cities.findAll()){
//////            System.out.println(cities1);
//////        }
//////
//////        for (User user1 : usersRepository.findAll()){
//////            System.out.println(user1);
//////        }
////        getCities();
////    }
//
//    @Transactional
//    public void getCities(){
//        for (Cities cities1 : cities.findAll()){
//            System.out.println(cities1);
//        }
//
//
////        for (User user1 : usersRepository.findAll()){
////            System.out.println(user1);
////        }
//    }
//
//}
