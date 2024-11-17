package com.shixin.business.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shixin.business.domain.UserExpand;

@Controller
@RequestMapping("/index")
public class IndexController {

	// localhost:8080/index/error

	@GetMapping("")
	public String index(HttpSession session, Model model) {
		UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
		model.addAttribute("user", user);
		return "index/index";
	}

	@GetMapping("/error")
	public String error() {
		// /WEB-INF/view/index//index.jsp
		return "index/index";
	}
}
