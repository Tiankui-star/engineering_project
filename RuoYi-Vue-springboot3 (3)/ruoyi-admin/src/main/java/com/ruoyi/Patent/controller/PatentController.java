package com.ruoyi.Patent.controller;

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
import com.ruoyi.Patent.domain.Patent;
import com.ruoyi.Patent.service.IPatentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 专利信息Controller
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
@RestController
@RequestMapping("/Patent/Patent")
public class PatentController extends BaseController
{
    @Autowired
    private IPatentService patentService;

    /**
     * 查询专利信息列表
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:list')")
    @GetMapping("/list")
    public TableDataInfo list(Patent patent)
    {
        startPage();
        List<Patent> list = patentService.selectPatentList(patent);
        return getDataTable(list);
    }

    /**
     * 导出专利信息列表
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:export')")
    @Log(title = "专利信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Patent patent)
    {
        List<Patent> list = patentService.selectPatentList(patent);
        ExcelUtil<Patent> util = new ExcelUtil<Patent>(Patent.class);
        util.exportExcel(response, list, "专利信息数据");
    }

    /**
     * 获取专利信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:query')")
    @GetMapping(value = "/{patentId}")
    public AjaxResult getInfo(@PathVariable("patentId") Long patentId)
    {
        return success(patentService.selectPatentByPatentId(patentId));
    }

    /**
     * 新增专利信息
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:add')")
    @Log(title = "专利信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Patent patent)
    {
        return toAjax(patentService.insertPatent(patent));
    }

    /**
     * 修改专利信息
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:edit')")
    @Log(title = "专利信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Patent patent)
    {
        return toAjax(patentService.updatePatent(patent));
    }

    /**
     * 删除专利信息
     */
    @PreAuthorize("@ss.hasPermi('Patent:Patent:remove')")
    @Log(title = "专利信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{patentIds}")
    public AjaxResult remove(@PathVariable Long[] patentIds)
    {
        return toAjax(patentService.deletePatentByPatentIds(patentIds));
    }
}
