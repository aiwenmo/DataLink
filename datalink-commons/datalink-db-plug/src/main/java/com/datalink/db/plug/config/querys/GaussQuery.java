package com.datalink.db.plug.config.querys;

/**
 * Gauss 表数据查询
 */
public class GaussQuery extends AbstractDbQuery {

    @Override
    public String schemaAllSql() {
        return null;
    }

    @Override
    public String tableAllSql() {
        return null;
    }

    @Override
    public String tablesSql() {
        return "SELECT DISTINCT T1.TABLE_NAME,T2.COMMENTS AS TABLE_COMMENT FROM USER_TAB_COLUMNS T1 " +
                "LEFT JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME = T2.TABLE_NAME WHERE 1=1 ";
    }

    @Override
    public String tableFieldsSql() {
        return
                "SELECT T1.COLUMN_NAME,T2.COMMENTS,T1.DATA_TYPE," +
                        "CASE WHEN CONSTRAINT_TYPE='P' THEN 'PRI' END AS KEY " +
                        "FROM USER_TAB_COLUMNS T1 " +
                        "LEFT JOIN USER_COL_COMMENTS T2 ON " +
                        "(T1.TABLE_NAME = T2.TABLE_NAME AND T1.COLUMN_NAME = T2.COLUMN_NAME) " +
                        "LEFT JOIN (" +
                        "SELECT T4.TABLE_NAME,T4.COLUMN_NAME,T5.CONSTRAINT_TYPE FROM USER_CONS_COLUMNS T4,USER_CONSTRAINTS T5 " +
                        "WHERE T4.CONSTRAINT_NAME = T5.CONSTRAINT_NAME AND T5.CONSTRAINT_TYPE = 'P'" +
                        ")T3 ON (T1.TABLE_NAME = T3.TABLE_NAME AND T1.COLUMN_NAME = T3.COLUMN_NAME) " +
                        "WHERE T1.TABLE_NAME = '%s' " +
                        "ORDER BY T1.TABLE_NAME,T1.COLUMN_ID";
    }

    @Override
    public String schemaName() {
        return null;
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "TABLE_COMMENT";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "DATA_TYPE";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }

    @Override
    public String isNotNull() {
        return null;
    }
}
