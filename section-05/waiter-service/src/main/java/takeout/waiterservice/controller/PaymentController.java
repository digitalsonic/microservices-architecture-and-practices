package takeout.waiterservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import takeout.waiterservice.model.Payment;
import takeout.waiterservice.service.PaymentService;

@RestController
@RequestMapping("/orders/{id}/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Payment createPayment(@PathVariable Long id) {
        return paymentService.createPayment(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Payment getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }
}
