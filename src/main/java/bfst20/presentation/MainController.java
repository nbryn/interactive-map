package bfst20.presentation;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class MainController {

    Model model;

    @FXML
    private MenuItem openFile;

    @FXML
    private VBox vbox;

    public MainController() {
        this.model = new Model();
    }

    @FXML
    public void initialize() {
        
        Canvas canvas = new Canvas(1270, 720);
        
        View view = new View(canvas);

        vbox.getChildren().add(canvas);

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("samsoe.osm").getFile());
            Parser parser = Parser.getInstance();
            parser.parseOSMFile(file);
            view.initializeData();
        } catch (Exception err) {
        }

        canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY());
        });
    }

    public static void main(String[] args) {
        Launcher.main(args);

    }

    public void load(ActionEvent actionEvent) throws IOException, XMLStreamException, FactoryConfigurationError {
        FileChooser chooser = new FileChooser();
    }
}