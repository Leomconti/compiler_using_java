module com.example.compiler_testes {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;

    requires org.kordamp.ikonli.javafx;

    opens com.example.compiler_testes to javafx.fxml;
    exports com.example.compiler_testes;
}