package com.odp.main.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.odp.main.CustomUserDetails;
import com.odp.main.Models.Game;
import com.odp.main.Repositorys.GameRepository;
import com.odp.main.Repositorys.PublisherRepository;

@Controller
@RequestMapping("/publisher")
@PreAuthorize("hasRole('PUBLISHER')")
public class PublisherController {

    @Autowired
    private GameRepository gameRepository;

    @ModelAttribute("balance")
    public BigDecimal getBalance(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getPublisher().getBalance();
        }
        return BigDecimal.ZERO;
    }
    
    // Yayıncı kontrol panelini göster
    @GetMapping("/panel")
    public String publisherPanel() {
        return "Publishers/publisher_panel";
    }

    // Yayıncının kendi oyunlarını listele
    @GetMapping("/games")
    public String listGames(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Game> games = gameRepository.findByPublisherId(userDetails.getPublisher().getId());
        model.addAttribute("games", games);
        return "Publishers/publisher_gameList";
    }

    // Oyun ekleme formunu göster
    @GetMapping("/add_game")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "Publishers/publisher_addGame";
    }

    // Oyun kaydet
    @PostMapping("/save_game")
    public String saveGame(@ModelAttribute("game") Game game,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           @RequestParam("gameFile") MultipartFile gameFile,
                           RedirectAttributes redirectAttributes,
                           Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        game.setPublisherId(userDetails.getPublisher().getId());
        game.setStatus("Pending"); // Oyun başlangıçta bekleme durumunda
        saveGameFiles(game, imageFile, gameFile);
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message", "Oyun başarıyla eklendi ve onay için gönderildi!");
        return "redirect:/publisher/games";
    }

    // Dosya kaydetme işlemi
    private void saveGameFiles(Game game, MultipartFile imageFile, MultipartFile gameFile) throws IOException {
        if (!imageFile.isEmpty()) {
            game.setImagePath(saveFile(imageFile, "images"));
        }
        if (!gameFile.isEmpty()) {
            game.setGameFilePath(saveFile(gameFile, "games"));
        }
    }

    private String saveFile(MultipartFile file, String directory) throws IOException {
        String baseDir = "C:\\GameFiles\\" + directory;
        String originalFilename = file.getOriginalFilename();
        String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        Path uploadPath = Paths.get(baseDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(safeFilename);
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }

    // Oyun detaylarını görüntüle
    @GetMapping("/game/{id}")
    public String viewGameDetails(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalStateException("Oyun bulunamadı."));
        model.addAttribute("game", game);
        return "Publishers/publisher_gameDetails";
    }

    // Oyun güncelle
    @PostMapping("/update_game/{id}")
    public String updateGame(@PathVariable Long id,
                             @ModelAttribute("game") Game updatedGame,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam("gameFile") MultipartFile gameFile,
                             RedirectAttributes redirectAttributes) throws IOException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid game Id:" + id));
        updateGameDetails(game, updatedGame);
        saveGameFiles(game, imageFile, gameFile);
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("message", "Oyun başarıyla güncellendi!");
        return "redirect:/publisher/games";
    }

    private void updateGameDetails(Game game, Game updatedGame) {
        game.setTitle(updatedGame.getTitle());
        game.setDescription(updatedGame.getDescription());
        game.setPrice(updatedGame.getPrice());
        game.setCategory(updatedGame.getCategory());
    }
    // Oyun silme
    @PostMapping("/delete_game/{id}")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gameRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Oyun başarıyla silindi!");
        return "redirect:/publisher/games";
    }
}
