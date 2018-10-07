package takeout.waiterservice;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class Waiter {
    @Value("${prefix.name}")
    private String prefix;
    @Value("${random.int}")
    private int number;

    public String getWaiterName() {
        return prefix + "-" + number;
    }
}
