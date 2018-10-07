package takeout.chefservice;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface TakeOutStream {
    @Output
    MessageChannel finishedOrders();
    @Input
    SubscribableChannel orderRequests();
}
