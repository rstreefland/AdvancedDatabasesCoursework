package uk.ac.rdg.rhys.program1;

/**
 * <h1>Evidence.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing data of the
 * evidence. This class provides getters and setters to access all of its
 * properties. This class makes use of enumerated types in order to restrict the
 * possible values stored.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Evidence {

	private int id;
	private String title;
	private String description;
	private int credibility;

	public enum Supports {
		BLANK(""), ACCUSED("Accused"), PROSECUTION("Prosecution");

		// Define and set the string of the enumerated type
		final private String value;
		Supports(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static Supports fromString(String text) {
			if (text != null) {
				for (Supports b : Supports.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return Supports.BLANK;
		}
	}

	public enum Type {
		BLANK(""), TESTIMONY("Testimony"), DOCUMENTARY("Documentary"), PHYSICAL(
				"Physical"), DIGITAL("Digital"), EXCULPATORY("Exculpatory"), DEMONSTRATIVE(
				"Demonstrative"), EYEWITNESS("Eyewitness Identification"), GENETIC(
				"Genetic"), LIES("Lies");

		// Define and set the string of the enumerated type
		final private String value;

		Type(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static Type fromString(String text) {
			if (text != null) {
				for (Type b : Type.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return Type.BLANK;
		}
	}

	private Supports supports;
	private Type type;

	public Evidence() {
		id = 0;
		title = "";
		description = "";
		credibility = 0;

		supports = Supports.BLANK;
		type = Type.BLANK;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

	public Supports getSupports() {
		return supports;
	}

	public void setSupports(Supports supports) {
		this.supports = supports;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
