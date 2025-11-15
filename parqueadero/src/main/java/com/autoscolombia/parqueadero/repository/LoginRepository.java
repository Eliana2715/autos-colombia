package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginRepository extends JpaRepository<Login, Long> {
    Login findByUsernameAndPassword(String username, String password);
}
