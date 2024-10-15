package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CourseViewController extends Controller implements Initializable {

    @FXML
    private Accordion acDates;
    @FXML
    private ScrollPane scrollPane;
    boolean havePermission = true;

    @Override
    public void initialize() {
        LocalDate startDate = LocalDate.of(2024, 7, 25);
        LocalDate endDate = LocalDate.of(2024, 11, 27);
        acDates.prefHeightProperty().bind(scrollPane.heightProperty().subtract(5));
        acDates.prefWidthProperty().bind(scrollPane.widthProperty().subtract(15));
        addWeeklyTitledPanes(startDate, endDate);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void addWeeklyTitledPanes(LocalDate startDate, LocalDate endDate) {
        LocalDate currentStartDate = startDate;
        createWeeklyTitledPanes("General");
        while (!currentStartDate.isAfter(endDate)) {
            LocalDate weekEndDate = currentStartDate.plusDays(6);
            if (weekEndDate.isAfter(endDate)) {
                weekEndDate = endDate;
            }

            createWeeklyTitledPanes(convertDateInMonth(currentStartDate, weekEndDate));


            currentStartDate = currentStartDate.plus(1, ChronoUnit.WEEKS);
        }
    }

    private void createWeeklyTitledPanes(String titledData) {
        TitledPane weekPane = new TitledPane();
        VBox.setVgrow(weekPane, Priority.ALWAYS);
        weekPane.getStyleClass().add("titledPane");

        Label weekDate = new Label(titledData);
        weekDate.getStyleClass().add("label2");

        HBox header = new HBox(weekDate);
        header.getStyleClass().add("hBox-TitledPane");
        createButtonToAddFile(header);
        weekPane.setGraphic(header);
        createVboxContentTitled(weekPane);
        acDates.getPanes().add(weekPane);
    }


    private void createVboxContentTitled(TitledPane weekPane) {
        List<Label> labels = List.of(
                new Label("Archivo: ListaOrdenada.docx"),
                new Label("Actividad: Práctica Evaluada.docx")
        );
        VBox content = new VBox();
        VBox.setVgrow(content, Priority.ALWAYS);
        content.setMaxHeight(Double.MAX_VALUE);
        content.getStyleClass().add("vbox-Background-TitledPane");

        for (Label label : labels) {
            HBox contentData = new HBox();
            createLabelToContent(contentData, label.getText(), this::goToFile);
            contentData.getStyleClass().add("hBox-TitledPane");
            createDeleteButton(contentData);
            content.getChildren().add(contentData);
        }

        weekPane.setContent(content);
    }

private void addFile(ActionEvent event) {
    HBox parent = (HBox) ((Button) event.getSource()).getParent();
    TitledPane titledPane = (TitledPane) parent.getParent().getParent();
    VBox content = (VBox) titledPane.getContent();
    HBox contentData = new HBox();
    createLabelToContent(contentData, "Archivo: NuevoArchivo.docx", this::goToFile);
    contentData.getStyleClass().add("hBox-TitledPane");
    createDeleteButton(contentData);
    content.getChildren().add(contentData);

    // Obtener la segunda fecha del rango del TitledPane
String titledData = ((Label) ((HBox) titledPane.getGraphic()).getChildren().get(0)).getText();
String[] dates = titledData.split(" - ");
if (dates.length == 2) {
    LocalDate secondDate = convertStringToDate(dates[0]);
    System.out.println("Segunda fecha en el rango: " + secondDate);
}
}

    private void goToFile(MouseEvent event) {
        System.out.println("Go to file");
    }

    private void deleteFile(ActionEvent event) {
        Button button = (Button) event.getSource();
        HBox parent = (HBox) button.getParent();
        Label label = (Label) parent.getChildren().get(0); // Asumiendo que el Label es el primer hijo
        String labelText = label.getText();
        System.out.println(labelText);
        // Lógica para eliminar de la base de datos usando labelText
        //    deleteFromDatabase(labelText);
        parent.getChildren().clear();
    }

    private void createLabelToContent(HBox header, String text, EventHandler<MouseEvent> action) {
        Label label = new Label(text);
        label.setOnMouseClicked(action);
        label.getStyleClass().add("label");
        header.getChildren().add(label);
    }

    private void createButtonToAddFile(HBox header) {
        addButtonToHeader(header, "btn-AddFile", this::addFile);
    }

    private void createDeleteButton(HBox header) {
        addButtonToHeader(header, "btn-DeleteFile", this::deleteFile);
    }

    private void addButtonToHeader(HBox header, String styleClass, EventHandler<ActionEvent> action) {
        if (havePermission) {
            Button button = new Button();
            button.getStyleClass().add(styleClass);
            button.setOnAction(action);
            header.getChildren().add(button);
        }
    }
    private String convertDateInMonth(LocalDate startDate, LocalDate endDate) {
        return startDate.getDayOfMonth() + " of " + startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " - " +
                endDate.getDayOfMonth() + " of " + endDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
private LocalDate convertStringToDate(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'of' MMMM yyyy", Locale.ENGLISH);
    return LocalDate.parse(dateString + " " + LocalDate.now().getYear(), formatter);
}
}