package org.jack.tools.json;

import org.codehaus.jettison.json.JSONObject;

public class JSonFormatter {
    public JSONObject format(JSONObject value) {
        JSonFormatterVisitor visitor = new JSonFormatterVisitor();
        Object obj = visitor.visit(value);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        return null;
    }
}
