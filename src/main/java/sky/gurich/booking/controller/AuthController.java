package sky.gurich.booking.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.auth.*;
import sky.gurich.booking.jwt.JWTUtil;
import sky.gurich.booking.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response,
                                   @RequestBody @Validated LoginRequest loginRequest) {
        //인증 처리 -> loadUserByUsername 호출
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        //인증된 객체 추출
        CustomMemberDto customMemberDto = ((CustomUserDetails) auth.getPrincipal()).getCustomMemberDto();
        LoginResponse apiResponse = authService.login(customMemberDto, response);

        return ResponseEntity.ok(ApiResponse.success(apiResponse));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpRequest request) {
        System.out.println("AuthController.signUp");
        authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 완료"));
    }

    @PostMapping("/token")
    public ResponseEntity<?> reIssueToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        String accessToken = authService.reIssueToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.success(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                    HttpServletResponse response) {
        authService.logout(refreshToken, response);
        return ResponseEntity.ok(ApiResponse.success("로그아웃 되었습니다."));
    }
}
