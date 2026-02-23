package hello.login.web.filter;

import hello.login.web.SessionConst;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class LoginCheckFilter implements Filter {
    private static final String[] whiteList = {"/", "/member/add", "/login", "logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)  servletRequest;
        String requestURL = String.valueOf(request.getRequestURL());

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            if (isLoginCheckPath(requestURL)) {
                HttpSession session = request.getSession();
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    response.sendRedirect("/login?redirectURL=" + requestURL);
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isLoginCheckPath(String requestURL) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURL);
    }
}
