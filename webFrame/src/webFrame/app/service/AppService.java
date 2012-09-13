package webFrame.app.service;

import java.sql.Connection;

import webFrame.app.db.DBUtils;


public abstract class AppService {
    protected Connection conn = null;

    public AppService() {
    }

    protected void initConn() throws Exception {
        conn = DBUtils.getConnection();
    }

}
