package fr.pir.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.pir.model.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleById(Long id);

    Role findRoleByName(String name);

}
