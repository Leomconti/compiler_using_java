package com.example.compiler_testes;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.geometry.Insets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeEditorApplication extends Application {

    private TextArea codeArea;
    private TextArea messageArea;
    private CodeEditorController controller;
    private Stage primaryStage;
    private VBox lineNumberBox;
    private ScrollPane scrollPane;
    private Label footerLabel;

    @Override
    public void start(Stage stage) {
        controller = new CodeEditorController();

        codeArea = new TextArea();
        codeArea.setPromptText("Escreva o codigo na linguagem 2023.2 aqui...");

        lineNumberBox = new VBox();
        lineNumberBox.setPrefWidth(30);
        lineNumberBox.setSpacing(2);

        HBox codeBox = new HBox(lineNumberBox, codeArea);
        scrollPane = new ScrollPane(codeBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        codeArea.textProperty().addListener((obs, oldText, newText) -> updateLineNumbers());
        codeArea.caretPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int caretPosition = newValue.intValue();
                int currentLine = getCurrentLine(caretPosition);
                highlightLine(currentLine);
                int col = caretPosition;
                for (int i = 0; i < currentLine; i++) {
                    col -= codeArea.getParagraphs().get(i).length() + 1; // +1 for the newline character
                }
                updateFooter(currentLine, col);
            }
        });
        // aqui o handle pra contar o tab como 4 espacos, pq quem nao coloca isso, tem problemas serios!
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                    String tabReplacement = "    "; // 4 spaces
                    codeArea.insertText(codeArea.getCaretPosition(), tabReplacement);
                    event.consume(); // this stops the event from being propagated further
                }
            }
        });



        messageArea = new TextArea();
        messageArea.setPromptText("Mensagens de saida aqui...");
        messageArea.setEditable(false);

        this.primaryStage = stage;
        stage.setTitle("Compilador - Sem arquivo");

        SplitPane splitPane = new SplitPane(scrollPane, messageArea);
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPositions(0.7);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new VBox(initMenuBar(), initToolBar()));
        borderPane.setCenter(splitPane);
        borderPane.setPadding(new Insets(10));

        footerLabel = new Label();
        borderPane.setBottom(footerLabel);

        Scene scene = new Scene(borderPane, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private int getCurrentLine(int caretPosition) {
        int line = 0;
        int passedChars = 0;
        for (CharSequence paragraph : codeArea.getParagraphs()) {
            int length = paragraph.length();
            if (caretPosition - passedChars <= length) {
                return line;
            }
            passedChars += length + 1; // +1 for the newline character
            line++;
        }
        return Math.min(line, codeArea.getParagraphs().size() - 1);
    }


    private void updateLineNumbers() {
        lineNumberBox.getChildren().clear();
        int lineCount = codeArea.getParagraphs().size();
        for (int i = 1; i <= lineCount; i++) {
            Label lineNumber = new Label(String.valueOf(i));
            lineNumber.setPrefHeight(codeArea.getFont().getSize() + 5);
            lineNumberBox.getChildren().add(lineNumber);
        }
    }

    private void highlightLine(int currentLine) {
        for (int i = 0; i < lineNumberBox.getChildren().size(); i++) {
            if (i == currentLine) {
                lineNumberBox.getChildren().get(i).setStyle("-fx-background-color: lightgray;");
            } else {
                lineNumberBox.getChildren().get(i).setStyle("");
            }
        }
    }


    private void updateFooter(int line, int col) {
        footerLabel.setText("Line: " + (line + 1) + " Col: " + col);
    }

    // montando os menus ( que clica e abre outros)
    private MenuBar initMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menu Arquivo
        Menu menuFile = new Menu("Arquivo");
        MenuItem newFile = new MenuItem("Novo");
        newFile.setOnAction(e -> onNewFileClicked());

        MenuItem openFile = new MenuItem("Abrir");
        openFile.setOnAction(e -> onOpenFileClicked());

        MenuItem saveFile = new MenuItem("Salvar");
        saveFile.setOnAction(e -> onSaveClicked());

        MenuItem saveFileAs = new MenuItem("Salvar Como");
        saveFileAs.setOnAction(e -> onSaveAsClicked());

        MenuItem exit = new MenuItem("Sair");
        exit.setOnAction(e -> onExitClicked());

        menuFile.getItems().addAll(newFile, openFile, saveFile, saveFileAs, new SeparatorMenuItem(), exit);

        // Menu Edição
        Menu menuEdit = new Menu("Edição");
        MenuItem copy = new MenuItem("Copiar");
        copy.setOnAction(e -> onCopyClicked());

        MenuItem paste = new MenuItem("Colar");
        paste.setOnAction(e -> onPasteClicked());

        MenuItem cut = new MenuItem("Recortar");
        cut.setOnAction(e -> onCutClicked());

        menuEdit.getItems().addAll(copy, paste, cut);

        // Menu Compilação
        Menu menuCompile = new Menu("Compilação");
        MenuItem compile = new MenuItem("Compilar");
        compile.setOnAction(e -> onBuildClicked());

        MenuItem run = new MenuItem("Executar");
        run.setOnAction(e -> onRunClicked());

        menuCompile.getItems().addAll(compile, run);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuCompile);

        return menuBar;
    }

    // montando a toolbar, que tera um botao para cada opcao do menu
    private ToolBar initToolBar() {
        Button newFileButton = new Button("Novo");
        newFileButton.setOnAction(e -> onNewFileClicked());

        Button openFileButton = new Button("Abrir");
        openFileButton.setOnAction(e -> onOpenFileClicked());

        Button saveFileButton = new Button("Salvar");
        saveFileButton.setOnAction(e -> onSaveClicked());

        Button saveAsButton = new Button("Salvar Como");
        saveAsButton.setOnAction(e -> onSaveAsClicked());

        Button exitButton = new Button("Sair");
        exitButton.setOnAction(e -> onExitClicked());

        Button copyButton = new Button("Copiar");
        copyButton.setOnAction(e -> onCopyClicked());

        Button pasteButton = new Button("Colar");
        pasteButton.setOnAction(e -> onPasteClicked());

        Button cutButton = new Button("Recortar");
        cutButton.setOnAction(e -> onCutClicked());

        Button compileButton = new Button("Compilar");
        compileButton.setOnAction(e -> onBuildClicked());

        Button runButton = new Button("Executar");
        runButton.setOnAction(e -> onRunClicked());

        ToolBar toolBar = new ToolBar(
                newFileButton, openFileButton, saveFileButton, saveAsButton,
                new Separator(), copyButton, pasteButton, cutButton,
                new Separator(), compileButton, runButton
        );

        return toolBar;
    }

    // setando as funcoes que vao chamar o controller do backend para executar as acoes do botao
    private void onNewFileClicked() {
        codeArea.clear();
        messageArea.setText(controller.onNewFileClicked());
        primaryStage.setTitle("Compilador - Sem arquivo");
    }

    private void onOpenFileClicked() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
                codeArea.setText(content);
                primaryStage.setTitle("Compilador - " + selectedFile.getName()); // Update the title
                messageArea.setText(controller.onOpenFileClicked(selectedFile, codeArea.getText()));
            } catch (IOException e) {
                messageArea.setText("Erro lendo o arquivo: " + e.getMessage());
            }
        } else {
            messageArea.setText("Selecao de arquivo cancelada.");
        }
    }


    private void onSaveAsClicked() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            messageArea.setText(controller.onSaveAsClicked(file, codeArea.getText()));
            primaryStage.setTitle("Compilador - " + file.getName()); // atualiza o titulo para o nome do arquivo salvo
        } else {
            messageArea.setText("File save cancelled.");
        }
    }

    private void onExitClicked() {
        controller.onExitClicked();
    }

    private void onCopyClicked() {
        codeArea.copy();
    }

    private void onPasteClicked() {
        codeArea.paste();
    }

    private void onCutClicked() {
        codeArea.cut();
    }

    private void onSaveClicked() {
        controller.onSaveClicked(codeArea.getText());
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
