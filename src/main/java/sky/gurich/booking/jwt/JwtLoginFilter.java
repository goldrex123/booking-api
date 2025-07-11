//package sky.gurich.booking.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import sky.gurich.booking.common.ApiResponse;
//import sky.gurich.booking.common.ApiResponseCode;
//import sky.gurich.booking.dto.auth.LoginRequest;
//
//import java.io.IOException;
//
//@Slf4j
//public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    public JwtLoginFilter() {
//        setFilterProcessesUrl("/api/auth/login");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        LoginRequest requestDto = null;
//        try {
//            requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
//        } catch (IOException e) {
//            log.error("Request body parsing error - {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//        return getAuthenticationManager().authenticate(
//                new UsernamePasswordAuthenticationToken(username, password, null)
//        );
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        log.info("Unsuccessful Authentication - {}", failed.getMessage());
//        response.setStatus(401);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//
//        ApiResponse<String> apiResponse = ApiResponse.fail(ApiResponseCode.LOGIN_FAIL, "로그인을 실패했습니다. 아이디 비밀번호를 확인하세요");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
//    }
//}
