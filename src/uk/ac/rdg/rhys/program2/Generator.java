package uk.ac.rdg.rhys.program2;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import uk.ac.rdg.rhys.program2.Accused.Verdict;
import uk.ac.rdg.rhys.program2.Crime.CrimeType;
import uk.ac.rdg.rhys.program2.Specific.SubstanceConsumption;
import uk.ac.rdg.rhys.program2.Specific.SubstanceIntentToSupply;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * <h1>Generator.java</h1>
 * <p>
 * This class generates a PDF report based on the data in the crime object. It
 * will ignore any blank values and not write them to the report.
 *
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Generator {

	// Create a local crime object so all methods have access
	private Crime crime;

	/**
	 * The constructor of the class
	 * 
	 * @param crime
	 *            The crime object containing the report data
	 * @throws IOException
	 *             Occurs if the file cannot be written to
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	public Generator(Crime crime) throws IOException, DocumentException {
		// Copy the crime object
		this.crime = crime;
	}

	/**
	 * Handles the process of creating the PDF report by calling the relevant
	 * methods for the information contained in the crime object
	 * 
	 * @param fileName
	 *            The name of the PDF file to write to
	 * @throws IOException
	 *             Occurs if the file cannot be written to
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	public void createPdf(String fileName) throws IOException,
			DocumentException {

		// Create the document
		Document document = new Document();

		// Create a FileOutputStream to allow writing to the PDF file
		PdfWriter.getInstance(document, new FileOutputStream(fileName));

		// Open the document for writing
		document.open();

		// START WRITING TO THE DOCUMENT
		header(document);

		document.add(createCourtTable());
		document.add(createCrimeTable());
		// Don't write summary if it is empty
		if (!crime.getSummary().isEmpty())
			document.add(createSummary());
		createAccused(document);
		createVictims(document);
		// Don't write Witnesses if the CrimeType is FRAUD
		if (crime.getCrimeType() != CrimeType.FRAUD)
			createWitnesses(document);
		createEvidence(document);
		footer(document);

		// STOP WRITING TO THE DOCUMENT
		document.close();

		// Interact with the user
		completed(fileName);
	}

	/**
	 * Notifies the user that the report was created successfully and prompts if
	 * the user would like to open the report.
	 * 
	 * @param fileName
	 *            The name of the PDF file to open
	 */
	private void completed(String fileName) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Report created");
		alert.setHeaderText("Report created successfully");
		alert.setContentText("Would you like to open the report now?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (Desktop.isDesktopSupported()) {
				try {
					File report = new File(fileName);
					Desktop.getDesktop().open(report);
				} catch (IOException ex) {
					System.out
							.println("No application was found to open the report - please open it manually");
				}
			}
		} else {
			return;
		}
	}

	/**
	 * Creates the report document's header
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void header(Document document) throws DocumentException {

		// Create table with three columns and set properties
		PdfPTable table = new PdfPTable(3);
		table.setHorizontalAlignment(1);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 100, 200, 100 });

		// Add 'confidential' marker
		PdfPCell confidential = new PdfPCell(
				new Phrase("CONFIDENTIAL", FONT[3]));
		confidential.setHorizontalAlignment(1);
		confidential.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(confidential);

		// Add the title to the header table
		PdfPCell title = new PdfPCell(new Paragraph("Court Report for "
				+ crime.getCourtName(), FONT[1]));
		title.setBorder(Rectangle.NO_BORDER);
		title.setHorizontalAlignment(1);
		table.addCell(title);

		// Add another 'confidential' marker
		table.addCell(confidential);

		document.add(table);

	}

	/**
	 * Creates and fills the table containing the court details
	 * 
	 * @return The table object to write to the document
	 */
	private PdfPTable createCourtTable() {

		// Create a table with 4 columns and set its properties
		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(15);
		table.setSpacingAfter(10);
		table.setWidthPercentage(100);

		// Create cells
		PdfPCell name = new PdfPCell(new Phrase("Court name", FONT[3]));
		PdfPCell nameValue = new PdfPCell(new Phrase(crime.getCourtName()));

		// Create cells
		PdfPCell type = new PdfPCell(new Phrase("Court type", FONT[3]));
		PdfPCell typeValue = new PdfPCell(new Phrase(crime.getCourtType()
				.toString()));

		// Create cells
		PdfPCell number = new PdfPCell(new Phrase("Case number", FONT[3]));
		PdfPCell numberValue = new PdfPCell(new Phrase(crime.getCaseNumber()
				+ ""));

		// Create cells
		PdfPCell date = new PdfPCell(new Phrase("Court Date", FONT[3]));
		PdfPCell dateValue = new PdfPCell(new Phrase(crime.getDay() + "/"
				+ crime.getMonth() + "/" + crime.getYear()));

		// Set cell borders
		name.setBorder(Rectangle.NO_BORDER);
		nameValue.setBorder(Rectangle.NO_BORDER);
		type.setBorder(Rectangle.NO_BORDER);
		typeValue.setBorder(Rectangle.NO_BORDER);
		number.setBorder(Rectangle.NO_BORDER);
		numberValue.setBorder(Rectangle.NO_BORDER);
		date.setBorder(Rectangle.NO_BORDER);
		dateValue.setBorder(Rectangle.NO_BORDER);

		// Add cells to table
		table.addCell(name);
		table.addCell(nameValue);
		table.addCell(type);
		table.addCell(typeValue);
		table.addCell(number);
		table.addCell(numberValue);
		table.addCell(date);
		table.addCell(dateValue);

		return table;
	}

	/**
	 * Creates and fills the table containing the crime specific details
	 * 
	 * @return The table object to write to the document
	 */
	private PdfPTable createCrimeTable() {

		// Create local 'specific' object
		Specific specific = crime.getSpecific();

		// Create table with two columns and set its properties
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(50);
		table.setHorizontalAlignment(0);

		PdfPCell crimeType = new PdfPCell(new Phrase("Crime", FONT[3]));
		PdfPCell crimeTypeValue = new PdfPCell(new Phrase(crime.getCrimeType()
				.toString()));
		crimeType.setBorder(Rectangle.NO_BORDER);
		crimeTypeValue.setBorder(Rectangle.NO_BORDER);
		table.addCell(crimeType);
		table.addCell(crimeTypeValue);

		// Add different content based on the crime type
		switch (crime.getCrimeType()) {
		case FRAUD:
			// Add fraudType cells to table and set their properties
			PdfPCell fraudType = new PdfPCell(new Phrase("Type of fraud",
					FONT[3]));
			PdfPCell fraudTypeValue = new PdfPCell(new Phrase(
					specific.getFraudType()));
			fraudType.setBorder(Rectangle.NO_BORDER);
			fraudTypeValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(fraudType);
			table.addCell(fraudTypeValue);

			// Add fraudTarget cells to table and set their properties
			PdfPCell fraudTarget = new PdfPCell(new Phrase("Target of fraud",
					FONT[3]));
			PdfPCell fraudTargetValue = new PdfPCell(new Phrase(specific
					.getFraudTarget().toString()));
			fraudTarget.setBorder(Rectangle.NO_BORDER);
			fraudTargetValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(fraudTarget);
			table.addCell(fraudTargetValue);

			// Add fraudCost cells to table and set their properties
			PdfPCell fraudCost = new PdfPCell(new Phrase("Cost of fraud",
					FONT[3]));
			PdfPCell fraudCostValue = new PdfPCell(new Phrase(
					specific.getFraudCost() + ""));
			fraudCost.setBorder(Rectangle.NO_BORDER);
			fraudCostValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(fraudCost);
			table.addCell(fraudCostValue);
			break;
		case MANSLAUGHTER:
			// Add manslaughterType cells to table and set their properties
			PdfPCell manslaughterType = new PdfPCell(new Phrase(
					"Type of manslaughter", FONT[3]));
			PdfPCell manslaughterTypeValue = new PdfPCell(new Phrase(specific
					.getManslaughterType().toString()));
			manslaughterType.setBorder(Rectangle.NO_BORDER);
			manslaughterTypeValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(manslaughterType);
			table.addCell(manslaughterTypeValue);

			// Add manslaughterSeriousness cells to table and set their
			// properties
			PdfPCell manslaughterSeriousness = new PdfPCell(new Phrase(
					"Seriousness", FONT[3]));
			PdfPCell manslaughterSeriousnessValue = new PdfPCell(new Phrase(
					specific.getManslaughterSeriousness().toString()));
			manslaughterSeriousness.setBorder(Rectangle.NO_BORDER);
			manslaughterSeriousnessValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(manslaughterSeriousness);
			table.addCell(manslaughterSeriousnessValue);

			// Add manslaughterControl cells to table and set their properties
			PdfPCell manslaughterControl = new PdfPCell(new Phrase(
					"Loss of Control", FONT[3]));
			PdfPCell manslaughterControlValue = new PdfPCell(new Phrase(
					specific.getManslaughterLossControl().toString()));
			manslaughterControl.setBorder(Rectangle.NO_BORDER);
			manslaughterControlValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(manslaughterControl);
			table.addCell(manslaughterControlValue);

			// Add manslaughterVehicle cells to table and set their properties
			PdfPCell manslaughterVehicle = new PdfPCell(new Phrase(
					"Vehicle involved", FONT[3]));
			PdfPCell manslaughterVehicleValue = new PdfPCell(new Phrase(
					specific.getVehicleInvolved().toString()));
			manslaughterVehicle.setBorder(Rectangle.NO_BORDER);
			manslaughterVehicleValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(manslaughterVehicle);
			table.addCell(manslaughterVehicleValue);
			break;
		case SUBSTANCEABUSE:
			// Add substanceName cells to table and set their properties
			PdfPCell substanceName = new PdfPCell(new Phrase("Substance name",
					FONT[3]));
			PdfPCell substanceNameValue = new PdfPCell(new Phrase(
					specific.getSubstanceName()));
			substanceName.setBorder(Rectangle.NO_BORDER);
			substanceNameValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(substanceName);
			table.addCell(substanceNameValue);

			// Add substanceType cells to table and set their properties
			PdfPCell substanceType = new PdfPCell(new Phrase("Substance type",
					FONT[3]));
			PdfPCell substanceTypeValue = new PdfPCell(new Phrase(specific
					.getSubstanceType().toString()));
			substanceType.setBorder(Rectangle.NO_BORDER);
			substanceTypeValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(substanceType);
			table.addCell(substanceTypeValue);

			// Add substanceClassification cells to table and set their
			// properties
			PdfPCell substanceClassification = new PdfPCell(new Phrase(
					"Substance classification", FONT[3]));
			PdfPCell substanceClassificationValue = new PdfPCell(new Phrase(
					specific.getSubstanceClassification().toString()));
			substanceClassification.setBorder(Rectangle.NO_BORDER);
			substanceClassificationValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(substanceClassification);
			table.addCell(substanceClassificationValue);

			// If the substance consumption value is not blank
			if (specific.getSubstanceConsumption() != SubstanceConsumption.BLANK) {
				// Add substanceConsumption cells to table and set their
				// properties
				PdfPCell substanceConsumption = new PdfPCell(new Phrase(
						"Substance consumption method", FONT[3]));
				PdfPCell substanceConsumptionValue = new PdfPCell(new Phrase(
						specific.getSubstanceConsumption().toString()));
				substanceConsumption.setBorder(Rectangle.NO_BORDER);
				substanceConsumptionValue.setBorder(Rectangle.NO_BORDER);
				table.addCell(substanceConsumption);
				table.addCell(substanceConsumptionValue);
			}

			// If the quantity seized value is not empty
			if (!specific.getQuantitySeized().isEmpty()) {
				// Add substanceQuantity cells to table and set their properties
				PdfPCell substanceQuantity = new PdfPCell(new Phrase(
						"Quantity seized", FONT[3]));
				PdfPCell substanceQuantityValue = new PdfPCell(new Phrase(
						specific.getQuantitySeized()));
				substanceQuantity.setBorder(Rectangle.NO_BORDER);
				substanceQuantityValue.setBorder(Rectangle.NO_BORDER);
				table.addCell(substanceQuantity);
				table.addCell(substanceQuantityValue);

				// Add substanceStreet cells to table and set their properties
				PdfPCell substanceStreet = new PdfPCell(new Phrase(
						"Street value", FONT[3]));
				PdfPCell substanceStreetValue = new PdfPCell(new Phrase(
						specific.getStreetValue() + ""));
				substanceStreet.setBorder(Rectangle.NO_BORDER);
				substanceStreetValue.setBorder(Rectangle.NO_BORDER);
				table.addCell(substanceStreet);
				table.addCell(substanceStreetValue);
			}

			// If the IntentToSupply value is not blank
			if (specific.getSubstanceIntentToSupply() != SubstanceIntentToSupply.BLANK) {
				// Add substanceIntent cells to table and set their properties
				PdfPCell substanceIntent = new PdfPCell(new Phrase(
						"Intent to supply", FONT[3]));
				PdfPCell substanceIntentValue = new PdfPCell(new Phrase(
						specific.getSubstanceIntentToSupply().toString()));
				substanceIntent.setBorder(Rectangle.NO_BORDER);
				substanceIntentValue.setBorder(Rectangle.NO_BORDER);
				table.addCell(substanceIntent);
				table.addCell(substanceIntentValue);
			}

			// If the associated crimes value is not blank
			if (!specific.getAssociatedCrimes().isEmpty()) {
				// Add substanceAssociated cells to table and set their
				// properties
				PdfPCell substanceAssociated = new PdfPCell(new Phrase(
						"Associated crimes", FONT[3]));
				PdfPCell substanceAssociatedValue = new PdfPCell(new Phrase(
						specific.getAssociatedCrimes()));
				substanceAssociated.setBorder(Rectangle.NO_BORDER);
				substanceAssociatedValue.setBorder(Rectangle.NO_BORDER);
				table.addCell(substanceAssociated);
				table.addCell(substanceAssociatedValue);
			}
			break;
		default:
			break;
		}

		return table;
	}

	/**
	 * Creates the report document's case summary
	 * 
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private PdfPTable createSummary() throws DocumentException {

		// If the summary is not empty
		if (!crime.getSummary().isEmpty()) {
			// Create table with two columns and set its properties
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10);
			table.setHorizontalAlignment(0);

			table.setWidths(new int[] { 60, 180 });

			PdfPCell summary = new PdfPCell(new Phrase("Case summary", FONT[3]));
			PdfPCell summaryValue = new PdfPCell(new Phrase(crime.getSummary()));
			summary.setBorder(Rectangle.NO_BORDER);
			summaryValue.setBorder(Rectangle.NO_BORDER);
			table.addCell(summary);
			table.addCell(summaryValue);

			return table;
		}
		return null;
	}

	/**
	 * Creates and fills the table containing the details of the accused
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void createAccused(Document document) throws DocumentException {

		// If these are accused
		if (crime.getAccused().size() != 0) {

			// Create title, set properties and add to table
			Paragraph accusedTitle = new Paragraph("Accused", FONT[2]);
			accusedTitle.setAlignment(0);
			accusedTitle.setSpacingBefore(10);
			accusedTitle.setSpacingAfter(20);
			document.add(accusedTitle);

			// Create table with three columns and set properties
			PdfPTable table = new PdfPTable(2);
			table.setSpacingAfter(10);
			table.setWidthPercentage(100);

			// Add details for each 'Accused' person
			for (int i = 0; i < crime.getAccused().size(); i++) {

				// Create local object for the current accused person
				Accused currentAccused = crime.getAccused().get(i);

				// Create name header cell and set properties
				PdfPCell name = new PdfPCell(new Phrase(
						currentAccused.getTitle() + " "
								+ currentAccused.getForename() + " "
								+ currentAccused.getSurname(), FONT[3]));
				name.setColspan(2);
				name.setFixedHeight(20);
				name.setBorder(Rectangle.BOX);
				table.addCell(name);

				// Create personal header cell and set properties
				PdfPCell personal = new PdfPCell(new Phrase("Personal details",
						FONT[3]));
				personal.setColspan(2);
				personal.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(personal);

				// Create date of birth cells and set properties
				PdfPCell dob = new PdfPCell(new Phrase("Date of birth"));
				PdfPCell dobValue = new PdfPCell(new Phrase(
						currentAccused.getDay() + "/"
								+ currentAccused.getMonth() + "/"
								+ currentAccused.getYear()));
				dob.setBorder(Rectangle.LEFT);
				dobValue.setBorder(Rectangle.RIGHT);
				table.addCell(dob);
				table.addCell(dobValue);

				// Create address cells and set properties
				PdfPCell address = new PdfPCell(new Phrase("Address"));
				PdfPCell addressValue = new PdfPCell(new Phrase(
						currentAccused.getFirstLine() + "\n"
								+ currentAccused.getSecondLine() + "\n"
								+ currentAccused.getPostCode()));
				address.setBorder(Rectangle.LEFT);
				addressValue.setBorder(Rectangle.RIGHT);
				table.addCell(address);
				table.addCell(addressValue);

				// Create country cells and set properties
				PdfPCell country = new PdfPCell(new Phrase("Country of origin"));
				PdfPCell countryValue = new PdfPCell(new Phrase(
						currentAccused.getCountryOrigin()));
				country.setBorder(Rectangle.LEFT);
				countryValue.setBorder(Rectangle.RIGHT);
				table.addCell(country);
				table.addCell(countryValue);

				// Create lawyer name cells and set properties
				PdfPCell lawyer = new PdfPCell(new Phrase("Lawyer name"));
				PdfPCell lawyerValue = new PdfPCell(new Phrase(
						currentAccused.getLawyerTitle() + " "
								+ currentAccused.getLawyerForename() + " "
								+ currentAccused.getLawyerSurname()));
				lawyer.setBorder(Rectangle.LEFT);
				lawyerValue.setBorder(Rectangle.RIGHT);
				table.addCell(lawyer);
				table.addCell(lawyerValue);

				// Create spacer cell and set properties
				PdfPCell spacer = new PdfPCell(new Phrase("\n"));
				spacer.setColspan(2);
				spacer.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(spacer);

				// If at least one of the previous conviction values are not
				// empty
				if (!currentAccused.getPreviousConvictionTitle().isEmpty()
						|| currentAccused.getPreviousConvictionDay() != 0
						|| !currentAccused.getPreviousConvictionDescription()
								.isEmpty()) {
					// Create previous conviction header cell and set properties
					PdfPCell conviction = new PdfPCell(new Phrase(
							"Previous conviction details", FONT[3]));
					conviction.setColspan(2);
					conviction.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
					table.addCell(conviction);
				}

				// If the previous conviction title cell is not empty
				if (!currentAccused.getPreviousConvictionTitle().isEmpty()) {
					// Create previous conviction title cells and set properties
					PdfPCell title = new PdfPCell(new Phrase("Title"));
					PdfPCell titleValue = new PdfPCell(new Phrase(
							currentAccused.getPreviousConvictionTitle()));
					title.setBorder(Rectangle.LEFT);
					titleValue.setBorder(Rectangle.RIGHT);
					table.addCell(title);
					table.addCell(titleValue);
				}

				// If the previous conviction day is not 0
				if (currentAccused.getPreviousConvictionDay() != 0) {
					// Create previous conviction date cells and set properties
					PdfPCell date = new PdfPCell(new Phrase("Date"));
					PdfPCell dateValue = new PdfPCell(new Phrase(
							currentAccused.getPreviousConvictionDay()
									+ "/"
									+ currentAccused
											.getPreviousConvictionMonth()
									+ "/"
									+ currentAccused
											.getPreviousConvictionYear()));
					date.setBorder(Rectangle.LEFT);
					dateValue.setBorder(Rectangle.RIGHT);
					table.addCell(date);
					table.addCell(dateValue);
				}

				// If the previous conviction description is not empty
				if (!currentAccused.getPreviousConvictionDescription()
						.isEmpty()) {
					// Create previous conviction description cells and set
					// properties
					PdfPCell description = new PdfPCell(new Phrase(
							"Description"));
					PdfPCell descriptionValue = new PdfPCell(new Phrase(
							currentAccused.getPreviousConvictionDescription()));
					description.setBorder(Rectangle.LEFT);
					descriptionValue.setBorder(Rectangle.RIGHT);
					table.addCell(description);
					table.addCell(descriptionValue);
				}

				// Add the spacer cell again
				table.addCell(spacer);

				// Create court header cell and set properties
				PdfPCell court = new PdfPCell(new Phrase("Court details",
						FONT[3]));
				court.setColspan(2);
				court.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(court);

				// Create involvement cells and set properties
				PdfPCell involvement = new PdfPCell(new Phrase("Involvement"));
				PdfPCell involvementValue = new PdfPCell(new Phrase(
						currentAccused.getInvolvement()));

				// If involvement is the last row in the table, set its
				// properties
				// accordingly
				if (currentAccused.getVerdict() == Verdict.BLANK
						|| currentAccused.getSentenceLength().isEmpty()
						|| currentAccused.getSentenceDescription().isEmpty()
						|| currentAccused.getFine() == 0) {

					involvement.setPaddingBottom(10);
					involvementValue.setPaddingBottom(10);
					involvement.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
					involvementValue.setBorder(Rectangle.RIGHT
							| Rectangle.BOTTOM);
				} else {
					involvement.setBorder(Rectangle.LEFT);
					involvementValue.setBorder(Rectangle.RIGHT);
				}
				table.addCell(involvement);
				table.addCell(involvementValue);

				// If verdict is not blank
				if (currentAccused.getVerdict() != Verdict.BLANK) {
					// Create verdict cells and set properties
					PdfPCell verdict = new PdfPCell(new Phrase("Court verdict"));
					PdfPCell verdictValue = new PdfPCell(new Phrase(
							currentAccused.getVerdict().toString()));
					verdict.setBorder(Rectangle.LEFT);
					verdictValue.setBorder(Rectangle.RIGHT);
					table.addCell(verdict);
					table.addCell(verdictValue);
				}

				// If sentence length is not blank
				if (!currentAccused.getSentenceLength().isEmpty()) {
					// Create sentence length cells and set properties
					PdfPCell sentenceLength = new PdfPCell(new Phrase(
							"Sentence length"));
					PdfPCell sentenceLengthValue = new PdfPCell(new Phrase(
							currentAccused.getSentenceLength()));
					sentenceLength.setBorder(Rectangle.LEFT);
					sentenceLengthValue.setBorder(Rectangle.RIGHT);
					table.addCell(sentenceLength);
					table.addCell(sentenceLengthValue);
				}

				// If sentence description is not empty
				if (!currentAccused.getSentenceDescription().isEmpty()) {
					// Create sentence description cells and set properties
					PdfPCell sentenceDescription = new PdfPCell(new Phrase(
							"Sentence description"));
					PdfPCell sentenceDescriptionValue = new PdfPCell(
							new Phrase(currentAccused.getSentenceDescription()));
					sentenceDescription.setBorder(Rectangle.LEFT);
					sentenceDescriptionValue.setBorder(Rectangle.RIGHT);
					table.addCell(sentenceDescription);
					table.addCell(sentenceDescriptionValue);
				}

				// If fine is not set to 0
				if (currentAccused.getFine() != 0) {
					// Create fine cells and set properties
					PdfPCell fine = new PdfPCell(new Phrase("Fine awarded"));
					PdfPCell fineValue = new PdfPCell(new Phrase(
							currentAccused.getFine() + ""));
					fine.setPaddingBottom(10);
					fineValue.setPaddingBottom(10);
					fine.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
					fineValue.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
					table.addCell(fine);
					table.addCell(fineValue);
				}
			}
			document.add(table);
		}
	}

	/**
	 * Creates and fills the table containing the details of the victims
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void createVictims(Document document) throws DocumentException {

		// If there are victims
		if (crime.getVictim().size() != 0) {

			// Create title, set properties and add to document
			Paragraph victimsTitle = new Paragraph("Victim(s)", FONT[2]);
			victimsTitle.setAlignment(0);
			victimsTitle.setSpacingAfter(20);
			document.add(victimsTitle);

			// Create table with two columns and add to table
			PdfPTable table = new PdfPTable(2);
			table.setSpacingAfter(10);
			table.setWidthPercentage(100);

			// Add details for each 'Victim'
			for (int i = 0; i < crime.getVictim().size(); i++) {

				// Create local victim object for current victim
				Victim currentVictim = crime.getVictim().get(i);

				// Create name header cell and set properties
				PdfPCell name = new PdfPCell(new Phrase(
						currentVictim.getTitle() + " "
								+ currentVictim.getForename() + " "
								+ currentVictim.getSurname(), FONT[3]));
				name.setColspan(2);
				name.setFixedHeight(20);
				name.setBorder(Rectangle.BOX);
				table.addCell(name);

				// Create personal header cell and set properties
				PdfPCell personal = new PdfPCell(new Phrase("Personal details",
						FONT[3]));
				personal.setColspan(2);
				personal.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(personal);

				// Create date of birth cells and set properties
				PdfPCell dob = new PdfPCell(new Phrase("Date of birth"));
				PdfPCell dobValue = new PdfPCell(new Phrase(
						currentVictim.getDay() + "/" + currentVictim.getMonth()
								+ "/" + currentVictim.getYear()));
				dob.setBorder(Rectangle.LEFT);
				dobValue.setBorder(Rectangle.RIGHT);
				table.addCell(dob);
				table.addCell(dobValue);

				// Create address cells and set properties
				PdfPCell address = new PdfPCell(new Phrase("Address"));
				PdfPCell addressValue = new PdfPCell(new Phrase(
						currentVictim.getFirstLine() + "\n"
								+ currentVictim.getSecondLine() + "\n"
								+ currentVictim.getPostCode()));
				address.setBorder(Rectangle.LEFT);
				addressValue.setBorder(Rectangle.RIGHT);
				table.addCell(address);
				table.addCell(addressValue);

				// Create country cells and set properties
				PdfPCell country = new PdfPCell(new Phrase("Country of origin"));
				PdfPCell countryValue = new PdfPCell(new Phrase(
						currentVictim.getCountryOrigin()));
				country.setBorder(Rectangle.LEFT);
				countryValue.setBorder(Rectangle.RIGHT);
				table.addCell(country);
				table.addCell(countryValue);

				// Create lawyer name cells and set properties
				PdfPCell lawyer = new PdfPCell(new Phrase("Lawyer name"));
				PdfPCell lawyerValue = new PdfPCell(new Phrase(
						currentVictim.getLawyerTitle() + " "
								+ currentVictim.getLawyerForename() + " "
								+ currentVictim.getLawyerSurname()));
				lawyer.setBorder(Rectangle.LEFT);
				lawyerValue.setBorder(Rectangle.RIGHT);
				table.addCell(lawyer);
				table.addCell(lawyerValue);

				// Create spacer cell and set properties
				PdfPCell spacer = new PdfPCell(new Phrase("\n"));
				spacer.setColspan(2);
				spacer.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(spacer);

				// Create compensation cells and set properties
				PdfPCell compensation = new PdfPCell(new Phrase("Compensation"));
				compensation.setPaddingBottom(10);
				PdfPCell compensationValue = new PdfPCell(new Phrase(
						currentVictim.getCompensation() + ""));
				compensationValue.setPaddingBottom(10);
				compensation.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
				compensationValue.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
				table.addCell(compensation);
				table.addCell(compensationValue);
			}
			document.add(table);
		}
	}

	/**
	 * Creates and fills the table containing the details of the Witnesses
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void createWitnesses(Document document) throws DocumentException {

		// If there are witnesses
		if (crime.getWitness().size() != 0) {

			// Create title, set properties and add to document
			Paragraph witnessesTitle = new Paragraph("Witness(es)", FONT[2]);
			witnessesTitle.setAlignment(0);
			witnessesTitle.setSpacingAfter(20);
			document.add(witnessesTitle);

			// Create table with two columns and set properties
			PdfPTable table = new PdfPTable(2);
			table.setSpacingAfter(10);
			table.setWidthPercentage(100);

			// Add details for each 'Witness'
			for (int i = 0; i < crime.getWitness().size(); i++) {

				// Create local object for the current witness
				Witness currentWitness = crime.getWitness().get(i);

				// Create name header cell and set properties
				PdfPCell name = new PdfPCell(new Phrase(
						currentWitness.getTitle() + " "
								+ currentWitness.getForename() + " "
								+ currentWitness.getSurname(), FONT[3]));
				name.setColspan(2);
				name.setFixedHeight(20);
				name.setBorder(Rectangle.BOX);
				table.addCell(name);

				// Create personal header cell and set properties
				PdfPCell personal = new PdfPCell(new Phrase("Personal details",
						FONT[3]));
				personal.setColspan(2);
				personal.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(personal);

				// Create date of birth cells and set properties
				PdfPCell dob = new PdfPCell(new Phrase("Date of birth"));
				PdfPCell dobValue = new PdfPCell(new Phrase(
						currentWitness.getDay() + "/"
								+ currentWitness.getMonth() + "/"
								+ currentWitness.getYear()));
				dob.setBorder(Rectangle.LEFT);
				dobValue.setBorder(Rectangle.RIGHT);
				table.addCell(dob);
				table.addCell(dobValue);

				// If occupation value is not empty
				if (!currentWitness.getOccupation().isEmpty()) {
					// Create occupation cells and set properties
					PdfPCell occupation = new PdfPCell(new Phrase("Occupation"));
					PdfPCell occupationValue = new PdfPCell(new Phrase(
							currentWitness.getOccupation()));
					occupation.setBorder(Rectangle.LEFT);
					occupationValue.setBorder(Rectangle.RIGHT);
					table.addCell(occupation);
					table.addCell(occupationValue);
				}

				// If first line of the address is not empty
				if (!currentWitness.getFirstLine().isEmpty()) {
					// Add address cells and set properties
					PdfPCell address = new PdfPCell(new Phrase("Address"));
					PdfPCell addressValue = new PdfPCell(new Phrase(
							currentWitness.getFirstLine() + "\n"
									+ currentWitness.getSecondLine() + "\n"
									+ currentWitness.getPostCode()));
					address.setBorder(Rectangle.LEFT);
					addressValue.setBorder(Rectangle.RIGHT);
					table.addCell(address);
					table.addCell(addressValue);
				}

				// Create spacer cell and set properties
				PdfPCell spacer = new PdfPCell(new Phrase("\n"));
				spacer.setColspan(2);
				spacer.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(spacer);

				// Create court header cell and set properties
				PdfPCell court = new PdfPCell(new Phrase("Court details",
						FONT[3]));
				court.setColspan(2);
				court.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(court);

				// Create witness type cells and set properties
				PdfPCell type = new PdfPCell(new Phrase("Type"));
				PdfPCell typeValue = new PdfPCell(new Phrase(currentWitness
						.getWitnessType().toString()));
				type.setBorder(Rectangle.LEFT);
				typeValue.setBorder(Rectangle.RIGHT);
				table.addCell(type);
				table.addCell(typeValue);

				// Create credibility cells and set properties
				PdfPCell credibility = new PdfPCell(new Phrase(
						"Credibility (1-10)"));
				PdfPCell credibilityValue = new PdfPCell(new Phrase(
						currentWitness.getCredibility() + ""));
				credibility.setBorder(Rectangle.LEFT);
				credibilityValue.setBorder(Rectangle.RIGHT);
				table.addCell(credibility);
				table.addCell(credibilityValue);

				// Create statement cells and set properties
				PdfPCell statement = new PdfPCell(new Phrase("Statement"));
				PdfPCell statementValue = new PdfPCell(new Phrase(
						currentWitness.getStatement()));
				statement.setPaddingBottom(10);
				statementValue.setPaddingBottom(10);
				statement.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
				statementValue.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
				table.addCell(statement);
				table.addCell(statementValue);
			}
			document.add(table);
		}
	}

	/**
	 * Creates and fills the table containing the details of the evidence items
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void createEvidence(Document document) throws DocumentException {

		// If there is evidence
		if (crime.getEvidence().size() != 0) {

			// Create title, set properties and add to document
			Paragraph evidenceTitle = new Paragraph("Evidence", FONT[2]);
			evidenceTitle.setAlignment(0);
			evidenceTitle.setSpacingAfter(20);
			document.add(evidenceTitle);

			// Create table with three columns and set properties
			PdfPTable table = new PdfPTable(2);
			table.setSpacingAfter(10);
			table.setWidthPercentage(100);

			// Add details for each evidence item
			for (int i = 0; i < crime.getEvidence().size(); i++) {

				// Create a local object for the evidence
				Evidence currentEvidence = crime.getEvidence().get(i);

				// Create title header cell and set properties
				PdfPCell title = new PdfPCell(new Phrase("Item: "
						+ currentEvidence.getTitle(), FONT[3]));
				title.setColspan(2);
				title.setFixedHeight(20);
				title.setBorder(Rectangle.BOX);
				table.addCell(title);

				// Create type cells and set properties
				PdfPCell type = new PdfPCell(new Phrase("Type"));
				PdfPCell typeValue = new PdfPCell(new Phrase(currentEvidence
						.getType().toString()));
				type.setBorder(Rectangle.LEFT);
				typeValue.setBorder(Rectangle.RIGHT);
				table.addCell(type);
				table.addCell(typeValue);

				// Create description cells and set properties
				PdfPCell description = new PdfPCell(new Phrase("Description"));
				PdfPCell descriptionValue = new PdfPCell(new Phrase(
						currentEvidence.getDescription()));
				description.setBorder(Rectangle.LEFT);
				descriptionValue.setBorder(Rectangle.RIGHT);
				table.addCell(description);
				table.addCell(descriptionValue);

				// Create credibility cells and set properties
				PdfPCell credibility = new PdfPCell(new Phrase(
						"Credibility (1-10)"));
				PdfPCell credibilityValue = new PdfPCell(new Phrase(
						currentEvidence.getCredibility() + ""));
				credibility.setBorder(Rectangle.LEFT);
				credibilityValue.setBorder(Rectangle.RIGHT);
				table.addCell(credibility);
				table.addCell(credibilityValue);

				// Create supports cells and set properties
				PdfPCell supports = new PdfPCell(new Phrase("Supports"));
				PdfPCell supportsValue = new PdfPCell(new Phrase(
						currentEvidence.getSupports().toString()));
				supports.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
				supports.setPaddingBottom(10);
				supportsValue.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
				supportsValue.setPaddingBottom(10);
				table.addCell(supports);
				table.addCell(supportsValue);
			}
			document.add(table);
		}
	}

	/**
	 * Creates the report document's footer
	 * 
	 * @param document
	 *            The report document
	 * @throws DocumentException
	 *             Occurs if there is an error writing to the document
	 */
	private void footer(Document document) throws DocumentException {

		// Create table with two columns and set properties
		PdfPTable table = new PdfPTable(2);
		table.setHorizontalAlignment(0);
		table.setSpacingBefore(10);
		table.setWidths(new int[] { 150, 180 });

		// Create cells for judge name and set properties
		PdfPCell name = new PdfPCell(new Phrase("Judge name (block capitals):",
				FONT[3]));
		PdfPCell nameValue = new PdfPCell();
		name.setBorder(Rectangle.NO_BORDER);
		nameValue.setBorder(Rectangle.BOTTOM);
		table.addCell(name);
		table.addCell(nameValue);

		// Create cells for judge signature and set properties
		PdfPCell signature = new PdfPCell(new Phrase("Judge signature:",
				FONT[3]));
		PdfPCell signatureValue = new PdfPCell();
		signature.setBorder(Rectangle.NO_BORDER);
		signatureValue.setBorder(Rectangle.BOTTOM);
		signature.setPaddingTop(10);
		signature.setPaddingBottom(10);
		signature.setColspan(2);
		signatureValue.setColspan(2);
		signatureValue.setFixedHeight(40);
		table.addCell(signature);
		table.addCell(signatureValue);

		Paragraph end = new Paragraph("END OF REPORT", FONT[3]);
		end.setSpacingBefore(20);
		end.setAlignment(1);

		// Add table to the document
		document.add(table);

		// Add END OF REPORT to the document
		document.add(end);
	}

	/**
	 * An array of predefined fonts for use in the report
	 */
	private static final Font[] FONT = new Font[4];
	static {
		FONT[0] = new Font(FontFamily.HELVETICA, 24);
		FONT[1] = new Font(FontFamily.HELVETICA, 18, Font.BOLD);
		FONT[2] = new Font(FontFamily.HELVETICA, 16, Font.BOLD);
		FONT[3] = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	}

}
