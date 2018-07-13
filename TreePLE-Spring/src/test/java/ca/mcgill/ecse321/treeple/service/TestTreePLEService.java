package ca.mcgill.ecse321.treeple.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse321.treeple.dto.SustainabilityAttributesDto;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Report;
import ca.mcgill.ecse321.treeple.model.Report.ReportType;
import ca.mcgill.ecse321.treeple.model.Species;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.Landtype;
import ca.mcgill.ecse321.treeple.model.TreePleManager;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

public class TestTreePLEService {
	private TreePleManager tm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("output" + File.separator + "data.xml");
	}

	@Before
	public void setUp() throws Exception {
		tm = new TreePleManager();
	}

	@After
	public void tearDown() throws Exception {
		tm.delete();
	}
	
	/**
	 * Test findAllTree with existing municipality and species
	 * @author Yan
	 */
	@Test
	public void testFindAllTrees() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		
		TreePLEService tps = new TreePLEService(tm);
		tm.addTree(treeA);
		tm.addTree(treeB);
		
		List<Tree> listA = null;
		try{
			listA = tps.findAllTrees("locationA", "treeA");
		}
		catch(InvalidInputException e){
			fail();
		}
		
		// check number of trees
		assertEquals(1, listA.size());
		
		// check tree content
		assertEquals(1.0, listA.get(0).getX(), 0.1);
		assertEquals(1.0, listA.get(0).getY(), 0.1);
		assertEquals(1.1, listA.get(0).getDiameter(), 0.1);
		assertEquals(1.1, listA.get(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, listA.get(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), listA.get(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), listA.get(0).getMunicipality().getName());	
	}
	
	/**
	 * Test findAllTree with existing municipality
	 * @author Ivraj
	 */
	@Test
	public void testFindAllTreesWithNullSpecies() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		
		TreePLEService tps = new TreePLEService(tm);
		tm.addTree(treeA);
		tm.addTree(treeB);
		
		List<Tree> listA = null;
		try{
			listA = tps.findAllTrees("locationA", null);
		}
		catch(InvalidInputException e){
			fail();
		}
		
		// check number of trees
		assertEquals(1, listA.size());
		
		// check tree content
		assertEquals(1.0, listA.get(0).getX(), 0.1);
		assertEquals(1.0, listA.get(0).getY(), 0.1);
		assertEquals(1.1, listA.get(0).getDiameter(), 0.1);
		assertEquals(1.1, listA.get(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, listA.get(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), listA.get(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), listA.get(0).getMunicipality().getName());	
	}
	
	/**
	 * Test findAllTree with existing species
	 * @author Yan
	 */
	@Test
	public void testFindAllTreesWithNullMunicipality() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		
		TreePLEService tps = new TreePLEService(tm);
		tm.addTree(treeA);
		tm.addTree(treeB);
		
		List<Tree> listA = null;
		try{
			listA = tps.findAllTrees(null, "treeA");
		}
		catch(InvalidInputException e){
			fail();
		}
		
		// check number of trees
		assertEquals(1, listA.size());
		
		// check tree content
		assertEquals(1.0, listA.get(0).getX(), 0.1);
		assertEquals(1.0, listA.get(0).getY(), 0.1);
		assertEquals(1.1, listA.get(0).getDiameter(), 0.1);
		assertEquals(1.1, listA.get(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, listA.get(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), listA.get(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), listA.get(0).getMunicipality().getName());	
	}
	/**
	 * Test findAllTree with "Marked for cutdown" or Diseased status.
	 * @author Chau
	 */
	@Test
	public void testFindAllTreesDiseased() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		Tree treeC = new Tree(3.0, 3.0, 3.3, 3.3, Landtype.Park, treeBSpecies, treeBMunicipality);
		Tree treeD= new Tree(4.0, 4.0, 4.4, 4.4, Landtype.Residential, treeASpecies, treeAMunicipality);
		
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDateA = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDateA, ReportType.Diseased, "John");
		treeA.addReport(reportA);

		c.set(2017, Calendar.MARCH, 25, 9, 0, 0);
		Date plantDateB = new Date(c.getTimeInMillis());
		Report reportB = new Report(plantDateB, ReportType.Marked, "Jill");
		treeB.addReport(reportB);

		c.set(2017, Calendar.MARCH, 26, 9, 0, 0);
		Date plantDateC = new Date(c.getTimeInMillis());
		Report reportC = new Report(plantDateC, ReportType.Planted, "Joel");
		treeC.addReport(reportC);

		TreePLEService tps = new TreePLEService(tm);
		tm.addTree(treeA);
		tm.addTree(treeB);
		tm.addTree(treeC);
		tm.addTree(treeD);
		
		List<Tree> listA = null;
		try{
			listA = tps.findAllTrees(null, null,true);
		}
		catch(InvalidInputException e){
			fail();
		}
		
		// check number of trees
		assertEquals(2, listA.size());
		
		// check tree content
		assertEquals(1.0, listA.get(0).getX(), 0.1);
		assertEquals(1.0, listA.get(0).getY(), 0.1);
		assertEquals(1.1, listA.get(0).getDiameter(), 0.1);
		assertEquals(1.1, listA.get(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, listA.get(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), listA.get(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), listA.get(0).getMunicipality().getName());
		
		
		assertEquals(2.0, listA.get(1).getX(), 0.1);
		assertEquals(2.0, listA.get(1).getY(), 0.1);
		assertEquals(2.2, listA.get(1).getDiameter(), 0.1);
		assertEquals(2.2, listA.get(1).getHeight(), 0.1);
		assertEquals(Landtype.Municipal, listA.get(1).getLandType());
		assertEquals(treeBSpecies.getNameEnglish(), listA.get(1).getSpecies().getNameEnglish());
		assertEquals(treeBMunicipality.getName(), listA.get(1).getMunicipality().getName());
		
		//
		try{
			listA = tps.findAllTrees(null, null,false);
		}
		catch(InvalidInputException e){
			fail();
		}
		
		// check number of trees
		assertEquals(4, listA.size());
		
			
	}
	
	/**
	 * Test findAllMunicipality method
	 * @author Chau
	 */
	@Test
	public void testfindAllMunicipality() {;
		Municipality MunicipalityA = new Municipality("locationA");
		Municipality MunicipalityB = new Municipality("locationB");
		Municipality MunicipalityC = new Municipality("locationB");

		TreePLEService tps = new TreePLEService(tm);
		tm.addMunicipality(MunicipalityA);
		tm.addMunicipality(MunicipalityB);
		tm.addMunicipality(MunicipalityC);

		List<Municipality> list = tps.findAllMunicipalities();

		// check number of municipality
		assertEquals(2, list.size());
		
		//check municipality's name
		assertEquals(MunicipalityA.getName(),tm.getMunicipality(0).getName());
		assertEquals(MunicipalityB.getName(),tm.getMunicipality(1).getName());
	}
	
	/**
	 * Test findAllSpecies method
	 * @author Chau
	 */
	@Test
	public void testfindAllSpecies() {

		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Species treeCSpecies = new Species(null, null, "treeA", null, null);
		Species treeDSpecies = new Species(null, null, "treeB", null, null);


		TreePLEService tps = new TreePLEService(tm);
		tm.addSpecies(treeASpecies);
		tm.addSpecies(treeBSpecies);
		tm.addSpecies(treeCSpecies);
		tm.addSpecies(treeDSpecies);

		List<Species> list = tps.findAllSpecies();

		// check number of municipality
		assertEquals(2, list.size());
		
		//check municipality's name
		assertEquals(treeASpecies.getNameEnglish(),tm.getSpecies(0).getNameEnglish());
		assertEquals(treeBSpecies.getNameEnglish(),tm.getSpecies(1).getNameEnglish());
		assertEquals(null,tm.getSpecies(0).getNameLatin());
		assertEquals(null,tm.getSpecies(1).getNameLatin());
		assertEquals(null,tm.getSpecies(0).getNameFrench());
		assertEquals(null,tm.getSpecies(1).getNameFrench());
		assertEquals(null,tm.getSpecies(0).getAcronym());
		assertEquals(null,tm.getSpecies(1).getAcronym());
		
		
	}
	/**
	 * Test loadFromFile with a file with existing tree data
	 * @author Yan
	 */
	@Test
	public void testLoadFromFile(){
		assertEquals(0, tm.getTree().size());
		
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "textDataFiles");
		File serverFile = new File(dir.getAbsolutePath() + File.separator + "test1_valid.csv");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.loadDataFromFile(serverFile.toURI());
		} catch (IOException | InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		assertEquals(1, tm.getTree().size());
		
		Tree aTree = tm.getTree(0);
//		assertEquals("Côte-des-Neiges - Notre-Dame-de-Grâce", aTree.getMunicipality().getName());
		assertEquals("Fraxinus americana", aTree.getSpecies().getNameLatin());
//		assertEquals("Frêne d'Amérique", aTree.getSpecies().getNameFrench());
		assertEquals("White Ash", aTree.getSpecies().getNameEnglish());
		assertEquals(45.0, aTree.getDiameter(), 0.001);
		assertEquals(4.0, aTree.getHeight(), 0.001);
		assertEquals(294495.978, aTree.getX(), 0.001);
		assertEquals(5036338.919, aTree.getY(), 0.001);
		assertEquals("Residential", aTree.getLandType().toString());
	}
	
	/**
	 * Test loadFromFile for a file that does not exist
	 * @author Ivraj
	 * @throws InvalidInputException 
	 * @throws IOException 
	 */
	@Test
	public void testNonExistantTestFile() throws InvalidInputException, IOException{
		assertEquals(0, tm.getTree().size());
		
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "textDataFiles");
		File serverFile = new File(dir.getAbsolutePath() + File.separator + "test100.csv");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.loadDataFromFile(serverFile.toURI());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		assertEquals("File does not exist.", error);
	}
	
	/**
	 * Test loadFromFile for a file with the wrong header format
	 * @author Ivraj
	 * @throws InvalidInputException 
	 * @throws IOException 
	 */
	@Test
	public void testLoadFromFileWithBadHeaders() throws InvalidInputException, IOException{
		assertEquals(0, tm.getTree().size());
		
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "textDataFiles");
		File serverFile = new File(dir.getAbsolutePath() + File.separator + "test2_invalid.csv");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.loadDataFromFile(serverFile.toURI());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		assertEquals("File has bad header format.", error);
	}
	
	/**
	 * Test loadFromFile for a file with the wrong header format
	 * @author Ivraj
	 * @throws InvalidInputException 
	 * @throws IOException 
	 */
	@Test
	public void testLoadFromFileInvalidParamter() throws InvalidInputException, IOException{
		assertEquals(0, tm.getTree().size());
		
		String rootPath = System.getProperty("user.dir");
		File dir = new File(rootPath + File.separator + "textDataFiles");
		File serverFile = new File(dir.getAbsolutePath() + File.separator + "test3_invalid.csv");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.loadDataFromFile(serverFile.toURI());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		assertEquals("LandType data error, it need to be one of Residential, Institutional, Park, Municipal.", error);
	}
	
	/**
	 * Test createTree by creating one tree
	 * @author Ivraj
	 */
	@Test
	public void testCreateTree() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		assertEquals(1, tm.numberOfTree());
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(1.1, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(1.1, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, tm.getTree(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), tm.getTree(0).getMunicipality().getName());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
	}

	/**
	 * Test createTree with invalid coordinates
	 * @author Ivraj-Chau
	 */
	@Test
	public void testCreateTreeInvalidCoordinates() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(-200, -400, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on both coordinates below threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		try {
			tps.createTree(200, 400, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on both coordinates above threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		try {
			tps.createTree(200, 0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on coordinate X above threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		try {
			tps.createTree(-200,0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on coordinate X below threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		try {
			tps.createTree(0, 400, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on coordinate Y above threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		try {
			tps.createTree(0,-400, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
			fail("No error reported on coordinate Y below threshold.");
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Invalid coordinates.", error);

		// check no change in memory
		assertEquals(0, tm.numberOfTree());
	}
	
	/**
	 * Test createTree with invalid height
	 * @author Ivraj
	 */
	@Test
	public void testCreateTreeInvalidHeight() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(0.1, 0.1, -1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Cannot have negative height.", error);

		// check no change in memory
		assertEquals(0, tm.numberOfTree());
	}
	
	/**
	 * Test createTree with invalid width
	 * @author Ivraj
	 */
	@Test
	public void testCreateTreeInvalidWidth() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(0.1, 0.1, 1.1, -1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Cannot have negative diameter.", error);

		// check no change in memory
		assertEquals(0, tm.numberOfTree());
	}
	/**
	 * Test createTree with all valid Landtype
	 * Chau
	 */
	@Test
	public void testCreateTreeValidLandType() {
		Species treeSpecies = new Species(null,null,"tree",null,null);
		Municipality treeMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		Report reportB = new Report(plantDate, ReportType.Planted, "Joel");
		Report reportC = new Report(plantDate, ReportType.Planted, "Jack");
		Report reportD = new Report(plantDate, ReportType.Planted, "Jill");
		
		TreePLEService tps = new TreePLEService(tm);
		String error="";
		try {
			error = "Institutional";
			tps.createTree(0.1, 0.1, 1.1, 1.1, Landtype.Institutional, treeSpecies, treeMunicipality, reportA);
			error = "Municipal";
			tps.createTree(0.1, 0.1, 1.1, 1.1, Landtype.Municipal, treeSpecies, treeMunicipality, reportB);
			error = "Park";
			tps.createTree(0.1, 0.1, 1.1, 1.1, Landtype.Park, treeSpecies, treeMunicipality, reportC);
			error = "Residential";
			tps.createTree(0.1, 0.1, 1.1, 1.1, Landtype.Residential, treeSpecies, treeMunicipality, reportD);
		} catch (InvalidInputException e) {
			fail("Error creating " +error);
		}
		
	}
	/**
	 * Test createTree with invalid LandType
	 * @author Ivraj
	 */
	@Test
	public void testCreateTreeInvalidLandType() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(0.1, 0.1, 1.1, 1.1, null, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Invalid type of land use.", error);

		// check no change in memory
		assertEquals(0, tm.numberOfTree());
	}
	
	
	/**
	 * Test addReport with multiple reports attached to a tree
	 * @author Ivraj
	 */
	@Test
	public void testAddReport() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		c.add(Calendar.DATE, 1);
		plantDate = new Date(c.getTimeInMillis());
		Report reportB = new Report(plantDate, ReportType.Marked, "Mary");
		try {
			tps.addReport(tm.getTree(0).getId(), reportB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals(2, tm.getTree(0).getReports().size());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
		assertEquals(reportB.getReporter(), tm.getTree(0).getReport(1).getReporter());
		assertEquals(reportB.getReportDate(), tm.getTree(0).getReport(1).getReportDate());
		assertEquals(reportB.getReportType(), tm.getTree(0).getReport(1).getReportType());
	}

	/**
	 * Test addReport by adding a null report to a tree
	 * @author Ivraj
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testAddNullReport() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Report reportA = null;
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		tm.getTree(0).getReport(0);
	}

	/**
	 * Test addReport for invalid ID (non-existent tree)
	 * @author Ivraj
	 */
	@Test
	public void testAddReportInvalidID() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		String error = null;
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		assertEquals(1, tm.getTree().size());
		
		c.add(Calendar.DATE, 1);
		plantDate = new Date(c.getTimeInMillis());
		Report reportB = new Report(plantDate, ReportType.Marked, "Mary");
		tps.addReport(6, reportB);
		
		//check report was not added
		assertEquals(1, tm.getTree(0).getReports().size());
		
		//check original report is intact
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());		
	}

	/**
	 * Test updateTree for new height
	 * @autho Ivraj
	 */
	@Test
	public void testUpdateTree() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("height", "10");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		
		assertEquals(1, tm.numberOfTree());
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(1.1, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(10, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, tm.getTree(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), tm.getTree(0).getMunicipality().getName());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
	}

	/**
	 * Test updateTree for new height and diameter
	 * @autho Ivraj
	 */
	@Test
	public void testUpdateTreeMultiple() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("height", "10");
		hmap.put("diameter", "20");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		
		assertEquals(1, tm.numberOfTree());
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(20, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(10, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, tm.getTree(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), tm.getTree(0).getMunicipality().getName());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
	}
	
	/**
	 * Test updateTree for non-existant tree.
	 * @autho Ivraj
	 */
	@Test
	public void testUpdateTreeDNE() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("height", "10");
		hmap.put("diameter", "20");
		String error = null;
		
		try {
			tps.updateATree(tm.getTree(0).getId()+1, hmap);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
		}
		
		//check existing tree is unchanged
		assertEquals(1, tm.numberOfTree());
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(1.1, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(1.1, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, tm.getTree(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), tm.getTree(0).getMunicipality().getName());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
		
		//check error
		assertEquals("There is no tree with that ID.", error);
	}
	
	/**
	 * Test updateTree for coordinates
	 * @author Chau
	 */
	@Test
	public void testUpdateTreeCoordinates() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("x", "10.0");
		hmap.put("y", "12.0");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		
		assertEquals(1, tm.numberOfTree());
		assertEquals(10.0, tm.getTree(0).getX(), 0.1);
		assertEquals(12.0, tm.getTree(0).getY(), 0.1);
	}
	
	/**
	 * Test updateTree for landType
	 * @author Chau
	 */
	@Test
	public void testUpdateTreeLandType() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		hmap.put("landType", "Residential");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		assertEquals(1, tm.numberOfTree());
		assertEquals(Landtype.Residential,tm.getTree(0).getLandType());
		
		hmap.put("landType", "Institutional");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		assertEquals(1, tm.numberOfTree());
		assertEquals(Landtype.Institutional,tm.getTree(0).getLandType());
		
		hmap.put("landType", "Park");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		assertEquals(1, tm.numberOfTree());
		assertEquals(Landtype.Park,tm.getTree(0).getLandType());
		
		hmap.put("landType", "Municipal");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		assertEquals(1, tm.numberOfTree());
		assertEquals(Landtype.Municipal,tm.getTree(0).getLandType());
	}
	
	/**
	 * Test updateTree for Municipal
	 * @author Chau
	 */
	@Test
	public void testUpdateTreeMunicipal() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		hmap.put("municipality", "Ottawa");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		assertEquals(1, tm.numberOfTree());
		assertEquals("Ottawa",tm.getTree(0).getMunicipality().getName());
	}

	
	/**
	 * Test updateTree for Species
	 * @author Chau
	 */
	@Test
	public void testUpdateTreeSpecies() throws Exception {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDate = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDate, ReportType.Planted, "John");
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality, reportA);
		} catch (InvalidInputException e) {
			fail();
		}
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		hmap.put("species_ac", "FA");
		hmap.put("species_la", "Fraxinus americana");
		hmap.put("species_en", "White Ash");
		hmap.put("species_fr", "Frêne d'Amérique");
		tps.updateATree(tm.getTree(0).getId(), hmap);
		
		assertEquals(1, tm.numberOfTree());
		assertEquals("FA",tm.getTree(0).getSpecies().getAcronym());
		assertEquals("Fraxinus americana",tm.getTree(0).getSpecies().getNameLatin());
		assertEquals("White Ash",tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals("Frêne d'Amérique",tm.getTree(0).getSpecies().getNameFrench());
	}
	
	/**
	 * Test caclulateSustainabilityAttributes
	 * @author Yan
	 */
	@Test
	public void testCaclulateSustainabilityAttributes() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		
		TreePLEService tps = new TreePLEService(tm);
		List<Tree> trees = new ArrayList<>();
		trees.add(treeA);
		trees.add(treeB);
		
		SustainabilityAttributesDto sustainabilityAttributesDto = tps.caclulateSustainabilityAttributes(trees);
		
		assertEquals(1.0, sustainabilityAttributesDto.getBiodiversityIndex(), 0.01);
		assertEquals(2.99, sustainabilityAttributesDto.getCarbonSequestration(), 0.01);
	}
	
	/**
	 * Test findSpeciesFromTreeList
	 * @author Yan-Chau
	 */
	@Test
	public void testfindSpeciesFromTreeList() {
		assertEquals(0, tm.getTree().size());
		
		Species treeASpecies = new Species(null, null, "a", null, null);
		Species treeBSpecies = new Species(null, null, "b", null, null);
		Species treeCSpecies = new Species(null, null, "c", null, null);
		Species treeDSpecies = new Species(null, null, "d", null, null);
		Species treeESpecies = new Species(null, null, null, null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Municipality treeBMunicipality = new Municipality("locationB");
		Tree treeA = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, treeASpecies, treeAMunicipality);
		Tree treeB = new Tree(2.0, 2.0, 2.2, 2.2, Landtype.Municipal, treeBSpecies, treeBMunicipality);
		
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDateC = new Date(c.getTimeInMillis());
		Report reportC = new Report(plantDateC, ReportType.CuttedDown, "John");
		Tree treeC = new Tree(3.0, 3.0, 3.3, 3.3, Landtype.Park, treeCSpecies, treeBMunicipality);
		treeC.addReport(reportC);

		c.set(2017, Calendar.MARCH, 17, 9, 0, 0);
		Date plantDateD = new Date(c.getTimeInMillis());
		Report reportD = new Report(plantDateD, ReportType.Planted, "John");
		Tree treeD = new Tree(3.0, 3.0, 3.3, 3.3, Landtype.Park, treeDSpecies, treeBMunicipality);
		treeD.addReport(reportD);

		Tree treeE = new Tree(3.0, 3.0, 3.3, 3.3, Landtype.Municipal, treeESpecies, treeBMunicipality);
		
		TreePLEService tps = new TreePLEService(tm);
		List<Tree> trees = new ArrayList<>();
		trees.add(treeA);
		trees.add(treeB);
		trees.add(treeC);
		trees.add(treeD);
		trees.add(treeE);
		
		List<String> speciesList= tps.findSpeciesFromTreeList(trees);
		
		assertEquals(3, speciesList.size());
		assertEquals("a", speciesList.get(0));
		assertEquals("b", speciesList.get(1));
		assertEquals("d", speciesList.get(2));

	}
	/**
	 * Test predictRemoveTrees method
	 * @author Chau
	 */
	@Test
	public void testpredictRemoveTrees() {
		assertEquals(0, tm.getTree().size());
		
		PersistenceXStream.initializeModelManager(PersistenceXStream.getFilename());

		
		Species treeASpecies = new Species(null, null, "treeA", null, null);
		Municipality treeAMunicipality = new Municipality("locationA");
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date plantDateA = new Date(c.getTimeInMillis());
		Report reportA = new Report(plantDateA, ReportType.Planted, "John");
		
		Species treeBSpecies = new Species(null, null, "treeB", null, null);
		Municipality treeBMunicipality = new Municipality("locationB");
		c.set(2017, Calendar.MARCH, 16, 10, 0, 0);
		Date plantDateB = new Date(c.getTimeInMillis());
		Report reportB = new Report(plantDateB, ReportType.Planted, "Jena");
		
		Species treeCSpecies = new Species(null, null, "treeC", null, null);
		Municipality treeCMunicipality = new Municipality("locationB");
		c.set(2017, Calendar.MARCH, 18, 10, 0, 0);
		Date plantDateC = new Date(c.getTimeInMillis());
		Report reportC = new Report(plantDateC, ReportType.Planted, "Jill");
		
		Species treeDSpecies = new Species(null, null, "treeD", null, null);
		Municipality treeDMunicipality = new Municipality("locationA");
		c.set(2017, Calendar.MARCH, 28, 10, 0, 0);
		Date plantDateD = new Date(c.getTimeInMillis());
		Report reportD = new Report(plantDateD, ReportType.Planted, "Jack");
		
		
		TreePLEService tps = new TreePLEService(tm);
		try {
			tps.createTree(1.0, 1.0, 1.1, 1.1, Landtype.Park, treeASpecies, treeAMunicipality, reportA);
			tps.createTree(2.0, 2.0, 2.2, 2.2, Landtype.Institutional, treeBSpecies, treeBMunicipality, reportB);
			tps.createTree(3.0, 3.0, 3.3, 3.3, Landtype.Residential, treeCSpecies, treeCMunicipality, reportC);
			tps.createTree(4.0, 4.0, 4.4, 4.4, Landtype.Institutional, treeDSpecies, treeDMunicipality, reportD);
		} catch (InvalidInputException e) {
			fail();
		}

		List<Tree> list = tps.predictRemoveTrees(treeAMunicipality,"Institutional");
		
		//check list of predicted remove tree
		assertEquals(1,list.size());
		
		//check tree's value.
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(1.1, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(1.1, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Park, tm.getTree(0).getLandType());
		assertEquals(treeASpecies.getNameEnglish(), tm.getTree(0).getSpecies().getNameEnglish());
		assertEquals(treeAMunicipality.getName(), tm.getTree(0).getMunicipality().getName());
		assertEquals(reportA.getReporter(), tm.getTree(0).getReport(0).getReporter());
		assertEquals(reportA.getReportDate(), tm.getTree(0).getReport(0).getReportDate());
		assertEquals(reportA.getReportType(), tm.getTree(0).getReport(0).getReportType());
		
	}
	
}
