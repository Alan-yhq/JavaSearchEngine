package org.jsoup.nodes;

public class BooleanAttribute extends Attribute {
    /**
     * Create a new boolean attribute from unencoded (raw) key.
     * @param key attribute key
     */
    public BooleanAttribute(String key) {
        super(key, null);
    }

    @Override
    protected boolean isBooleanAttribute() {
        return true;
    }
}
