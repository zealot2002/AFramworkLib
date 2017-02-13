package org.zzy.aframwork.util;



import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by lishiming on 16/11/23.
 */

public class DateAndTimeUtil {

    public static String Timestamp2String(Timestamp timestamp) throws Exception {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(timestamp);

    }

}
