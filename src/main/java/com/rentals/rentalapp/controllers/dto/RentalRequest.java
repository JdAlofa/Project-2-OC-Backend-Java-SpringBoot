package com.rentals.rentalapp.controllers.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RentalRequest {

    private String name;
    private Integer surface;
    private BigDecimal price;
    private String description;
    private MultipartFile picture;
}
