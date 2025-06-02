package com.rentals.rentalapp.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentals.rentalapp.controllers.dto.MessageRequest;
import com.rentals.rentalapp.model.Message;
import com.rentals.rentalapp.model.User;
import com.rentals.rentalapp.services.MessageService;
import com.rentals.rentalapp.services.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }

        Optional<User> optionalUser = userService.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }
        User user = optionalUser.get();

        if (messageRequest.getRental_id() == null || messageRequest.getMessage() == null || messageRequest.getMessage().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyMap());
        }

        Message newMessage = messageService.createMessage(user, messageRequest.getRental_id(), messageRequest.getMessage());

        if (newMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyMap());
        }

        return ResponseEntity.ok(Map.of("message", "Message sent with success"));
    }
}
