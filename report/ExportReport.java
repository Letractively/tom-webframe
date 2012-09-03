package webFrame.report;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import webFrame.app.control.AppControl;
import webFrame.app.control.RequestContext;
import webFrame.report.Excel;
import webFrame.report.SuperCsv.user;
import webFrame.util.FileUtils;

public final class ExportReport<T> extends AppControl<T> {
    public List<Object> excelList = new ArrayList<Object>();

    String split_subtitle = "\\^\\#";   //^#   字段标题分隔符
    String split_end = "\\&\\&\\&";     //&&&  数据分隔符
    String split = "\\^\\|";            //^| 所有数据普通分割符

    
    /**
     * 导出Excel
     */
    @Override
    public String exec(RequestContext resContext, T t) throws Exception {
        HttpServletResponse res = resContext.getResponse();
        Map<String, String> map = (Map<String, String>) t;

        String header = map.get("header"); //头标题
        String subheader = map.get("subheader");  //副标题
        String titles = map.get("title");  // 字段栏
        String detail = map.get("detail"); //内容
        String msg = setExcelData(titles, detail);
        if ((msg == null) || (msg.length() > 0)) {
            resContext.alertMsg(msg, "");
            return null;
        }

        OutputStream os = res.getOutputStream();
        res.reset();

        String displayName = URLEncoder.encode(header+".xls", "UTF-8");
        res.setHeader("Content-disposition", "attachment; fileName=" + displayName);
        res.setContentType("application/vnd.ms-excel;charset=UTF-8");

        /*创建excel*/
        Excel excel = new Excel();
        excel.createExcel(os, this.excelList, header, subheader);
        return null;

    }

    /**
     * @param excelList
     * @param dataList
     * @param title
     */
    private void addExcelData(List<Object> excelList, List<String[]> dataList, String[] title) {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("title", title);
        hm.put("data", dataList);
        excelList.add(hm);
    }


    public String setExcelData(String titles, String detail)
            throws Exception {
        if (detail == null || detail.isEmpty()) {
            return "没有数据导出";
        }


        List<String[]> dataList = new ArrayList<String[]>();

        String[] table = detail.split(split_end);
        for (int i = 0; i < table.length; i++) {   //循环数据
            String rows = table[i];
            String[] row = rows.split(split);
            dataList.add(row);
        }

        /*解析标题*/
        String title[] = titles.split(split_subtitle); //{title + "^#" + subtitle}

        for (int i = 0; i < title.length; i++) {
            String t[] = title[i].split(split);
            if (i == title.length - 1) {
                addExcelData(this.excelList, dataList, t); //字段标题 + 数据
            } else {
                addExcelData(this.excelList, null, t); // 只有字段标题
            }
        }

        if ((this.excelList == null) || (this.excelList.size() == 0)) {
            return "没有数据导出!";
        }
        return "";
    }
    
    
    
    
    public String exportCsv (RequestContext resContext) throws Exception {
    	 String displayName = URLEncoder.encode("你哈珀.csv", "UTF-8");
    	 resContext.setHeader("Content-disposition", "attachment; fileName=" + displayName);
    	 resContext.setContentType("application/vnd.ms-excel;charset=UTF-8");
    	 List list = new ArrayList<Object>();
        String[] a = { "aa", "bb", "cc", "dd", "ee" };
 		for (int j = 0; j < 10000; j++) {

 			Map<String, Object> map = new HashMap<String, Object>();
            map.put("aa","aa");
            map.put("bb","bb");
            map.put("cc","cc");
            map.put("dd","dd");
            map.put("ee","ee");

			list.add(map); // Arrays.asList(a)
		}
    	 OutputStream out = resContext.getOutputStream(); //()
        SuperCsv.csvMapToOutStream(out,list, new String[] { "aa", "bb", "cc", "dd", "ee" });
    	 return null;
    }

}

