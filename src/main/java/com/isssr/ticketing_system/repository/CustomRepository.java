package com.isssr.ticketing_system.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository {

    List customQuery(String query);

}
