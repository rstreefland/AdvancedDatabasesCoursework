package uk.ac.rdg.rhys.program1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.rdg.rhys.program1.Accused.Verdict;
import uk.ac.rdg.rhys.program1.Crime.CourtType;
import uk.ac.rdg.rhys.program1.Crime.CrimeType;
import uk.ac.rdg.rhys.program1.Evidence.Supports;
import uk.ac.rdg.rhys.program1.Evidence.Type;
import uk.ac.rdg.rhys.program1.Specific.ManslaughterLossControl;
import uk.ac.rdg.rhys.program1.Specific.ManslaughterSeriousness;
import uk.ac.rdg.rhys.program1.Specific.ManslaughterType;
import uk.ac.rdg.rhys.program1.Specific.VehicleInvolved;
import uk.ac.rdg.rhys.program1.Witness.WitnessType;

public class Manslaughter {

	// Declare class objects
	private VBox localMainScreen;
	private HBox mainMenuBar = new HBox();
	private Button save = new Button("Save");
	private Button discard = new Button("Discard");
	private Button add = new Button("Add");
	private DatePicker courtDateValue = new DatePicker();

	private Crime crime;

	public Manslaughter(VBox mainScreen) {
		File folder = new File("cases/");
		folder.mkdirs();
		
		// Get the number of XML files in the current directory
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
		int caseNumber = listOfFiles.length + 1;

		// Instantiate the crime object
		crime = new Crime(CrimeType.MANSLAUGHTER, caseNumber);

		// Make the mainScreen available locally
		localMainScreen = mainScreen;

		// Add discard and save buttons to the mainMenuBar
		mainMenuBar.getChildren().addAll(discard, save);
	}

	public void mainMenu(BorderPane border, StackPane stack, Button back) {

		// Create a new screen and grid
		VBox newManslaughterScreen = new VBox(10);
		GridPane grid = new GridPane();

		// Align everything centrally
		newManslaughterScreen.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);

		// Set the gap between each grid object
		grid.setHgap(10);
		grid.setVgap(12);

		// Create buttons
		Button accusedButton = new Button("Acccused");
		Button victimsButton = new Button("Victims");
		Button witnessesButton = new Button("Witnesses");
		Button evidenceButton = new Button("Evidence");
		Button specificButton = new Button("Crime Specific");

		// Set button widths
		accusedButton.prefWidthProperty()
				.bind(border.widthProperty().divide(4));
		victimsButton.prefWidthProperty()
				.bind(border.widthProperty().divide(4));
		witnessesButton.prefWidthProperty().bind(
				border.widthProperty().divide(4));
		evidenceButton.prefWidthProperty().bind(
				border.widthProperty().divide(4));
		specificButton.prefWidthProperty().bind(
				border.widthProperty().divide(4));

		// Set button heights
		accusedButton.prefHeightProperty().bind(
				border.heightProperty().divide(8));
		victimsButton.prefHeightProperty().bind(
				border.heightProperty().divide(8));
		witnessesButton.prefHeightProperty().bind(
				border.heightProperty().divide(8));
		evidenceButton.prefHeightProperty().bind(
				border.heightProperty().divide(8));
		specificButton.prefHeightProperty().bind(
				border.heightProperty().divide(8));

		// Create labels
		Label titleLabel = new Label("Modify case information");
		Label caseNumberLabel = new Label("Case number:");
		Label crimeTypeLabel = new Label("Crime type:");
		Label courtNameLabel = new Label("Court name: *");
		Label courtTypeLabel = new Label("Court type: *");
		Label courtDateLabel = new Label("Court date: *");
		Label caseNumberValueLabel = new Label("" + crime.getCaseNumber());
		Label crimeTypeValueLabel = new Label(crime.getCrimeType().toString());

		// Set label styles
		titleLabel.getStyleClass().add("label-header");
		caseNumberLabel.getStyleClass().add("label-bright");
		crimeTypeLabel.getStyleClass().add("label-bright");
		courtNameLabel.getStyleClass().add("label-bright");
		courtTypeLabel.getStyleClass().add("label-bright");
		courtDateLabel.getStyleClass().add("label-bright");

		// Create TextField for the court name
		TextField courtNameValue = new TextField(crime.getCourtName());
		ComboBox<CourtType> courtTypeValue = new ComboBox<>();
		courtTypeValue.getItems().setAll(CourtType.values());
		courtTypeValue.setValue(crime.getCourtType());

		save.disableProperty().bind(
				Bindings.equal(courtTypeValue.valueProperty(), CourtType.BLANK)
						.or(Bindings.isNull(courtDateValue.valueProperty()))
						.or(Bindings.isEmpty(courtNameValue.textProperty())));

		// Add labels, buttons and text field to the grid
		grid.add(caseNumberLabel, 0, 0);
		grid.add(caseNumberValueLabel, 1, 0);
		grid.add(crimeTypeLabel, 0, 1);
		grid.add(crimeTypeValueLabel, 1, 1);
		grid.add(courtNameLabel, 0, 2);
		grid.add(courtNameValue, 1, 2);
		grid.add(courtTypeLabel, 0, 3);
		grid.add(courtTypeValue, 1, 3);
		grid.add(courtDateLabel, 0, 4);
		grid.add(courtDateValue, 1, 4);
		grid.add(accusedButton, 0, 5);
		grid.add(victimsButton, 1, 5);
		grid.add(witnessesButton, 0, 6);
		grid.add(evidenceButton, 1, 6);

		// Add title and grid to the screen
		newManslaughterScreen.getChildren().addAll(titleLabel, grid,
				specificButton);

		// Remove previous screen from stack and add the current one
		stack.getChildren().remove(0);
		stack.getChildren().add(newManslaughterScreen);

		// Add mainMenuBar to the bottom of the screen
		border.setBottom(mainMenuBar);

		// When the courtNameValue text field is updated - Save the new value
		courtNameValue.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				crime.setCourtName(courtNameValue.getText());
			}
		});

		// When courtTypeValue field is updated - Save the new value
		courtTypeValue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				crime.setCourtType(courtTypeValue.getValue());
			}
		});

		// When courtDateValue field is updated - Save the new value
		courtDateValue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Split the Court Date into separate year, month, and day
				// integers
				String dobString = String.valueOf(courtDateValue.getValue());
				String split[] = dobString.split("-");
				int year = Integer.parseInt(split[0].toString());
				int month = Integer.parseInt(split[1]);
				int day = Integer.parseInt(split[2]);
				crime.setYear(year);
				crime.setMonth(month);
				crime.setDay(day);
			}
		});

		// When the 'Accused' button is pressed
		accusedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				accused(border, stack, back);
			}
		});

		// When the 'Victims' button is pressed
		victimsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				victims(border, stack, back);
			}
		});

		// When the 'Witnesses' button is pressed
		witnessesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				witnesses(border, stack, back);
			}
		});

		// When the 'Evidence' button is pressed
		evidenceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				evidence(border, stack, back);
			}
		});

		// When the 'Specific' button is pressed
		specificButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				specific(border, stack, back);
			}
		});

		// When the 'Discard' button is pressed
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Discard Case");
				alert.setHeaderText("Discard Case");
				alert.setContentText("Are you sure that you want to discard this case?\n(It can not be recovered if it has been discarded)");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Remove the current screen from the stack
					stack.getChildren().remove(0);
					// Add the main screen to the stack
					stack.getChildren().add(localMainScreen);
					// Remove any buttons from the bottom of the screen
					border.setBottom(null);
				}
			}
		});

		// When the 'Save' button is pressed
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				String date = crime.getDay() + "-" + crime.getMonth() + "-"
						+ crime.getYear();
				String fileName = crime.getCaseNumber() + "_"
						+ crime.getCrimeType() + "_" + date + ".xml";
				String filePath = "cases/" + fileName;
				@SuppressWarnings("unused")
				Exporter export = new Exporter(filePath, crime);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Case Saved");
				alert.setHeaderText("Case Saved Successfully");
				alert.setContentText("The case was saved successfully!");

				alert.showAndWait();

				// Remove the current screen from the stack
				stack.getChildren().remove(0);
				// Add the main screen to the stack
				stack.getChildren().add(localMainScreen);
				// Remove any buttons from the bottom of the screen
				border.setBottom(null);
			}
		});
	}

	private void accused(BorderPane border, StackPane stack, Button back) {
		// Add back button to bottom of screen
		border.setBottom(back);

		// Create a new screen and align everything centrally
		VBox accusedScreen = new VBox(10);
		accusedScreen.setAlignment(Pos.CENTER);

		// Create an observable ArrayList from the Accused object
		final ObservableList<Accused> accusedObservable = FXCollections
				.observableArrayList(crime.getAccused());

		// Create a new table
		TableView<Accused> table = new TableView<Accused>();

		// Add ID column
		TableColumn<Accused, String> idCol = new TableColumn<Accused, String>(
				"ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Accused, String>(
				"id"));
		table.getColumns().add(idCol);

		// Add Title column
		TableColumn<Accused, String> titleCol = new TableColumn<Accused, String>(
				"Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Accused, String>(
				"title"));
		table.getColumns().add(titleCol);

		// Add Forename column
		TableColumn<Accused, String> forenameCol = new TableColumn<Accused, String>(
				"Forename");
		forenameCol
				.setCellValueFactory(new PropertyValueFactory<Accused, String>(
						"forename"));
		table.getColumns().add(forenameCol);

		// Add Surname column
		TableColumn<Accused, String> surnameCol = new TableColumn<Accused, String>(
				"Surname");
		surnameCol
				.setCellValueFactory(new PropertyValueFactory<Accused, String>(
						"surname"));
		table.getColumns().add(surnameCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(accusedObservable);
		table.maxWidthProperty().bind(border.widthProperty().divide(1.2));
		table.prefHeightProperty().bind(border.heightProperty().divide(1.5));

		// Create screen title and apply style
		Label title = new Label("Accused");
		title.getStyleClass().add("label-header");

		// Create HBox for buttons
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);

		// Create button
		Button newAccused = new Button("+");
		Button removeAccused = new Button("-");

		// Add buttons to HBox and HBox to current screen
		buttons.getChildren().addAll(newAccused, removeAccused);
		accusedScreen.getChildren().addAll(title, table, buttons);

		// Remove previous screen from stack and add new one
		stack.getChildren().remove(0);
		stack.getChildren().add(accusedScreen);

		// If '+' button is pressed
		newAccused.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Remove any buttons from the bottom of the screen
				border.setBottom(null);

				addAccused(border, stack, back);
			}
		});

		// If '-' button is pressed
		removeAccused.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (table.getItems().size() != 0) {
					// Remove any buttons from the bottom of the screen
					border.setBottom(null);

					// Get the current table selection
					TableViewSelectionModel<Accused> selection = table
							.getSelectionModel();

					// Delete the relevant 'Accused' object based on the
					// selection
					crime.getAccused().remove(selection.getFocusedIndex());

					accused(border, stack, back);
				}
			}
		});

		// If 'Back' button is pressed
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainMenu(border, stack, back);

				// Add the mainMenuBar to the bottom of the screen
				border.setBottom(mainMenuBar);
			}
		});
	}

	private void addAccused(BorderPane border, StackPane stack, Button back) {

		// Create new screen and align everything centrally
		VBox accusedScreen = new VBox(10);
		accusedScreen.setAlignment(Pos.CENTER);

		// Create grid and set its properties
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(6);

		// Create HBox and display it at the bottom of the screen
		HBox hBox = new HBox();
		border.setBottom(hBox);

		// Create local discard button and add discard and add buttons to hBox
		Button discard = new Button("Discard");
		hBox.getChildren().addAll(discard, add);

		// Create form elements
		TextField title = new TextField("");
		TextField forename = new TextField("");
		TextField surname = new TextField("");
		DatePicker dob = new DatePicker();
		TextField firstLine = new TextField("");
		TextField secondLine = new TextField("");
		TextField postcode = new TextField("");
		TextField countryOrigin = new TextField("");
		TextField lawyerTitle = new TextField("");
		TextField lawyerForename = new TextField("");
		TextField lawyerSurname = new TextField("");
		TextArea involvement = new TextArea("");
		TextField convictionTitle = new TextField("");
		DatePicker convictionDate = new DatePicker();
		TextArea convictionDescription = new TextArea("");
		TextField sentenceLength = new TextField("");
		TextArea sentenceDescription = new TextArea("");
		TextField fine = new TextField("");

		// Set prompts for form elements
		title.setPromptText("title");
		forename.setPromptText("forename");
		surname.setPromptText("surname");
		firstLine.setPromptText("first line");
		secondLine.setPromptText("second line");
		postcode.setPromptText("postcode");
		countryOrigin.setPromptText("country");
		lawyerTitle.setPromptText("title");
		lawyerForename.setPromptText("forename");
		lawyerSurname.setPromptText("surname");
		involvement.setPromptText("involvement");
		ComboBox<Verdict> verdict = new ComboBox<>();
		verdict.getItems().setAll(Verdict.values());
		convictionTitle.setPromptText("title");
		convictionDescription.setPromptText("description");
		sentenceLength.setPromptText("length");
		sentenceDescription.setPromptText("description");
		fine.setPromptText("fine");

		// Set preferred sizes for TextArea elements
		involvement.setPrefWidth(5);
		involvement.setPrefHeight(1);
		convictionDescription.setPrefWidth(5);
		convictionDescription.setPrefHeight(1);
		sentenceDescription.setPrefWidth(5);
		sentenceDescription.setPrefHeight(1);

		// Create form labels
		Label personalLabel = new Label("Personal");
		Label locationLabel = new Label("Location");
		Label lawyerLabel = new Label("Lawyer");
		Label convictionsLabel = new Label("Previous Convictions");
		Label crimeLabel = new Label("Crime");
		Label sentenceLabel = new Label("Sentence");

		// Set label styles
		personalLabel.getStyleClass().add("label-bright");
		locationLabel.getStyleClass().add("label-bright");
		lawyerLabel.getStyleClass().add("label-bright");
		convictionsLabel.getStyleClass().add("label-bright");
		crimeLabel.getStyleClass().add("label-bright");
		sentenceLabel.getStyleClass().add("label-bright");

		// Allow only numbers to be entered
		fine.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// If any of these fields are empty - disable the add button
		add.disableProperty().bind(
				Bindings.isEmpty(title.textProperty())
						.or(Bindings.isEmpty(forename.textProperty()))
						.or(Bindings.isNull(dob.valueProperty()))
						.or(Bindings.isEmpty(firstLine.textProperty()))
						.or(Bindings.isEmpty(secondLine.textProperty()))
						.or(Bindings.isEmpty(postcode.textProperty()))
						.or(Bindings.isEmpty(countryOrigin.textProperty()))
						.or(Bindings.isEmpty(lawyerTitle.textProperty()))
						.or(Bindings.isEmpty(lawyerForename.textProperty()))
						.or(Bindings.isEmpty(lawyerSurname.textProperty()))
						.or(Bindings.isEmpty(involvement.textProperty())));

		/* Add form elements to grid */
		// Personal section
		grid.add(personalLabel, 0, 0);
		grid.add(new Label("Title: *"), 0, 1);
		grid.add(title, 1, 1);
		grid.add(new Label("Forename: *"), 2, 1);
		grid.add(forename, 3, 1);
		grid.add(new Label("Surname: *"), 0, 2);
		grid.add(surname, 1, 2);
		grid.add(new Label("Date of birth: *"), 2, 2);
		grid.add(dob, 3, 2);

		// Location section
		grid.add(locationLabel, 0, 3);
		grid.add(new Label("First line: *"), 0, 4);
		grid.add(firstLine, 1, 4);
		grid.add(new Label("Second line: *"), 2, 4);
		grid.add(secondLine, 3, 4);
		grid.add(new Label("Postcode: *"), 0, 5);
		grid.add(postcode, 1, 5);
		grid.add(new Label("Country of origin: *"), 2, 5);
		grid.add(countryOrigin, 3, 5);

		// Lawyer section
		grid.add(lawyerLabel, 0, 6);
		grid.add(new Label("Title: *"), 0, 7);
		grid.add(lawyerTitle, 1, 7);
		grid.add(new Label("Forename: *"), 2, 7);
		grid.add(lawyerForename, 3, 7);
		grid.add(new Label("Surname: *"), 0, 8);
		grid.add(lawyerSurname, 1, 8);

		// Previous convictions section
		grid.add(convictionsLabel, 0, 9);
		grid.add(new Label("Title:"), 0, 10);
		grid.add(convictionTitle, 1, 10);
		grid.add(new Label("Date:"), 2, 10);
		grid.add(convictionDate, 3, 10);
		grid.add(new Label("Description:"), 0, 11);
		grid.add(convictionDescription, 1, 11);

		// Crime section
		grid.add(crimeLabel, 0, 12);
		grid.add(new Label("Involvement: *"), 0, 13);
		grid.add(involvement, 1, 13);
		grid.add(new Label("Verdict:"), 2, 13);
		grid.add(verdict, 3, 13);

		// Sentence section
		grid.add(sentenceLabel, 0, 14);
		grid.add(new Label("Length:"), 0, 15);
		grid.add(sentenceLength, 1, 15);
		grid.add(new Label("Description:"), 2, 15);
		grid.add(sentenceDescription, 3, 15);
		grid.add(new Label("Fine:"), 0, 16);
		grid.add(fine, 1, 16);

		// Add grid to the current screen
		accusedScreen.getChildren().add(grid);

		// Remove the current screen from the stack and add the current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(accusedScreen);

		// When the add button is pressed
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Temporary variables to represent the current objects
				int i = crime.getAccused().size();

				// Add a new 'Accused' object to the ArrayList
				crime.getAccused().add(new Accused());

				// Create a local copy of the object
				Accused localAccused = new Accused();
				localAccused = crime.getAccused().get(i);

				// Set object properties based on input field values
				localAccused.setId(i + 1);
				localAccused.setTitle(title.getText());
				localAccused.setForename(forename.getText());
				localAccused.setSurname(surname.getText());
				localAccused.setFirstLine(firstLine.getText());
				localAccused.setSecondLine(secondLine.getText());
				localAccused.setPostCode(postcode.getText());
				localAccused.setCountryOrigin(countryOrigin.getText());
				localAccused.setLawyerTitle(lawyerTitle.getText());
				localAccused.setLawyerForename(lawyerForename.getText());
				localAccused.setLawyerSurname(lawyerSurname.getText());
				localAccused.setInvolvement(involvement.getText());
				localAccused.setPreviousConvictionTitle(convictionTitle
						.getText());
				localAccused
						.setPreviousConvictionDescription(convictionDescription
								.getText());
				localAccused.setSentenceLength(sentenceLength.getText());
				localAccused.setSentenceDescription(sentenceDescription
						.getText());

				// Don't set the verdict value to null
				if (verdict.getValue() != null) {
					localAccused.setVerdict(verdict.getValue());
				}

				// Prevent an error if the fine field is empty
				try {
					localAccused.setFine(Integer.parseInt(fine.getText()));
				} catch (Exception ex) {
					localAccused.setFine(0);
				}

				// Split the DOB into separate year, month, and day integers
				String dobString = String.valueOf(dob.getValue());
				String split[] = dobString.split("-");
				int year = Integer.parseInt(split[0].toString());
				int month = Integer.parseInt(split[1]);
				int day = Integer.parseInt(split[2]);
				localAccused.setYear(year);
				localAccused.setMonth(month);
				localAccused.setDay(day);

				// Prevent an error if the conviction date is empty
				try {
					// Split the conviction date into separate year, month, and
					// day integers
					String convictionString = String.valueOf(convictionDate
							.getValue());
					String splitConviction[] = convictionString.split("-");
					int yearConviction = Integer.parseInt(splitConviction[0]
							.toString());
					int monthConviction = Integer.parseInt(splitConviction[1]);
					int dayConviction = Integer.parseInt(splitConviction[2]);
					localAccused.setPreviousConvictionYear(yearConviction);
					localAccused.setPreviousConvictionMonth(monthConviction);
					localAccused.setPreviousConvictionDay(dayConviction);
				} catch (Exception ex) {
					localAccused.setPreviousConvictionYear(0);
					localAccused.setPreviousConvictionMonth(0);
					localAccused.setPreviousConvictionDay(0);
				}

				// Copy localAccused object into the ArrayList
				crime.getAccused().set(i, localAccused);

				accused(border, stack, back);
			}
		});

		// If discard button is pressed
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				accused(border, stack, back);
			}
		});
	}

	private void victims(BorderPane border, StackPane stack, Button back) {
		border.setBottom(back);

		// Create new screen and align everything centrally
		VBox victimScreen = new VBox(10);
		victimScreen.setAlignment(Pos.CENTER);

		// Create an observable list based on the values of the 'Victim' object
		final ObservableList<Victim> victimObservable = FXCollections
				.observableArrayList(crime.getVictim());

		// Create table
		TableView<Victim> table = new TableView<Victim>();

		// Create ID column
		TableColumn<Victim, String> idCol = new TableColumn<Victim, String>(
				"ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Victim, String>("id"));
		table.getColumns().add(idCol);

		// Create Title column
		TableColumn<Victim, String> titleCol = new TableColumn<Victim, String>(
				"Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Victim, String>(
				"title"));
		table.getColumns().add(titleCol);

		// Create Forename column
		TableColumn<Victim, String> forenameCol = new TableColumn<Victim, String>(
				"Forename");
		forenameCol
				.setCellValueFactory(new PropertyValueFactory<Victim, String>(
						"forename"));
		table.getColumns().add(forenameCol);

		// Create surname column
		TableColumn<Victim, String> surnameCol = new TableColumn<Victim, String>(
				"Surname");
		surnameCol
				.setCellValueFactory(new PropertyValueFactory<Victim, String>(
						"surname"));
		table.getColumns().add(surnameCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(victimObservable);
		table.maxWidthProperty().bind(border.widthProperty().divide(1.2));
		table.prefHeightProperty().bind(border.heightProperty().divide(1.5));

		// Create screen title
		Label title = new Label("Victims");
		title.getStyleClass().add("label-header");

		// Create HBox for buttons and align centrally
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);

		// Create buttons
		Button newVictim = new Button("+");
		Button removeVictim = new Button("-");

		// Add buttons to HBox
		buttons.getChildren().addAll(newVictim, removeVictim);

		// Add label, table, and buttons to screen
		victimScreen.getChildren().addAll(title, table, buttons);

		// Remove previous screen from stack and add new screen
		stack.getChildren().remove(0);
		stack.getChildren().add(victimScreen);

		// If the '+' button is pressed
		newVictim.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Remove any buttons from the bottom of the screen
				border.setBottom(null);

				addVictim(border, stack, back);
			}
		});

		// If the '-' button is pressed
		removeVictim.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (table.getItems().size() != 0) {
					// Remove any buttons from the bottom of the screen
					border.setBottom(null);

					// Get the current table selection
					TableViewSelectionModel<Victim> selection = table
							.getSelectionModel();

					// Delete the relevant 'Victim' object based on the
					// selection
					crime.getVictim().remove(selection.getFocusedIndex());

					victims(border, stack, back);
				}
			}
		});

		// If the back button is pressed
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainMenu(border, stack, back);

				// Add the mainMenuBar to the bottom of the screen
				border.setBottom(mainMenuBar);
			}
		});
	}

	private void addVictim(BorderPane border, StackPane stack, Button back) {

		// Create new screen
		VBox victimScreen = new VBox(10);
		victimScreen.setAlignment(Pos.CENTER);

		// Create new grid and set its properties
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Create new HBox and display it at the bottom of the screen
		HBox hBox = new HBox();
		border.setBottom(hBox);

		// Create local discard button and add discard and add buttons to hBox
		Button discard = new Button("Discard");
		hBox.getChildren().addAll(discard, add);

		// Create form elements
		TextField title = new TextField("");
		TextField forename = new TextField("");
		TextField surname = new TextField("");
		DatePicker dob = new DatePicker();
		TextField firstLine = new TextField("");
		TextField secondLine = new TextField("");
		TextField postcode = new TextField("");
		TextField countryOrigin = new TextField("");
		TextField lawyerTitle = new TextField("");
		TextField lawyerForename = new TextField("");
		TextField lawyerSurname = new TextField("");
		TextField compensation = new TextField("");

		// Set form field prompts
		title.setPromptText("title");
		forename.setPromptText("forename");
		surname.setPromptText("surname");
		firstLine.setPromptText("first line");
		secondLine.setPromptText("second line");
		postcode.setPromptText("postcode");
		countryOrigin.setPromptText("country");
		lawyerTitle.setPromptText("title");
		lawyerForename.setPromptText("forename");
		lawyerSurname.setPromptText("surname");
		compensation.setPromptText("compensation");

		// Create form labels
		Label personalLabel = new Label("Personal");
		Label locationLabel = new Label("Location");
		Label lawyerLabel = new Label("Lawyer");
		Label compensationLabel = new Label("Compensation");

		// Set label styles
		personalLabel.getStyleClass().add("label-bright");
		locationLabel.getStyleClass().add("label-bright");
		lawyerLabel.getStyleClass().add("label-bright");
		compensationLabel.getStyleClass().add("label-bright");

		// Allow only numbers to be entered
		compensation.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// If any of these fields are empty - disable the add button
		add.disableProperty().bind(
				Bindings.isEmpty(title.textProperty())
						.or(Bindings.isEmpty(forename.textProperty()))
						.or(Bindings.isNull(dob.valueProperty()))
						.or(Bindings.isEmpty(firstLine.textProperty()))
						.or(Bindings.isEmpty(secondLine.textProperty()))
						.or(Bindings.isEmpty(postcode.textProperty()))
						.or(Bindings.isEmpty(countryOrigin.textProperty()))
						.or(Bindings.isEmpty(lawyerTitle.textProperty()))
						.or(Bindings.isEmpty(lawyerForename.textProperty()))
						.or(Bindings.isEmpty(lawyerSurname.textProperty())));

		/* Add form elements to grid */
		// Personal section
		grid.add(personalLabel, 0, 0);
		grid.add(new Label("Title: *"), 0, 1);
		grid.add(title, 1, 1);
		grid.add(new Label("Forename: *"), 2, 1);
		grid.add(forename, 3, 1);
		grid.add(new Label("Surname: *"), 0, 2);
		grid.add(surname, 1, 2);
		grid.add(new Label("Date of birth: *"), 2, 2);
		grid.add(dob, 3, 2);

		// Location section
		grid.add(locationLabel, 0, 3);
		grid.add(new Label("First line: *"), 0, 4);
		grid.add(firstLine, 1, 4);
		grid.add(new Label("Second line: *"), 2, 4);
		grid.add(secondLine, 3, 4);
		grid.add(new Label("Postcode: *"), 0, 5);
		grid.add(postcode, 1, 5);
		grid.add(new Label("Country of origin: *"), 2, 5);
		grid.add(countryOrigin, 3, 5);

		// Lawyer section
		grid.add(lawyerLabel, 0, 6);
		grid.add(new Label("Title: *"), 0, 7);
		grid.add(lawyerTitle, 1, 7);
		grid.add(new Label("Forename: *"), 2, 7);
		grid.add(lawyerForename, 3, 7);
		grid.add(new Label("Surname: *"), 0, 8);
		grid.add(lawyerSurname, 1, 8);

		// Compensation section
		grid.add(compensationLabel, 0, 9);
		grid.add(new Label("Compensation amount:"), 0, 10);
		grid.add(compensation, 1, 10);

		// Add grid to the current screen
		victimScreen.getChildren().add(grid);

		// Remove previous screen from the stack and add the current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(victimScreen);

		// When the add button is pressed
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Temporary variables to represent the current objects
				int i = crime.getVictim().size();

				// Add a new 'Victim' object to the ArrayList
				crime.getVictim().add(new Victim());

				// Create a local copy of the object
				Victim localVictim = new Victim();
				localVictim = crime.getVictim().get(i);

				// Set object properties based on input field values
				localVictim.setId(i + 1);
				localVictim.setTitle(title.getText());
				localVictim.setForename(forename.getText());
				localVictim.setSurname(surname.getText());
				localVictim.setFirstLine(firstLine.getText());
				localVictim.setSecondLine(secondLine.getText());
				localVictim.setPostCode(postcode.getText());
				localVictim.setCountryOrigin(countryOrigin.getText());
				localVictim.setLawyerTitle(lawyerTitle.getText());
				localVictim.setLawyerForename(lawyerForename.getText());
				localVictim.setLawyerSurname(lawyerSurname.getText());

				// Prevent an error if the compensation field is empty
				try {
					localVictim.setCompensation(Integer.parseInt(compensation
							.getText()));
				} catch (Exception ex) {
					localVictim.setCompensation(0);
				}

				// Split the DOB into separate year, month, and day integers
				String dobString = String.valueOf(dob.getValue());
				String split[] = dobString.split("-");
				int year = Integer.parseInt(split[0].toString());
				int month = Integer.parseInt(split[1]);
				int day = Integer.parseInt(split[2]);
				localVictim.setYear(year);
				localVictim.setMonth(month);
				localVictim.setDay(day);

				// Copy localVictim object into the ArrayList
				crime.getVictim().set(i, localVictim);
				victims(border, stack, back);
			}
		});

		// If the discard button is pressed
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				victims(border, stack, back);
			}
		});
	}

	private void witnesses(BorderPane border, StackPane stack, Button back) {
		border.setBottom(back);

		// Create new screen and align everything centrally
		VBox witnessScreen = new VBox(10);
		witnessScreen.setAlignment(Pos.CENTER);

		// Create an observable list based on the values of the 'Victim' object
		final ObservableList<Witness> witnessObservable = FXCollections
				.observableArrayList(crime.getWitness());

		// Create table
		TableView<Witness> table = new TableView<Witness>();

		// Create ID column
		TableColumn<Witness, String> idCol = new TableColumn<Witness, String>(
				"ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Witness, String>(
				"id"));
		table.getColumns().add(idCol);

		// Create Title column
		TableColumn<Witness, String> titleCol = new TableColumn<Witness, String>(
				"Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Witness, String>(
				"title"));
		table.getColumns().add(titleCol);

		// Create Forename column
		TableColumn<Witness, String> forenameCol = new TableColumn<Witness, String>(
				"Forename");
		forenameCol
				.setCellValueFactory(new PropertyValueFactory<Witness, String>(
						"forename"));
		table.getColumns().add(forenameCol);

		// Create surname column
		TableColumn<Witness, String> surnameCol = new TableColumn<Witness, String>(
				"Surname");
		surnameCol
				.setCellValueFactory(new PropertyValueFactory<Witness, String>(
						"surname"));
		table.getColumns().add(surnameCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(witnessObservable);
		table.maxWidthProperty().bind(border.widthProperty().divide(1.2));
		table.prefHeightProperty().bind(border.heightProperty().divide(1.5));

		// Create screen title
		Label title = new Label("Witnesses");
		title.getStyleClass().add("label-header");

		// Create HBox for buttons and align centrally
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);

		// Create buttons
		Button newVictim = new Button("+");
		Button removeVictim = new Button("-");

		// Add buttons to HBox
		buttons.getChildren().addAll(newVictim, removeVictim);

		// Add label, table, and buttons to screen
		witnessScreen.getChildren().addAll(title, table, buttons);

		// Remove previous screen from stack and add new screen
		stack.getChildren().remove(0);
		stack.getChildren().add(witnessScreen);

		// If the '+' button is pressed
		newVictim.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Remove any buttons from the bottom of the screen
				border.setBottom(null);

				addWitness(border, stack, back);
			}
		});

		// If the '-' button is pressed
		removeVictim.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (table.getItems().size() != 0) {
					// Remove any buttons from the bottom of the screen
					border.setBottom(null);

					// Get the current table selection
					TableViewSelectionModel<Witness> selection = table
							.getSelectionModel();

					// Delete the relevant 'Victim' object based on the
					// selection
					crime.getVictim().remove(selection.getFocusedIndex());

					witnesses(border, stack, back);
				}
			}
		});

		// If the back button is pressed
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainMenu(border, stack, back);

				// Add the mainMenuBar to the bottom of the screen
				border.setBottom(mainMenuBar);
			}
		});
	}

	private void addWitness(BorderPane border, StackPane stack, Button back) {

		// Create new screen
		VBox witnessScreen = new VBox(10);
		witnessScreen.setAlignment(Pos.CENTER);

		// Create new grid and set its properties
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Create new HBox and display it at the bottom of the screen
		HBox hBox = new HBox();
		border.setBottom(hBox);

		// Create local discard button and add discard and add buttons to hBox
		Button discard = new Button("Discard");
		hBox.getChildren().addAll(discard, add);

		// Create form elements
		TextField title = new TextField("");
		TextField forename = new TextField("");
		TextField surname = new TextField("");
		DatePicker dob = new DatePicker();
		TextField occupation = new TextField("");
		TextField firstLine = new TextField("");
		TextField secondLine = new TextField("");
		TextField postcode = new TextField("");
		ComboBox<WitnessType> witnessType = new ComboBox<>();
		witnessType.getItems().setAll(WitnessType.values());
		TextField credibility = new TextField("");
		TextArea statement = new TextArea("");
		statement.setMaxSize(200, 100);

		// Set form field prompts
		title.setPromptText("title");
		forename.setPromptText("forename");
		surname.setPromptText("surname");
		occupation.setPromptText("occupation");
		firstLine.setPromptText("first line");
		secondLine.setPromptText("second line");
		postcode.setPromptText("postcode");
		credibility.setPromptText("credibility");
		statement.setPromptText("statement");

		// Create form labels
		Label personalLabel = new Label("Personal");
		Label locationLabel = new Label("Location");
		Label witnessLabel = new Label("Witness");

		// Set label styles
		personalLabel.getStyleClass().add("label-bright");
		locationLabel.getStyleClass().add("label-bright");
		witnessLabel.getStyleClass().add("label-bright");

		// Allow only numbers between 1 and 10 to be entered
		credibility.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);
		credibility.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					int temp = Integer.parseInt(newValue);

					if (temp > 10) {
						credibility.setText(Integer.toString(10));
					}
					if (temp < 1) {
						credibility.setText(Integer.toString(1));
					}
				} catch (Exception ex) {
					// This is normal behavior
				}
			}
		});

		// If any of these fields are empty - disable the add button
		add.disableProperty().bind(
				Bindings.isEmpty(title.textProperty())
						.or(Bindings.isEmpty(forename.textProperty()))
						.or(Bindings.isNull(dob.valueProperty()))
						.or(Bindings.isNull(witnessType.valueProperty()))
						.or(Bindings.isEmpty(credibility.textProperty()))
						.or(Bindings.isEmpty(statement.textProperty())));

		/* Add form elements to grid */
		// Personal section
		grid.add(personalLabel, 0, 0);
		grid.add(new Label("Title: *"), 0, 1);
		grid.add(title, 1, 1);
		grid.add(new Label("Forename: *"), 2, 1);
		grid.add(forename, 3, 1);
		grid.add(new Label("Surname: *"), 0, 2);
		grid.add(surname, 1, 2);
		grid.add(new Label("Date of birth: *"), 2, 2);
		grid.add(dob, 3, 2);
		grid.add(new Label("Occupation:"), 0, 3);
		grid.add(occupation, 1, 3);

		// Location section
		grid.add(locationLabel, 0, 4);
		grid.add(new Label("First line:"), 0, 5);
		grid.add(firstLine, 1, 5);
		grid.add(new Label("Second line:"), 2, 5);
		grid.add(secondLine, 3, 5);
		grid.add(new Label("Postcode:"), 0, 6);
		grid.add(postcode, 1, 6);

		// Witness section
		grid.add(witnessLabel, 0, 7);
		grid.add(new Label("Type: *"), 0, 8);
		grid.add(witnessType, 1, 8);
		grid.add(new Label("Credibility (1-10): *"), 2, 8);
		grid.add(credibility, 3, 8);
		grid.add(new Label("Statement: *"), 0, 9);
		grid.add(statement, 1, 9);

		// Add grid to the current screen
		witnessScreen.getChildren().add(grid);

		// Remove previous screen from the stack and add the current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(witnessScreen);

		// When the add button is pressed
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Temporary variables to represent the current objects
				int i = crime.getWitness().size();

				// Add a new 'Victim' object to the ArrayList
				crime.getWitness().add(new Witness());

				// Create a local copy of the object
				Witness localWitness = new Witness();
				localWitness = crime.getWitness().get(i);

				// Set object properties based on input field values
				localWitness.setId(i + 1);
				localWitness.setTitle(title.getText());
				localWitness.setForename(forename.getText());
				localWitness.setSurname(surname.getText());
				localWitness.setOccupation(occupation.getText());
				localWitness.setFirstLine(firstLine.getText());
				localWitness.setSecondLine(secondLine.getText());
				localWitness.setPostCode(postcode.getText());
				localWitness.setWitnessType(witnessType.getValue());
				localWitness.setCredibility(Integer.parseInt(credibility
						.getText()));
				localWitness.setStatement(statement.getText());

				// Split the DOB into separate year, month, and day integers
				String dobString = String.valueOf(dob.getValue());
				String split[] = dobString.split("-");
				int year = Integer.parseInt(split[0].toString());
				int month = Integer.parseInt(split[1]);
				int day = Integer.parseInt(split[2]);
				localWitness.setYear(year);
				localWitness.setMonth(month);
				localWitness.setDay(day);

				// Copy localVictim object into the ArrayList
				crime.getWitness().set(i, localWitness);
				witnesses(border, stack, back);
			}
		});

		// If the discard button is pressed
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				witnesses(border, stack, back);
			}
		});
	}

	private void evidence(BorderPane border, StackPane stack, Button back) {
		border.setBottom(back);

		// Create new screen and align everything centrally
		VBox evidenceScreen = new VBox(10);
		evidenceScreen.setAlignment(Pos.CENTER);

		// Create an observable list based on the values of the 'Evidence'
		// object
		final ObservableList<Evidence> evidenceObservable = FXCollections
				.observableArrayList(crime.getEvidence());

		// Create table
		TableView<Evidence> table = new TableView<Evidence>();

		TableColumn<Evidence, String> idCol = new TableColumn<Evidence, String>(
				"ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Evidence, String>(
				"id"));
		table.getColumns().add(idCol);

		// Create ID column
		TableColumn<Evidence, String> titleCol = new TableColumn<Evidence, String>(
				"Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<Evidence, String>(
				"title"));
		table.getColumns().add(titleCol);

		// Create Type column
		TableColumn<Evidence, String> typeCol = new TableColumn<Evidence, String>(
				"Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<Evidence, String>(
				"type"));
		table.getColumns().add(typeCol);

		// Create Supports column
		TableColumn<Evidence, String> supportsCol = new TableColumn<Evidence, String>(
				"Supports");
		supportsCol
				.setCellValueFactory(new PropertyValueFactory<Evidence, String>(
						"supports"));
		table.getColumns().add(supportsCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(evidenceObservable);
		table.maxWidthProperty().bind(border.widthProperty().divide(1.2));
		table.prefHeightProperty().bind(border.heightProperty().divide(1.5));

		// Create screen title
		Label title = new Label("Evidence");
		title.getStyleClass().add("label-header");

		// Create HBox for buttons and align centrally
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);

		// Create buttons
		Button newEvidence = new Button("+");
		Button removeEvidence = new Button("-");

		// Add buttons to HBox
		buttons.getChildren().addAll(newEvidence, removeEvidence);

		// Add label, table, and buttons to screen
		evidenceScreen.getChildren().addAll(title, table, buttons);

		// Remove previous screen from stack and add current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(evidenceScreen);

		// If the '+' button is pressed
		newEvidence.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Remove any buttons from the bottom of the screen
				border.setBottom(null);

				addEvidence(border, stack, back);
			}
		});

		// If the '-' button is pressed
		removeEvidence.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (table.getItems().size() != 0) {
					// Remove any buttons from the bottom of the screen
					border.setBottom(null);

					// Get the current table selection
					TableViewSelectionModel<Evidence> selection = table
							.getSelectionModel();

					// Delete the relevant 'Victim' object based on the
					// selection
					crime.getEvidence().remove(selection.getFocusedIndex());

					evidence(border, stack, back);
				}
			}
		});

		// If the back button is pressed
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainMenu(border, stack, back);

				// Add the mainMenuBar to the bottom of the screen
				border.setBottom(mainMenuBar);
			}
		});
	}

	private void addEvidence(BorderPane border, StackPane stack, Button back) {

		// Create new screen and align everything centrally
		VBox evidenceScreen = new VBox(10);
		evidenceScreen.setAlignment(Pos.CENTER);

		// Create grid and set properties
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Create new HBox and add to bottom of screen
		HBox hBox = new HBox();
		border.setBottom(hBox);

		// Create local discard button and add discard and ad buttons to hBox
		Button discard = new Button("Discard");
		hBox.getChildren().addAll(discard, add);

		// Add form fields
		TextField title = new TextField("");
		ComboBox<Type> type = new ComboBox<>();
		type.getItems().setAll(Type.values());
		TextArea description = new TextArea("");
		TextField credibility = new TextField("");
		ComboBox<Supports> supports = new ComboBox<>();
		supports.getItems().setAll(Supports.values());

		// Set field prompts
		title.setPromptText("title");
		description.setPromptText("description");
		credibility.setPromptText("credibility");

		// Set TextArea preferred size
		description.setPrefWidth(8);
		description.setPrefHeight(6);

		// Create form label and set style
		Label evidenceLabel = new Label("Evidence Item");
		evidenceLabel.getStyleClass().add("label-bright");

		// Allow only numbers between 1 and 10 to be entered
		credibility.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);
		credibility.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					int temp = Integer.parseInt(newValue);

					if (temp > 10) {
						credibility.setText(Integer.toString(10));
					}
					if (temp < 1) {
						credibility.setText(Integer.toString(1));
					}
				} catch (Exception ex) {
					// This is normal behavior
				}
			}
		});
		
		// If any of these fields are empty - disable the add button
		add.disableProperty().bind(
				Bindings.isEmpty(title.textProperty())
						.or(Bindings.isEmpty(title.textProperty()))
						.or(Bindings.isNull(type.valueProperty()))
						.or(Bindings.isEmpty(description.textProperty()))
						.or(Bindings.isEmpty(credibility.textProperty()))
						.or(Bindings.isNull(supports.valueProperty())));

		/* Add form elements to grid */
		grid.add(evidenceLabel, 0, 0);
		grid.add(new Label("Title: *"), 0, 1);
		grid.add(title, 1, 1);
		grid.add(new Label("Type: *"), 0, 2);
		grid.add(type, 1, 2);
		grid.add(new Label("Description: *"), 0, 3);
		grid.add(description, 1, 3);
		grid.add(new Label("Credibility (1-10): *"), 0, 4);
		grid.add(credibility, 1, 4);
		grid.add(new Label("Supports: *"), 0, 5);
		grid.add(supports, 1, 5);

		// Add grid to current screen
		evidenceScreen.getChildren().add(grid);

		// Remove previous screen from the stack and add the current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(evidenceScreen);

		// If add button is pressed
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Temporary variables to represent the current objects
				int i = crime.getEvidence().size();

				// Add a new 'Victim' object to the ArrayList
				crime.getEvidence().add(new Evidence());

				// Create a local copy of the object
				Evidence localEvidence = new Evidence();
				localEvidence = crime.getEvidence().get(i);

				// Set object properties based on input field values
				localEvidence.setId(i + 1);
				localEvidence.setType(type.getValue());
				localEvidence.setTitle(title.getText());
				localEvidence.setDescription(description.getText());
				localEvidence.setSupports(supports.getValue());

				// Prevent an error if the credibility field is empty
				try {
					localEvidence.setCredibility(Integer.parseInt(credibility
							.getText()));
				} catch (Exception ex) {
					localEvidence.setCredibility(0);
				}

				// Copy localEvidence object into the ArrayList
				crime.getEvidence().set(i, localEvidence);

				evidence(border, stack, back);
			}
		});

		// If the discard button is pressed
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				evidence(border, stack, back);
			}
		});
	}

	private void specific(BorderPane border, StackPane stack, Button back) {
		border.setBottom(back);

		// Create new screen and align everything centrally
		VBox specificScreen = new VBox(10);
		specificScreen.setAlignment(Pos.CENTER);

		// Create grid and set its properties
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Create HBox and display it on the bottom
		HBox hBox = new HBox();
		border.setBottom(hBox);

		// Create local discard button and add discard and add buttons to hBox
		Button discard = new Button("Discard");
		hBox.getChildren().addAll(discard, add);

		// Create form fields and fill them with their existing values
		TextArea summary = new TextArea(crime.getSummary());
		ComboBox<ManslaughterType> type = new ComboBox<>();
		type.getItems().setAll(ManslaughterType.values());
		type.setValue(crime.getSpecific().getManslaughterType());
		ComboBox<ManslaughterSeriousness> seriousness = new ComboBox<>();
		seriousness.getItems().setAll(ManslaughterSeriousness.values());
		seriousness.setValue(crime.getSpecific().getManslaughterSeriousness());
		ComboBox<ManslaughterLossControl> lossControl = new ComboBox<>();
		lossControl.getItems().setAll(ManslaughterLossControl.values());
		lossControl.setValue(crime.getSpecific().getManslaughterLossControl());
		ComboBox<VehicleInvolved> vehicleInvolved = new ComboBox<>();
		vehicleInvolved.getItems().setAll(VehicleInvolved.values());
		vehicleInvolved.setValue(crime.getSpecific().getVehicleInvolved());
		
		summary.setMaxSize(300, 100);

		// Create form label and set style
		Label specificLabel = new Label("Crime Specific Details");
		specificLabel.getStyleClass().add("label-header");

		// Disable add button if field is empty
		add.disableProperty().bind(
				Bindings.isNull(type.valueProperty())
						.or(Bindings.isEmpty(summary.textProperty()))
						.or(Bindings.isNull(seriousness.valueProperty()))
						.or(Bindings.isNull(lossControl.valueProperty()))
						.or(Bindings.isNull(vehicleInvolved.valueProperty())));

		// Add form elements to grid
		grid.add(new Label("Case Summary: *"), 0, 0);
		grid.add(summary, 1, 0);
		grid.add(new Label("Manslaughter type: *"), 0, 1);
		grid.add(type, 1, 1);
		grid.add(new Label("Manslaughter seriousness: *"), 0, 2);
		grid.add(seriousness, 1, 2);
		grid.add(new Label("Loss of control: *"), 0, 3);
		grid.add(lossControl, 1, 3);
		grid.add(new Label("Vehicle involved: *"), 0, 4);
		grid.add(vehicleInvolved, 1, 4);

		// Add label and grid to current screen
		specificScreen.getChildren().addAll(specificLabel, grid);

		// Remove previous screen from the stack and add the current screen
		stack.getChildren().remove(0);
		stack.getChildren().add(specificScreen);

		// If add button is selected
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				// Create a local copy of the object
				Specific localSpecific = crime.getSpecific();

				// Set object property based on input field value
				crime.setSummary(summary.getText());
				localSpecific.setManslaughterType(type.getValue());
				localSpecific.setManslaughterSeriousness(seriousness.getValue());
				localSpecific.setManslaughterLossControl(lossControl.getValue());
				localSpecific.setVehicleInvolved(vehicleInvolved.getValue());

				// Copy localSpecific object back into crime
				crime.setSpecific(localSpecific);

				mainMenu(border, stack, back);
			}
		});

		// If discard button is selected
		discard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainMenu(border, stack, back);
				border.setBottom(mainMenuBar);
			}
		});
	}

	/**
	 * This event handler restricts the input of text to numbers
	 */
	private EventHandler<KeyEvent> restrictInput = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent inputevent) {
			if (!inputevent.getCharacter().matches("\\d")) {
				inputevent.consume();
			}
		}
	};
}
