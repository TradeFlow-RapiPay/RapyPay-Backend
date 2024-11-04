package dev.TradeFlow.RapiPay.Iam.controllers;

import dev.TradeFlow.RapiPay.Iam.controllers.resource.UserRequest;
import dev.TradeFlow.RapiPay.Iam.controllers.response.SignInResponse;
import dev.TradeFlow.RapiPay.Iam.controllers.transform.SignUpFromResourceToEntity;
import dev.TradeFlow.RapiPay.Iam.entities.User;
import dev.TradeFlow.RapiPay.Iam.services.UserService;
import dev.TradeFlow.RapiPay.Iam.tokenconfig.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody UserRequest userRequest) {
        var user = SignUpFromResourceToEntity.transform(userRequest);
        User existingUser = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            return ResponseEntity.badRequest().build();
        }
        User insertedUser = userService.createUser(user).orElse(null);
        if (insertedUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(insertedUser, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody User user) {
        User existingUser = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        String token = jwtUtil.generateToken(user.getUsername());
        SignInResponse response = new SignInResponse(existingUser.getId().toString(), existingUser.getRoles().get(0).getName().toString(), existingUser.getUsername(), token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}