package org.jeecg.modules.zzj;


import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextController {

    public static void main(String[] args) throws Exception {
        //String encoding = "GBK";
        String excelPath="D://tt.xls";
        File excel = new File(excelPath);
        if (excel.isFile() && excel.exists()) {   //判断文件是否存在
            String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
            Workbook wb;
            //根据文件后缀（xls/xlsx）进行判断
            if ( "xls".equals(split[1])){
                FileInputStream fis = new FileInputStream(excel);   //文件流对象
                wb = new HSSFWorkbook(fis);
            }else if ("xlsx".equals(split[1])){
                wb = new XSSFWorkbook(excel);
            }else {
                System.out.println("文件类型错误!");
                return;
            }
            List<KaiLaiOrder> list=new ArrayList<>();
            //开始解析
            Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

            int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
            int lastRowIndex = sheet.getLastRowNum();
            System.out.println("firstRowIndex: "+firstRowIndex);
            System.out.println("lastRowIndex: "+lastRowIndex);

            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                System.out.println("rIndex: " + rIndex);
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    KaiLaiOrder kaiLaiOrder=new KaiLaiOrder();
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                        String lieConclum=sheet.getRow(0).getCell(cIndex).toString();
                        System.out.println("列名:"+lieConclum);
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
                            switch (lieConclum){
                                case "预定入住时间":
                                    kaiLaiOrder.setActualCheckInDate(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                                    break;
                                case "预定离店时间":
                                    kaiLaiOrder.setActualCheckOutDate(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                                    break;
                                case "房型代码":
                                    kaiLaiOrder.setRoomType(cell.toString());
                                    break;
                                case "手机号":
                                    kaiLaiOrder.setPhone(cell.toString());
                                    break;
                                case "订单来源":
                                    kaiLaiOrder.setChanelCode(cell.toString());
                                    break;
                                case "团队订单号":
                                    kaiLaiOrder.setBlockId(cell.toString());
                                    break;
                            }
                        }
                    }
                    list.add(kaiLaiOrder);
                }
            }
            for (KaiLaiOrder kaiLaiOrder : list){
                System.out.println(kaiLaiOrder);
            }
        } else {
            System.out.println("找不到指定的文件");
        }
    }

}
