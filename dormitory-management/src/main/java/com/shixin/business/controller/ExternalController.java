package com.shixin.business.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.shixin.business.domain.External;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.domain.vo.ExportInRegisterEntity;
import com.shixin.business.service.ExternalServiceI;
import com.shixin.other.utils.ExportExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/external")
public class ExternalController {

    @Autowired
    private ExternalServiceI externalServiceI;

    @GetMapping("/externalView")
    public String getExternalView() {
        return "external/externalView";
    }

    @PostMapping("/saveExternal")
    @ResponseBody
    public Boolean saveExternal(External external, HttpSession session) {
        try {
            UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
            externalServiceI.saveExternal(external, user.getStaffinfo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/externalList")
    @ResponseBody
    public Page<External> externalList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return externalServiceI.externalList(pageable, user.getStaffinfo());
    }

    @GetMapping("/overVisit")
    @ResponseBody
    public Boolean overVisit(String id) {
        try {
            externalServiceI.overVisit(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/historyView")
    public String getHistoryView() {
        return "external/historyView";
    }

    @GetMapping("/externalHistoryList")
    @ResponseBody
    public Page<External> externalHistoryList(@PageableDefault Pageable pageable, HttpSession session, String stuname, String startTime, String endTime) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return externalServiceI.externalHistoryList(pageable, user.getStaffinfo(), stuname, startTime, endTime);
    }

    @GetMapping("/exportHistoryExcel")
    public void exportHistoryExcel(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        List<External> list = externalServiceI.findHistoryList(user.getStaffinfo());
        if (list.size() > 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("历史来访登记表", "历史来访登记表1"), External.class, list);
            ExportExcelUtil.downLoadExcel(response, "历史来访登记表", workbook);
        }
    }
}
