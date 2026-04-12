package fr.pir;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CrossOrigin
@SpringBootApplication
public class RDFApplication implements CommandLineRunner {

	private static final Logger L = LogManager.getLogger(RDFApplication.class);

	public static void main(String[] args) {
		L.debug("");

		SpringApplication.run(RDFApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		L.debug("Start main code");

		//
	}

}
