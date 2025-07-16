package pr.generation.authserver.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;
import pr.generation.authserver.domain.GitHubToken;
import pr.generation.authserver.entity.Account;
import pr.generation.authserver.repository.AccountRepository;
import pr.generation.authserver.utils.GitHubApiClient;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;

    private final GitHubApiClient gitHubApiClient;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        Long githubId = ((Number) attributes.get("id")).longValue();
        String username = (String) attributes.get("login");
        String email = (String) attributes.get("email");  // nullable
        String accessToken = userRequest.getAccessToken().getTokenValue();

        if (email == null || email.isBlank()) {
            email = gitHubApiClient.getVerifiedPrimaryEmail(accessToken);
        }

        GitHubToken token = GitHubToken.builder()
                .accessToken(accessToken)
                .issuedAt(userRequest.getAccessToken().getIssuedAt())
                .build();

        String finalEmail = email;
        Account account = accountRepository.findByGithubId(githubId)
                .map(existing -> {
                    existing.setGithubToken(token);
                    existing.setUsername(username); // 닉네임 변경 대응
                    existing.setEmail(finalEmail);
                    existing.setLastLoginAt(LocalDateTime.now());
                    return existing;
                })
                .orElse(Account.builder()
                        .githubId(githubId)
                        .username(username)
                        .email(email)
                        .githubToken(token)
                        .lastLoginAt(LocalDateTime.now())
                        .build());

        accountRepository.save(account);

        return oAuth2User;
    }
}