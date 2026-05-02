package com.ttcsn5.webstudyenglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ttcsn5.webstudyenglish.dto.request.UpdateProfileRequest;
import com.ttcsn5.webstudyenglish.service.AccountService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/user/profile")
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileApiController {

    @Autowired
    private AccountService accService;

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            // principal.getName() lấy username của người đang đăng nhập
            accService.updateUserDetail(request);
            return ResponseEntity.ok(Map.of("message", "Cập nhật thành công!"));
        } catch (RuntimeException e) {
            // Trả về lỗi (ví dụ: sai mật khẩu) để Frontend hiện Toast đỏ
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}