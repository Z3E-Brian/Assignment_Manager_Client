package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseContentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.FileService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Logger;

public class CourseViewController extends Controller {

    @FXML
    private Accordion acDates;
    @FXML
    private ScrollPane scrollPane;
    boolean havePermission = true;
    LocalDate startDate;
    LocalDate endDate;
    List<AssignmentDto> assignments;
    List<CourseContentDto> courseContents;
    CourseDto courseDto;

    @Override
    public void initialize() {
        courseDto = (CourseDto) AppContext.getInstance().get("course");
        startDate = courseDto.getStartDate();
        endDate = courseDto.getEndDate();
        loanAssignments();
        loadCourseContents();
        acDates.prefHeightProperty().bind(scrollPane.heightProperty().subtract(5));
        acDates.prefWidthProperty().bind(scrollPane.widthProperty().subtract(15));
        addWeeklyTitledPanes(startDate, endDate);
    }


    private void addWeeklyTitledPanes(LocalDate startDate, LocalDate endDate) {
        LocalDate currentStartDate = startDate;
        boolean isFirstIteration = true;
        while (!currentStartDate.isAfter(endDate)) {
            if (isFirstIteration) {
                createWeeklyTitledPanes(currentStartDate, endDate);
                isFirstIteration = false;
            } else {
                LocalDate weekEndDate = currentStartDate.plusDays(6).isAfter(endDate) ? endDate : currentStartDate.plusDays(6);
                createWeeklyTitledPanes(currentStartDate, weekEndDate);
                currentStartDate = currentStartDate.plusWeeks(1);
            }
        }
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


    private void createVboxContentTitled(TitledPane weekPane, String titledData) {
        VBox content = new VBox();
        VBox.setVgrow(content, Priority.ALWAYS);
        content.setMaxHeight(Double.MAX_VALUE);
        content.getStyleClass().add("vbox-Background-TitledPane");

        if (assignments != null) {
            assignments.stream()
                    .filter(assignment -> assignment.getAddress().equals(titledData))
                    .forEach(assignment -> {
                        if (content.getChildren().stream().noneMatch(node -> ((Label) ((HBox) node).getChildren().get(0)).getText().equals(assignment.getId().toString()))) {
                            HBox contentData = new HBox();
                            createLabelToContent(contentData, assignment.getTitle(), this::goToFile);
                            contentData.getStyleClass().add("hBox-TitledPane");
                            createDeleteButton(contentData);
                            content.getChildren().add(contentData);
                        }
                    });
        }
        if (courseContents != null) {
            courseContents.stream()
                    .filter(courseContent -> courseContent.getAddress().equals(titledData))
                    .flatMap(courseContent -> courseContent.getFiles().stream())
                    .forEach(file -> {
                        if (content.getChildren().stream().noneMatch(node -> ((Label) ((HBox) node).getChildren().get(0)).getText().equals(file.getId().toString()))) {
                            HBox contentData = new HBox();
                            createLabelToContent(contentData, file.getName(), this::goToFile);
                            contentData.getStyleClass().add("hBox-TitledPane");
                            createDeleteButton(contentData);
                            content.getChildren().add(contentData);
                        }
                    });
        }

        weekPane.setContent(content);
    }

    private void addFile(ActionEvent event) {
        HBox parent = (HBox) ((Button) event.getSource()).getParent();
        TitledPane titledPane = (TitledPane) parent.getParent().getParent();
        VBox content = (VBox) titledPane.getContent();
        HBox contentData = new HBox();
        contentData.getStyleClass().add("hBox-TitledPane");
        createDeleteButton(contentData);
        content.getChildren().add(contentData);

        String titledData = ((Label) ((HBox) titledPane.getGraphic()).getChildren().get(0)).getText();
        AppContext.getInstance().set("position", titledData);
        FlowController.getInstance().goViewInWindowModal("AddAssignmentOrFileView", getStage(), false);
    }

    private void goToFile(MouseEvent event) {
        HBox parent = (HBox) ((Label) event.getSource()).getParent();
        String labelText = ((Label) parent.getChildren().getFirst()).getText();
        FileDto fileDto = getFileDto(labelText);
        if (fileDto != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileDto.getName());
            fileChooser.setTitle("Save File");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                try {
                    new FileService().downloadFileInChunks(fileDto.getId(), Paths.get(file.getAbsolutePath()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        AssignmentDto assignmentDto = getAssignmentDto(labelText);
        if (assignmentDto != null) {
            AppContext.getInstance().set("assignment", assignmentDto);
            //TODO: ADD functionality to go to assignment
            System.out.println(assignmentDto.getDescription());
           // FlowController.getInstance().goViewInWindowModal("AssignmentView", getStage(), false);
        }

    }

    private void deleteFile(ActionEvent event) {
        HBox parent = (HBox) ((Button) event.getSource()).getParent();
        String labelText = ((Label) parent.getChildren().getFirst()).getText();

        if (new Message().showConfirmation("Delete File", getStage(), "Are you sure you want to delete the file?")) {
            assignments.stream()
                    .filter(assignment -> assignment.getTitle().equals(labelText))
                    .findFirst()
                    .ifPresent(assignment -> {
                        try {
                            Answer answer = new AssignmentService().deleteAssignment(assignment.getId());
                            if (answer.getState()) assignments.remove(assignment);
                        } catch (Exception e) {
                            Logger.getLogger(CourseViewController.class.getName()).severe("Unexpected error: " + e.getMessage());
                        }
                    });

            courseContents.stream()
                    .flatMap(courseContent -> courseContent.getFiles().stream())
                    .filter(file -> file.getName().equals(labelText))
                    .findFirst()
                    .ifPresent(file -> {
                        try {
                            Answer answer = new FileService().deleteFile(file.getId());
                            if (answer.getState()) {
                                courseContents.stream()
                                        .filter(courseContent -> courseContent.getFiles().contains(file))
                                        .findFirst()
                                        .ifPresent(courseContent -> courseContent.getFiles().remove(file));
                            }
                        } catch (Exception e) {
                            Logger.getLogger(CourseViewController.class.getName()).severe("Unexpected error: " + e.getMessage());
                        }
                    });
        }

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

    private void loanAssignments() {
        try {
            Answer answer = new AssignmentService().getAssignmentsByCourseId(courseDto.getId());
            if (answer.getState()) {
                assignments = (List<AssignmentDto>) answer.getResult("assignments");
            } else {
                Logger.getLogger(CourseViewController.class.getName()).severe("Failed to load assignments: " + answer.getMessage());
            }
        } catch (Exception e) {
            Logger.getLogger(CourseViewController.class.getName()).severe("Unexpected error: " + e.getMessage());
        }
    }

    private void loadCourseContents() {
        try {
            Answer answer = new CourseContentService().getAllCourseContentById(courseDto.getId());
            if (answer.getState()) {
                courseContents = (List<CourseContentDto>) answer.getResult("courseContent");
            } else {
                Logger.getLogger(CourseViewController.class.getName()).severe("Failed to load course contents: " + answer.getMessage());
            }
        } catch (Exception e) {
            Logger.getLogger(CourseViewController.class.getName()).severe("Unexpected error: " + e.getMessage());
        }
    }

    private FileDto getFileDto(String name) {
        return courseContents.stream()
                .flatMap(courseContent -> courseContent.getFiles().stream())
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    private AssignmentDto getAssignmentDto(String title) {
        return assignments.stream()
                .filter(assignment -> assignment.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
}