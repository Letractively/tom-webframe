package webFrame.app.control;

import java.sql.Connection;
import java.util.TimerTask;

import webFrame.app.db.DBUtils;
import webFrame.app.db.Record;
import webFrame.app.listener.Variable;

public abstract class AutoTask extends TimerTask {
	protected boolean hasDB = false;

	protected Connection conn = null;

	protected Record<String, Connection> threadRec = new Record<String, Connection>();

	public AutoTask(boolean hasDB) {
		try {
			Variable.threadMap.put(Thread.currentThread(), this.threadRec);

			if (hasDB)
				this.conn = DBUtils.getConnection();
		} catch (Exception e) {
			try {
				AppUtils.autoException(e, "");
			} catch (Exception localException1) {
			}
		}
	}

	@Override
	public void run() {
		try {
			execTask();
		} catch (Exception e) {
			try {
				AppUtils.autoException(e, "");
			} catch (Exception localException1) {
			}
		} finally {
			if (this.hasDB) {
				DBUtils.closeConnection(this.conn);
				try {
					DBUtils.closeConnection(DBUtils.getConnection());
				} catch (Exception e) {
				}
			}

			this.threadRec.recMap.clear();
			Variable.threadMap.remove(Thread.currentThread());
		}
	}

	protected abstract void execTask() throws Exception;
}
