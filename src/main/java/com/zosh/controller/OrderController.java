package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.AddressDTO;
import com.zosh.dto.OrderDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.OrderException;
import com.zosh.exception.UserException;
import com.zosh.mapper.UserMapper;
import com.zosh.modal.Order;
import com.zosh.modal.User;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrderHandler(@RequestBody AddressDTO shippingAddressDTO) throws UserException, CartException {
        // Fetch user profile from SecurityContext
        UserDTO userDTO = userService.findUserProfileByAuth();
        // Convert UserDTO to User entity using the UserMapper
        User user = UserMapper.toEntity(userDTO);
        // Create the order
        OrderDTO orderDTO = orderService.createOrder(user, shippingAddressDTO);

        return new ResponseEntity<>(orderDTO, HttpStatus.OK); // Return OrderDTO
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> usersOrderHistoryHandler() throws OrderException, UserException {
        // Fetch user profile from SecurityContext
        UserDTO userDTO = userService.findUserProfileByAuth();
        User user = UserMapper.toEntity(userDTO); // Convert UserDTO to User entity
        // Get user's order history
        List<OrderDTO> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderHandler(@PathVariable String orderId) throws OrderException, UserException {
        // Fetch user profile from SecurityContext
        UserDTO userDTO = userService.findUserProfileByAuth();
        User user = UserMapper.toEntity(userDTO); // Convert UserDTO to User entity
        // Find the order by ID
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED); // Return Order entity
    }
}
