package com.selfpro.selfprodemo.todolist;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.selfpro.selfprodemo.member.Member;
import com.selfpro.selfprodemo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;
	private final MemberService memberService;

	// 내일 과제 관리 GET
	@GetMapping("/settingpage")
	public String getSettingPage(Principal principal, Model model) {
		System.out.println("투두리스트 접속");
		
		try {
			LocalDate today = LocalDate.now();
			String memberId = principal.getName();

			// '내일' 과제 찾아오기
			List<TodoList> todos = todoService.getTommorowTodos(memberId, today);

			// 'todos'가 null일 경우 빈 리스트로 초기화
			if (todos == null) {
				todos = new ArrayList<>();
			}

			// TEST, LOG
			for (TodoList todoList : todos) {
				System.out.println("내일 과제 받아오기 : " + todoList.getContent());
			}

			// 'todos' 리스트를 모델에 담아 뷰로 전송
			model.addAttribute("todos", todos);

			return "/SelfPro/todo";
			
		} catch (Exception e) {
			//로그인 안하고 진입하려고 하면
			return "redirect:/custom-error/noLogin";
			
		}

	}

	// GPT
	@GetMapping("/checkpage")
	public String getCheckPage(Principal principal, Model model) {
		
		try {
			System.out.println("채점 페이지 접속");
			LocalDate today = LocalDate.now();
			String memberId = principal.getName();

			// 결산 여부 가져오기 위한 멤버 정보 가져오기
			Optional<Member> memberOp = memberService.getMemberInfo(memberId);
			Member member = memberOp.get();
			model.addAttribute("member", member);

			// 어제 등록한 과제들 가져오기
			List<TodoList> todos = todoService.getTodayTodos(memberId, today);

			// 전통적인 for문으로 DTO 변환
			List<TodoCheckDto> dtoList = new ArrayList<>();
			for (TodoList t : todos) {
				TodoCheckDto dto = new TodoCheckDto();
				dto.setTodoId(t.getTodoId());
				dto.setCompleted(t.isCompleted());
				dto.setContent(t.getContent()); // 라벨에 쓸 텍스트
				dtoList.add(dto);
			}

			// 폼 오브젝트에 담기
			TodoForm form = new TodoForm();
			form.setTodos(dtoList);
			model.addAttribute("todoForm", form);

			return "/SelfPro/dailycheck";
		} catch (Exception e) {
			//로그인 안하고 진입하려고 하면
			return "redirect:/custom-error/noLogin";
		}

		
	}

	// 내일의 목표가 빈 리스트인 경우 (저장 버튼 떠있을때)
	@PostMapping("/creategoal")
	public String dailyGoalSetting(@RequestParam(name = "todos", required = false) List<String> todos,
			RedirectAttributes redirectAttributes, Principal principal) {

		// 유저 아이디 확인
		String memberId = principal.getName();
		//System.out.println("이름 가져오는지 TEST : " + memberId);

		// 작성일
		LocalDate writeDate = LocalDate.now();
		// 작성일 출력 (현재 시간)
		//System.out.println("작성일 TEST : " + writeDate);

		if (todos != null) {
			for (String todo : todos) {
				System.out.println("todo리스트 들어왔는지 TEST : " + todo);

				todoService.todoInsert(memberId, todo, writeDate);

			}
		} else {
			System.out.println("EMPTY TODOS");
			redirectAttributes.addFlashAttribute("todoListLessThan1", true); // 실패 메세지 가져가기
			return "redirect:/todo/settingpage";
		}
		redirectAttributes.addFlashAttribute("todoListSuccess", true);

		return "redirect:/todo/settingpage";
	}

	// 내일의 과제 설정에서 수정버튼 떠있을때
	@PostMapping("/updategoal")
	public String dailyGoalUpdate(@RequestParam(name = "todos", required = false) List<String> todos,
			RedirectAttributes redirectAttributes, Principal principal) {

		// 유저 아이디 확인
		String memberId = principal.getName();
		System.out.println("이름 가져오는지 TEST : " + memberId);

		// 작성일
		LocalDate writeDate = LocalDate.now();
		// 작성일 출력 (현재 시간)
		System.out.println("작성일 TEST : " + writeDate);

		if (todos != null) {// 목록에 뭐가 들어있다면

			// 일단 삭제후
			todoService.todoDelete(memberId, writeDate);
			for (String todo : todos) {
				System.out.println("todo리스트 들어왔는지 TEST : " + todo);

				// 다시 저장하기
				todoService.todoInsert(memberId, todo, writeDate);
			}

		} else if (todos == null) {
			todoService.todoDelete(memberId, writeDate);
			// 이번에 빈칸 되면 삭제만 !
		}

		redirectAttributes.addFlashAttribute("todoListSuccess", true);

		return "redirect:/todo/settingpage";
	}

	// 데일리체크 post (결산 클릭)
	@PostMapping("/closing")
	public String todayClosing(// GPT 제공코드
			@ModelAttribute TodoForm form, Principal principal) {

		String memberId = principal.getName();
		LocalDate writeDate = LocalDate.now();

		// 오늘치 경험치 정산
		int dailyExp = 0;

		// 과제 달성 여부 체크 (하나씩)
		for (TodoCheckDto dto : form.getTodos()) {
			dailyExp += todoService.dayClosing(dto.isCompleted(), dto.getTodoId(), writeDate);
		}
		//System.out.println("@###########" + dailyExp);

		memberService.calculateExp(memberId, dailyExp); // 경험치 정산
		todoService.dayClosingHitogoto(form.getHitogoto(), memberId, writeDate);// 한마디 저장

		return "redirect:/todo/checkpage";
	}

}
