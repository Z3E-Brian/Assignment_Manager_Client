package org.una.programmingIII.Assignment_Manager_Client.Util;

import lombok.Data;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Interfaces.SessionObserver;
import org.una.programmingIII.Assignment_Manager_Client.Service.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Data
public class SessionManager {

    private static SessionManager instance;
    private LoginResponse loginResponse;
    private List<SessionObserver> observers = new ArrayList<>();
    private AuthenticationService authenticationService = new AuthenticationService();


    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addObserver(SessionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(SessionObserver observer) {
        observers.remove(observer);
    }

    public void validateTokens() throws Exception {
        if (loginResponse != null && loginResponse.getAccessToken() != null) {
            if (!(isValidToken(loginResponse.getRefreshToken()))) {
                notifySessionExpired();
            } else if (!(isValidToken(loginResponse.getAccessToken()))) {
                loginResponse.setRefreshToken(getRefreshToken());
            }
        }
    }

    private boolean isValidToken(String token) throws Exception {
        return authenticationService.validateToken(token);
    }

    private String getRefreshToken() {
        return "";
    }

    private void notifySessionExpired() {
        for (SessionObserver observer : observers) {
            observer.onSessionExpired();
        }
    }
}
