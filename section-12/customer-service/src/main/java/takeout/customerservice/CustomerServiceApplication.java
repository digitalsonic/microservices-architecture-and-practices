package takeout.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import takeout.customerservice.model.Order;
import takeout.customerservice.model.Payment;

import java.net.URI;
import java.util.List;

@SpringBootApplication
@RestController
@Slf4j
public class CustomerServiceApplication {
	@Autowired
	private RestTemplate restTemplate;
	@Value("${waiter.url}")
	private String waiterUrl;
	@Value("${customer.name}-${random.int}")
	private String customer;

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PostMapping("/orders")
	public Order createNewOrder() {
		URI uri = UriComponentsBuilder.fromHttpUrl(waiterUrl + "/orders").build().toUri();
		Order rawOrder = Order.builder().customer(customer).items("Coffee").price(2000L).build();

		List<Order> orders = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Order>>() {}).getBody();
		log.info("There are {} orders ahead.", orders.size());

		Order order = restTemplate.postForObject(uri, rawOrder, Order.class);
		log.info("Order created: {}", order);

		return order;
	}
}

