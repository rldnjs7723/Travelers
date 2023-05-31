package com.travelers.article.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.travelers.article.model.dto.*;

public interface ArticleService {

	List<ArticleDto> listArticle(ArticleParameterDto articleParameterDto) throws SQLException;
	ArticleDto getArticle(int articleNo, String articleType) throws Exception;
	void writeArticle(ArticleDto articleDto) throws SQLException;
	// int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	void updateHit(int articleNo) throws SQLException;
//	void updateLike(int articleNo, boolean on) throws SQLException;
	void updateLike(int articleNo) throws SQLException;
	int modifyArticle(ArticleDto articleDto) throws SQLException;
	void modifyHotplace(HotPlaceDto hotplaceDto) throws SQLException;
	void deleteArticle(int articleNo, String articleType) throws Exception;
	String getUserIdByEmail(String email) throws SQLException;

	List<CommentDto> listComment(int articleNo) throws SQLException;
	public CommentDto getComment(int commentNo) throws Exception;

	void writeComment(CommentDto commentDto) throws SQLException;

	int modifyComment(CommentDto commentDto) throws SQLException;

	int updateCommentLike(CommentDto commentDto) throws SQLException;

	int deleteComment(int commentNo) throws SQLException;
	ArticleBookmarkDto getArticleBookmark (ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	List<ArticleBookmarkDto> getArticleBookmarks (ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void addArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void setArticleLike(ArticleBookmarkDto articleBookmarkDto) throws SQLException;
	void toggleArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException;


}
