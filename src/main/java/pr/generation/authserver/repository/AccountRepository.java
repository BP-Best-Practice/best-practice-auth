package pr.generation.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pr.generation.authserver.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByGithubId(Long githubId);

    Optional<Account> findByUsername(String username);

    boolean existsByGithubId(Long githubId);
}
