package com.softeng.dingtalk.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER)
@HeadStyle(verticalAlignment = VerticalAlignment.CENTER, horizontalAlignment = HorizontalAlignment.CENTER,
        fillForegroundColor = 42, bottomBorderColor = 42, leftBorderColor = 42, rightBorderColor = 42)
@HeadFontStyle(fontHeightInPoints = 11)
public class AcData {
    @ColumnWidth(15)
    @ExcelProperty("学号")
    private String num;
    @ExcelProperty("姓名")
    private String name;
    @ColumnWidth(5)
    @ExcelProperty("AC 变化")
    private Double ac;
    @ColumnWidth(150)
    @ExcelProperty("AC 变化原因")
    @ContentStyle(horizontalAlignment = HorizontalAlignment.LEFT)
    @HeadStyle(horizontalAlignment = HorizontalAlignment.LEFT,
            fillForegroundColor = 42, bottomBorderColor = 42, leftBorderColor = 42, rightBorderColor = 42)
    private String reason;
}
