package com.example.compiler_testes;

import java.util.*;

public class First {
    static public final RecoverySet programa = new RecoverySet();
    static public final RecoverySet finalPrograma = new RecoverySet();
    static public final RecoverySet atribuicao = new RecoverySet();
    static public final RecoverySet entrada = new RecoverySet();
    static public final RecoverySet saida = new RecoverySet();
    static public final RecoverySet selecao = new RecoverySet();
    static public final RecoverySet repeticao = new RecoverySet();
    static public final RecoverySet listaComandos = new RecoverySet();
    static public final RecoverySet EOF = new RecoverySet();

    static {
        programa.add(CompilerConstants.DO);
        programa.add(CompilerConstants.THIS);

        finalPrograma.add(CompilerConstants.DESCRIPTION);

        atribuicao.add(CompilerConstants.DESIGNATE);

        entrada.add(CompilerConstants.READ);

        saida.add(CompilerConstants.WRITE);

        selecao.add(CompilerConstants.AVALIATE);

        repeticao.add(CompilerConstants.REPEAT);

        listaComandos.add(CompilerConstants.READ);
        listaComandos.add(CompilerConstants.WRITE);
        listaComandos.add(CompilerConstants.DESIGNATE);
        listaComandos.add(CompilerConstants.AVALIATE);
        listaComandos.add(CompilerConstants.REPEAT);


        EOF.add(CompilerConstants.EOF);
    }
}

