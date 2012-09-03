package webFrame.app.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import webFrame.app.listener.Variable;


public class PageController {
	public int totalRowCount = 0; // 总行数
	public int totalPageCount = 0; // 总页数
	public int maxPageRowCount = 100; // 每页最大行数
	public int currPageRowCount = 0; // 当前页行数
	public int currPageNum = 1; // 当前页数
	public int currPageStartRowNum = 1; // 当前页开始行
	public int currPageEndRowNum = 1; // 当前页结束行
	public List<Record<String, Object>> pageList = null; // 分页列表
	public List<Record<String, Object>> totalList = null; // 总记录列表

	private Connection connection = null;
	private String sql = "";

	public PageController(Connection con, String sql) throws Exception {
		if ((con == null) || (sql == null))
			return;
		this.connection = con;
		this.sql = sql;

		this.maxPageRowCount = Variable.maxPageRowCount;
	}

	public PageController(List<Record<String, Object>> list) throws Exception {
		if (list == null)
			return;
		this.totalList = list;
		this.maxPageRowCount = Variable.maxPageRowCount;
	}

	public void setTotalCount() throws Exception {
		setTotalRowCount(); // 设置总行数
		setTotalPageCount(); // 设置总页数
	}

	public List<Record<String, Object>> getPageList(int intPageNum) throws Exception {
		setTotalCount();
		if (!setPage(intPageNum))
			return null;

		this.pageList = getSubList(this.currPageStartRowNum, this.currPageEndRowNum);
		return this.pageList;
	}

	private boolean setPage(int intPageNum) throws Exception {
		if (this.totalRowCount < 1)
			return false;

		if (intPageNum == 0)
			intPageNum = 1;
		else if ((intPageNum > this.totalPageCount) || (intPageNum < 0)) {
			intPageNum = this.totalPageCount;
		}

		this.currPageNum = intPageNum;
		setCurrentPageRowsCount();
		this.currPageStartRowNum = ((intPageNum - 1) * this.maxPageRowCount + 1);
		this.currPageEndRowNum = (this.currPageStartRowNum + this.maxPageRowCount - 1);

		return true;
	}

	private List<Record<String, Object>> getSubList(int startRow, int endRow) throws Exception {
		if (this.sql.length() > 0) { // 真分
			if (DBUtils.isDB2()) {
				return DBUtils.getListByRS(
						DBUtils.execSelectSplit_DB2(this.connection, sql, startRow, this.maxPageRowCount), 0, 0);
			} else if (DBUtils.isMysql()) {
				return DBUtils.getListByRS(
						//mysql limit 索引从0开始,到截取的长度
						DBUtils.execSelectSplit_mysql(this.connection, sql, startRow-1, this.maxPageRowCount), 0, 0);
			} else if (DBUtils.isOracle()) {
				return DBUtils.getListByRS(
						DBUtils.execSelectSplit_ORA(this.connection, sql, startRow, this.maxPageRowCount), 0, 0);
			} else {// 半假分页
				return DBUtils.getRecords(this.connection, this.sql, startRow, endRow - startRow + 1);
			}
		}

		/* 存假分页,拆分list,定位index */
		List<Record<String, Object>> list = new ArrayList<Record<String, Object>>();
		for (int i = startRow - 1; i < endRow; i++) {
			if ((i < 0) || (i >= this.totalList.size()))
				break;
			list.add(this.totalList.get(i));
		}

		this.pageList = list;
		return this.pageList;
	}

	/**
	 * 总行数
	 */
	private void setTotalRowCount() throws Exception {
		if (this.sql.length() > 0) {
			String _sql = "";
			if (this.sql.toLowerCase().indexOf(" with ur") > 0)
				_sql = "select count(*) as cnt from (" + this.sql.replaceAll(" with ur", "") + ") as t with ur";
			else {
				_sql = "select count(*) as cnt from (" + this.sql + ") as t";
			}
			this.totalRowCount = DBUtils.getRecord(this.connection, _sql).getInt("cnt");
		} else {
			this.totalRowCount = this.totalList.size();
		}
	}

	/**
	 * 总页数
	 */
	private void setTotalPageCount() throws Exception {
		if (this.totalRowCount % this.maxPageRowCount > 0)
			this.totalPageCount = (this.totalRowCount / this.maxPageRowCount + 1);
		else
			this.totalPageCount = (this.totalRowCount / this.maxPageRowCount);
	}

	/**
	 * 设置当前页行数
	 */
	private void setCurrentPageRowsCount() {
		if (this.currPageNum < this.totalPageCount) // 当前页小于总页数
			this.currPageRowCount = this.maxPageRowCount; // 当前页总 行数为最大当前页行数
		else
			// 否则当前页行数为:总行数-((总页数-1)*当前最大行数)
			this.currPageRowCount = (this.totalRowCount - (this.totalPageCount - 1) * this.maxPageRowCount);
	}
	
	
	
	
	public static void main(String[] args) {
//		PageController page = new PageController(conn, sql);
//		if (Page.getParameterEc(req, "rowNum").length() > 0) {
//			page.maxPageRowCount = Integer.parseInt(Page.getParameterEc(req, "rowNum"));
//		}
//		int pageNum = Page.getParameterEc(req, "pageNum").length()==0? 1: Integer.parseInt(Page.getParameterEc(req, "pageNum"));
//		List list = page.getPageList(pageNum);
//		Page.setAttribute(req, "page", page);
	}
}
