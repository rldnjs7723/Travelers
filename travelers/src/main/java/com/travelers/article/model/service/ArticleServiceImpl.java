package com.travelers.article.model.service;

import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelers.article.model.dto.*;
import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.attraction.model.dto.PlanDto;
import com.travelers.attraction.model.mapper.AttractionMapper;
import com.travelers.common.model.dto.FileInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelers.article.model.mapper.ArticleMapper;
import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.mapper.UserMapper;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

	private ArticleMapper articleMapper;
	private UserMapper userMapper;
	private AttractionMapper attractionMapper;

	@Autowired
	public ArticleServiceImpl(ArticleMapper articleMapper, UserMapper userMapper, AttractionMapper attractionMapper) {
		super();
		this.articleMapper = articleMapper;
		this.userMapper = userMapper;
		this.attractionMapper = attractionMapper;
	}

	@Override
	public void writeArticle(ArticleDto articleDto) throws SQLException {
		articleMapper.writeArticle(articleDto);
		if ("hotplace".equals(articleDto.getArticleType())) {
			HotPlaceDto hotPlaceDto = (HotPlaceDto) articleDto;
			articleMapper.writeHotplace(hotPlaceDto);
		} else if ("schedule".equals(articleDto.getArticleType())) {
			ScheduleDto scheduleDto = (ScheduleDto) articleDto;
			articleMapper.writeSchedule(scheduleDto);
		}
		List<FileInfoDto> fileInfos = articleDto.getFileInfos();
		if (fileInfos != null && !fileInfos.isEmpty()) {
			System.out.println("ServiceImpl:: 파일 저장");
			articleMapper.registerFile(articleDto);
		}
	}

	@Override
	public List<ArticleDto> listArticle(ArticleParameterDto articleParameterDto) throws SQLException {
		int start = articleParameterDto.getPg() == 0 ? 0 : (articleParameterDto.getPg() - 1) * articleParameterDto.getSpp();
		articleParameterDto.setStart(start);
		if ("".equals(articleParameterDto.getKey()))
			articleParameterDto.setKey("all");
		return articleMapper.listArticle(articleParameterDto);
	}

	// 접두사, 접미사 일치 길이 테이블 계산
	public static int[] makeTable(String keyword) {
		int size = keyword.length();
		int[] table = new int[size];

		int j = 0;
		for (int i = 1; i < size; i++) {
			while (j > 0 && keyword.charAt(i) != keyword.charAt(j)) {
				j = table[j - 1];
			}

			if (keyword.charAt(i) == keyword.charAt(j)) {
				table[i] = ++j;
			}
		}

		return table;
	}


	@Override
	public ArticleDto getArticle(int articleNo, String articleType) throws Exception {
		ArticleDto articleDto = null;
		if ("hotplace".equals(articleType)) {
			HotPlaceDto hotPlaceDto = articleMapper.getHotPlace(articleNo);
			List<Integer> rateList = articleMapper.getTotalRate(hotPlaceDto.getContentId());
			int sum = 0;
			for (int rate : rateList) {
				sum += rate;
			}
			hotPlaceDto.setTotalRate(Math.round(( (double) sum / rateList.size() ) * 100) / 100.0);
			articleDto = hotPlaceDto;
		} else if ("schedule".equals(articleType)) {
			articleDto = articleMapper.getSchedule(articleNo);
			log.debug("ArticleService getArticle schedule: {}", articleDto);

			// JSON으로 저장된 계획 경로 파싱
			ObjectMapper objectMapper = new ObjectMapper();
			List<HashMap> plans = objectMapper.readValue(((ScheduleDto) articleDto).getPath(), List.class);
			log.debug("ArticleService getArticle parsed: {}", plans);

			// 중복된 ContentId 제거
			HashSet<Integer> contentIdSet = new HashSet<>();
			for(int i = 0; i < plans.size(); i++) {
				for(Integer contentId: (ArrayList<Integer>) plans.get(i).get("path")) {
					contentIdSet.add(contentId);
				}
			}

			// ContentId가 일치하는 관광지 정보 가져오기
			List<AttractionDto> attractionDetail = attractionMapper.getScheduleInfo(new ArrayList<>(contentIdSet));
//			log.debug("ArticleService getArticle attractionDetail: {}", attractionDetail);

			// 관광지 정보 매핑
			HashMap<Integer, AttractionDto> detailMap = new HashMap<>();
			for(AttractionDto attraction: attractionDetail) {
				detailMap.put(attraction.getContentId(), attraction);
			}

			// 리턴 할 계획 정보 생성
			List<PlanDto> planDetail = new ArrayList<>();
			for(int i = 0; i < plans.size(); i++) {
				PlanDto plan = new PlanDto();
				plan.setDay((Integer) plans.get(i).get("day"));
				plan.setPath(new ArrayList<>());
				for(Integer contentId: (ArrayList<Integer>) plans.get(i).get("path")) {
					plan.getPath().add(detailMap.get(contentId));
				}

				planDetail.add(plan);
			}
//			log.debug("ArticleService getArticle planDetail: {}", planDetail);
			
			// JSON 형태로 변환
			((ScheduleDto) articleDto).setPath(objectMapper.writeValueAsString(planDetail));
		} else {
			articleDto = articleMapper.getArticle(articleNo);
		}
		articleDto.setFileInfos(articleMapper.fileInfoList(articleNo));
		return articleDto;
//		if ("hotplace".equals(articleType)) {
//			return articleMapper.getHotPlace(articleNo);
//		} else if ("schedule".equals(articleType)) {
//			return articleMapper.getSchedule(articleNo);
//		}
//		return articleMapper.getArticle(articleNo);
	}

	@Override
	public void updateHit(int articleNo) throws SQLException {
		articleMapper.updateHit(articleNo);
	}

	@Override
	public void updateLike(int articleNo) throws SQLException {
		articleMapper.updateLike(articleNo);
	}

	@Override
	public int modifyArticle(ArticleDto articleDto) throws SQLException {
		return articleMapper.modifyArticle(articleDto);
	}

	public void modifyHotplace(HotPlaceDto hotplaceDto) throws SQLException {
		articleMapper.modifyHotplace(hotplaceDto);
		articleMapper.modifyArticle(hotplaceDto);
	}

	@Override
	public void deleteArticle(int articleNo, String articleType) throws Exception {
		articleMapper.deleteFile(articleNo);
		if ("hotplace".equals(articleType)) {
			articleMapper.deleteHotplace(articleNo);
		} else if ("schedule".equals(articleType)) {
			articleMapper.deleteSchedule(articleNo);
		}
		articleMapper.deleteArticle(articleNo);
	}

	@Override
	public String getUserIdByEmail(String email) throws SQLException {
		return userMapper.getUser(email).getId();
	}

	@Override
	public List<CommentDto> listComment(int articleNo) throws SQLException {
		List<CommentDto> comments = articleMapper.listComment(articleNo);
		return comments;
	}
	@Override
	public CommentDto getComment(int commentNo) throws Exception {
		CommentDto commentDto = articleMapper.getComment(commentNo);
		return commentDto;
	}

	@Override
	public void writeComment(CommentDto commentDto) throws SQLException {
		articleMapper.writeComment(commentDto);
	}

	@Override
	public int modifyComment(CommentDto commentDto) throws SQLException {
		return articleMapper.modifyComment(commentDto);
	}

	@Override
	public int updateCommentLike(CommentDto commentDto) throws SQLException {
		commentDto.setLike(commentDto.getLike()+1);
		return articleMapper.modifyComment(commentDto);
	}

	@Override
	public int deleteComment(int commentNo) throws SQLException {
		return articleMapper.deleteComment(commentNo);
	}

	@Override
	public ArticleBookmarkDto getArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException {
		System.out.println("ArticleServiceImpl:: getArticleBookmark - articleBookmarkDto: " + articleBookmarkDto);
		return articleMapper.getArticleBookmark(articleBookmarkDto);
	}

	public List<ArticleBookmarkDto> getArticleBookmarks(ArticleBookmarkDto articleBookmarkDto) throws SQLException {
		System.out.println("ArticleServiceImpl:: getArticleBookmarks - articleBookmarkDto: " + articleBookmarkDto);
		return articleMapper.getArticleBookmarks(articleBookmarkDto);
	}

	@Override
	public void addArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException {
		articleMapper.addArticleBookmark(articleBookmarkDto);
	}

	@Override
	public void setArticleLike(ArticleBookmarkDto articleBookmarkDto) throws SQLException {
		articleBookmarkDto.setLike(true);
		articleMapper.setArticleLike(articleBookmarkDto);
	}

	@Override
	public void toggleArticleBookmark(ArticleBookmarkDto articleBookmarkDto) throws SQLException {
		articleBookmarkDto.setBookmark(!articleBookmarkDto.isBookmark());
		articleMapper.toggleArticleBookmark(articleBookmarkDto);
	}

}
