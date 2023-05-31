package com.travelers.article.model.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.travelers.article.model.dto.*;
import com.travelers.common.model.dto.FileInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {
	
//	List<ArticleDto> listArticle(Map<String, Object> param) throws SQLException;
	List<ArticleDto> listArticle(ArticleParameterDto articleParameterDto) throws SQLException;
	ArticleDto getArticle(int articleNo) throws SQLException;
	HotPlaceDto getHotPlace(int articleNo) throws SQLException;
	ScheduleDto getSchedule(int articleNo) throws SQLException;
	void writeArticle(ArticleDto articleDto) throws SQLException;
	void writeHotplace(HotPlaceDto hotPlaceDto) throws SQLException;
	void writeSchedule(ScheduleDto scheduleDto) throws SQLException;

	void registerFile(ArticleDto articleDto) throws SQLException;

	// Integer getTotalArticleCount(Map<String, Object> param) throws SQLException;
	int modifyArticle(ArticleDto articleDto) throws SQLException;
	int modifyHotplace(HotPlaceDto hotPlaceDto) throws SQLException;
	int modifySchedule(ScheduleDto scheduleDto) throws SQLException;
//	void updateHit(HashMap<String, Object> map) throws SQLException;
	void updateHit(int articleNo) throws SQLException;
//	void updateLike(HashMap<String, Object> map) throws SQLException;
	void updateLike(int articleNo) throws SQLException;
	int deleteArticle(int articleNo) throws SQLException;
	int deleteHotplace(int articleNo) throws SQLException;
	int deleteSchedule(int articleNo) throws SQLException;
	void deleteFile(int articleNo) throws Exception;
	List<FileInfoDto> fileInfoList(int articleNo) throws Exception;

//	CommentDto getComment(int commentNo) throws SQLException;

	List<CommentDto> listComment(int articleNo) throws SQLException;
	CommentDto getComment(int commentNo) throws SQLException;

	void writeComment(CommentDto commentDto) throws SQLException;

	int modifyComment(CommentDto commentDto) throws SQLException;

	int deleteComment(int commentNo) throws SQLException;
	ArticleBookmarkDto getArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	List<ArticleBookmarkDto> getArticleBookmarks(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void addArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void setArticleLike(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void toggleArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	List<Integer> getTotalRate(int articleNo) throws SQLException;

}
