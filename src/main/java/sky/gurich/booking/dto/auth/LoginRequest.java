package sky.gurich.booking.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "사용자 아이디는 필수 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;
}
