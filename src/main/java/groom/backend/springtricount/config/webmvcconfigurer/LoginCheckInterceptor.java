package groom.backend.springtricount.config.webmvcconfigurer;

import groom.backend.springtricount.member.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null &&
                session.getAttribute("loginMember") != null &&
                session.getAttribute("loginMember") instanceof MemberDto) {
            return true;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
}
