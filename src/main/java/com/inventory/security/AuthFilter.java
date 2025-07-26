package com.inventory.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.config.SecurityConfig;
import com.inventory.exception.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final UserInfoService userInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            authenticateRequest(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleErrorResponse(response, e);
        }
    }

    private void handleErrorResponse(HttpServletResponse response, Exception ex) {
        SecurityContextHolder.clearContext();
        SecurityException exception;
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (ex instanceof SecurityException securityException) {
            exception = securityException;
        } else {
            exception = new SecurityException("SEC004", ex.getMessage());
        }
        try {
            logExceptionMessage(ex);
            response.getOutputStream().println(mapper.writeValueAsString(exception));
        } catch (IOException ioException) {
            logger.error("App Security Error - IOException in mapping the json to security exception");
        }
    }

    private void authenticateRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            logger.error("No header for request: " + request.getRequestURI());
            throw new SecurityException("SEC001", "Token is required");
        }
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userEmail = authService.extractUsername(token);

            if (userEmail == null) {
                logger.error("Invalid token: " + token);
                throw new SecurityException("SEC002", "Invalid token");
            }

            authenticateUser(userEmail, request);

        } else {
            logger.error("Invalid token format: " + authHeader);
            throw new SecurityException("SEC003", "Invalid token format");
        }
    }

    private void authenticateUser(String userEmail, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoService.loadUserByUsername(userEmail);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getRequestURI();
//        System.out.println("Incoming path: " + path);
//        AntPathMatcher matcher = new AntPathMatcher();
//        for (String pattern : SecurityConfig.PUBLIC_ENDPOINTS) {
//            System.out.println("Matching " + path + " against " + pattern);
//            if (matcher.match(pattern, path)) {
//                System.out.println("Matched! Skipping filter");
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<RequestMatcher> matchers = new ArrayList<>();
        for (String pattern : SecurityConfig.PUBLIC_ENDPOINTS) {
            matchers.add(new AntPathRequestMatcher(pattern));
        }
        RequestMatcher excludeFromFilterRequestMatcher = new OrRequestMatcher(matchers);
        boolean shouldNotFilter = excludeFromFilterRequestMatcher.matches(request);
        logger.info("Should Not Filter: " + shouldNotFilter);

        return shouldNotFilter;
    }

    private void logExceptionMessage(Exception ex) {
        String errorMessage = "Exception occurred in Security Filter for user: " + getCurrentOrDefaultPrincipalEmail();
        errorMessage = errorMessage + " . Exception Stacktrace: " + getStacktraceAsString(ex);
        logger.error(errorMessage);
    }

    private String getCurrentOrDefaultPrincipalEmail() {
        String userEmail = "UserWithoutSecurity";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails userinfodetails) {
            userEmail = userinfodetails.getEmail();
        }
        return userEmail;
    }

    private String getStacktraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}