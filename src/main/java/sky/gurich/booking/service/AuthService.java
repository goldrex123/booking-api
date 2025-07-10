package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.auth.SignUpRequest;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
