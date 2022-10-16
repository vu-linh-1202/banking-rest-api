package com.hcl.bankingrestapi.security.securitys;

import com.hcl.bankingrestapi.security.enums.EnumJwtConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vu Linh
 * Define a filter that executes once per request
 * Check the user's request before it reaches its destination.
 * It will take out the Header Authorization and check the JWT string submitted by the user is valid
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    /**
     * Get JWT from the Authorization header( by removing Bearer prefix)
     * if the request has JWT, validate it, parse username from it
     * from username, get UserDetails to create an Authentication object
     * set the current UserDetails in SecurityContext using setAuthentication(authentication) method
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // get jwt from request
            String token = getToken(request);
            if (StringUtils.hasText(token)) {
                boolean isValid = jwtTokenGenerator.validateToken(token);
                if (isValid) {
                    // get user id from jwt character
                    Long userId = jwtTokenGenerator.findUserIdByToken(token);
                    //get user information from id
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUserId(userId);
                    if (userDetails != null) {
                        // if user != null, set information for Security Context
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed on set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * This method used to get jwt from bearer
     * check if Authorization header contains jwt information
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String fullToken = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(fullToken)) {
            String bearer = EnumJwtConstant.BEARER.getConstant();
            if (fullToken.startsWith(bearer)) {
                token = fullToken.substring(bearer.length());
            }
        }
        return token;
    }
}
