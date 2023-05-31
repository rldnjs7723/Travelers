package com.travelers.user.model.mapper;

import java.sql.SQLException;
import java.util.List;

import com.travelers.article.model.dto.ArticleDto;
import com.travelers.attraction.model.dto.AttractionDto;
import com.travelers.common.model.dto.FileInfoDto;
//import com.travelers.user.model.dto.UserModifyDto;
import org.apache.ibatis.annotations.Mapper;

import com.travelers.user.model.dto.UserDto;

@Mapper
public interface UserMapper {

	int idCheck(String id) throws SQLException;
	int emailCheck(String email) throws SQLException;
	int phoneCheck(String phone) throws SQLException;
	int joinUser(UserDto userDto) throws SQLException;
	UserDto getUser(String email) throws SQLException;
	UserDto getUserPhone(String phone) throws SQLException;
	int modifyUser(UserDto userDto) throws SQLException;
	int deleteUser(UserDto userDto) throws SQLException;
	int insertAttractionBookmark(String email, int contentId) throws SQLException;
	int deleteAttractionBookmark(String email, int contentId) throws SQLException;
	List<AttractionDto> getAttractionBookmark(String email) throws SQLException;
	void registerFile(UserDto UserDto) throws SQLException;
	List<FileInfoDto> fileInfo(String id) throws Exception;
}
