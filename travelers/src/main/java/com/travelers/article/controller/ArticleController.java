package com.travelers.article.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.travelers.article.model.dto.*;
import com.travelers.common.model.dto.FileInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import com.travelers.article.model.service.ArticleService;
import com.travelers.common.util.RestExceptionUtil;
import com.travelers.common.util.SecurityUtil;

import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/article")
@CrossOrigin("*")
@Slf4j
@Api
public class ArticleController {
	private static final long serialVersionUID = 1L;

	@Value("${file.path}")
	private String uploadPath;

	@Value("${file.imgPath}")
	private String uploadImgPath;

	private ArticleService articleService;
	
	@Autowired
	private static SecurityUtil securityUtil;

	@Autowired
	public ArticleController(ArticleService articleService) {
		super();
		this.articleService = articleService;
	}

	@GetMapping
//	public ResponseEntity<?> articleList(String keyword, String order) {
	public ResponseEntity<?> articleList(ArticleParameterDto articleParameterDto) {
		log.debug("ArticleController:: articleList call");
		try {
			List<ArticleDto> list = articleService.listArticle(articleParameterDto);
			System.out.println(list.size());
			log.debug("ArticleController:: 게시글 목록: " + list);
			if(list != null && !list.isEmpty()) {
				log.debug("ArticleController:: 게시글 목록 정상 반환");
				return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);
			} else {
				log.debug("ArticleController:: 빈 게시글 목록 ");
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.debug("ArticleController:: 게시글 목록 조회 중 에러 발생");
			return exceptionHandling(e);
		}
	}
	
	@GetMapping("/{articleNo}/{articleType}")
	public ResponseEntity<?> articleView(@PathVariable("articleNo") String no, @PathVariable("articleType") String articleType) {
		log.debug("ArticleController:: articleView call");
		try {
			int articleNo = Integer.parseInt(no);
			log.debug("ArticleController:: articleList call - updateHit()");
			articleService.updateHit(articleNo);
			ArticleDto articleDto = articleService.getArticle(articleNo, articleType);
			return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@PostMapping
	public ResponseEntity<?> articleWrite(@RequestBody ArticleDto articleDto) {
		log.debug("ArticleController:: articleWrite call");
		try {
			articleService.writeArticle(articleDto);
			List<ArticleDto> list = articleService.listArticle(new ArticleParameterDto());
			return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@PostMapping("/{articleNo}")
	public ResponseEntity<?> commentWrite(@RequestBody CommentDto commentDto, @PathVariable("articleNo") String no) {
		log.debug("ArticleController:: commentWrite call");
		try {
			articleService.writeComment(commentDto);
			List<CommentDto> comments = articleService.listComment(Integer.parseInt(no));
			return new ResponseEntity<List<CommentDto>>(comments, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@GetMapping("/comment/{articleNo}")
	public ResponseEntity<?> commentList(@RequestParam("articleNo") String no) {
		log.debug("ArticleController:: commentList call");
		try {
			int articleNo = Integer.parseInt(no);
			System.out.println("ArticleController - commentList - articleNo: " + articleNo);
			log.debug("ArticleController:: articleList call - updateHit()");
			List<CommentDto> comments = articleService.listComment(articleNo);
			System.out.println("ArticleController - commentList - comments: " + comments);
			return new ResponseEntity<List<CommentDto>>(comments, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@PostMapping("/hotplace")
	public ResponseEntity<?> hotplaceWrite(HotPlaceDto hotPlaceDto, @RequestPart("thumbNail") List<MultipartFile> files) {
		log.debug("ArticleController:: hotplaceWrite call");
		try {
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = uploadImgPath + File.separator + today;
			log.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
			for (MultipartFile mfile : files) {
				FileInfoDto fileInfoDto = new FileInfoDto();
				String originalFileName = mfile.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					fileInfoDto.setSaveFolder(today);
					fileInfoDto.setOriginalFile(originalFileName);
					fileInfoDto.setSaveFile(saveFileName);
					log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
					mfile.transferTo(new File(folder, saveFileName));
				}
				fileInfos.add(fileInfoDto);
			}
			hotPlaceDto.setFileInfos(fileInfos);
			articleService.writeArticle(hotPlaceDto);
			List<ArticleDto> list = articleService.listArticle(new ArticleParameterDto());
			return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
//	@PostMapping("/schedule")
//	public ResponseEntity<?> shareSchedule(@RequestBody ScheduleDto scheduleDto, HttpServletRequest request, HttpServletResponse response) {
//		try {
//			log.debug("AttractionController:: shareSchedule call");
//			List<Integer> contentIdList = (List<Integer>) request.getAttribute("contentIdList");
//			ObjectMapper mapper = new ObjectMapper();
//			String path = mapper.writeValueAsString(contentIdList);
//			log.debug("AttractionController:: shareSchedule call - path: " + path + path.getClass());
//			if (scheduleDto.getUserId() == null) {
//				scheduleDto.setUserId(articleService.getUserIdByEmail(securityUtil.getCurrentMemberId()));
//			}
//			scheduleDto.setPath(path);
//			scheduleDto.setArticleType("schedule");
//			articleService.writeArticle(scheduleDto);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RestExceptionUtil.messageHandling("여행 계획 공유 중 에러가 발생했습니다..", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return RestExceptionUtil.messageHandling("여행 계획 게시글이 등록되었습니다.", HttpStatus.OK);
//	}

	@PutMapping()
	public ResponseEntity<?> articleModify(@RequestBody ArticleDto articleDto) {
		log.debug("ArticleController:: articleModify call");
		try {
			articleService.modifyArticle(articleDto);
			List<ArticleDto> list = articleService.listArticle(new ArticleParameterDto());
			return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@PutMapping("/like")
	public ResponseEntity<?> articleLike(@RequestBody ArticleBookmarkDto articleBookmarkDto) {
		log.debug("ArticleController:: articleLike call");
		try {
			ArticleBookmarkDto result = articleService.getArticleBookmark(articleBookmarkDto);
			if (result == null) {
				articleBookmarkDto.setLike(true);
				articleService.addArticleBookmark(articleBookmarkDto);
			} else {
				articleService.setArticleLike(articleBookmarkDto);
			}
			articleService.updateLike(articleBookmarkDto.getArticleNo());
			ArticleDto articleDto = articleService.getArticle(articleBookmarkDto.getArticleNo(), articleBookmarkDto.getArticleType());
			System.out.println("CONTROLLER:: ARTICLE_LIKE - articleDto: " + articleDto);
			return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	@PutMapping("/bookmark")
	public ResponseEntity<?> articleBookmark(@RequestBody ArticleBookmarkDto articleBookmarkDto) {
		log.debug("ArticleController:: articleBookmark call");
		try {
			ArticleBookmarkDto result = articleService.getArticleBookmark(articleBookmarkDto);
			if (result == null) {
				articleBookmarkDto.setBookmark(true);
				articleService.addArticleBookmark(articleBookmarkDto);
			} else {
				articleService.toggleArticleBookmark(articleBookmarkDto);
			}
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	@GetMapping("/info")
	public ResponseEntity<?> getArticleInfo(ArticleBookmarkDto articleBookmarkDto) {
		log.debug("ArticleController:: getArticleInfo call");
		try {
			ArticleBookmarkDto result = articleService.getArticleBookmark(articleBookmarkDto);
			System.out.println("GET_ARTICLE_INFO:: RESULT - " + result);
			if (result == null) {
				result = new ArticleBookmarkDto();
				System.out.println("GET_ARTICLE_INFO:: RESULT was null - " + result);
			}
			return new ResponseEntity<ArticleBookmarkDto>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	@GetMapping("/infos")
	public ResponseEntity<?> getArticleInfos(ArticleBookmarkDto articleBookmarkDto) {
		log.debug("ArticleController:: getArticleInfo call");
		try {
			List<ArticleBookmarkDto> result = articleService.getArticleBookmarks(articleBookmarkDto);
			System.out.println("GET_ARTICLE_INFO:: RESULT - " + result);
			if (result == null || result.size()==0) {
				result.add(new ArticleBookmarkDto());
				System.out.println("GET_ARTICLE_INFO:: RESULT was null - " + result);
			}
			return new ResponseEntity<List<ArticleBookmarkDto>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@PutMapping("/hotplace")
	public ResponseEntity<?> hotplaceModify(HotPlaceDto hotPlaceDto) {
		log.debug("ArticleController:: hotplaceModify call");
		try {
			System.out.println(hotPlaceDto.getArticleNo());
			System.out.println(hotPlaceDto);
			articleService.modifyHotplace(hotPlaceDto);
			List<ArticleDto> list = articleService.listArticle(new ArticleParameterDto());
			return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@PutMapping("/comment")
	public ResponseEntity<?> commentModify(@RequestBody CommentDto commentDto) {
		log.debug("ArticleController:: commentModify call");
		try {
			System.out.println(commentDto);
			articleService.modifyComment(commentDto);
			CommentDto comment = articleService.getComment(commentDto.getCommentNo());
			System.out.println(comment);
			return new ResponseEntity<CommentDto>(comment, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}


	@DeleteMapping("/{articleNo}/{articleType}")
	public ResponseEntity<?> articleDelete(@PathVariable("articleNo") String no, @PathVariable("articleType") String articleType) {
		try {
			System.out.println(no + ", " + articleType);
			articleService.deleteArticle(Integer.parseInt(no), articleType);
			List<ArticleDto> list = articleService.listArticle(new ArticleParameterDto());
			if(list != null && !list.isEmpty()) {
				return new ResponseEntity<List<ArticleDto>>(list, HttpStatus.OK);				
			} else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@DeleteMapping("/comment")
	public ResponseEntity<?> commentDelete(CommentDto commentDto) {
		try {
			System.out.println(commentDto);
			articleService.deleteComment(commentDto.getCommentNo());
			List<CommentDto> list = articleService.listComment(commentDto.getArticleNo());
			if(list != null && !list.isEmpty()) {
				return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
