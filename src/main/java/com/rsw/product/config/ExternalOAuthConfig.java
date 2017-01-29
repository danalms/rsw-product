package com.rsw.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;


/**
 * Configuration for a an external OAuth provider (Github in this case)
 *
 * By default, the resource server config used by @EnableResourceServer will obtain the incoming OAuth2 token
 * and apply that to the Spring security context via the OAuth2AuthenticationProcessingFilter.
 * This presumes the Auth Server is producing an OAuth2 token.
 *
 * A custom AuthoritiesExtractor is included here as an example of how to customize the Authorities applied by
 * UserInfoTokenServices.  Each application with @EnableResourceServer would need the same customization to ensure
 * the proper Spring Security context is set (Principal and Authorities).  UserInfoTokenServices performs a REST based
 * lookup of the User details from the auth server, using the configured userInfoUri property.  The REST call itself
 * needs the OAuth2 token provided from the authentication process.  In this example, the authentication is initiated
 * by the Zuul server (gateway) and the token handed down to this service via the @EnableOAuth2Sso configuration.
 * upstream Zuul server.
 *
 */
@Configuration
@Profile(value = "github")
public class ExternalOAuthConfig {

    Logger LOG = LoggerFactory.getLogger(ExternalOAuthConfig.class);


    // Only need this rest template if the authoritiesExtractor needs to make additional call(s) to github
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    /**
     * AuthoritiesExtractor is a Spring-provided functional interface - the framework injects a bean of this type
     * or falls back to its own baked-in impl with a variety of keys it looks for in the response to the call to
     * the URL defined by the "security.oauth2.resource.userInfoUri" property
     * @param template
     * @return
     */
    @Bean
    public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
        return map -> {
            Integer repos = 0;
            if (map.containsKey("public_repos")) {
                try {
                    repos = (Integer) map.get("public_repos");
                } catch (Exception e) {
                    LOG.warn("Github public_repos attribute is not an integer");
                    // ignore
                }
            } else {
                LOG.warn("Github userInfo has no public_repos attribute");
            }

            if (repos >= 2) {
                LOG.info("Github user is an ADMIN!");
                return AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
            } else {
                return AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            // Example of making an additional call to github to examine organizations...
//            String url = (String) map.get("organizations_url");
//            @SuppressWarnings("unchecked")
//            List<Map<String, Object>> orgs = restTemplate.getForObject(url, List.class);
//            if (orgs.stream()
//                    .anyMatch(org -> "some-organization".equals(org.get("login")))) {
//                return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
//            }
//            throw new BadCredentialsException("Not in some-organization");
        };
    }

}
