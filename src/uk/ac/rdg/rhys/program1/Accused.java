package uk.ac.rdg.rhys.program1;

/**
 * <h1>Accused.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing data of the
 * accused. This class provides getters and setters to access all of its
 * properties. This class makes use of enumerated types in order to restrict the
 * possible values stored.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Accused {

	private int id;
	protected String title;
	private String forename;
	private String surname;
	private int day;
	private int month;
	private int year;
	private String firstLine;
	private String secondLine;
	private String postCode;
	private String countryOrigin;
	private String lawyerTitle;
	private String lawyerForename;
	private String lawyerSurname;
	private String involvement;
	private String previousConvictionTitle;
	private int previousConvictionDay;
	private int previousConvictionMonth;
	private int previousConvictionYear;
	private String previousConvictionDescription;
	private String sentenceLength;
	private String sentenceDescription;
	private int fine;

	public enum Verdict {
		BLANK(""), INNOCENT("Innocent"), GUILTY("Guilty");

		// Define and set the string of the enumerated type
		final private String value;

		Verdict(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static Verdict fromString(String text) {
			if (text != null) {
				for (Verdict b : Verdict.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return Verdict.BLANK;
		}
	}

	private Verdict verdict;

	public Accused() {
		id = 0;
		title = "";
		forename = "";
		surname = "";
		day = 0;
		month = 0;
		year = 0;
		firstLine = "";
		secondLine = "";
		postCode = "";
		countryOrigin = "";
		lawyerTitle = "";
		lawyerForename = "";
		lawyerSurname = "";
		involvement = "";
		previousConvictionTitle = "";
		previousConvictionDay = 0;
		previousConvictionMonth = 0;
		previousConvictionYear = 0;
		previousConvictionDescription = "";
		sentenceLength = "";
		sentenceDescription = "";
		fine = 0;
		verdict = Verdict.BLANK;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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

	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}

	public String getSecondLine() {
		return secondLine;
	}

	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountryOrigin() {
		return countryOrigin;
	}

	public void setCountryOrigin(String countryOrigin) {
		this.countryOrigin = countryOrigin;
	}

	public String getLawyerTitle() {
		return lawyerTitle;
	}

	public void setLawyerTitle(String lawyerTitle) {
		this.lawyerTitle = lawyerTitle;
	}

	public String getLawyerForename() {
		return lawyerForename;
	}

	public void setLawyerForename(String lawyerForename) {
		this.lawyerForename = lawyerForename;
	}

	public String getLawyerSurname() {
		return lawyerSurname;
	}

	public void setLawyerSurname(String lawyerSurname) {
		this.lawyerSurname = lawyerSurname;
	}

	public String getInvolvement() {
		return involvement;
	}

	public void setInvolvement(String involvement) {
		this.involvement = involvement;
	}

	public String getSentenceLength() {
		return sentenceLength;
	}

	public void setSentenceLength(String sentenceLength) {
		this.sentenceLength = sentenceLength;
	}

	public String getSentenceDescription() {
		return sentenceDescription;
	}

	public void setSentenceDescription(String sentenceDescription) {
		this.sentenceDescription = sentenceDescription;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}

	public Verdict getVerdict() {
		return verdict;
	}

	public void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}

	public String getPreviousConvictionTitle() {
		return previousConvictionTitle;
	}

	public void setPreviousConvictionTitle(String previousConvictionTitle) {
		this.previousConvictionTitle = previousConvictionTitle;
	}

	public int getPreviousConvictionDay() {
		return previousConvictionDay;
	}

	public void setPreviousConvictionDay(int previousConvictionDay) {
		this.previousConvictionDay = previousConvictionDay;
	}

	public int getPreviousConvictionMonth() {
		return previousConvictionMonth;
	}

	public void setPreviousConvictionMonth(int previousConvictionMonth) {
		this.previousConvictionMonth = previousConvictionMonth;
	}

	public int getPreviousConvictionYear() {
		return previousConvictionYear;
	}

	public void setPreviousConvictionYear(int previousConvictionYear) {
		this.previousConvictionYear = previousConvictionYear;
	}

	public String getPreviousConvictionDescription() {
		return previousConvictionDescription;
	}

	public void setPreviousConvictionDescription(
			String previousConvictionDescription) {
		this.previousConvictionDescription = previousConvictionDescription;
	}

}
