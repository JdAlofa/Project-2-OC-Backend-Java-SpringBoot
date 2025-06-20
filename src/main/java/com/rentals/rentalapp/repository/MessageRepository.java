package com.rentals.rentalapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentals.rentalapp.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
