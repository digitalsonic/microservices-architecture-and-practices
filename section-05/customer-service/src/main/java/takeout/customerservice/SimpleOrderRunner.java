package takeout.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import takeout.customerservice.model.Order;
import takeout.customerservice.model.Payment;

import java.net.URI;
import java.util.List;

@Component
@Slf4j
public class SimpleOrderRunner implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${waiter.url}")
    private String waiterUrl;
    @Value("${customer.name}")
    private String customer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(waiterUrl + "/orders").build().toUri();
        Order rawOrder = Order.builder().customer(customer).items("Coffee").price(2000L).build();

        List<Order> orders = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {}).getBody();
        log.info("There are {} orders ahead.", orders.size());

        Order order = restTemplate.postForObject(uri, rawOrder, Order.class);
        log.info("Order created: {}", order);

        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity(
                waiterUrl + "/orders/{id}/payment", null, Payment.class, order.getId());
        Payment payment = paymentResponse.getBody();
        log.info("Payment Response: {}", paymentResponse.getStatusCodeValue());
        log.info("Payment Content: {}", payment);

        payment.setState("S");
        restTemplate.put(waiterUrl + "/orders/{id}/payment", payment, order.getId());

        Order payedOrder = restTemplate.getForObject(waiterUrl + "/orders/{id}", Order.class, order.getId());
        log.info("Now, let's see the Order after payment: {}", payedOrder);
    }
}

