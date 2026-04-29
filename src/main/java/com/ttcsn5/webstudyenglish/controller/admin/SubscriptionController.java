package com.ttcsn5.webstudyenglish.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public String subscription(
            @RequestParam(value = "cnt", required = false, defaultValue = "0") Integer cnt,
            @RequestParam(name = "planName", required = false, defaultValue = "") String planName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("ADMIN")) {
            return "redirect:/login";
        }

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        // Chỉ chuyển đổi khi người dùng có chọn ngày
        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }

        Pageable pageable = PageRequest.of(cnt, 20, Sort.by("id").descending());

        // Gọi Service
        Page<Subscription> subscriptions = subscriptionService.searchSubscription(planName, startDateTime, endDateTime,
                pageable);

        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("startDate", startDate); // Gửi lại để giữ giá trị trên input
        model.addAttribute("endDate", endDate); // Gửi lại để giữ giá trị trên input
        model.addAttribute("planName", planName);
        model.addAttribute("path", "admin/subscription");
        model.addAttribute("cnt", cnt);
        return "admin/adminhome";
    }

}
