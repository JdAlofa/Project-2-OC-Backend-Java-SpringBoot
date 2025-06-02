package com.rentals.rentalapp.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentals.rentalapp.model.Rental;
import com.rentals.rentalapp.repository.RentalRepository;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;


    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental createRental(String name, Integer surface, BigDecimal price, String description, String pictureUrl,
            Long ownerId) {
        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .picture(pictureUrl)
                .description(description)
                .owner_id(ownerId)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        return rentalRepository.save(rental);
    }

    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    public Optional<Rental> updateRental(Long id, String name, Integer surface, BigDecimal price, String description) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setUpdated_at(LocalDateTime.now());
            return Optional.of(rentalRepository.save(rental));
        }
        return Optional.empty();
    }
}
