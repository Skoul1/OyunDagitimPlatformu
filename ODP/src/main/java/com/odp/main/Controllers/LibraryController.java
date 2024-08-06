package com.odp.main.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.odp.main.Models.Game;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.UserRepository;

@Controller
public class LibraryController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/library")
    public String viewLibrary(Model model) {
        // Kullanıcı kimliğini doğrulama
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/loginAndRegister";
        }

        // Kullanıcıyı bulma
        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        if (user != null) {
            // Kullanıcının oyun kütüphanesini modele ekleme
            List<Game> library = user.getLibrary();
            model.addAttribute("games", library);
            return "library";
        } else {
            model.addAttribute("message", "Kütüphanenizi yüklerken bir hata oluştu.");
            return "error";
        }
    }
}

