package ca.mcgill.ecse321.treeple.dto;

import java.sql.Date;

public class TreeDto {
	
	private int id;
	private SpeciesDto species;
	private MunicipalityDto municipality;
	private double x;
	private double y;
	private double height;
	private double diameter;
	private String landType;
	private String status;
	private Date reportDate;
	private String reporter;
	
	public TreeDto(){
		
	};
	
	public TreeDto(int id, int x, int y, double height, double diameter) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
		this.diameter = diameter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public double getX() {
		return x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public double getY() {
		return y;
	}

	public void setY(double d) {
		this.y = d;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public SpeciesDto getSpecies() {
		return species;
	}

	public void setSpecies(SpeciesDto species) {
		this.species = species;
	}

	public MunicipalityDto getMunicipality() {
		return municipality;
	}

	public void setMunicipality(MunicipalityDto municipality) {
		this.municipality = municipality;
	}

	public String getLandType() {
		return landType;
	}

	public void setLandType(String landType) {
		this.landType = landType;
	}
	
	

}
