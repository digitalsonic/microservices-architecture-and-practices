package takeout.waiterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import takeout.waiterservice.model.TakeOutOrder;

import java.util.List;

public interface TakeOutOrderRepository extends JpaRepository<TakeOutOrder, Long> {
    List<TakeOutOrder> findByCustomerOrderById(String customer);
}
