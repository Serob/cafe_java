package com.serob.cafe.services;

import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleUserService {
    private UserRepository userRepo;

    public RoleUserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity mustBeManager(Long id){
        Optional<User> managerOptional = userRepo.findById(id);
        return mustBeManager(managerOptional);
    }

    public ResponseEntity mustBeManager(Optional<User> user){
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!user.get().isManager()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return null;
    }

    public ResponseEntity mustBeWaiter(Long id){
        Optional<User> waiterOptional = userRepo.findById(id);
        return mustBeWaiter(waiterOptional);
    }

    public ResponseEntity mustBeWaiter(Optional<User> user){
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.get().isManager()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return null;
    }

}
