/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;

import javax.swing.ImageIcon;

// line 36 "../../../../../TreePLE.ump"
public class Species
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Species Attributes
  private String acronym;
  private String nameLatin;
  private String nameEnglish;
  private String nameFrench;
  private ImageIcon icon;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Species(String aAcronym, String aNameLatin, String aNameEnglish, String aNameFrench, ImageIcon aIcon)
  {
    acronym = aAcronym;
    nameLatin = aNameLatin;
    nameEnglish = aNameEnglish;
    nameFrench = aNameFrench;
    icon = aIcon;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAcronym(String aAcronym)
  {
    boolean wasSet = false;
    acronym = aAcronym;
    wasSet = true;
    return wasSet;
  }

  public boolean setNameLatin(String aNameLatin)
  {
    boolean wasSet = false;
    nameLatin = aNameLatin;
    wasSet = true;
    return wasSet;
  }

  public boolean setNameEnglish(String aNameEnglish)
  {
    boolean wasSet = false;
    nameEnglish = aNameEnglish;
    wasSet = true;
    return wasSet;
  }

  public boolean setNameFrench(String aNameFrench)
  {
    boolean wasSet = false;
    nameFrench = aNameFrench;
    wasSet = true;
    return wasSet;
  }

  public boolean setIcon(ImageIcon aIcon)
  {
    boolean wasSet = false;
    icon = aIcon;
    wasSet = true;
    return wasSet;
  }

  public String getAcronym()
  {
    return acronym;
  }

  public String getNameLatin()
  {
    return nameLatin;
  }

  public String getNameEnglish()
  {
    return nameEnglish;
  }

  public String getNameFrench()
  {
    return nameFrench;
  }

  public ImageIcon getIcon()
  {
    return icon;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "acronym" + ":" + getAcronym()+ "," +
            "nameLatin" + ":" + getNameLatin()+ "," +
            "nameEnglish" + ":" + getNameEnglish()+ "," +
            "nameFrench" + ":" + getNameFrench()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "icon" + "=" + (getIcon() != null ? !getIcon().equals(this)  ? getIcon().toString().replaceAll("  ","    ") : "this" : "null");
  }
}