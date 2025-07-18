package sky.gurich.booking.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.auth.CustomMemberDto;
import sky.gurich.booking.dto.auth.LoginResponse;
import sky.gurich.booking.dto.auth.SignUpRequest;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.exception.ExpiredTokenException;
import sky.gurich.booking.jwt.JWTUtil;
import sky.gurich.booking.repository.MemberRepository;

import java.time.Duration;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(CustomMemberDto customMemberDto, HttpServletResponse response) {
        String username = customMemberDto.getUsername();
        String accessToken = jwtUtil.generateAccessToken(username, null);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        refreshTokenService.saveTokenOnRedis(username, refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());

        return LoginResponse.toDto(customMemberDto,accessToken);
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        Optional<Member> findMember = memberRepository.findByUsername(request.getUsername());
        if (findMember.isPresent()) {
            throw new IllegalStateException("중복되는 아이디가 이미 존재합니다.");
        }

        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        Member member = request.toEntity();
        memberRepository.save(member);
    }

    public String reIssueToken(String refreshToken) {
        // 토큰 검증
        if (refreshToken == null) {
            throw new JwtException("리프레쉬 토큰이 없습니다.");
        } else {
            jwtUtil.validateToken(refreshToken);

            if (jwtUtil.getTokenType(refreshToken).equals("access")) {
                throw new JwtException("새로운 Access 토큰 발급을 위해서는 Refresh 토큰 타입이 필요합니다.");
            }
        }


        String userId = jwtUtil.getUserId(refreshToken);
        String storedRefreshToken = refreshTokenService.getToken(userId);

        if (storedRefreshToken == null) {
            throw new ExpiredTokenException("만료된 Refresh 토큰입니다, 다시 로그인 하세요.");
        }


        if (!storedRefreshToken.equals(refreshToken)) {
            // 토큰 불일치
            throw new IllegalArgumentException("Refresh 토큰이 불일치 합니다.");
        }

        return jwtUtil.generateAccessToken(userId, null);
    }

    public void logout(String refreshToken, HttpServletResponse response) {

        jwtUtil.validateToken(refreshToken);
        String userId = jwtUtil.getUserId(refreshToken);
        refreshTokenService.deleteToken(userId);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
    }
}
