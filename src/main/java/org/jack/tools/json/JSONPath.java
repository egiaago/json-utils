package org.jack.tools.json;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class JSONPath {
    public static final String Separator = "/";
    private List<JSONPathItem> items;
    private boolean absolute = false;

    private JSONPath(JSONPath original) {
        if (original != null) {
            this.items = Lists.newLinkedList(original.items);
            this.absolute = original.absolute;
        }
    }

    public JSONPath() {
        super();
        items = new LinkedList<JSONPathItem>();
    }

    public JSONPath root() {
        absolute = true;
        return this;
    }

    public JSONPath path(String itemKey) {
        if (Separator.equals(itemKey)) {
            this.absolute = true;
        } else {
            items.add(new JSONPathItem().setItemKey(itemKey));
        }
        return this;
    }

    public JSONPath path(String itemKey, String idName, String idValue) {
        if (itemKey != null) {
            this.items.add(new JSONPathItem().setItemKey(itemKey).setIdName(idName).setIdValue(idValue));
        }
        return this;
    }

    public JSONPath path(JSONPathItem pathItem) {
        if (pathItem != null) {
            this.items.add(pathItem);
        }
        return this;
    }

    public JSONPath copy() {
        JSONPath jp = new JSONPath(this);
        return jp;
    }

    public JSONPath pop() {
        if (!items.isEmpty()) {
            items.remove(0);
        }
        return this;
    }

    public JSONPath push(JSONPathItem item) {
        if (!items.isEmpty()) {
            items.add(0, item);
        }
        return this;
    }

    public JSONPathItem peek() {
        if (!items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ImmutableList<JSONPathItem> items() {
        return ImmutableList.<JSONPathItem> copyOf(this.items);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (absolute) {
            builder.append(Separator);
        }
        for (JSONPathItem jp : this.items) {
            builder.append(jp).append(Separator);
        }
        return builder.toString();
    }

}
