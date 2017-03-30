package org.libresonic.player.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class JWTRequestParameterProcessingFilter extends AbstractAuthenticationProcessingFilter {

//    private AuthenticationManager authenticationManager;
    private final static UrlPathHelper pathHelper = new UrlPathHelper();


    protected JWTRequestParameterProcessingFilter() {
        super(request -> findToken(request).isPresent());
    }
//
//    public JWTRequestParameterProcessingFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            Optional<JWTAuthenticationToken> token = findToken(request);
//            if(token.isPresent()) {
//                Authentication authResult = authenticationManager.authenticate(token.get());
//                SecurityContextHolder.getContext().setAuthentication(authResult);
////                request.setAttribute("__spring_security_scpf_applied", Boolean.TRUE);
//            }
//        }
//        chain.doFilter(request, response);
//
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            Optional<JWTAuthenticationToken> token = findToken(request);
            if(token.isPresent()) {
                return getAuthenticationManager().authenticate(token.get());
            }
            throw new AuthenticationServiceException("Invalid auth method");
    }

    private static Optional<JWTAuthenticationToken> findToken(HttpServletRequest request) {
        String token = request.getParameter(JWTSecurityUtil.JWT_PARAM_NAME);
        if(!StringUtils.isEmpty(token)) {
            return Optional.of(new JWTAuthenticationToken(AuthorityUtils.NO_AUTHORITIES, token, request.getRequestURI() + "?" + request.getQueryString()));
        }
        return Optional.empty();
    }

//    @Override
//    public void destroy() {
//
//    }
}
