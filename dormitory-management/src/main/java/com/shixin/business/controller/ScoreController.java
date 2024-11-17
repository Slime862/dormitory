package com.shixin.business.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.shixin.business.domain.*;
import com.shixin.business.domain.vo.ExportScoreEntity;
import com.shixin.business.domain.vo.ExportScoreHistoryEntity;
import com.shixin.business.service.ScoreServiceI;
import com.shixin.business.service.StuServiceI;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreServiceI scoreServiceI;

    @Autowired
    private StuServiceI stuServiceI;

    @GetMapping("/view")
    public String getScoreView() {
        return "score/score";
    }

    @GetMapping("/historyView")
    public String getHistoryView() {
        return "score/historyView";
    }

    @PostMapping("/createScore")
    public void createScore(String name, HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        try {
            List<ExportScoreEntity> exportScoreEntityList = scoreServiceI.createScore(user.getStaffinfo(), name);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("评分登记表", "评分登记表1"), ExportScoreEntity.class, exportScoreEntityList);
            ExportExcelUtil.setSumFormula(workbook, 'F', 'D', 'E');
            ExportExcelUtil.downLoadExcel(response, "评分登记表", workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/isCreateScore")
    @ResponseBody
    public Boolean isCreateScore(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return scoreServiceI.isCreateScore(user.getStaffinfo());
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public Boolean importExcel(@RequestParam("file") MultipartFile file) {
        try {
            scoreServiceI.importExcel(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<ScoreExtend> getScoreList(@PageableDefault Pageable pageable, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return scoreServiceI.getScoreList(pageable, user.getStaffinfo());
    }

    @PostMapping("/updateScore")
    @ResponseBody
    public Boolean updateScore(Score score) {
        try {
            if (null == score.getSanitary()) {
                score.setSum(score.getTidy());
            }
            if (null == score.getTidy()) {
                score.setSum(score.getSanitary());
            }
            if (null != score.getSanitary() && null != score.getTidy()) {
                score.setSum(score.getSanitary() + score.getTidy());
            }
            scoreServiceI.updateScore(score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @GetMapping("/push")
    @ResponseBody
    public Boolean push(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        try {
            scoreServiceI.push(user.getStaffinfo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/getScoreNames")
    @ResponseBody
    public List<Scoringbatch> getScoreNames(HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return scoreServiceI.getScoreNames(user);
    }

    @GetMapping("/historyList")
    @ResponseBody
    public Page<ScoreExtend> historyList(@PageableDefault Pageable pageable, HttpSession session, String scoringBatchId) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        return scoreServiceI.historyList(pageable, user, scoringBatchId);
    }

    @GetMapping("/exportHistoryExcel")
    public void exportExcel(HttpSession session, HttpServletResponse response) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Staffinfo staffinfo = user.getStaffinfo();
        List<ExportScoreHistoryEntity> list = scoreServiceI.findHistory(staffinfo);
        if (list.size() > 0) {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("历史评分表", "历史评分表1"), ExportScoreHistoryEntity.class, list);
            ExportExcelUtil.downLoadExcel(response, "历史评分表", workbook);
        }
    }

    @GetMapping("/stuScoreView")
    public String getScoreView(Model model, HttpSession session) {
        UserExpand user = (UserExpand) session.getAttribute("LOGIN_USER");
        Stuinfo stuinfo = stuServiceI.findById(user.getUsername());
        model.addAttribute("dormId", stuinfo.getDormid());
        return "score/stuScoreView";
    }
}
