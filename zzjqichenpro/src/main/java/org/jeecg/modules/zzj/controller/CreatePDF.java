package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.entity.Checkupaccounts;
import org.jeecg.modules.zzj.entity.Klbill;
import org.jeecg.modules.zzj.entity.Relatedpdf;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.jeecg.modules.zzj.util.R;
import org.jeecg.modules.zzj.util.SendemailsUtils;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Api(tags = "PDF")
@RestController
@RequestMapping("/zzj/CreatePDF")
public class CreatePDF {

    @Autowired
    private IklrecordService iklrecordService;

    /**
     * 生成PDF
     */
//    @ApiOperation(value = "生成账单PDF")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public R exports(@RequestParam(name = "roomkey", required = true) String roomkey,
                     @RequestParam(name = "resrowld", required = true) String resrowld) {
//        1、获取模板
        FileInputStream inputStream = null;
        try {
            // String path =session.getServletContext().getRealPath("");
            inputStream = new FileInputStream("D:\\img\\dill6.jasper");
        } catch (FileNotFoundException e) {
            return R.error("FileNotFoundException");
        }
//       2、为了填充变量
        String uu = UUID.randomUUID().toString();
        File file = new File("D:\\" + uu + ".pdf");
        Map parameter = new HashMap();
        List<Relatedpdf> list = iklrecordService.querypdf1(roomkey);
        List<Klbill> lists = iklrecordService.queryklbill(roomkey);
        List<Checkupaccounts> arrlist = iklrecordService.querythrow(resrowld);
        System.out.println(list + ":list");
        System.out.println(lists + ":lists");
        System.out.println(arrlist + ":arrlist");
        if (lists == null || lists.size() == 0 || list == null || list.size() == 0 ||
                arrlist == null || arrlist.size() == 0) {
            return R.error("参数为空无法生成PDF!");
        }
        String name = list.get(0).getName();//客人姓名
        String reach = list.get(0).getReach();//到店日期
        String out = list.get(0).getOut();//离店日期
        BigDecimal roomrate = new BigDecimal("0");//房费
        BigDecimal catering = new BigDecimal("0");//餐饮费
        BigDecimal qita = new BigDecimal("0");//其他
        BigDecimal sum = new BigDecimal("0");//总计
        BigDecimal roomratevat = new BigDecimal("0");//房费VAT
        BigDecimal cateringvat = new BigDecimal("0");//餐饮VAT
        BigDecimal incidentalsvat = new BigDecimal("0");//杂费VAT
        int payment = 0;//付款金额

        for (int i = 0; i < lists.size(); i++) {
            String trndescription = lists.get(i).getTrndescription();
            String transactioncode = lists.get(i).getTransactioncode();
            if (transactioncode.equals("9000")) {
                int cash = Integer.parseInt(lists.get(i).getPrice());//现金
                if (cash < 0) {
                    payment = cash + payment;
                }
            }
            if (transactioncode.equals("9130")) {
                int WeChatPay = Integer.parseInt(lists.get(i).getPrice());//微信支付
                if (WeChatPay < 0) {
                    payment = WeChatPay + payment;
                }
            }
            if (transactioncode.equals("9140")) {
                int Alipay = Integer.parseInt(lists.get(i).getPrice());//支付宝
                if (Alipay < 0) {
                    payment = Alipay + payment;
                }
            }
            if (transactioncode.equals("9150")) {
                int transfer = Integer.parseInt(lists.get(i).getPrice());//银行汇款
                if (transfer < 0) {
                    payment = transfer + payment;
                }
            }
            if (transactioncode.equals("9200")) {
                int poscash = Integer.parseInt(lists.get(i).getPrice());//POS-现金
                if (poscash < 0) {
                    payment = poscash + payment;
                }
            }
            if (transactioncode.equals("9300")) {
                int Foreigncard = Integer.parseInt(lists.get(i).getPrice());//POS-国外卡
                if (Foreigncard < 0) {
                    payment = Foreigncard + payment;
                }
            }
            if (transactioncode.equals("9320")) {
                int Domesticcard = Integer.parseInt(lists.get(i).getPrice());//POS-国内卡
                if (Domesticcard < 0) {
                    payment = Domesticcard + payment;
                }
            }

            int of = trndescription.indexOf("房费");
            if (of != -1) {
                roomrate = new BigDecimal(lists.get(i).getPrice());
                int ofs = trndescription.indexOf("VAT");
                if (ofs != -1) {
                    roomratevat = new BigDecimal(lists.get(i).getPrice());
                }
            }
            int of1 = trndescription.indexOf("餐");
            if (of1 != -1) {
                catering = new BigDecimal(lists.get(i).getPrice());
                int of1s = trndescription.indexOf("VAT");
                if (of1s != -1) {
                    cateringvat = new BigDecimal(lists.get(i).getPrice());
                }
            }
            sum.add(new BigDecimal(lists.get(i).getPrice()));
        }
        qita = sum.subtract(roomrate).subtract(catering);
        incidentalsvat = qita.subtract(roomratevat).subtract(cateringvat);

        String nametype = arrlist.get(0).getNametype();
        if (StringUtils.isEmpty(nametype)) {
            return R.error("参数为空");
        }
        if (nametype.equals("Individual")) {
            Integer blockid = arrlist.get(0).getBlockid();
            if (blockid.equals(0)) {
                for (int i = 0; i < arrlist.size(); i++) {
                    parameter.put("clienttype", "旅游散客");//公司名称
                }
            } else if (blockid > 0) {
                for (int i = 0; i < arrlist.size(); i++) {
                    parameter.put("clienttype", "旅游团队");//公司名称
                }
            }
        }
        if (nametype.equals("Agent")) {
            for (int i = 0; i < arrlist.size(); i++) {
                parameter.put("clienttype", "旅行社");//公司名称
            }
        }
        if (nametype.equals("Company")) {
            for (int i = 0; i < arrlist.size(); i++) {
                parameter.put("clienttype", "协议公司");//公司名称
            }
        }

        parameter.put("dyname", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());//打印时间
        parameter.put("username", name);//客户姓名
        parameter.put("chkin", reach);//到店日期
        parameter.put("chkout", out);//离店日期
        parameter.put("roomkey", roomkey);//房间号
        parameter.put("sum", sum.toString());//消费总计
        parameter.put("roomrate", roomrate.toString());//房费
        parameter.put("rest", qita.toString());//杂费
        parameter.put("catering", catering.toString());//餐饮费
        parameter.put("roomratevat", roomratevat.toString());//房费VAT
        parameter.put("cateringvat", cateringvat.toString());//餐饮VAT
        parameter.put("incidentalsvat", incidentalsvat.toString());//杂费VAT
        parameter.put("ordernumber", resrowld);//订单号
        parameter.put("payment", String.valueOf(payment));//付款
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameter, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(file));
        } catch (Exception e) {
            return R.error("写入异常!");
        }
        String files = file.toString();
        if (files == null) {
            return R.error("参数为空无法生成PDF");
        }
        return R.ok(files);
    }


    /**
     * @param response
     * @param path     PDF路径
     * @return
     */
//    @ApiOperation(value = "获取pdf")
    @RequestMapping(value = "/pdfs", method = RequestMethod.GET)
    public R pdfs(HttpServletResponse response, String path) {
        try {
            //  本地的pdf文件
            SendemailsUtils.outputPdf(response, path);
            return R.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return R.error("请求类型错误!");
        }
    }

    /**
     * @param paths 图片路径
     * @param response
     */
    @ApiOperation(value="获取图片")
    @RequestMapping(value = "/images",method = RequestMethod.GET)
    public void images(String paths,HttpServletResponse response) {
        response.setContentType("image/*");
        try {
            FileCopyUtils.copy(new FileInputStream(paths), response.getOutputStream());
        } catch (Exception e) {
            log.error("getQrImage()方法出现异常:获取图片失败", e.getMessage());
        }
    }


    /**
     * http://47.99.146.180:8067/Self_help_inter/CreateReport/Self_help_GetReportpathBillPrint?resId=(预定号)&name=(生成账单的pdf名字，建议用32位guid码，避免重复)
     * 发送pdf至邮箱
     * username 用户姓名
     * email  用户邮箱
     */
    @ApiOperation(value = "pdf发送至邮箱")
    @RequestMapping(value = "/sendemail", method = RequestMethod.GET)
    public R sendemail(@RequestParam(name = "username", required = true) String username,
                       @RequestParam(name = "email", required = true) String email) {
        String subject = "宾客,您的消费明细:";//邮件主题
        String msg = "消费明细帐单发送至附件,请查收!";//邮件内容

        String usernames = SendemailsUtils.getPinYin(username);
        String format = new SimpleDateFormat("ddHHmmss").format(new Date());
        String path = (usernames + format) + ".pdf";
        try {
            String urlTypeName = java.net.URLEncoder.encode(username, "utf-8");
            SendemailsUtils.downLoadFromUrl("http://47.99.146.180:8067/Report/" + urlTypeName + ".pdf", path, "D:\\");
            String filename = "D:\\" + path;//附件地址
            Thread.sleep(1000);
            SendemailsUtils.sendMail(email, subject, msg, filename);
        } catch (Exception e) {
            return R.error("发送失败!");
        }
        return R.ok();
    }


    /**
     * 消费明细pdf转图片
     * username: 客户姓名
     */
    @ApiOperation(value = "生成客户消费明细图片")
    @RequestMapping(value = "/pdfToImage", method = RequestMethod.GET)
    public R pdfToImage(@RequestParam(name = "username", required = true) String username) {
        String path = UuidUtils.getUUID()+".pdf";
        String paths = "D:\\"+UuidUtils.getUUID()+".png";
        try {
            String urlTypeName = java.net.URLEncoder.encode(username, "utf-8");
            SendemailsUtils.downLoadFromUrl("http://47.99.146.180:8067/Report/" + urlTypeName + ".pdf", path, "D:\\");
            SendemailsUtils.pdf2multiImage("D:\\" + path, paths);
        } catch (IOException e) {
            log.error("pdfToImage()方法异常:转换图片失败");
            return R.error();
        }
        return R.ok(paths);
    }
}








