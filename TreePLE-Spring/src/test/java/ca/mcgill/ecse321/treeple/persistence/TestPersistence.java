package ca.mcgill.ecse321.treeple.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Report;
import ca.mcgill.ecse321.treeple.model.Report.ReportType;
import ca.mcgill.ecse321.treeple.model.Species;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.Landtype;
import ca.mcgill.ecse321.treeple.model.TreePleManager;

/**
 * Persistence layer unit test
 * @author Yan
 *
 */
public class TestPersistence {

	private TreePleManager tm;

	@Before
	public void setUp() throws Exception {
		tm = new TreePleManager();

		// create report
		Calendar c = Calendar.getInstance();
		c.set(2015, Calendar.SEPTEMBER, 15, 8, 30, 0);
		Date reportDate = new Date(c.getTimeInMillis());
		String reporter = "Martin";
		Report aReport = new Report(reportDate, ReportType.Planted, reporter);

		// create species
		Species aSpecies = new Species(null, null, "oak", null, null);

		// create municipality
		Municipality aMunicipality = new Municipality("locationA");

		// create tree
		Tree aTree = new Tree(1.0, 1.0, 1.1, 1.1, Landtype.Institutional, aSpecies, aMunicipality);

		//
		tm.addTree(aTree);
		tm.addReport(aReport);
		tm.addSpecies(aSpecies);
		tm.addMunicipality(aMunicipality);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		// initialize model file
		PersistenceXStream.initializeModelManager("output" + File.separator + "data.xml");
		// save model that is loaded during test setup
		if (!PersistenceXStream.saveToXMLwithXStream(tm))
			fail("Could not save file.");

		// clear the model in memory
		tm.delete();
		assertEquals(0, tm.getMunicipality().size());
		assertEquals(0, tm.getReport().size());
		assertEquals(0, tm.getTree().size());
		assertEquals(0, tm.getSpecies().size());

		// load model
		tm = (TreePleManager) PersistenceXStream.loadFromXMLwithXStream();
		if (tm == null)
			fail("Could not load file.");

		// check report
		Calendar c = Calendar.getInstance();
		c.set(2015, Calendar.SEPTEMBER, 15, 8, 30, 0);
		Date reportDate = new Date(c.getTimeInMillis());
		String reporter = "Martin";
		assertEquals(1, tm.getReport().size());
		assertEquals(reportDate.toString(), tm.getReport(0).getReportDate().toString());
		assertEquals(reporter, tm.getReport(0).getReporter());
		assertEquals(ReportType.Planted, tm.getReport(0).getReportType());
		// check species
		assertEquals(1, tm.getSpecies().size());
		assertEquals("oak", tm.getSpecies(0).getNameEnglish());	
		// check municipality
		assertEquals(1, tm.getMunicipality().size());
		assertEquals("locationA", tm.getMunicipality(0).getName());	
		// check tree
		assertEquals(1, tm.getTree().size());
		assertEquals(1.0, tm.getTree(0).getX(), 0.1);
		assertEquals(1.0, tm.getTree(0).getY(), 0.1);
		assertEquals(1.1, tm.getTree(0).getDiameter(), 0.1);
		assertEquals(1.1, tm.getTree(0).getHeight(), 0.1);
		assertEquals(Landtype.Institutional, tm.getTree(0).getLandType());
	}

	@After
	public void tearDown() throws Exception {
		tm.delete();
	}
}
