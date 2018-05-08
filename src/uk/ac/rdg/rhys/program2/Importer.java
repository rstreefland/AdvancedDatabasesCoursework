package uk.ac.rdg.rhys.program2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import uk.ac.rdg.rhys.program2.Accused.Verdict;
import uk.ac.rdg.rhys.program2.Crime.CourtType;
import uk.ac.rdg.rhys.program2.Crime.CrimeType;
import uk.ac.rdg.rhys.program2.Evidence.Supports;
import uk.ac.rdg.rhys.program2.Evidence.Type;
import uk.ac.rdg.rhys.program2.Specific.FraudTarget;
import uk.ac.rdg.rhys.program2.Specific.ManslaughterLossControl;
import uk.ac.rdg.rhys.program2.Specific.ManslaughterSeriousness;
import uk.ac.rdg.rhys.program2.Specific.ManslaughterType;
import uk.ac.rdg.rhys.program2.Specific.SubstanceClassification;
import uk.ac.rdg.rhys.program2.Specific.SubstanceConsumption;
import uk.ac.rdg.rhys.program2.Specific.SubstanceIntentToSupply;
import uk.ac.rdg.rhys.program2.Specific.SubstanceType;
import uk.ac.rdg.rhys.program2.Specific.VehicleInvolved;
import uk.ac.rdg.rhys.program2.Witness.WitnessType;

import com.itextpdf.text.DocumentException;

/**
 * <h1>Importer.java</h1>
 * <p>
 * This class opens the specified XML file and parses the data into the crime
 * object using the JDOM library.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Importer {

	// Declare default namespace
	private Namespace ns = Namespace
			.getNamespace("http://rhys.streefland.co.uk/");

	// Create a local crime object so all methods have access
	Crime crime;

	/**
	 * Constructor for the class. It calls the reader() method of the class to
	 * start the import process
	 * 
	 * @param fileName
	 *            The name and path of the XML file to parse
	 */
	public Importer(String fileName) {
		try {
			reader(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses the generator class to generate the report by supplying the crime
	 * object containing the crime data. Creates the file name of the report
	 * automatically in the following format
	 * (caseNumber_crimeType_dd-mm-yy_report.pdf)
	 */
	public void generateReport() {

		// Create the file path by concatenating the relevant data
		String date = crime.getDay() + "-" + crime.getMonth() + "-"
				+ crime.getYear();
		String fileName = crime.getCaseNumber() + "_" + crime.getCrimeType()
				+ "_" + date + "_report.pdf";
		String filePath = "cases/reports/" + fileName;

		// Try to generate the report using the generator class
		try {
			Generator generator = new Generator(crime);
			generator.createPdf(filePath);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses the generator class to generate the report by supplying the crime
	 * object containing the crime data. This overloaded instance of the method
	 * uses a file name of the users choice rather than an automatically
	 * generated one.
	 * 
	 * @param reportPath
	 *            The name and path of the report
	 */
	public void generateReport(String reportPath) {
		// Try to generate the report using the generator class
		try {
			Generator generator = new Generator(crime);
			generator.createPdf(reportPath);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the process of extracting the relevant data from the DOM Document
	 * and storing it in the crime object
	 * 
	 * @param fileName
	 *            The name and path of the XML file to parse
	 * @throws IOException
	 *             Occurs if the .xml file cannot be opened
	 */
	private void reader(String fileName) throws IOException {

		// Create objects required for import
		SAXBuilder saxBuilder = new SAXBuilder();
		File xmlFile = new File(fileName);

		// Create document for import
		Document document;

		try {
			// Import XML file into the document
			document = (Document) saxBuilder.build(xmlFile);

			// Get the root element of the document
			Element crimeElement = document.getRootElement();

			// Get the crime type and use it to initialize the crime object
			CrimeType crimeType = CrimeType.fromString(crimeElement
					.getAttributeValue("type"));
			crime = new Crime(crimeType, 0);

			// Call separate methods to import the relevant data
			accused(crimeElement);
			victims(crimeElement);
			witnesses(crimeElement);
			evidence(crimeElement);
			court(crimeElement);
			specific(crimeElement);

		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Import information about the accused into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void accused(Element crimeElement) {

		// Get the accused element
		Element accused = crimeElement.getChild("accused", ns);

		// Don't do anything if accused is empty
		if (accused != null) {
			// Get a list of the accused people
			List<Element> accusedList = accused.getChildren("accused_person",
					ns);

			// Import each accused person
			for (int i = 0; i < accusedList.size(); i++) {

				// Created an element and an object for the current accused
				// person
				Element accusedElement = accusedList.get(i);
				Accused currentAccused = new Accused();

				currentAccused.setId(Integer.parseInt(accusedElement
						.getAttributeValue("id")));

				Element name = accusedElement.getChild("name", ns);
				currentAccused.setTitle(name.getChildText("title", ns));
				currentAccused.setForename(name.getChildText("forename", ns));
				currentAccused.setSurname(name.getChildText("surname", ns));

				Element dob = accusedElement.getChild("dob", ns);
				if (dob != null) {
					currentAccused.setDay(Integer.parseInt(dob.getChildText(
							"day", ns)));
					currentAccused.setMonth(Integer.parseInt(dob.getChildText(
							"month", ns)));
					currentAccused.setYear(Integer.parseInt(dob.getChildText(
							"year", ns)));
				}

				Element address = accusedElement.getChild("address", ns);
				currentAccused.setFirstLine(address.getChildText("first_line",
						ns));
				currentAccused.setSecondLine(address.getChildText(
						"second_line", ns));
				currentAccused
						.setPostCode(address.getChildText("postcode", ns));

				currentAccused.setCountryOrigin(accusedElement.getChildText(
						"country_of_origin", ns));

				// Declare namespace for lawyer name
				Namespace lawyerNamespace = Namespace
						.getNamespace("http://rhys.streefland.co.uk/lawyer");

				Element lawyer = accusedElement.getChild("lawyer",
						lawyerNamespace);
				currentAccused.setLawyerTitle(lawyer.getChildText("title",
						lawyerNamespace));
				currentAccused.setLawyerForename(lawyer.getChildText(
						"forename", lawyerNamespace));
				currentAccused.setLawyerSurname(lawyer.getChildText("surname",
						lawyerNamespace));

				currentAccused.setInvolvement(accusedElement.getChildText(
						"involvement", ns));

				// Declare namespace for previous conviction
				Namespace previousConvictionNamespace = Namespace
						.getNamespace("http://rhys.streefland.co.uk/previousconviction");

				Element previousConviction = accusedElement.getChild(
						"previous_conviction", previousConvictionNamespace);
				currentAccused.setPreviousConvictionTitle(previousConviction
						.getChildText("title", previousConvictionNamespace));

				Element date = previousConviction.getChild("date",
						previousConvictionNamespace);
				if (date != null) {
					currentAccused.setPreviousConvictionDay(Integer
							.parseInt(date.getChildText("day",
									previousConvictionNamespace)));
					currentAccused.setPreviousConvictionMonth(Integer
							.parseInt(date.getChildText("month",
									previousConvictionNamespace)));
					currentAccused.setPreviousConvictionYear(Integer
							.parseInt(date.getChildText("year",
									previousConvictionNamespace)));
				}

				currentAccused
						.setPreviousConvictionDescription(previousConviction
								.getChildText("description",
										previousConvictionNamespace));

				currentAccused.setVerdict(Verdict.fromString(accusedElement
						.getChildText("court_verdict", ns)));

				Element sentence = accusedElement.getChild("sentence", ns);
				if (sentence != null) {
					currentAccused.setSentenceLength(sentence.getChildText(
							"length", ns));
					currentAccused.setSentenceDescription(sentence
							.getChildText("description", ns));
				}

				if (accusedElement.getChild("fine", ns) != null)
					currentAccused.setFine(Integer.parseInt(accusedElement
							.getChildText("fine", ns)));

				crime.getAccused().add(currentAccused);
			}
		}
	}

	/**
	 * Import information about the victims into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void victims(Element crimeElement) {

		// Get the victims element
		Element victims = crimeElement.getChild("victims", ns);

		// Don't do anything if victims is empty
		if (victims != null) {

			// Get a list of victims
			List<Element> victimList = victims.getChildren("victim", ns);

			// Import each victim
			for (int i = 0; i < victimList.size(); i++) {

				// Create an element and an object for the current accused
				// person
				Element victimElement = victimList.get(i);
				Victim currentVictim = new Victim();

				currentVictim.setId(Integer.parseInt(victimElement
						.getAttributeValue("id")));

				Element name = victimElement.getChild("name", ns);
				currentVictim.setTitle(name.getChildText("title", ns));
				currentVictim.setForename(name.getChildText("forename", ns));
				currentVictim.setSurname(name.getChildText("surname", ns));

				Element dob = victimElement.getChild("dob", ns);
				if (dob != null) {
					currentVictim.setDay(Integer.parseInt(dob.getChildText(
							"day", ns)));
					currentVictim.setMonth(Integer.parseInt(dob.getChildText(
							"month", ns)));
					currentVictim.setYear(Integer.parseInt(dob.getChildText(
							"year", ns)));
				}

				Element address = victimElement.getChild("address", ns);
				currentVictim.setFirstLine(address.getChildText("first_line",
						ns));
				currentVictim.setSecondLine(address.getChildText("second_line",
						ns));
				currentVictim.setPostCode(address.getChildText("postcode", ns));

				currentVictim.setCountryOrigin(victimElement.getChildText(
						"country_of_origin", ns));
				
				// Declare namespace for lawyer name
				Namespace lawyerNamespace = Namespace
						.getNamespace("http://rhys.streefland.co.uk/lawyer");

				Element lawyer = victimElement.getChild("lawyer", lawyerNamespace);
				currentVictim.setLawyerTitle(lawyer.getChildText("title", lawyerNamespace));
				currentVictim.setLawyerForename(lawyer.getChildText("forename",
						lawyerNamespace));
				currentVictim.setLawyerSurname(lawyer.getChildText("surname",
						lawyerNamespace));

				if (victimElement.getChild("compensation", ns) != null)
					currentVictim.setCompensation(Integer
							.parseInt(victimElement.getChildText(
									"compensation", ns)));

				crime.getVictim().add(currentVictim);
			}
		}
	}

	/**
	 * Import information about the witnesses into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void witnesses(Element crimeElement) {

		// Get the witnesses element
		Element witnesses = crimeElement.getChild("witnesses", ns);

		// Don't do anything if witnesses is empty
		if (witnesses != null) {
			
			// Declare witness namespace
			Namespace witnessNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/witness");

			// Get a list of witnesses
			List<Element> witnessList = witnesses.getChildren("witness", witnessNamespace);

			// Import each witness
			for (int i = 0; i < witnessList.size(); i++) {

				// Create an element and an object for the current witness
				Element witnessElement = witnessList.get(i);
				Witness currentWitness = new Witness();

				currentWitness.setId(Integer.parseInt(witnessElement
						.getAttributeValue("id")));

				Element name = witnessElement.getChild("name", witnessNamespace);
				currentWitness.setTitle(name.getChildText("title", witnessNamespace));
				currentWitness.setForename(name.getChildText("forename", witnessNamespace));
				currentWitness.setSurname(name.getChildText("surname", witnessNamespace));

				Element dob = witnessElement.getChild("dob", witnessNamespace);
				if (dob != null) {
					currentWitness.setDay(Integer.parseInt(dob.getChildText(
							"day", witnessNamespace)));
					currentWitness.setMonth(Integer.parseInt(dob.getChildText(
							"month", witnessNamespace)));
					currentWitness.setYear(Integer.parseInt(dob.getChildText(
							"year", witnessNamespace)));
				}

				Element address = witnessElement.getChild("address", witnessNamespace);

				if (address != null) {
					currentWitness.setFirstLine(address.getChildText(
							"first_line", witnessNamespace));
					currentWitness.setSecondLine(address.getChildText(
							"second_line", witnessNamespace));
					currentWitness.setPostCode(address.getChildText("postcode",
							witnessNamespace));
				}

				currentWitness.setWitnessType(WitnessType
						.fromString(witnessElement.getChildText("type",
								witnessNamespace)));

				if (witnessElement.getChild("occupation", witnessNamespace) != null)
					currentWitness.setOccupation(witnessElement.getChildText(
							"occupation", witnessNamespace));

				if (witnessElement.getChild("credibility", witnessNamespace) != null)
					currentWitness.setCredibility(Integer
							.parseInt(witnessElement.getChildText(
									"credibility", witnessNamespace)));

				currentWitness.setStatement(witnessElement.getChildText(
						"statement", witnessNamespace));

				crime.getWitness().add(currentWitness);
			}
		}
	}

	/**
	 * Import information about the witnesses into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void evidence(Element crimeElement) {

		// Get the evidence element
		Element evidence = crimeElement.getChild("evidence", ns);
		
		// Declare evidence namespace
		Namespace evidenceNamespace = Namespace
				.getNamespace("http://rhys.streefland.co.uk/evidence");


		// Don't do anything if evidence is empty
		if (evidence != null) {

			// Get a list of evidence items
			List<Element> evidenceItemList = evidence.getChildren(
					"evidence_item", evidenceNamespace);

			// Import each evidence item
			for (int i = 0; i < evidenceItemList.size(); i++) {

				// Create an element and an object for the current item of evidence
				Element evidenceElement = evidenceItemList.get(i);
				Evidence currentEvidence = new Evidence();

				currentEvidence.setTitle(evidenceElement.getChildText(
						"title", evidenceNamespace));

				currentEvidence.setType(Type.fromString(evidenceElement
						.getChildText("type", evidenceNamespace)));

				currentEvidence.setDescription(evidenceElement.getChildText(
						"description", evidenceNamespace));

				if (evidenceElement.getChild("credibility", evidenceNamespace) != null)
					currentEvidence.setCredibility(Integer
							.parseInt(evidenceElement.getChildText(
									"credibility", evidenceNamespace)));

				currentEvidence.setSupports(Supports.fromString(evidenceElement
						.getChildText("supports", evidenceNamespace)));

				crime.getEvidence().add(currentEvidence);
			}
		}

	}

	/**
	 * Import information about the court into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void court(Element crimeElement) {

		// Get the court element
		Element court = crimeElement.getChild("court", ns);

		// Don't do anything if court is empty
		if (court != null) {
			crime.setCaseNumber(Integer.parseInt(court.getChildText(
					"case_number", ns)));

			Element date = court.getChild("court_date", ns);
			if (date != null) {
				crime.setDay(Integer.parseInt(date.getChildText("day", ns)));
				crime.setMonth(Integer.parseInt(date.getChildText("month", ns)));
				crime.setYear(Integer.parseInt(date.getChildText("year", ns)));
			}

			crime.setCourtName(court.getChildText("court_name", ns));

			crime.setCourtType(CourtType.fromString(court.getChildText(
					"court_type", ns)));

			// Don't import the summary if it is empty
			if (!crime.getSummary().isEmpty())
				crime.setSummary(court.getChildText("case_summary", ns));
		}
	}

	/**
	 * Import the crime specific information into the crime object
	 * 
	 * @param crimeElement
	 *            The element containing the crime data
	 */
	private void specific(Element crimeElement) {

		// Get the crime specific element
		Element specific = crimeElement.getChild("crime_specific", ns);

		// Don't do anything if specific is empty
		if (specific != null) {
			Specific currentSpecific = new Specific();

			switch (crime.getCrimeType()) {
			case FRAUD:
				// Declare namespace for fraud
				Namespace fraudNamespace = Namespace
				.getNamespace("http://rhys.streefland.co.uk/fraud");

				if (specific.getChild("type", fraudNamespace) != null)
					currentSpecific.setFraudType(specific.getChildText(
							"type", fraudNamespace));
				if (specific.getChild("target", fraudNamespace) != null)
					currentSpecific.setFraudTarget(FraudTarget
							.fromString(specific.getChildText("target",
									fraudNamespace)));
				if (specific.getChild("cost", fraudNamespace) != null)
					currentSpecific.setFraudCost(Integer.parseInt(specific
							.getChildText("cost", fraudNamespace)));
				break;
			case MANSLAUGHTER:
				// Declare namespace for manslaughter
				Namespace manslaughterNamespace = Namespace
						.getNamespace("http://rhys.streefland.co.uk/manslaughter");
				
				currentSpecific.setManslaughterType(ManslaughterType
						.fromString(specific.getChildText("type",
								manslaughterNamespace)));
				currentSpecific
						.setManslaughterSeriousness(ManslaughterSeriousness
								.fromString(specific.getChildText(
										"seriousness", manslaughterNamespace)));
				currentSpecific
						.setManslaughterLossControl(ManslaughterLossControl
								.fromString(specific.getChildText(
										"loss_of_control", manslaughterNamespace)));
				currentSpecific.setVehicleInvolved(VehicleInvolved
						.fromString(specific.getChildText("vehicle_involved",
								manslaughterNamespace)));
				break;
			case SUBSTANCEABUSE:
				// Declare namespace for substance abuse
				Namespace substanceAbuseNamespace = Namespace
				.getNamespace("http://rhys.streefland.co.uk/substanceabuse");
				
				currentSpecific.setSubstanceName(specific.getChildText(
						"name", substanceAbuseNamespace));

				currentSpecific
						.setSubstanceType(SubstanceType.fromString(specific
								.getChildText("type", substanceAbuseNamespace)));

				currentSpecific
						.setSubstanceClassification(SubstanceClassification
								.fromString(specific.getChildText(
										"classification", substanceAbuseNamespace)));

				currentSpecific.setSubstanceConsumption(SubstanceConsumption
						.fromString(specific.getChildText(
								"consumption", substanceAbuseNamespace)));

				currentSpecific.setQuantitySeized(specific.getChildText(
						"quantity_seized", substanceAbuseNamespace));

				if (specific.getChild("street_value") != null)
					currentSpecific.setStreetValue(Integer.parseInt(specific
							.getChildText("street_value", substanceAbuseNamespace)));

				currentSpecific
						.setSubstanceIntentToSupply(SubstanceIntentToSupply
								.fromString(specific.getChildText(
										"intent_to_supply", substanceAbuseNamespace)));

				currentSpecific.setAssociatedCrimes(specific.getChildText(
						"associated_crimes", substanceAbuseNamespace));
				break;
			default:
				break;
			}
			crime.setSpecific(currentSpecific);
		}
	}

}
