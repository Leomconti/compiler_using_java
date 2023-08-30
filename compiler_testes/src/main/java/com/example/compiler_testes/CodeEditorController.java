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
    private File currentFile = null;

    public String onNewFileClicked() {
        currentFile = null;
        return "Novo arquivo criado!";
    }

    public String onOpenFileClicked(File selectedFile, String content) {
        if (selectedFile != null) {
            try {
                content = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
                currentFile = selectedFile;
                return "Arquivo carregado com suceso!";
            } catch (IOException e) {
                return "Erro lendo o arquivo: " + e.getMessage();
            }
        } else {
            return "Abrir arquivo cancelado.";
        }
    }

    public String onSaveClicked(String content) {
        if (currentFile != null) {
            return onSaveAsClicked(currentFile, content); // se ja tem salva
        } else {
            // se nao tem via pro salvar como
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Arquivo");

            // Set the default extension filter to .txt
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File file = fileChooser.showSaveDialog(null);
            return onSaveAsClicked(file, content);
        }
    }

    public String onSaveAsClicked(File file, String content) {
        if (file != null) {
            // Check and append .txt extension if needed
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(content);
                fileWriter.close();
                currentFile = file;
                return "Salvo com Sucesso!";
            } catch (IOException ex) {
                return "Erro ao salvar: " + ex.getMessage();
            }
        } else {
            return "Salvar cancelado.";
        }
    }

    // aqui vamos implementar a logica que vai buildar o codigo, chamando o analizador lexico e mostrnando os resultados na tela

    private String formatLexema(String lexema, int maxSize) {
        if (lexema.length() <= maxSize) {
            return lexema;
        }

        return lexema.substring(0, maxSize) + "\n" + formatLexema(lexema.substring(maxSize), maxSize);
    }

    public String compileCode(String code) {
        try {
            Compiler lexer = new Compiler(new java.io.StringReader(code));
            StringBuilder tokensOutput = new StringBuilder();

            int lexemaSize = 40; // Aumentando o espaço para o lexema
            int lineSize = 10;
            int columnSize = 10;
            int categorySize = 30;
            int categoryNumberSize = 20;

            tokensOutput.append(String.format(
                    "%-" + lexemaSize + "s | " +
                            "%-" + lineSize + "s | " +
                            "%-" + columnSize + "s | " +
                            "%-" + categorySize + "s | " +
                            "%-" + categoryNumberSize + "s\n",
                    "Lexema", "Linha", "Coluna", "Categoria", "Número da Categoria"
            ));
            tokensOutput.append("-".repeat(lexemaSize + lineSize + columnSize + categorySize + categoryNumberSize + 12) + "\n");

            while (true) {
                Token token = lexer.getNextToken();
                if (token.kind == Compiler.EOF) break;

                String lexema = formatLexema(token.image, lexemaSize);
                String[] lexemaLines = lexema.split("\n");

                int line = token.beginLine;
                int column = token.beginColumn;
                String category = getTokenCategory(token.kind);
                int categoryNumber = token.kind;

                // Adicione as linhas do lexema que foram quebradas
                for (int i = 0; i < 5; i++) {
                    if (i < lexemaLines.length) {
                        tokensOutput.append(String.format(
                                "%-" + lexemaSize + "s | ", lexemaLines[i]
                        ));
                    } else {
                        tokensOutput.append(String.format(
                                "%-" + lexemaSize + "s | ", ""
                        ));
                    }

                    if (i == lexemaLines.length - 1) { // Adicione as outras informações na última linha do lexema
                        tokensOutput.append(String.format(
                                "%-" + lineSize + "d | " +
                                        "%-" + columnSize + "d | " +
                                        "%-" + categorySize + "s | " +
                                        "%-" + categoryNumberSize + "d\n",
                                line, column, category, categoryNumber
                        ));
                    } else {
                        tokensOutput.append("\n");
                    }
                }
            }

            return "Compilação bem-sucedida!\n\n" + tokensOutput.toString();
        } catch (Exception e) {
            return "Erro durante a compilação: " + e.getMessage();
        }
    }

    private String getTokenCategory(int kind) {
        // slk, intellij reformatou pra esse switch, olha que baita @anderson
        return switch (kind) {
            case (0) -> "EOF";
            case (1) -> "IDENTIFIER";
            default -> "Category for " + kind;
        };
    }

}
