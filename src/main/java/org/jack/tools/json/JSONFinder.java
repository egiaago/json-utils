package org.jack.tools.json;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

public class JSONFinder {
    private JSONObject jsonObj;

    public JSONFinder(JSONObject jsonObject) {
        this.jsonObj = jsonObject;
    }

    /**
     * Returns a JSONObject searching it in parent JSONObject and applying the predicate to evaluate if is ok.
     * @param parent the root
     * @param predicate a predicate to check if it's the correct JSONObject
     * @return the found JSONObject or null if not found
     * @see Predicate
     */
    public static JSONObject find(JSONObject parent, Predicate<JSONObject> predicate) {
        if (predicate == null) {
            return null;
        }

        if (parent == null) {
            return null;
        }

        if (predicate.apply(parent)) {
            return parent;
        }

        Object obj;
        JSONObject result = null;
        for (Object key : ImmutableList.<Object> copyOf(parent.keys())) {
            try {
                obj = parent.get((String) key);
                if (obj instanceof JSONObject) {
                    if ((result = find((JSONObject) obj, predicate)) != null) {
                        return result;
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray arrayObj = (JSONArray) obj;
                    if ((result = find(arrayObj, predicate)) != null) {
                        return result;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Returns a JSONObject searching it in parent JSONArray and applying the predicate to evaluate if is ok.
     * 
     * @param parent the root
     * @param predicate a predicate to check if it's the correct JSONObject
     * @return the found JSONObject or null if not found
     * @see Predicate
     */
    public static JSONObject find(JSONArray arrayObj, Predicate<JSONObject> predicate) {
        if (predicate == null) {
            return null;
        }

        if (arrayObj == null) {
            return null;
        }

        Object obj;
        JSONObject result = null;
        for (int arrayIndex = 0; arrayIndex < arrayObj.length(); ++arrayIndex) {
            try {
                obj = arrayObj.get(arrayIndex);
                if (obj instanceof JSONObject) {
                    if ((result = find((JSONObject) obj, predicate)) != null) {
                        return result;
                    }
                } else if (obj instanceof JSONArray) {
                    if ((result = find((JSONArray) obj, predicate)) != null) {
                        return result;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONObject find(Predicate<JSONObject> predicate) {
        return find(this.jsonObj, predicate);
    }

    public static List<JSONObject> findAll(JSONObject parent, Predicate<JSONObject> predicate) {
        if (predicate == null) {
            return null;
        }

        if (parent == null) {
            return null;
        }

        List<JSONObject> result = new LinkedList<>();
        if (predicate.apply(parent)) {
            result.add(parent);
            return result;
        }

        Object obj;
        Object key;
        List<JSONObject> res;
        Iterator<?> jsonIte = parent.keys();
        while (jsonIte.hasNext()) {
            key = jsonIte.next();
            try {
                obj = parent.get((String) key);
                if (obj instanceof JSONObject) {
                    if ((res = findAll((JSONObject) obj, predicate)) != null) {
                        result.addAll(res);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray arrayObj = (JSONArray) obj;
                    if ((res = findAll(arrayObj, predicate)) != null) {
                        result.addAll(res);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<JSONObject> findAll(JSONArray arrayObj, Predicate<JSONObject> predicate) {
        if (predicate == null) {
            return null;
        }

        if (arrayObj == null) {
            return null;
        }

        Object obj;

        List<JSONObject> result = new LinkedList<>();
        List<JSONObject> res = null;
        for (int arrayIndex = 0; arrayIndex < arrayObj.length(); ++arrayIndex) {
            try {
                obj = arrayObj.get(arrayIndex);
                if (obj instanceof JSONObject) {
                    if ((res = findAll((JSONObject) obj, predicate)) != null) {
                        result.addAll(res);
                    }
                } else if (obj instanceof JSONArray) {
                    if ((res = findAll((JSONArray) obj, predicate)) != null) {
                        result.addAll(res);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<JSONObject> findAll(Predicate<JSONObject> predicate) {
        return findAll(this.jsonObj, predicate);
    }

    public <T> T get(JSONPath path, Class<T> cls) {
        return get(this.jsonObj, path, cls);
    }

    public static <T> T get(JSONObject jsonObj, JSONPath path, Class<T> cls) {
        if (path == null) {
            return null;
        }
        JSONPathItem item = path.peek();
        if (item == null) {
            return null;
        }
        try {
            Object obj = jsonObj.get(item.getItemKey());
            JSONPath childPath = path.copy().pop();
            if (childPath.isEmpty()) {
                if (JSONArray.class.isAssignableFrom(cls) && !(obj instanceof JSONArray)) {
                    JSONArray a = new JSONArray();
                    a.put(obj);
                    return (T) a;
                }
                return (T) obj;
            }
            if (obj instanceof JSONObject) {
                return get((JSONObject) obj, childPath, cls);
            } else {
                return get((JSONArray) obj, path, cls);
            }
        } catch (JSONException e) {
        }
        return (T) null;
    }

    public static <T> T get(JSONArray jsonArray, JSONPath path, Class<T> cls) {
        if (path == null) {
            return null;
        }
        JSONPathItem item = path.peek();
        if (item == null) {
            return null;
        }
        if (item.getIndexes() != null) {
            List<Integer> indexes = item.getIndexes();
            int index = indexes.get(0);
            indexes.remove(0);
            if (index >= 0 && index <= jsonArray.length()) {
                try {
                    Object value = jsonArray.get(index);
                    if (value instanceof JSONArray) {
                        JSONPath childPath = path.copy().pop();
                        if (!indexes.isEmpty()) {
                            item.setIndexes(indexes);
                            childPath.push(item);
                        }
                        if (childPath.isEmpty()) {
                            if (cls.isAssignableFrom(value.getClass())) {
                                return (T) value;
                            }
                        }
                        return get((JSONArray) value, childPath, cls);
                    } else {
                        if (!indexes.isEmpty()) {
                            return null;
                        }
                        JSONPath childPath = path.copy().pop();
                        if (childPath.isEmpty()) {
                            if (cls.isAssignableFrom(value.getClass())) {
                                return (T) value;
                            }
                        }
                        if (value instanceof JSONObject) {
                            return get((JSONObject) value, childPath, cls);
                        } else {
                            return null;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            for (int iItem = 0; iItem < jsonArray.length(); iItem++) {
                Object value;
                try {
                    value = jsonArray.get(iItem);
                    if (value instanceof JSONObject) {
                        JSONObject jsonObj = (JSONObject) value;
                        try {
                            Object obj = jsonObj.get(item.getIdName());
                            if (obj != null) {
                                if (obj.equals(item.getIdValue())) {
                                    JSONPath childPath = path.copy().pop();
                                    if (childPath.isEmpty()) {
                                        return (T) jsonObj;
                                    }
                                    if (jsonObj instanceof JSONObject) {
                                        return get((JSONObject) jsonObj, childPath, cls);
                                    } else {
                                        return null;
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return (T) null;
    }

    public static <T> T get(JSONObject jsonObj, String path, Class<T> cls) {
        JSONPath jsonPath = JSONPaths.get(path);
        return get(jsonObj, jsonPath, cls);
    }

    public static <T> T get(JSONObject jsonObj, String path, Function<Object, T> converter) {
        JSONPath jsonPath = JSONPaths.get(path);
        try {
            Class<?> returnType = converter.getClass().getMethod("apply", Object.class).getReturnType();
            if ( returnType != null ) {
                T value = (T) get(jsonObj, jsonPath, returnType);
                return converter.apply(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject jobj = JSONUtils
                .toJSONObject(new StringReader(
                        "{\"network-endpoint-pair\":[{\"network-endpoint-pair-id\":\"nep-pair:1\",\"path-changes\":1,\"multilayer-route\":{\"route-id\":\"route:13\",\"route-point\":[{\"route-point-id\":1,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU2\",\"tp-ref\":\"SPOPAU2:OCH-1-4-3\"},{\"route-point-id\":2,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU2\",\"forward-label\":{\"wdm-label\":{\"grid-cs\":\"wdm-label:flexi-dwdm-at-6_25-GHz\",\"n\":-96,\"identifier\":0}},\"tp-ref\":\"SPOPAU2:L-3\"},{\"route-point-id\":3,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU1\",\"forward-label\":{\"wdm-label\":{\"grid-cs\":\"wdm-label:flexi-dwdm-at-6_25-GHz\",\"n\":-96,\"identifier\":0}},\"tp-ref\":\"SPOPAU1:L-3\"},{\"route-point-id\":4,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU1\",\"forward-label\":{\"wdm-label\":{\"grid-cs\":\"wdm-label:flexi-dwdm-at-6_25-GHz\",\"n\":-96,\"identifier\":0}},\"tp-ref\":\"SPOPAU1:L-2\"},{\"route-point-id\":5,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU3\",\"forward-label\":{\"wdm-label\":{\"grid-cs\":\"wdm-label:flexi-dwdm-at-6_25-GHz\",\"n\":-96,\"identifier\":0}},\"tp-ref\":\"SPOPAU3:L-2\"},{\"route-point-id\":6,\"topology-ref\":\"mlmt:1\",\"loose\":false,\"node-name\":\"SPOPAU3\",\"tp-ref\":\"SPOPAU3:OCH-1-4-2\"}]},\"last-path-change\":\"2015-07-21T18:39:28Z\",\"name\":\"a1\",\"profile\":\"default-platinum-profile\",\"bandwidth\":100,\"creation-time\":\"2015-07-21T18:39:28Z\",\"oper-status\":\"down\",\"oper-status-details\":\"no error\",\"supporting-tunnel-a\":\"tunnel:13\",\"a-end\":{\"node-name\":\"SPOPAU2\",\"topology-ref\":\"mlmt:1\",\"tp-ref\":\"SPOPAU2:TXP-1-17-4\"},\"operational-state-transition\":0,\"z-end\":{\"node-name\":\"SPOPAU3\",\"topology-ref\":\"mlmt:1\",\"tp-ref\":\"SPOPAU3:TXP-1-3-4\"},\"supporting-tunnel-z\":\"tunnel:13\",\"admin-status\":\"up\"}]}"));
        String op = JSONFinder.get(jobj, JSONPaths.get("network-endpoint-pair[0]/oper-status"), String.class);
        System.out.println(JSONFinder.get(jobj, JSONPaths.get("network-endpoint-pair[0]"), JSONObject.class));
        System.out.println(op);
        
        JSONObject jobj1 = JSONUtils
                .toJSONObject(new StringReader("{\"a\" : [[{\"b\": \"pippo\",\"c\": 1}],{\"b\": \"pluto\",\"c\" : 9}]}"));
        String op1 = JSONFinder.get(jobj1, JSONPaths.get("/a[0,0]/b"), String.class);
        System.out.println(op1);
        System.out.println(JSONFinder.get(jobj1, JSONPaths.get("/a"), JSONArray.class));
    }

}
