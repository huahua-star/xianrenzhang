package org.jeecg.modules.zzj.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.TblTxnp;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.service.ITblTxnpService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="流水表")
@RestController
@RequestMapping("/zzj/tblTxnp")
public class TblTxnpController {
	@Autowired
	private ITblTxnpService tblTxnpService;

	@Autowired
	private IKaiLaiOrderService iKaiLaiOrderService;
	/**
	  * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "流水表-分页列表查询")
	@ApiOperation(value="流水表-分页列表查询", notes="流水表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TblTxnp>> queryPageList(String startTime,String endTime,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<TblTxnp>> result = new Result<IPage<TblTxnp>>();
		QueryWrapper<TblTxnp> queryWrapper =new QueryWrapper<TblTxnp>().between("create_time",startTime,endTime);
		Page<TblTxnp> page = new Page<TblTxnp>(pageNo, pageSize);
		IPage<TblTxnp> pageList = tblTxnpService.page(page, queryWrapper);
		for (TblTxnp tblTxnp : pageList.getRecords()){
			KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(tblTxnp.getPreOrderid());
			tblTxnp.setKaiLaiOrder(kaiLaiOrder);
		}
		result.setSuccess(true);
		result.setResult(pageList);
		result.setCode(200);
		return result;
	}
	
	/**
	  *   添加
	 * @param tblTxnp
	 * @return
	 */
	@AutoLog(value = "流水表-添加")
	@ApiOperation(value="流水表-添加", notes="流水表-添加")
	@PostMapping(value = "/add")
	public Result<TblTxnp> add(@RequestBody TblTxnp tblTxnp) {
		Result<TblTxnp> result = new Result<TblTxnp>();
		try {
			tblTxnpService.save(tblTxnp);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param tblTxnp
	 * @return
	 */
	@AutoLog(value = "流水表-编辑")
	@ApiOperation(value="流水表-编辑", notes="流水表-编辑")
	@PutMapping(value = "/edit")
	public Result<TblTxnp> edit(@RequestBody TblTxnp tblTxnp) {
		Result<TblTxnp> result = new Result<TblTxnp>();
		TblTxnp tblTxnpEntity = tblTxnpService.getById(tblTxnp.getId());
		if(tblTxnpEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = tblTxnpService.updateById(tblTxnp);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流水表-通过id删除")
	@ApiOperation(value="流水表-通过id删除", notes="流水表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			tblTxnpService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "流水表-批量删除")
	@ApiOperation(value="流水表-批量删除", notes="流水表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<TblTxnp> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<TblTxnp> result = new Result<TblTxnp>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.tblTxnpService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流水表-通过id查询")
	@ApiOperation(value="流水表-通过id查询", notes="流水表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TblTxnp> queryById(@RequestParam(name="id",required=true) String id) {
		Result<TblTxnp> result = new Result<TblTxnp>();
		TblTxnp tblTxnp = tblTxnpService.getById(id);
		if(tblTxnp==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(tblTxnp);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<TblTxnp> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              TblTxnp tblTxnp = JSON.parseObject(deString, TblTxnp.class);
              queryWrapper = QueryGenerator.initQueryWrapper(tblTxnp, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<TblTxnp> pageList = tblTxnpService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "流水表列表");
      mv.addObject(NormalExcelConstants.CLASS, TblTxnp.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("流水表列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<TblTxnp> listTblTxnps = ExcelImportUtil.importExcel(file.getInputStream(), TblTxnp.class, params);
              tblTxnpService.saveBatch(listTblTxnps);
              return Result.ok("文件导入成功！数据行数:" + listTblTxnps.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
