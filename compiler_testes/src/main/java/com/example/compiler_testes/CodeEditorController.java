package com.example.compiler_testes;

import com.example.compiler_testes.Compiler;
import com.example.compiler_testes.Token;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeEditorController {
    private File currentFile = null; // This variable will store the current file if a file has been opened or saved before.

    public String onNewFileClicked() {
        currentFile = null; // Reset the current file when a new file is created.
        return "New file created!";
    }

    public String onOpenFileClicked(File selectedFile, String content) {
        if (selectedFile != null) {
            try {
                content = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
                currentFile = selectedFile; // Update the current file when a file is opened.
                return "File loaded successfully!";
            } catch (IOException e) {
                return "Error reading file: " + e.getMessage();
            }
        } else {
            return "File selection cancelled.";
        }
    }

    public String onSaveClicked(String content) {
        if (currentFile != null) {
            return onSaveAsClicked(currentFile, content); // If a file path exists, save to that path.
        } else {
            // If no file path exists, prompt the user to choose a location.
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            File file = fileChooser.showSaveDialog(null);
            return onSaveAsClicked(file, content);
        }
    }

    public String onSaveAsClicked(File file, String content) {
        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
                currentFile = file; // Update the current file when a file is saved.
                return "File saved successfully!";
            } catch (IOException ex) {
                return "Error saving file: " + ex.getMessage();
            }
        } else {
            return "File save cancelled.";
        }
    }

    public void onExitClicked() {
        System.exit(0);
    }

    // aqui vamos implementar a logica que vai buildar o codigo, chamando o analizador lexico e mostrnando os resultados na tela
    public String compileCode(String code) {
        try {
            Compiler lexer = new Compiler(new java.io.StringReader(code));
            StringBuilder tokensOutput = new StringBuilder();

            tokensOutput.append(String.format("%-20s %-10s %-10s %-30s\n", "Lexema", "Linha", "Coluna", "Categoria"));

            while (true) {
                Token token = lexer.getNextToken();
                if (token.kind == Compiler.EOF) break;

                String lexema = token.image;
                int line = token.beginLine;
                int column = token.beginColumn;
                String category = getTokenCategory(token.kind); // Assuming you have a method to get the category

                tokensOutput.append(String.format("%-20s %-10d %-10d %-30s\n", lexema, line, column, category));
            }

            return "Build successful!\n\n" + tokensOutput.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    private String getTokenCategory(int kind) {
        // You can implement this method to return the category of the token based on its kind.
        // For now, I'm just returning a placeholder string.
        return "Category for " + kind;
    }

}
