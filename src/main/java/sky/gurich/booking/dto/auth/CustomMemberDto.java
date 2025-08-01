package sky.gurich.booking.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.ChurchGroup;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.entity.MemberRole;

@Getter
@Setter
public class CustomMemberDto {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private ChurchGroup churchGroup;
    private MemberRole memberRole;

    @Builder
    public CustomMemberDto(Long id, String username, String password, String nickname, ChurchGroup churchGroup, MemberRole memberRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.churchGroup = churchGroup;
        this.memberRole = memberRole;
    }

    public static CustomMemberDto toDto(Member member) {
        return CustomMemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .churchGroup(member.getChurchGroup())
                .memberRole(member.getMemberRole())
                .build();
    }
}
