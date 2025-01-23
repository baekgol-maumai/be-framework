package ai.maum.beframework.util;

import ai.maum.beframework.vo.BaseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * 데이터 유틸
 * @author baekgol@maum.ai
 */
public class DataUtil {
    private DataUtil() {}

    /**
     * 바이너리 데이터를 Base64 인코딩된 문자열로 변환한다.
     * @param bytes 바이너리 데이터
     * @return Base64로 인코딩된 문자열
     */
    public static String convertToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 바이너리 데이터 목록을 Base64 인코딩된 문자열로 변환한다.
     * @param bytesList 바이너리 데이터 목록
     * @return Base64로 인코딩된 문자열
     */
    public static String convertToBase64(List<?> bytesList) {
        return Base64.getEncoder()
                .encodeToString(bytesList.stream()
                        .reduce(new ByteArrayOutputStream(),
                                (result, e) -> {
                                    try {
                                        result.write((byte[])e);
                                        return result;
                                    } catch(IOException ex) {
                                        throw BaseException.of(ex);
                                    }
                                },
                                (c1, c2) -> c1)
                        .toByteArray());
    }
}
