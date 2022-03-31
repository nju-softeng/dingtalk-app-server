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
public class UserProperty {
    @ColumnWidth(15)
    @ExcelProperty("学号")
    private String stuNum;
    @ExcelProperty("保管人")
    private String preserver;
    @ExcelProperty("物品名称")
    private String name;
    @ExcelProperty("物品型号")
    private String type;
    @ExcelProperty("保管开始时间")
    private String startTime;

}
