package com.odp.main.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.odp.main.Models.Game;
import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.GameRepository;
import com.odp.main.Repositorys.PublisherRepository;
import com.odp.main.Repositorys.UserRepository;
import com.odp.main.SecurityConfig;
import com.odp.main.Services.CustomUserDetailsService;
import com.odp.main.Services.PaymentService;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PublisherRepository publisherRepo;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private CustomUserDetailsService userService;
	@Autowired
	private PaymentService paymentService;

	// Ana sayfayı görüntüleme
	@GetMapping("/")
	public String viewHomePage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String username = auth.getName();
			model.addAttribute("username", username);

			String roleMessage = getUserRoleMessage(auth, username);
			model.addAttribute("welcomeMessage", roleMessage);
		}

		// Onaylanmış oyunları modele ekle
		addApprovedGamesToModel(model);
		return "index";
	}

	// Kimlik doğrulama kontrolü
	private boolean isAuthenticated(Authentication auth) {
		return auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
	}

	// Kullanıcı rol mesajını al
	private String getUserRoleMessage(Authentication auth, String username) {
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return "Hoşgeldiniz Admin " + username;
		} else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PUBLISHER"))) {
			return "Hoşgeldiniz Yayımcı " + username;
		} else {
			return "Hoşgeldiniz Kullanıcı " + username;
		}
	}

	// Onaylanmış oyunları modele ekleme
	private void addApprovedGamesToModel(Model model) {
		List<Game> approvedGames = gameRepository.findByStatus("Approved");
		model.addAttribute("approvedGames", approvedGames);
	}

	// Kayıt formunu görüntüleme
	@GetMapping("/loginAndRegister")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "loginAndRegister";
	}

	// Publisher Kayıt formunu görüntüleme
	@GetMapping("/publishersRegister")
	public String showSignUpPBLSForm(Model model) {

		model.addAttribute("publisher", new Publisher());
		return "publishersRegister";
	}

	// Kullanıcı kaydını işleme
	@PostMapping("/process_register")
	public String processRegistration(@ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "loginAndRegister";
		}
		user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
		userRepo.save(user);
		return "register_success"; // Kayıttan sonra başarı sayfasına yönlendir
	}

	// Yayımcı kaydını işleme
	@PostMapping("/process_publisher_register")
	public String processPublisherRegistration(@ModelAttribute Publisher publisher) {
	    publisher.setPassword(securityConfig.passwordEncoder().encode(publisher.getPassword()));
	    publisherRepo.save(publisher);
	    return "register_success";
	}


	// Ödeme sayfasını görüntüleme
	@GetMapping("/pay/{gameId}")
	public String payForGame(@PathVariable Long gameId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// Kullanıcı giriş yapmamışsa
		if (!isAuthenticated(auth)) {
			return "redirect:/loginAndRegister";
		}

		// Kullanıcı giriş yapmışsa
		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		Game game = gameRepository.findById(gameId).orElse(null);

		if (user != null && game != null) {
			model.addAttribute("user", user);
			model.addAttribute("game", game);
			return "payment";
		} else {
			model.addAttribute("message", "Ödeme işlemi başarısız!");
			return "payment_result";
		}
	}

	// Ödeme işlemini işleme
	@PostMapping("/process_payment")
	public String processPayment(@RequestParam Long userId, @RequestParam Long gameId, @RequestParam String cardNumber,
			@RequestParam String expiryDate, @RequestParam String cvv, Model model) {
		User user = userRepo.findById(userId).orElse(null);
		Game game = gameRepository.findById(gameId).orElse(null);

		if (user != null && game != null) {
			// Ödeme işlemi simülasyonu
			boolean paymentSuccessful = paymentService.processCompletePayment(user, game, cardNumber, expiryDate, cvv);

			if (paymentSuccessful) {
				// Oyunu kullanıcının kütüphanesine ekleme
				user.getLibrary().add(game);
				userRepo.save(user);

				model.addAttribute("message", "Ödeme işlemi başarılı! Oyun kütüphanenize eklendi.");
				return "payment_result";
			} else {
				model.addAttribute("message", "Ödeme işlemi başarısız!");
				return "payment_result";
			}
		} else {
			model.addAttribute("message", "Ödeme işlemi başarısız!");
			return "payment_result";
		}
	}

	// Dosya indirme işlevi
	@GetMapping("/download/{gameId}")
	public ResponseEntity<InputStreamResource> downloadGameFile(@PathVariable Long gameId)
			throws FileNotFoundException {
		Game game = gameRepository.findById(gameId).orElseThrow(() -> new FileNotFoundException("Game not found"));
		File file = new File(game.getGameFilePath());
		if (!file.exists()) {
			throw new FileNotFoundException("File not found");
		}

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}
}
