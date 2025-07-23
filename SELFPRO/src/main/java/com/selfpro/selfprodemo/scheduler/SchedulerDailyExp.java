package com.selfpro.selfprodemo.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.selfpro.selfprodemo.member.Member;
import com.selfpro.selfprodemo.member.MemberRepository;
import com.selfpro.selfprodemo.member.MemberService;
import com.selfpro.selfprodemo.todolist.TodoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SchedulerDailyExp {

	private final MemberService memberService;

	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정 0시
	@Transactional
	public void autoDailyClosing() {

		LocalDate yesterday = LocalDate.now().minusDays(1);

		List<Member> members = memberRepository.findByTodayClosingDone(false);

		for (Member member : members) {
			// System.out.println("지금 결산 기록 False인 멤버 :: " + member.getMemberLoginID());

			// 해당 아이디의 어제 과제 수 == 실패한 과제수 이므로
			int expForMinus = (todoRepository.countByMemberMemberLoginIDAndWriteDate(member.getMemberLoginID(), yesterday))
					* -5;
			// System.out.println( "EXP FOR MINUS @@ == " + expForMinus );
			memberService.calculateExp(member.getMemberLoginID(), expForMinus); // for문 안에서 한놈씩 해당 이엑스피 반영
		} // 멤버 한명씩 for

	}// 메서드

}
