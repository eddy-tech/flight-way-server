package org.intech.reservation.roots;

public interface AuthenticationEndPoint {
    String AUTHENTICATION_ENDPOINT = "/auth";
    String REGISTER_ENDPOINT = AUTHENTICATION_ENDPOINT + "/register";
    String AUTHENTICATE_ENDPOINT = AUTHENTICATION_ENDPOINT + "/authenticate";
    String REFRESH_TOKEN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/refresh-token";
    String LOGOUT_ENDPOINT = AUTHENTICATION_ENDPOINT + "/logout";
}
