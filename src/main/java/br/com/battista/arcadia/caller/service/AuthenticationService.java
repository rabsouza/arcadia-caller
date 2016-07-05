package br.com.battista.arcadia.caller.service;

import static br.com.battista.arcadia.caller.constants.CacheConstant.DURATION_IN_MIN_CACHE;
import static br.com.battista.arcadia.caller.constants.CacheConstant.MAXIMUM_SIZE_CACHE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    private LoadingCache<String, User> cache;

    @PostConstruct
    public void setup() {
        cache = createCache();
    }

    public void validHeader(String profile) throws AuthenticationException {
        HttpStatus status = UNAUTHORIZED;
        if (Strings.isNullOrEmpty(profile)) {
            log.error("Header param 'profile' can not be null! Return code: {} with reason: {}", status.value(), status.getReasonPhrase());

            throw new AuthenticationException("Header param 'profile' can not be null!");
        } else if (ProfileAppConstant.get(profile) == null) {
            log.error("Invalid Profile! Return code: {} with reason: {}", status.value(), status.getReasonPhrase());

            throw new AuthenticationException("Invalid application Profile.");
        }

        log.info("Active profile: {}.", profile);
    }


    public void authetication(String token) throws AuthenticationException {
        if (Strings.isNullOrEmpty(token)) {
            HttpStatus status = UNAUTHORIZED;
            log.error("Header param 'token' can not be null! Return code: {} with reason: {}", status.value(), status.getReasonPhrase());

            throw new AuthenticationException("Header param 'token' can not be null!");
        }

        try {
            User user = cache.get(token);
            if (user == null || user.getPk() == null || user.getVersion() == null) {
                throw new AuthenticationException("Invalid token.");
            }
        } catch (InvalidCacheLoadException e) {
            log.warn("Invalid token. Cause: {}", e.getLocalizedMessage());
            throw new AuthenticationException("Invalid token.", e);
        } catch (ExecutionException e) {
            log.error("Error get the token from cache. Cause: {}", e.getLocalizedMessage(), e);
            throw new AuthenticationException("Error get the token from cache.", e);
        }
    }

    private LoadingCache<String, User> createCache() {
        return CacheBuilder.newBuilder()
                       .maximumSize(MAXIMUM_SIZE_CACHE)
                       .expireAfterAccess(DURATION_IN_MIN_CACHE, TimeUnit.MINUTES)
                       .build(new CacheLoader<String, User>() {
                           @Override
                           public User load(String token) throws Exception {
                               return userRepository.findByToken(token);
                           }
                       });
    }

}
