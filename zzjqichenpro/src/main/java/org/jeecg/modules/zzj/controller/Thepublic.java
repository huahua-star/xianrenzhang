package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.util.Base64Img;
import org.jeecg.modules.zzj.util.Publicsecurity.JaxbUtil;
import org.jeecg.modules.zzj.util.Publicsecurity.clientele;
import org.jeecg.modules.zzj.util.R;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Api(tags = "公安旅店录入信息接口")
@RestController
@RequestMapping("/zzj/Thepublic")
public class Thepublic {

    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;

    /**
     * @param imgurl       照片
     * @param cardType     证件类型
     * @param idEntityCard 证件号码(身份证号码)
     * @return
     */
    @ApiOperation(value = "公安旅店录入信息接口", httpMethod = "GET")
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public R information(String imgurl, String cardType, String idEntityCard) {

        String path = "D:\\img\\";//本地xml文件的路径
        String pathname;
        String fullpath;
        File newFile = null;
        String uuid = UuidUtils.getUUID();
        pathname = uuid + ".tmp";
        fullpath = path + pathname;
        File filename = new File(fullpath);
        try {
            if (!filename.exists()) {
                filename.createNewFile();
            }
        } catch (IOException e) {
            return R.error("创建文件失败");
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        clientele user = new clientele();
        List<KaiLaiOrder> list = iKaiLaiOrderService.searchResvByIdcard(idEntityCard);
        if (list == null || list.size() == 0) {
            return R.error("信息为空无法录入");
        }
        if (cardType.equals("ID")) {
            user.setCARD_TYPE("11");//身份证
        }
        if (cardType.equals("PSP")) {
            user.setCARD_TYPE("93");//中华人民共和国护照
        }
        if (cardType.equals("HKP")) {
            user.setCARD_TYPE("20");//中华人民共和国往来港澳通行证
        }
        if (cardType.equals("TWP")) {
            user.setCARD_TYPE("台湾居民往来大陆许可证");
        }
        if (cardType.equals("FRP")) {
            user.setCARD_TYPE("外宾居留证");
        }
        if (cardType.equals("DP")) {
            user.setCARD_TYPE("外交护照");
        }
        if (cardType.equals("FTP")) {
            user.setCARD_TYPE("外宾旅游证");
        }
        if (cardType.equals("SP")) {
            user.setCARD_TYPE("公务护照");
        }
        if (cardType.equals("MO")) {
            user.setCARD_TYPE("90");//中国人民解放军军官证
        }
        if (cardType.equals("MS")) {
            user.setCARD_TYPE("92");//中国人民解放军士兵证
        }
        if (cardType.equals("OC")) {
            user.setCARD_TYPE("91");//中国人民武装警察部队警官证
        }
        if (cardType.equals("PTP")) {
            user.setCARD_TYPE("证照办理证明");
        }

        String checkintime = null;
        String checkouttime = null;
        String birthdaytime = null;

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list.get(0).getActualCheckInDate());
            checkintime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list.get(0).getActualCheckOutDate());
            checkouttime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list.get(0).getBorthday());
            birthdaytime = new SimpleDateFormat("yyyy/MM/dd").format(date2);
        } catch (ParseException e) {
            return R.error("转换日期失败");
        }

        user.setENTER_TIME(checkintime);//入住时间
        user.setEXIT_TIME(checkouttime);//离店时间
        user.setBIRTHDAY(birthdaytime);//出生日期
        user.setFLAG("CHINESE");
        user.setCREATE_TIME(formatter2.format(new Date()));
        user.setID(UuidUtils.getUUID());
        user.setOPERATETYPE("0");//对该旅客的操作类型 只有入住
        user.setROOM_NO(list.get(0).getRoomNo());//房间号
        user.setNAME(list.get(0).getAltName());//入住人姓名
        user.setSEX(list.get(0).getGender() + 1 + "");//性别
        user.setNATION(list.get(0).getNation()); //民族
        user.setCARD_NO(idEntityCard);//入住人身份证号码
        user.setREASON("22");//来京理由 默认22
        user.setOCCUPATION("22");//职业 默认22
        user.setADDRDETAIL(list.get(0).getAddress());//地址
        user.setCREDITCARD_NO("");//旅客信用卡号 可以为空
        user.setCREDITCARD_TYPE("");//旅客信用卡类型 可以为空
        user.setPHOTO(Base64Img.GetImageStr(imgurl));

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fullpath));
            String dataXml = JaxbUtil.convertToXml(user);
            System.out.println("dataXml!"+dataXml);
            out.write(dataXml); // \r\n即为换行
            out.flush();
            out.close(); // 最后记得关闭文件
            File parentDir = new File("\\\\192.168.11.129\\data");  //部署时需要修改IP地址
            File targetpath = new File(parentDir, uuid + ".tmp");
            SAXReader reader = new SAXReader();
            Document read = reader.read(fullpath);
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(targetpath);
            //设置输出格式
            OutputFormat format = OutputFormat.createCompactFormat();
            format.setEncoding("utf-8");
            XMLWriter writers = new XMLWriter(outStream, format);
            writers.write(read);
            outStream.close();//释放流
        } catch (Exception e) {
            return R.error("文件传输失败");
        }
        return R.ok();
    }
}
