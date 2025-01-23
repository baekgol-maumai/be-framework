package ai.maum.beframework.vo;

import ai.maum.beframework.codemessage.CodeMessage;
import lombok.Getter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 기본 예외
 * @author baekgol@maum.ai
 */
@Getter
public class BaseException extends RuntimeException {
    private CodeMessage info;

    private BaseException(CodeMessage info) {
        super(info.getMessage());
        this.info = info;
    }

    private BaseException(Exception e) {
        super(e);
    }

    @Override
    public String toString() {
        Throwable e = getCause();
        String info = "예외";

        if(this.info != null) {
            info += ("(" + this.info.getCode());

            if(e != null) info += (", "
                    + e.getClass().getSimpleName()
                    + "): "
                    + getStackTrace(e));
            else info += ")";
        }
        else if(e != null) info += ("("
                + e.getClass().getSimpleName()
                + "): "
                + getStackTrace(e));
        else info += (": " + getMessage());

        return info;
    }

    public static BaseException of(CodeMessage info) {
        return new BaseException(info);
    }

    public static BaseException of(Exception e) {
        return new BaseException(e);
    }

    private static String getStackTrace(Throwable e) {
        String message = e.getMessage();

        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            message = sw.toString();
        } catch(IOException ignored) {}

        return message;
    }
}
