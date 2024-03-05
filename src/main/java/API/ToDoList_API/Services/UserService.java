package API.ToDoList_API.Services;


import API.ToDoList_API.Models.User;
import API.ToDoList_API.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){

        this.userRepository = userRepository;

    }

    public Optional<User> Authenticate(String username, String password){
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder().matches(password, user.getPassword()));
    }

    public User registerUser(String username, String password) throws IllegalArgumentException{
        Validator validator = new Validator();

        if(!validator.registerValidate(username, password))
            throw new IllegalArgumentException("Username or password value are invalid: Minimum length for password: 8 characters" +
                    "Maximum length of values: 255 characters");

        if(!validator.checkIfUsernameExists(username))
            throw new IllegalArgumentException("Username already exists in the database");


        User user = new User(username, passwordEncoder().encode(password));
        userRepository.save(user);
        return user;
    }

    /**
     *
     * @return - Returns instance of BCryptPasswordEncoder in order to gain easier access to decode and encode password.
     */
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final class Validator{
        private final  int MAX_VALUE_LENGTH = 255;
        private final  int MIN_PASSWORD_LENGTH = 8;
        private Validator(){}

        private  boolean checkIfUsernameExists(String username){
            return userRepository.findByUsername(username)
                    .filter(user -> user.getUsername().equals(username))
                    .isEmpty();
        }
        private  boolean isUsernameValid(String username) {
            return username.length() <= MAX_VALUE_LENGTH;
        }

        private  boolean isPasswordValid(String password) {
            return password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_VALUE_LENGTH;
        }
        private  boolean registerValidate(String username, String password){
            if(username == null || password == null) return false;

         return isPasswordValid(password) && isUsernameValid(username);
        }


    }

}
