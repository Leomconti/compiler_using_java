package com.example.compiler_testes;

import com.example.compiler_testes.CompilerConstants;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String compileCode(String code) {
        List<Token> tokensList = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        Compiler lexer;
        Compiler parser;

        try {
            lexer = new Compiler(new java.io.StringReader(code));
            Token token;

            while (true) {
                try {
                    token = lexer.getNextToken();
                } catch (TokenMgrError e) {
                    output.append("Erro lexico: ").append(e.getMessage());
                    continue;
                }
                if (token.kind == Compiler.EOF) break;  // END OF FILE
                tokensList.add(token);  // Guarda os tokens que esta lendo em uma lista
            }
        } catch (Exception e) {
            output.append("Erro durante a compilação: ").append(e.getMessage());
            return output.toString();
        }
        // Comentar abaixo para nao mostrar output dos tokens
        //  output.append(formatTokenOutput(tokensList));

        // check for errors in lexer
        if (lexer.token_source.foundLexErrors() != 0) {
            output.append("Erros Lexicos encontrados: ").append(lexer.token_source.foundLexErrors());
            String errorMessages = lexer.token_source.getErroLexico();
            if (!errorMessages.isEmpty()) {
                output.append("\n").append(errorMessages);
            }
            // if there are errors don't continue to parser
            return output.toString();
        }

        // STARTS PARSER
        output.append("Sem erros Lexicos, prosseguindo para o Parser...\n");
        parser = new Compiler(new java.io.StringReader(code));
        try {
            parser.programa();
        } catch (ParseException e) {
            // Pegar erros peculiares
            output.append("Erro sintatico: ").append(e.getMessage());
        }
//        int qtdSinErrors = parser.token_source.foundSinErrors();
//        String sintaticErrors = parser.token_source.getErroSintatico();
        int qtdSinErrors = 0;
        String sintaticErrors = "";

        // checar se tem erros no parser
        if (qtdSinErrors != 0) {
            output.append("Erros Sintaticos encontrados: ").append(qtdSinErrors).append("\n");
            if (!sintaticErrors.isEmpty()) {
                output.append("\n").append(sintaticErrors);
            }
            // if there are errors don't continue to parser
            return output.toString();
        }
        // M3
        output.append("Sem erros Sintaticos, ate a M3...\n");
        return output.toString();
    }

    public String formatTokenOutput(List<Token> tokensList) {
        StringBuilder output = new StringBuilder();
        for (Token token : tokensList) {
            String lexema = token.image;
            int line = token.beginLine;
            int column = token.beginColumn;
            String category = getTokenCategory(token.kind);
            output.append(String.format("Line: %d, Column: %d, Lexema: %s, Category: %s\n", line, column, lexema, category));
        }
        return output.toString();
    }

    public String getTokenCategory(int kind) {
        //System.out.println(kind);
        switch (kind) {
            case CompilerConstants.IDENTIFICADOR:
                return "Identificador";
            case CompilerConstants.RESULT:
                return "Result";
            case CompilerConstants.AVALIATE:
                return "Avaliate";
            case CompilerConstants.DO:
                return "Do";
            case CompilerConstants.THIS:
                return "This";
            case CompilerConstants.BODY:
                return "Body";
            case CompilerConstants.DESCRIPTION:
                return "Description";
            case CompilerConstants.DECLARATION:
                return "Declaration";
            case CompilerConstants.TYPE:
                return "Type";
            case CompilerConstants.IS:
                return "Is";
            case CompilerConstants.CONSTANT:
                return "Constant";
            case CompilerConstants.AND:
                return "And";
            case CompilerConstants.VARIABLE:
                return "Variable";
            case CompilerConstants.DESIGNATE:
                return "Designate";
            case CompilerConstants.AS:
                return "As";
            case CompilerConstants.READ:
                return "Read";
            case CompilerConstants.WRITE:
                return "Write";
            case CompilerConstants.ALL:
                return "All";
            case CompilerConstants.REPEAT:
                return "Repeat";
            case CompilerConstants.INTEGER_DEF:
                return "Integer_Def";
            case CompilerConstants.REAL_DEF:
                return "Real_Def";
            case CompilerConstants.LITERAL_DEF:
                return "Literal_Def";
            case CompilerConstants.LOGIC_DEF:
                return "Logic_Def";
            case CompilerConstants.INTEGER:
                return "Integer";
            case CompilerConstants.REAL:
                return "Real";
            case CompilerConstants.TRUE:
                return "True";
            case CompilerConstants.UNTRUE:
                return "Untrue";
            case CompilerConstants.LITERAL:
                return "Literal";
            case CompilerConstants.PLUS:
                return "Plus";
            case CompilerConstants.MINUS:
                return "Minus";
            case CompilerConstants.MULTIPLY:
                return "Multiply";
            case CompilerConstants.DIVIDE:
                return "Divide";
            case CompilerConstants.POWER:
                return "Power";
            case CompilerConstants.MODULO:
                return "Modulo";
            case CompilerConstants.DOUBLE_MODULO:
                return "Double_Modulo";
            case CompilerConstants.LESS_THAN:
                return "Less_Than";
            case CompilerConstants.LESS_THAN_EQUAL:
                return "Less_Than_Equal";
            case CompilerConstants.GREATER_THAN_EQUAL:
                return "Greater_Than_Equal";
            case CompilerConstants.GREATER_THAN:
                return "Greater_Than";
            case CompilerConstants.EQUAL:
                return "Equal";
            case CompilerConstants.NOT_EQUAL:
                return "Not_Equal";
            case CompilerConstants.AND_LOGIC:
                return "And_Logic";
            case CompilerConstants.OR_LOGIC:
                return "Or_Logic";
            case CompilerConstants.NOT_LOGIC:
                return "Not_Logic";
            case CompilerConstants.HASH:
            case CompilerConstants.DOLLAR:
            case CompilerConstants.OPEN_PAREN:
            case CompilerConstants.CLOSE_PAREN:
            case CompilerConstants.COMMA:
            case CompilerConstants.DOT:
            case CompilerConstants.COLON:
            case CompilerConstants.SEMICOLON:
            case CompilerConstants.S_LESS_THAN:
            case CompilerConstants.S_GREATER_THAN:
            case CompilerConstants.QUESTION_MARK:
            case CompilerConstants.AT_SIGN:
            case CompilerConstants.OPEN_BRACKET:
            case CompilerConstants.CLOSE_BRACKET:
            case CompilerConstants.BACKTICK:
            case CompilerConstants.OPEN_BRACE:
            case CompilerConstants.CLOSE_BRACE:
            case CompilerConstants.TILDE:
                return "Special_Symbol";
            case CompilerConstants.EQUALS:
                return "Equals";
            default:
                return String.format("Unknown token '%d'", kind);
        }
    }

}
