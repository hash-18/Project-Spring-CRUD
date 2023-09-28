package com.mac.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mac.crud.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
