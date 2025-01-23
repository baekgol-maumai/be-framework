package ai.maum.beframework.vo.meta.type.binary;

/**
 * 혼합 유형
 * @author baekgol@maum.ai
 */
public enum FusionType implements BinaryType {
    AUDIO_IMAGE;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "FUSION";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
