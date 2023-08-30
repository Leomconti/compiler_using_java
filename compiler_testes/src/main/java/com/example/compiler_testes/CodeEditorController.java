package com.example.compiler_testes;

import com.example.compiler_testes.Compiler;
import com.example.compiler_testes.Token;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    // classe para deixar mais facil mostrar um token ( utilizando override no tostring)
    public class TokenInfo {
        public String lexema;
        public int line;
        public int column;
        public String category;
        public int categoryNumber;

        public TokenInfo(String lexema, int line, int column, int categoryNumber) {
            this.lexema = lexema;
            this.line = line;
            this.column = column;
            this.category = getTokenCategory(categoryNumber);
            this.categoryNumber = categoryNumber;
        }

        @Override
        public String toString() {
            return "Linha: " + line + ", Coluna: " + column + "\nCategoria " + categoryNumber + ": " + category + "\nLexema: " + lexema;
        }
    }

    public String compileCode(String code) {
        List<TokenInfo> tokensList = new ArrayList<>();
        try {
            Compiler lexer = new Compiler(new java.io.StringReader(code));
            Token token;
            while (true) {
                try {
                    token = lexer.getNextToken();
                } catch (TokenMgrError e) {
                    System.out.println("Erro léxico: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    System.out.println("Erro durante a compilação: " + e.getMessage());
                    break;
                }
                if (token.kind == Compiler.EOF) break;

                String lexema = token.image;
                int line = token.beginLine;
                int column = token.beginColumn;
                String category = getTokenCategory(token.kind); // suponho que essa função retorna o nome da categoria baseado no número da categoria
                int categoryNumber = token.kind;

                tokensList.add(new TokenInfo(lexema, line, column, categoryNumber));
            }
        } catch (Exception e) {
            System.out.println(tokensList);
            return "Erro durante a compilação: " + e.getMessage();
        }

        if (tokensList.isEmpty()) {
            return "Nenhum token foi encontrado.";
        }

        StringBuilder tokensOutput = new StringBuilder();
        for (TokenInfo tokenInfo : tokensList) {
            tokensOutput.append(tokenInfo.toString()).append("\n");
        }

        return tokensOutput.toString();
    }


    private String getTokenCategory(int kind) {
        // slk, intellij reformatou pra esse switch, olha que baita @anderson
        return switch (kind) {
            case (0) -> "EOF";
            case (1) -> "IDENTIFIER";
            case (13) -> "Comentario de Linha";
            case (14) -> "Comentario de Bloco";
            default -> "Category for " + kind;
        };
    }

}
