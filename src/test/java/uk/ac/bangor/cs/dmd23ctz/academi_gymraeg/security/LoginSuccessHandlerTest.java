package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class LoginSuccessHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    private LoginSuccessHandler loginSuccessHandler;

    @BeforeEach
    void setUp() {
        loginSuccessHandler = new LoginSuccessHandler();
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectAdminToAdminDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_ADMIN";
        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/admin/dashboard");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectStudentToStudentDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_STUDENT";
        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/student/dashboard");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectLecturerToLecturerDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_LECTURER";
        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/lecturer/dashboard");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectToHomeWhenRoleDoesNotMatch() throws Exception {
        GrantedAuthority authority = () -> "ROLE_USER";
        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectUsingFirstMatchingRole() throws Exception {
        GrantedAuthority firstAuthority = () -> "ROLE_LECTURER";
        GrantedAuthority secondAuthority = () -> "ROLE_ADMIN";
        doReturn(List.of(firstAuthority, secondAuthority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/lecturer/dashboard");
    }
}