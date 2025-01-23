package ai.maum.beframework.vo.meta.type;

import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

/**
 * 요청 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum RequestType implements Type {
    JSON(MediaType.APPLICATION_JSON),
    MULTIPART_FORMDATA(MediaType.MULTIPART_FORM_DATA);

    private final MediaType value;
}
