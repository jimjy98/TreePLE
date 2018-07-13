package ca.mcgill.ecse321.treeple.dto;

import java.util.List;

public class ForcastingBiodiversityDto {
	private List<String> species;
	private SustainabilityAttributesDto attributes;
	public ForcastingBiodiversityDto(List<String> species, SustainabilityAttributesDto attributes) {
		super();
		this.species = species;
		this.attributes = attributes;
	}
	public ForcastingBiodiversityDto() {
		super();
	}
	public List<String> getSpecies() {
		return species;
	}
	public void setSpecies(List<String> species) {
		this.species = species;
	}
	public SustainabilityAttributesDto getAttributes() {
		return attributes;
	}
	public void setAttributes(SustainabilityAttributesDto attributes) {
		this.attributes = attributes;
	}
	
}
