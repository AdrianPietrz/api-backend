package api.backend;

import com.stripe.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		Stripe.apiKey = "sk_test_7mJuPfZsBzc3JkrANrFrcDqC";
		SpringApplication.run(BackendApplication.class, args);
	}

}
