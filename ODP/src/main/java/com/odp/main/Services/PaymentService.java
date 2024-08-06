package com.odp.main.Services;

import com.odp.main.Models.Admin;
import com.odp.main.Models.Game;
import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.AdminRepository;
import com.odp.main.Repositorys.PublisherRepository;
import com.odp.main.Repositorys.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Transactional
    public boolean processCompletePayment(User user, Game game, String cardNumber, String expiryDate, String cvv) {
        if (!processPayment(cardNumber, expiryDate, cvv)) {
            return false; // Payment validation failed
        }

        // Add game to user's library
        addUserGame(user, game);

        // Update financial records for admin and publisher
        updateFinancialRecords(game);

        return true;
    }

    private void addUserGame(User user, Game game) {
        user.getLibrary().add(game);
        userRepository.save(user);
    }

    private void updateFinancialRecords(Game game) {
        BigDecimal gamePrice = game.getPrice();
        BigDecimal commission = gamePrice.multiply(BigDecimal.valueOf(0.15));
        BigDecimal publisherEarnings = gamePrice.subtract(commission);

        Admin admin = adminRepository.findById(1L).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setBalance(admin.getBalance().add(commission));
        adminRepository.save(admin);

        Publisher publisher = publisherRepository.findById(game.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisher.setBalance(publisher.getBalance().add(publisherEarnings));
        publisherRepository.save(publisher);
    }

    private boolean processPayment(String cardNumber, String expiryDate, String cvv) {
        // Simple card validation logic
        return cardNumber != null && expiryDate != null && cvv != null 
               && cardNumber.matches("[0-9]{13,16}") && cvv.matches("[0-9]{3}");
    }
}
