package takeout.waiterservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import takeout.waiterservice.model.TakeOutOrder;
import takeout.waiterservice.repository.TakeOutOrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private TakeOutOrderRepository orderRepository;
    @Value("${waiter.name}-${random.int}")
    private String waiterName;

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
            order.setWaiter(waiterName);
            order.setState("I");
            order.setCreateTime(new Date());
            order.setModifyTime(new Date());
            orderRepository.save(order);
        }
        return order;
    }

    public void payOrder(Long id) {
        TakeOutOrder order = orderRepository.getOne(id);
        order.setState("P");
        order.setModifyTime(new Date());
        orderRepository.save(order);
    }
}
