package com.rentals.rentalapp.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentals.rentalapp.model.Message;
import com.rentals.rentalapp.model.User;
import com.rentals.rentalapp.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(User user, Long rentalId, String messageContent) {
        if (rentalId == null || messageContent == null || messageContent.trim().isEmpty()) {
            return null;
        }
        Message message = Message.builder()
                .user_id(user.getId())
                .rental_id(rentalId)
                .message(messageContent)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }
}
