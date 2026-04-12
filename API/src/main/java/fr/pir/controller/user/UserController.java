package fr.pir.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.user.UserTableInterface;
import fr.pir.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger L = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserTableInterface>> getUsers() {
        L.debug("");

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUsers());
    }

}
