package com.ruoyi.Paper.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.Paper.domain.AcademicPaper;
import com.ruoyi.Paper.service.IAcademicPaperService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学术论文信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/Paper/Paper")
public class AcademicPaperController extends BaseController
{
    @Autowired
    private IAcademicPaperService academicPaperService;

    /**
     * 查询学术论文信息列表
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcademicPaper academicPaper)
    {
        startPage();
        List<AcademicPaper> list = academicPaperService.selectAcademicPaperList(academicPaper);
        return getDataTable(list);
    }

    /**
     * 导出学术论文信息列表
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:export')")
    @Log(title = "学术论文信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AcademicPaper academicPaper)
    {
        List<AcademicPaper> list = academicPaperService.selectAcademicPaperList(academicPaper);
        ExcelUtil<AcademicPaper> util = new ExcelUtil<AcademicPaper>(AcademicPaper.class);
        util.exportExcel(response, list, "学术论文信息数据");
    }

    /**
     * 获取学术论文信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:query')")
    @GetMapping(value = "/{paperId}")
    public AjaxResult getInfo(@PathVariable("paperId") Long paperId)
    {
        return success(academicPaperService.selectAcademicPaperByPaperId(paperId));
    }

    /**
     * 新增学术论文信息
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:add')")
    @Log(title = "学术论文信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcademicPaper academicPaper)
    {
        return toAjax(academicPaperService.insertAcademicPaper(academicPaper));
    }

    /**
     * 修改学术论文信息
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:edit')")
    @Log(title = "学术论文信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcademicPaper academicPaper)
    {
        return toAjax(academicPaperService.updateAcademicPaper(academicPaper));
    }

    /**
     * 删除学术论文信息
     */
    @PreAuthorize("@ss.hasPermi('Paper:Paper:remove')")
    @Log(title = "学术论文信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{paperIds}")
    public AjaxResult remove(@PathVariable Long[] paperIds)
    {
        return toAjax(academicPaperService.deleteAcademicPaperByPaperIds(paperIds));
    }
}
