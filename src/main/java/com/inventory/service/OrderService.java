package com.inventory.service;

import com.inventory.dto.request.OrderRequest;
import com.inventory.dto.response.OrderItemResponse;
import com.inventory.dto.response.OrderResponse;
import com.inventory.entity.*;
import com.inventory.exception.OrderException;
import com.inventory.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    /** Create new order */
    public OrderResponse createOrder(OrderRequest request) {
        Store store = storeRepository.findByIdAndActiveTrue(request.getStoreId())
                .orElseThrow(() -> new OrderException("ORD001", "Store not found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new OrderException("ORD002", "User not found", HttpStatus.NOT_FOUND));

        Order order = new Order();
        order.setStore(store);
        order.setCreatedBy(user);

//        BigDecimal total = BigDecimal.ZERO;

        List<OrderItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new OrderException("ORD003", "Product not found", HttpStatus.NOT_FOUND));

            Category category = categoryRepository.findById(itemReq.getCategoryId())
                    .orElseThrow(() -> new OrderException("ORD004", "Category not found", HttpStatus.NOT_FOUND));

            if (!product.getCategory().getId().equals(category.getId())) {
                throw new OrderException("ORD005", "Product does not belong to selected category", HttpStatus.BAD_REQUEST);
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setCategory(category);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());

            return item;
        }).collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setItems(items);
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        notificationService.createNotification(user, "ORDER_PLACED", "Your order has been placed successfully!", "/api/v1/orders/" + order.getId());
        return mapToResponse(saved);
    }

    /** Get order by ID */
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("ORD006", "Order not found", HttpStatus.NOT_FOUND));
        return mapToResponse(order);
    }

    /** List all orders */
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /** Mapper */
    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStoreName(order.getStore().getName());
        response.setCreatedBy(order.getCreatedBy().getFullName());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(order.getItems().stream().map(this::mapToItemResponse).collect(Collectors.toList()));
        return response;
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        OrderItemResponse resp = new OrderItemResponse();
        resp.setProductId(item.getProduct().getId());
        resp.setProductName(item.getProduct().getName());
        resp.setCategoryName(item.getCategory().getName());
        resp.setQuantity(item.getQuantity());
        resp.setPrice(item.getPrice());
        resp.setTotalPrice(item.getTotalPrice());
        return resp;
    }
}
