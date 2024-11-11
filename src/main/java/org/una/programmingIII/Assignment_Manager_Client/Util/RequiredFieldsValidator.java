package org.una.programmingIII.Assignment_Manager_Client.Util;

import javafx.scene.Node;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import lombok.Setter;

import java.util.List;

@Setter
public class RequiredFieldsValidator {

    private List<Node> requiredFields;

    public RequiredFieldsValidator(List<Node> requiredFields) {
        this.requiredFields = requiredFields;
    }


    public String validate() {
        StringBuilder invalid = new StringBuilder();
        for (Node node : requiredFields) {
            String floatingText = getFloatingText(node);
            if (floatingText != null) {
                if (!invalid.isEmpty()) {
                    invalid.append(", ");
                }
                invalid.append(floatingText);
            }
        }
        return invalid.isEmpty() ? "" : "The following required fields are empty or have formatting issues: [" + invalid + "].";
    }

    private String getFloatingText(Node node) {
        String floatingText = null;
        if (node instanceof MFXTextField && isEmpty(((MFXTextField) node).getText())) {
            floatingText = ((MFXTextField) node).getFloatingText();
        } else if (node instanceof MFXComboBox && ((MFXComboBox<?>) node).getValue() == null) {
            floatingText = ((MFXComboBox<?>) node).getFloatingText();
        } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
            floatingText = ((MFXDatePicker) node).getFloatingText();
        }
        return floatingText;
    }

    private boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
}