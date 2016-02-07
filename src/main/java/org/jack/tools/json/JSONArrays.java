package org.jack.tools.json;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import com.google.common.base.Function;

public class JSONArrays {
	/**
	 * Transforms a JSONArray in an Iterable<T> object. This can be used to iterate with a for-each java instruction on a JSONArray.
	 * 
	 * @param array the JSONArray
	 * @param itemType the class of contained object in JSONArray
	 * @return an iterable
	 */
	public static <T> Iterable<T> toIterable(JSONArray array, Class<T> itemType) {
        return new JSONArrayIterable<T>(array);
    }
	
	/**
	 * Transforms the JSONArray element of class Tin in a list of TOut element, applying to each JSONArray element the Function transf.
	 * 
	 * @param array the JSONArray
	 * @param transf the transformation function
	 * @return a List of TOut elements
	 */
    public static <Tin, TOut> List<TOut> transform(JSONArray array, Function<Tin, TOut> transf) {
        List<TOut> result = new LinkedList<>();
        for (Tin item : new JSONArrayIterable<Tin>(array)) {
            TOut out = transf.apply(item);
            if ( out != null ) {
                result.add(out);
            }
        }
        return result;
    }
    

    /**
	 * This method is used to apply the Function executor to each JSONArray element.
	 * 
     * @param array the JSONArray
     * @param executor the function to be applied to each element of array
     */
    public static <Tin> void foreach(JSONArray array, Function<Tin, Void> executor) {
        for (Tin item : new JSONArrayIterable<Tin>(array)) {
            executor.apply(item);
        }
    }
//    public static <TOut> TOut accumulate(JSONArray array, Function<Tin, Void> executor) {
//        List<TOut> result = new LinkedList<>();
//        for (Tin item : new JSONArrayIterable<Tin>(array)) {
//            executor.apply(item);
//        }
//        return result;
//    }
}
