package ai.maum.beframework.vo.meta.type.binary;

/**
 * 사진 유형
 * @author baekgol@maum.ai
 */
public enum ImageType implements BinaryType {
    PNG,
    JPEG,
    GIF;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "IMAGE";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
