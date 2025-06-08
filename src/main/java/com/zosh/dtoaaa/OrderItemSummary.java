package com.zosh.dtoaaa;

public class OrderItemSummary {
	
	private String productTitle;
    private int quantity;

    public OrderItemSummary(String productTitle, int quantity) {
        this.productTitle = productTitle;
        this.quantity = quantity;
    }

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
    
    

}
