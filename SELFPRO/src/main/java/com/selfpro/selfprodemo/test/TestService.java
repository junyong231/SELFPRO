package com.selfpro.selfprodemo.test;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selfpro.selfprodemo.member.Member;
import com.selfpro.selfprodemo.member.MemberRepository;
import com.selfpro.selfprodemo.member.MemberService;
import com.selfpro.selfprodemo.todolist.Hitogoto;
import com.selfpro.selfprodemo.todolist.HitogotoRepository;
import com.selfpro.selfprodemo.todolist.TodoList;
import com.selfpro.selfprodemo.todolist.TodoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;
	private final HitogotoRepository hitogotoRepository;
	private final MemberService memberService;
	
	@Transactional
	public void MakeMemberDailyCheckFalse(String memberId) {
		memberRepository.updateTodayClosigDoneFalseForTest(memberId);
	}
	
	@Transactional
	public void ForceTodoCreate(String memberId, LocalDate yesterday) {
		TodoList newTodo = new TodoList();
		Optional<Member> getOptionalMember = memberService.getMemberInfo(memberId);
		Member foundMember = getOptionalMember.get();
		newTodo.setMember(foundMember);
		newTodo.setContent("Task for Test");
		newTodo.setWriteDate(yesterday);

		todoRepository.save(newTodo);
		
	}
	
	@Transactional
	public void UpdateYesterDayTaskFalseForTest(String memberId, LocalDate yesterday) {
		
		todoRepository.updateYesterdayTaskFalse(memberId,yesterday);
		
	}
	
	@Transactional
	public void ForceHitokotoCreate(String memberId, LocalDate yesterday) {
		
		Hitogoto newHitogoto = new Hitogoto();

		Optional<Member> getOptionalMember = memberService.getMemberInfo(memberId);
		Member foundMember = getOptionalMember.get();
		newHitogoto.setMember(foundMember);
		newHitogoto.setContent("ひとこと for TEST");
		newHitogoto.setWriteDate(yesterday);

		hitogotoRepository.deleteByWriteDate(yesterday);
		hitogotoRepository.save(newHitogoto);
		
		
	}
	
	@Transactional
	public void ForceXp25Plus(String memberId) {
		Optional<Member> getOptionalMember = memberService.getMemberInfo(memberId);
		Member foundMember = getOptionalMember.get();
		
		memberRepository.updateDailyExpPlus25(memberId);
	}
	
	
}
