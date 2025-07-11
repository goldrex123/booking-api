package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.gurich.booking.dto.auth.CustomMemberDto;
import sky.gurich.booking.dto.auth.CustomUserDetails;
import sky.gurich.booking.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .map(value -> new CustomUserDetails(CustomMemberDto.toDto(value)))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

    }
}
