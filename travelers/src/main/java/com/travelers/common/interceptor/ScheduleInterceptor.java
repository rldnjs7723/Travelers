package com.travelers.common.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.plugin.Intercepts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelers.attraction.model.service.AttractionService;
import com.travelers.common.util.SecurityUtil;
import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.service.UserService;

import lombok.RequiredArgsConstructor;

//@SuppressWarnings("deprecation")
//public class ConfirmInterceptor extends HandlerInterceptorAdapter {
//spring 5.3 부터는 HandlerInterceptor implements
@Component
@RequiredArgsConstructor
public class ScheduleInterceptor implements HandlerInterceptor { 
	
	private final UserService userService;
	private final AttractionService attractionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		try {
			// Jackson
//			ObjectMapper mapper = new ObjectMapper();
//			Cookie[] cookies = request.getCookies();
//	    	
//	    	String json = null;
//	    	if(cookies != null) {
//	    		for(Cookie cookie: cookies) {
//		    		if(cookie.getName().equals("scheduleJSON")) {
//		    			json = cookie.getValue();
//		    			break;
//		    		}
//		    	}
//	    	}
//	    	
//	    	if(json == null) {
//	    		contentIdList = new ArrayList<Integer>();
//	    		Cookie cookie = new Cookie("scheduleJSON", mapper.writeValueAsString(contentIdList));
//	    		cookie.setPath("/");
//				cookie.setMaxAge(60*60*24*365);
//				response.addCookie(cookie);
//	    	} else {
//	    		contentIdList = (List<Integer>) mapper.readValue(json, List.class);
//	    	}
			
			HttpSession session = request.getSession();
			List<Integer> contentIdList = (List<Integer>) session.getAttribute("contentIdList");
			if(contentIdList == null) contentIdList = new ArrayList<Integer>();
			
	    	request.setAttribute("contentIdList", contentIdList);
	    	return true;
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/error");
			return false;
		}
	}
	
}