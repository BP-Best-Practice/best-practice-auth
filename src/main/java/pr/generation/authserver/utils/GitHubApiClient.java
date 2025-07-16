package pr.generation.authserver.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubApiClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .build();

    public String getVerifiedPrimaryEmail(String accessToken) {
        List<Map> emailList = webClient.get()
                .uri("/user/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (emailList == null || emailList.isEmpty()) return null;

        for (Map<String, Object> emailEntry : emailList) {
            Boolean primary = (Boolean) emailEntry.get("primary");
            Boolean verified = (Boolean) emailEntry.get("verified");
            String email = (String) emailEntry.get("email");

            if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                return email;
            }
        }
        return null;
    }
}
