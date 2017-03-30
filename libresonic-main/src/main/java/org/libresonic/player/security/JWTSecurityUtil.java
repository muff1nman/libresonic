package org.libresonic.player.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

public class JWTSecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTSecurityUtil.class);

    public static final String JWT_PARAM_NAME = "jwt";
    public static final String CLAIM_PATH = "path";
    private static SecureRandom secureRandom = new SecureRandom();

    public static String generateKey() {
        BigInteger randomInt = new BigInteger(130, secureRandom);
        return randomInt.toString(32);
    }

    public static Algorithm getAlgorithm(String jwtKey) {
        try {
            return Algorithm.HMAC256(jwtKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createToken(String jwtKey, String path) {
        UriComponents components = UriComponentsBuilder.fromUriString(path).build();
        String query = components.getQuery();
        String claim = components.getPath() + (!StringUtils.isBlank(query) ? "?" + components.getQuery() : "");
        logger.debug("Creating token with claim " + claim);
        return JWT.create()
                .withClaim(CLAIM_PATH, claim)
                // TODO:AD make this configurable
                .withExpiresAt(DateUtils.addDays(new Date(), 7))
                .sign(getAlgorithm(jwtKey));
    }
}
