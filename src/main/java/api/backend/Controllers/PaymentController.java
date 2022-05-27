package api.backend.Controllers;


import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PaymentController {


    @RequestMapping(value = "/api/payment", method = RequestMethod.POST)
    public ResponseEntity<?> payment() throws StripeException {

        

        return new ResponseEntity<>("a", HttpStatus.OK);
    }

}
