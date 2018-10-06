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
import takeout.customerservice.model.TakeOutOrder;
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
        TakeOutOrder rawOrder = TakeOutOrder.builder().customer(customer).items("Coffee").price(2000L).build();

        List<TakeOutOrder> takeOutOrders = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TakeOutOrder>>() {}).getBody();
        log.info("There are {} takeOutOrders ahead.", takeOutOrders.size());

        TakeOutOrder order = restTemplate.postForObject(uri, rawOrder, TakeOutOrder.class);
        log.info("TakeOutOrder created: {}", order);

        ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity(
                waiterUrl + "/orders/{id}/payment", null, Payment.class, order.getId());
        Payment payment = paymentResponse.getBody();
        log.info("Payment Response: {}", paymentResponse.getStatusCodeValue());
        log.info("Payment Content: {}", payment);

        payment.setState("S");
        restTemplate.put(waiterUrl + "/orders/{id}/payment", payment, order.getId());

        TakeOutOrder payedOrder = restTemplate
                .getForObject(waiterUrl + "/orders/{id}", TakeOutOrder.class, order.getId());
        log.info("Now, let's see the TakeOutOrder after payment: {}", payedOrder);
    }
}

