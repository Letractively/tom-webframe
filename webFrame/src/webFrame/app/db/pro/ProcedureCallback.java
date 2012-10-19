package webFrame.app.db.pro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import webFrame.app.db.DBUtils;


public abstract class ProcedureCallback {

	String str_sql = null;

	Map<Integer, Object[]> paramsMap = new LinkedHashMap<Integer, Object[]>();

	public ProcedureCallback() {
	}

	protected void registerInParameter() {
		paramsMap.put(paramsMap.size() + 1, null);
	}

	protected void registerOutParameter(String type, String name) {
		paramsMap.put(paramsMap.size() + 1, new Object[] { name, type });
	}

	public ProcedureResult doInCallback(Connection conn, String sql, Object... objects) throws SQLException {

		this.str_sql = StringUtils.defaultString(parse(sql), sql);  //parse(sql) 方法需要被重写

		CallableStatement cs = null;

		cs = conn.prepareCall(this.str_sql);

		try {

			for (int i = 0; i < paramsMap.size(); i++) {
				Integer key = i + 1;
				Object[] p = paramsMap.get(key);
				if (p == null) {
					if (objects == null || i >= objects.length) {
						throw new IllegalArgumentException("not enought parameter");
					}
					ProceUtils.bindStatementInput(cs, key, objects[i]); //设置输入参数
				} else {
					String type = (String) p[1];
					ProceUtils.bindStatementOutput(cs, key, type); //设置输出参数u
				}
			}

			return getResult(cs, cs.execute());
		} finally {
			DBUtils.close(cs);
		}
	}

	public String parse(String sql) { //解析正确的存储过程需要重写
		return sql;
	}

	protected ProcedureResult getResult(CallableStatement cstmt, boolean executeResult) throws SQLException {

		Map<String, Object> retmap = new LinkedHashMap<String, Object>();

		int index = 0;

		if (executeResult) { // 有结果集返回
			ResultSet rs = null;
			try {
				rs = cstmt.getResultSet();
				retmap.put("DATASET_" + String.valueOf(index++), ProceUtils.convertResultSetToMapList(rs, false));

				while (cstmt.getMoreResults()) {// 还有结果集
					rs = cstmt.getResultSet();
					retmap.put("DATASET_" + String.valueOf(index++), ProceUtils.convertResultSetToMapList(rs, false));

					DBUtils.close(rs);
				}
			} finally {
				DBUtils.close(rs);
			}
		}

		for (int i = 0; i < paramsMap.size(); i++) {
			Integer key = i + 1;
			Object[] p = paramsMap.get(key);
			if (p != null) {
				String name = (String) p[0]; //输出参数名称
				String type = (String) p[1];  //输出参数类型
				retmap.put(name.toUpperCase(), ProceUtils.convertType(cstmt, key, type)); //输出参数如果是Oracle cursor直接转成ResultSet
			}
		}

		return new ProcedureResult(retmap);
	}
}
