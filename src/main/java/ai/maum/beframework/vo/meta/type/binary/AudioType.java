package ai.maum.beframework.vo.meta.type.binary;

/**
 * 오디오 유형
 * @author baekgol@maum.ai
 */
public enum AudioType implements BinaryType {
    WAV,
    MP3,
    OGG;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "AUDIO";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
