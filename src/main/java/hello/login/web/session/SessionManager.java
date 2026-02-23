package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    //세션 생성
    //클라이언트 로그인 -> 해당 정보 DB에서 가져옴 -> 세션에 저장
    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();

        sessionStore.put(sessionId, value);

        Cookie sessionCookie = new Cookie("sessionId", sessionId);
        response.addCookie(sessionCookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie findCookie = findCookie(request);

        if (findCookie == null)
            return null;

        return sessionStore.get(findCookie.getValue());
    }

    public void expire(HttpServletRequest request) {
        Cookie findCookie = findCookie(request);

        if (findCookie != null)
            sessionStore.remove(findCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getValue().equals("sessionId"))
                .findAny()
                .orElse(null);
    }
}
