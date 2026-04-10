package uk.ac.bangor.cs.dmd23ctz.academi_gymraeg.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom authentication success handler that redirects users to
 * role-specific dashboards after successful login.
 *
 * <p>This implementation avoids hardcoded role checks by using a
 * mapping between roles and their corresponding redirect URLs.</p>
 *
 * <p>If no matching role is found, the user is redirected to the
 * default home page.</p>
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    /** Role constants to avoid hardcoding strings */
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String ROLE_LECTURER = "ROLE_LECTURER";

    /**
     * Mapping of roles to their respective dashboard URLs.
     * This makes the logic scalable and easier to maintain.
     */
    private static final Map<String, String> ROLE_REDIRECT_MAP = Map.of(
        ROLE_ADMIN, "/admin/dashboard",
        ROLE_STUDENT, "/student/dashboard",
        ROLE_LECTURER, "/lecturer/dashboard"
    );

    /**
     * Called automatically when authentication is successful.
     * Redirects the user based on their assigned role.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Get all authorities (roles) assigned to the user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Loop through roles and redirect based on first match found
        for (GrantedAuthority authority : authorities) {

            String role = authority.getAuthority();

            // Check if role exists in the map
            if (ROLE_REDIRECT_MAP.containsKey(role)) {
                response.sendRedirect(ROLE_REDIRECT_MAP.get(role));
                // Stop after first valid redirect
                return; 
            }
        }

        // Default fallback if no role matches
        response.sendRedirect("/");
    }
}