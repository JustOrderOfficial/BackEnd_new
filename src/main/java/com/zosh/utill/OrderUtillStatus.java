package com.zosh.utill;

import com.zosh.user.domain.OrderStatus;

public class OrderUtillStatus {
	
	public static OrderStatus getOrderStatusByCode(int code) {
        switch (code) {
            case 0: return OrderStatus.PENDING;
            case 1: return OrderStatus.PLACED;
            case 2: return OrderStatus.CONFIRMED;
            case 3: return OrderStatus.SHIPPED;
            case 4: return OrderStatus.DELIVERED;
            case 5: return OrderStatus.CANCELLED;
            default: throw new IllegalArgumentException("Invalid OrderStatus code: " + code);
        }
    }

}
