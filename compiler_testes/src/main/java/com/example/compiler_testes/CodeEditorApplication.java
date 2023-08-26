package com.example.compiler_testes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeEditorApplication extends Application {

    private TextArea codeArea;
    private TextArea messageArea;
    private CodeEditorController controller;
    private Stage primaryStage; // titulozao la em cima para a classe toda conseguir atualiza-lo

    @Override
    public void start(Stage stage) {
        controller = new CodeEditorController();  // chama o controlador do backend, que tera as funcoes necessarias
        // criar a area do codigo
        codeArea = new TextArea();
        codeArea.setPromptText("Escreva o codigo na linguagem 2023.2 aqui...");

        // criar a area do output
        messageArea = new TextArea();
        messageArea.setPromptText("Mensagens de saida aqui...");
        messageArea.setEditable(false); // proibido escrever na area de output
        // informacoes do arquibo selecionado
        this.primaryStage = stage;
        stage.setTitle("Compilador - Sem arquivo");

        // criar o split pane para poder mexer cima e baixo
        SplitPane splitPane = new SplitPane(codeArea, messageArea);
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPositions(0.7);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new VBox(initMenuBar(), initToolBar())); // menu e toolbar em cima
        borderPane.setCenter(splitPane);
        borderPane.setPadding(new Insets(10));

        Scene scene = new Scene(borderPane, 600, 500);

        stage.setScene(scene);
        stage.show();
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
