package com.shixin.business.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.shixin.business.domain.vo.ExportScoreEntity;
import com.shixin.business.service.RepairServiceI;
import com.shixin.other.utils.ExportExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shixin.business.domain.Repair;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.StaffServiceI;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffServiceI staffServiceI;

    @Autowired
    private RepairServiceI repairServiceI;

    @GetMapping("/view")
    public String getStaffView() {
        return "staff/staff";
    }

    @GetMapping("/infoView")
    public String getInfoView(HttpSession session, Model model) throws Exception {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Staffinfo staffinfo = staffServiceI.findById(user.getUsername());
        model.addAttribute("staffinfo", new ObjectMapper().writeValueAsString(staffinfo));
        return "staff/infoView";
    }

    @GetMapping("/stu")
    public String getStuView() {
        return "staff/stu";
    }

    @GetMapping("/updatePwd")
    public String updatePwd(HttpSession session, Model model) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Staffinfo staffinfo = staffServiceI.findById(user.getUsername());
        model.addAttribute("id", staffinfo.getId());
        return "staff/updatePwd";
    }

    @PostMapping("/updatePhone")
    @ResponseBody
    public Boolean updatePhone(Staffinfo staffinfo) {
        return staffServiceI.updatePhone(staffinfo);
    }

    @PostMapping("/updatePwd")
    @ResponseBody
    public Boolean updatePwd(String id, String oldPwd, String newPwd) {
        return staffServiceI.updatePwd(id, oldPwd, newPwd);
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<Staffinfo> getStuInfo(@PageableDefault Pageable pageable, String searchName, String searchDorm) {
        return staffServiceI.findStaffs(pageable, searchName, searchDorm);
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public Boolean importExcel(@RequestParam("file") MultipartFile file) {
        return staffServiceI.importExcel(file);
    }

    @GetMapping("/resetPwd")
    @ResponseBody
    public Boolean resetPwd(String staffId) {
        return staffServiceI.resetPwd(staffId);
    }

    @GetMapping("/del")
    @ResponseBody
    public Boolean del(String staffId) {
        return staffServiceI.del(staffId);
    }

    @PostMapping("/update")
    @ResponseBody
    public Boolean update(Staffinfo staffinfo) {
        return staffServiceI.update(staffinfo);
    }

    @GetMapping("/repairUntreatedView")
    public String getRepairView() {
        return "staff/repairUntreatedView";
    }

    @GetMapping("/repairUntreatedInfo")
    @ResponseBody
    public Page<Repair> getRepairUntreatedInfo(@PageableDefault Pageable pageable, HttpSession session, String reason) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return staffServiceI.findRepairUntreated(pageable, user.getStaffinfo(), reason);
    }

    @PostMapping("/batchDeal")
    @ResponseBody
    public Boolean batchDeal(@RequestBody List<String> ids) {
        return staffServiceI.batchDeal(ids);
    }

    @GetMapping("/dealRepairUntreated")
    @ResponseBody
    public Boolean dealRepairUntreated(String repairId) {
        return staffServiceI.dealRepairUntreated(repairId);
    }

    @GetMapping("/repairProcessView")
    public String getRepairProcessView() {
        return "staff/repairProcessView";
    }

    @GetMapping("/repairProcessInfo")
    @ResponseBody
    public Page<Repair> getRepairProcessInfo(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return staffServiceI.findRepairProcessInfo(pageable, user.getStaffinfo());
    }

    @GetMapping("/exportExcel")
    public void exportExcel(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Staffinfo staffinfo = user.getStaffinfo();
        String s = staffinfo.getDormname() + staffinfo.getDormno();
        List<Repair> list = repairServiceI.findRepairLikeDormNameEqualStatus(s, 2);
        if (list.size() > 0 ){
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("历史保修单", "历史保修单表1"), Repair.class, list);
            ExportExcelUtil.downLoadExcel(response, "历史保修单", workbook);
        }
    }

    @GetMapping("/dealRepairProcess")
    @ResponseBody
    public Boolean dealRepairProcess(String repairId) {
        return staffServiceI.dealRepairProcess(repairId);
    }

    @GetMapping("/repairHistoryView")
    public String getRepairHistoryView() {
        return "staff/repairHistory";
    }

    @GetMapping("/repairHistory")
    @ResponseBody
    public Page<Repair> getRepairHistory(@PageableDefault Pageable pageable, HttpSession session, String reason) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return staffServiceI.findRepairHistory(pageable, reason, user.getStaffinfo());
    }
}
