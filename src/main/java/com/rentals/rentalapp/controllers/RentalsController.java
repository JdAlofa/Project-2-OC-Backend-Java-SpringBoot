package com.rentals.rentalapp.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Added @PathVariable
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Added @PutMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentals.rentalapp.model.Rental;
import com.rentals.rentalapp.model.User;
import com.rentals.rentalapp.services.FileStorageService;
import com.rentals.rentalapp.services.RentalService;
import com.rentals.rentalapp.services.UserService;

@RestController
@RequestMapping("/api/rentals")
public class RentalsController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAllRentals(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }

        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(Map.of("rentals", rentals));
    }

    @PostMapping("")
    public ResponseEntity<?> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }

        Optional<User> optionalUser = userService.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }
        User user = optionalUser.get();

        try {
            if (picture == null || picture.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Picture file is required and cannot be empty."));
            }
            String pictureUrl = fileStorageService.storeFile(picture);
            rentalService.createRental(
                    name,
                    surface,
                    price,
                    description,
                    pictureUrl,
                    user.getId());
            return ResponseEntity.ok(Map.of("message", "Rental created !"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Could not store file or create rental: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }

        Optional<Rental> optionalRental = rentalService.getRentalById(id);

        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            return ResponseEntity.ok(rental);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Rental not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }

        Optional<User> optionalUser = userService.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
        }
        try {
            Optional<Rental> updatedRental = rentalService.updateRental(id, name, surface, price, description);
            if (updatedRental.isPresent()) {
                return ResponseEntity.ok(Map.of("message", "Rental updated !"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Rental not found or update failed."));
            }
        } catch (Exception e) {
            // Log the exception e
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating rental: " + e.getMessage()));
        }
    }
}

