package com.example.oauthtutor.common.security;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

// https://kauth.kakao.com/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code

// curl -v -X POST https://kauth.kakao.com/oauth/token \
// -d 'grant_type=authorization_code' \
// -d 'client_id={REST_API_KEY}' \
// -d 'client_secret={SECRET_KEY}' \
// -d 'redirect_uri={REDIRECT_URI}' \
// -d 'code={AUTHORIZE_CODE}'
    /*
    {
    "token_type":"bearer",
    "access_token":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "expires_in":43199,
    "refresh_token":"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",
    "refresh_token_expires_in":25184000,
    "scope":"account_email profile"
    }
     */
public enum CustomOAuth2Provider {
    KAKAO {
        public ClientRegistration.Builder getBuilder(String registrationId) {
            ClientRegistration.Builder builder = this.getBuilder(registrationId,
                    ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL);
            builder.scope("profile");
            builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
            builder.tokenUri("https://kauth.kakao.com/oauth/token");
            builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
            builder.userNameAttributeName("id");
            builder.clientName("Kakao");
            return builder;
        }
    },
    LOCAL {
        public ClientRegistration.Builder getBuilder(String registrationId) {
            ClientRegistration.Builder builder = this.getBuilder(registrationId,
                    ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
            builder.scope(new String[]{"read", "write"});
            builder.authorizationUri("http://localhost:8080/oauth/authorize");
            builder.tokenUri("http://localhost:8080/oauth/token");
            builder.userInfoUri("http://localhost:8080/oauth2/me");
            builder.userNameAttributeName("name");
            builder.clientName("Local");
            return builder;
        }
    };
    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

    private CustomOAuth2Provider() {
    }

    protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUri(redirectUri);
        return builder;
    }

    public abstract ClientRegistration.Builder getBuilder(String var1);
}
