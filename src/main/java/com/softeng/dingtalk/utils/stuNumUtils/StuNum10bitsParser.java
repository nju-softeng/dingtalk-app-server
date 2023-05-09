package com.softeng.dingtalk.utils.stuNumUtils;

import com.softeng.dingtalk.enums.Position;

import java.time.LocalDate;

public class StuNum10bitsParser extends StuNumParser{
    @Override
    public Position parse(String stuNum) {
        String studentTypeNo = stuNum.substring(0, 2);
        int admissionYear = 2000 + Integer.parseInt(stuNum.substring(2, 4));
        Position position;
        switch (studentTypeNo) {
            case "MG":
                position = Position.ACADEMIC;
                break;
            case "MF":
                position = Position.PROFESSIONAL;
                break;
            case "DG":
                position = Position.DOCTOR;
                break;
            case "DZ":
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
