package com.ttcsn5.webstudyenglish.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.dto.request.UpdateProfileRequest;
import com.ttcsn5.webstudyenglish.dto.request.UserRequest;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.totalenum.LoginResponse;
import com.ttcsn5.webstudyenglish.totalenum.LoginStatus;
import com.ttcsn5.webstudyenglish.totalenum.RegisterStatus;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepo are;
    @Autowired
    private HashPassword hashPassword;

    public List<User> findByCnt(Integer cnt) {
        return are.findAll(PageRequest.of(cnt, 10, Sort.by("id").ascending())).getContent();
    }

    public LoginResponse checkLogin(UserRequest userRequest) {
        User user = this.findByEmail(userRequest.getEmail());
        if (user == null) {
            return new LoginResponse(LoginStatus.INVALID_EMAIL, null);
        } else if (hashPassword.checkPassword(userRequest.getPassword(), user.getPassword()) == false) {
            return new LoginResponse(LoginStatus.INVALID_PASSWORD, null);
        }
        return new LoginResponse(LoginStatus.SUCCESS, user);
    }

    public RegisterStatus checkRegister(String username, String email) {
        if (this.existsByEmail(email)) {
            return RegisterStatus.EMAIL_EXISTS;
        }
        if (this.existsByUsername(username)) {
            return RegisterStatus.USERNAME_EXISTS;
        }
        return RegisterStatus.SUCCESS;
    }

    public void updateStreak(User user) {
        if (user == null)
            return;

        LocalDateTime now = LocalDateTime.now();
        // Lấy ngày cuối cùng user tương tác (đã lưu trong DB)
        LocalDateTime lastUpdate = user.getUpdatedAt();

        if (lastUpdate != null) {
            LocalDate today = now.toLocalDate();
            LocalDate lastDate = lastUpdate.toLocalDate();

            // Tính số ngày chênh lệch
            long daysBetween = ChronoUnit.DAYS.between(lastDate, today);

            if (daysBetween == 1) {
                // Nếu cách đúng 1 ngày (hôm qua học, nay lại học) -> Tăng streak
                user.setStreakCnt(user.getStreakCnt() + 1);
            } else if (daysBetween > 1) {
                // Nếu bỏ lỡ từ 2 ngày trở lên -> Reset về 1
                user.setStreakCnt(1);
            }
            // Nếu daysBetween == 0 (đăng nhập nhiều lần trong ngày) -> Giữ nguyên streak
        } else {
            // Lần đầu tiên có hoạt động
            user.setStreakCnt(1);
        }

        // Cập nhật thời điểm tương tác mới nhất
        user.setUpdatedAt(now);
        are.save(user);
    }

    public List<User> searchUser(String username, String email, int roleId, int cnt) {
        Pageable pageable = PageRequest.of(cnt, 10, Sort.by("id").ascending());

        if (roleId == 0) {
            return are.findByUsernameContainingAndEmailContaining(username, email, pageable)
                    .getContent();
        }

        return are.findByUsernameContainingAndEmailContainingAndRoleId_Id(
                username, email, roleId, pageable).getContent();
    }

    public void updateUserDetail(UpdateProfileRequest request) {
        User user = are.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        // 1. Luôn cập nhật Email
        user.setEmail(request.getEmail());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 2. Nếu người dùng muốn đổi mật khẩu (có nhập mật khẩu cũ)
        if (request.getOldPassword() != null && !request.getOldPassword().isEmpty()) {

            // Kiểm tra mật khẩu cũ có khớp với DB không
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
            }

            // Kiểm tra độ dài mật khẩu mới
            if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
                throw new RuntimeException("Mật khẩu mới phải có ít nhất 6 ký tự!");
            }

            // Mã hóa mật khẩu mới và lưu
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        are.save(user);
    }

    public List<User> searchByUsernameEmailRole(String username, String email, int role) {
        return are.searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndRoleId_Id(username,
                email, role);
    }

    public AccountRepo getAre() {
        return are;
    }

    public void setAre(AccountRepo are) {
        this.are = are;
    }

    public boolean existsByEmail(String email) {
        return are.existsByEmail(email);
    }

    public void saveUser(User user) {
        are.save(user);
    }

    public User findByEmail(String email) {
        return are.findByEmail(email).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return are.existsByUsername(username);
    }

    public List<User> searchByUsernameEmail(String username, String email) {
        return are.searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username,
                email);
    }
}
