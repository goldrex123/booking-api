package sky.gurich.booking.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.common.ApiResponseCode;
import sky.gurich.booking.dto.auth.CustomMemberDto;
import sky.gurich.booking.dto.auth.CustomUserDetails;
import sky.gurich.booking.entity.ChurchGroup;
import sky.gurich.booking.entity.MemberRole;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);

            if (token != null) {
                jwtUtil.validateToken(token);
                if (jwtUtil.getTokenType(token).equals("refresh")) {
                    throw new JwtException("Refresh 토큰은 접근 권한이 없습니다.");
                }

                String userId = jwtUtil.getUserId(token);
                CustomUserDetails customUserDetails = new CustomUserDetails(CustomMemberDto.builder()
                        .username(userId)
                        .password("")
                        .nickname("")
                        .churchGroup(ChurchGroup.FATHER)
                        .memberRole(MemberRole.ROLE_USER)
                        .build());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                throw new JwtException("토큰값이 없습니다.");
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            String apiResponse = new ObjectMapper().writeValueAsString(ApiResponse.fail(ApiResponseCode.AUTHENTICATION_FAIL, e.getMessage()));
            response.getWriter().write(apiResponse);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth");
    }
}