package com.zosh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.modal.Order;
import com.zosh.user.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Optional<Order> findByOrderId(String orderId);
	
//	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus = com.zosh.user.domain.OrderStatus.PLACED OR o.orderStatus = com.zosh.user.domain.OrderStatus.CONFIRMED OR o.orderStatus = com.zosh.user.domain.OrderStatus.SHIPPED OR o.orderStatus = com.zosh.user.domain.OrderStatus.DELIVERED)")
//	public List<Order> getUsersOrders(@Param("userId") Long userId);
	
	
//	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus IN (com.zosh.user.domain.OrderStatus.PLACED, com.zosh.user.domain.OrderStatus.CONFIRMED, com.zosh.user.domain.OrderStatus.SHIPPED, com.zosh.user.domain.OrderStatus.DELIVERED)")
//	public List<Order> getUsersOrders(@Param("userId") Long userId);
	
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus IN :statuses")
	public List<Order> getUsersOrders(@Param("userId") Long userId, @Param("statuses") List<OrderStatus> statuses);




	
//	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus IN :statuses OR o.orderStatus IS NULL)")
//	public List<Order> getUsersOrders(@Param("userId") Long userId, @Param("statuses") List<OrderStatus> statuses);


//	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus = PLACED OR o.orderStatus = CONFIRMED OR o.orderStatus = SHIPPED OR o.orderStatus = DELIVERED)")
//	public List<Order> getUsersOrders(@Param("userId") Long userId);
	
	List<Order> findAllByOrderByCreatedAtDesc();
	
	void deleteByOrderId(String orderId); 
}
