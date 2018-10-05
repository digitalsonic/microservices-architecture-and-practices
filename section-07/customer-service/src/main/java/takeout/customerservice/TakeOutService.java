package takeout.customerservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import takeout.customerservice.model.Order;
import takeout.customerservice.model.Payment;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class TakeOutService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${waiter.url}")
    private String waiterUrl;
    @Value("${customer.name}")
    private String customer;

    @HystrixCommand(fallbackMethod = "defaultWait")
    public int askHowLongToWait() {
        URI uri = UriComponentsBuilder.fromHttpUrl(waiterUrl + "/orders").build().toUri();
        List<Order> orders = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {}).getBody();
        log.info("There are {} orders ahead.", orders.size());
        return orders.size();
    }

    public Long makeAnOrder() {
        Order rawOrder = Order.builder().customer(customer).items("Coffee").price(2000L).build();
        Order order = restTemplate.postForObject(waiterUrl + "/orders", rawOrder, Order.class);
        log.info("Order created: {}", order);
        return order.getId();
    }

    public void payTheOrder(Long id) {
        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity(
                waiterUrl + "/orders/{id}/payment", null, Payment.class, id);
        Payment payment = paymentResponse.getBody();
        log.info("Payment Response: {}", paymentResponse.getStatusCodeValue());
        log.info("Payment Content: {}", payment);

        payment.setState("S");
        restTemplate.put(waiterUrl + "/orders/{id}/payment", payment, id);

        Order payedOrder = restTemplate.getForObject(waiterUrl + "/orders/{id}", Order.class, id);
        log.info("Now, let's see the Order after payment: {}", payedOrder);
    }

    public int defaultWait() {
        log.warn("Fall back to -1");
        return -1;
    }
}
