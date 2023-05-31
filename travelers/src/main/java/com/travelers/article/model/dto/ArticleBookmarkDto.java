package com.travelers.article.model.dto;

import com.travelers.common.model.dto.FileInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ArticleBookmarkDto {
	
	private int articleNo;
	private int contentId;
	private String articleType;
//	private String userId;
	private String email;
	private boolean isLike;
	private boolean isBookmark;

}
