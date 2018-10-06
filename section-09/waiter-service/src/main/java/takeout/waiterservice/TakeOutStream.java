package takeout.waiterservice;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface TakeOutStream {
    @Input
    SubscribableChannel finishedOrders();
    @Output
    MessageChannel orderRequests();
    @Output
    MessageChannel takeoutFood();
}
