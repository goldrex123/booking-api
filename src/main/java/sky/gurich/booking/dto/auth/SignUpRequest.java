package sky.gurich.booking.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.ChurchGroup;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.entity.MemberRole;

@Getter @Setter
public class SignUpRequest {
    @NotBlank(message = "사용자 아이디는 필수 값입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;
    @NotNull(message = "사용자 닉네임은 필수 값입니다.")
    private String nickname;
    @NotNull(message = "소속 회 정보는 필수 값입니다.")
    private ChurchGroup churchGroup;

    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .churchGroup(this.churchGroup)
                .memberRole(MemberRole.ROLE_USER)
                .build();
    }
}
