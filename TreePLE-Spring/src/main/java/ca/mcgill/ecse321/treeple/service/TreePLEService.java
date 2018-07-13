package ca.mcgill.ecse321.treeple.service;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.treeple.dto.SustainabilityAttributesDto;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Report;
import ca.mcgill.ecse321.treeple.model.Report.ReportType;
import ca.mcgill.ecse321.treeple.model.Species;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.Landtype;
import ca.mcgill.ecse321.treeple.model.TreePleManager;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

@Service
public class TreePLEService {
	private TreePleManager tm;
	private TreePleManager tp;

	public TreePLEService(TreePleManager tm) {
		this.tm = tm;
	}
	
	private TreePleManager initializeTreePredictor(){
		return PersistenceXStream.initializeModelManager(PersistenceXStream.getFilename());
	}
	
	/**
	 * This method creates a copy of tree data, and remove a landType within a municipality.
	 * Return the tree list with the sustainability index
	 * @param municipality
	 * @param landType
	 * @return <List>
	 */
	public List<Tree> predictRemoveTrees(Municipality municipality, String landType){
		// load tree data from data file
		tp = initializeTreePredictor();
		List<Tree> originalList = tp.getTree();
		
		return originalList.stream().filter(t -> !t.getLandType().toString().equals(landType) && 
				t.getMunicipality().getName().toString().equals(municipality.getName().toString()))
				.collect(Collectors.toList());
	}

	/**
	 * This method parse the csv file and store in to data persistence
	 * @param csvFilePath
	 * @return
	 * @throws IOException
	 * @throws InvalidInputException
	 */
	public boolean loadDataFromFile(URI csvFilePath) throws IOException, InvalidInputException {
		Reader reader;
		try {
			reader = Files.newBufferedReader(Paths.get(csvFilePath), Charset.forName("UTF-8"));
		} catch (Exception InvalidInputException) {
			throw new InvalidInputException("File does not exist.");
		}
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("Municipal", "LandType", "X", "Y",
				"Name_Latin", "Name_French", "Name_English", "Diameter", "Height").withIgnoreHeaderCase().withTrim());
		try {
			for (CSVRecord csvRecord : csvParser.getRecords()) {
				// skip the first line as header
				if (csvRecord.getRecordNumber() != 1) {
					// Accessing values by the names assigned to each column
					Municipality tempMunicipality = new Municipality(csvRecord.get("Municipal").trim());
					if (!EnumUtils.isValidEnum(Landtype.class, csvRecord.get("LandType").trim())) {
						System.out.println(csvRecord.get("LandType").trim());
						throw new InvalidInputException(
								"LandType data error, it need to be one of Residential, Institutional, Park, Municipal.");
					}
					Species tempSpecies = new Species(null, csvRecord.get("Name_Latin").trim(),
							csvRecord.get("Name_English").trim(), csvRecord.get("Name_French").trim(), null);
					Tree tempTree = new Tree(Double.parseDouble(csvRecord.get("X").trim()),
							Double.parseDouble(csvRecord.get("Y").trim()),
							Double.parseDouble(csvRecord.get("Height").trim()),
							Double.parseDouble(csvRecord.get("Diameter").trim()),
							Landtype.valueOf(csvRecord.get("LandType").trim()), tempSpecies, tempMunicipality);
					tm.addTree(tempTree);
					tm.addSpecies(tempSpecies);
					tm.addMunicipality(tempMunicipality);
				}
			}
		}
		catch (InvalidInputException e) {
			throw e;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			throw new InvalidInputException("File has bad header format.");
		}
		PersistenceXStream.saveToXMLwithXStream(tm);
		return true;
	}
	
	/**
	 * This method adds a report for a tree based on ID
	 * @param id
	 * @param newReport
	 * @return
	 */
	public Tree addReport(int id, Report newReport) {
		Iterator<Tree> i = tm.getTree().iterator();
		int index = 0;
		while (i.hasNext()) {
			Tree o = i.next();
			if (o.getId() == id) {
				o.addReport(newReport);
				tm.addOrMoveTreeAt(o, index);
				PersistenceXStream.saveToXMLwithXStream(tm);
				return o;
			}
			index++;
		}
		return null;
	}
	
	/**
	 * This method creates a new tree and save to data persistence
	 * @param aX
	 * @param aY
	 * @param aHeight
	 * @param aDiameter
	 * @param aLandType
	 * @param aSpecies
	 * @param aMunicipality
	 * @param aReport
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree createTree(double aX, double aY, double aHeight, double aDiameter, Landtype aLandType, Species aSpecies,
			Municipality aMunicipality, Report aReport) throws InvalidInputException {
		if (aY < -180 || aY > 180 || aX < -90 || aX > 90) {
			throw new InvalidInputException("Invalid coordinates.");
		}
		if (aHeight < 0) {
			throw new InvalidInputException("Cannot have negative height.");
		}
		if (aDiameter < 0) {
			throw new InvalidInputException("Cannot have negative diameter.");
		}
		if (aLandType != Landtype.Institutional && aLandType != Landtype.Municipal &&
				aLandType != Landtype.Park && aLandType != Landtype.Residential) {
			throw new InvalidInputException("Invalid type of land use.");
		}
		Tree t = new Tree(aX, aY, aHeight, aDiameter, aLandType, aSpecies, aMunicipality);
		if (aReport != null)
			t.addReport(aReport);
		tm.addTree(t);
		tm.addSpecies(aSpecies);
		tm.addMunicipality(aMunicipality);
		PersistenceXStream.saveToXMLwithXStream(tm);
		return t;
	}
	
	/**
	 * This method find trees based on municipality and species, returns the filtered tree list
	 * @param municipality
	 * @param species
	 * @return List<Tree>
	 * @throws InvalidInputException
	 */
	public List<Tree> findAllTrees(String municipality, String species) throws InvalidInputException {
		if ( municipality != null && !municipality.isEmpty() && species != null && !species.isEmpty()) {
			return tm.getTree().stream().filter(t -> t.getMunicipality().getName().toLowerCase().equals(municipality.toLowerCase())
					&& t.getSpecies().getNameEnglish().toLowerCase().equals(species.toLowerCase())).collect(Collectors.toList());
		}
		if ( municipality != null && !municipality.isEmpty()) {
			return tm.getTree().stream().filter(t -> t.getMunicipality().getName().toLowerCase().equals(municipality.toLowerCase())
					).collect(Collectors.toList());
		}
		if ( species != null && !species.isEmpty()) {
			return tm.getTree().stream().filter(t -> t.getSpecies().getNameEnglish().toLowerCase().equals(species.toLowerCase())).collect(Collectors.toList());
		}
		return tm.getTree();
	}
	
	/**
	 * This method update a tree based on tree ID
	 * @param id
	 * @param requestData
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree updateATree(int id, HashMap<String, String> requestData) throws InvalidInputException {
		Iterator<Tree> i = tm.getTree().iterator();
		int index = 0;
		while (i.hasNext()) {
			Tree o = i.next();
			if (o.getId() == id) {
				if (requestData.containsKey("x") && !requestData.get("x").trim().isEmpty()) {
					o.setX(Double.parseDouble(requestData.get("x").trim()));
				}
				if (requestData.containsKey("y") && !requestData.get("y").trim().isEmpty()) {
					o.setY(Double.parseDouble(requestData.get("y").trim()));
				}
				if (requestData.containsKey("height") && !requestData.get("height").trim().isEmpty()) {
					o.setHeight(Double.parseDouble(requestData.get("height").trim()));
				}
				if (requestData.containsKey("diameter") && !requestData.get("diameter").trim().isEmpty()) {
					o.setDiameter(Double.parseDouble(requestData.get("diameter").trim()));
				}
				if (requestData.containsKey("landType") && !requestData.get("landType").trim().isEmpty()) {
					o.setLandType(Landtype.valueOf(requestData.get("landType").trim()));
				}
				if (requestData.containsKey("municipality") && !requestData.get("municipality").trim().isEmpty()) {
					o.setMunicipality(new Municipality(requestData.get("municipality").trim()));
				}
				Species temp = o.getSpecies();
				if (requestData.containsKey("species_ac") && !requestData.get("species_ac").trim().isEmpty()) {
					temp.setAcronym(requestData.get("species_ac").trim());
				}
				if (requestData.containsKey("species_la") && !requestData.get("species_la").trim().isEmpty()) {
					temp.setNameLatin(requestData.get("species_la").trim());
				}
				if (requestData.containsKey("species_en") && !requestData.get("species_en").trim().isEmpty()) {
					temp.setNameEnglish(requestData.get("species_en").trim());
				}
				if (requestData.containsKey("species_fr") && !requestData.get("species_fr").trim().isEmpty()) {
					temp.setNameFrench(requestData.get("species_fr").trim());
				}
				o.setSpecies(temp);
				tm.addOrMoveTreeAt(o, index);
				PersistenceXStream.saveToXMLwithXStream(tm);
				return o;
			}
			index++;
		}
		throw new InvalidInputException("There is no tree with that ID.");
	}
	
	/**
	 * This method finds the unique species in current data persistence
	 * @return List<Species>
	 */
	public List<Species> findAllSpecies(){
		List<Species> result = new ArrayList<>();
		List<Species> originalSpecies = tm.getSpecies();
		for(Species o: originalSpecies){
			boolean matched = false;
			for(Species n: result){
				if(n.getNameEnglish().equals(o.getNameEnglish())){
					matched = true;
				}
			}
			if(!matched){
				result.add(o);
			}
		}
		return result;
	}
	
	/**
	 * This method finds the unique municipality in current data persistence
	 * @return List<Species>
	 */
	public List<Municipality> findAllMunicipalities(){
		List<Municipality> result = new ArrayList<>();
		List<Municipality> originalMunicipality = tm.getMunicipality();
		for(Municipality o: originalMunicipality){
			boolean matched = false;
			for(Municipality n: result){
				if(n.getName().equals(o.getName())){
					matched = true;
				}
			}
			if(!matched){
				result.add(o);
			}
		}
		return result;
	}
	
	/**
	 * This method is to provide an example for calculate sustainability attributes in business layer.
	 * If more professional calculation algorithm is going to be used. Can overwrite this method. 
	 * @param List <tree>
	 * @return
	 */
	public SustainabilityAttributesDto caclulateSustainabilityAttributes(List<Tree> trees){
		/*
		 * biodiversity index = the number of species in the area (numerator) / the total number of individuals in the area (denominator )
		 *
		 * method of calculating carbon sequestration refers to following paper.
		 * https://www.broward.org/NaturalResources/ClimateChange/Documents/Calculating%20CO2%20Sequestration%20by%20Trees.pdf
		 */
		List<String> speciesList = new ArrayList<>();
		double carbonIndex = 0;
				
		for(Tree t: trees){
			//check if tree is CuttedDown
			if(t.getReports().size() > 0){
				if(t.getReport(t.getReports().size()-1).getReportType()==ReportType.CuttedDown){
					continue;
				}
			}
			
			carbonIndex += 0.25* t.getDiameter() * t.getDiameter() * t.getHeight();
			if(!speciesList.contains(t.getSpecies().getNameEnglish().toLowerCase())){
				speciesList.add(t.getSpecies().getNameEnglish().toLowerCase());
			}
		}
		double bioIndex = speciesList.size() / (1.0*trees.size());
		
		return new SustainabilityAttributesDto(bioIndex, carbonIndex);
	}
	
	/**
	 * Given a tree list, this method returns the unique species within the tree list
	 * @param trees
	 * @return List<String> contains the species
	 */
	public List<String> findSpeciesFromTreeList(List<Tree> trees){
		List<String> speciesList = new ArrayList<>();
				
		for(Tree t: trees){
			//check if tree is CuttedDown
			if(t.getReports().size() > 0){
				if(t.getReport(t.getReports().size()-1).getReportType()==ReportType.CuttedDown){
					continue;
				}
			}
			if(t.getSpecies().getNameEnglish() !=null && !speciesList.contains(t.getSpecies().getNameEnglish().toLowerCase())){
				speciesList.add(t.getSpecies().getNameEnglish().toLowerCase());
			}
		}
		
		return speciesList;
	}

	/**
	 * This method find trees based on municipality, species and status, returns the filtered tree list
	 * @param municipality
	 * @param species
	 * @param markedOrDiseased if this is true, return the marked or diseased tree
	 * @return
	 * @throws InvalidInputException
	 */
	public List<Tree> findAllTrees(String municipality, String species, boolean markedOrDiseased) throws InvalidInputException {
		if(markedOrDiseased){
			List<Tree> filterList = new ArrayList<>();
			List<Tree> originalList = findAllTrees(municipality, species);
			for(Tree t: originalList){
				if(t.hasReports()){
					if(t.getReport(t.getReports().size()-1).getReportType().toString().equals("Marked") ||t.getReport(t.getReports().size()-1).getReportType().toString().equals("Diseased")){
						filterList.add(t);
					}
				}
			}
			return filterList;
		}else{
			return findAllTrees(municipality, species);
		}
	}
}
