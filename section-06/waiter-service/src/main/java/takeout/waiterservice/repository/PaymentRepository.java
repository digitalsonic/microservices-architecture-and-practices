package takeout.waiterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import takeout.waiterservice.model.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
