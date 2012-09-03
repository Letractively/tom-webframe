package webFrame.app.service;

import java.sql.Connection;

import webFrame.app.db.DBUtils;
import webFrame.app.db.Record;
import webFrame.app.listener.Variable;


public abstract class AppService {
    private Record<String, Connection> threadRec = new Record<String, Connection>();
    protected Connection conn = null;

    public AppService() {
    }

    protected void initConn() throws Exception {
        Variable.threadMap.put(Thread.currentThread(), threadRec);
        conn = DBUtils.getConnection();
    }

}
