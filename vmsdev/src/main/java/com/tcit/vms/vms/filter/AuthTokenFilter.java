package com.tcit.vms.vms.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.exception.JwtTokenException;
import com.tcit.vms.vms.jwt.JwtUtils;
import com.tcit.vms.vms.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Arrays;

public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String[] AUTH_WHITELIST = {
            "/api/Auth/Authentication",
            "/api/GetAllCampusList",
            "/api/GetAllVisitorTypeList",
            "/api/GetAllDepartmentList",
            "/api/GetStaffsList",
            "/api/GetStaffsByDepartmentIdList/",
            "/api/GetStaffByStaffIdList/",
            "api/approveOrRejectVisitorByHost",
            "api/approveOrRejectVisitorBySecurity",
            "/api/createVisitDetails",
            "/api/forgot-password",
            "/api/reset-password",
            "/api/GetVisitByVisitId",
            "/api/Auth/test",
            "/api/image/**",
            "/api/image",
            "/swagger-ui",
            "/api/enroll",
            "api/GetAllReasonList",
            "api/GetAllVisitsForTab",
            "/api/AddReason",
            "/api/DeleteReason",
            "/swagger-ui/**"

    };
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private Boolean isValidUrl(String url){
        return Arrays.stream(AUTH_WHITELIST).anyMatch(e->{
            return (url.contains(e) || e.startsWith(url) || url.startsWith(e));
        });
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, JwtTokenException {
        try {
            String url= request.getRequestURI();
            if(url != null && !isValidUrl(url)){
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.validateJwtToken(jwt) && jwtUtils.isTokenExpired(jwt)) {
                    String username = jwtUtils.getEmailFromJwtToken(jwt);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    throw new JwtTokenException("Token not valid");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception occured in AuthTokenFilter{}",e.getMessage());
            ResponseDto errorResponse = new ResponseDto("","","");
            errorResponse.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            response.getWriter().write(convertObjectToJson(errorResponse));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
    }
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYWxhaV9pdDAzN0BnbWFpbC5jb20iLCJleHAiOjE2ODA2OTQ1MTYsImlhdCI6MTY4MDA4OTcxNn0.KDlGI8hbD2k-uDkBfun7X5tHOngInQKskhzEQ7O9oEu9vwMgCoHXHLx01olD1U4yeZPcDR8a3gNuHwk0VhHqjQ";
    }
}
