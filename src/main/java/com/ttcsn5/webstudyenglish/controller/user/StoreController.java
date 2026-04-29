package com.ttcsn5.webstudyenglish.controller.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ttcsn5.webstudyenglish.entity.Plan;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.PlanService;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/store")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StoreController {

    PlanService planService;
    SubscriptionService subscriptionService;

    @GetMapping
    public String store(@RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }

        List<Plan> plans = planService.searchPlansUser(keyword, true, maxPrice);
        model.addAttribute("plans", plans);
        model.addAttribute("userPath", "user/store/index");
        model.addAttribute("activeMenu", "store");

        model.addAttribute("keyword", keyword);
        model.addAttribute("maxPrice", maxPrice);
        return "user/index";
    }

    @PostMapping("/purchase")
    public String purchase(@RequestParam(name = "planId") Integer planId,
            Model model, HttpSession session, RedirectAttributes ra) {
        User user1 = (User) session.getAttribute("user");
        if (user1 == null || !user1.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }

        Set<Subscription> subscriptions = (Set<Subscription>) session.getAttribute("subscription");
        if (subscriptions != null) {
            boolean alreadySubscribed = subscriptions.stream()
                    .anyMatch(sub -> sub.getPlan().getId().equals(planId));

            if (alreadySubscribed) {
                ra.addFlashAttribute("message", "Bạn đang có một gói cước còn hạn sử dụng. Vui lòng quay lại sau!");
                ra.addFlashAttribute("error", true);
                return "redirect:/user/store";
            }
        }
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        Plan plan = planService.getPlanById(planId);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        int randomPart = (int) (Math.random() * 900) + 100; // Tạo 3 số ngẫu nhiên từ 100-999

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(plan.getDurationDays()))
                .transactionId("TX" + timestamp + "U" + user.getId() + "R" + randomPart)
                .paymentStatus("Paid")
                .active(true)
                .build();

        subscriptionService.saveSubscription(subscription);
        ra.addFlashAttribute("message", "Chúc mừng! Bạn đã mua +" + plan.getName() + " thành công.");
        ra.addFlashAttribute("error", false); // Kích hoạt màu xanh

        subscriptions.add(subscription);
        session.setAttribute("subscription", subscriptions);

        return "redirect:/user/store";
    }
}
