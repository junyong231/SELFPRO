package com.selfpro.selfprodemo.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

		// read()
		// Optional<SiteUser> _siteUser =
		// this.userRepository.findByusername(userLoginId);
		Optional<Member> _member = this.memberRepository.findByMemberLoginID(userLoginId);

		if (_member.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}
		Member member = _member.get();

		// 최소 하나의 권한은 필요함. 실서비스에서 권한 안 써도 "ROLE_USER" 정도는 넣어야 함
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new User(member.getMemberLoginID(), member.getMemberLoginPW(), authorities);
	}

}
