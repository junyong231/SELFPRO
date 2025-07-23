package com.selfpro.selfprodemo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.selfpro.selfprodemo.member.Member;
import com.selfpro.selfprodemo.member.MemberService;
import com.selfpro.selfprodemo.todolist.Hitogoto;
import com.selfpro.selfprodemo.todolist.TodoList;
import com.selfpro.selfprodemo.todolist.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final MemberService memberService;
	private final TodoService todoService;

	@GetMapping(value = { "/", "/index" })
	public String index(Model model, Principal principal) {

		System.out.println("index");
		boolean isLoggedIn = (principal != null);
		LocalDate today = LocalDate.now();
		//LocalDate yesterday = LocalDate.now().minusDays(1);

		// 로그인 여부 보내기
		model.addAttribute("isLoggedIn", isLoggedIn);

		// 로그인 시 회원정보 뿌리기
		if (principal != null) {// 로그인 성공 시
			String memberId = principal.getName(); // 로그인한 사용자 이름
			model.addAttribute("username", memberId);
			System.out.println("username ::: " + memberId);

			Optional<Member> getMember = memberService.getMemberInfo(memberId);
			Member memberInfo = getMember.get();
			System.out.println("!!" + memberInfo.getMemberLevel()); // test!

			model.addAttribute("member", memberInfo);

			// 로그인 시 과제 정보 뿌리기
			List<TodoList> todos = todoService.getTodayTodos(memberId, today);

			// TEST, LOG
//			for (TodoList todoList : todos) {
//				System.out.println("어제 과제 받아오기 : " + todoList.getContent());
//			}

			model.addAttribute("todos", todos);

			// 로그인 시 어제의 한마디 뿌리기
			Optional<Hitogoto> hitogoto = todoService.hitogotoFromYesterday(memberId, today);

			
			
			// 히토고토 뿌리기
			if (hitogoto.isPresent()) { // 있으면
				Hitogoto hitogotoContent = hitogoto.get();
				String hitogoFromYesterDay = hitogotoContent.getContent();

				System.out.println("히토고토:: " + hitogoFromYesterDay);
				model.addAttribute("hitogoto", hitogoFromYesterDay);
			}

		}

		return "/SelfPro/main";
	}
}