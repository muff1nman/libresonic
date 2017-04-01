package org.libresonic.player.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationProvider.class);

    private final JWTVerifier verifier;

    public JWTAuthenticationProvider(String jwtSignAndVerifyKey) {
        Algorithm algorithm = JWTSecurityUtil.getAlgorithm(jwtSignAndVerifyKey);
        this.verifier = JWT.require(algorithm).build();
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        JWTAuthenticationToken authentication = (JWTAuthenticationToken) auth;
        if(authentication.getCredentials() == null || !(authentication.getCredentials() instanceof String)) {
            logger.error("Credentials not present");
            return null;
        }
        String token = (String) authentication.getCredentials();
        Claim path = verifier.verify(token).getClaim(JWTSecurityUtil.CLAIM_PATH);
        authentication.setAuthenticated(true);

        // TODO:AD This is super unfortunate, but not sure there is a better way when using JSP
        if(StringUtils.contains(authentication.getRequestedPath(), "/WEB-INF/jsp/")) {
            logger.warn("BYPASSING AUTH FOR WEB-INF page");
        } else

        if(!roughlyEqual(path.asString(), authentication.getRequestedPath())) {
            throw new InsufficientAuthenticationException("Credentials not valid for path " + authentication
                    .getRequestedPath() + ". They are valid for " + path.asString());
        }

//        JWT decode = JWT.decode(verify.getToken());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("IS_AUTHENTICATED_FULLY"));
        authorities.add(new SimpleGrantedAuthority("ROLE_TEMP"));
        return new JWTAuthenticationToken(authorities, token, authentication.getRequestedPath());

//        Jws<Claims> claimsJws = Jwts.parser()
//                .setSigningKey(jwtSignAndVerifyKey)
//                .parseClaimsJws(token);
//        claimsJws.getSignature().
//                .getBody()
//                .getSubject();
//
//
//        return authentication;
    }

    private static boolean roughlyEqual(String expectedRaw, String requestedPathRaw) {
        logger.debug("Comparing expected [{}] vs requested [{}]", expectedRaw, requestedPathRaw);
        if(StringUtils.isEmpty(expectedRaw)) {
            logger.debug("False: empty expected");
            return false;
        }
        try {
            UriComponents expected = UriComponentsBuilder.fromUriString(expectedRaw).build();
            UriComponents requested = UriComponentsBuilder.fromUriString(requestedPathRaw).build();

            if(!Objects.equals(expected.getPath(), requested.getPath())) {
                logger.debug("False: expected path [{}] does not match requested path [{}]",
                        expected.getPath(), requested.getPath());
                return false;
            }

            MapDifference<String, List<String>> difference = Maps.difference(expected.getQueryParams(),
                    requested.getQueryParams());

            if(difference.entriesDiffering().size() != 0 ||
                    difference.entriesOnlyOnLeft().size() != 0 ||
                    difference.entriesOnlyOnRight().size() != 1 ||
                    difference.entriesOnlyOnRight().get(JWTSecurityUtil.JWT_PARAM_NAME) == null) {
                logger.debug("False: expected query params [{}] do not match requested query params [{}]", expected.getQueryParams(), requested.getQueryParams());
                return false;
            }

        } catch(Exception e) {
            logger.warn("Exception encountered while comparing paths", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
