package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {

            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/dashboard");
                return;
            }

            if (authority.getAuthority().equals("ROLE_STUDENT")) {
                response.sendRedirect("/student/dashboard");
                return;
            }

            if (authority.getAuthority().equals("ROLE_LECTURER")) {
                response.sendRedirect("/lecturer/dashboard");
                return;
            }
        }

        response.sendRedirect("/");
    }
}