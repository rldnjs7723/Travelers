package com.travelers.attraction.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PlanDto {

	int day;
	List<AttractionDto> path;
}
