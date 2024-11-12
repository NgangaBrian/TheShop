package com.example.TheShop.database.services;

import com.example.TheShop.darajaApi.dtos.OrdersModel;
import com.example.TheShop.database.models.OrderDTO;
import com.example.TheShop.database.models.OrderedProductDTO;
import com.example.TheShop.database.models.Payments;
import com.example.TheShop.database.repository.OrderRepository;
import com.example.TheShop.database.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<OrderDTO> getOrderByCustomerId(Long customerId) {
        List<OrdersModel> orders = orderRepository.findByCustomer_id(customerId);

        return orders.stream().map(ordersModel -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(ordersModel.getId());
            orderDTO.setOrderDate(ordersModel.getOrder_date().toString());
            orderDTO.setPaymentId(ordersModel.getPayment_id());

            Payments payments = paymentRepository.findById(ordersModel.getPayment_id())
                            .orElseThrow(() -> new RuntimeException("Payment not found"));

            orderDTO.setAmountPaid(payments.getAmount());

            orderDTO.setOrderedProducts(ordersModel.getProducts().stream().map(productsItem -> {
                OrderedProductDTO productDTO = new OrderedProductDTO();
                productDTO.setProductId(productsItem.getId());
                productDTO.setQuantity(productsItem.getQuantity());
                productDTO.setProductName(productsItem.getItemsModel().getProduct_name());
                productDTO.setImageUrl(productsItem.getItemsModel().getImage_url());
                return productDTO;
            }).collect(Collectors.toList()));
            return orderDTO;
        }).collect(Collectors.toList());
    }
}
