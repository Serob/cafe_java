package com.serob.cafe.controllers.api;

import com.serob.cafe.entities.CafeTable;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
//import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/users")
public class UserController {

    //Will inject with constructor
    private UserRepository userRepo;

    @Autowired
    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    //@PreAuthorize("hasAuthority('ADMIN_USER')")
    @PostMapping()
    public ResponseEntity<User> createUserByManager(@Validated @RequestBody User user, @RequestParam("manager_id") Long id) {
        Optional<User> managerOptional = userRepo.findById(id);

        if (managerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!managerOptional.get().isManager()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setRole(UserRole.WAITER);
        User savedUser = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        Optional<User> userOptional = userRepo.findById(id);
        return userOptional
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        User expectedUser = userRepo.findByEmail(user.getEmail());
        if (expectedUser == null || ! expectedUser.getPassword().equals(user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(expectedUser);
    }

    @GetMapping()
    public ResponseEntity<Iterable<User>> getAllUsers(){
        Iterable<User> allUsers = userRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @GetMapping("{id}/tables}")
    public ResponseEntity<Iterable<CafeTable>> getTables(@PathVariable Long id) {
        Optional<User> waiterOptional = userRepo.findById(id);
        if (waiterOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (waiterOptional.get().isManager()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User waiter = waiterOptional.get();
        return ResponseEntity.status(HttpStatus.OK).body(waiter.getAssignedTables());
    }
}
