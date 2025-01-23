package ai.maum.beframework.vo.meta.type.binary;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.type.DataType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 바이너리 유형 인터페이스
 * @author baekgol@maum.ai
 */
@JsonTypeName("BINARY")
public interface BinaryType extends DataType {
    @JsonProperty
    String getSpec();

    static BinaryType valueOf(String format, String spec) {
        return switch(format) {
            case "AUDIO" -> AudioType.valueOf(spec);
            case "DOCUMENT" -> DocumentType.valueOf(spec);
            case "IMAGE" -> ImageType.valueOf(spec);
            case "VIDEO" -> VideoType.valueOf(spec);
            case "ZIP" -> ZipType.valueOf(spec);
            case "FUSION" -> FusionType.valueOf(spec);
            default -> throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE); };
    }
}
