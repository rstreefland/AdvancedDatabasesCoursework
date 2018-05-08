package uk.ac.rdg.rhys.program1;

/**
 * <h1>Specific.java</h1>
 * <p>
 * This class is used solely as a means of temporarily storing data of the
 * crime specific information. This class provides getters and setters to access all of its
 * properties. This class makes use of enumerated types in order to restrict the
 * possible values stored.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-02-17
 */
public class Specific {

	private String fraudType;
	private int fraudCost;
	private String substanceName;
	private String quantitySeized;
	private int streetValue;
	private String associatedCrimes;

	public enum FraudTarget {
		BLANK(""), INDIVIDUAL("Individual"), MULTIPLE("Multiple Individuals"), ORGANISATION(
				"Organisation"), COMPANY("Company"), GOVERNMENT("Government");

		// Define and set the string of the enumerated type
		final private String value;

		FraudTarget(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static FraudTarget fromString(String text) {
			if (text != null) {
				for (FraudTarget b : FraudTarget.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return FraudTarget.BLANK;
		}
	}

	public enum ManslaughterType {
		BLANK(""), VOLUNTARY("Voluntary"), INVOLUNTARY("Involuntary"), CONSRTUCTIVE(
				"Constructive"), NEGLIENT("Criminally neglient");

		// Define and set the string of the enumerated type
		final private String value;

		ManslaughterType(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static ManslaughterType fromString(String text) {
			if (text != null) {
				for (ManslaughterType b : ManslaughterType.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return ManslaughterType.BLANK;
		}
	}

	public enum ManslaughterSeriousness {
		BLANK(""), MISDEMEANOUR("Misdemenour"), FELONY("Felony");

		// Define and set the string of the enumerated type
		final private String value;

		ManslaughterSeriousness(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static ManslaughterSeriousness fromString(String text) {
			if (text != null) {
				for (ManslaughterSeriousness b : ManslaughterSeriousness
						.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return ManslaughterSeriousness.BLANK;
		}
	}

	public enum ManslaughterLossControl {
		BLANK(""), YES("Yes"), NO("No");

		// Define and set the string of the enumerated type
		final private String value;

		ManslaughterLossControl(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static ManslaughterLossControl fromString(String text) {
			if (text != null) {
				for (ManslaughterLossControl b : ManslaughterLossControl
						.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return ManslaughterLossControl.BLANK;
		}
	}

	public enum VehicleInvolved {
		BLANK(""), YES("Yes"), NO("No");

		// Define and set the string of the enumerated type
		final private String value;

		VehicleInvolved(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static VehicleInvolved fromString(String text) {
			if (text != null) {
				for (VehicleInvolved b : VehicleInvolved.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return VehicleInvolved.BLANK;
		}
	}

	public enum SubstanceClassification {
		BLANK(""), A("A"), B("B"), C("C"), TEMPORARY("Temporary");

		// Define and set the string of the enumerated type
		final private String value;

		SubstanceClassification(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static SubstanceClassification fromString(String text) {
			if (text != null) {
				for (SubstanceClassification b : SubstanceClassification
						.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return SubstanceClassification.BLANK;
		}
	}

	public enum SubstanceType {
		BLANK(""), STIMULANT("Stimulant"), DEPRESSANT("Depressant"), OPIATE(
				"Opiate"), OPIOID("Opioid"), HALLUCINOGEN("Hallucinogen");

		// Define and set the string of the enumerated type
		final private String value;

		SubstanceType(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static SubstanceType fromString(String text) {
			if (text != null) {
				for (SubstanceType b : SubstanceType.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return SubstanceType.BLANK;
		}
	}

	public enum SubstanceConsumption {
		BLANK(""), SMOKING("Smoking"), ISUFFLATION("Insufflation"), ORAL("Oral"), INJECTION(
				"Injection"), SUPPOSITORIES("Suppositories");

		// Define and set the string of the enumerated type
		final private String value;

		SubstanceConsumption(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static SubstanceConsumption fromString(String text) {
			if (text != null) {
				for (SubstanceConsumption b : SubstanceConsumption.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return SubstanceConsumption.BLANK;
		}
	}

	public enum SubstanceIntentToSupply {
		BLANK(""), YES("Yes"), NO("No");

		// Define and set the string of the enumerated type
		final private String value;

		SubstanceIntentToSupply(String s) {
			value = s;
		}

		// Return the string defined for the enumerated type
		public String toString() {
			return value;
		}

		// Convert an incoming string to the enumerated type
		public static SubstanceIntentToSupply fromString(String text) {
			if (text != null) {
				for (SubstanceIntentToSupply b : SubstanceIntentToSupply
						.values()) {
					if (text.equalsIgnoreCase(b.value)) {
						return b;
					}
				}
			}
			return SubstanceIntentToSupply.BLANK;
		}
	}

	private FraudTarget fraudTarget;
	private ManslaughterType manslaughterType;
	private ManslaughterSeriousness manslaughterSeriousness;
	private ManslaughterLossControl manslaughterLossControl;
	private VehicleInvolved vehicleInvolved;
	private SubstanceClassification substanceClassification;
	private SubstanceType substanceType;
	private SubstanceConsumption substanceConsumption;
	private SubstanceIntentToSupply substanceIntentToSupply;

	public Specific() {
		fraudType = "";
		fraudCost = 0;
		substanceName = "";
		quantitySeized = "";
		streetValue = 0;
		associatedCrimes = "";
		
		substanceConsumption = SubstanceConsumption.BLANK;
		setSubstanceIntentToSupply(SubstanceIntentToSupply.BLANK);
	}

	public String getFraudType() {
		return fraudType;
	}

	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}

	public int getFraudCost() {
		return fraudCost;
	}

	public void setFraudCost(int fraudCost) {
		this.fraudCost = fraudCost;
	}

	public String getSubstanceName() {
		return substanceName;
	}

	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}

	public String getAssociatedCrimes() {
		return associatedCrimes;
	}

	public void setAssociatedCrimes(String associatedCrimes) {
		this.associatedCrimes = associatedCrimes;
	}

	public ManslaughterType getManslaughterType() {
		return manslaughterType;
	}

	public void setManslaughterType(ManslaughterType manslaughterType) {
		this.manslaughterType = manslaughterType;
	}

	public ManslaughterSeriousness getManslaughterSeriousness() {
		return manslaughterSeriousness;
	}

	public void setManslaughterSeriousness(
			ManslaughterSeriousness manslaughterSeriousness) {
		this.manslaughterSeriousness = manslaughterSeriousness;
	}

	public SubstanceClassification getSubstanceClassification() {
		return substanceClassification;
	}

	public void setSubstanceClassification(
			SubstanceClassification substanceClassification) {
		this.substanceClassification = substanceClassification;
	}

	public SubstanceType getSubstanceType() {
		return substanceType;
	}

	public void setSubstanceType(SubstanceType substanceType) {
		this.substanceType = substanceType;
	}

	public SubstanceConsumption getSubstanceConsumption() {
		return substanceConsumption;
	}

	public void setSubstanceConsumption(
			SubstanceConsumption substanceConsumption) {
		this.substanceConsumption = substanceConsumption;
	}

	public VehicleInvolved getVehicleInvolved() {
		return vehicleInvolved;
	}

	public void setVehicleInvolved(VehicleInvolved vehicleInvolved) {
		this.vehicleInvolved = vehicleInvolved;
	}

	public ManslaughterLossControl getManslaughterLossControl() {
		return manslaughterLossControl;
	}

	public void setManslaughterLossControl(
			ManslaughterLossControl manslaughterLossControl) {
		this.manslaughterLossControl = manslaughterLossControl;
	}

	public FraudTarget getFraudTarget() {
		return fraudTarget;
	}

	public void setFraudTarget(FraudTarget fraudTarget) {
		this.fraudTarget = fraudTarget;
	}

	public String getQuantitySeized() {
		return quantitySeized;
	}

	public void setQuantitySeized(String quantitySeized) {
		this.quantitySeized = quantitySeized;
	}

	public int getStreetValue() {
		return streetValue;
	}

	public void setStreetValue(int streetValue) {
		this.streetValue = streetValue;
	}

	public SubstanceIntentToSupply getSubstanceIntentToSupply() {
		return substanceIntentToSupply;
	}

	public void setSubstanceIntentToSupply(
			SubstanceIntentToSupply substanceIntentToSupply) {
		this.substanceIntentToSupply = substanceIntentToSupply;
	}
}
