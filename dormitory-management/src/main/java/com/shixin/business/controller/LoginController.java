package com.shixin.business.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shixin.business.domain.User;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.LoginServiceI;

/**
 * 登录控制器
 * 
 * @author Rice
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private IndexController indexController;

	@Autowired
	private LoginServiceI loginServiceI;

	@GetMapping("/toLogin")
	public String toLogin() {
		return "login/login";
	}

	@PostMapping("")
	public String login(User user, HttpSession session, Model model) {
		UserExpand info = loginServiceI.getUserLoginInfo(user);
		if (null != info) {
			session.setAttribute("LOGIN_USER", info);
			return "redirect:/index";
		}

		model.addAttribute("msg", "error");
		return "login/login";
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public Boolean logout(HttpSession session) {
		session.removeAttribute("LOGIN_USER");
		return true;
	}

}
