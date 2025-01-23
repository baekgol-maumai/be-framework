package ai.maum.beframework.vo.meta.type.binary;

/**
 * 압축 유형
 * @author baekgol@maum.ai
 */
public enum ZipType implements BinaryType {
    ZIP,
    GZIP;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "ZIP";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
