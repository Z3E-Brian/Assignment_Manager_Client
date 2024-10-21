package org.una.programmingIII.Assignment_Manager_Client.Util;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;


public class Format {

    public DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");

    private Format() {
    }

    private static final class InstanceHolder {
        private static final Format INSTANCE = new Format();
    }

    private static void createInstance() {
    }

    public static Format getInstance() {
        if (InstanceHolder.INSTANCE == null) {
            createInstance();
        }
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public TextFormatter twoDecimalFormat() {
        TextFormatter numericFormat = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            if (c.getControlNewText().contains(",")) {
                ParsePosition parsePosition = new ParsePosition(0);
                Object object = decimalFormat.parse(c.getControlNewText(), parsePosition);

                if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                } else {
                    Pattern validDoubleText = Pattern.compile("^[0-9]*+(\\.[0-9]{0,2})?$");
                    if (validDoubleText.matcher(c.getControlNewText().replace(",", "")).matches()) {
                        return c;
                    } else {
                        return null;
                    }
                }
            } else {
                Pattern validDoubleText = Pattern.compile("^[0-9]*+(\\.[0-9]{0,2})?$");
                if (validDoubleText.matcher(c.getControlNewText().replace(",", "")).matches()) {
                    return c;
                } else {
                    return null;
                }
            }
        });
        return numericFormat;
    }

public TextFormatter integerFormat() {
    return new TextFormatter<>(c -> {
        String newText = c.getControlNewText();
        if (newText.isEmpty() || newText.matches("\\d+")) {
            return c;
        }
        return null;
    });
}

    public TextFormatter idFormat(Integer maxLength) {
        TextFormatter<String> cedulaFormat = new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            if (maxLength > 0) {
                if (((TextInputControl) c.getControl()).getLength() >= maxLength && !c.isDeleted()) {
                    return null;
                }
                if (c.getText().length() > maxLength && !c.isDeleted()) {
                    return null;
                }
            }
            c.setText(c.getText().replaceAll("[^a-zA-Z0-9-]", ""));
            if(c.getControlNewText().matches(".*-{2,}.*")){
                return null;
            }
            return c;

        });
        return cedulaFormat;
    }

    public TextFormatter lettersFormat(Integer maxLength) {
        TextFormatter<String> letrasFormat = new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            if (maxLength > 0) {
                if (((TextInputControl) c.getControl()).getLength() >= maxLength && !c.isDeleted()) {
                    return null;
                }
                if (c.getText().length() > maxLength && !c.isDeleted()) {
                    return null;
                }
            }
            //c.setText(c.getText().replaceAll("[^a-zA-Z ]", ""));
            if(c.getControlNewText().matches(".*[^a-zA-Z ].*")){
                return null;
            }
            if(c.getControlNewText().matches(".*\\s{2,}.*")){
                return null;
            }
            return c;

        });
        return letrasFormat;
    }
    public TextFormatter textFormat(Integer maxLength) {
        TextFormatter<String> letrasFormat = new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            if (maxLength > 0 && c.getControlNewText().length() > maxLength) {
                return null;
            }
            return c;
        });
        return letrasFormat;
    }

    public TextFormatter maxLengthFormat(Integer length) {
        TextFormatter maxLengthFormat = new TextFormatter<>(c
                -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            if (((TextInputControl) c.getControl()).getLength() >= length && !c.isDeleted()) {
                return null;
            }
            if (c.getText().length() > length && !c.isDeleted()) {
                return null;
            }
            return c;
        });
        return maxLengthFormat;
    }
    public TextFormatter integerFormat(int max) {
        TextFormatter<Integer> numericFormat = new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }

            int newValue;
            try {
                newValue = Integer.parseInt(change.getControlNewText());
            } catch (NumberFormatException e) {
                return null;
            }

            if (newValue <= max) {
                return change;
            } else {
                return null;
            }
        });

        return numericFormat;
    }

}