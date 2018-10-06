package takeout.waiterservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import takeout.waiterservice.TakeOutStream;
import takeout.waiterservice.Waiter;
import takeout.waiterservice.model.TakeOutOrder;
import takeout.waiterservice.repository.TakeOutOrderRepository;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    private TakeOutOrderRepository orderRepository;
    @Autowired
    private Waiter waiter;
    @Autowired
    private TakeOutStream takeOutStream;
    @Autowired
    private DataSource dataSource;

    public List<TakeOutOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public TakeOutOrder getOrder(Long id) {
        return id == null ? null : orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order " + id + " is NOT found."));
    }

    public void deleteOrder(Long id) {
        orderRepository.delete(
                orderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Order " + id + " is NOT found."))
        );
    }

    public TakeOutOrder createOrder(TakeOutOrder order) {
        if (order != null) {
            order.setId(null);
            order.setWaiter(waiter.getWaiterName());
            order.setState("I");
            order.setCreateTime(new Date());
            order.setModifyTime(new Date());
            orderRepository.save(order);
        }
        log.info("Creat Order {}", order);
        return order;
    }

    public void payOrder(Long id) {
        TakeOutOrder order = orderRepository.getOne(id);
        order.setState("P");
        order.setModifyTime(new Date());
        orderRepository.save(order);
        log.info("Order {} payed.", id);

        takeOutStream.orderRequests().send(MessageBuilder.withPayload(order).build());
        log.info("Send order {} to chef.", id);
    }

    @StreamListener("finishedOrders")
    public void finishOrder(TakeOutOrder order) {
        Long id = order.getId();
        log.info("Chef {} has finished cooking order {}.", order.getChef(), id);

        TakeOutOrder savedOrder = orderRepository.getOne(id);
        savedOrder.setChef(order.getChef());
        savedOrder.setState("S");
        savedOrder.setModifyTime(new Date());
        orderRepository.save(savedOrder);

        log.info("Tell customer we've finished order {}.", id);
        takeOutStream.takeoutFood().send(MessageBuilder
                .withPayload(savedOrder)
                .setHeader("customer", savedOrder.getCustomer())
                .build(), 1000);
    }
}
