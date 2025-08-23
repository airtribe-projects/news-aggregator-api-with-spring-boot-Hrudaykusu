//package com.newsaggregator.demo.config;
//
//import com.newsaggregator.demo.utility.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class AuthJwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//      String autorizationHeader= request.getHeader("authorization");
//      if(autorizationHeader==null){
//          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//          response.getWriter().write("missing authorization header");
//          return;
//      }
//
//      if(!JwtUtil.ValidateJwtToken(autorizationHeader)){
//          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//          response.getWriter().write("invalid jwt token");
//          return;
//      }
//
//      filterChain.doFilter(request, response);
//
//
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getRequestURI();
//        return path.contains("register") || path.contains("signin") || path.contains("h2-console") || path.contains("hello");
//    }
//
//
//
//
//}


package com.newsaggregator.demo.config;

import com.newsaggregator.demo.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class AuthJwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // skip public endpoints
        return path.startsWith("/api/register")
                || path.startsWith("/api/signin")
                || path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // If SecurityContext already has authentication, skip (defensive)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // No token - let it continue and Spring will eventually return 401 for protected endpoints
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            if (!JwtUtil.validateJwtToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            }

            String username = JwtUtil.getUsernameFromToken(token);
            if (username != null) {
                // Create an authenticated Authentication token (no password required here)
                User principal = new User(username, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // token parse/validation issues
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired JWT token");
        }
    }
}

