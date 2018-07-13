/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.sql.Date;

// line 26 "../../../../../TreePLE.ump"
public class Report
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ReportType { Planted, Diseased, Marked, CuttedDown }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Report Attributes
  private Date reportDate;
  private ReportType reportType;
  private String Reporter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Report(Date aReportDate, ReportType aReportType, String aReporter)
  {
    reportDate = aReportDate;
    reportType = aReportType;
    Reporter = aReporter;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReportDate(Date aReportDate)
  {
    boolean wasSet = false;
    reportDate = aReportDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setReportType(ReportType aReportType)
  {
    boolean wasSet = false;
    reportType = aReportType;
    wasSet = true;
    return wasSet;
  }

  public boolean setReporter(String aReporter)
  {
    boolean wasSet = false;
    Reporter = aReporter;
    wasSet = true;
    return wasSet;
  }

  public Date getReportDate()
  {
    return reportDate;
  }

  public ReportType getReportType()
  {
    return reportType;
  }

  public String getReporter()
  {
    return Reporter;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "Reporter" + ":" + getReporter()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "reportDate" + "=" + (getReportDate() != null ? !getReportDate().equals(this)  ? getReportDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reportType" + "=" + (getReportType() != null ? !getReportType().equals(this)  ? getReportType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}