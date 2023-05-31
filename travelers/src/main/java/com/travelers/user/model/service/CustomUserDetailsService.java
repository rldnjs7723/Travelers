package com.travelers.user.model.service;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelers.user.model.dto.UserDto;
import com.travelers.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserDto userDto;
		try {
			userDto = userMapper.getUser(username);
			
			if(userDto == null) throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
	    	
	    	UserDetails userInfo = User.builder()
	    			.username(userDto.getEmail())
	    			.password(passwordEncoder.encode(userDto.getPassword()))
	    			.roles(userDto.getGrade())
	    			.build();
	        return userInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
		}
    }
}