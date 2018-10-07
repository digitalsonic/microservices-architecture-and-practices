package takeout.chefservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
@EnableBinding(TakeOutStream.class)
@Slf4j
public class ChefServiceApplication {
	@Value("${chef.name}-${random.int}")
	private String chef;

	public static void main(String[] args) {
		SpringApplication.run(ChefServiceApplication.class, args);
	}

	@StreamListener("orderRequests")
	@SendTo("finishedOrders")
//	@Transformer(inputChannel = "orderRequests", outputChannel = "finishedOrders")
	public TakeOutOrder processOrder(TakeOutOrder request) {
		log.info("Receiving Order: {}", request);
		log.info("Cocking order {}...", request.getId());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		request.setChef(chef);
		request.setState("C");
		return request;
	}
}
