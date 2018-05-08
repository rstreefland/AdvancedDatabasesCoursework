package uk.ac.rdg.rhys.program2;

import java.util.ArrayList;

/**
 * <h1>Crime.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing the data of the
 * crime. This class provides getters and setters to access all of its
 * properties. This class holds other objects which are used to represent the
 * accused, victims, witnesses and evidence items. This class makes use of
 * enumerated types in order to restrict the possible values stored.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Crime {

	private String courtName;
	private int caseNumber;
	private int day;
	private int month;
	private int year;
	private String summary;

	private ArrayList<Accused> accused = new ArrayList<Accused>();
	private ArrayList<Victim> victim = new ArrayList<Victim>();
	private ArrayList<Witness> witness = new ArrayList<Witness>();
	private ArrayList<Evidence> evidence = new ArrayList<Evidence>();
	private Specific specific;

	public enum CrimeType {
		BLANK(""), FRAUD("Fraud"), MANSLAUGHTER("Manslaughter"), SUBSTANCEABUSE(
				"Substance Abuse");

		// Define and set the string of the enumerated type
		final private String value;

		CrimeType(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static CrimeType fromString(String text) {
			if (text != null) {
				for (CrimeType b : CrimeType.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return CrimeType.BLANK;
		}
	}

	public enum CourtType {
		BLANK(""), MAGISTRATES("Magistrates court"), YOUTH("Youth court"), CROWN(
				"Crown court");

		// Define and set the string of the enumerated type
		final private String value;

		CourtType(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static CourtType fromString(String text) {
			if (text != null) {
				for (CourtType b : CourtType.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return CourtType.BLANK;
		}
	}

	private CrimeType crimeType;
	private CourtType courtType;

	public Crime(CrimeType newCrimeType, int newCaseNumber) {
		specific = new Specific();

		crimeType = newCrimeType;
		caseNumber = newCaseNumber;
		courtName = "";
		courtType = CourtType.BLANK;
		summary = "";
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(int caseNumber) {
		this.caseNumber = caseNumber;
	}

	public CourtType getCourtType() {
		return courtType;
	}

	public void setCourtType(CourtType courtType) {
		this.courtType = courtType;
	}

	public Specific getSpecific() {
		return specific;
	}

	public void setSpecific(Specific specific) {
		this.specific = specific;
	}

	public ArrayList<Accused> getAccused() {
		return accused;
	}

	public void setAccused(ArrayList<Accused> accused) {
		this.accused = accused;
	}

	public ArrayList<Victim> getVictim() {
		return victim;
	}

	public void setVictim(ArrayList<Victim> victim) {
		this.victim = victim;
	}

	public ArrayList<Witness> getWitness() {
		return witness;
	}

	public void setWitness(ArrayList<Witness> witness) {
		this.witness = witness;
	}

	public ArrayList<Evidence> getEvidence() {
		return evidence;
	}

	public void setEvidence(ArrayList<Evidence> evidence) {
		this.evidence = evidence;
	}

	public CrimeType getCrimeType() {
		return crimeType;
	}

	public void setCrimeType(CrimeType crimeType) {
		this.crimeType = crimeType;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
