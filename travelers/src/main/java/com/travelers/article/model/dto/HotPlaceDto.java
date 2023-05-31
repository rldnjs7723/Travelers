package com.travelers.article.model.dto;

import java.util.List;

import com.travelers.common.model.dto.FileInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HotPlaceDto extends ArticleDto {

	private int contentId;
	private int contentTypeId;
	private int rate;
	private double totalRate;
	private String visitDate;

}
