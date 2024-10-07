package org.una.programmingIII.Assignment_Manager_Client.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class AppContext {

    private static AppContext INSTANCE = null;
    private static HashMap<String, Object> context = new HashMap<>();

    private AppContext() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AppContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContext();
                }
            }
        }
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Object get(String parameter){
        return context.get(parameter);
    }

    public void set(String nombre, Object valor) {
        context.put(nombre, valor);
    }

    public void delete(String parameter) {
        context.put(parameter, null);
    }

}
