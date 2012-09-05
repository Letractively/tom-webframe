package webFrame.app.db;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import webFrame.app.bean.*;
import webFrame.app.listener.Variable;
import webFrame.report.Log;

public class DBUtils {

	private static String Key_Connection = "connection";

	public static boolean isDB2() throws Exception {
		return Variable.dbpool.driverClass.toLowerCase().indexOf("db2") != -1;
	}

    public static boolean isOracle() throws Exception {
        return Variable.dbpool.driverClass.toLowerCase().indexOf("oracle") != -1;
    }

    public static boolean isMysql() throws Exception {
        return Variable.dbpool.driverClass.toLowerCase().indexOf("mysql") != -1;
    }

	public static Connection getConnection() throws Exception {
		Connection conn = getThreadConnection();
		if ((conn == null) || (conn.isClosed())) {
			conn = getConnection(Variable.dbpool);
			setThreadConnection(conn);
		}

		return conn;
	}

	public static Connection getConnection(DBPool _dbpool) throws Exception {
		try {
			return _dbpool.cpds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			Log.writeLog("获取数据库连接失败! 可能没有初始化,正在重新初始化..." + e.toString());
			_dbpool.init();
			try {
				return _dbpool.cpds.getConnection();
			} catch (Exception ee) {
				Log.writeLog("数据库连接失败! 请检查数据库配制!" + ee.toString());
				throw ee;
			}
		}
	}

	private static Connection getThreadConnection() {
		Connection conn = null;
		Record<String, Connection> rec = Variable.threadMap.get(Thread.currentThread());
		if ((rec != null) && (rec.hasColumn(Key_Connection))) {
			Object obj = rec.getV(Key_Connection);
			if (obj != null) {
				conn = (Connection) obj;
			}
		}
		return conn;
	}

	private static void setThreadConnection(Connection _conn) {
		Record<String, Connection> rec = Variable.threadMap.get(Thread.currentThread());
		if (rec != null)
			rec.put(Key_Connection, _conn);
	}

	/*********************设置PreparedStatement预编译参数******************/
	private static void setPSTValue(PreparedStatement _pst, int _index, Object _obj) throws Exception {
		if (_obj instanceof String) {
			_pst.setString(_index, (String) _obj);
		} else if (_obj instanceof Integer) {
			_pst.setInt(_index, ((Integer) _obj));
		} else if (_obj instanceof Double) {
			_pst.setDouble(_index, (Double) _obj);
		} else if (_obj instanceof Float) {
			_pst.setFloat(_index, ((Float) _obj));
		} else if (_obj instanceof Long) {
			_pst.setLong(_index, ((Long) _obj));
		} else if (_obj instanceof Boolean) {
			_pst.setBoolean(_index, ((Boolean) _obj));
		} else if (_obj instanceof java.util.Date) {
			_pst.setDate(_index, new java.sql.Date(((java.util.Date) _obj).getTime()));
		}else if (_obj instanceof BigDecimal) {
			_pst.setBigDecimal(_index, (BigDecimal) _obj);
		} else if (_obj instanceof Blob) {
			_pst.setBlob(_index, (Blob) _obj);
		} else if (_obj instanceof Clob) {
			_pst.setClob(_index, (Clob) _obj);
		} else {
			_pst.setObject(_index, _obj);
		}
	}

	/*********************************根据Result返回List<Record>结果集,可定位***************/
	public static List<Record<String, Object>> getListByRS(ResultSet _rs, int _startIndex, int _count) throws Exception {
		try {
			List<Record<String, Object>> list = new ArrayList<Record<String, Object>>();

			if ((_startIndex < 0) || (_startIndex > 1)) {
				_rs.absolute(_startIndex-1);
			}
			ResultSetMetaData rsmd = _rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			int iCount = 0;
			while (_rs.next()) {
				iCount++;

				if ((_count > 0) && (iCount > _count)) {
					break;
				}
				Record<String, Object> rec = new Record<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rec.put(rsmd.getColumnName(i).toLowerCase(), _rs.getObject(i));
				}
				list.add(rec);
			}
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			close(_rs);
		}
	}

	/*********************************根据条件和字段名称取字段值***************/

    public static String getStringBySql(Connection _conn, String _sql,String  _default) throws Exception{
        ResultSet res = null;
        String str = _default;
        try {
            res = execSelect(_conn, _sql);
            if (res != null && res.next()) {
                str = res.getString(1);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(res);
        }
        return str;
    }

    public static String getStringBySql(String _sql, String _default) throws Exception {
        Connection con = getConnection();
        String str = getStringBySql(con, _sql, _default);
        closeThreadConnection(con);
        return str;
    }


	@Deprecated
	public static String getStringByValue(String _table, String _getField, String _field, Object _value, String _defaultValue) throws Exception {
		Connection con = getConnection();
		String name = getStringByValue(con, _table, _getField, _field, _value, _defaultValue);
		closeThreadConnection(con);
		return name;
	}

	@Deprecated
	public static String getStringByValue(Connection _conn, String _table, String _getField, String _field, Object _value, String _defaultValue) throws Exception {
		ResultSet rs = null;
        String name = _defaultValue;
		try {
			String sql = "select " + _getField + " from " + _table + " where " + _field + "=?";
			rs = execPrepSelect(_conn, sql, _value);
			if ((rs != null) && (rs.next())) {
				name = rs.getString(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			close(rs);
		}
        return name;
	}

	@Deprecated
	public static String getStringByValues(String _table, String _getField, String[] _field, Object[] _value, String _defaultValue) throws Exception {
		Connection con = getConnection();
		String name = getStringByValues(con, _table, _getField, _field, _value, _defaultValue);
		closeThreadConnection(con);
		return name;
	}

	@Deprecated
	public static String getStringByValues(Connection _conn, String _table, String _getField, String[] _field, Object[] _value, String _defaultValue) throws Exception {
		ResultSet rs = null;
		try {
			String name = _defaultValue;
			String sql = "select " + _getField + " from " + _table + " where ";
			for (int i = 0; i < _field.length; i++) {
				sql = sql + _field[i] + " = ? and ";
			}
			sql = sql.substring(0, sql.length() - 4);

			rs = execPrepSelect(_conn, sql, _value);
			if ((rs != null) && (rs.next())) {
				name = rs.getString(1);
			}
			return name;
		} catch (Exception e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	/******************************* sql 返回对象 bean *******************************************/
	public static <T> T getBean(String _sql, Class<T> _bean, Object... _values) throws Exception {
		Connection con = getConnection();
		T obj = getBean(con, _sql, _bean, _values); //
		closeThreadConnection(con);
		return obj;
	}

	public static <T> T getBean(String _sql, Class<T> _bean, int _startIndex) throws Exception {
		Connection con = getConnection();
		T obj = getBean(con, _sql, _bean, _startIndex);
		closeThreadConnection(con);
		return obj;
	}

	public static <T> T getBean(Connection _conn, String _sql, Class<T> _bean, Object... _values) throws Exception {
		List<T> list = getBeanObjects(_conn, _sql, _bean, true, 0, 1, _values);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return list.get(0);
	}

	public static <T> T getBean(Connection _conn, String _sql, Class<T> _bean, int _startIndex) throws Exception {
		List<T> list = getBeanObjects(_conn, _sql, _bean, true, _startIndex, 1);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return list.get(0);
	}

	/********************************* sql 返回对象 beans ************************************/
	public static <T> List<T> getBeans(String _sql, Class<T> _bean, Object... _values) throws Exception {
		Connection conn = getConnection();
		List<T> list = getBeans(conn, _sql, _bean, _values);
		closeThreadConnection(conn);
		return list;
	}

	public static <T> List<T> getBeans(String _sql, Class<T> _bean, int _startIndex, int _count) throws Exception {
		Connection conn = getConnection();
		List<T> list = getBeans(conn, _sql, _bean, _startIndex, _count);
		closeThreadConnection(conn);
		return list;
	}

	public static <T> List<T> getBeans(Connection _conn, String _sql, Class<T> _bean, Object... _values) throws Exception {
		return getBeanObjects(_conn, _sql, _bean, false, 0, 0,_values);
	}

	public static <T> List<T> getBeans(Connection _conn, String _sql, Class<T> _bean, int _startIndex, int _count) throws Exception {
		return getBeanObjects(_conn, _sql, _bean, false, _startIndex, _count);
	}

	private static <T> List<T> getBeanObjects(Connection _conn, String _sql, Class<T> _bean, boolean _first, int _startIndex, int _count, Object... _values) throws Exception {
		List<T> list = new ArrayList<T>();

		if (_first) {
			Record<String, Object> rec = getRecordPrep(_conn, _sql, _startIndex, _values); //
			if ((rec == null) || (rec.size() == 0)) {
				list.add(null);
			} else {
				T bean = ObjectToBean(rec, _bean);
				list.add(bean);
			}
		} else {
			List<Record<String, Object>> data = getRecordsPrep(_conn, _sql, _startIndex, _count, _values);
			for (int i = 0; i < data.size(); i++) {
				T bean = ObjectToBean(data.get(i), _bean);
				list.add(bean);
			}
		}
		return list;
	}

	/********************************* 对象转换成bean(对象支持Record 和 Map) ************************************/
	@SuppressWarnings("unchecked")
	public static <T> T ObjectToBean(Object _obj, Class<T> _bean) throws Exception {
		String className = _bean.getName() + "$set";
		Class<?> beanClass = null;
		try {
			beanClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (Exception localException) {
		}
		if (beanClass == null) {
			beanClass = BeanSet.getBeanSetClass(_bean, className).toClass();
		}
		BeanSetIf bi = (BeanSetIf) beanClass.newInstance();
		bi.setTarget(_bean.newInstance());
		if (_obj instanceof Record<?, ?>) {
			bi.setValue((Record<?, ?>) _obj);
		} else if (_obj instanceof Map<?, ?>) {
			Map<String, Object> map = (Map<String, Object>) _obj;
			Record<String, Object> rec = new Record<String, Object>(map);
			bi.setValue(rec);
		}
		return _bean.cast(bi.getTarget()) ;
	}

	/********************************* 按对象插入数据库 (对象支持Record,Map,Bean) ***************/
	public static int insertDB(Object _obj, String _tableName) throws Exception {
		Connection con = getConnection();
		int i = insertDB(con, _obj, _tableName);
		closeThreadConnection(con);
		return i;
	}

	public static int insertDB(Connection _conn, Object _obj, String _tableName) throws Exception {
		String sql = getInsertStr(_obj, _tableName);
		int i = 0;
		if ((sql != null) && (sql.length() > 0))
			i = exec(_conn, sql);
		else {
			i = -1;
		}
		return i;
	}

	/********************************* 按对象修改数据库 (对象支持Record,Map,Bean) ***************/
	public static int updateDB(Object _obj, String _tableName, String _where) throws Exception {
		Connection con = getConnection();
		int i = updateDB(con, _obj, _tableName, _where);
		closeThreadConnection(con);
		return i;
	}

	public static int updateDB(Connection _conn, Object _obj, String _tableName, String _where) throws Exception {
		String sql = getUpdateStr(_obj, _tableName, _where);
		int i = 0;
		if ((sql != null) && (sql.length() > 0))
			i = exec(_conn, sql);
		else {
			return -1;
		}
		return i;
	}

	/********************************* 按对象插入修改数据库 (有数据修改:没数据插入,对象支持Record,Map,Bean) ***************/
	public static int insertOrUpdateDB(Object _obj, String _tableName, String _where) throws Exception {
		Connection con = getConnection();
		int i = insertOrUpdateDB(con, _obj, _tableName, _where);
		closeThreadConnection(con);
		return i;
	}

	public static int insertOrUpdateDB(Connection _conn, Object _obj, String _tableName, String _where) throws Exception {
		String sql = "SELECT 1 FROM " + _tableName +" WHERE " + _where;
		ResultSet res= execSelect(_conn, sql);
		if (!res.next()) {
			return insertDB(_conn, _obj, _tableName);
		}
		return updateDB(_conn, _obj, _tableName, _where);
	}

	/*********************************按对象得到updateSQL语句***************/
	@SuppressWarnings("unchecked")
	private static String getUpdateBody(Object _obj) throws Exception {
		String update = "";
		if (_obj instanceof Map<?, ?>) {
			Map<String, Object> map = ((Map<String, Object>) _obj);
			update = getUpdateBody(map);
		} else if (_obj instanceof Record<?, ?>) {
			LinkedHashMap<String, Object> linkedMap = ((Record<String, Object>) _obj).recMap;
			Map<String, Object> map = linkedMap;
			update = getUpdateBody(map);
		} else {
			Class<?> bean = _obj.getClass();

			String className = bean.getName() + "$get";
			Class<?> beanClass = null;
			try {
				beanClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (Exception localException) {
			}
			if (beanClass == null) {
				beanClass = BeanGet.getBeanGetClass(bean, className).toClass();
			}
			BeanGetIf bi = (BeanGetIf) beanClass.newInstance();
			bi.setTarget(_obj);
			update = bi.getUpdateString();

		}
		return update;
	}

	private static String getUpdateBody(Map<String, Object> _map) {
		String update = "";
		Iterator<String> ite = _map.keySet().iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			Object obj = _map.get(key);
			if (update.length() > 0) {
				update = update + ", ";
			}
			if (obj instanceof String) {
				update = update + key + " = '" + _map.get(key).toString().replaceAll("'", "''") + "'";
			}else if(obj instanceof Timestamp){
                update = update + key + " = '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_map.get(key)) + "'";
            }else if (obj instanceof Date) {
				update = update + key + " = '" + new SimpleDateFormat("yyyy-MM-dd").format(_map.get(key)) + "'";
			} else{
				update = update + key + " = " + _map.get(key);
			}
		}
		return update;
	}

	private static String getUpdateStr(Object _obj, String _tableName, String _where) throws Exception {
		String update = getUpdateBody(_obj);
		return "UPDATE " + _tableName + " SET " + update + " WHERE " + _where;
	}

	/*********************************按对象得到InsertSQL语句***************/
	private static String getInsertStr(Object _obj, String _tableName) throws Exception {
		String insert = getInsertBody(_obj);
		return "INSERT INTO " + _tableName + " " + insert;
	}

	@SuppressWarnings("unchecked")
	private static String getInsertBody(Object _obj) throws Exception {
		String insert = "";
		if (_obj instanceof Record<?, ?>) { // _obj.getClass().getName().equalsIgnoreCase("webFrame.util.Record")
			Map<String, Object> map =  ((Record<String, Object>) _obj).recMap;
			insert = getInsertBody(map);
		} else if (_obj instanceof Map<?, ?>) {
			insert = getInsertBody((Map<String, Object>) _obj);
		} else {
			Class<?> bean = _obj.getClass();

			String className = bean.getName() + "$get";
			Class<?> beanClass = null;
			try {
				beanClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (Exception localException) {
			}
			if (beanClass == null) {
				beanClass = BeanGet.getBeanGetClass(bean, className).toClass();
			}
			BeanGetIf bi = (BeanGetIf) beanClass.newInstance();
			bi.setTarget(_obj);
			insert = bi.getInsertString();
		}
		return insert;
	}

	private static String getInsertBody(Map<String, Object> map) {
		String field = "";
		String value = "";
		Iterator<String> ite = map.keySet().iterator();
		while (ite.hasNext()) { 
			String key = ite.next();
			Object obj = map.get(key);
			if (field.length() > 0) {
				field +=", ";
				value += ", ";
			}
			field += key;
			if (obj instanceof String){
               value = value + "'" + obj.toString().replaceAll("'", "''") + "'";
            }else if(obj instanceof Timestamp){
                value = value + "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj) + "'";
            }else if (obj instanceof Date)
				value = value + "'" + new SimpleDateFormat("yyyy-MM-dd").format(obj) + "'";
			else {
				value = value + map.get(key);
			}

		}
		return "(" + field + ") VALUES (" + value + ")";
	}

	/* DB2-利用sql查询数据的分页 */
	public static ResultSet execSelectSplit_DB2(Connection _conn, String _sql, int _startIndex, int _count) throws Exception {
		try {
			Statement state = _conn.createStatement(1004, 1007);
			if (_count > 0) {
				_sql = "select a.* from (select rownumber() over()  rownum, t.* from (" + _sql + ") t) a where a.rownum between " + _startIndex + " and " + (_startIndex + _count - 1);
			}
			Log.writeLog_SQL("execSelectSplit_DB2: "+ _sql);
			return state.executeQuery(_sql);
		} catch (Exception e) {
			Log.writeLog("DBUtils.execSelect(sql=" + _sql + ")", e);
			throw e;
		}
	}

	/* msyql-利用sql查询数据的分页 */
	public static ResultSet execSelectSplit_mysql(Connection _conn, String _sql, int _startIndex, int _count) throws Exception {
		try {
			Statement state = _conn.createStatement(1004, 1007);
			if (_count > 0) { // 2 2
				_sql = "select * from (" + _sql + ")a limit " + _startIndex + ", " + _count;
			}
			Log.writeLog_SQL("execSelectSplit_mysql: "+ _sql);
			return state.executeQuery(_sql);
		} catch (Exception e) {
			Log.writeLog("DBUtils.execSelect(sql=" + _sql + ")", e);
			throw e;
		}
	}

	/* oracle-利用sql查询数据的分页 */
	public static ResultSet execSelectSplit_ORA(Connection _conn, String _sql, int _startIndex, int _count) throws Exception {
		try {
			Statement state = _conn.createStatement(1004, 1007);
			if (_count > 0) { //
				_sql = "select * from (select rownum rw,a.* from (" + _sql + ")a)t where t.rw between " + _startIndex + " and " + (_startIndex + _count - 1);
			}
			Log.writeLog_SQL("execSelectSplit_ORA: "+_sql);
			return state.executeQuery(_sql);
		} catch (Exception e) {
			Log.writeLog("DBUtils.execSelect(sql=" + _sql + ")", e);
			throw e;
		}
	}

	/** 根据sql返回ResultSet(无预编译) if(!res.next)判断没有数据,
     * 取数据时无需判断(会向下位移少取数据),判断后需要用do{}while()
     * 而iterator hasnext后 需要next向下位移
     */
	public static ResultSet execSelect(Connection _conn, String _sql) throws Exception {
		try {
			Statement state = _conn.createStatement(1004, 1007);
			Log.writeLog_SQL("execSelect: "+_sql);
			return state.executeQuery(_sql);
		} catch (Exception e) {
			Log.writeLog("DBUtils.execSelect(sql=" + _sql + ")", e);
			throw e;
		}
	}

	/*********************************根据sql返回ResultSet(有参数,可预编译)***************/
	private static ResultSet execPrepSelect(Connection _conn, String _sql, Object... _values) throws Exception {
		try {
			PreparedStatement pst = _conn.prepareStatement(_sql);
			if (_values != null) {
				for (int i = 0; i < _values.length; i++) {
					setPSTValue(pst, i + 1, _values[i]);
				}
			}
			Log.writeLog_SQL("execPrepSelect: "+_sql);
			return pst.executeQuery();
		} catch (Exception e) {
			Log.writeLog("DBUtils.execPrepSelect(sql=" + _sql + ")", e);
			throw e;
		}
	}

	/*********************************执行SQL语句***************/
	public static int exec(String _sql) throws Exception {
		Connection con = getConnection();
		int i = exec(con, _sql);
		closeThreadConnection(con);
		return i;
	}

	public static int exec(Connection _conn, String _sql) throws Exception {
		Statement state = null;
		try {
			state = _conn.createStatement();
			Log.writeLog_SQL("exec: "+_sql);
			return state.executeUpdate(_sql);
		} catch (Exception e) {
			Log.writeLog("DBUtils.exec(sql=" + _sql + ")", e);
			throw e;
		} finally {
			close(state);
		}
	}

	/*********************************执行SQL语句,有参数可预编译***************/
	public static int execPrep(String _sql, Object... _values) throws Exception {
		Connection con = getConnection();
		int i = execPrep(con, _sql, _values);
		closeThreadConnection(con);
		return i;
	}

	public static int execPrep(Connection _conn, String _sql, Object... _values) throws Exception {
		PreparedStatement pst = null;
		try {
			pst = _conn.prepareStatement(_sql);
			if (_values != null) {
				for (int i = 0; i < _values.length; i++) {
					setPSTValue(pst, i + 1, _values[i]);
				}
			}
			Log.writeLog_SQL("execPrep: "+ _sql);
			return pst.executeUpdate();
		} catch (Exception e) {
			Log.writeLog("DBUtils.execPrep(sql=" + _sql + ")", e);
			throw e;
		} finally {
			close(pst);
		}
	}

	/*********************************查询SQL语句,返回Record结果集(无预编译)***************/
	public static Record<String, Object> getRecord(String _sql) throws Exception {
		Connection con = getConnection();
		Record<String, Object> rec = getRecord(con, _sql);
		closeThreadConnection(con);
		return rec;
	}

	public static Record<String, Object> getRecord(String _sql, int _startIndex) throws Exception {
		Connection con = getConnection();
		Record<String, Object> rec = getRecord(con, _sql, _startIndex);
		closeThreadConnection(con);
		return rec;
	}

	public static Record<String, Object> getRecord(Connection _conn, String _sql) throws Exception {
		return getRecord(_conn, _sql, 0);
	}

	public static Record<String, Object> getRecord(Connection _conn, String _sql, int _startIndex) throws Exception {
		List<Record<String, Object>> list = getListByRS(execSelect(_conn, _sql), _startIndex, 1);

		if ((list == null) || (list.size() == 0))
			return null;
		return  list.get(0);
	}


	/*********************************查询SQL语句,返回List<Record>结果集(无预编译)***************/
	public static List<Record<String, Object>> getRecords(String _sql) throws Exception {
		Connection con = getConnection();
		List<Record<String, Object>> list = getRecords(con, _sql);
		closeThreadConnection(con);
		return list;
	}

	public static List<Record<String, Object>> getRecords(String _sql, int _startIndex, int _count) throws Exception {
		Connection con = getConnection();
		List<Record<String, Object>> list = getRecords(con, _sql, _startIndex, _count);
		closeThreadConnection(con);
		return list;
	}

	public static List<Record<String, Object>> getRecords(Connection _conn, String _sql) throws Exception {
		return getListByRS(execSelect(_conn, _sql), 0, 0);
	}

	public static List<Record<String, Object>> getRecords(Connection _conn, String _sql, int _startIndex, int _count) throws Exception {
		return getListByRS(execSelect(_conn, _sql), _startIndex, _count);
	}


	/*********************************查询SQL语句,返回Record结果集(有参数,可预编译)***************/
	public static Record<String, Object> getRecordPrep(String _sql, Object... _values) throws Exception {
		Connection con = getConnection();
		Record<String, Object> rec = getRecordPrep(con, _sql, _values);
		closeThreadConnection(con);
		return rec;
	}

	public static Record<String, Object> getRecordPrep(Connection _con, String _sql, Object... _values) throws Exception {
		return getRecordPrep(_con, _sql, 0, _values);

	}
	public static Record<String, Object> getRecordPrep(Connection _conn, String _sql, int _startIndex, Object... _values) throws Exception {
		List<Record<String, Object>> list = getListByRS(execPrepSelect(_conn, _sql, _values), _startIndex, 1);

		if ((list == null) || (list.size() == 0))
			return null;
		return (Record<String, Object>) list.get(0);
	}

	/*********************************查询SQL语句,返回List<Record>结果集(有参数,可预编译)***************/
	public static List<Record<String, Object>> getRecordsPrep(String _sql, Object... _values) throws Exception {
		Connection con = getConnection();
		List<Record<String, Object>> list = getRecordsPrep(con, _sql, _values);
		closeThreadConnection(con);
		return list;
	}

	public static List<Record<String, Object>> getRecordsPrep(Connection _conn, String _sql, Object... _values) throws Exception {
		return getRecordsPrep(_conn, _sql, 0, 0, _values);
	}

	public static List<Record<String, Object>> getRecordsPrep(Connection _conn, String _sql, int _startIndex, int _count, Object... _values) throws Exception {
		return getListByRS(execPrepSelect(_conn, _sql, _values), _startIndex, _count);
	}


	/***********************存储过程调用 *****************************************/
	public static ProcedureResult executeProcedure(final String sql, Object... paramArgs) throws Exception {
		Connection conn = getConnection();
		ProcedureResult proResult =  executeProcedure(conn,sql,paramArgs);
		closeThreadConnection(conn);
		return proResult;
	}

	/**
	 * ProcedureResult cr = executeProcedure("{call getSoft(?,name{string})}" ,"183" );
	 *	List<Map<String,Object>> list = cr.getDataSet(0);	//返回结果集,返回参数都为大写,map.get("ID")
	 *	System.out.println(cr.get("NAME"));	// out参数获取,返回参数都为大写
	 * @param conn
	 * @param sql
	 * @param paramArgs
	 * @return
	 * @throws SQLException
	 */
	public static ProcedureResult executeProcedure(Connection conn, final String sql, Object... paramArgs) throws Exception {

		return execProcedure(conn,sql, new ProcedureCallback() {

			/*覆写paser(sql),解析出正确的sql*/
			@Override
			public String parse(final String sql) {
				String callstr = StringUtils.lowerCase(sql);

				String nameHead = StringUtils.substringBefore(callstr, "(");
				String paramterBody = StringUtils.substringBetween(callstr, "(", ")");

				StringBuffer callBuffer = new StringBuffer(50);
				if (!StringUtils.contains(sql, "call")) {
					callBuffer.append("{ call ");
				}
				callBuffer.append(nameHead);
				if (paramterBody != null) {
					callBuffer.append(" (");
					String[] paramterArray = StringUtils.split(paramterBody, ",");
					for (int i = 0; i < paramterArray.length; i++) {
						String p = StringUtils.trim(paramterArray[i]);
						if (StringUtils.isNotBlank(p)) {
							if (StringUtils.equals(p, "?")) { // 输入参数
								registerInParameter(); //初始化输入参数
							} else { // 输出出参数
								String paramname = StringUtils.trim(StringUtils.substringBefore(p, "{"));
								String paramtype = StringUtils.trim(StringUtils.substringBetween(p, "{", "}"));

								paramname = StringUtils.defaultIfEmpty(paramname, "outvar");
								paramtype = StringUtils.defaultIfEmpty(paramtype, "string");

								registerOutParameter(paramtype, paramname); //初始化输出参数

								paramterArray[i] = "?";
							}
						}
					}
					callBuffer.append(StringUtils.join(paramterArray, ","));
					callBuffer.append(")");
				}
				callBuffer.append("}");
				return callBuffer.toString();
			}
		}, paramArgs);
	}

	protected static ProcedureResult execProcedure(Connection con, String sql, ProcedureCallback callback, Object... paramArgs) throws SQLException {
		ProcedureResult result = null;
		if (callback == null) { //复写parse(sql)可重写,sql表现形式可自己控制
			Log.writeLog("DBUtils.execProcedure: Callback object must not be null"  );
			throw new RuntimeException("Callback object must not be null");
		}
		try {
			result = callback.doInCallback(con, sql, paramArgs);
		} catch (SQLException e) {
			Log.writeLog("DBUtils.execProcedure: " + e.getMessage());
			throw e;
		} finally {
		}
		Log.writeLog_SQL("execProcedure: " + sql);
		return result;
	}


	/***********************以下都为connection关闭操作*****************************************/
	public static void close(ResultSet _rs) {
		if(_rs!=null)
		try {
			_rs.close();
		} catch (Exception localException) {
		}
	}

	public static void close(Statement _state) {
		if(_state!=null)
		try {
			_state.close();
		} catch (Exception localException) {
		}
	}

	public static void closeThreadConnection(Connection _conn) {
		try {
			if (getThreadConnection() != _conn)
				closeConnection(_conn);
		} catch (Exception localException) {
		}
	}

	public static void closeConnection(Connection _conn) {
		if(_conn!=null)
		try {
			_conn.close();
		} catch (Exception localException) {
		}
	}

	public static void closeThreadConnection(ResultSet _rs) {
		if(_rs==null) return;
		try {
			Statement state = _rs.getStatement();
			Connection conn = _rs.getStatement().getConnection();
			_rs.close();
			state.close();
			if (getThreadConnection() != conn)
				conn.close();
		} catch (Exception localException) {
		}
	}

	public static void closeConnection(ResultSet _rs) {
		if(_rs==null) return;
		try {
			Statement state = _rs.getStatement();
			Connection conn = _rs.getStatement().getConnection();
			_rs.close();
			state.close();
			conn.close();
		} catch (Exception localException) {
		}
	}

	public static void closeThreadConnection(Statement _state) {
		if(_state==null) return ;
		try {
			Connection conn = _state.getConnection();
			_state.close();
			if (getThreadConnection() != conn)
				conn.close();
		} catch (Exception localException) {
		}
	}

	public static void closeConnection(Statement _state) {
		if(_state==null) return;
		try {
			Connection conn = _state.getConnection();
			_state.close();
			conn.close();
		} catch (Exception localException) {
		}
	}

	public static void rollbackConnection(Connection _conn) {
		if(_conn!=null)
		try {
			_conn.rollback();
		} catch (Exception localException) {
		}
	}
}
