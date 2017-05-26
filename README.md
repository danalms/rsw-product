# rsw-product

Sample of a PostgreSQL-backed microservice 

Key concepts being demonstrated are:

1. Swagger API presentation and functional testing, using the springfox Swagger implementation and its annotations
   *The Swagger API forms are accessible by hitting the Gateway's composite at localhost:8080/swagger-ui.html,
   or the same URI on this service once you have authenticated.*
2. OAuth2 security as a Resource Server and SSO recipient
   1. As an OAuth2 Resource Server, there are three separate Spring profiles that invoke different configurations
      to demonstrate the following variations:
      1. profile = *github* :: User details are obtained from Github.com, where Github.com provides the Authorization 
         Server role.  (Gateway SSO initiates the token acquisition)  These are OAuth2 tokens.
      2. profile = *rossoauth2* :: User details are obtained from the auth server repo's Auth Server 
         implementation, set for OAuth2 tokens
         See [rsw-auth](https://github.com/danalms/rsw-auth/blob/master/README.md) and 
         [rsw-gateway](https://github.com/danalms/rsw-gateway/blob/master/README.md) for details, as this 
         setting depends on the Auth Server's and Gateway's property settings for oauth2
      3. profile = *rossjwt* :: User details are provided in the JWT as the token. 
         See [rsw-auth](https://github.com/danalms/rsw-auth/blob/master/README.md) and 
         [rsw-gateway](https://github.com/danalms/rsw-gateway/blob/master/README.md) for details, as this 
         setting depends on the Auth Server's and Gateway's property settings for jwt

A minor customization of the AuthoritiesExtractor is provided in ExternalOAuthConfig as an example of how to 
manipulate/override the default authorities being extracted by Spring for the userInfoUri endpoint. 
Of course, for JWT authorities, a completely different technique is require to override authorities. 
See the Gateway configuration for an example of a JWT token enhancer.



