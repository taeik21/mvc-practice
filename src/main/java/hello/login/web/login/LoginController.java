package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request,
            @RequestParam(defaultValue = "/") String redirectURL){
        if (bindingResult.hasErrors())
            return "login/loginForm";

        Member member = loginService.login(form.getLoginId(), form.getPassword());

        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
//      쿠키 사용
//        Cookie idCookie = new Cookie("memberId", String.valueOf(member.getId()));
//        response.addCookie(idCookie);

//      세션 직접 구현
//        sessionManager.createSession(member, response);

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
//      쿠키 생성
//        Cookie cookie = new Cookie("memberId", null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);

//      세션 직접 구현
//        sessionManager.expire(request);

        HttpSession session = request.getSession(false);

        if (session != null)
            session.invalidate();

        return "redirect:/";
    }
}
