package com.travelers.attraction.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttractionDto {

	int contentId;
	int contentTypeId;
	String title;
	String addr1;
	String addr2;
	String zipcode;
	String tel;
	String image1;
	String image2;
	int readcount;
	int sidoCode;
	int gugunCode;
	double latitude;
	double longitude;
	String mlevel;
	String overview;
	String createTime;
	String modifyTime;
	
}
