package fr.pir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.user.Role;
import fr.pir.repository.user.RoleRepository;

@CrossOrigin
@SpringBootApplication
public class RDFApplication implements CommandLineRunner {

	private static final Logger L = LogManager.getLogger(RDFApplication.class);

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		L.debug("");

		SpringApplication.run(RDFApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		L.debug("Start main code");

		seedRoleIfMissing("USER");
		seedRoleIfMissing("ADMIN");
	}

	private void seedRoleIfMissing(String name) {
		if (roleRepository.findRoleByName(name) == null) {
			Role role = new Role();
			role.setName(name);
			roleRepository.save(role);
			L.info("Seeded missing role: {}", name);
		}
	}

}
