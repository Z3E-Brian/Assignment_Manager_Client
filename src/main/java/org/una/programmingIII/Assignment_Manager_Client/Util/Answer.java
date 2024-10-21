package org.una.programmingIII.Assignment_Manager_Client.Util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
@Data
public class Answer  implements Serializable {

    private Boolean state;
    private String message;
    private String internalMessage;
    private final HashMap<String, Object> result;

    public Answer() {
        this.result = new HashMap<>();
    }

    public Answer(Boolean state, String message, String internalMessage) {
        this.state = state;
        this.message = message;
        this.internalMessage = internalMessage;
        this.result = new HashMap<>();
    }

    public Answer(Boolean state, String message, String internalMessage, String name, Object result) {
        this.state = state;
        this.message = message;
        this.internalMessage = internalMessage;
        this.result = new HashMap<>();
        this.result.put(name, result);
    }

    public Object getResult(String name) {
        return result.get(name);
    }

    public void setResult(String name, Object result) {
        this.result.put(name, result);
    }
}