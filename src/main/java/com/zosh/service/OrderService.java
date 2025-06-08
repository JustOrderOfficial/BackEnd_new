package com.zosh.service;

import java.util.List;

import com.zosh.dto.AddressDTO;
import com.zosh.dto.OrderDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.OrderException;
import com.zosh.modal.Order;
import com.zosh.modal.User;

public interface OrderService {
	
	public OrderDTO createOrder(User user, AddressDTO shippingAdressDTO) throws CartException;
	
	public Order findOrderById(String orderId) throws OrderException;
	
	public List<OrderDTO> usersOrderHistory(Long userId);
	
	public OrderDTO placedOrder(String orderId) throws OrderException;
	
	public OrderDTO confirmedOrder(String orderId)throws OrderException;
	
	public OrderDTO shippedOrder(String orderId) throws OrderException;
	
	public OrderDTO deliveredOrder(String orderId) throws OrderException;
	
	public OrderDTO canceledOrder(String orderId) throws OrderException;
	
	public List<OrderDTO> getAllOrders();
	
	public void deleteOrder(String orderId) throws OrderException;
	
}
