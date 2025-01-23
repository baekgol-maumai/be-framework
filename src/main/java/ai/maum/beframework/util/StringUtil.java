package ai.maum.beframework.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
 * 문자열 유틸
 * @author baekgol@maum.ai
 */
public class StringUtil {
    private StringUtil() {}

    /**
     * 해당 문자열이 패턴과 일치하는지 확인한다.
     * @param target 대상 문자열
     * @param pattern 패턴, 정규식
     * @return 패턴 일치 유무
     */
    public static boolean matches(String target, String pattern) {
        return Pattern.compile(pattern)
                .matcher(target)
                .matches();
    }

    /**
     * Camel 형식의 문자열 또는 Snake 형식의 문자열을 반대 형식으로 변환한다.
     * 대상 문자열의 형식이 isCamel과 일치하지 않을 경우 대상 문자열을 그대로 반환한다.
     * @param target 대상 문자열
     * @param isCamel Camel 형식의 대상 문자열인지 유무
     * @return 변환된 형식의 문자열
     */
    public static String convertNaming(String target, boolean isCamel) {
        if((isCamel && Character.isUpperCase(target.charAt(0)))
        || (!isCamel && !target.contains("_"))) return target;

        int len = target.length();
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<len; i++) {
            char c = target.charAt(i);

            if(isCamel) {
                if(Character.isUpperCase(c)) {
                    sb.append('_');
                    sb.append(Character.toLowerCase(c));
                }
                else sb.append(c);
            }
            else {
                if(i>0 && target.charAt(i-1) == '_') sb.append(Character.toUpperCase(c));
                else if(c != '_') sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 예외 추적 정보를 문자열로 변환한다.
     * @param e 예외
     * @return 예외 추적 정보가 담긴 문자열
     */
    public static String getStackTrace(Throwable e) {
        String message = e.getMessage();

        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            message = sw.toString();
        } catch(IOException ignored) {}

        return message;
    }

    /**
     * 문자열을 축약한다.
     * @param value 문자열
     * @return 축약된 문자열
     */
    public static String abbreviate(String value) {
        final int len = value.length();
        return len > 50
                ? value.substring(0, 40) + "..." + value.substring(len - 11)
                : value;
    }
}
