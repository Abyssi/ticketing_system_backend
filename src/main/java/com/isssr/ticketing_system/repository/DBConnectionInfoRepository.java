package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBConnectionInfoRepository extends JpaRepository<DBConnectionInfo, Long> {

    DBConnectionInfo findByUrlAndUsernameAndPassword(String url, String username, String password);

}
