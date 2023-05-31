package com.travelers.user.model.dto;

import java.sql.Blob;
import java.util.List;

import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.common.model.dto.FileInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
	
	private int idx;
	private String email;
	private String password;
	private String newpassword;
	private String passwordSalt;
	private String name;
	private String id;
	private String token;
	private String birth;
	private String gender;
	private String phone;
	private String registerDate;
	private String address;
	private String grade;
	private int exp;
	private String lastSigninDate;
	private String profileMsg;
	// private Blob profileImg;
	private List<FileInfoDto> profileImgInfo;
	private String devices;
	private List<AttractionDto> attractionBookmark;

}
