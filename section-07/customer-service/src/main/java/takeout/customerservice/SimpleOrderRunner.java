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
    private TakeOutService takeOutService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 100; i++) {
            if (takeOutService.askHowLongToWait() >=0) {
                break;
            }
            Thread.sleep(5000);
        }
        Long orderId = takeOutService.makeAnOrder();
        takeOutService.payTheOrder(orderId);
    }
}

