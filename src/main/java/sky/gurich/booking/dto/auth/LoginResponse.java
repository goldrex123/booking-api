package sky.gurich.booking.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.ChurchGroup;

@Getter
@Setter
public class LoginResponse {

    private Long id;
    private String accessToken;
    private String username;
    private String nickname;
    private ChurchGroup churchGroup;

    @Builder
    public LoginResponse(Long id, String accessToken, String username, String nickname, ChurchGroup churchGroup) {
        this.id = id;
        this.accessToken = accessToken;
        this.username = username;
        this.nickname = nickname;
        this.churchGroup = churchGroup;
    }

    public static LoginResponse toDto(CustomMemberDto member, String accessToken) {
        return LoginResponse.builder()
                .id(member.getId())
                .accessToken(accessToken)
                .username(member.getUsername())
                .nickname(member.getNickname())
                .churchGroup(member.getChurchGroup())
                .build();
    }
}
