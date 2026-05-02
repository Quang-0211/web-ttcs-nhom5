package com.ttcsn5.webstudyenglish.data;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Role;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.RoleRepo;
import com.ttcsn5.webstudyenglish.service.HashPassword;

@Component
public class DataIn implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private CategoryRepo cateRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private HashPassword hashPassword;

    @Override
    public void run(String... args) throws Exception {

        // ===== CHECK CATEGORY EXIST (TRÁNH INSERT TRÙNG) =====
        if (cateRepo.count() > 0) {
            System.out.println(">> DATA ALREADY EXISTS, SKIP INIT");
            return;
        }

        // ===== ROLE =====
        Role adminRole = roleRepo.findByCode("ADMIN");
        if (adminRole == null) {
            adminRole = roleRepo.save(new Role("ADMIN", "Admin"));
        }

        // ===== USER =====
        User user = accountRepo.findByUsername("admin").get();
        if (user == null) {
            user = new User(
                    "admin",
                    hashPassword.hashPassword("123456"),
                    "admin@gmail.com",
                    adminRole
            );
            accountRepo.save(user);
        }

        // ===== CATEGORY CHA =====
        Category article = cateRepo.save(new Category("Article", LocalDateTime.now(), user));
        Category quiz = cateRepo.save(new Category("Quiz", LocalDateTime.now(), user));
        Category video = cateRepo.save(new Category("Video", LocalDateTime.now(), user));

        // ===== TOPIC LIST =====
        String[] topics = {
                "Daily Communication",
                "Greetings and Introductions",
                "Making Small Talk",
                "Asking for Directions",
                "Ordering Food",
                "Shopping Conversations",
                "Talking on the Phone",
                "Making Appointments",
                "Expressing Opinions",
                "Giving Advice",
                "Inviting Someone",
                "Apologizing",
                "Talking about Work",
                "Talking about Family",
                "Travel English",
                "At the Airport",
                "At the Hotel",
                "At the Restaurant",
                "Job Interviews",
                "Business Meetings"
        };

        // ===== INSERT CHILD =====
        saveChildren(topics, article, user, "");
        saveChildren(topics, quiz, user, " Quiz");
        saveChildren(topics, video, user, " Video");

        System.out.println(">> INIT DATA SUCCESS");
    }

    // ===== HELPER FUNCTION =====
    private void saveChildren(String[] topics, Category parent, User user, String suffix) {
        for (String t : topics) {
            Category c = new Category(t + suffix, LocalDateTime.now(), parent, user);
            cateRepo.save(c);
        }
    }
}