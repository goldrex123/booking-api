package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.auth.SignUpRequest;
import sky.gurich.booking.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        System.out.println("AuthController.login");
        return null;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 완료"));
    }
}
