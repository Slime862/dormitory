package com.shixin.business.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.shixin.business.domain.InRegister;
import com.shixin.business.domain.OutRegister;
import com.shixin.business.domain.Registerbatch;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.domain.vo.ExportInRegisterEntity;
import com.shixin.business.domain.vo.ExportOutRegisterEntity;
import com.shixin.business.domain.vo.InRegisterExtend;
import com.shixin.business.domain.vo.OutRegisterExtend;
import com.shixin.business.service.RegisterServiceI;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterServiceI registerServiceI;

    @GetMapping("/inRegisterView")
    public String getInRegisterView() {
        return "register/inRegisterView";
    }

    @GetMapping("/isCreateRegister")
    @ResponseBody
    public Boolean isCreateRegister() {
        return registerServiceI.isCreateRegister();
    }

    @PostMapping("/createRegister")
    @ResponseBody
    public Boolean createRegister(String name, HttpSession session) {
        try {
            UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
            registerServiceI.createRegister(name, user.getStaffinfo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/getRegisterNames")
    @ResponseBody
    public List<Registerbatch> getRegisterNames(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.getRegisterNames(user.getStaffinfo().getId(), "in");
    }

    @GetMapping("/inRegisterList")
    @ResponseBody
    public Page<InRegisterExtend> inRegisterList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.inRegisterList(pageable, user.getStaffinfo());
    }

    @GetMapping("/createInHistory")
    @ResponseBody
    public Boolean createInHistory(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.createInHistory(user.getStaffinfo());
    }

    @GetMapping("/inHistoryList")
    @ResponseBody
    public Page<InRegisterExtend> historyList(@PageableDefault Pageable pageable, HttpSession session, String registerBatchId) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.historyList(pageable, user.getStaffinfo(), registerBatchId);
    }

    @GetMapping("/inExportExcel")
    public void inExportExcel(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        List<ExportInRegisterEntity> list = registerServiceI.findInHistoryList(user.getStaffinfo());
        if (list.size() > 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("入校登记表", "入校登记表1"), ExportInRegisterEntity.class, list);
            ExportExcelUtil.downLoadExcel(response, "入校登记表", workbook);
        }
    }

    @GetMapping("/stuInRegisterView")
    public String getInRegisterView(Model model, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        InRegister inRegister = registerServiceI.getInRegisterId(user.getUsername());
        String inRegisterId = null;
        if (inRegister != null) {
            inRegisterId = inRegister.getId();
        }
        model.addAttribute("inRegisterId", inRegisterId);
        return "register/stuInRegisterView";
    }

    @PostMapping("/saveStuRegister")
    @ResponseBody
    public Boolean saveStuRegister(Date arrivetime, String inRegisterId) {
        try {
            registerServiceI.saveStuRegister(arrivetime, inRegisterId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/isStuSaveRegister")
    @ResponseBody
    public Boolean isStuSaveRegister(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.isStuSaveRegister(user.getUsername());
    }

    @GetMapping("/stuHistoryList")
    @ResponseBody
    public Page<InRegisterExtend> stuHistoryList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.stuHistoryList(pageable, user.getUsername());
    }


    @GetMapping("/outRegisterView")
    public String getOutRegisterView() {
        return "register/outRegisterView";
    }

    @GetMapping("/isCreateOutRegister")
    @ResponseBody
    public Boolean isCreateOutRegister() {
        return registerServiceI.isCreateOutRegister();
    }

    @PostMapping("/createOutRegister")
    @ResponseBody
    public Boolean createOutRegister(String name, HttpSession session) {
        try {
            UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
            registerServiceI.createOutRegister(name, user.getStaffinfo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/getOutRegisterNames")
    @ResponseBody
    public List<Registerbatch> getOutRegisterNames(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.getRegisterNames(user.getStaffinfo().getId(), "out");
    }

    @GetMapping("/outHistoryList")
    @ResponseBody
    public Page<OutRegisterExtend> outHistoryList(@PageableDefault Pageable pageable, HttpSession session, String registerBatchId) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.outHistoryList(pageable, user.getStaffinfo(), registerBatchId);
    }

    @GetMapping("/outRegisterList")
    @ResponseBody
    public Page<OutRegisterExtend> outRegisterList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.outRegisterList(pageable, user.getStaffinfo());
    }

    @GetMapping("/createOutHistory")
    @ResponseBody
    public Boolean createOutHistory(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.createOutHistory(user.getStaffinfo());
    }

    @GetMapping("/outExportExcel")
    public void outExportExcel(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        List<ExportOutRegisterEntity> list = registerServiceI.findOutHistoryList(user.getStaffinfo());
        if (list.size() > 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("离校登记表", "离校登记表1"), ExportOutRegisterEntity.class, list);
            ExportExcelUtil.downLoadExcel(response, "离校登记表", workbook);
        }
    }

    @GetMapping("/stuOutRegisterView")
    public String getOutRegisterView(Model model, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        OutRegister outRegister = registerServiceI.getOutRegisterId(user.getUsername());
        String outRegisterId = null;
        if (outRegister != null) {
            outRegisterId = outRegister.getId();
        }
        model.addAttribute("outRegisterId", outRegisterId);
        return "register/stuOutRegisterView";
    }

    @GetMapping("/isStuSaveOutRegister")
    @ResponseBody
    public Boolean isStuSaveOutRegister(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.isStuSaveOutRegister(user.getUsername());
    }

    @PostMapping("/saveStuOutRegister")
    @ResponseBody
    public Boolean saveStuOutRegister(Date leavetime, String outRegisterId, String city) {
        try {
            registerServiceI.saveStuOutRegister(leavetime, outRegisterId, city);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/stuOutHistoryList")
    @ResponseBody
    public Page<OutRegisterExtend> stuOutHistoryList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return registerServiceI.stuOutHistoryList(pageable, user.getUsername());
    }

    @GetMapping("/inRegisterHistoryView")
    public String getInRegisterHistoryView() {
        return "register/inRegisterHistoryView";
    }

    @GetMapping("/outRegisterHistoryView")
    public String getOutRegisterHistoryView() {
        return "register/outRegisterHistoryView";
    }
}
