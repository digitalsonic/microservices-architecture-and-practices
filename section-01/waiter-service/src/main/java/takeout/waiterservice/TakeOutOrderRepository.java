package takeout.waiterservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeOutOrderRepository extends JpaRepository<TakeOutOrder, Long> {
}
