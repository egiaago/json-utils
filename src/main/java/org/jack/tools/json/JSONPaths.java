package org.jack.tools.json;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONPaths {

    private static String pathItemPatternStr = "(?<key>[a-zA-Z_0-9\\-:]+)(?:\\[(?:(?:(?<idName>[a-zA-Z_0-9\\-:]+)=(?<idValue>.+))|(?<index>\\d+[,\\d+]*))\\])?";
    private static Pattern pathItemPattern;

    static {
        pathItemPattern = Pattern.compile(pathItemPatternStr);
    }

    public static JSONPath get(String path) {
        JSONPath json_path = new JSONPath();
        if (path == null) {
            return null;
        }
        if (path.startsWith(JSONPath.Separator)) {
            json_path.root();
            path = path.substring(1);
        }

        String[] items = path.split(String.format("\\%s", JSONPath.Separator));
        for (String item : items) {
            Matcher pathItemMatcher = pathItemPattern.matcher(item);
            if (pathItemMatcher.matches()) {
                JSONPathItem p = new JSONPathItem();
                if (pathItemMatcher.groupCount() >= 1) {
                    p.setItemKey(pathItemMatcher.group("key"));
                    p.setIdName(pathItemMatcher.group("idName"));
                    p.setIdValue(pathItemMatcher.group("idValue"));
                    try {
                        String indexes = pathItemMatcher.group("index");
                        if (indexes != null) {
                            List<Integer> is = new LinkedList<>();
                            for (String index : indexes.split(",")) {
                                is.add(Integer.parseInt(index));
                            }
                            p.setIndexes(is);
                        }
                    } catch (NumberFormatException e) {

                    }
                    json_path.path(p);
                }
            } else {
                return null;
            }
        }
        return json_path;
    }

    public static void main(String[] args) {
        JSONPath jp = JSONPaths.get("/a/b[1]/c");
        System.out.println(jp);
        jp = JSONPaths.get("/a/b[1,2,3]/c");
        System.out.println(jp);
        // Pattern p = Pattern.compile(pathItemPatternStr);
        // Matcher m = p.matcher("a[1a]");
        // if ( m.matches() ) {
        // System.out.println("OK");
        // }
        // else {
        // System.out.println("NOT OK");
        // }
    }
}
