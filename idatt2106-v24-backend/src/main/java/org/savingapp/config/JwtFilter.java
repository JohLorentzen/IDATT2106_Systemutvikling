package org.savingapp.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.savingapp.service.JwtServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Filter that handles JWT authentication.
 * Extends OncePerRequestFilter to ensure that it is executed once per request.
 */
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtServiceImpl;
    private final UserDetailsService userDetailsService;


    /**
     * Overridden from OncePerRequestFilter class.
     * Handles the JWT authentication logic.
     *
     * @param request     HttpServletRequest instance representing the HTTP request.
     * @param response    HttpServletResponse instance representing the HTTP response.
     * @param filterChain FilterChain instance used to invoke the next filter in the chain.
     * @throws ServletException if the request could not be handled.
     * @throws IOException      if an input or output error is detected when the servlet handles the request.
     */
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws
            ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Check if the Authorization header is valid
        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT from the Authorization header
        jwt = authorizationHeader.substring(7);

        // Check if the JWT is valid
        if (!jwtServiceImpl.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the JWT
        username = jwtServiceImpl.extractUsername(jwt);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Load the user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Set the authentication token for the current user
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
