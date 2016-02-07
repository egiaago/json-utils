package org.jack.tools.json;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;

public class JSONArrayIterable<T> implements Iterable<T> {

    private JSONArray array;
    
    public JSONArrayIterable(JSONArray array) {
        super();
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new JSONArrayIterator<T>(array);
    }

}
