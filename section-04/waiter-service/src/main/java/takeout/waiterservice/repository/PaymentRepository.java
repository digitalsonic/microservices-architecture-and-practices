package takeout.waiterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import takeout.waiterservice.model.Payment;

import java.util.Optional;

@RepositoryRestResource
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);
}
