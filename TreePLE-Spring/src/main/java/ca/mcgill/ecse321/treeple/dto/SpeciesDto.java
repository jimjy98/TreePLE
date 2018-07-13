package ca.mcgill.ecse321.treeple.dto;

public class SpeciesDto {
	public SpeciesDto(String nameEnglish) {
		super();
		this.nameEnglish = nameEnglish;
	}

	private String nameEnglish;

	public String getNameEnglish() {
		return nameEnglish;
	}

	public void setNameEnglish(String nameEnglish) {
		this.nameEnglish = nameEnglish;
	}

	public SpeciesDto() {
		super();
	}
	
}
