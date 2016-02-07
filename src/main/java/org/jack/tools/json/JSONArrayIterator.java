package org.jack.tools.json;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class JSONArrayIterator<T> implements Iterator<T> {
    private int current_index;
    private JSONArray array;
    
    public JSONArrayIterator(JSONArray array) {
        super();
        this.current_index = 0;
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        if ( array == null ) {
            return false;
        }
        return this.current_index < array.length();
    }

    @Override
    public T next() {
        if ( array == null ) {
            return null;
        }
        if ( current_index < array.length() ) {
            Object item;
            try {
                item = array.get(current_index++);
                return (T)item;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void remove() {

    }

}
