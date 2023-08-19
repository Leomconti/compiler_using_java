package com.example.compiler_testes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.geometry.Orientation;
import javafx.geometry.Insets;


public class CodeEditorApplication extends Application {

    private TextArea codeArea;
    private TextArea messageArea;
    private CodeEditorController controller;

    @Override
    public void start(Stage stage) {
        controller = new CodeEditorController();

        // Creating the main code input area
        codeArea = new TextArea();
        codeArea.setPromptText("Write your code here...");

        // Create the TextArea for messages/output
        messageArea = new TextArea();
        messageArea.setPromptText("Output messages here...");
        messageArea.setEditable(false); // disable writing in the message area

        // Creating the top left menus
        Menu saveMenu = new Menu("Save");
        saveMenu.setOnAction(e -> onSaveClicked());

        Menu clearMenu = new Menu("Clear");
        clearMenu.setOnAction(e -> onClearClicked());

        Menu buildMenu = new Menu("Build");
        buildMenu.setOnAction(e -> onBuildClicked());

        Menu runMenu = new Menu("Run");
        runMenu.setOnAction(e -> onRunClicked());

        MenuBar menuBar = new MenuBar(saveMenu, clearMenu, buildMenu, runMenu);


        // Create a SplitPane and add the codeArea and messageArea to it
        // Create a SplitPane and add the codeArea and messageArea to it
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(codeArea, messageArea);

        // Set the orientation to VERTICAL
        splitPane.setOrientation(Orientation.VERTICAL);

        // Set the initial divider position (0.0 means top/left, 1.0 means bottom/right)
        splitPane.setDividerPositions(0.7);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);       // Set the MenuBar at the top
        borderPane.setCenter(splitPane);  // Set the SplitPane at the center
        borderPane.setPadding(new Insets(10));  // 10 units of padding on all sides

        Scene scene = new Scene(borderPane, 600, 500);

        stage.setTitle("Code Editor");
        stage.setScene(scene);
        stage.show();
    }

    private void onSaveClicked() {
        messageArea.setText("Saving...");
    }

    private void onClearClicked() {
        messageArea.setText("Clearing the code are...");
        codeArea.clear();
    }

    private void onBuildClicked() {
        String code = codeArea.getText();
        String result = controller.compileCode(code);
        messageArea.setText(result);
    }

    private void onRunClicked() {
        messageArea.setText("Running...");
    }

    public static void main(String[] args) {
        launch();
    }
}
