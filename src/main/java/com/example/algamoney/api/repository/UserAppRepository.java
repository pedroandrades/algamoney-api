package com.example.algamoney.api.repository;

import com.example.algamoney.api.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    public Optional<UserApp> findByEmail(String email);
}
