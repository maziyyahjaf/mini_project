package com.example.maziyyah.light_touch.light_touch.component;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // skip filter for permitted endpoints
        String path = request.getRequestURI();

         // Skip filter for WebSocket handshakes
        if (path.startsWith("/ws")) { 
            filterChain.doFilter(request, response);
            return;
        }

        // for testing
        if (path.equals("/api/emotions") && request.getMethod().equals("GET")) { 
            filterChain.doFilter(request, response);
            return;
        }
        // may need to extend the list etc
        // Skip filter for public API endpoints
        if (path.equals("/api/validate-device") || path.equals("/webhook")) {
            filterChain.doFilter(request, response);
            return;
        }

        // if (path.equals("/api/validate-device")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }
        // if (path.equals("/webhook")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }

         // For all other paths, including /api/hugs/count/**, authentication is required
        logger.info("in authentication filter");
        String authHeader = request.getHeader("Authorization");
        logger.info("auth header in filter {}", authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }
        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();
            logger.info("firebase uid received: {}", firebaseUid);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(firebaseUid, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (FirebaseAuthException e) {
           logger.error("Firebase Authentication Failed", e.getMessage());
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.getWriter().write("Invalid Firebase token");
        }
       

    }

}
