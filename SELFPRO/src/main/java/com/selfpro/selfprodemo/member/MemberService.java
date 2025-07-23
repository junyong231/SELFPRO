package com.selfpro.selfprodemo.member;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	public Member create(String memberLoginID, String memberLoginPW) {
		// 회원가입 로직

		Member member = new Member();
		member.setMemberLoginID(memberLoginID);
		member.setMemberLoginPW(memberLoginPW);

		// 비밀번호 암호화
		member.setMemberLoginPW(passwordEncoder.encode(memberLoginPW));

		System.out.println("가입 성공");
		return this.memberRepository.save(member);

	}

	// 로그인 시 회원정보 뿌리기 위해서 회원 찾아 레벨 업뎃
	@Transactional
	public Optional<Member> getMemberInfo(String memberLoginID) {

		// username을 통해 회원 정보 조회
		Optional<Member> memberOptional = memberRepository.findByMemberLoginID(memberLoginID);

		// 회원 정보가 존재하면 레벨 계산
		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			memberRepository.updateMemberLevel(memberLoginID, member.getDailyEXP());
		}

		return memberRepository.findByMemberLoginID(memberLoginID);
	}

	// 아이디 중복 체크
	public boolean checkDuplicateId(String memberLoginId) {
		return memberRepository.existsByMemberLoginID(memberLoginId);
	}

	// 결산 시 경험치 계산
	@Transactional
	public void calculateExp(String memberId, int dailyGainExp) { // 멤버 아이디와 오늘의 총 결산 exp가 왔다

		int presentExp = memberRepository.findDailyExpByMemberId(memberId);

	    System.out.println("✅ memberId: " + memberId);
	    System.out.println("📊 presentExp: " + presentExp);
	    System.out.println("📥 dailyGainExp: " + dailyGainExp);
	    System.out.println("🔍 합산 결과: " + (presentExp + dailyGainExp));
		//경험치 음수 방지
		if (presentExp + dailyGainExp < 0) {  //dailyExp가 음수일 수 있으니 + 연산
			System.out.println("📛 음수 방지: exp 0으로 리셋");
			memberRepository.updateDailyExpToZero(memberId);
		} else {
			memberRepository.updateDailyExp(memberId, dailyGainExp);
		}
		getMemberInfo(memberId); //톱페이지에서 레벨 계산
		
		// 오늘의 결산 여부 True로 바꾸기
		memberRepository.updateTodayClosingDone(memberId);

	}



}
