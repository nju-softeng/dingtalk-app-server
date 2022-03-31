package com.softeng.dingtalk.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER,
        fillForegroundColor = 42, bottomBorderColor = 42, leftBorderColor = 42, rightBorderColor = 42)
@HeadFontStyle(fontHeightInPoints = 11)
public class UserPrize {
    @ColumnWidth(15)
    @ExcelProperty("学号")
    private String stuNum;
    @ExcelProperty("姓名")
    private String stuName;
    @ExcelProperty("获将时间")
    private String prizeTime;
    @ExcelProperty("奖项名称")
    private String prizeName;
    @ExcelProperty("奖项级别")
    private String level;
    @ExcelProperty("备注")
    private String remark;
}
