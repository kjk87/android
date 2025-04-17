package com.pplus.utils.part.format;

import android.util.Patterns;
import android.webkit.URLUtil;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

/**
 * Created by 안명훈 on 16. 6. 29..
 * <p>
 * <pre>
 *      포멧 유틸 클래스
 * </pre>
 * <p>
 * <pre>
 * <b>History:</b>
 * 안명훈, 1.0, 2016.6.29 초기작성
 * 안명훈, 1.1, 2016.7.4
 * - 전화번호 포멧 추가
 * - 문자열을 숫자만 변환 포멧 추가
 * - 금액 포멧 추가
 *
 * </pre>
 *
 * @author 안명훈
 * @version 1.1
 */
public class FormatUtil {

    public static boolean isValidUrl(String url){
        return URLUtil.isValidUrl(url) && Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * 휴대폰 번호 체크
     */
    public static boolean isCellPhoneNumber(String number) {

        String regex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
        return Pattern.matches(regex, number);
    }

    /**
     * 전화번호 체크
     */
    public static boolean isPhoneNumber(String number) {

        String regex = "^(^\\+62|62|^08)(\\d{3,4}-?){2}\\d{3,4}$";
        return Pattern.matches(regex, number);
    }

    /**
     * 이메일 체크
     */
    public static boolean isEmailAddress(String email) {

        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        String regex2 = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\-)+\\w+\\.+\\w+$";
        return Pattern.matches(regex, email) || Pattern.matches(regex2, email);
    }


    /**
     * 숫자만 구성된 경우 전화번호 포멧으로 변경 합니다.
     */
    public static String getPhoneNumber(String result) {

        if (isNull(result)) {
            return null;
        }
        String regEx = "";
        if (result.startsWith("02")) {
            regEx = "(\\d{2})(\\d{3,4})(\\d{4})";
        } else if (result.length() < 12) {
            regEx = "(\\d{2,3})(\\d{3,4})(\\d{4})";
        } else if (result.length() == 12) {
            regEx = "(\\d{4})(\\d{4})(\\d{4})";
        } else {
            return result;
        }

        if (!Pattern.matches(regEx, result)) return null;

        result = result.replaceAll(regEx, "$1-$2-$3");

        return result;
    }

    /**
     * 문자를 제거한 숫자만 반환한다.
     * <p/>
     * return str.replaceAll("[^\\d]", ""); 또는 return str.replaceAll("\\D", "");
     */
    public static String getOnlyNumber(String result) {

        if (isNull(result)) {
            return "";
        }

        return result.replaceAll("[^0-9]", "");
    }

    /**
     * <pre>
     *     금액 형태로 반환 받습니다.
     * </pre>
     */
    public static String getMoneyType(String result) {

        if (isNull(result)) {
            return "";
        }

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("###,###");
        df.setDecimalFormatSymbols(dfs);

        try {

            double inputNum = Double.parseDouble(result);
            result = df.format(inputNum).toString();

        } catch (NumberFormatException e) {
            LogUtil.e("FormatUtil", e.toString());
        }

        return result;
    }

    public static String getRpType(String result) {

        return getMoneyType(result).replace(",",".");
    }

    public static String getMoneyTypeFloat(String result) {

        if (isNull(result)) {
            return "";
        }

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("#,###.##");
        df.setRoundingMode(RoundingMode.DOWN);
        df.setDecimalFormatSymbols(dfs);

        try {

            double inputNum = Double.parseDouble(result);
            result = df.format(inputNum).toString();

        } catch (NumberFormatException e) {
            LogUtil.e("FormatUtil", e.toString());
        }

        return result;
    }

    public static String getCoinType(String result) {

        if (isNull(result)) {
            return "";
        }

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("#,###.####");
        df.setRoundingMode(RoundingMode.DOWN);
        df.setDecimalFormatSymbols(dfs);

        try {

            double inputNum = Double.parseDouble(result);
            result = df.format(inputNum).toString();

        } catch (NumberFormatException e) {
            LogUtil.e("FormatUtil", e.toString());
        }

        return result;
    }

    private static boolean isNull(String s) {

        return StringUtils.isEmpty(s);
    }
}
