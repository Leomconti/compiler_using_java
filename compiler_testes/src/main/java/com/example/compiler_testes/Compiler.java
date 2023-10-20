/* Compiler.java */
/* Generated By:JavaCC: Do not edit this line. Compiler.java */
package com.example.compiler_testes;


public class Compiler implements CompilerConstants {
    public static void main(String[] args) {
    }


    public void handleError(ParseException e, String rule) {
        int line = e.currentToken.next.beginLine;
        int column = e.currentToken.next.beginColumn;
        String found = e.currentToken.next.image;

        String formattedError = formatPrint(rule, line, column, found, "Expected_Token", "Syntax Error");

        }

    public String formatPrint(String rule, int line, int column, String found, String expected, String msg){
        StringBuilder output = new StringBuilder();
        output.append("Rule: ").append(rule).append("\n");
        output.append("Syntax Error: Line ").append(line).append(", Column ").append(column).append("\n");
        output.append("Encountered: ").append(found).append("\n");
        output.append("Expected: ").append(expected).append("\n");
        output.append("Message: ").append(msg).append("\n");
        System.err.println(output.toString());
        return output.toString();
    }

  final public void programa() throws ParseException {
    try {
      jj_consume_token(DO);
      jj_consume_token(THIS);
      jj_consume_token(IDENTIFICADOR);
      jj_consume_token(OPEN_BRACKET);
      jj_consume_token(CLOSE_BRACKET);
      DeclarationOrNothing();
      jj_consume_token(BODY);
      jj_consume_token(OPEN_BRACKET);
      ListaComandos();
      jj_consume_token(CLOSE_BRACKET);
      FinalPrograma();
    } catch (ParseException e) {
handleError(e, "programa");
    }
}

  final public void FinalPrograma() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DESCRIPTION:{
        jj_consume_token(DESCRIPTION);
        jj_consume_token(LITERAL);
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "FinalPrograma");
    }
}

  final public void Atribuicao() throws ParseException {
    try {
      jj_consume_token(DESIGNATE);
      jj_consume_token(THIS);
      ListaDeIdentificadores();
      jj_consume_token(AS);
      Expressao();
    } catch (ParseException e) {
handleError(e, "atribuicao");
    }
}

  final public void Entrada() throws ParseException {
    try {
      jj_consume_token(READ);
      jj_consume_token(THIS);
      jj_consume_token(OPEN_BRACKET);
      ListaDeIdentificadores();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "entrada");
    }
}

  final public void Saida() throws ParseException {
    try {
      Write();
      jj_consume_token(OPEN_BRACKET);
      listaIdentificadorConstante();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "saida");
    }
}

  final public void listaIdentificadorConstante() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IDENTIFICADOR:{
      jj_consume_token(IDENTIFICADOR);
      break;
      }
    case INTEGER:
    case REAL:
    case LITERAL:{
      ConstantValue();
      break;
      }
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    adicionalIdentificadorConstante();
}

  final public void ConstantValue() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGER:{
      jj_consume_token(INTEGER);
      break;
      }
    case REAL:{
      jj_consume_token(REAL);
      break;
      }
    case LITERAL:{
      jj_consume_token(LITERAL);
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
}

  final public void adicionalIdentificadorConstante() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMMA:{
        jj_consume_token(COMMA);
        listaIdentificadorConstante();
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "ListaDeIdentificadoresAdicional");
    }
}

  final public void Write() throws ParseException {
    try {
      jj_consume_token(WRITE);
      Write_();
    } catch (ParseException e) {
handleError(e, "write");
    }
}

  final public void Write_() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ALL:{
        jj_consume_token(ALL);
        jj_consume_token(THIS);
        break;
        }
      case THIS:{
        jj_consume_token(THIS);
        break;
        }
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "write_");
    }
}

  final public void Selecao() throws ParseException {
    try {
      jj_consume_token(AVALIATE);
      jj_consume_token(THIS);
      Expressao();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case TRUE:{
        TrueUntrueResult();
        break;
        }
      case UNTRUE:{
        UntrueTrueResult();
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "selecao");
    }
}

  final public void trueResult() throws ParseException {
    try {
      jj_consume_token(TRUE);
      jj_consume_token(RESULT);
      jj_consume_token(OPEN_BRACKET);
      ListaComandos();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "TrueResult");
    }
}

  final public void UntrueResult() throws ParseException {
    try {
      jj_consume_token(UNTRUE);
      jj_consume_token(RESULT);
      jj_consume_token(OPEN_BRACKET);
      ListaComandos();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "TrueResult");
    }
}

  final public void TrueUntrueResult() throws ParseException {
    try {
      trueResult();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case UNTRUE:{
        UntrueResult();
        break;
        }
      default:
        jj_la1[6] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "TrueResult");
    }
}

  final public void UntrueTrueResult() throws ParseException {
    try {
      UntrueResult();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case TRUE:{
        trueResult();
        break;
        }
      default:
        jj_la1[7] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "UntrueTrueResult");
    }
}

  final public void Comando() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DESIGNATE:{
        Atribuicao();
        break;
        }
      case READ:{
        Entrada();
        break;
        }
      case WRITE:{
        Saida();
        break;
        }
      case AVALIATE:{
        Selecao();
        break;
        }
      case REPEAT:{
        Repeticao();
        break;
        }
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "comando");
    }
}

  final public void ListaComandos() throws ParseException {
    try {
      Comando();
      jj_consume_token(DOT);
      ComandoAdicional();
    } catch (ParseException e) {
handleError(e, "listaComandos");
    }
}

  final public void ComandoAdicional() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AVALIATE:
      case DESIGNATE:
      case READ:
      case WRITE:
      case REPEAT:{
        ListaComandos();
        break;
        }
      default:
        jj_la1[9] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "ComandoAdicional");
    }
}

  final public void Repeticao() throws ParseException {
    try {
      jj_consume_token(REPEAT);
      jj_consume_token(THIS);
      Expressao();
      jj_consume_token(OPEN_BRACKET);
      ListaComandos();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "repeticao");
    }
}

  final public void Expressao() throws ParseException {
    try {
      ExpressaoAritOuLogica();
      Expressao_();
    } catch (ParseException e) {
handleError(e, "expressao");
    }
}

// the [] means that the token is optional, sooo, it's basically the OR Episolon
  final public void Expressao_() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LESS_THAN:
      case LESS_THAN_EQUAL:
      case GREATER_THAN_EQUAL:
      case GREATER_THAN:
      case EQUAL:
      case NOT_EQUAL:{
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case EQUAL:{
          jj_consume_token(EQUAL);
          ExpressaoAritOuLogica();
          break;
          }
        case NOT_EQUAL:{
          jj_consume_token(NOT_EQUAL);
          ExpressaoAritOuLogica();
          break;
          }
        case LESS_THAN:{
          jj_consume_token(LESS_THAN);
          ExpressaoAritOuLogica();
          break;
          }
        case GREATER_THAN:{
          jj_consume_token(GREATER_THAN);
          ExpressaoAritOuLogica();
          break;
          }
        case LESS_THAN_EQUAL:{
          jj_consume_token(LESS_THAN_EQUAL);
          ExpressaoAritOuLogica();
          break;
          }
        case GREATER_THAN_EQUAL:{
          jj_consume_token(GREATER_THAN_EQUAL);
          ExpressaoAritOuLogica();
          break;
          }
        default:
          jj_la1[10] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "expressao_");
    }
}

  final public void ExpressaoAritOuLogica() throws ParseException {
    try {
      Termo2();
      MenorPrioridade();
    } catch (ParseException e) {
handleError(e, "ExpressaoAritOuLogica");
    }
}

  final public void MenorPrioridade() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case PLUS:
    case MINUS:
    case OR_LOGIC:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PLUS:{
        jj_consume_token(PLUS);
        Termo2();
        MenorPrioridade();
        break;
        }
      case MINUS:{
        jj_consume_token(MINUS);
        Termo2();
        MenorPrioridade();
        break;
        }
      case OR_LOGIC:{
        jj_consume_token(OR_LOGIC);
        Termo2();
        MenorPrioridade();
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[13] = jj_gen;
      ;
    }
}

  final public void Termo2() throws ParseException {
    Termo1();
    MediaPrioridade();
}

  final public void MediaPrioridade() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case MULTIPLY:{
      jj_consume_token(MULTIPLY);
      Termo1();
      MediaPrioridade();
      break;
      }
    case DIVIDE:{
      jj_consume_token(DIVIDE);
      Termo1();
      MediaPrioridade();
      break;
      }
    case MODULO:{
      jj_consume_token(MODULO);
      Termo1();
      MediaPrioridade();
      break;
      }
    case AND:{
      jj_consume_token(AND);
      Termo1();
      MediaPrioridade();
      break;
      }
    case OR_LOGIC:{
      jj_consume_token(OR_LOGIC);
      Termo1();
      MediaPrioridade();
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
}

  final public void Termo1() throws ParseException {
    Elemento();
    MaiorPrioridade();
}

  final public void MaiorPrioridade() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case POWER:{
      jj_consume_token(POWER);
      Elemento();
      MaiorPrioridade();
      break;
      }
    default:
      jj_la1[15] = jj_gen;
      ;
    }
}

  final public void Elemento() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        jj_consume_token(IDENTIFICADOR);
        Indice();
        break;
        }
      case INTEGER:{
        jj_consume_token(INTEGER);
        break;
        }
      case REAL:{
        jj_consume_token(REAL);
        break;
        }
      case LITERAL:{
        jj_consume_token(LITERAL);
        break;
        }
      case TRUE:{
        jj_consume_token(TRUE);
        break;
        }
      case UNTRUE:{
        jj_consume_token(UNTRUE);
        break;
        }
      case OPEN_PAREN:{
        jj_consume_token(OPEN_PAREN);
        Expressao();
        jj_consume_token(CLOSE_PAREN);
        break;
        }
      case NOT_LOGIC:{
        jj_consume_token(NOT_LOGIC);
        jj_consume_token(OPEN_PAREN);
        Expressao();
        jj_consume_token(CLOSE_PAREN);
        break;
        }
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "Elemento");
    }
}

  final public void Indice() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OPEN_BRACE:{
      jj_consume_token(OPEN_BRACE);
      Expressao();
      jj_consume_token(CLOSE_BRACE);
      break;
      }
    default:
      jj_la1[17] = jj_gen;
      ;
    }
}

  final public void DeclarationOrNothing() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DECLARATION:{
        jj_consume_token(DECLARATION);
        Declaration_Enumerado_E_ConstAndVars();
        break;
        }
      default:
        jj_la1[18] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "DeclarationOrNothing");
    }
}

  final public void Declaration_Enumerado_E_ConstAndVars() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case TYPE:{
        EnumeradoEouVars();
        break;
        }
      case CONSTANT:{
        ConstAndVarsOnly();
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "Declaration_Enumerado_E_ConstAndVars");
    }
}

  final public void declEnumerado() throws ParseException {
    jj_consume_token(IDENTIFICADOR);
    jj_consume_token(IS);
    jj_consume_token(OPEN_BRACE);
    ListaDeIdentificadores();
    jj_consume_token(CLOSE_BRACE);
    jj_consume_token(DOT);
}

  final public void EnumeradoEouVars() throws ParseException {
    try {
      jj_consume_token(TYPE);
      jj_consume_token(OPEN_BRACKET);
      declEnumerado();
      jj_consume_token(CLOSE_BRACKET);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DECLARATION:{
        jj_consume_token(DECLARATION);
        ConstAndVarsOnly();
        break;
        }
      default:
        jj_la1[20] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "EnumeradoEouVars");
    }
}

  final public void ConstAndVarsOnly() throws ParseException {
    try {
      jj_consume_token(CONSTANT);
      jj_consume_token(AND);
      jj_consume_token(VARIABLE);
      jj_consume_token(OPEN_BRACKET);
      ConstAndVarsOuVarsAndConstOuVarsOuConst();
      jj_consume_token(CLOSE_BRACKET);
    } catch (ParseException e) {
handleError(e, "ConstAndVarsOnly");
    }
}

  final public void ConstAndVarsOuVarsAndConstOuVarsOuConst() throws ParseException {
    try {
      jj_consume_token(AS);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CONSTANT:{
        jj_consume_token(CONSTANT);
        ConstAndVars();
        break;
        }
      case VARIABLE:{
        jj_consume_token(VARIABLE);
        VarsAndConst();
        break;
        }
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "ConstAndVarsOuVarsAndConstOuVarsOuConst");
    }
}

  final public void Vars() throws ParseException {
    ListaDeIdentificadores();
    jj_consume_token(IS);
    TipoVariavel();
    jj_consume_token(DOT);
    VarsAdicional();
}

  final public void VarsAdicional() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        Vars();
        break;
        }
      default:
        jj_la1[22] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "VarsAdicional");
    }
}

  final public void Const() throws ParseException {
    ListaDeIdentificadores();
    jj_consume_token(IS);
    ConstanteMatchValor();
    jj_consume_token(DOT);
    ConstAdicional();
}

  final public void ConstAdicional() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        Const();
        break;
        }
      default:
        jj_la1[23] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "ConstAdicional");
    }
}

  final public void ConstAndVars() throws ParseException {
    try {
      Const();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AS:{
        jj_consume_token(AS);
        jj_consume_token(VARIABLE);
        Vars();
        break;
        }
      default:
        jj_la1[24] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "ConstAndVars");
    }
}

  final public void VarsAndConst() throws ParseException {
    try {
      Vars();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AS:{
        jj_consume_token(AS);
        jj_consume_token(CONSTANT);
        Const();
        break;
        }
      default:
        jj_la1[25] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "VarsAndConst");
    }
}

  final public void ConstanteMatchValor() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INTEGER_DEF:{
        jj_consume_token(INTEGER_DEF);
        jj_consume_token(EQUALS);
        jj_consume_token(INTEGER);
        break;
        }
      case REAL_DEF:{
        jj_consume_token(REAL_DEF);
        jj_consume_token(EQUALS);
        jj_consume_token(REAL);
        break;
        }
      case LITERAL_DEF:{
        jj_consume_token(LITERAL_DEF);
        jj_consume_token(EQUALS);
        jj_consume_token(LITERAL);
        break;
        }
      default:
        jj_la1[26] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "TipoConstante");
    }
}

  final public void TipoVariavel() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INTEGER_DEF:{
        jj_consume_token(INTEGER_DEF);
        break;
        }
      case REAL_DEF:{
        jj_consume_token(REAL_DEF);
        break;
        }
      case LITERAL_DEF:{
        jj_consume_token(LITERAL_DEF);
        break;
        }
      case LOGIC_DEF:{
        jj_consume_token(LOGIC_DEF);
        break;
        }
      default:
        jj_la1[27] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException e) {
handleError(e, "TipoVariavel");
    }
}

  final public void ListaDeIdentificadores() throws ParseException {
    jj_consume_token(IDENTIFICADOR);
    ListaDeIdentificadoresAdicional();
}

  final public void ListaDeIdentificadoresAdicional() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMMA:{
        jj_consume_token(COMMA);
        ListaDeIdentificadores();
        break;
        }
      default:
        jj_la1[28] = jj_gen;
        ;
      }
    } catch (ParseException e) {
handleError(e, "ListaDeIdentificadoresAdicional");
    }
}

  /** Generated Token Manager. */
  public CompilerTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[29];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
	   jj_la1_init_0();
	   jj_la1_init_1();
	   jj_la1_init_2();
	}
	private static void jj_la1_init_0() {
	   jj_la1_0 = new int[] {0x20000,0x800,0x0,0x0,0x10008000,0x0,0x0,0x0,0x2d002000,0x2d002000,0x0,0x0,0x0,0x0,0x400000,0x0,0x800,0x0,0x40000,0x280000,0x40000,0xa00000,0x800,0x800,0x2000000,0x2000000,0xc0000000,0xc0000000,0x0,};
	}
	private static void jj_la1_init_1() {
	   jj_la1_1 = new int[] {0x0,0x4c0,0x4c0,0x80000000,0x0,0x300,0x200,0x100,0x0,0x0,0xfc0000,0xfc0000,0x2001800,0x2001800,0x2016000,0x8000,0x240007c0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x1,0x3,0x80000000,};
	}
	private static void jj_la1_init_2() {
	   jj_la1_2 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x400,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
	}

  /** Constructor with InputStream. */
  public Compiler(java.io.InputStream stream) {
	  this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Compiler(java.io.InputStream stream, String encoding) {
	 try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
	 token_source = new CompilerTokenManager(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
	  ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
	 try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
	 token_source.ReInit(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Compiler(java.io.Reader stream) {
	 jj_input_stream = new SimpleCharStream(stream, 1, 1);
	 token_source = new CompilerTokenManager(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
	if (jj_input_stream == null) {
	   jj_input_stream = new SimpleCharStream(stream, 1, 1);
	} else {
	   jj_input_stream.ReInit(stream, 1, 1);
	}
	if (token_source == null) {
 token_source = new CompilerTokenManager(jj_input_stream);
	}

	 token_source.ReInit(jj_input_stream);
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Compiler(CompilerTokenManager tm) {
	 token_source = tm;
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CompilerTokenManager tm) {
	 token_source = tm;
	 token = new Token();
	 jj_ntk = -1;
	 jj_gen = 0;
	 for (int i = 0; i < 29; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
	 Token oldToken;
	 if ((oldToken = token).next != null) token = token.next;
	 else token = token.next = token_source.getNextToken();
	 jj_ntk = -1;
	 if (token.kind == kind) {
	   jj_gen++;
	   return token;
	 }
	 token = oldToken;
	 jj_kind = kind;
	 throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
	 if (token.next != null) token = token.next;
	 else token = token.next = token_source.getNextToken();
	 jj_ntk = -1;
	 jj_gen++;
	 return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
	 Token t = token;
	 for (int i = 0; i < index; i++) {
	   if (t.next != null) t = t.next;
	   else t = t.next = token_source.getNextToken();
	 }
	 return t;
  }

  private int jj_ntk_f() {
	 if ((jj_nt=token.next) == null)
	   return (jj_ntk = (token.next=token_source.getNextToken()).kind);
	 else
	   return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
	 jj_expentries.clear();
	 boolean[] la1tokens = new boolean[94];
	 if (jj_kind >= 0) {
	   la1tokens[jj_kind] = true;
	   jj_kind = -1;
	 }
	 for (int i = 0; i < 29; i++) {
	   if (jj_la1[i] == jj_gen) {
		 for (int j = 0; j < 32; j++) {
		   if ((jj_la1_0[i] & (1<<j)) != 0) {
			 la1tokens[j] = true;
		   }
		   if ((jj_la1_1[i] & (1<<j)) != 0) {
			 la1tokens[32+j] = true;
		   }
		   if ((jj_la1_2[i] & (1<<j)) != 0) {
			 la1tokens[64+j] = true;
		   }
		 }
	   }
	 }
	 for (int i = 0; i < 94; i++) {
	   if (la1tokens[i]) {
		 jj_expentry = new int[1];
		 jj_expentry[0] = i;
		 jj_expentries.add(jj_expentry);
	   }
	 }
	 int[][] exptokseq = new int[jj_expentries.size()][];
	 for (int i = 0; i < jj_expentries.size(); i++) {
	   exptokseq[i] = jj_expentries.get(i);
	 }
	 return new ParseException(token, exptokseq, tokenImage);
  }

  private boolean trace_enabled;

/** Trace enabled. */
  final public boolean trace_enabled() {
	 return trace_enabled;
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
