package groom.backend.springtricount.config.webmvcconfigurer;

import groom.backend.springtricount.annotation.Login;
import groom.backend.springtricount.member.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = parameter.getParameterType().isAssignableFrom(MemberDto.class);
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            return resolveArgumentIfRequestIsNotNull(request);
        }
        return null;
    }

    private MemberDto resolveArgumentIfRequestIsNotNull(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null &&
                session.getAttribute("loginMember") != null &&
                session.getAttribute("loginMember") instanceof MemberDto member) {
            return member;
        }
        return null;
    }
}
