package com.odp.main.Controllers;

import com.odp.main.CustomUserDetails;
import com.odp.main.Models.Admin;
import com.odp.main.Models.Game;
import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.GameRepository;
import com.odp.main.Repositorys.PublisherRepository;
import com.odp.main.Repositorys.UserRepository;
import com.odp.main.Services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Controller that handles administrative operations.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private NotificationService notificationService;

    
    @ModelAttribute("balance")
    public BigDecimal getBalance(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Admin admin = customUserDetails.getAdmin();
            if (admin != null) {
                log.debug("Current admin balance: {}", admin.getBalance());
                return admin.getBalance();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Displays a list of games pending approval.
     * @param model the UI model
     * @return the view name
     */
    @GetMapping("/pending_games")
    public String listPendingGames(Model model) {
        List<Game> pendingGames = gameRepository.findByStatus("Pending");
        pendingGames.forEach(game -> log.info("Game Name: {}", game.getTitle()));
        model.addAttribute("games", pendingGames);
        return "Admin/admin_pending_games";
    }

    /**
     * Activates a game.
     * @param gameId the ID of the game to activate
     * @param redirectAttributes for flash attributes
     * @return redirection path
     */
    @PostMapping("/activate_game/{gameId}")
    public String activateGame(@PathVariable Long gameId, RedirectAttributes redirectAttributes) {
        return changeGameStatus(gameId, "Approved", "Oyun aktif edildi!", redirectAttributes);
    }

    /**
     * Deactivates a game.
     * @param gameId the ID of the game to deactivate
     * @param redirectAttributes for flash attributes
     * @return redirection path
     */
    @PostMapping("/deactivate_game/{gameId}")
    public String deactivateGame(@PathVariable Long gameId, RedirectAttributes redirectAttributes) {
        return changeGameStatus(gameId, "Suspended", "Oyun deaktif edildi!", redirectAttributes);
    }

    /**
     * Changes the status of a game.
     * @param gameId the game ID
     * @param status new status
     * @param message message to log
     * @param redirectAttributes for flash attributes
     * @return redirection path
     */
    private String changeGameStatus(Long gameId, String status, String message, RedirectAttributes redirectAttributes) {
        try {
            Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Invalid game Id: " + gameId));
            game.setStatus(status);
            gameRepository.save(game);
            notificationService.sendNotification(game.getPublisherId(), message);
            redirectAttributes.addFlashAttribute("message", "Oyun durumu güncellendi: " + status);
        } catch (Exception e) {
            log.error("Error updating game status", e);
            redirectAttributes.addFlashAttribute("error", "Bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin/games";
    }

    /**
     * Lists all games.
     * @param model the UI model
     * @return the view name
     */
    @GetMapping("/games")
    public String listGamesForApproval(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);
        return "Admin/admin_games";
    }

    /**
     * Lists all users.
     * @param model the UI model
     * @return the view name
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Admin/admin_users";
    }

    /**
     * Lists all publishers.
     * @param model the UI model
     * @return the view name
     */
    @GetMapping("/publishers")
    public String listPublishers(Model model) {
        List<Publisher> publishers = publisherRepository.findAll();
        model.addAttribute("publishers", publishers);
        return "Admin/admin_publishers";
    }

    /**
     * Displays the admin dashboard.
     * @param model the UI model
     * @return the view name
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games != null ? games : Collections.emptyList());
        return "Admin/admin_panel";
    }

    /**
     * Deletes a user.
     * @param id the user ID
     * @param redirectAttributes for flash attributes
     * @return redirection path
     */
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Kullanıcı başarıyla silindi.");
        } catch (Exception e) {
            log.error("Error deleting user, ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Kullanıcı silinemedi.");
        }
        return "redirect:/admin/users";
    }

    /**
     * Deletes a publisher.
     * @param id the publisher ID
     * @param redirectAttributes for flash attributes
     * @return redirection path
     */
    @PostMapping("/deletePublisher/{id}")
    public String deletePublisher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            publisherRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Yayıncı başarıyla silindi.");
        } catch (Exception e) {
            log.error("Error deleting publisher, ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Yayıncı silinemedi.");
        }
        return "redirect:/admin/dashboard";
    }
}
