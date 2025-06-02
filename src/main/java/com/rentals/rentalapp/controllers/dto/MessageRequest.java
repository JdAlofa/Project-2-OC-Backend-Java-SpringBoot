
package com.rentals.rentalapp.controllers.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private Long rental_id;
    private String message;
}
