package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/publisher")
@PreAuthorize("hasRole('PUBLISHER')")
public class PublisherController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    // Yayıncının panelini görüntüle
    @GetMapping("/panel")
    public String publisherPanel(Model model) {
        return "publisher_panel";
    }

    // Yayıncının oyun listesini görüntüle
    @GetMapping("/games")
    public String listGames(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "list_games";
    }

    // Yeni oyun ekleme formunu görüntüle
    @GetMapping("/add_game")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "add_game_form";
    }

    // Yeni oyun ekleme işlemi
    @PostMapping("/save_game")
    public String saveGame(@ModelAttribute("game") Game game, RedirectAttributes redirectAttributes) {
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message", "Game added successfully!");
        return "redirect:/publisher/games";
    }

    // Oyun detaylarını görüntüle
    @GetMapping("/game/{id}")
    public String viewGameDetails(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            return "redirect:/publisher/games";
        }
        model.addAttribute("game", game);
        return "game_details";
    }

    // Oyun güncelleme formunu görüntüle
    @GetMapping("/edit_game/{id}")
    public String showEditGameForm(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElse(null);
        model.addAttribute("game", game);
        return "edit_game_form";
    }

    // Oyun güncelleme işlemi
    @PostMapping("/update_game/{id}")
    public String updateGame(@PathVariable Long id, @ModelAttribute("game") Game updatedGame, RedirectAttributes redirectAttributes) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid game Id:" + id));
        game.setTitle(updatedGame.getTitle());
        game.setDescription(updatedGame.getDescription());
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message", "Game updated successfully!");
        return "redirect:/publisher/games";
    }

    // Oyun silme işlemi
    @GetMapping("/delete_game/{id}")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game != null) {
            gameRepository.delete(game);
            redirectAttributes.addFlashAttribute("message", "Game deleted successfully!");
        }
        return "redirect:/publisher/games";
    }
}
