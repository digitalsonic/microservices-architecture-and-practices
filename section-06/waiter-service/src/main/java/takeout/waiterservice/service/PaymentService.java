package takeout.waiterservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import takeout.waiterservice.model.Payment;
import takeout.waiterservice.model.TakeOutOrder;
import takeout.waiterservice.repository.PaymentRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order " + orderId + " has NO payment!"));
    }

    public Payment createPayment(Long orderId) {
        Optional<Payment> savedPayment = paymentRepository.findByOrderId(orderId);
        if (savedPayment.isPresent()) {
            return savedPayment.get();
        }

        TakeOutOrder order = orderService.getOrder(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order " + orderId + " is NOT Found!");
        }
        Payment payment = Payment.builder()
                .orderId(orderId)
                .price(order.getPrice())
                .state("I")
                .createTime(new Date())
                .modifyTime(new Date()).build();
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long orderId, Payment payment) {
        Payment savedPayment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order " + orderId + " has NO payment!"));

        if ("S".equalsIgnoreCase(payment.getState())) {
            orderService.payOrder(orderId);
        }
        savedPayment.setState(payment.getState());
        savedPayment.setModifyTime(new Date());
        return paymentRepository.save(savedPayment);
    }
}
