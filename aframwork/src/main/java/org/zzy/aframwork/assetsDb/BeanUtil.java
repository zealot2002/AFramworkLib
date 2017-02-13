package org.zzy.aframwork.assetsDb;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kai.wang on 1/13/14.
 */
public class BeanUtil {

    /**
     * 从cursor中提取bean
     * @param cursor
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T cursorToBean(Cursor cursor, Class<T> cls) {
        T bean = null;
        if (cursor != null && cursor.getCount() > 0) {
            Field[] fields = cls.getDeclaredFields();
            String[] columns = cursor.getColumnNames();
            try {
                while (cursor.moveToNext()) {
                    bean = cls.newInstance();
                    for(String column : columns){
                        Field field = findFieldByName(fields,column);
                        String letter=column.substring(0, 1).toUpperCase();
                        String setter = "set" + letter + column.substring(1);
                        Method setMethod = cls.getMethod(setter,new Class[]{field.getType()});
                        setMethod.invoke(bean,getValueByField(cursor,column,field));
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 从cursor中提取bean 列表
     * @param cursor
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> cursorToBeans(Cursor cursor, Class<T> cls) {
        List<T> beans = new ArrayList<T>();
        if (cursor != null && cursor.getCount() > 0) {
            Field[] fields = cls.getDeclaredFields();
            String[] columns = cursor.getColumnNames();
            try {
                while (cursor.moveToNext()) {
                    T bean = cls.newInstance();
                    for(String column : columns){
                        Field field = findFieldByName(fields,column);
                        String letter=column.substring(0, 1).toUpperCase();
                        String setter = "set" + letter + column.substring(1);
                        Method setMethod = cls.getMethod(setter,new Class[]{field.getType()});
                        setMethod.invoke(bean,getValueByField(cursor,column,field));
                    }
                    beans.add(bean);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return beans;
    }

    /**
     * 根据字段取出值
     * @param cursor
     * @param columnName
     * @param field
     * @return
     */
    private static Object getValueByField(Cursor cursor, String columnName, Field field){
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) return null;// 如果不存在此列则返回null
        Class fieldClass = field.getType();
        // 根据属性类型从Cursor中获取值
        if (fieldClass == String.class) return cursor.getString(index);

        else if (fieldClass == int.class || fieldClass == Integer.class)
            return cursor.getInt(index);
        else if (fieldClass == long.class ||fieldClass == Long.class)
            return cursor.getLong(index);

        else if (fieldClass == double.class ||fieldClass == Double.class)
            return cursor.getDouble(index);

        else if (fieldClass == float.class ||fieldClass == Float.class)
            return cursor.getFloat(index);

        else if (fieldClass == short.class ||fieldClass == Short.class)
            return cursor.getShort(index);

        else if (fieldClass == Timestamp.class){
            String s = cursor.getString(index);
            if(s!=null){
                return Timestamp.valueOf(s);
            }
            return null;
        }


        return null;
    }

    private static Field findFieldByName(Field[] fields, String name){
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }
}
