package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.OrderDTO;
import com.zosh.exception.OrderException;
import com.zosh.modal.Order;
import com.zosh.response.ApiResponse;
import com.zosh.service.OrderService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> getAllOrdersHandler() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<OrderDTO> confirmedOrderHandler(@PathVariable String orderId,
                                                           @RequestHeader("Authorization") String jwt) throws OrderException {
        OrderDTO order = orderService.confirmedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/ship")
    public ResponseEntity<OrderDTO> shippedOrderHandler(@PathVariable String orderId,
                                                        @RequestHeader("Authorization") String jwt) throws OrderException {
        OrderDTO order = orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<OrderDTO> deliveredOrderHandler(@PathVariable String orderId,
                                                           @RequestHeader("Authorization") String jwt) throws OrderException {
        OrderDTO order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> canceledOrderHandler(@PathVariable String orderId,
                                                          @RequestHeader("Authorization") String jwt) throws OrderException {
        OrderDTO order = orderService.canceledOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable String orderId,
                                                           @RequestHeader("Authorization") String jwt) throws OrderException {
        orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse("Order Deleted Successfully", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
