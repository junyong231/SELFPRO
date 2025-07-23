package com.selfpro.selfprodemo.todolist;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.selfpro.selfprodemo.member.Member;
import com.selfpro.selfprodemo.member.MemberRepository;
import com.selfpro.selfprodemo.member.MemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoService {

	private final TodoRepository todoRepository;
	private final HitogotoRepository hitogotoRepository;
	
	private final MemberService memberService;

	// 투두 DB 등록
	public void todoInsert(String memberId, String todo, LocalDate writeDate) {
		TodoList newTodo = new TodoList();
		Optional<Member> getOptionalMember = memberService.getMemberInfo(memberId);
		Member foundMember = getOptionalMember.get();
		newTodo.setMember(foundMember);
		newTodo.setContent(todo);
		newTodo.setWriteDate(writeDate);

		todoRepository.save(newTodo);
	}

	// 투두 페이지 입장 시 상태유지
	public List<TodoList> getTommorowTodos(String memberId, LocalDate today) {

		return todoRepository.findByMemberMemberLoginIDAndWriteDate(memberId, today);
	}

	// 수정 전 모두 지워버리기
	@Transactional
	public void todoDelete(String memberId, LocalDate writedate) {

		todoRepository.deleteByMemberMemberLoginIDAndWriteDate(memberId, writedate);

	}

	// 오늘의 목표 불러오기 (어제 작성)
	public List<TodoList> getTodayTodos(String memberId, LocalDate today) {
		LocalDate yesterday = today.minusDays(1);

		return todoRepository.findByMemberMemberLoginIDAndWriteDate(memberId, yesterday);

	}

	// 결산버튼 (UPDATE)
	@Transactional
	public int dayClosing(Boolean completeStatus, Long todoIdValue, LocalDate writeDate) {
		// 하나의 투두가 들어왔다.

		todoRepository.closingUpdate(completeStatus, todoIdValue); // id로 db에서 달성 여부 변경

		int dailyExp = 0;

		if (completeStatus) {
			dailyExp += 5;
		} else {
			dailyExp -= 5;
		}

		return dailyExp;

	}

	// 결산버튼 (CREATE) (히토코토)
	public void dayClosingHitogoto(String hitogo, String memberId, LocalDate writeDate) {
		Hitogoto newHitogoto = new Hitogoto();

		Optional<Member> getOptionalMember = memberService.getMemberInfo(memberId);
		Member foundMember = getOptionalMember.get();
		newHitogoto.setMember(foundMember);
		newHitogoto.setContent(hitogo);
		newHitogoto.setWriteDate(writeDate);

		hitogotoRepository.save(newHitogoto);

	}

	// 어제의 한마디
	public Optional<Hitogoto> hitogotoFromYesterday(String memberId, LocalDate today) {
		LocalDate yesterday = today.minusDays(1);

		return hitogotoRepository.findByMember_MemberLoginIDAndWriteDate(memberId, yesterday);

	}

	public void expCalculatingAndMemberClosingChange(boolean completed, String memberId, LocalDate writeDate) {
		// 한 개의 투두디티오 들어옴

	}
	


}
