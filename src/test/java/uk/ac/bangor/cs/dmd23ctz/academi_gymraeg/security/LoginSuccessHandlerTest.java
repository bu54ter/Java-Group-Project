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

/**
 * JUnit test class for {@link LoginSuccessHandler}.
 *
 * <p>This class tests the custom login success handler. It checks that users
 * are redirected to the correct dashboard after login based on their assigned
 * Spring Security role.</p>
 */
@ExtendWith(MockitoExtension.class)
class LoginSuccessHandlerTest {

    /**
     * Mock HTTP request passed into the success handler.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mock HTTP response used to verify redirect behaviour.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mock authentication object containing the logged-in user's authorities.
     */
    @Mock
    private Authentication authentication;

    /**
     * Login success handler being tested.
     */
    private LoginSuccessHandler loginSuccessHandler;

    /**
     * Creates a fresh LoginSuccessHandler before each test.
     */
    @BeforeEach
    void setUp() {
        loginSuccessHandler = new LoginSuccessHandler();
    }

    /**
     * Tests that an admin user is redirected to the admin dashboard.
     */
    @Test
    void onAuthenticationSuccess_ShouldRedirectAdminToAdminDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_ADMIN";

        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/admin/dashboard");
    }

    /**
     * Tests that a student user is redirected to the student dashboard.
     */
    @Test
    void onAuthenticationSuccess_ShouldRedirectStudentToStudentDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_STUDENT";

        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/student/dashboard");
    }

    /**
     * Tests that a lecturer user is redirected to the lecturer dashboard.
     */
    @Test
    void onAuthenticationSuccess_ShouldRedirectLecturerToLecturerDashboard() throws Exception {
        GrantedAuthority authority = () -> "ROLE_LECTURER";

        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/lecturer/dashboard");
    }

    /**
     * Tests that a user is redirected to the home page when their role does
     * not match one of the recognised dashboard roles.
     */
    @Test
    void onAuthenticationSuccess_ShouldRedirectToHome_WhenRoleDoesNotMatch() throws Exception {
        GrantedAuthority authority = () -> "ROLE_USER";

        doReturn(List.of(authority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/");
    }

    /**
     * Tests that the first matching role is used when more than one role is
     * present.
     *
     * <p>In this test, ROLE_LECTURER is checked before ROLE_ADMIN, so the
     * lecturer dashboard should be used.</p>
     */
    @Test
    void onAuthenticationSuccess_ShouldRedirectUsingFirstMatchingRole() throws Exception {
        GrantedAuthority firstAuthority = () -> "ROLE_LECTURER";
        GrantedAuthority secondAuthority = () -> "ROLE_ADMIN";

        doReturn(List.of(firstAuthority, secondAuthority)).when(authentication).getAuthorities();

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/lecturer/dashboard");
    }
}