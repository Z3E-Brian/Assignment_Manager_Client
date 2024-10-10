package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.DatesTitle;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class CourseViewController extends Controller implements Initializable {

    @FXML
    private Accordion acDates;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox hbxContainer;
    DatesTitle datesTitle = new DatesTitle();

    @Override
    public void initialize() {
        LocalDate startDate = LocalDate.of(2024, 7, 25);
        LocalDate endDate = LocalDate.of(2024, 11, 27);
        scrollPane.prefHeightProperty().bind(hbxContainer.heightProperty());
        scrollPane.prefWidthProperty().bind(hbxContainer.widthProperty());
        acDates.prefHeightProperty().bind(scrollPane.heightProperty());
        acDates.prefWidthProperty().bind(scrollPane.widthProperty());
        addWeeklyTitledPanes(startDate, endDate);
    }

    private void addWeeklyTitledPanes(LocalDate startDate, LocalDate endDate) {
        LocalDate currentStartDate = startDate;

        while (!currentStartDate.isAfter(endDate)) {
            LocalDate weekEndDate = currentStartDate.plusDays(6);
            if (weekEndDate.isAfter(endDate)) {
                weekEndDate = endDate;
            }

            TitledPane weekPane = new TitledPane();
            VBox.setVgrow(weekPane, Priority.ALWAYS);
            weekPane.getStyleClass().add("titledPane");
            weekPane.setText(datesTitle.addWeeklyTitledPanes(currentStartDate, weekEndDate));

            VBox content = new VBox();
            VBox.setVgrow(content, Priority.ALWAYS);
            content.setMaxHeight(Double.MAX_VALUE);
            content.getStyleClass().add("vbox-Background-TitledPane");
            content.getChildren().add(new Label("Archivo: ListaOrdenada.docx"));
            content.getChildren().add(new Label("Actividad: Práctica Evaluada.docx"));

            weekPane.setContent(content);

            acDates.getPanes().add(weekPane);

            currentStartDate = currentStartDate.plus(1, ChronoUnit.WEEKS);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}