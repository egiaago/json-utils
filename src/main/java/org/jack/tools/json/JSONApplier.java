package org.jack.tools.json;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
/**
 * This class can be used to apply a Function to each element of a JSON hierarchy.
 * 
 * @author guest
 *
 */
public class JSONApplier {

	/**
	 * The Function f is applied to the whole hierarchy starting from parent JSONObject root.
	 * 
	 * @param parent the root of the hierarchy
	 * @param f the function to be applied
	 */
    @SuppressWarnings("unchecked")
    public static void apply(JSONObject parent, Function<Object, Void> f) {
        if (f == null) {
            return;
        }

        if (parent == null) {
            return;
        }

        f.apply(parent);

        Object obj;
        Object result = null;
        for (Object key : ImmutableList.<Object> copyOf(parent.keys())) {
            try {
                obj = parent.get((String) key);
                if (obj instanceof JSONObject) {
                    apply((JSONObject) obj, f);
                } else if (obj instanceof JSONArray) {
                    JSONArray arrayObj = (JSONArray) obj;
                    apply(arrayObj, f);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /**
	 * The Function f is applied to the whole hierarchy starting from the list of parents contained in JSONArray roots.
     * 
     * @param arrayObj the JSONArray 
     * @param f the function to be applied
     */
    public static void apply(JSONArray arrayObj, Function<Object, Void> f) {
        if (f == null) {
            return;
        }

        if (arrayObj == null) {
            return;
        }

        f.apply(arrayObj);

        Object obj;

        Object result = null;
        for (int arrayIndex = 0; arrayIndex < arrayObj.length(); ++arrayIndex) {
            try {
                obj = arrayObj.get(arrayIndex);
                if (obj instanceof JSONObject) {
                    apply((JSONObject) obj, f);
                } else if (obj instanceof JSONArray) {
                    apply((JSONArray) obj, f);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

}
