package takeout.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import takeout.customerservice.model.Payment;
import takeout.customerservice.model.TakeOutOrder;

import java.net.URI;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(TakeOutStream.class)
@Slf4j
@RestController
public class CustomerServiceApplication {
	@Autowired
	private RestTemplate restTemplate;
	@Value("${waiter.url}")
	private String waiterUrl;
	@Value("${customer.name}")
	private String customer;

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PostMapping("/orders")
	public TakeOutOrder createNewOrder() {
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

		return payedOrder;
	}

	@StreamListener(target = "takeoutFood", condition = "headers['customer']=='${customer.name}'")
	public void takeoutFood(TakeOutOrder takeOutOrder) {
		log.info("I've taken the food {}", takeOutOrder);
	}
}
