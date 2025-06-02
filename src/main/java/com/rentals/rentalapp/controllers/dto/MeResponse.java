package com.rentals.rentalapp.controllers.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
