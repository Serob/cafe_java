package com.serob.cafe.controllers.api;

import com.serob.cafe.entities.CafeTable;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.TableRepository;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.services.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    //Will inject with constructor
    private UserRepository userRepo;
    private TableRepository tableRepo;
    private RoleUserService roleService;

    @Autowired
    public TableController(UserRepository userRepo, TableRepository tableRepo, RoleUserService roleService) {
        this.userRepo = userRepo;
        this.tableRepo = tableRepo;
        this.roleService = roleService;
    }

    @PostMapping()
    public ResponseEntity<CafeTable> createTable(@Validated @RequestBody CafeTable table, @RequestParam("manager_id") Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        ResponseEntity badResponse = roleService.mustBeManager(userOptional);
        if (badResponse != null) {
            return badResponse;
        }
        User user = userOptional.get();
        //Didn't save id for table. DO not have time to check the reason.
       /* user.addTable(table);
        User savedUser = userRepo.save(user);*/

        table.setCreatedBy(user);
        CafeTable createdTable = tableRepo.save(table);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @GetMapping()
    public ResponseEntity<Iterable<CafeTable>> getAllTables(){
        Iterable<CafeTable> allTables = tableRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allTables);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CafeTable> assignToWaiter(
            @PathVariable Long id,
            @RequestParam("manager_id") Long managerId,
            @RequestParam("waiter_id") Long waiterId) {
        //--validate---
        ResponseEntity badManagerResponse = roleService.mustBeManager(managerId);
        if(badManagerResponse != null) {
            return badManagerResponse;
        }
        Optional<User> waiterOptional = userRepo.findById(waiterId);
        ResponseEntity badWaiterResponse = roleService.mustBeWaiter(waiterOptional);
        if(badWaiterResponse != null) {
            return badWaiterResponse;
        }

        Optional<CafeTable> tableOptional= tableRepo.findById(id);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //------
        CafeTable table = tableOptional.get();
        User waiter = waiterOptional.get();
        table.setAssignedTo(waiter);
        CafeTable savedTable = tableRepo.save(table);
        return ResponseEntity.status(HttpStatus.OK).body(savedTable);
    }

}
