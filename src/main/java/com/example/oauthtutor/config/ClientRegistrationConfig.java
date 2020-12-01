package com.example.oauthtutor.config;

import java.util.Map;

public class ClientRegistrationConfig {
    private Map<String, Registration> registration;

    public Map<String, Registration> getRegistration() {
        return registration;
    }

    public void setRegistration(Map<String, Registration> registration) {
        this.registration = registration;
    }

    public static class Registration {
        private String clientId;
        private String clientSecret;
        private String scope;
        private String redirectUri;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }
    }
}
