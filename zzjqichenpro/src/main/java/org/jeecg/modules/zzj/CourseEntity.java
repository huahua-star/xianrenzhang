package org.jeecg.modules.zzj;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelVerify;

import java.io.Serializable;

public class CourseEntity implements Serializable {
    /** 主键 */
    private String id;
    /** 课程名称 */
    @Excel(name = "课程名称", orderNum = "1", needMerge = true)
    private String name;
    /** 老师主键
    @ExcelEntity(id = "yuwen")
    @ExcelVerify()
    private TeacherEntity teacher;
    *//** 老师主键 *//*
    @ExcelEntity(id = "shuxue")
    private TeacherEntity shuxueteacher;

    @ExcelCollection(name = "选课学生", orderNum = "4")
    private List<StudentEntity> students;*/

}

