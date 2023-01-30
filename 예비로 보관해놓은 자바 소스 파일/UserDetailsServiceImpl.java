package com.muglang.muglangspace.service.mglguser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.repository.MglgUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	MglgUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(userRepository.findById(Integer.parseInt(username)).isEmpty()) {
			return null;
		} else {
			MglgUser user = userRepository.findById(Integer.parseInt(username)).get();
			
			return CustomUserDetails.builder()
									.user(user)
									.build();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
