package com.softeng.dingtalk.utils.stuNumUtils;

import com.softeng.dingtalk.exception.CustomExceptionEnum;

public class StuNumParserFactory {
    public static StuNumParser generateParser(int stuNumLength){
        if(stuNumLength == 10) {
            return new StuNum10bitsParser();
        } else if(stuNumLength == 12) {
            return new StuNum12bitsParser();
        }
        CustomExceptionEnum.WRONG_FORMAT_OF_STUDENT_NUMBER.throwDirectly();
        return null;
    }
}
