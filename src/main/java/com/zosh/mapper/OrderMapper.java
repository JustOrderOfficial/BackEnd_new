package com.zosh.mapper;

import java.util.stream.Collectors;

import com.zosh.dto.OrderDTO;
import com.zosh.modal.Order;
import com.zosh.modal.User;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        dto.setOrderItems(order.getOrderItems()
                .stream().map(OrderItemMapper::toDTO).collect(Collectors.toList()));
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setShippingAddress(AddressMapper.toDTO(order.getShippingAddress()));
        dto.setPaymentDetails(PaymentDetailsMapper.toDTO(order.getPaymentDetails()));
        dto.setTotalPrice(order.getTotalPrice());
        dto.setTotalDiscountedPrice(order.getTotalDiscountedPrice());
        dto.setDiscounte(order.getDiscounte());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalItem(order.getTotalItem());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    public static Order toEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderId(dto.getOrderId());

        // Associate user by ID (may need to be set by service layer)
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            order.setUser(user);
        }

        order.setOrderItems(dto.getOrderItems()
                .stream().map(itemDTO -> {
                    var item = OrderItemMapper.toEntity(itemDTO);
                    item.setOrder(order); // important for bidirectional mapping
                    return item;
                }).collect(Collectors.toList()));

        order.setOrderDate(dto.getOrderDate());
        order.setDeliveryDate(dto.getDeliveryDate());
        order.setShippingAddress(AddressMapper.toEntity(dto.getShippingAddress()));
        order.setPaymentDetails(PaymentDetailsMapper.toEntity(dto.getPaymentDetails()));
        order.setTotalPrice(dto.getTotalPrice());
        order.setTotalDiscountedPrice(dto.getTotalDiscountedPrice());
        order.setDiscounte(dto.getDiscounte());
        order.setOrderStatus(dto.getOrderStatus());
        order.setTotalItem(dto.getTotalItem());
        order.setCreatedAt(dto.getCreatedAt());
        return order;
    }
}
