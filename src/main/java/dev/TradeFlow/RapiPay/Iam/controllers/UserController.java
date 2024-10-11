package dev.TradeFlow.RapiPay.Iam.controllers;

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
    public ResponseEntity<User> signUp(@RequestBody User user) {
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
    public ResponseEntity<String> signIn(@RequestBody User user) {
        User existingUser = userService.getUserByUsername(user.getUsername()).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}