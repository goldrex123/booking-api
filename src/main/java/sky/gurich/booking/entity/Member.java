package sky.gurich.booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private ChurchGroup churchGroup;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public Member(String username, String password, String nickname, ChurchGroup churchGroup, MemberRole memberRole) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.churchGroup = churchGroup;
        this.memberRole = memberRole;
    }
}
