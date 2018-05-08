package uk.ac.rdg.rhys.program1;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

public class Main extends Application {

	// Create and initialize objects needed for the GUI
	private StackPane stack = new StackPane();
	private BorderPane border = new BorderPane();
	private VBox topContainer = new VBox();

	VBox mainScreen = new VBox(30);
	Button back = new Button("Back");

	public void newCase() {

		VBox newCaseScreen = new VBox(10);
		GridPane grid = new GridPane();

		newCaseScreen.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);

		grid.setHgap(10);
		grid.setVgap(12);

		Button button1 = new Button("Fraud");
		Button button2 = new Button("Manslaughter");
		Button button3 = new Button("Substance abuse");

		button1.prefWidthProperty().bind(border.widthProperty().divide(3.5));
		button2.prefWidthProperty().bind(border.widthProperty().divide(3.5));
		button3.prefWidthProperty().bind(border.widthProperty().divide(3.5));

		button1.prefHeightProperty().bind(border.heightProperty().divide(5));
		button2.prefHeightProperty().bind(border.heightProperty().divide(5));
		button3.prefHeightProperty().bind(border.heightProperty().divide(5));

		Label titleLabel = new Label("Please specify the crime:");
		titleLabel.getStyleClass().add("label-header");

		grid.add(button2, 0, 0);
		grid.add(button3, 1, 0);

		newCaseScreen.getChildren().addAll(titleLabel, button1, grid);

		stack.getChildren().remove(0);
		stack.getChildren().add(newCaseScreen);

		button1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newFraud();
			}
		});

		button2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newManslaughter();
			}
		});

		button3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newSubstanceAbuse();
			}
		});

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				stack.getChildren().remove(0);
				stack.getChildren().add(mainScreen);
				border.setBottom(null);
			}
		});
	}

	public void newFraud() {
		border.setBottom(null);
		Fraud fraud = new Fraud(mainScreen);
		fraud.mainMenu(border, stack, back);
	}

	public void newManslaughter() {
		border.setBottom(null);
		Manslaughter manslaughter = new Manslaughter(mainScreen);
		manslaughter.mainMenu(border, stack, back);
	}

	public void newSubstanceAbuse() {
		border.setBottom(null);
		SubstanceAbuse substanceAbuse = new SubstanceAbuse(mainScreen);
		substanceAbuse.mainMenu(border, stack, back);
	}

	public void removeCase() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a case to remove");

		File currentDirectory = new File("cases/");
		fileChooser.setInitialDirectory(currentDirectory);
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Case files (.xml)", "*.xml"));

		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Case Removal");
			alert.setHeaderText("Case Removal");
			alert.setContentText("Are you sure you want to remove this case?\n(It will be permanently deleted)");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				try {
					selectedFile.delete();
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Case Removed");
					alert2.setHeaderText("Case Removed Successfully");
					alert2.setContentText("The case was removed successfully!");
					alert2.showAndWait();
				} catch (Exception ex) {
					System.out.println("Case removal failed");
				}
			} else {
				return;
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Set up MAIN MENU
		MenuBar mainMenu = new MenuBar();
		mainMenu.getStyleClass().add("menu");
		topContainer.getChildren().add(mainMenu);
		border.setTop(topContainer);

		// Create FILE sub-menu.
		Menu fileMenu = new Menu("File");
		MenuItem exit = new MenuItem("Exit");
		fileMenu.getItems().addAll(exit);

		Menu caseMenu = new Menu("Case");
		MenuItem newCase = new MenuItem("New case");
		MenuItem removeCase = new MenuItem("Remove case");
		caseMenu.getItems().addAll(newCase, removeCase);

		Menu helpMenu = new Menu("Help");
		MenuItem about = new MenuItem("About");
		helpMenu.getItems().add(about);

		mainMenu.getMenus().addAll(fileMenu, caseMenu, helpMenu);

		GridPane grid = new GridPane();

		mainScreen.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);

		grid.setHgap(10);
		grid.setVgap(12);

		Button newCaseButton = new Button("New case");
		Button removeCaseButton = new Button("Remove case");

		newCaseButton.prefWidthProperty()
				.bind(border.widthProperty().divide(3));
		removeCaseButton.prefWidthProperty().bind(
				border.widthProperty().divide(3));

		newCaseButton.prefHeightProperty().bind(
				border.heightProperty().divide(4));
		removeCaseButton.prefHeightProperty().bind(
				border.heightProperty().divide(4));

		Label titleLabel = new Label(
				"Welcome to the Greenvale Police Department Case Management Tool");
		Label descriptionLabel = new Label(
				"Please choose one of the options below to begin.");

		titleLabel.getStyleClass().add("label-header");

		grid.add(newCaseButton, 0, 0);
		grid.add(removeCaseButton, 1, 0);

		mainScreen.getChildren().addAll(titleLabel, descriptionLabel, grid);

		stack.getChildren().add(mainScreen);

		border.setCenter(stack);

		newCaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				border.setBottom(back);
				newCase();
			}
		});

		removeCaseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				border.setBottom(back);
				removeCase();
			}
		});

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
			}
		});

		newCase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				border.setBottom(back);
				newCase();
			}
		});

		removeCase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				border.setBottom(back);
				removeCase();
			}
		});

		about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Dialog<ButtonType> dialog = new Dialog<ButtonType>();
				dialog.setTitle("About");
				dialog.setHeaderText("About");

				ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(okButton);

				Label label = new Label(
						"Greenvale Police Department Case Management Tool v1.0\n\nThis tool was created for SE2FD11 Advanced Databases by Rhys Streefland.\n\nThis tool is designed to allow the user to add or remove case details for\nthree different types of crime (fraud, manslaughter, and substance abuse)\nThis tool stores each case in a separate XML file. \n\nFor detailed operating instructions please see the report.");
				dialog.getDialogPane().setContent(label);
				dialog.showAndWait();
			}
		});

		// Make the scene and apply CSS
		final Scene scene = new Scene(border, 800, 700);
		scene.getStylesheets().add(
				Main.class.getResource("style.css").toExternalForm());
		border.getStyleClass().add("background");

		// Setup the Stage
		primaryStage.setTitle("Greenvale Police Department");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(800);
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
