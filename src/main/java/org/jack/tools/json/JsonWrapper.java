package org.jack.tools.json;

import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Function;

public class JsonWrapper<T extends JsonWrapper> {
    private JSONObject jsonObj;

    public JSONObject getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
    }
    
    public T jsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
        return (T) this;
    }

    public String get(String id) {
        return JSONUtils.get(this.getJsonObj(), id, String.class);
    }
    
    public boolean has(String id) {
        return JSONUtils.get(this.getJsonObj(), id, String.class) != null;
    }
    
    public <T> T get(String id, Class<T> type) {
        return JSONUtils.get(this.getJsonObj(), id, type);
    }
    
    public <T> T get(String id, Function<Object,T> trasf) {
        return JSONUtils.get(this.getJsonObj(), id, trasf);
    }
    
}
