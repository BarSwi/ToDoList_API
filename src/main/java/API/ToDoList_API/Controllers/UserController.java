package API.ToDoList_API.Controllers;

import API.ToDoList_API.Models.User;
import API.ToDoList_API.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test/{username}")
    public ResponseEntity<?> test(@PathVariable String username){
        String password = "CqzjNqU7WXMHQy7BL2whUqlB9aqG4WR1O3XJ90d40zg=";
        User authenticatedUser = userService.Authenticate(username, password).orElse(null);

        if (authenticatedUser != null) {
            // Authentication successful, return the user or any required information
            return ResponseEntity.ok(authenticatedUser);
        } else {
            // Authentication failed, provide an appropriate response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed for user: " + username + HttpStatus.UNAUTHORIZED.value());
        }
    }
}
