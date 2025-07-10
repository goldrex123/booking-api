package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.gurich.booking.dto.auth.CustomMemberDto;
import sky.gurich.booking.dto.auth.CustomUserDetails;
import sky.gurich.booking.entity.Member;
import sky.gurich.booking.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService.loadUserByUsername");
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent()) {
            return new CustomUserDetails(CustomMemberDto.toDto(member.get()));
        }

        return null;
    }
}
