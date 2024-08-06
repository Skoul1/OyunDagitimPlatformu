package com.odp.main.Controllers;

import com.odp.main.Models.Game;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.UserRepository;
import com.odp.main.Services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/game/{id}")
    public String showGameDetails(@PathVariable("id") Long id, Model model) {
        Game game = gameService.getGameById(id);
        model.addAttribute("game", game);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("ownsGame", false);
            return "showGames";
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("ownsGame", false);
            return "showGames";
        }

        boolean ownsGame = gameService.userOwnsGame(user.getUserId(), id);
        model.addAttribute("ownsGame", ownsGame);

        return "showGames";
    }
}
