package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepo; // Kullanıcı repository

    @Autowired
    private PublisherRepository publisherRepo; // Yayıncı repository

    @Autowired
    private SecurityConfig securityConfig; // Security konfigürasyon

    @GetMapping("/")
    public String viewHomePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            model.addAttribute("username", username);
            boolean isPublisher = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_PUBLISHER"));
            if (isPublisher) {
                model.addAttribute("welcomeMessage", "Hoşgeldiniz Yayımcı " + username);
            } else {
                model.addAttribute("welcomeMessage", "Hoşgeldiniz Kullanıcı " + username);
            }
        }
        return "index";
    }

    @GetMapping("/loginAndRegister")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("publisher", new Publisher());
        return "loginAndRegister";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user) {
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        userRepo.save(user);
        return "register_success";
    }

    @PostMapping("/process_publisher_register")
    public String processPublisherRegistration(Publisher publisher) {
        publisher.setPassword(securityConfig.passwordEncoder().encode(publisher.getPassword()));
        publisherRepo.save(publisher);
        return "register_success";
    }
}
