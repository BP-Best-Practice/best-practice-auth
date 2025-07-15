package pr.generation.authserver.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile({"local", "dev"}) // 이 Bean은 local 또는 dev 프로파일에서만 생성됨
public class DotEnvConfig {
    // local 또는 dev 프로파일일 때만 이 Bean이 Spring Container에 등록됨

    // 의존성 주입이 완료된 후에 실행되어야 하는 method에 사용
    @PostConstruct
    public void loadDotenv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing() // 파일이 없어도 예외 발생하지 않음
                    .load();

            dotenv.entries().forEach(entry -> {
                // 이미 시스템 프로퍼티에 있는 경우 덮어쓰지 않음
                if (System.getProperty(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });

            log.info("환경변수 파일 로드 완료: {} 개의 변수", dotenv.entries().size());

        } catch (Exception e) {
            log.warn("환경변수 파일 로드 실패: {}", e.getMessage());
        }
    }
}
