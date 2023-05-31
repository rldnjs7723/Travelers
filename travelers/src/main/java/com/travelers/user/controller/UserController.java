package com.travelers.user.controller;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.travelers.common.model.dto.FileInfoDto;
//import com.travelers.user.model.dto.UserModifyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.travelers.article.model.dto.ArticleDto;
import com.travelers.common.util.RestExceptionUtil;
import com.travelers.common.util.SecurityUtil;
import com.travelers.user.model.dto.TokenInfo;
import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
@Api
public class UserController {

    private final UserService userService;

    @Value("${file.imgPath}")
    private String uploadImgPath;

    @GetMapping("/idCheck/{id}")
    public ResponseEntity<?> idCheck(@PathVariable("id") String id) {
        try {
            int count = userService.idCheck(id);
            if (count > 0) {
                return RestExceptionUtil.messageHandling("중복된 아이디입니다.", HttpStatus.CONFLICT);
            } else {
                return RestExceptionUtil.messageHandling("사용 가능한 별명입니다.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("아이디 체크 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/emailCheck/{email}")
    public ResponseEntity<?> emailCheck(@PathVariable("email") String email) {
        try {
            int count = userService.emailCheck(email);
            if (count > 0) {
                return RestExceptionUtil.messageHandling("중복된 이메일입니다.", HttpStatus.CONFLICT);
            } else {
                return RestExceptionUtil.messageHandling("사용 가능한 이메일입니다.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("아이디 체크 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/phoneCheck/{phone}")
    public ResponseEntity<?> phoneCheck(@PathVariable("phone") String phone) {
        try {
            int count = userService.phoneCheck(phone);
            if (count > 0) {
                return RestExceptionUtil.messageHandling("중복된 연락처입니다.", HttpStatus.CONFLICT);
            } else {
                return RestExceptionUtil.messageHandling("사용 가능한 연락처입니다.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("연락처 체크 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{phone}")
    public ResponseEntity<?> findEmail(@PathVariable("phone") String phone) {
        try {
            UserDto userDto = userService.getUserPhone(phone);
            if (userDto == null) {
                return RestExceptionUtil.messageHandling("해당 연락처로 가입된 계정을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return RestExceptionUtil.messageHandling(userDto.getEmail(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("계정 체크 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/{email}/{phone}")
    public ResponseEntity<?> findPassword(@PathVariable("email") String email, @PathVariable("phone") String phone) {
        try {
            UserDto userDto = userService.getUser(email);
            if(userDto == null) {
                return RestExceptionUtil.messageHandling("해당 이메일로 가입된 계정을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(!userDto.getPhone().equals(phone)) {
                return RestExceptionUtil.messageHandling("연락처가 다릅니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 새로운 비밀번호 발급
            String password = userService.getNewPassword(email);
            return RestExceptionUtil.messageHandling(password, HttpStatus.OK);
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("연락처 체크 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> map, Model model, HttpServletResponse response) {
        log.debug("login map: {}", map);
        String email = map.get("email");
        String password = map.get("password");

        try {
            TokenInfo tokenInfo = userService.loginUser(email, password);
            if (tokenInfo != null) {
                // 토큰 정보 전달
                return new ResponseEntity<TokenInfo>(tokenInfo, HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("아이디 또는 비밀번호가 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("아이디 또는 비밀번호가 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody UserDto userInfo, HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");
        userInfo.setToken(refreshToken);
        log.debug("refresh user: {}", userInfo);
        try {
            TokenInfo tokenInfo = userService.refreshUser(userInfo);
            if (tokenInfo != null) {
                // 토큰 정보 전달
                return new ResponseEntity<TokenInfo>(tokenInfo, HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout/{email}")
    public ResponseEntity<?> logout(@PathVariable("email") String email, HttpServletResponse response) {
        try {
            // Refresh Token 제거
            UserDto userInfo = userService.getUser(email);
            userInfo.setToken(null);
            userService.modifyUser(userInfo);

            return RestExceptionUtil.messageHandling("로그아웃 되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return RestExceptionUtil.messageHandling("로그아웃 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> userInfo(@PathVariable("email") String email) {
        try {
            UserDto userInfo = userService.getUser(email);
            return new ResponseEntity<UserDto>(userInfo, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("유저 정보를 가져오는 도중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        log.debug("register info: {}", userDto);

        try {
            if (userService.joinUser(userDto) > 0) {
                return RestExceptionUtil.messageHandling("회원 가입 완료", HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("회원 가입 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("회원 가입 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDto userDto) {
        log.debug("delete user: {}", userDto);
        try {
            if (userService.deleteUser(userDto) > 0) {
                return RestExceptionUtil.messageHandling("회원 탈퇴 완료", HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("회원 탈퇴 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("회원 탈퇴 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //	@PutMapping
//	public ResponseEntity<?> modify(@RequestBody Map<String, String> map) {
//		try {
//			if(userService.modifyUser(map) > 0) {
//				return RestExceptionUtil.messageHandling("회원정보 수정 완료", HttpStatus.OK);
//			} else {
//				return RestExceptionUtil.messageHandling("회원정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RestExceptionUtil.messageHandling("회원정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
    @PutMapping
    public ResponseEntity<?> modify(UserDto userDto, @RequestPart(value="profileImg", required = false) MultipartFile profileImg) {
        log.debug("modify user userDto: {}", userDto);
        log.debug("modify user profileImg: {}", profileImg);
        try {
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadImgPath + File.separator + today;
            log.debug("저장 폴더 : {}", saveFolder);
            File folder = new File(saveFolder);
            if (!folder.exists())
                folder.mkdirs();
//            FileInfoDto fileInfo = new FileInfoDto();
            if (profileImg != null) {
                FileInfoDto fileInfoDto = new FileInfoDto();
                String originalFileName = profileImg.getOriginalFilename();
                if (!originalFileName.isEmpty()) {
                    String saveFileName = UUID.randomUUID().toString()
                            + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    fileInfoDto.setSaveFolder(today);
                    fileInfoDto.setOriginalFile(originalFileName);
                    fileInfoDto.setSaveFile(saveFileName);
                    log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", profileImg.getOriginalFilename(), saveFileName);
                    profileImg.transferTo(new File(folder, saveFileName));
                }
                List<FileInfoDto> fileInfoDtoList = new ArrayList<FileInfoDto>();
                fileInfoDtoList.add(fileInfoDto);
                userDto.setProfileImgInfo(fileInfoDtoList);
            }

            if (userService.modifyUserImg(userDto) > 0) {
                return RestExceptionUtil.messageHandling("회원정보 수정 완료", HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("회원정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("회원정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/attraction/{contentId}")
    public ResponseEntity<?> insertAttractionBookmark(@PathVariable("contentId") int contentId) {
        try {
            String email = SecurityUtil.getCurrentMemberId();
            if (userService.insertAttractionBookmark(email, contentId) > 0) {
                return RestExceptionUtil.messageHandling("북마크 추가 완료", HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("북마크 정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("북마크 정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/attraction/{contentId}")
    public ResponseEntity<?> deleteAttractionBookmark(@PathVariable("contentId") int contentId) {
        try {
            String email = SecurityUtil.getCurrentMemberId();
            if (userService.deleteAttractionBookmark(email, contentId) > 0) {
                return RestExceptionUtil.messageHandling("북마크 추가 완료", HttpStatus.OK);
            } else {
                return RestExceptionUtil.messageHandling("북마크 정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestExceptionUtil.messageHandling("북마크 정보 수정 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
