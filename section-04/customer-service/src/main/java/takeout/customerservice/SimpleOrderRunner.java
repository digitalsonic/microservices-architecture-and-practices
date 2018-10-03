package takeout.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import takeout.customerservice.model.TakeOutOrder;

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
        ResponseEntity<Resources<Link>> response =
                restTemplate.exchange(waiterUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Resources<Link>>() {});
        log.info("{}", response.getBody());
        List<Link> links = response.getBody().getLinks();
        Link orderLink = null;
        for (Link l : links) {
            if ("takeOutOrders".equals(l.getRel())) {
                orderLink = l;
                break;
            }
        }
        if (orderLink == null) {
            log.warn("Can NOT find takeOutOrders Link.");
            return;
        } else {
            log.info("Get Link for TakeOutOrders - {}", orderLink);
        }

        String url = orderLink.expand().getHref().toString();
        log.info("Ready to call {}", url);
        PagedResources<TakeOutOrder> orders = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedResources<TakeOutOrder>>() {}).getBody();
        log.info("There are {} orders ahead.", orders.getContent().size());
        log.info("Order info: {}", orders);
    }
}

