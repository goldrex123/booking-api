package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveTokenOnRedis(String username, String refreshToken) {
        stringRedisTemplate.opsForValue().set("RT:"+username, refreshToken, Duration.ofDays(1));
//        stringRedisTemplate.opsForValue().set("RT:"+username, refreshToken, Duration.ofSeconds(20));
    }

    public String getToken(String username) {
        return stringRedisTemplate.opsForValue().get("RT:" + username);
    }

    public void deleteToken(String username) {
        stringRedisTemplate.delete("RT:" + username);
    }
}
