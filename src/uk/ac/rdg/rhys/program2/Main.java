package uk.ac.rdg.rhys.program2;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

/**
 * <h1>Main.java</h1>
 * <p>
 * This class contains the majority of the code used to create the JavaFX GUI.
 * Serves as the starting point of the program because every function of the
 * program is initated by this class
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Main extends Application {

	// Create objects for GUI
	private StackPane stack = new StackPane();
	private BorderPane border = new BorderPane();
	private VBox topContainer = new VBox();
	private VBox mainScreen = new VBox(30);

	/**
	 * Initiates the process of generating a report by allowing the user to
	 * select the case to import and the name of the report. Uses the Importer
	 * class and the Generator class respectively to import from the XML file
	 * and generate the PDF report.
	 */
	private void generateReport() {
		// Set the current directory - create if it doesn't already exist
		File directory = new File("cases/reports/");
		directory.mkdirs();

		// Create a FileChooser object and set it's properties
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a case file for the report");
		File currentDirectory = new File("cases/");
		fileChooser.setInitialDirectory(currentDirectory);
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Case files (.xml)", "*.xml"));

		// Create an object for the file selected by the FileChooser
		File selectedFile = fileChooser.showOpenDialog(null);

		// If the user has selected a file
		if (selectedFile != null) {

			// Use the importer class to import the data from the selected file
			Importer importer = new Importer(selectedFile.getAbsolutePath());

			// Create a dialog asking the user if they'd like to name the file
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Report name");
			alert.setHeaderText("Manually name the report?");
			alert.setContentText("If you select cancel then the file will be named\nautomatically");
			Optional<ButtonType> result = alert.showAndWait();

			// If the user selects ok
			if (result.get() == ButtonType.OK) {
				// Open a file chooser allowing the user to specify the file
				// name and path
				FileChooser fileChooser2 = new FileChooser();
				fileChooser2.setTitle("Choose report save name/location");
				fileChooser2.getExtensionFilters()
						.addAll(new ExtensionFilter("Case report files (.pdf)",
								"*.pdf"));

				// Set the initial directory
				File currentDirectory2 = new File("cases/reports/");
				fileChooser2.setInitialDirectory(currentDirectory2);

				File selectedFile2 = fileChooser2.showSaveDialog(null);

				// If the user has selected a file
				if (selectedFile2 != null) {
					// Generate the report using the user selected file name
					importer.generateReport(selectedFile2.getAbsolutePath());
				}
			} else {
				// Generate the report using an automatically generated file
				// name
				importer.generateReport();
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Align everything centrally
		mainScreen.setAlignment(Pos.CENTER);

		// Create and set up the Main menu
		MenuBar mainMenu = new MenuBar();
		mainMenu.getStyleClass().add("menu");
		topContainer.getChildren().add(mainMenu);
		border.setTop(topContainer);

		// Create and set up the File sub-menu
		Menu fileMenu = new Menu("File");
		MenuItem generateReport = new MenuItem("Generate Report");
		MenuItem exit = new MenuItem("Exit");
		fileMenu.getItems().addAll(generateReport, exit);

		// Create and set up the Help sub-menu
		Menu helpMenu = new Menu("Help");
		MenuItem about = new MenuItem("About");
		helpMenu.getItems().add(about);

		// Add the file and help sub-menus to the main menu
		mainMenu.getMenus().addAll(fileMenu, helpMenu);

		// Create the title and description labels
		Label titleLabel = new Label(
				"Welcome to the Court Report Management Tool");
		titleLabel.getStyleClass().add("label-header");
		Label descriptionLabel = new Label(
				"Please choose the option below to begin.");

		// Create the Generate Report button and set its size
		Button generateReportButton = new Button("Generate Report");
		generateReportButton.prefWidthProperty().bind(
				border.widthProperty().divide(3));
		generateReportButton.prefHeightProperty().bind(
				border.heightProperty().divide(4));

		// Add everything to the mainScreen VBox
		mainScreen.getChildren().addAll(titleLabel, descriptionLabel,
				generateReportButton);

		// Add the mainScreen VBox to the StackPane
		stack.getChildren().add(mainScreen);

		// Set the stack as the center of the BorderPane
		border.setCenter(stack);

		// When the generate report button is pressed
		generateReportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				generateReport();
			}
		});

		// When the generateReport menu button is pressed
		generateReport.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				generateReport();
			}
		});

		// When the exit menu button is pressed
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
			}
		});

		// When the about menu button is pressed
		about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Create a dialog to display the 'about' information
				Dialog<ButtonType> dialog = new Dialog<ButtonType>();
				dialog.setTitle("About");
				dialog.setHeaderText("About");

				ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(okButton);

				Label label = new Label(
						"Court Report Management Tool v1.0\n\nThis tool was created for SE2FD11 Advanced Databases by Rhys Streefland.\n\nThis tool is designed to generate a PDF report based on an XML document\ncontaining crime data. This XML file should have been generated by the\nCourt Report Management tool which is supplied with this tool.\n\nFor detailed operating instructions please see the report.");
				dialog.getDialogPane().setContent(label);
				dialog.showAndWait();
			}
		});

		// Create the scene and apply CSS
		final Scene scene = new Scene(border, 500, 400);
		scene.getStylesheets().add(
				Main.class.getResource("style.css").toExternalForm());
		border.getStyleClass().add("background");

		// Setup the Stage
		primaryStage.setTitle("Court Report Management Tool");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(500);
		primaryStage.show();

	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		String java = System.getProperty("java.specification.version");
		double version = Double.valueOf(java);
		if (version < 1.8) {
			JOptionPane
					.showMessageDialog(
							null,
							"Java 8 is required to run this application!\nPlease install JRE 8 and try again...",
							"Error", JOptionPane.ERROR_MESSAGE);
			Platform.exit();
		}
		launch(args);
	}
}
