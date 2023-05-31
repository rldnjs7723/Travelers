package com.travelers.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.attraction.model.dto.SidoDto;
import com.travelers.attraction.model.service.AttractionService;
import com.travelers.common.util.SecurityUtil;
import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Api
public class MainController {
	
	private final UserService userService;
	private final AttractionService attractionService;
	
	@RequestMapping("/")
	public String index(Model model) {
		try {
			String email = SecurityUtil.getCurrentMemberId();
			UserDto loginInfo = userService.getUser(email);
			model.addAttribute("loginInfo", loginInfo);
			
			return "/index";
		} catch (Exception e) {
			e.printStackTrace();
			return "/error";
		}
	}
	
	@GetMapping("/mypage")
	public String mypage() {
		return "/user/mypage";
	}
	
	@GetMapping("/search")
	public String search(Model model) {
		try {
			List<SidoDto> sidoList = attractionService.getSido();
			model.addAttribute("sidoList", sidoList);
			
			return "/trip/search";
		} catch (Exception e) {
			e.printStackTrace();
			return "/error";
		}
	}
	
	@GetMapping("/schedule")
	public String schedule(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			List<Integer> contentIdList = (List<Integer>) request.getAttribute("contentIdList");
	    	List<AttractionDto> scheduleInfo = attractionService.getScheduleInfo(contentIdList);
	    	model.addAttribute("scheduleInfo", scheduleInfo);
	    	
			return "/trip/schedule";
		} catch (Exception e) {
			e.printStackTrace();
			return "/error";
		}
	}
	
//	@RequestMapping("/error")
//	public String error() {
//		return "/error/error";
//	}
}
