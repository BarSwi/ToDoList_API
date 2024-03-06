package API.ToDoList_API.Controllers;

import API.ToDoList_API.Models.User;
import API.ToDoList_API.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/authentication")
    public ResponseEntity<?> authUser(@RequestBody Map<String, String> request){

        //TODO: Implement JWT Token logic
        String username = request.get("username");
        String password = request.get("password");
        User authenticatedUser = userService.Authenticate(username, password).orElse(null);



        if (authenticatedUser != null) {
            // Authentication successful, return the user or any required information
            return ResponseEntity.ok(authenticatedUser);
        } else {
            // Authentication failed, provide an appropriate response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed for user: " + username);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");
        try{
            User user = userService.registerUser(username, password);
            return ResponseEntity.ok(user.getUsername());
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }


    }
}
