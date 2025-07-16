package pr.generation.authserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubToken {

    @Column(name = "github_access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "github_token_issued_at")
    private Instant issuedAt;

    @Column(name = "github_token_expires_at")
    private Instant expiresAt;
}
