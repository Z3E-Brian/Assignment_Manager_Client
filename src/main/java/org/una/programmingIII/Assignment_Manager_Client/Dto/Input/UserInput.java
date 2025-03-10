package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;


public class UserInput implements Serializable {
   private static final long serialVersionUID = 1L;
    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty lastName;
    public SimpleStringProperty secondLastName;
    public SimpleStringProperty email;
    public SimpleStringProperty identificationNumber;
    public SimpleStringProperty password;
    public SimpleStringProperty careerId;
    public SimpleBooleanProperty isActive;

     @Setter
     @Getter
     public List<PermissionType> role;

    public UserInput() {
        this.id = new SimpleStringProperty("");
        this.careerId = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.secondLastName = new SimpleStringProperty("");
        this.identificationNumber = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.isActive = new SimpleBooleanProperty(false);
        this.password = new SimpleStringProperty("");
        this.role = new ArrayList<>();
    }

    public Long getId() {
        if (this.id.get() != null && !this.id.get().isBlank()) {
            return Long.valueOf(this.id.get());
        }
        return null;
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public Long getCareerId() {
        if (this.careerId.get() != null && !this.careerId.get().isBlank()) {
            return Long.valueOf(this.careerId.get());
        }
        return null;
    }

    public void setCareerId(Long id) {
        this.careerId.set(id.toString());
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getSecondLastName() {
        return secondLastName.get();
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName.set(secondLastName);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String nombre) {
        this.name.set(nombre);
    }


    public String getIdentificationNumber() {
        return identificationNumber.get();
    }

    public void setIdentificationNumber(String cedula) {
        this.identificationNumber.set(cedula);
    }


    public String getEmail() {
        return email.get();
    }

    public void setEmail(String correo) {
        this.email.set(correo);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String correo) {
        this.email.set(correo);
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public void setIsActive(boolean administrador) {
        this.isActive.set(administrador);
    }


}
