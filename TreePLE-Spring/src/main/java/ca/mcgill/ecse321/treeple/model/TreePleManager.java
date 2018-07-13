/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Date;

// line 7 "../../../../../TreePLE.ump"
public class TreePleManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TreePleManager Associations
  private List<Tree> tree;
  private List<Species> species;
  private List<Municipality> municipality;
  private List<Report> report;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TreePleManager()
  {
    tree = new ArrayList<Tree>();
    species = new ArrayList<Species>();
    municipality = new ArrayList<Municipality>();
    report = new ArrayList<Report>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Tree getTree(int index)
  {
    Tree aTree = tree.get(index);
    return aTree;
  }

  public List<Tree> getTree()
  {
    List<Tree> newTree = Collections.unmodifiableList(tree);
    return newTree;
  }

  public int numberOfTree()
  {
    int number = tree.size();
    return number;
  }

  public boolean hasTree()
  {
    boolean has = tree.size() > 0;
    return has;
  }

  public int indexOfTree(Tree aTree)
  {
    int index = tree.indexOf(aTree);
    return index;
  }

  public Species getSpecies(int index)
  {
    Species aSpecies = species.get(index);
    return aSpecies;
  }

  public List<Species> getSpecies()
  {
    List<Species> newSpecies = Collections.unmodifiableList(species);
    return newSpecies;
  }

  public int numberOfSpecies()
  {
    int number = species.size();
    return number;
  }

  public boolean hasSpecies()
  {
    boolean has = species.size() > 0;
    return has;
  }

  public int indexOfSpecies(Species aSpecies)
  {
    int index = species.indexOf(aSpecies);
    return index;
  }

  public Municipality getMunicipality(int index)
  {
    Municipality aMunicipality = municipality.get(index);
    return aMunicipality;
  }

  public List<Municipality> getMunicipality()
  {
    List<Municipality> newMunicipality = Collections.unmodifiableList(municipality);
    return newMunicipality;
  }

  public int numberOfMunicipality()
  {
    int number = municipality.size();
    return number;
  }

  public boolean hasMunicipality()
  {
    boolean has = municipality.size() > 0;
    return has;
  }

  public int indexOfMunicipality(Municipality aMunicipality)
  {
    int index = municipality.indexOf(aMunicipality);
    return index;
  }

  public Report getReport(int index)
  {
    Report aReport = report.get(index);
    return aReport;
  }

  public List<Report> getReport()
  {
    List<Report> newReport = Collections.unmodifiableList(report);
    return newReport;
  }

  public int numberOfReport()
  {
    int number = report.size();
    return number;
  }

  public boolean hasReport()
  {
    boolean has = report.size() > 0;
    return has;
  }

  public int indexOfReport(Report aReport)
  {
    int index = report.indexOf(aReport);
    return index;
  }

  public static int minimumNumberOfTree()
  {
    return 0;
  }

  public boolean addTree(Tree aTree)
  {
    boolean wasAdded = false;
    if (tree.contains(aTree)) { return false; }
    tree.add(aTree);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTree(Tree aTree)
  {
    boolean wasRemoved = false;
    if (tree.contains(aTree))
    {
      tree.remove(aTree);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addTreeAt(Tree aTree, int index)
  {  
    boolean wasAdded = false;
    if(addTree(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTree()) { index = numberOfTree() - 1; }
      tree.remove(aTree);
      tree.add(index, aTree);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTreeAt(Tree aTree, int index)
  {
    boolean wasAdded = false;
    if(tree.contains(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTree()) { index = numberOfTree() - 1; }
      tree.remove(aTree);
      tree.add(index, aTree);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTreeAt(aTree, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfSpecies()
  {
    return 0;
  }

  public boolean addSpecies(Species aSpecies)
  {
    boolean wasAdded = false;
    if (species.contains(aSpecies)) { return false; }
    species.add(aSpecies);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSpecies(Species aSpecies)
  {
    boolean wasRemoved = false;
    if (species.contains(aSpecies))
    {
      species.remove(aSpecies);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addSpeciesAt(Species aSpecies, int index)
  {  
    boolean wasAdded = false;
    if(addSpecies(aSpecies))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecies()) { index = numberOfSpecies() - 1; }
      species.remove(aSpecies);
      species.add(index, aSpecies);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSpeciesAt(Species aSpecies, int index)
  {
    boolean wasAdded = false;
    if(species.contains(aSpecies))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecies()) { index = numberOfSpecies() - 1; }
      species.remove(aSpecies);
      species.add(index, aSpecies);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSpeciesAt(aSpecies, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfMunicipality()
  {
    return 0;
  }

  public boolean addMunicipality(Municipality aMunicipality)
  {
    boolean wasAdded = false;
    if (municipality.contains(aMunicipality)) { return false; }
    municipality.add(aMunicipality);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMunicipality(Municipality aMunicipality)
  {
    boolean wasRemoved = false;
    if (municipality.contains(aMunicipality))
    {
      municipality.remove(aMunicipality);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addMunicipalityAt(Municipality aMunicipality, int index)
  {  
    boolean wasAdded = false;
    if(addMunicipality(aMunicipality))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMunicipality()) { index = numberOfMunicipality() - 1; }
      municipality.remove(aMunicipality);
      municipality.add(index, aMunicipality);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMunicipalityAt(Municipality aMunicipality, int index)
  {
    boolean wasAdded = false;
    if(municipality.contains(aMunicipality))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMunicipality()) { index = numberOfMunicipality() - 1; }
      municipality.remove(aMunicipality);
      municipality.add(index, aMunicipality);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMunicipalityAt(aMunicipality, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfReport()
  {
    return 0;
  }

  public boolean addReport(Report aReport)
  {
    boolean wasAdded = false;
    if (report.contains(aReport)) { return false; }
    report.add(aReport);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReport(Report aReport)
  {
    boolean wasRemoved = false;
    if (report.contains(aReport))
    {
      report.remove(aReport);
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
      if(index > numberOfReport()) { index = numberOfReport() - 1; }
      report.remove(aReport);
      report.add(index, aReport);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReportAt(Report aReport, int index)
  {
    boolean wasAdded = false;
    if(report.contains(aReport))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReport()) { index = numberOfReport() - 1; }
      report.remove(aReport);
      report.add(index, aReport);
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
    tree.clear();
    species.clear();
    municipality.clear();
    report.clear();
  }

}