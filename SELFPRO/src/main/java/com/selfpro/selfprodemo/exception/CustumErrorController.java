package com.selfpro.selfprodemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/custom-error")
public class CustumErrorController {

	@GetMapping("/noLogin")
	public String noLoginErr(Model model) {

		model.addAttribute("alertMessage", "로그인이 필요합니다. 로그인 페이지로 이동합니다.");
		return "/SelfPro/login";

	}

	private static final String VIEW_PATH = "/Error/"; // ② 공통 경로 상수

	@RequestMapping // ③ 메서드 수준 @RequestMapping ← HTTP 메서드 무관 (get이든 post든..)
	public String handleError(HttpServletRequest request) {

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) { // 404
				return VIEW_PATH + "404";
			}
			if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) { // 500
				return VIEW_PATH + "500";
			}
		}

		// 그 밖의 예외(400 등)용 기본 페이지
		return VIEW_PATH + "error";
	}

}
