package org.una.programmingIII.Assignment_Manager_Client.Interfaces;

import java.util.ArrayList;
import java.util.List;

class TokenManager {
    private List<TokenObserver> observers = new ArrayList<>();
    private String accessToken;
    private String refreshToken;

    public void addObserver(TokenObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TokenObserver observer) {
        observers.remove(observer);
    }

    public void expireAccessToken() {
        this.accessToken = null;
        notifyObservers("ACCESS_TOKEN_EXPIRED");
    }

    public void renewAccessToken() {
        this.accessToken = "nuevoAccessToken";
        notifyObservers("ACCESS_TOKEN_RENEWED");
    }

    public void expireRefreshToken() {
        this.refreshToken = null;
        notifyObservers("REFRESH_TOKEN_EXPIRED");
    }

    private void notifyObservers(String tokenStatus) {
        for (TokenObserver observer : observers) {
            observer.update(tokenStatus);
        }
    }
}
