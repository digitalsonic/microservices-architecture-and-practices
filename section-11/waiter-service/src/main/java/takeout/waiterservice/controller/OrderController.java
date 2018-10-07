package takeout.waiterservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import takeout.waiterservice.model.TakeOutOrder;
import takeout.waiterservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public TakeOutOrder getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @DeleteMapping("/{id}")
    public List<TakeOutOrder> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return orderService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TakeOutOrder createOrder(@RequestBody TakeOutOrder order) {
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<TakeOutOrder> getAllOrder() {
        return orderService.getAllOrders();
    }
}
