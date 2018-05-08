package uk.ac.rdg.rhys.program1;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import uk.ac.rdg.rhys.program1.Accused.Verdict;
import uk.ac.rdg.rhys.program1.Crime.CourtType;
import uk.ac.rdg.rhys.program1.Crime.CrimeType;
import uk.ac.rdg.rhys.program1.Specific.FraudTarget;
import uk.ac.rdg.rhys.program1.Specific.ManslaughterLossControl;
import uk.ac.rdg.rhys.program1.Specific.ManslaughterType;
import uk.ac.rdg.rhys.program1.Specific.SubstanceClassification;
import uk.ac.rdg.rhys.program1.Specific.SubstanceConsumption;
import uk.ac.rdg.rhys.program1.Specific.SubstanceIntentToSupply;
import uk.ac.rdg.rhys.program1.Specific.SubstanceType;
import uk.ac.rdg.rhys.program1.Specific.VehicleInvolved;

/**
 * <h1>Exporter.java</h1>
 * <p>
 * This class converts the data stored in the crime object into a DOM Document
 * using the JDOM library. The DOM document is then written to a .xml file.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Exporter {

	// Declare default namespace
	private Namespace ns = Namespace
			.getNamespace("http://rhys.streefland.co.uk/");

	/**
	 * 
	 * Constructor for the class. It calls the writer() method of the class to
	 * start the export process
	 * 
	 * @param fileName
	 *            The name and path of the XML file that will be created
	 * @param crime
	 *            The crime object that contains the crime data
	 */
	public Exporter(String fileName, Crime crime) {
		try {
			writer(crime, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the process of creating a DOM document based on the data in the
	 * crime object and then writing the DOM document to the .xml file. It does
	 * not write any empty XML tags to the file.
	 * 
	 * @param crime
	 *            The crime object that contains the crime data
	 * @param fileName
	 *            The name and path of the XML file that will be created
	 * @throws IOException
	 *             Occurs if the .xml file cannot be written to
	 */
	private void writer(Crime crime, String fileName) throws IOException {

		// Create DOM Document
		Document doc = new Document();

		// Create the root element 'crime' and set its type
		Element crimeElement = new Element("crime", ns);
		crimeElement.setAttribute("type", crime.getCrimeType().toString());
		doc.setRootElement(crimeElement);

		// If the crime has any accused then add the accused
		if (crime.getAccused().size() != 0) {
			crimeElement.addContent(accused(crime));
		}

		// If the crime has any victims then add the victims
		if (crime.getCrimeType() != CrimeType.SUBSTANCEABUSE
				&& crime.getVictim().size() != 0) {
			crimeElement.addContent(victims(crime));
		}

		// If the crime has any witnesses then add the witnesses
		if (crime.getCrimeType() != CrimeType.FRAUD
				&& crime.getWitness().size() != 0) {
			crimeElement.addContent(witnesses(crime));
		}

		// If the crime has any evidence items then add the evidence
		if (crime.getEvidence().size() != 0) {
			crimeElement.addContent(evidence(crime));
		}

		// Add the court and crime specific information
		crimeElement.addContent(court(crime));
		crimeElement.addContent(specific(crime));

		// Write the DOM document to the .xml file
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		xmlOutputter.output(doc, new FileOutputStream(fileName));
	}

	/**
	 * Copy information about the accused from the crime object into the DOM
	 * Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing information of the accused people
	 */
	public Element accused(Crime currentCrime) {

		// Create an element for the accused
		Element accused = new Element("accused", ns);

		// Write each accused object to the accused element
		for (int j = 0; j < currentCrime.getAccused().size(); j++) {

			// Create a local object for the current accused person
			Accused currentAccused = currentCrime.getAccused().get(j);

			Element accusedPerson = new Element("accused_person", ns);
			accusedPerson.setAttribute("id", "" + currentAccused.getId());

			Element name = new Element("name", ns);
			name.addContent(new Element("title", ns).setText(currentAccused
					.getTitle()));
			name.addContent(new Element("forename", ns).setText(currentAccused
					.getForename()));
			name.addContent(new Element("surname", ns).setText(currentAccused
					.getSurname()));
			accusedPerson.addContent(name);

			Element dob = new Element("dob", ns);

			dob.addContent(new Element("day", ns).setText(""
					+ currentAccused.getDay()));
			dob.addContent(new Element("month", ns).setText(""
					+ currentAccused.getMonth()));
			dob.addContent(new Element("year", ns).setText(""
					+ currentAccused.getYear()));
			accusedPerson.addContent(dob);

			Element address = new Element("address", ns);
			address.addContent(new Element("first_line", ns)
					.setText(currentAccused.getFirstLine()));
			address.addContent(new Element("second_line", ns)
					.setText(currentAccused.getSecondLine()));
			address.addContent(new Element("postcode", ns)
					.setText(currentAccused.getPostCode()));
			accusedPerson.addContent(address);

			accusedPerson.addContent(new Element("country_of_origin", ns)
					.setText(currentAccused.getCountryOrigin()));

			// Declare namespace for lawyer name
			Namespace lawyerNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/lawyer");

			Element lawyer = new Element("lawyer", lawyerNamespace);
			lawyer.addContent(new Element("title", lawyerNamespace)
					.setText(currentAccused.getLawyerTitle()));
			lawyer.addContent(new Element("forename", lawyerNamespace)
					.setText(currentAccused.getLawyerForename()));
			lawyer.addContent(new Element("surname", lawyerNamespace)
					.setText(currentAccused.getLawyerSurname()));
			accusedPerson.addContent(lawyer);

			accusedPerson.addContent(new Element("involvement", ns)
					.setText(currentAccused.getInvolvement()));

			// Declare namespace for previous conviction
			Namespace previousConvictionNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/previousconviction");

			Element previousConviction = new Element("previous_conviction",
					previousConvictionNamespace);

			// Only write the value if it's not blank
			if (currentAccused.getPreviousConvictionTitle() != null)
				previousConviction.addContent(new Element("title",
						previousConvictionNamespace).setText(currentAccused
						.getPreviousConvictionTitle()));

			// Only write the value if it's not blank
			if (currentAccused.getDay() != 0) {
				Element date = new Element("date", previousConvictionNamespace);
				date.addContent(new Element("day", previousConvictionNamespace)
						.setText("" + currentAccused.getPreviousConvictionDay()));
				date.addContent(new Element("month",
						previousConvictionNamespace).setText(""
						+ currentAccused.getPreviousConvictionMonth()));
				date.addContent(new Element("year", previousConvictionNamespace)
						.setText(""
								+ currentAccused.getPreviousConvictionYear()));
				previousConviction.addContent(date);
			}

			// Only write the value if it's not blank
			if (currentAccused.getPreviousConvictionDescription() != null)
				previousConviction.addContent(new Element("description",
						previousConvictionNamespace).setText(currentAccused
						.getPreviousConvictionDescription()));

			accusedPerson.addContent(previousConviction);

			// Only write the value if it's not blank
			if (currentAccused.getVerdict() != Verdict.BLANK)
				accusedPerson.addContent(new Element("court_verdict", ns)
						.setText(currentAccused.getVerdict().toString()));

			// Only write the value if it's not blank
			if (!currentAccused.getSentenceLength().isEmpty()
					|| !currentAccused.getSentenceDescription().isEmpty()) {
				Element sentence = new Element("sentence", ns);

				if (!currentAccused.getSentenceLength().isEmpty())
					sentence.addContent(new Element("length", ns)
							.setText(currentAccused.getSentenceLength()));

				if (!currentAccused.getSentenceDescription().isEmpty())
					sentence.addContent(new Element("description", ns)
							.setText(currentAccused.getSentenceDescription()));
				accusedPerson.addContent(sentence);
			}

			accusedPerson.addContent(new Element("fine", ns).setText(""
					+ currentAccused.getFine()));

			// Add the current accused person to the accused element
			accused.addContent(accusedPerson);
		}
		return accused;
	}

	/**
	 * Copy information about the victims from the crime object into the DOM
	 * Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing information of the victims
	 */
	public Element victims(Crime currentCrime) {

		// Create an element for the victims
		Element victims = new Element("victims", ns);

		// Write each victim the victims element
		for (int j = 0; j < currentCrime.getVictim().size(); j++) {

			// Create a local object for the current victim
			Victim currentVictim = currentCrime.getVictim().get(j);

			Element victim = new Element("victim", ns);
			victim.setAttribute("id", "" + currentVictim.getId());

			Element name = new Element("name", ns);
			name.addContent(new Element("title", ns).setText(currentVictim
					.getTitle()));
			name.addContent(new Element("forename", ns).setText(currentVictim
					.getForename()));
			name.addContent(new Element("surname", ns).setText(currentVictim
					.getSurname()));
			victim.addContent(name);

			Element dob = new Element("dob", ns);
			dob.addContent(new Element("day", ns).setText(""
					+ currentVictim.getDay()));
			dob.addContent(new Element("month", ns).setText(""
					+ currentVictim.getMonth()));
			dob.addContent(new Element("year", ns).setText(""
					+ currentVictim.getYear()));
			victim.addContent(dob);

			Element address = new Element("address", ns);
			address.addContent(new Element("first_line", ns)
					.setText(currentVictim.getFirstLine()));
			address.addContent(new Element("second_line", ns)
					.setText(currentVictim.getSecondLine()));
			address.addContent(new Element("postcode", ns)
					.setText(currentVictim.getPostCode()));
			victim.addContent(address);

			victim.addContent(new Element("country_of_origin", ns)
					.setText(currentVictim.getCountryOrigin()));

			// Declare namespace for lawyer name
			Namespace lawyerNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/lawyer");

			Element lawyer = new Element("lawyer", lawyerNamespace);
			lawyer.addContent(new Element("title", lawyerNamespace)
					.setText(currentVictim.getLawyerTitle()));
			lawyer.addContent(new Element("forename", lawyerNamespace)
					.setText(currentVictim.getLawyerForename()));
			lawyer.addContent(new Element("surname", lawyerNamespace)
					.setText(currentVictim.getLawyerSurname()));
			victim.addContent(lawyer);

			victim.addContent(new Element("compensation", lawyerNamespace)
					.setText("" + currentVictim.getCompensation()));

			// Add the current victim to the victims element
			victims.addContent(victim);
		}
		return victims;
	}

	/**
	 * Copy information about the witnesses from the crime object into the DOM
	 * Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing information of the witnesses
	 */
	public Element witnesses(Crime currentCrime) {

		Element witnesses = new Element("witnesses", ns);

		// Declare witness namespace
		Namespace witnessNamespace = Namespace
				.getNamespace("http://rhys.streefland.co.uk/witness");

		for (int j = 0; j < currentCrime.getWitness().size(); j++) {
			Witness currentWitness = currentCrime.getWitness().get(j);

			Element witness = new Element("witness", witnessNamespace);
			witness.setAttribute("id", "" + currentWitness.getId());

			Element name = new Element("name", witnessNamespace);
			name.addContent(new Element("title", witnessNamespace)
					.setText(currentWitness.getTitle()));
			name.addContent(new Element("forename", witnessNamespace)
					.setText(currentWitness.getForename()));
			name.addContent(new Element("surname", witnessNamespace)
					.setText(currentWitness.getSurname()));
			witness.addContent(name);

			Element dob = new Element("dob", witnessNamespace);
			dob.addContent(new Element("day", witnessNamespace).setText(""
					+ currentWitness.getDay()));
			dob.addContent(new Element("month", witnessNamespace).setText(""
					+ currentWitness.getMonth()));
			dob.addContent(new Element("year", witnessNamespace).setText(""
					+ currentWitness.getYear()));
			witness.addContent(dob);

			// Only write the value if it's not blank
			if (!currentWitness.getPostCode().isEmpty()) {
				Element address = new Element("address", witnessNamespace);
				address.addContent(new Element("first_line", witnessNamespace)
						.setText(currentWitness.getFirstLine()));
				address.addContent(new Element("second_line", witnessNamespace)
						.setText(currentWitness.getSecondLine()));
				address.addContent(new Element("postcode", witnessNamespace)
						.setText(currentWitness.getPostCode()));
				witness.addContent(address);
			}

			witness.addContent(new Element("occupation", witnessNamespace)
					.setText(currentWitness.getOccupation()));

			witness.addContent(new Element("type", witnessNamespace)
					.setText(currentWitness.getWitnessType().toString()));

			witness.addContent(new Element("credibility", witnessNamespace)
					.setText("" + currentWitness.getCredibility()));

			witness.addContent(new Element("statement", witnessNamespace)
					.setText("" + currentWitness.getStatement()));

			witnesses.addContent(witness);
		}
		return witnesses;
	}

	/**
	 * Copy information about the evidence items from the crime object into the
	 * DOM Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing information of evidence items
	 */
	public Element evidence(Crime currentCrime) {

		// Create an element for the evidence
		Element evidence = new Element("evidence", ns);

		// Declare evidence namespace
		Namespace evidenceNamespace = Namespace
				.getNamespace("http://rhys.streefland.co.uk/evidence");

		// Create a local object for the current evidence item
		for (int j = 0; j < currentCrime.getEvidence().size(); j++) {

			// Create a local object for the current evidence item
			Evidence currentEvidence = currentCrime.getEvidence().get(j);

			Element evidenceItem = new Element("evidence_item",
					evidenceNamespace);
			evidenceItem.setAttribute("id", "" + currentEvidence.getId());

			evidenceItem.addContent(new Element("title", evidenceNamespace)
					.setText(currentEvidence.getTitle()));

			evidenceItem.addContent(new Element("type", evidenceNamespace)
					.setText(currentEvidence.getType().toString()));

			evidenceItem
					.addContent(new Element("description", evidenceNamespace)
							.setText(currentEvidence.getDescription()));

			evidenceItem.addContent(new Element("credibility",
					evidenceNamespace).setText(""
					+ currentEvidence.getCredibility()));

			evidenceItem.addContent(new Element("supports", evidenceNamespace)
					.setText(currentEvidence.getSupports().toString()));

			// Add the current evidence item to the evidence element
			evidence.addContent(evidenceItem);
		}
		return evidence;
	}

	/**
	 * Copy information about the court from the crime object into the DOM
	 * Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing information of the court
	 */
	public Element court(Crime currentCrime) {

		// Create an element for the court data
		Element court = new Element("court", ns);

		court.addContent(new Element("case_number", ns).setText(""
				+ currentCrime.getCaseNumber()));

		// Only write the value if it's not blank
		if (currentCrime.getDay() != 0) {
			Element courtDate = new Element("court_date", ns);
			courtDate.addContent(new Element("day", ns).setText(""
					+ currentCrime.getDay()));
			courtDate.addContent(new Element("month", ns).setText(""
					+ currentCrime.getMonth()));
			courtDate.addContent(new Element("year", ns).setText(""
					+ currentCrime.getYear()));
			court.addContent(courtDate);
		}

		court.addContent(new Element("court_name", ns).setText(currentCrime
				.getCourtName()));

		// Only write the value if it's not blank
		if (currentCrime.getCourtType() != CourtType.BLANK)
			court.addContent(new Element("court_type", ns).setText(currentCrime
					.getCourtType().toString()));

		court.addContent(new Element("case_summary", ns).setText(currentCrime
				.getSummary()));

		return court;
	}

	/**
	 * Copy the crime specific information from the crime object into the DOM
	 * Document
	 *
	 * @param currentCrime
	 *            The crime object that contains the crime data
	 * @return The DOM element containing the crime specific information
	 */
	public Element specific(Crime currentCrime) {

		// Create an element for the crime specific data
		Specific currentSpecific = currentCrime.getSpecific();

		Element specific = new Element("crime_specific", ns);

		// Only write the relevant information for the current crime
		switch (currentCrime.getCrimeType()) {
		case FRAUD:
			// Declare namespace for fraud
			Namespace fraudNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/fraud");

			// Only write the value if it's not blank
			if (!currentSpecific.getFraudType().isEmpty())
				specific.addContent(new Element("type", fraudNamespace)
						.setText(currentSpecific.getFraudType()));

			// Only write the value if it's not blank
			if (currentSpecific.getFraudTarget() != null
					&& currentSpecific.getFraudTarget() != FraudTarget.BLANK)
				specific.addContent(new Element("target", fraudNamespace)
						.setText(currentSpecific.getFraudTarget().toString()));

			specific.addContent(new Element("cost", fraudNamespace).setText(""
					+ currentSpecific.getFraudCost()));
			break;
		case MANSLAUGHTER:
			// Declare namespace for manslaughter
			Namespace manslaughterNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/manslaughter");

			// Only write the value if it's not blank
			if (currentSpecific.getManslaughterType() != null
					&& currentSpecific.getManslaughterType() != ManslaughterType.BLANK)
				specific.addContent(new Element("type", manslaughterNamespace)
						.setText(currentSpecific.getManslaughterType()
								.toString()));
			
			// Only write the value if it's not blank
			if (currentSpecific.getManslaughterSeriousness() != null)
			{
			specific.addContent(new Element("seriousness",
					manslaughterNamespace).setText(""
					+ currentSpecific.getManslaughterSeriousness().toString()));
			}

			// Only write the value if it's not blank
			if (currentSpecific.getManslaughterLossControl() != null
					&& currentSpecific.getManslaughterLossControl() != ManslaughterLossControl.BLANK)
				specific.addContent(new Element("loss_of_control",
						manslaughterNamespace).setText(""
						+ currentSpecific.getManslaughterLossControl()
								.toString()));

			// Only write the value if it's not blank
			if (currentSpecific.getVehicleInvolved() != null
					&& currentSpecific.getVehicleInvolved() != VehicleInvolved.BLANK)
				specific.addContent(new Element("vehicle_involved",
						manslaughterNamespace).setText(currentSpecific
						.getVehicleInvolved().toString()));
			break;
		case SUBSTANCEABUSE:
			// Declare namespace for substance abuse
			Namespace substanceAbuseNamespace = Namespace
					.getNamespace("http://rhys.streefland.co.uk/substanceabuse");

			// Only write the value if it's not blank
			if (!currentSpecific.getSubstanceName().isEmpty())
				specific.addContent(new Element("name", substanceAbuseNamespace)
						.setText(currentSpecific.getSubstanceName()));

			// Only write the value if it's not blank
			if (currentSpecific.getSubstanceType() != null
					&& currentSpecific.getSubstanceType() != SubstanceType.BLANK)
				specific.addContent(new Element("type", substanceAbuseNamespace)
						.setText(currentSpecific.getSubstanceType().toString()));

			// Only write the value if it's not blank
			if (currentSpecific.getSubstanceClassification() != null
					&& currentSpecific.getSubstanceClassification() != SubstanceClassification.BLANK)
				specific.addContent(new Element("classification",
						substanceAbuseNamespace).setText(currentSpecific
						.getSubstanceClassification().toString()));

			// Only write the value if it's not blank
			if (currentSpecific.getSubstanceConsumption() != null
					&& currentSpecific.getSubstanceConsumption() != SubstanceConsumption.BLANK)
				;
			specific.addContent(new Element("consumption",
					substanceAbuseNamespace).setText(currentSpecific
					.getSubstanceConsumption().toString()));

			// Only write the value if it's not blank
			if (!currentSpecific.getQuantitySeized().isEmpty())
				;
			specific.addContent(new Element("quantity_seized",
					substanceAbuseNamespace).setText(currentSpecific
					.getQuantitySeized().toString()));
			specific.addContent(new Element("street_value",
					substanceAbuseNamespace).setText(Integer
					.toString(currentSpecific.getStreetValue())));

			// Only write the value if it's not blank
			if (currentSpecific.getSubstanceIntentToSupply() != null
					&& currentSpecific.getSubstanceIntentToSupply() != SubstanceIntentToSupply.BLANK)
				;
			specific.addContent(new Element("intent_to_supply",
					substanceAbuseNamespace).setText(currentSpecific
					.getSubstanceIntentToSupply().toString()));

			// Only write the value if it's not blank
			if (!currentSpecific.getAssociatedCrimes().isEmpty())
				;
			specific.addContent(new Element("associated_crimes",
					substanceAbuseNamespace).setText(currentSpecific
					.getAssociatedCrimes()));
			break;
		default:
			break;
		}

		return specific;
	}

}
