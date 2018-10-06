package takeout.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import takeout.customerservice.model.TakeOutOrder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(TakeOutStream.class)
@Slf4j
public class CustomerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@StreamListener(target = "takeoutFood", condition = "headers['customer']=='${customer.name}'")
	public void takeoutFood(TakeOutOrder takeOutOrder) {
		log.info("I've taken the food {}", takeOutOrder);
	}
}
