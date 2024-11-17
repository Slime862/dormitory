package com.shixin.business.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.shixin.business.domain.vo.ExportScoreEntity;
import com.shixin.other.utils.ExportExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shixin.business.domain.Repair;
import com.shixin.business.domain.Stuinfo;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.StuServiceI;

@Controller
@RequestMapping("/stu")
public class StuController {

    @Autowired
    private StuServiceI stuServiceI;

    @GetMapping("/view")
    public String getDormView() {
        return "stu/stu";
    }

    @PostMapping("/exportInfo")
    public void exportInfo(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        List<Stuinfo> list = stuServiceI.findAll(user);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("学生信息", "学生信息表1"), Stuinfo.class, list);
        ExportExcelUtil.downLoadExcel(response, "学生信息表", workbook);
    }

    @GetMapping("/infoView")
    public String getInfoView(HttpSession session, Model model) throws Exception {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Stuinfo stuinfo = stuServiceI.findById(user.getUsername());
        model.addAttribute("stuinfo", new ObjectMapper().writeValueAsString(stuinfo));
        return "stu/infoView";
    }

    @GetMapping("/updatePwd")
    public String updatePwd(HttpSession session, Model model) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Stuinfo stuinfo = stuServiceI.findById(user.getUsername());
        model.addAttribute("id", stuinfo.getId());
        return "stu/updatePwd";
    }

    @PostMapping("/updatePhone")
    @ResponseBody
    public Boolean updatePhone(Stuinfo stuinfo) {
        return stuServiceI.updatePhone(stuinfo);
    }

    @PostMapping("/updatePwd")
    @ResponseBody
    public Boolean updatePwd(String id, String oldPwd, String newPwd) {
        return stuServiceI.updatePwd(id, oldPwd, newPwd);
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<Stuinfo> getStuInfo(@PageableDefault Pageable pageable, HttpSession session, String searchId, String searchDormName) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return stuServiceI.findStus(pageable, user, searchId, searchDormName);
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public Boolean importExcel(@RequestParam("file") MultipartFile file) {
        return stuServiceI.importExcel(file);
    }

    @PostMapping("/leaveSchool")
    @ResponseBody
    public Boolean leaveSchool(@RequestBody(required = true) List<String> ids) {
        return stuServiceI.leaveSchool(ids);
    }

    @GetMapping("/resetPwd")
    @ResponseBody
    public Boolean resetPwd(String stuId) {
        return stuServiceI.resetPwd(stuId);
    }

    @GetMapping("/del")
    @ResponseBody
    public Boolean del(String stuId) {
        return stuServiceI.del(stuId);
    }

    @PostMapping("/update")
    @ResponseBody
    public Boolean del(Stuinfo stuinfo) {
        return stuServiceI.update(stuinfo);
    }

    @GetMapping("/repairView")
    public String repairView() {
        return "stu/repairView";
    }

    @GetMapping("/repairInfo")
    @ResponseBody
    public Page<Repair> getRepairInfo(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return stuServiceI.findRepair(pageable, user.getStuinfo().getId());
    }

    @PostMapping("/createRepair")
    @ResponseBody
    public Boolean createRepair(Repair repair, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Stuinfo stuinfo = stuServiceI.findById(user.getUsername());
        return stuServiceI.createRepair(repair, stuinfo);
    }

    @GetMapping("/delRepairInfo")
    @ResponseBody
    public Boolean delRepairInfo(String repairId) {
        return stuServiceI.delRepairInfo(repairId);
    }


    @GetMapping("/repairHistoryView")
    public String repairHistoryView() {
        return "stu/repairHistory";
    }

    @GetMapping("/repairHistory")
    @ResponseBody
    public Page<Repair> getRepairHistory(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return stuServiceI.findRepairHistory(pageable, user.getStuinfo().getId());
    }


    @GetMapping("/getStus")
    @ResponseBody
    public List<Stuinfo> getStus(String dormid) {
        return stuServiceI.getStus(dormid);
    }
}
