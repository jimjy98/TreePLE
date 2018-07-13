package ca.mcgill.ecse321.treeple.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ca.mcgill.ecse321.treeple.dto.ForcastingBiodiversityDto;
import ca.mcgill.ecse321.treeple.dto.MunicipalityDto;
import ca.mcgill.ecse321.treeple.dto.SpeciesDto;
import ca.mcgill.ecse321.treeple.dto.TreeDto;
import ca.mcgill.ecse321.treeple.dto.TreeGroupDto;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Report;
import ca.mcgill.ecse321.treeple.model.Report.ReportType;
import ca.mcgill.ecse321.treeple.model.Species;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.Landtype;
import ca.mcgill.ecse321.treeple.service.InvalidInputException;
import ca.mcgill.ecse321.treeple.service.TreePLEService;

@RestController
public class TreePLERestController {

	@Autowired
	private TreePLEService service;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping("/")
	public String index() {
		return "Tree PLE application root.";
	}
	
	/**
	 * Given a “municipality” and “landType”, received a list of trees in “municipality”, but exclude the given “landType”.
	 * @param municipality
	 * @param landType
	 * @return
	 * @throws InvalidInputException
	 */
	@GetMapping(value = { "/forecasting/remove", "/forecasting/remove/" })
	public TreeGroupDto forcastingRemove(@RequestParam(value = "municipality", required = true) String municipality,
			@RequestParam(value = "landType", required = true) String landType) throws InvalidInputException {
		
		List<Tree> treeList = service.predictRemoveTrees(new Municipality(municipality), landType);
		TreeGroupDto treeGroupDto = new TreeGroupDto();
		List<TreeDto> treeListDto = new ArrayList<TreeDto>();
		
		for (Tree tree : treeList) {
			treeListDto.add(convertToDto(tree));
		}
		treeGroupDto.setTreeList(treeListDto);
		treeGroupDto.setAttributes(service.caclulateSustainabilityAttributes(treeList));
		return treeGroupDto;
	}
	
	/**
	 * Given a “municipality”, received a list of species in current “municipality”.
	 * @param municipality
	 * @return
	 * @throws InvalidInputException
	 */
	@GetMapping(value = { "/forecasting/biodiversity", "/forecasting/biodiversity/" })
	public ForcastingBiodiversityDto forcastingBiodiversity(@RequestParam(value = "municipality", required = true) String municipality) throws InvalidInputException {
		
		List<Tree> treeList = service.findAllTrees(municipality, null);
		List<String> species = service.findSpeciesFromTreeList(treeList);
		
		return new ForcastingBiodiversityDto(species, service.caclulateSustainabilityAttributes(treeList));
	}

	/**
	 * Get  a tree list with filter condition and auto calculated sustainability attributes
	 * @param municipality
	 * @param species
	 * @param markedOrDiseased
	 * @return
	 * @throws InvalidInputException
	 */
	@GetMapping(value = { "/trees", "/trees/" })
	public TreeGroupDto findAllTrees(@RequestParam(value = "municipality", required = false) String municipality,
			@RequestParam(value = "species", required = false) String species, @RequestParam(value = "markedOrDiseased", required = false, defaultValue="false") boolean markedOrDiseased) throws InvalidInputException {
		
		List<TreeDto> treeListDto = new ArrayList<TreeDto>();
		TreeGroupDto treeGroupDto = new TreeGroupDto();
		List<Tree> treeList = service.findAllTrees(municipality, species, markedOrDiseased);
		
		for (Tree tree : treeList) {
			treeListDto.add(convertToDto(tree));
		}
		
		treeGroupDto.setTreeList(treeListDto);
		treeGroupDto.setAttributes(service.caclulateSustainabilityAttributes(treeList));
		return treeGroupDto;
	}
	
	/**
	 * Get a distinct list of species in database
	 * @return
	 */
	@GetMapping(value = { "/species", "/species/" })
	public List<SpeciesDto> findAllSpecies(){
		List<Species> species = service.findAllSpecies();
		List<SpeciesDto> speciesDtos = new ArrayList<>();
		for(Species s: species){
			speciesDtos.add(convertToDto(s));
		}
		return speciesDtos;
	}
	
	/**
	 * Get a distinct list of municipalities in database
	 * @return
	 */
	@GetMapping(value = { "/municipalities", "/municipalities/" })
	public List<MunicipalityDto> findAllMunicipalities(){
		List<Municipality> municipalities = service.findAllMunicipalities();
		List<MunicipalityDto> municipalityDtos = new ArrayList<>();
		for(Municipality m: municipalities){
			municipalityDtos.add(convertToDto(m));
		}
		return municipalityDtos;
	}

	/**
	 * User reports for planting a new tree
	 * @param municipalityName
	 * @param x
	 * @param y
	 * @param height
	 * @param diameter
	 * @param speciesName
	 * @param landType
	 * @param date
	 * @param reporter
	 * @return
	 * @throws InvalidInputException
	 * @throws IllegalArgumentException
	 */
	@PostMapping(value = { "/tree/report", "/tree/report" })
	public TreeDto createATree(@RequestParam(value = "municipality", required = true) String municipalityName,
			@RequestParam(value = "x", required = true) double x, @RequestParam(value = "y", required = true) double y,
			@RequestParam(value = "height", required = false, defaultValue = "0.0") double height,
			@RequestParam(value = "diameter", required = false, defaultValue = "0.0") double diameter,
			@RequestParam(value = "species", required = false, defaultValue = "None") String speciesName,
			@RequestParam(value = "landType", required = true) String landType,
			@RequestParam(value = "date", required = true) Date date,
			@RequestParam(value = "reporter", required = true) String reporter) throws InvalidInputException, IllegalArgumentException {
		Municipality municipality = new Municipality(municipalityName);
		Species species = new Species(null, null, speciesName, null, null);
		Report report = new Report(date, ReportType.Planted, reporter);
		Landtype lt;
		try{
			lt = Landtype.valueOf(landType);
		}catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid landType enum, it must be one of the Residential, Institutional, Park, Municipal(case sensitive)");
		}
		Tree tree = service.createTree(x, y, height, diameter, lt, species, municipality,
				report);
		return convertToDto(tree);
	}
	
	/**
	 * Update tree data within the database
	 * @param id
	 * @param requestData
	 * @return
	 * @throws InvalidInputException
	 */
	@PostMapping(value = { "/tree/{id}", "/tree/{id}/" })
	public TreeDto updateATree(@PathVariable("id") int id, @RequestBody HashMap<String, String> requestData) throws InvalidInputException {

		Tree tree = service.updateATree(id, requestData);
		return convertToDto(tree);
	}

	/**
	 * Report a tree as cutdown, diseased or to be cut down
	 * @param id
	 * @param status
	 * @param date
	 * @param reporter
	 * @return
	 * @throws IllegalArgumentException
	 */
	@PostMapping(value = { "/tree/report/{id}", "/tree/report/{id}/" })
	public TreeDto addAReport(@PathVariable("id") int id,
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "date", required = true) Date date,
			@RequestParam(value = "reporter", required = true) String reporter) throws IllegalArgumentException{
		
		ReportType rt;
		try{
			rt = ReportType.valueOf(status);
		}catch (Exception e) {
			throw new IllegalArgumentException("Invalid status, it must be one of Planted, Diseased, Marked, CuttedDown(case sensitive)");
		}
		Report newReport = new Report(date, rt, reporter);

		Tree tree = service.addReport(id, newReport);
		return convertToDto(tree);
	}

	/**
	 * Upload single file using Spring Controller, upload a text that will initialize the tree database
	 * @param name
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", headers=("content-type=multipart/form-data"), method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam(value = "name", required = false) String name,
			@RequestParam("file") MultipartFile file) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("user.dir");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();
				if (name == null || name.isEmpty()) {
					name = "temp.csv";
				}

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				service.loadDataFromFile(serverFile.toURI());

				return "You successfully uploaded file=" + name;
			} catch (Exception e) {
				e.printStackTrace();
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	/**
	 * DTO conversion method
	 * @param tree
	 * @return
	 */
	private TreeDto convertToDto(Tree tree) {
		TreeDto treeDto = new TreeDto();
		treeDto.setDiameter(tree.getDiameter());
		treeDto.setMunicipality(convertToDto(tree.getMunicipality()));
		treeDto.setHeight(tree.getHeight());
		treeDto.setId(tree.getId());
		treeDto.setX(tree.getX());
		treeDto.setY(tree.getY());
		treeDto.setHeight(tree.getHeight());
		treeDto.setSpecies(convertToDto(tree.getSpecies()));
		if(tree.getLandType() != null){
			treeDto.setLandType(tree.getLandType().toString());
		}
		if (tree.numberOfReports() >= 1) {
			treeDto.setStatus(tree.getReport(tree.numberOfReports() - 1).getReportType().name());
			treeDto.setReportDate(tree.getReport(tree.numberOfReports() - 1).getReportDate());
			treeDto.setReporter(tree.getReport(tree.numberOfReports() - 1).getReporter());
		} else {
			treeDto.setStatus("NoReport");
			treeDto.setReportDate(null);
			treeDto.setReporter(null);

		}
		return treeDto;
	}
	
	/**
	 * DTO conversion method
	 * @param municipality
	 * @return
	 */
	private MunicipalityDto convertToDto(Municipality municipality){
		MunicipalityDto municipalityDto = new MunicipalityDto();
		municipalityDto.setName(municipality.getName());
		return municipalityDto;
	}
	
	/**
	 * DTO conversion method
	 * @param species
	 * @return
	 */
	private SpeciesDto convertToDto(Species species){
		SpeciesDto speciesDto = new SpeciesDto();
		speciesDto.setNameEnglish(species.getNameEnglish());
		return speciesDto;
	}

}