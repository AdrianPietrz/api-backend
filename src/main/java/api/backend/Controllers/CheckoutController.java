package api.backend.Controllers;

import api.backend.Models.UserModel;
import api.backend.Models.Video;
import api.backend.Repositories.UserRepository;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.MailService;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import com.google.gson.Gson;
import com.stripe.model.PaymentIntent;
import com.stripe.param.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class CheckoutController {

    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private UserDetailsServices userDetailsService;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailSender;

    private String stripePublicKey = "sk_test_7mJuPfZsBzc3JkrANrFrcDqC";



    @RequestMapping(value = "/bought/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> bought(@RequestHeader(name = "Authorization") String token, @PathVariable Long id) throws Exception {
        token = jwtTokenUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsService.getUserByUsername(token);

        Video video = videoRepository.getById(id);
        if(!user.getVideoList().contains(video)){
            user.addVideo(video);
            video.getUsers().add(user);
            videoRepository.save(video);
            userRepository.save(user);

        }
        return new ResponseEntity<>("Bought!", HttpStatus.OK);
    }

    @RequestMapping(value = "/checkout/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> checkout(@RequestHeader(name = "Authorization") String token,@PathVariable Long id) throws Exception {


        Gson gson = new Gson();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(100L)
                .setCurrency("usd")
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        //mailSender.sendEmail(user.getEmail(),video.getTitle());
        return new ResponseEntity<>(gson.toJson(paymentIntent.getClientSecret()),HttpStatus.OK);

   }





}
