package bytecryb.clio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import bytecryb.clio.model.ResultUser;
import bytecryb.clio.model.User;
import bytecryb.clio.repository.UserRepository;


@RestController
@RequestMapping("/auth")
public class UserAuthentication {

    @Autowired
    private UserRepository userRepo;
    
    @PostMapping(path = "/login", consumes = "application/json")
    public String loginUser(@RequestBody ResultUser resUser) {
        return resUser.getFirstName();
    }

    @PostMapping(path = "/logout", consumes = "application/json")
    public String logoutUser(@RequestBody ResultUser resUser) {
        return resUser.getLastName();
    }

        // takes in a json with the parameters {username, email, password}
    @PostMapping(path = "/signup", consumes = "application/json")
    public User signupUser(@RequestBody User user) {
        return this.userRepo.save(user);
    }
}