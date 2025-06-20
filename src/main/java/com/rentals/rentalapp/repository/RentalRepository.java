package com.rentals.rentalapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentals.rentalapp.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
