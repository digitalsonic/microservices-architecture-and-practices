package takeout.waiterservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import takeout.waiterservice.model.TakeOutOrder;
import takeout.waiterservice.repository.TakeOutOrderRepository;

import java.util.Date;

@Component
public class DataInitializerRunner implements ApplicationRunner {
    @Autowired
    private TakeOutOrderRepository orderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TakeOutOrder order = TakeOutOrder.builder()
                .customer("Steve")
                .waiter("Tony")
                .items("Hamburger")
                .price(1000L)
                .state("I")
                .createTime(new Date())
                .modifyTime(new Date()).build();
        orderRepository.save(order);

        order = TakeOutOrder.builder()
                .customer("Bruce")
                .waiter("Tony")
                .items("Sandwich")
                .price(1000L)
                .state("I")
                .createTime(new Date())
                .modifyTime(new Date()).build();
        orderRepository.save(order);
    }
}
