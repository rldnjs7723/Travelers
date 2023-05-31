package com.travelers.article.model.dto;

import com.travelers.common.model.dto.FileInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CommentDto {

	private int articleNo;
//	private String articleType;
	private int commentNo;
	private int parentCommentNo;
	private String userId;
	private String writeTime;
	private String content;
	private int like;

}
