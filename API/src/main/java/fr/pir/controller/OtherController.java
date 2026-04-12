package fr.pir.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping
public class OtherController {

	private static final Logger L = LogManager.getLogger(OtherController.class);

	@GetMapping({ "/hello", "/api/hello" })
	public ResponseEntity<String> helloWorld() {
		L.debug("");

		return ResponseEntity.status(HttpStatus.OK).body("Hello World!");
	}

	/**
	 * Endpoint to serve the Swagger UI HTML file.
	 *
	 * @return String -> Redirects to the static swagger.html file
	 */
	@GetMapping({ "/swagger", "/public" })
	public String redirectToSwagger() {
		return "redirect:/swagger.html";
	}

}
