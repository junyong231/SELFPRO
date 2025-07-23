package com.selfpro.selfprodemo.member;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// Get - 로그인
	@GetMapping("/login")
	public String getLogIn() {

		//System.out.println("Get LogIn TEST");

		return "/SelfPro/login";
	}

	// Get - 회원가입
	@GetMapping("/signup")
	public String getSignUp(MemberCreateForm memberCreateForm) {

		//System.out.println("Get SignUp TEST");

		return "/SelfPro/signup";
	}

	// Post - 회원가입
	@PostMapping("/signup")
	public String postSignUp(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// 바인딩 리졸트: 유효성 검사 실패 사항 담아둠

		if (bindingResult.hasErrors()) {
			// 유효성 검사 실패 시 회원가입 페이지로 돌아간다.
			System.out.println("바인딩 리저트 에러");
			bindingResult.rejectValue("memberLoginId", "signUpErr", "会員登録ができませんでした");
			return "/SelfPro/signup";
		}

		// 비번확인 틀림
		if (!memberCreateForm.getMemberPassword1().equals(memberCreateForm.getMemberPassword2())) {
			bindingResult.rejectValue("memberPassword2", "passwordInCorrect", "パスワードが一致しません");
			return "/SelfPro/signup";
		}

		// 중복체크
		if (memberService.checkDuplicateId(memberCreateForm.getMemberLoginId())) {
			bindingResult.rejectValue("memberLoginId", "duplicateMemberLoginId", "このIDはすでに使用されています");
			return "/SelfPro/signup";
		}

		try {
			memberService.create(memberCreateForm.getMemberLoginId(), memberCreateForm.getMemberPassword1());

		} catch (DataIntegrityViolationException e) {
			// 예외 메시지나 코드 확인
			e.printStackTrace();


			return "/SelfPro/signup";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("joinFailed", e.getMessage());
			System.out.println("joinFailed");
			return "/SelfPro/signup";
		}

		redirectAttributes.addFlashAttribute("signupSuccess", true); // 성공 메세지 가져가기

		return "redirect:/member/login"; // 메인페이지로 간다...
	}

}
