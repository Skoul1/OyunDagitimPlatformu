package com.odp.main.Services;

import org.springframework.stereotype.Service;

// Bildirim gönderme işlemlerini yöneten servis sınıfı.
@Service
public class NotificationService {

    // Belirtilen yayımcıya bildirim göndermek için kullanılan metot.
    public void sendNotification(Long publisherId, String message) {
        // Bildirim mesajını konsola basar. Gerçek bir projede bu kısım, bir e-posta gönderme,
        // SMS gönderme veya başka bir bildirim kanalı ile değiştirilebilir.
        System.out.println("Bildirim gönderildi: Yayımcı ID: " + publisherId + ", Mesaj: " + message);
    }
}
