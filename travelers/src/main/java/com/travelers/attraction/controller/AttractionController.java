package com.travelers.attraction.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelers.article.controller.ArticleController;
import com.travelers.article.model.dto.ArticleDto;
import com.travelers.article.model.dto.ScheduleDto;
import com.travelers.article.model.service.ArticleService;
import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.attraction.model.dto.GugunDto;
import com.travelers.attraction.model.dto.SidoDto;
import com.travelers.attraction.model.service.AttractionService;
import com.travelers.common.util.RestExceptionUtil;
import com.travelers.common.util.SecurityUtil;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/attraction")
@RequiredArgsConstructor
@Slf4j
@Api
public class AttractionController {
	
	@Autowired
	private final AttractionService attractionService;
	@Autowired
	private final ArticleService articleService;
	@Autowired
	private static SecurityUtil securityUtil;

	@GetMapping
	public ResponseEntity<?> sido() {
		try {
			List<SidoDto> sidoList = attractionService.getSido();
			return new ResponseEntity<List<SidoDto>>(sidoList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("시도 정보 로드 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{sidoCode}")
	public ResponseEntity<?> gugun(@PathVariable("sidoCode") int sidoCode) {
		try {
			List<GugunDto> gugunList = attractionService.getGugun(sidoCode);

			return new ResponseEntity<List<GugunDto>>(gugunList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("구군 정보 로드 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{sidoCode}/{gugunCode}/{query}/{contentType}")
	public ResponseEntity<?> search(@PathVariable("sidoCode") int sidoCode, @PathVariable("gugunCode") int gugunCode,
			@PathVariable("query") String query, @PathVariable("contentType") int contentType) {
		try {
			query = query.substring(1);
			List<AttractionDto> attractionList = attractionService.getAttractionList(sidoCode, gugunCode, query,
					contentType);
			return new ResponseEntity<List<AttractionDto>>(attractionList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("관광지 정보 로드 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/detail/{contentId}")
	public ResponseEntity<?> detail(@PathVariable("contentId") int contentId) {
		try {
			AttractionDto attraction = attractionService.getAttraction(contentId);
			return new ResponseEntity<AttractionDto>(attraction, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("관광지 정보 로드 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/schedule/{contentId}")
	public ResponseEntity<?> addSchedule(@PathVariable("contentId") int contentId, HttpServletRequest request, HttpServletResponse response) {
		List<Integer> contentIdList = (List<Integer>) request.getAttribute("contentIdList");
		// 중복된 id가 있는 경우 실패 (추후 최단 경로 알고리즘 제거 예정)
		for(int id: contentIdList) {
			if(id == contentId) return RestExceptionUtil.messageHandling("현재 관광지는 이미 여행 경로에 등록되어 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		contentIdList.add(contentId);
		
		try {
			HttpSession session = request.getSession();
			session.setAttribute("contentIdList", contentIdList);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("여행 계획 갱신 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return RestExceptionUtil.messageHandling("선택된 관광지가 여행 계획에 추가되었습니다.", HttpStatus.OK);
	}
	
	@DeleteMapping("/schedule/{contentId}")
	public ResponseEntity<?> deleteSchedule(@PathVariable("contentId") int contentId, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Integer> contentIdList = (List<Integer>) request.getAttribute("contentIdList");
			contentIdList.remove(new Integer(contentId));
			HttpSession session = request.getSession();
			session.setAttribute("contentIdList", contentIdList);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("관광지 제거 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return RestExceptionUtil.messageHandling("선택된 관광지가 여행 계획에서 제거되었습니다.", HttpStatus.OK);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> shareSchedule(@RequestBody ScheduleDto scheduleDto, HttpServletRequest request, HttpServletResponse response) {
		try {
			log.debug("AttractionController:: shareSchedule call");
//			List<Integer> contentIdList = (List<Integer>) request.getAttribute("contentIdList");
//			ObjectMapper mapper = new ObjectMapper();
//			String path = mapper.writeValueAsString(contentIdList);
//			log.debug("AttractionController:: shareSchedule call - path: " + path + " " + path.getClass());
//			if (scheduleDto.getUserId() == null) {
//				scheduleDto.setUserId(articleService.getUserIdByEmail(securityUtil.getCurrentMemberId()));
//			}
//			scheduleDto.setPath(path);
//			scheduleDto.setArticleType("schedule");
			articleService.writeArticle(scheduleDto);
		} catch (Exception e) {
			e.printStackTrace();
			return RestExceptionUtil.messageHandling("여행 계획 공유 중 에러가 발생했습니다..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return RestExceptionUtil.messageHandling("여행 계획 게시글이 등록되었습니다.", HttpStatus.OK);
	}
	
}

