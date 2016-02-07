package org.jack.tools.json;

import java.util.List;

public class JSONPathItem {
    private String itemKey;
    private String idName;
    private String idValue;
    private List<Integer> indexes;

    public JSONPathItem() {
        super();
    }

    public String getIdName() {
        return idName;
    }

    public JSONPathItem setIdName(String idName) {
        this.idName = idName;
        return this;
    }

    public String getIdValue() {
        return idValue;
    }

    public JSONPathItem setIdValue(String idValue) {
        this.idValue = idValue;
        return this;
    }

    public String getItemKey() {
        return itemKey;
    }

    public JSONPathItem setItemKey(String itemKey) {
        this.itemKey = itemKey;
        return this;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public JSONPathItem setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(itemKey);
        if (idValue != null && idName != null) {
            builder.append("[").append(idName).append("=").append(idValue).append("]");
        }
        if (indexes != null) {
            builder.append(indexes);
        }
        return builder.toString();
    }

}
