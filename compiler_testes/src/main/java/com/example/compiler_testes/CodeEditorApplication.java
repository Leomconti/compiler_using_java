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
import java.util.Collection;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;


public class CodeEditorApplication extends Application {

    private TextArea messageArea;
    private CodeEditorController controller;
    private Stage primaryStage;
    private ScrollPane scrollPane;
    private Label footerLabel;
    private CodeArea codeArea;


    @Override
    public void start(Stage stage) {
        controller = new CodeEditorController();

        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        HBox codeBox = new HBox(codeArea);
        HBox.setHgrow(codeArea, Priority.ALWAYS); // se nao tava ficando pequeno num canto ali, dai isso expande
        scrollPane = new ScrollPane(codeBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


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

        // criar uma funcao para escutar o cursor e pegar a posicao
        codeArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            int pos = newPos;
            int line = codeArea.getCurrentParagraph();
            int col = pos - codeArea.getAbsolutePosition(line, 0);
            updateFooter(line, col);
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
    // funao que vai catar a linha pela posicao da parada


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
                codeArea.replaceText(content);
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
        System.out.println("Saindo...");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("O arquivo foi modificado.");
        alert.setContentText("Deseja salvar as alterações antes de sair?");

        ButtonType buttonYes = new ButtonType("Sim");
        ButtonType buttonNo = new ButtonType("Não");
        ButtonType buttonCancel = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonYes) {
            String saved = controller.onSaveClicked(codeArea.getText());
            System.exit(0);
        } else if (result.get() == buttonNo) {
            System.exit(0);
        }
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
