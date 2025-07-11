package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest loginRequest) {
        //인증 처리 -> loadUserByUsername 호출
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        //인증된 객체
        CustomMemberDto customMemberDto = ((CustomUserDetails) auth.getPrincipal()).getCustomMemberDto();
        LoginResponse response = authService.login(customMemberDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 완료"));
    }
}
