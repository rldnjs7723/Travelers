package com.travelers.user.model.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.travelers.common.model.dto.FileInfoDto;
//import com.travelers.user.model.dto.UserModifyDto;
import com.travelers.common.util.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelers.common.provider.JwtTokenProvider;
import com.travelers.user.model.dto.TokenInfo;
import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	
	private static final int SALT_SIZE = 16;
	
	private final UserMapper userMapper;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public int idCheck(String id) throws Exception {
		return userMapper.idCheck(id);
	}

	@Override
	public int emailCheck(String email) throws Exception {
		return userMapper.emailCheck(email);
	}

	@Override
	public int phoneCheck(String phone) throws Exception {
		return userMapper.phoneCheck(phone);
	}

	@Override
	public int joinUser(UserDto userDto) throws Exception {
		String salt = getSalt();
		String hashingPassword = hashing(userDto.getPassword().getBytes(), salt);
		userDto.setPassword(hashingPassword);
		userDto.setPasswordSalt(salt);
		
		return userMapper.joinUser(userDto);
	}

	@Override
	public UserDto getUser(String email) throws Exception {
		UserDto userDto = userMapper.getUser(email);
		userDto.setAttractionBookmark(userMapper.getAttractionBookmark(email));
//		articleDto.setFileInfos(articleMapper.fileInfoList(articleNo));
		userDto.setProfileImgInfo(userMapper.fileInfo(userDto.getId()));
		return userDto;
	}

	@Override
	public UserDto getUserPhone(String phone) throws Exception {
		return userMapper.getUserPhone(phone);
	}

	@Override
	public String getNewPassword(String email) throws Exception {
		UserDto userDto = userMapper.getUser(email);

		// 새 비밀번호 발급
		String password = SecurityUtil.getRamdomPassword();
		// 비밀번호 Encoding
		String salt = userDto.getPasswordSalt();
		String hashingPassword = hashing(password.getBytes(), salt);
	
		// 비밀번호 수정
		userDto.setPassword(hashingPassword);
		userMapper.modifyUser(userDto);
		return password;
	}

	@Transactional
	@Override
	public TokenInfo loginUser(String email, String password) throws Exception {
		UserDto userInfo = null;
		if((userInfo = userMapper.getUser(email)) == null) return null;
		
		// 비밀번호 Encoding
		String salt = userInfo.getPasswordSalt();
		String hashingPassword = hashing(password.getBytes(), salt);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, hashingPassword);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

		// RefreshToken DB에 저장
		userInfo.setToken(tokenInfo.getRefreshToken());
		userMapper.modifyUser(userInfo);
		return tokenInfo;
	}

	@Override
	public TokenInfo refreshUser(UserDto userDto) throws Exception {
		// RefreshToken Validate
		if(userDto.getToken() != null && jwtTokenProvider.validateToken(userDto.getToken())) {
			// 저장된 refreshToken과 동일한지 체크
			UserDto userInfo = userMapper.getUser(userDto.getEmail());
			if(userDto.getToken().equals(userInfo.getToken())) {
				// Access 토큰 새로 생성
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());
				Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
				TokenInfo tokenInfo = jwtTokenProvider.refresh(authentication, userDto.getToken());

				return tokenInfo;
			} else {
				System.out.println("refreshToken이 다릅니다.");
				return null;
			}
		} else {
			// RefreshToken도 만료된 경우
			System.out.println("refreshToken이 만료되었습니다.");
			return null;
		}
	}

//	@Override
//	public int modifyUser(Map<String, String> map) throws Exception {
//		UserDto userInfo = null;
//		if((userInfo = userMapper.getUser(map.get("email")) ) == null) return 0;
//
//		// 비밀번호 Encoding
//		String salt = userInfo.getPasswordSalt();
//		String hashingPassword = hashing(map.get("password").getBytes(), salt);
//		// 비밀번호가 다르면 수정 불가
//		if(!userInfo.getPassword().equals(hashingPassword)) return 0;
//
//		// 수정된 비밀번호 반영
//		if(map.get("newpassword") != null && map.get("newpassword").length() > 0) {
//			hashingPassword = hashing(map.get("newpassword").getBytes(), salt);
//			userInfo.setPassword(hashingPassword);
//		}
//
//		// 수정된 이미지 반영
//		if(map.get("profileImg") != null && map.get("profileImg").length() > 0) {
////			userInfo.setProfileImg(map.get("profileImg"));
//		}
//
//		return userMapper.modifyUser(userInfo);
//	}

	@Override
	public int modifyUserImg(UserDto userDto) throws Exception {
		log.debug("UserServiceImpl modifyUserImg: userDto {}", userDto);

		UserDto userInfo = null;
		if((userInfo = userMapper.getUser(userDto.getEmail())) == null) return 0;

		// 비밀번호 Encoding
		String salt = userInfo.getPasswordSalt();
		String hashingPassword = hashing(userDto.getPassword().getBytes(), salt);
		// 비밀번호가 다르면 수정 불가
		if(!userInfo.getPassword().equals(hashingPassword)) return 0;

		// 수정된 비밀번호 반영
		if(userDto.getNewpassword() != null && userDto.getNewpassword().length() > 0) {
			hashingPassword = hashing(userDto.getNewpassword().getBytes(), salt);
			userInfo.setPassword(hashingPassword);
		}

		// 수정된 연락처 반영
		if(userDto.getPhone() != null && userDto.getPhone().length() > 0) {
			userInfo.setPhone(userDto.getPhone());
		}

		// 수정된 토큰 반영
		if(userDto.getToken() != null && userDto.getToken().length() > 0) {
			userInfo.setToken(userDto.getToken());
		}

		List<FileInfoDto> fileInfo = userDto.getProfileImgInfo();
//		// 수정된 이미지 반영
//		if(userModifyDto.getProfileImg() != null) {
////			userInfo.setProfileImg(map.get("profileImg"));
//		}
		if (fileInfo != null && !fileInfo.isEmpty()) {
			System.out.println("ServiceImpl:: 파일 저장");
			userMapper.registerFile(userDto);
		}

		return userMapper.modifyUser(userInfo);
	}

	@Override
	public int modifyUser(UserDto userDto) throws Exception {
		return userMapper.modifyUser(userDto);
	}

	@Override
	public int deleteUser(UserDto userDto) throws Exception {
		UserDto userInfo = null;
		if((userInfo = userMapper.getUser(userDto.getEmail())) == null) return 0;

		// 비밀번호 Encoding
		String salt = userInfo.getPasswordSalt();
		userDto.setPassword(hashing(userDto.getPassword().getBytes(), salt));

		return userMapper.deleteUser(userDto);
	}

	@Override
	public int insertAttractionBookmark(String email, int contentId) throws Exception {
		return userMapper.insertAttractionBookmark(email, contentId);
	}

	@Override
	public int deleteAttractionBookmark(String email, int contentId) throws Exception {
		return userMapper.deleteAttractionBookmark(email, contentId);
	}


	@Override
	public String hashing(byte[] password, String salt) throws Exception {

		// SHA-256 해시 함수를 사용
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		// key-stretching
		for (int i = 0; i < 5; i++) {
			// password와 salt를 합쳐 새로운 문자열 생성
			String temp = byteToString(password) + salt;
			// temp의 문자열을 해싱하여 md에 저장
			md.update(temp.getBytes());
			// md 객체의 다이제스트를 얻어 password를 갱신
			password = md.digest();
		}

		return byteToString(password);
	}

	@Override
	public String getSalt() throws Exception {
		SecureRandom rnd = new SecureRandom();
		byte[] temp = new byte[SALT_SIZE];
		rnd.nextBytes(temp);

		return byteToString(temp);
	}
	
	// 바이트 값을 16진수로 변경
	@Override
	public String byteToString(byte[] temp) {
		StringBuilder sb = new StringBuilder();
		for (byte a : temp) {
			sb.append(String.format("%02x", a));
		}
		return sb.toString();
	}

}
