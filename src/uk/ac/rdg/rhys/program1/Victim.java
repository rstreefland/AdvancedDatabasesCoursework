package uk.ac.rdg.rhys.program1;

/**
 * <h1>Victim.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing data of the
 * victims. This class provides getters and setters to access all of its
 * properties.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Victim {
	
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
	private String countryOrigin;
	private String lawyerTitle;
	private String lawyerForename;
	private String lawyerSurname;
	private int compensation;
	
	public Victim()
	{
		id = 0;
		title = "blank";
		forename = "blank";
		surname = "blank";
		day = 0;
		month = 0;
		year = 0;
		firstLine = "blank";
		secondLine = "blank";
		postCode = "blank";
		countryOrigin = "blank";
		lawyerTitle = "";
		lawyerForename = "";
		lawyerSurname = "";
		compensation = 0;
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

	public int getCompensation() {
		return compensation;
	}

	public void setCompensation(int compensation) {
		this.compensation = compensation;
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

}
