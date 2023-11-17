package pl.jakubtworek.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {
    @Bean
    UserFacade userFacade(
            UserRepository userRepository,
            UserQueryRepository userQueryRepository,
            JwtService jwtService
    ) {
        return new UserFacade(
                userRepository,
                userQueryRepository,
                jwtService
        );
    }

    @Bean
    JwtService jwtService() {
        return new JwtService();
    }
}
