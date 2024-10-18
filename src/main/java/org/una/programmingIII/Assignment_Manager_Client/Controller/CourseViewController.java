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
import org.una.programmingIII.Assignment_Manager_Client.Util.Prueba;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class CourseViewController extends Controller implements Initializable {

    @FXML
    private Accordion acDates;
    @FXML
    private ScrollPane scrollPane;
    boolean havePermission = true;
    ArrayList<Prueba> listPruebas;
    LocalDate startDate;
    LocalDate endDate;

    @Override
    public void initialize() {
        startDate = LocalDate.of(2024, 7, 25);
        endDate = LocalDate.of(2024, 11, 27);
        listPruebas = new ArrayList<>(Arrays.asList(
                new Prueba(LocalDate.of(2020, 7, 26), "General","First"),
                new Prueba(LocalDate.of(2024, 8, 5),"25 of July - 31 of July", "Second")));
        acDates.prefHeightProperty().bind(scrollPane.heightProperty().subtract(5));
        acDates.prefWidthProperty().bind(scrollPane.widthProperty().subtract(15));
        addWeeklyTitledPanes(startDate, endDate);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void addWeeklyTitledPanes(LocalDate startDate, LocalDate endDate) {
        LocalDate currentStartDate = startDate;
        boolean isFirstIteration = true;
        while (!currentStartDate.isAfter(endDate)) {
            if (isFirstIteration) {
                createWeeklyTitledPanes(currentStartDate, endDate);
                isFirstIteration = false;
            }else{
            LocalDate weekEndDate = currentStartDate.plusDays(6).isAfter(endDate) ? endDate : currentStartDate.plusDays(6);
            createWeeklyTitledPanes(currentStartDate, weekEndDate);
            currentStartDate = currentStartDate.plusWeeks(1);
        }}
    }

    private void createWeeklyTitledPanes(LocalDate currentStartDate, LocalDate weekEndDate) {
        String titledData = (currentStartDate.equals(startDate) && weekEndDate.equals(endDate)) ? "General" : convertDateInMonth(currentStartDate, weekEndDate);
        TitledPane weekPane = new TitledPane();
        VBox.setVgrow(weekPane, Priority.ALWAYS);
        weekPane.getStyleClass().add("titledPane");

        Label weekDate = new Label(titledData);
        weekDate.getStyleClass().add("label2");

        HBox header = new HBox(weekDate);
        header.getStyleClass().add("hBox-TitledPane");
        createButtonToAddFile(header);
        weekPane.setGraphic(header);
        createVboxContentTitled(weekPane, titledData);
        acDates.getPanes().add(weekPane);
    }

    private void createVboxContentTitled(TitledPane weekPane,String titledData) {
        VBox content = new VBox();
        VBox.setVgrow(content, Priority.ALWAYS);
        content.setMaxHeight(Double.MAX_VALUE);
        content.getStyleClass().add("vbox-Background-TitledPane");

        for (Prueba prueba : listPruebas) {
            String positionData = prueba.getPosition();
            if (positionData.equals(titledData)) {
                HBox contentData = new HBox();
                createLabelToContent(contentData, prueba.getName(), this::goToFile);
                contentData.getStyleClass().add("hBox-TitledPane");
                createDeleteButton(contentData);
                content.getChildren().add(contentData);
            }
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

//Catch the position of the titledPane
        String titledData = ((Label) ((HBox) titledPane.getGraphic()).getChildren().get(0)).getText();

            System.out.println(titledData);

    }

    private void goToFile(MouseEvent event) {
        System.out.println("Go to file");
    }

    private void deleteFile(ActionEvent event) {
        Button button = (Button) event.getSource();
        HBox parent = (HBox) button.getParent();
        Label label = (Label) parent.getChildren().getFirst();
        String labelText = label.getText();
        System.out.println(labelText);
        // Logic to delete the file
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

}