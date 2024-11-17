package com.shixin.business.controller;

import com.shixin.business.domain.UserExpand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Dorm;
import com.shixin.business.service.DormServiceI;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/dorm")
public class DormController {

	@Autowired
	private DormServiceI dormServiceI;

	@GetMapping("/view")
	public String getDormView() {
		return "dorm/dorm";
	}

	@GetMapping("/list")
	@ResponseBody
	public Page<Dorm> getDormInfo(@PageableDefault Pageable pageable) {
		return dormServiceI.findDorms(pageable);
	}

	@GetMapping("/getDorms")
	@ResponseBody
	public List<Dorm> getDorms(HttpSession session) {
		UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
		return dormServiceI.getDorms(user.getStaffinfo());
	}
	
	@PostMapping("/importExcel")
	@ResponseBody
	public Boolean importExcel(@RequestParam("file") MultipartFile file) {
		return dormServiceI.importExcel(file);
	}
}
