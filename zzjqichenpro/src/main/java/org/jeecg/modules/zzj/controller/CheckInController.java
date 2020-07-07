package org.jeecg.modules.zzj.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.modules.zzj.common.ReturnCode;
import org.jeecg.modules.zzj.common.ReturnMessage;
import org.jeecg.modules.zzj.mapper.CheckInMapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.zzj.entity.CheckIn;
import org.jeecg.modules.zzj.service.ICheckInService;
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
 * @Description: 入住功能
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="入住功能")
@RestController
@RequestMapping("/zzj/checkIn")
public class CheckInController {
	@Autowired
	private ICheckInService checkInService;

	@Resource
	private CheckInMapper mapper;
	 /**
	  * 分页列表查询
	 * @param checkIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "入住功能-分页列表查询")
	@ApiOperation(value="入住功能-分页列表查询", notes="入住功能-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CheckIn>> queryPageList(CheckIn checkIn,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CheckIn>> result = new Result<IPage<CheckIn>>();
		QueryWrapper<CheckIn> queryWrapper = QueryGenerator.initQueryWrapper(checkIn, req.getParameterMap());
		Page<CheckIn> page = new Page<CheckIn>(pageNo, pageSize);
		IPage<CheckIn> pageList = checkInService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param checkIn
	 * @return
	 */
	@AutoLog(value = "入住功能-添加")
	@ApiOperation(value="入住功能-添加", notes="入住功能-添加")
	@PostMapping(value = "/add")
	public Result<CheckIn> add(@RequestBody CheckIn checkIn,HttpServletRequest req) {
		Result<CheckIn> result = new Result<CheckIn>();
		try {
			System.out.println(checkIn);
			checkInService.save(checkIn);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param checkIn
	 * @return
	 */
	@AutoLog(value = "入住功能-编辑")
	@ApiOperation(value="入住功能-编辑", notes="入住功能-编辑")
	@PutMapping(value = "/edit")
	public Result<CheckIn> edit(@RequestBody CheckIn checkIn) {
		Result<CheckIn> result = new Result<CheckIn>();
		CheckIn checkInEntity = checkInService.getById(checkIn.getId());
		if(checkInEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = checkInService.updateById(checkIn);
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
	@AutoLog(value = "入住功能-通过id删除")
	@ApiOperation(value="入住功能-通过id删除", notes="入住功能-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			checkInService.removeById(id);
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
	@AutoLog(value = "入住功能-批量删除")
	@ApiOperation(value="入住功能-批量删除", notes="入住功能-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<CheckIn> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CheckIn> result = new Result<CheckIn>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.checkInService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入住功能-通过id查询")
	@ApiOperation(value="入住功能-通过id查询", notes="入住功能-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CheckIn> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CheckIn> result = new Result<CheckIn>();
		CheckIn checkIn = checkInService.getById(id);
		if(checkIn==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(checkIn);
			result.setSuccess(true);
		}
		return result;
	}
	 /**
	  * 获取订单信息
	  * @param
	  * @return
	  */
	 @AutoLog(value = "获取订单-通过身份证信息或订单号或手机号查询订单")
	 @ApiOperation(value="获取订单-getOrder", notes="获取订单-getOrder")
	 @GetMapping(value = "/getOrders")
	 public Result<CheckIn> getOrders(CheckIn checkIn) throws Exception {
	 	Result<CheckIn> result = new Result<CheckIn>();
	 	Map<String,String> map=new HashMap<>();
		if (StringUtil.isNullOrEmpty(checkIn.getName()))
		{
			map.put("name",checkIn.getName());
		}
		 if (StringUtil.isNullOrEmpty(checkIn.getPhonenum()))
		 {
			 map.put("phone",checkIn.getPhonenum());
		 }
		 if (StringUtil.isNullOrEmpty(checkIn.getOrderid()))
		 {
			 map.put("orderid",checkIn.getOrderid());
		 }
		if (map.size()>0){
			/*//xml文件转对象
			File file = new File("");
			JAXBContext jaxbC = JAXBContext.newInstance(CheckIn.class);
			Unmarshaller us = jaxbC.createUnmarshaller();
			CheckIn in=(CheckIn)us.unmarshal(file);
			//xml 类型的String 字符串 转对象
			String xml=new String("");
			CheckIn inCheck= XmlUtil.XmlToBean(xml,CheckIn.class);*/
			//获取房间号
			String roomNum=getRoomNum();
			//赋值
			checkIn.setRoomnum(roomNum);
			//将订单信息加入到入住表里
			if(checkInService.save(checkIn)){
				result.setCode(ReturnCode.getSuccess);
				result.setMessage(ReturnMessage.success);
			}else{
				result.setMessage(ReturnMessage.erorDateBase);
				result.setCode(ReturnCode.erorDateBase);
			}
		}else{
			result.setMessage(ReturnMessage.lackParameter);
			result.setCode(ReturnCode.lackParameter);
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
      QueryWrapper<CheckIn> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CheckIn checkIn = JSON.parseObject(deString, CheckIn.class);
              queryWrapper = QueryGenerator.initQueryWrapper(checkIn, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CheckIn> pageList = checkInService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "入住功能列表");
      mv.addObject(NormalExcelConstants.CLASS, CheckIn.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入住功能列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CheckIn> listCheckIns = ExcelImportUtil.importExcel(file.getInputStream(), CheckIn.class, params);
              checkInService.saveBatch(listCheckIns);
              return Result.ok("文件导入成功！数据行数:" + listCheckIns.size());
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

	 /**
	  * 通过获取房间号
	  */
	 public String getRoomNum() {
		 /**
		  * 调用接口 随机 获取一个可用的房间号并返回
		  */
		 return null;
	 }
	 /**
	  * 入住预授权
	  * @param
	  * @return
	  */
	 @AutoLog(value = "入住预授权")
	 @ApiOperation(value="入住预授权", notes="入住预授权")
	 @GetMapping(value = "/preOccupancy")
	 public Result<CheckIn> preOccupancy() {
		 Result<CheckIn> result = new Result<CheckIn>();

		 return result;
	 }
}


//入住添加 同住人 判断
//判断该人在今天是否已经入住
/*//request.getParameterMap 将前端页面 发送的所有 参数 都以 key value的形式传递过来，
// 且转换为Map<String,String[]>类型的 且不能修改
QueryWrapper<CheckIn> queryWrapper =new QueryWrapper<>();
queryWrapper.eq("idnumber",checkIn.getIdnumber());
queryWrapper.eq("state","1");
Integer i=mapper.selectCount(queryWrapper);
if (i!=null&&i>0){
	result.setMessage("该客户已经入住"+ReturnMessage.notRepeat);
	result.setCode(ReturnCode.postSuccess);
}else {
	checkInService.save(checkIn);
	result.success("添加成功！");
}*/