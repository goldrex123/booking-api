package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.auth.CustomMemberDto;
import sky.gurich.booking.dto.auth.LoginResponse;
import sky.gurich.booking.dto.auth.SignUpRequest;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.jwt.JWTUtil;
import sky.gurich.booking.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public LoginResponse login(CustomMemberDto customMemberDto) {
        String token = jwtUtil.generateAccessToken(customMemberDto.getUsername(), null);
        return LoginResponse.toDto(customMemberDto,token);
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
}
