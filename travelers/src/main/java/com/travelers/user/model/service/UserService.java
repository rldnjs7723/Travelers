package com.travelers.user.model.service;

import java.sql.SQLException;
import java.util.Map;

import com.travelers.user.model.dto.TokenInfo;
import com.travelers.user.model.dto.UserDto;
//import com.travelers.user.model.dto.UserModifyDto;

public interface UserService {

	int idCheck(String id) throws Exception;

	int emailCheck(String email) throws Exception;
	int phoneCheck(String phone) throws Exception;
	int joinUser(UserDto userDto) throws Exception;
	UserDto getUser(String email) throws Exception;
	UserDto getUserPhone (String phone) throws Exception;
	String getNewPassword(String email) throws Exception;
	TokenInfo loginUser(String email, String password) throws Exception;
	TokenInfo refreshUser(UserDto userDto) throws Exception;
//	int modifyUser(Map<String, String> map) throws Exception;
	int modifyUserImg(UserDto userDto) throws Exception;
	int modifyUser(UserDto userDto) throws Exception;
	int deleteUser(UserDto userDto) throws Exception;

	int insertAttractionBookmark(String email, int contentId) throws Exception;
	int deleteAttractionBookmark(String email, int contentId) throws Exception;

	// 비밀번호 암호화 관련
	String hashing(byte[] password, String salt) throws Exception;
	String getSalt() throws Exception;
	String byteToString(byte[] temp) throws Exception;
}
