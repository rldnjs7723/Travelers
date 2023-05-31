package com.travelers.attraction.model.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.attraction.model.dto.GugunDto;
import com.travelers.attraction.model.dto.SidoDto;

@Mapper
public interface AttractionMapper {
	// 시도 정보
	List<SidoDto> getSido() throws Exception;

	// 구군 정보
	List<GugunDto> getGugun(int sidoCode) throws Exception;

	// 관광지 정보 검색
//	List<AttractionDto> getAttractionList(int sidoCode, int gugunCode, String query, int contentType) throws Exception;
	List<AttractionDto> getAttractionList(HashMap<String, Object> map) throws Exception;

	AttractionDto getAttraction(int contentId) throws Exception;
	
	// 리스트에 포함된 관광지 정보 전부 가져오기
	List<AttractionDto> getScheduleInfo(List<Integer> scheduleList) throws Exception;


}
