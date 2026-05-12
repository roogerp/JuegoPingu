module JuegoPinguino {
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	opens Controlador to javafx.fxml, javafx.graphics;
	opens Vista to javafx.fxml, javafx.graphics;

	exports Controlador;
	exports Vista;
}
