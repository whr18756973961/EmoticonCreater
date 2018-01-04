package com.android.emoticoncreater.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

/**
 * 数据库工具
 */

public class LiteOrmHelper {

    private final String DB_NAME = "YangHai.db";

    private final boolean DEBUGGABLE = true; // 是否输出log

    private LiteOrm liteOrm;

    public LiteOrmHelper(Context context) {
        liteOrm = LiteOrm.newSingleInstance(context, DB_NAME);
        liteOrm.setDebugged(DEBUGGABLE);
    }

    public LiteOrmHelper(Context context, boolean isSingle) {
        if (isSingle) {
            liteOrm = LiteOrm.newSingleInstance(context, DB_NAME);
        } else {
            liteOrm = LiteOrm.newCascadeInstance(context, DB_NAME);
        }
        liteOrm.setDebugged(DEBUGGABLE);
    }

    public <T> T queryFirst(Class<T> cla, String where, Object... whereArgs) {
        final List<T> list = queryAll(cla, where, whereArgs);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public <T> T queryFirst(Class<T> cla) {
        final List<T> list = queryAll(cla);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public <T> List<T> queryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }

    public <T> List<T> queryAll(Class<T> cla, String where, Object... whereArgs) {
        return liteOrm.query(new QueryBuilder<>(cla).where(where, whereArgs));
    }

    public <T> List<T> queryAllOrderDescBy(Class<T> cla, String orderBy) {
        return liteOrm.query(new QueryBuilder<>(cla).appendOrderDescBy(orderBy));
    }

    public <T> List<T> queryAllWhereDescBy(Class<T> cla, String where, String orderBy, Object... whereArgs) {
        return liteOrm.query(new QueryBuilder<>(cla).where(where, whereArgs).appendOrderDescBy(orderBy));
    }

    public <T> long save(T objec) {
        return liteOrm.save(objec);
    }

    public <T> long saveAll(List<T> objects) {
        return liteOrm.save(objects);
    }

    public <T> int update(T object) {
        return liteOrm.update(object);
    }

    public <T> int update(Class<T> cla, String where, Object... whereArgs) {
        return liteOrm.update(new QueryBuilder<>(cla).where(where, whereArgs));
    }

    public <T> int updateAll(List<T> objects) {
        return liteOrm.update(objects);
    }

    public <T> int delete(T object) {
        return liteOrm.delete(object);
    }

    public <T> int deleteAll(Class<T> cla) {
        return liteOrm.delete(cla);
    }

    public void closeDB() {
        if (liteOrm != null) {
            liteOrm.close();
        }
    }
}
