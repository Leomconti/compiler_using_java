package com.example.compiler_testes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;


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
            int col = pos+1 - codeArea.getAbsolutePosition(line, 0);
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
        Image newFileIcon = new Image("file:resources/Newfile.png");
        ImageView iconNewFile = new ImageView(newFileIcon);
        iconNewFile.setFitWidth(16);
        iconNewFile.setFitHeight(16);
        Button newFileButton = new Button("Novo", iconNewFile);
        newFileButton.setOnAction(e -> onNewFileClicked());

        Image openFileIcon = new Image("file:resources/Open File.png");
        ImageView iconOpenFile = new ImageView(openFileIcon);
        iconOpenFile.setFitWidth(16);
        iconOpenFile.setFitHeight(16);
        Button openFileButton = new Button("Abrir", iconOpenFile);
        openFileButton.setOnAction(e -> onOpenFileClicked());

        Image saveFileIcon = new Image("file:resources/floppy disk.png");
        ImageView iconSaveFile = new ImageView(saveFileIcon);
        iconSaveFile.setFitWidth(16);
        iconSaveFile.setFitHeight(16);
        Button saveFileButton = new Button("Salvar", iconSaveFile);
        saveFileButton.setOnAction(e -> onSaveClicked());

        Image saveAsIcon = new Image("file:resources/diskette.png");
        ImageView iconSaveAs = new ImageView(saveAsIcon);
        iconSaveAs.setFitWidth(16);
        iconSaveAs.setFitHeight(16);
        Button saveAsButton = new Button("Salvar Como", iconSaveAs);
        saveAsButton.setOnAction(e -> onSaveAsClicked());

        Image exitIcon = new Image("file:resources/Arrow Right.png");
        ImageView iconExit = new ImageView(exitIcon);
        iconExit.setFitWidth(16);
        iconExit.setFitHeight(16);
        Button exitButton = new Button("Sair", iconExit);
        exitButton.setOnAction(e -> onExitClicked());

        Image copyIcon = new Image("file:resources/files.png");
        ImageView iconCopy = new ImageView(copyIcon);
        iconCopy.setFitWidth(16);
        iconCopy.setFitHeight(16);
        Button copyButton = new Button("Copiar", iconCopy);
        copyButton.setOnAction(e -> onCopyClicked());

        Image pasteIcon = new Image("file:resources/Paste icon.png");
        ImageView iconPaste = new ImageView(pasteIcon);
        iconPaste.setFitWidth(16);
        iconPaste.setFitHeight(16);
        Button pasteButton = new Button("Colar", iconPaste);
        pasteButton.setOnAction(e -> onPasteClicked());

        Image cutIcon = new Image("file:resources/Scissors.png");
        ImageView iconCut = new ImageView(cutIcon);
        iconCut.setFitWidth(16);
        iconCut.setFitHeight(16);
        Button cutButton = new Button("Recortar", iconCut);
        cutButton.setOnAction(e -> onCutClicked());

        Image compileIcon = new Image("file:resources/BuildOk.png");
        ImageView iconCompile = new ImageView(compileIcon);
        iconCompile.setFitWidth(16);
        iconCompile.setFitHeight(16);
        Button compileButton = new Button("Compilar", iconCompile);
        compileButton.setOnAction(e -> onBuildClicked());

        Image runIcon = new Image("file:resources/Arrow Right.png");
        ImageView iconRun = new ImageView(runIcon);
        iconRun.setFitWidth(16);
        iconRun.setFitHeight(16);
        Button runButton = new Button("Executar", iconRun);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Cuidado!");
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
            codeArea.clear();
            messageArea.setText(controller.onNewFileClicked());
            primaryStage.setTitle("Compilador - Sem arquivo");
        }
    }

    private void onOpenFileClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Cuidado!");
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
        String result = "";
        try {
            result = controller.compileCode(code);
        }
        catch (Exception e) {
            result = e.getMessage();
        }
        messageArea.setText(result);
    }


    private void onRunClicked() {
        messageArea.setText("Running...");
    }

    public static void main(String[] args) {
        launch();
    }
}
