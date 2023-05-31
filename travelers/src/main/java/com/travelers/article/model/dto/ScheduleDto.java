package com.travelers.article.model.dto;

import com.travelers.article.model.dto.ArticleDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScheduleDto extends ArticleDto {
	
	private String startDate;
	private String endDate;
	private String path;

}
