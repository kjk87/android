package com.pplus.prnumberbiz.core.util;

import com.pple.pplus.utils.part.utils.StringUtils;

/**
 * Created by j2n on 2016. 9. 28..
 */

public class PplusNumberUtil{

    public static String getPrNumberFormat(String number){

        if(StringUtils.isEmpty(number)) {
            return "";
        }

        number = getOnlyNumber(number);

        return number + "#";
    }

    public static String getOnlyNumber(String number){

        if(StringUtils.isEmpty(number)) {
            return "";
        }
        /**
         * 하이픈 제거
         * 국가 코드 제거
         * 현 국내용으로만 적용
         * */
        number = number.replace("+82", "0").replace("-", "");

        /**
         * 예외 번호 추가함 - 서울 번호는 예외 케이스#
         * */
        String regex = "^0(?:2)(?:\\d{2}|\\d{3,4})\\d{4}$";

        switch (number.length()) {
            case 0:
                return "";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return number;
            case 6:
                return number.replaceAll("(\\d{3})(\\d{3})", "$1-$2");
            case 7:
                return number.replaceAll("(\\d{3})(\\d{4})", "$1-$2");
            case 8:
                return number.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
            case 9:
                if(number.matches(regex)) {
                    return number.replaceAll("(\\d{2})(\\d{3})(\\d{3,4})", "$1-$2-$3");
                } else {
                    return number.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3");
                }
            case 10:
            case 11:
                if(number.matches(regex)) {
                    return number.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                } else {
                    return number.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
            case 12:
                return number.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{3})", "$1-$2-$3-$4");
            case 13:
            case 14:
            case 15:
                return number.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{3})(\\d{1,3})", "$1-$2-$3-$4-$5");

            default:
        }
        return number;
    }

}
