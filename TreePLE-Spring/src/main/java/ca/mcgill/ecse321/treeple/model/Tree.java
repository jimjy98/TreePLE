/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Date;

// line 14 "../../../../../TreePLE.ump"
public class Tree
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Landtype { Residential, Institutional, Park, Municipal }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  public static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tree Attributes
  private double x;
  private double y;
  private double height;
  private double diameter;
  private Landtype landType;

  //Autounique Attributes
  private int id;

  //Tree Associations
  private Species species;
  private Municipality municipality;
  private List<Report> reports;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tree(double aX, double aY, double aHeight, double aDiameter, Landtype aLandType, Species aSpecies, Municipality aMunicipality)
  {
    x = aX;
    y = aY;
    height = aHeight;
    diameter = aDiameter;
    landType = aLandType;
    id = nextId++;
    if (!setSpecies(aSpecies))
    {
      throw new RuntimeException("Unable to create Tree due to aSpecies");
    }
    boolean didAddMunicipality = setMunicipality(aMunicipality);
    if (!didAddMunicipality)
    {
      throw new RuntimeException("Unable to create tree due to municipality");
    }
    reports = new ArrayList<Report>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setX(double aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(double aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public boolean setHeight(double aHeight)
  {
    boolean wasSet = false;
    height = aHeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiameter(double aDiameter)
  {
    boolean wasSet = false;
    diameter = aDiameter;
    wasSet = true;
    return wasSet;
  }

  public boolean setLandType(Landtype aLandType)
  {
    boolean wasSet = false;
    landType = aLandType;
    wasSet = true;
    return wasSet;
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }

  public double getHeight()
  {
    return height;
  }

  public double getDiameter()
  {
    return diameter;
  }

  public Landtype getLandType()
  {
    return landType;
  }

  public int getId()
  {
    return id;
  }

  public Species getSpecies()
  {
    return species;
  }

  public Municipality getMunicipality()
  {
    return municipality;
  }

  public Report getReport(int index)
  {
    Report aReport = reports.get(index);
    return aReport;
  }

  public List<Report> getReports()
  {
    List<Report> newReports = Collections.unmodifiableList(reports);
    return newReports;
  }

  public int numberOfReports()
  {
    int number = reports.size();
    return number;
  }

  public boolean hasReports()
  {
    boolean has = reports.size() > 0;
    return has;
  }

  public int indexOfReport(Report aReport)
  {
    int index = reports.indexOf(aReport);
    return index;
  }

  public boolean setSpecies(Species aNewSpecies)
  {
    boolean wasSet = false;
    if (aNewSpecies != null)
    {
      species = aNewSpecies;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setMunicipality(Municipality aMunicipality)
  {
    boolean wasSet = false;
    if (aMunicipality == null)
    {
      return wasSet;
    }

    Municipality existingMunicipality = municipality;
    municipality = aMunicipality;
    if (existingMunicipality != null && !existingMunicipality.equals(aMunicipality))
    {
      existingMunicipality.removeTree(this);
    }
    municipality.addTree(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfReports()
  {
    return 0;
  }

  public boolean addReport(Report aReport)
  {
    boolean wasAdded = false;
    if (reports.contains(aReport)) { return false; }
    reports.add(aReport);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReport(Report aReport)
  {
    boolean wasRemoved = false;
    if (reports.contains(aReport))
    {
      reports.remove(aReport);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addReportAt(Report aReport, int index)
  {  
    boolean wasAdded = false;
    if(addReport(aReport))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReports()) { index = numberOfReports() - 1; }
      reports.remove(aReport);
      reports.add(index, aReport);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReportAt(Report aReport, int index)
  {
    boolean wasAdded = false;
    if(reports.contains(aReport))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReports()) { index = numberOfReports() - 1; }
      reports.remove(aReport);
      reports.add(index, aReport);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReportAt(aReport, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    species = null;
    Municipality placeholderMunicipality = municipality;
    this.municipality = null;
    if(placeholderMunicipality != null)
    {
      placeholderMunicipality.removeTree(this);
    }
    reports.clear();
  }

  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tree other = (Tree) obj;
		if (id != other.id)
			return false;
		return true;
	}

  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "," +
            "height" + ":" + getHeight()+ "," +
            "diameter" + ":" + getDiameter()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "landType" + "=" + (getLandType() != null ? !getLandType().equals(this)  ? getLandType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "species = "+(getSpecies()!=null?Integer.toHexString(System.identityHashCode(getSpecies())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "municipality = "+(getMunicipality()!=null?Integer.toHexString(System.identityHashCode(getMunicipality())):"null");
  }
}