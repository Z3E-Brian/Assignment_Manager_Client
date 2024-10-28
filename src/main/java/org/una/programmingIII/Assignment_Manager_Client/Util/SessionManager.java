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
    private boolean isRunningTokenValidationThread;


    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addObserver(SessionObserver observer) {
        observers.add(observer);
        isRunningTokenValidationThread = true;
    }

    public void removeObserver(SessionObserver observer) {
        isRunningTokenValidationThread = false;
        observers.remove(observer);
    }

    private void validateTokens() throws Exception {
        if (loginResponse != null && loginResponse.getAccessToken() != null) {
            if (!(isValidToken(loginResponse.getRefreshToken()))) {
                this.isRunningTokenValidationThread = false;
                notifySessionExpired();
            } else if (!(isValidToken(loginResponse.getAccessToken()))) {// TODO validar inactividad si no refresh token
                System.out.println(loginResponse.getAccessToken());
                loginResponse.setAccessToken(setNewRefreshToken());
                System.out.println(loginResponse.getAccessToken());
            }
        }
    }

    private boolean isValidToken(String token) throws Exception {
        return authenticationService.validateToken(token);
    }

    private String setNewRefreshToken() throws Exception {
        return authenticationService.refreshToken(loginResponse.getAccessToken());
    }

    private void notifySessionExpired() {
        for (SessionObserver observer : observers) {
            observer.onSessionExpired();
        }
    }

    public void startTokenValidationTask() {
        if (isRunningTokenValidationThread) {
            new Thread(() -> {
                while (isRunningTokenValidationThread) {
                    try {
                        Thread.sleep(1000);
                        validateTokens();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
