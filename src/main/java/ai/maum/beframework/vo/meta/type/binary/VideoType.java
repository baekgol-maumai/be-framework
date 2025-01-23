package ai.maum.beframework.vo.meta.type.binary;

/**
 * 영상 유형
 * @author baekgol@maum.ai
 */
public enum VideoType implements BinaryType {
    MP4,
    WEBM;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "VIDEO";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
