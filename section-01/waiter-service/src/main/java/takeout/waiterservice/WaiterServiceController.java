package takeout.waiterservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WaiterServiceController {
    @Autowired
    private TakeOutOrderRepository orderRepository;

    @RequestMapping("/orders/{id}")
    public TakeOutOrder getOrder(@PathVariable("id") Long id) {
        return id == null ? null : orderRepository.getOne(id);
    }

    @RequestMapping("/orders")
    public List<TakeOutOrder> getAllOrder() {
        return orderRepository.findAll();
    }
}
