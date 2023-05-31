package com.travelers.article.model.dto;

import java.sql.Blob;
import java.util.List;

import com.travelers.common.model.dto.FileInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArticleDto {
	
	private int articleNo;
	private String articleType;
	private String userId;
	private String title;
	private String writeTime;
	private String content;
	private int hit;
	private int like;
	private List<FileInfoDto> fileInfos;

}
