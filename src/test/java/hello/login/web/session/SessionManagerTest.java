package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        Member member = new Member();
        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(member, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        Object result = sessionManager.getSession(request);

        Assertions.assertThat(result).isEqualTo(member);

        sessionManager.expire(request);
        Object result2 = sessionManager.getSession(request);
        Assertions.assertThat(result2).isNull();
    }
}