package com.serob.cafe.repositories;

import com.serob.cafe.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByEmail(String email);
}
