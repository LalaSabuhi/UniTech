package az.unibank.unitech.services;
import az.unibank.unitech.dto.request.RegisterRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.entities.User;
import az.unibank.unitech.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static az.unibank.unitech.enums.ResponseEnum.*;
import static java.lang.String.format;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByPin(String pin) {
        Optional<User> user = userRepository.findUserByUsername(pin);
        return user.orElse(null);
    }

    public boolean usernameExists(String pin) {
        return userRepository.findUserByUsername(pin).isPresent();
    }

    public List<User> getAll() {
        Iterable<User> usersIterator = userRepository.findAll();
        List<User> users = new ArrayList<>();
        usersIterator.forEach(users::add);

        return users;
    }

    public CommonResponse register(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getPin()).isPresent())
            return new CommonResponse(SAME_PIN_ERROR.getCode(), SAME_PIN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());

        User user = new User();

        user.setFullName(request.getName());
        user.setUsername(request.getPin());
        user.setEnabled(true);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return CommonResponse.success();
    }

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(pin)
                .orElseThrow(
                        () -> new UsernameNotFoundException(format("User with pin - %s, not found", pin))
                );
    }
}
