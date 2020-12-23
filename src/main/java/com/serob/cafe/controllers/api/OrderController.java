package com.serob.cafe.controllers.api;

import com.serob.cafe.entities.CafeTable;
import com.serob.cafe.entities.Order;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.OrderRepository;
import com.serob.cafe.repositories.TableRepository;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.services.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderRepository orderRepo;
    private RoleUserService roleService;
    private UserRepository userRepo;
    private TableRepository tableRepo;

    @Autowired
    public OrderController(OrderRepository orderRepo, RoleUserService roleService,
                           UserRepository userRepo,TableRepository tableRepo) {
        this.orderRepo = orderRepo;
        this.roleService = roleService;
        this.userRepo = userRepo;
        this.tableRepo = tableRepo;
    }

    @PostMapping()
    public ResponseEntity<Order> creatOrder(
            @Validated @RequestBody Order order,
            @RequestParam("waiter_id") Long waiterId,
            @RequestParam("table_id") Long tableId) {

        //---- Validate ----
        Optional<User> waiterOptional = userRepo.findById(waiterId);
        ResponseEntity badWaiterResponse = roleService.mustBeWaiter(waiterOptional);
        if(badWaiterResponse != null) {
            return badWaiterResponse;
        }

        Optional<CafeTable> tableOptional = tableRepo.findById(tableId);
        if (tableOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CafeTable table = tableOptional.get();
        Set<Order> existingOrders = table.getOrders();
        boolean hasOpen = existingOrders.stream().anyMatch(Order::isOpen);
        if (hasOpen) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        order.setTable(table);
        Order savedOrder = orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}
