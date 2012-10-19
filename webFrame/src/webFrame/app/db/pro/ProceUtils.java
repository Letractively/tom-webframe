package webFrame.app.db.pro;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * procedure工具
 *
 * @author dargoner
 */
public class ProceUtils {

    /**
     * ResultSet 转换List<Map>
     */
    public static List<Map<String, Object>> convertResultSetToMapList(ResultSet rs, Boolean toLowerCase) {
        List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();

        ResultSetMetaData rsmd;

        if (rs == null)
            return resultListMap;
        try {
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    Object colValue = null;
                    String colName = rsmd.getColumnName(i);
                    if (toLowerCase) {
                        colName = colName.toLowerCase();
                    }
                    if (rsmd.getColumnType(i) == java.sql.Types.CLOB) {
                        colValue = rs.getClob(i);
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        colValue = rs.getBlob(i);
                    } else {
                        colValue = rs.getObject(i);
                    }
                    rowMap.put(colName, colValue);
                }
                resultListMap.add(rowMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    rs = null;
                }
            }
        }
        return resultListMap;
    }

    /**
     * 存储过程输入参数绑定
     */
    public static void bindStatementInput(CallableStatement cs, int indexed, Object value) throws SQLException {
        if (value instanceof String) {
            cs.setString(indexed, (String) value);
        } else if (value instanceof Integer) {
            cs.setInt(indexed, ((Integer) value));
        } else if (value instanceof Double) {
            cs.setDouble(indexed, (Double) value);
        } else if (value instanceof Float) {
            cs.setFloat(indexed, ((Float) value));
        } else if (value instanceof Long) {
            cs.setLong(indexed, ((Long) value));
        } else if (value instanceof Boolean) {
            cs.setBoolean(indexed, ((Boolean) value));
        } else if (value instanceof java.util.Date) {
            cs.setDate(indexed, new java.sql.Date(((java.util.Date) value).getTime()));
        } else if (value instanceof BigDecimal) {
            cs.setBigDecimal(indexed, (BigDecimal) value);
        } else if (value instanceof Blob) {
            cs.setBlob(indexed, (Blob) value);
        } else if (value instanceof Clob) {
            cs.setClob(indexed, (Clob) value);
        } else {
            cs.setObject(indexed, value);
        }
    }

    /**
     * 存储过程传出参数绑定
     */
    public static void bindStatementOutput(CallableStatement cs, int indexed, String dataType) throws SQLException {
        dataType = StringUtils.lowerCase(dataType);

        if (StringUtils.equals(dataType, "string") || StringUtils.equals(dataType, "char")) {
            cs.registerOutParameter(indexed, Types.CHAR);
        } else if (StringUtils.equals(dataType, "int") || StringUtils.equals(dataType, "integer")) {
            cs.registerOutParameter(indexed, Types.INTEGER);
        } else if (StringUtils.equals(dataType, "date")) {
            cs.registerOutParameter(indexed, Types.DATE);
        } else if (StringUtils.equals(dataType, "timestamp")) {
            cs.registerOutParameter(indexed, Types.TIMESTAMP);
        } else if (StringUtils.equals(dataType, "decimal")) {
            cs.registerOutParameter(indexed, Types.DECIMAL);
        } else if (StringUtils.equals(dataType, "float")) {
            cs.registerOutParameter(indexed, Types.FLOAT);
        } else if (StringUtils.equals(dataType, "double")) {
            cs.registerOutParameter(indexed, Types.DOUBLE);
        } else if (StringUtils.equals(dataType, "cursor")) {// OracleTypes.CURSOR,Oralce游标作为out参数
            cs.registerOutParameter(indexed, -10);
        } else {
            cs.registerOutParameter(indexed, Types.CHAR);
        }
    }

    /**
     * 传出参数类型转换
     *
     * @throws SQLException
     */
    public static Object convertType(CallableStatement cs, int indexed, String dataType) throws SQLException {
        dataType = StringUtils.lowerCase(dataType);

        Object dataValue = null;
        try {
            dataValue = cs.getObject(indexed);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (dataValue == null) {
            return null;
        }

        if (StringUtils.equals(dataType, "cursor")) {
            return ProceUtils.convertResultSetToMapList((ResultSet) dataValue, false); //oracle游标作为传出参数接收
        } else {
            return dataValue;
        }

    }

}
