package com.zosh.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zosh.dto.AddressDTO;
import com.zosh.dto.CartDTO;
import com.zosh.dto.CartItemDTO;
import com.zosh.dto.OrderDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.OrderException;
import com.zosh.mapper.AddressMapper;
import com.zosh.mapper.OrderItemMapper;
import com.zosh.mapper.OrderMapper;
import com.zosh.modal.Address;
import com.zosh.modal.Order;
import com.zosh.modal.OrderItem;
import com.zosh.modal.User;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.OrderItemRepository;
import com.zosh.repository.OrderRepository;
import com.zosh.repository.UserRepository;
import com.zosh.user.domain.OrderStatus;
import com.zosh.user.domain.PaymentStatus;

@Service
public class OrderServiceImplementation implements OrderService {

    private OrderRepository orderRepository;
    private CartService cartService;
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;

    public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService,
                                      AddressRepository addressRepository, UserRepository userRepository,
                                      OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImplementation.class);

    @Override
    public OrderDTO createOrder(User user, AddressDTO shippingAddressDTO) throws CartException {

        // Convert AddressDTO to Address entity and link user
        Address shippingAddress = AddressMapper.toEntity(shippingAddressDTO);
        shippingAddress.setUser(user);
        Address savedAddress = addressRepository.save(shippingAddress);
        
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        // Get CartDTO and map CartItemDTOs to OrderItem entities
        CartDTO cartDTO = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        // Iterate over cart items and map to order items
        for (CartItemDTO itemDTO : cartDTO.getCartItems()) {
            OrderItem orderItem = OrderItemMapper.fromCartItemDTO(itemDTO);
            orderItem.setOrder(null); // To avoid circular reference issues
            OrderItem savedItem = orderItemRepository.save(orderItem);
            orderItems.add(savedItem);
        }

        // Create Order entity
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalPrice(cartDTO.getTotalPrice());
        order.setTotalDiscountedPrice(cartDTO.getTotalDiscountedPrice());
        order.setDiscounte(cartDTO.getDiscountAmount());
        order.setTotalItem(cartDTO.getTotalItems());
        order.setShippingAddress(savedAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.getPaymentDetails().setStatus(PaymentStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // Generate and set Order ID
        String orderId = "ORD-" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" +
                UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        order.setOrderId(orderId);

        // Save Order entity
        Order savedOrder = orderRepository.save(order);

        // Link order items to the saved order
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        // Return the saved order as DTO
        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO confirmedOrder(String orderId) throws OrderException {
        // Find the order entity by orderId
        Order order = findOrderById(orderId);
        
        // Update order status
        order.setOrderStatus(OrderStatus.CONFIRMED);
        
        // Save updated order
        Order savedOrder = orderRepository.save(order);
        
        // Convert to DTO before returning
        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO shippedOrder(String orderId) throws OrderException {
        // Find the order entity by orderId
        Order order = findOrderById(orderId);
        
        // Update order status
        order.setOrderStatus(OrderStatus.SHIPPED);
        
        // Save updated order
        Order savedOrder = orderRepository.save(order);
        
        // Convert to DTO before returning
        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO deliveredOrder(String orderId) throws OrderException {
        // Find the order entity by orderId
        Order order = findOrderById(orderId);
        
        // Update order status
        order.setOrderStatus(OrderStatus.DELIVERED);
        
        // Save updated order
        Order savedOrder = orderRepository.save(order);
        
        // Convert to DTO before returning
        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO canceledOrder(String orderId) throws OrderException {
        // Find the order entity by orderId
        Order order = findOrderById(orderId);
        
        // Update order status
        order.setOrderStatus(OrderStatus.CANCELLED);
        
        // Save updated order
        Order savedOrder = orderRepository.save(order);
        
        // Convert to DTO before returning
        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public Order findOrderById(String orderId) throws OrderException {
        return orderRepository.findByOrderId(orderId)
               .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
    }

    @Override
    public List<OrderDTO> usersOrderHistory(Long userId) {
        List<OrderStatus> statuses = List.of(
            OrderStatus.PLACED,
            OrderStatus.CONFIRMED,
            OrderStatus.SHIPPED,
            OrderStatus.DELIVERED
        );
        List<Order> orders = orderRepository.getUsersOrders(userId, statuses);
        return orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(String orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteByOrderId(orderId);
    }

    @Override
    public OrderDTO placedOrder(String orderId) throws OrderException {
        // Find the order by orderId
        Order order = findOrderById(orderId);

        // Set the order status to PLACED
        order.setOrderStatus(OrderStatus.PLACED);

        // Save the updated order
        Order savedOrder = orderRepository.save(order);

        // Return the updated order as OrderDTO
        return OrderMapper.toDTO(savedOrder);
    }

}
