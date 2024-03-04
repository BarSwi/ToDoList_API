package API.ToDoList_API.Services;


import API.ToDoList_API.Models.User;
import API.ToDoList_API.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> Authenticate(String username, String password){
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }
}
