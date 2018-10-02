package takeout.waiterservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializerRunner implements ApplicationRunner {
    @Autowired
    private TakeOutOrderRepository orderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TakeOutOrder order = TakeOutOrder.builder()
                .customer("Tony")
                .waiter("Steve")
                .items("Hamburger")
                .createTime(new Date())
                .modifyTime(new Date()).build();
        orderRepository.save(order);

        order = TakeOutOrder.builder()
                .customer("Tony")
                .waiter("Bruce")
                .items("Sandwich")
                .createTime(new Date())
                .modifyTime(new Date()).build();
        orderRepository.save(order);
    }
}
