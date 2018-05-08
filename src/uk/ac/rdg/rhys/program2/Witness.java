package uk.ac.rdg.rhys.program2;

/**
 * <h1>Witness.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing data of the
 * Witnesses. This class provides getters and setters to access all of its
 * properties. This class makes use of enumerated types in order to restrict the
 * possible values stored.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Witness {
	
	private int id;
	private String title;
	private String forename;
	private String surname;
	private int day;
	private int month;
	private int year;
	private String firstLine;
	private String secondLine;
	private String postCode;
	private String occupation;
	private int credibility;
	private String statement;
	
	public enum WitnessType {
		BLANK(""), HERESAY("Heresay"),EXPERT("Expert");
		
		// Define and set the string of the enumerated type
		final private String value;
		WitnessType(String s) {
			value = s;
		}
		
		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}
		
		// Convert an incoming string to the enumerated type
		public static WitnessType fromString(String text) {
		    if (text != null) {
		      for (WitnessType b : WitnessType.values()) {
		        if (text.equalsIgnoreCase(b.value)) {
		          return b;
		        }
		      }
		    }
		    return WitnessType.BLANK;
		  }	
	}
	
	private WitnessType witnessType;
	
	public Witness()
	{
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
		credibility = 0;
		statement = "";
		witnessType = WitnessType.BLANK;
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


	public WitnessType getWitnessType() {
		return witnessType;
	}


	public void setWitnessType(WitnessType witnessType) {
		this.witnessType = witnessType;
	}


	public int getCredibility() {
		return credibility;
	}


	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
}
