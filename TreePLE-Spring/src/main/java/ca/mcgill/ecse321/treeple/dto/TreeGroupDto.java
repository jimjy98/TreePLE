package ca.mcgill.ecse321.treeple.dto;

import java.util.List;

public class TreeGroupDto {
	private List<TreeDto> trees;
	private SustainabilityAttributesDto attributes;
	public List<TreeDto> getTreeList() {
		return trees;
	}
	public void setTreeList(List<TreeDto> treeList) {
		this.trees = treeList;
	}
	public SustainabilityAttributesDto getAttributes() {
		return attributes;
	}
	public void setAttributes(SustainabilityAttributesDto attributes) {
		this.attributes = attributes;
	}
}
