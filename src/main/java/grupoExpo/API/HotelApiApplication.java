package grupoExpo.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelApiApplication {

	public static void main(String[] args) {

        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");
        String dbDriver = System.getenv("BD_DRIVER");

		SpringApplication.run(HotelApiApplication.class, args);
	}

}
