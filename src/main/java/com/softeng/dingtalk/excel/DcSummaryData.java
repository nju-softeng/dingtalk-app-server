package com.softeng.dingtalk.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.softeng.dingtalk.po.DcSummaryPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER,
        fillForegroundColor = 42, bottomBorderColor = 42, leftBorderColor = 42, rightBorderColor = 42)
@HeadFontStyle(fontHeightInPoints = 11)
public class DcSummaryData {
    @ColumnWidth(15)
    @ExcelProperty("学号")
    private String stuNum;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("助研金")
    private double salary;
    @ExcelProperty("第一周DC")
    private double week1;
    @ExcelProperty("第二周DC")
    private double week2;
    @ExcelProperty("第三周DC")
    private double week3;
    @ExcelProperty("第四周DC")
    private double week4;
    @ExcelProperty("第五周DC")
    private double week5;
    @ExcelProperty("总DC")
    private double total;
    @ExcelProperty("当前AC")
    private double ac;
    @ExcelProperty("Top-Up")
    private double topUp;

    public DcSummaryData(String stuNum, String name, DcSummaryPo dcSummaryPo) {
        this.stuNum = stuNum;
        this.name = name;
        this.salary = dcSummaryPo.getSalary();
        this.week1 = dcSummaryPo.getWeek1();
        this.week2 = dcSummaryPo.getWeek2();
        this.week3 = dcSummaryPo.getWeek3();
        this.week4 = dcSummaryPo.getWeek4();
        this.week5 = dcSummaryPo.getWeek5();
        this.total = dcSummaryPo.getTotal();
        this.ac = dcSummaryPo.getAc();
        this.topUp = dcSummaryPo.getTopup();
    }

}
