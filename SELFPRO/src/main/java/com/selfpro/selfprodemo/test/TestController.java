package com.selfpro.selfprodemo.test;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.selfpro.selfprodemo.scheduler.SchedulerDailyExp;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

	private final SchedulerDailyExp schedulerDailyExp;
	private final TestService testService;
	
	
	@GetMapping("/testpage")
	public String testpage(Principal principal) {
		
		if (principal != null) {
			return "/SelfPro/test";
		}
		else {
			//로그인 안하고 진입하려고 하면
			return "redirect:/custom-error/noLogin";
		}

	}
	
	@GetMapping("/forceMidnightLogicForTest")
	public String forceMidnight(RedirectAttributes redirectAttributes) {
		
		//12시가 아니어도 자동 실
		schedulerDailyExp.autoDailyClosing();
		
		redirectAttributes.addFlashAttribute("forcedMidnightDone", true);

		return "redirect:/test/testpage";
	}
	
	
	@GetMapping("/enableDailySettlementAgainForTest")
	public String mataNyuryokuDekiru(Principal principal, RedirectAttributes redirectAttributes) {

		String memberId = principal.getName();
		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		testService.MakeMemberDailyCheckFalse(memberId); //회원의 결산여부 false로 바꾸기
		testService.UpdateYesterDayTaskFalseForTest(memberId,yesterday); //체크 다 풀어주기
		
		redirectAttributes.addFlashAttribute("mataNyuryokuDekiru", true);
		
		return "redirect:/test/testpage";
	}
	
	@GetMapping("/forceTodayTodoCreateForTest")
	public String ForceTodoCreate(Principal principal, RedirectAttributes redirectAttributes) {

		String memberId = principal.getName();
		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		testService.ForceTodoCreate(memberId,yesterday);
		
		redirectAttributes.addFlashAttribute("ForceTodoCreate", true);
		
		return "redirect:/test/testpage";
	}
	
	@GetMapping("/forceHitokoto")
	public String ForceHitokotoCreate(Principal principal, RedirectAttributes redirectAttributes) {

		String memberId = principal.getName();
		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		testService.ForceHitokotoCreate(memberId,yesterday);
		
		redirectAttributes.addFlashAttribute("ForceHitokotoCreate", true);
		
		return "redirect:/test/testpage";
	}
	
	@GetMapping("/xp25plus")
	public String xp25plus(Principal principal, RedirectAttributes redirectAttributes) {

		String memberId = principal.getName();
		
		testService.ForceXp25Plus(memberId);
		
		redirectAttributes.addFlashAttribute("xp25plus", true);
		
		return "redirect:/test/testpage";
	}
}
