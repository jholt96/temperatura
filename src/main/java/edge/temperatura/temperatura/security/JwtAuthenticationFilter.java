/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Creates an authentication Filter for JWT auth.
Checks if the JWT token is valid and if it is then moves to the next filter. If it is not valid then blocks the request. 

*/
package edge.temperatura.temperatura.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
  
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
  
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                                                                throws ServletException, IOException {
        //Try to authenticate the JWT token
        try {
            String jwt = parseJwt(request);

            //if the token has been passed then try to validate it. 
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
        
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {

            logger.error("Cannot authentication: {}", e);
        }
  
        filterChain.doFilter(request, response);
    }
    //get the JWT token out of the header so that it can be validated
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
    
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
    
        return null;
    }
}