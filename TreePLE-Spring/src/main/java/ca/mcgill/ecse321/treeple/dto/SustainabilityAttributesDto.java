package ca.mcgill.ecse321.treeple.dto;

public class SustainabilityAttributesDto {
	private double biodiversityIndex;
	private double carbonSequestration;
	
	
	public SustainabilityAttributesDto() {
		super();
	}

	public SustainabilityAttributesDto(double biodiversityIndex, double carbonSequestration) {
		super();
		this.biodiversityIndex = biodiversityIndex;
		this.carbonSequestration = carbonSequestration;
	}
	
	public double getBiodiversityIndex() {
		return biodiversityIndex;
	}

	public void setBiodiversityIndex(double biodiversityIndex) {
		this.biodiversityIndex = biodiversityIndex;
	}

	public double getCarbonSequestration() {
		return carbonSequestration;
	}

	public void setCarbonSequestration(double carbonSequestration) {
		this.carbonSequestration = carbonSequestration;
	}

}
