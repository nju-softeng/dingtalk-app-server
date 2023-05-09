package com.softeng.dingtalk.utils.stuNumUtils;

import com.softeng.dingtalk.enums.Position;

import java.time.LocalDate;

public class StuNum12bitsParser extends StuNumParser{
    @Override
    public Position parse(String stuNum) {
        int studentTypeNo = Integer.parseInt(stuNum.substring(0, 2));
        int admissionYear = Integer.parseInt(stuNum.substring(2, 6));
        Position position;
        switch (studentTypeNo) {
            case 50: case 51:
                position = Position.ACADEMIC;
                break;
            case 52: case 53:
                position = Position.PROFESSIONAL;
                break;
            case 60: case 61: case 62: case 63:
                position = Position.DOCTOR;
                break;
            case 65: case 66:
                LocalDate cur = LocalDate.now();
                int curYear = cur.getYear();
                //                直博生前三年视作学硕，之后视为博士生
                if(curYear - admissionYear <= 3) position = Position.ACADEMIC;
                else if(curYear - admissionYear > 3) position = Position.DOCTOR;
                else position = Position.OTHER;
                break;
            default:
                position = Position.OTHER;
        }
        return position;
    }
}
