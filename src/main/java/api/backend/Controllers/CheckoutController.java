//package api.backend.Controllers;
//
//import com.google.gson.Gson;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.net.RequestOptions;
//import com.stripe.param.*;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class CheckoutController {
//
//
//    private String stripePublicKey = "sk_test_7mJuPfZsBzc3JkrANrFrcDqC";
//
//
//    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
//    public ResponseEntity<?> checkout() throws StripeException {
//        Gson gson = new Gson();
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(100L)
//                .setCurrency("usd")
//                .build();
//        PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//
//        System.out.println(paymentIntent.getClientSecret());
//        return new ResponseEntity<>(gson.toJson(paymentIntent.getClientSecret()),HttpStatus.OK);
//
//    }
//
//}
