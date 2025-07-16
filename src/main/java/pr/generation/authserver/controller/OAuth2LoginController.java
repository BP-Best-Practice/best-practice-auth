package pr.generation.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class OAuth2LoginController {
    @GetMapping("/login-success")
    public String getLoginInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        System.out.println("GitHub 로그인 사용자 정보: " + oauth2User.getAttributes());
        return "redirect:/";
    }
}
