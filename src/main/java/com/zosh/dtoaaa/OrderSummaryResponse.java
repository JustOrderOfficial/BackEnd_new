package com.zosh.dtoaaa;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryResponse {
	
	private Long orderId;
    private LocalDateTime orderDate;
    private String status;
    private int totalItems;
    private double totalAmount;
    private List<OrderItemSummary> items;

    public OrderSummaryResponse(Long orderId, LocalDateTime orderDate, String status,
                                int totalItems, double totalAmount, List<OrderItemSummary> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.items = items;
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<OrderItemSummary> getItems() {
		return items;
	}

	public void setItems(List<OrderItemSummary> items) {
		this.items = items;
	}
    
    
    
    

}
