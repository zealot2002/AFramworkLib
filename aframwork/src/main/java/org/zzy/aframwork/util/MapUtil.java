package org.zzy.aframwork.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by admin on 16/12/16.
 */

public class MapUtil {
    /**
     * 方法名称:transStringToMap
     * 传入参数:mapString 形如 username'chenziwen^password'1234
     * 返回值:Map
     */
    public static Map transStringToMap(String mapString){
        Map map = new HashMap();
        StringTokenizer items;
        for(StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
            map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "'");
        return map;
    }

    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    public static String transMapToString(Map map){
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            entry = (Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( "'" ).append(null==entry.getValue()?"":
                    entry.getValue().toString()).append (iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

}
