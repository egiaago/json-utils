package org.jack.tools.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.codehaus.jackson.impl.Indenter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Function;

public class JSONUtils {

    static public JSONObject toJSONObject(String fileName) {
        if (fileName == null) {
            return null;
        }
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            JSONObject obj = new JSONObject(new String(encoded));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public JSONObject toJSONObject(InputStream in) {
        if (in == null) {
            return null;
        }
        return toJSONObject(new InputStreamReader(in));
    }

    static public JSONObject toJSONObject(Reader reader) {
        if (reader == null) {
            return null;
        }
        try {
            LineNumberReader lnr = new LineNumberReader(reader);
            StringBuilder result = new StringBuilder();
            for (String line = lnr.readLine(); line != null; line = lnr.readLine()) {
                result.append(line);
            }
            JSONObject obj = new JSONObject(result.toString());
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public JSONObject toJSONObject(InputStream in, VelocityContext context) {
        if (in == null) {
            return null;
        }
        return toJSONObject(new InputStreamReader(in), context);
    }

    static public JSONObject toJSONObject(Reader reader, VelocityContext context) {
        if (reader == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        try {
			reader.reset();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Velocity.init();
        Velocity.evaluate(context, sw, "", reader);

        JSONObject obj = null;
        try {
            obj = new JSONObject(sw.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class MyDefaultPrettyPrinter extends DefaultPrettyPrinter {

        @Override
        public void indentArraysWith(Indenter i) {
            super.indentArraysWith(i);
        }

        @Override
        public void indentObjectsWith(Indenter i) {
            super.indentObjectsWith(i);
        }

    }

    public static String toPrettyString(JSONObject jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        return jsonObj.toString();
    }

    public static <T> T get(JSONObject obj, String id, Class<T> type) {
        if ( obj == null || id == null) {
            return null;
        }
        try {
            if ( obj.has(id) ) {
                Object value = obj.get(id);
                if ( value == null ) {
                    return null;
                }
                if ( type.isAssignableFrom(value.getClass()) ) {
                    return (T) value;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <TIn, TOut> TOut get(JSONObject obj, String id, Function<Object, TOut> converter) {
        if ( obj == null || id == null) {
            return null;
        }
        try {
            if ( obj.has(id) ) {
                Object value = obj.get(id);
                if ( value == null ) {
                    return null;
                }
                if ( converter != null ) {
                    return converter.apply(value);
                }
                return (TOut) value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
