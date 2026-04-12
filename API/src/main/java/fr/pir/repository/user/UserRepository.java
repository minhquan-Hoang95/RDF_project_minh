package fr.pir.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.pir.model.user.User;
import fr.pir.model.user.UserTableInterface;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name != 'ADMIN'")
    List<UserTableInterface> findUsersForInterface();

    @Query("SELECT u FROM User u WHERE u.role.name = 'ADMIN' AND u.id != ?1")
    List<User> findAdminsWithoutCurrentUser(Long id);

}
