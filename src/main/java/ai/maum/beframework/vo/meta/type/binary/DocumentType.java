package ai.maum.beframework.vo.meta.type.binary;

/**
 * 문서 유형
 * @author baekgol@maum.ai
 */
public enum DocumentType implements BinaryType {
    PDF,
    WORD;

    @Override
    public String getScheme() {
        return "BINARY";
    }

    @Override
    public String getFormat() {
        return "DOCUMENT";
    }

    @Override
    public String getSpec() {
        return this.name();
    }
}
