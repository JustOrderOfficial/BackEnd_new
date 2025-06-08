package com.zosh.service;

import org.springframework.stereotype.Service;

import com.zosh.dto.OrderItemDTO;
import com.zosh.mapper.OrderItemMapper;
import com.zosh.repository.OrderItemRepository;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

	private OrderItemRepository orderItemRepository;
	
	public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}
	
	@Override
	public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
		var orderItem = OrderItemMapper.toEntity(orderItemDTO);
		orderItem = orderItemRepository.save(orderItem);
		return OrderItemMapper.toDTO(orderItem);
	}
}
