package webFrame.report;

import java.io.*;
import java.util.*;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import webFrame.util.FileUtils;

public class SuperCsv {

	/**************************** 读取csv ********************************************/
	@SuppressWarnings("unchecked")
	private static <T> List<T> readCsv(File file, Class<T> _class, CellProcessor[] processer, String[] header) throws Exception {
		T t = _class.newInstance();
		List<T> list = new ArrayList<T>();
		/* 返回List<Map> */
		if (t instanceof Map<?, ?>) {
			CsvMapReader reader = null;
			try {
				reader = new CsvMapReader(new FileReader(file), CsvPreference.EXCEL_PREFERENCE);
				reader.getCSVHeader(true);
				if (processer == null) {
					while ((t = _class.cast(reader.read(header))) != null) {
						list.add(t);
					}
				} else {
					while ((t = _class.cast(reader.read(header, processer))) != null) {
						list.add(t);
					}
				}
			} catch (Exception e) {
				Log.writeLog("SuperCsv.readCvs", e);
			} finally {
				closeReader(reader);
			}
		} else {
			CsvListReader reader = null;
			try {

				reader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_PREFERENCE);
				reader.getCSVHeader(true);
				if (processer == null) {
					while ((t = _class.cast(reader.read())) != null) {
						list.add((T) ((ArrayList) t).clone()); // 必须克隆,read()方法中只用了一个list做引用,没有新建
					}
				} else {
					while ((t = _class.cast(reader.read(processer))) != null) {
						list.add((T) ((ArrayList) t).clone());
					}
				}
			} catch (Exception e) {
				Log.writeLog("SuperCsv.readCvs", e);
			} finally {
				closeReader(reader);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<ArrayList> csvToList(File file, CellProcessor[] processer) throws Exception {
		return readCsv(file, ArrayList.class, processer, null);
	}

	@SuppressWarnings("unchecked")
	public static List<ArrayList> csvToList(File file) throws Exception {
		return readCsv(file, ArrayList.class, null, null);
	}

	@SuppressWarnings("unchecked")
	public static List<HashMap> csvToMap(File file, String[] key, CellProcessor[] processer) throws Exception {
		return readCsv(file, HashMap.class, processer, key);
	}

	@SuppressWarnings("unchecked")
	public static List<HashMap> csvToMap(File file, String[] key) throws Exception {
		return readCsv(file, HashMap.class, null, key);
	}

	public static <T> List<T> csvToBean(File file, Class<T> _class, String[] header, CellProcessor[] processer) throws Exception {
		T t = _class.newInstance();
		List<T> list = new ArrayList<T>();
		CsvBeanReader reader = null;
		try {
			reader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_PREFERENCE);
			reader.getCSVHeader(true);
			if (processer == null) {
				while ((t = _class.cast(reader.read(_class, header))) != null) {
					list.add(t);
				}
			} else {
				while ((t = _class.cast(reader.read(_class, header, processer))) != null) {
					list.add(t);
				}
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.csvToBean", e);
		} finally {
			closeReader(reader);
		}
		return list;
	}

	public static <T> List<T> csvToBean(File file, Class<T> _class, String[] header) throws Exception {
		return csvToBean(file, _class, header, null);
	}

	public static String[] readCsvHead(File file) {
		CsvListReader reader = null;
		try {
			reader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_PREFERENCE);
			return reader.getCSVHeader(true);
		} catch (Exception e) {
			Log.writeLog("SuperCsv.readCvsHead", e);
		} finally {
			closeReader(reader);
		}
		return null;
	}

	/******************************* 转换成 csv ****************************************/
	public static <T> void beanToCsv(File file, List<T> list, String[] key, CellProcessor[] processer) {
		CsvBeanWriter writer = null;
		try {
			writer = new CsvBeanWriter(new FileWriter(file, true), CsvPreference.EXCEL_PREFERENCE);
			for (T t : list) {
				if (processer == null) {
					writer.write(t, key);
				} else {
					writer.write(t, key, processer);
				}
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.beanToCvs", e);
		} finally {
			closeWriter(writer);
		}
	}

	public static <T> void beanToCsv(File file, List<T> list, String[] key) throws Exception {
		beanToCsv(file, list, key, null);
	}

	public static void MaptoCsv(File file, List<Map<String, Object>> list, String[] key, CellProcessor[] processer) {
		CsvMapWriter writer = null;
		try {
			writer = new CsvMapWriter(new FileWriter(file, true), CsvPreference.EXCEL_PREFERENCE);
			for (Map<String, Object> paramMap : list) {
				if (processer == null) {
					writer.write(paramMap, key);
				} else {
					writer.write(paramMap, key, processer);
				}
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.MaptoCsv", e);
		} finally {
			closeWriter(writer);
		}
	}

	public static void MaptoCsv(File file, List<Map<String, Object>> list, String[] key) {
		MaptoCsv(file, list, key, null);
	}

	public static void ListToCsv(File file, List<Object> list, CellProcessor[] processer) {
		CsvListWriter writer = null;
		try {
			writer = new CsvListWriter(new FileWriter(file, true), CsvPreference.EXCEL_PREFERENCE);
			for (Object obj : list) {
				if (processer == null) {
					if (obj instanceof List<?>) {
						writer.write((List<?>) obj);
					} else {
						writer.write((String[]) obj);
					}
				} else {
					if (obj instanceof List<?>) {
						writer.write((List<?>) obj, processer);
					} else {
						writer.write((String[]) obj);
					}
				}
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.listTocsv(List<Object> obj only support List String[])", e);
		} finally {
			closeWriter(writer);
		}
	}

	public static void ListToCsv(File file, List<Object> list) {
		ListToCsv(file, list, null);
	}

	public static void writeHeader(File file, String[] header) {
		CsvListWriter writer = null;
		try {
			writer = new CsvListWriter(new FileWriter(file, true), CsvPreference.EXCEL_PREFERENCE);
			writer.writeHeader(header);
		} catch (Exception e) {
			Log.writeLog("SuperCsv.writeHeader", e);
		} finally {
			closeWriter(writer);
		}
	}

	private static <T> OutputStream csvToOutStream(List<T> list, String[] key) {
		CsvBeanWriter writer = null;
		OutputStream out = new ByteArrayOutputStream();
		try {
			writer = new CsvBeanWriter(new OutputStreamWriter(out), CsvPreference.EXCEL_PREFERENCE);
			for (T t : list) {
				writer.write(t, key);
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.OutputStream", e);
		} finally {
			closeWriter(writer);
		}
		return out;
	}
	
	public static <T> void csvToOutStream(OutputStream out,List<T> list, String[] key){
		CsvBeanWriter writer = null;
		try {
			writer = new CsvBeanWriter(new OutputStreamWriter(out), CsvPreference.EXCEL_PREFERENCE);
			for (T t : list) {
				writer.write(t, key);
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.OutputStream", e);
		} finally {
			closeWriter(writer);
		}
	}
	
	public static void csvMapToOutStream(OutputStream out,List<Map<String, Object>> list, String[] key){
		CsvMapWriter writer = null;
		try {
			writer = new CsvMapWriter(new OutputStreamWriter(out), CsvPreference.EXCEL_PREFERENCE);
			for (Map<String, Object> map : list) {
				writer.write(map, key);
			}
		} catch (Exception e) {
			Log.writeLog("SuperCsv.OutputStream", e);
		} finally {
			closeWriter(writer);
		}
	}


	public static <T> InputStream cvsToInStream(List<T> list, String[] key) {
		OutputStream out = csvToOutStream(list, key);
		ByteArrayInputStream in = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
		FileUtils.close(null, out);
		return in;
	}

	
	public static void closeReader(AbstractCsvReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
	}

	public static void closeWriter(AbstractCsvWriter writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				writer = null;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		long i = System.currentTimeMillis();
		File file = new File("d:\\aa.csv");
		// System.out.println(Arrays.asList(readCsvHead(file)) ); //10W Ok
		// System.out.println(csvToList(file)); ;
		// System.out.println(csvToMap(file,new
		// String[]{"aa","bb","cc","dd","ee"})); ; //10W 超出
		// System.out.println(csvToBean(file,user.class,new
		// String[]{"aa","bb","cc","dd","ee"})); //10w Ok

		List list = new ArrayList<Object>();
		for (int j = 0; j < 1000000; j++) {
			String[] a = { "aa", "bb", "cc", "dd", "ee" }; // 10w 172ms 50w
															// 800ms

			// List<String> l = new ArrayList<String>(); //10w 234ms 50w 1100ms
			// l.add("aa");
			// l.add("aa");
			// l.add("aa");
			// l.add("aa");
			// l.add("aa");

			// Map<String, Object> map = new HashMap<String, Object>(); // 10w
			// 430ms 50w 超出
			// map.put("aa", "aa");
			// map.put("bb", "bb");
			// map.put("cc", "cc");
			// map.put("dd", "dd");
			// map.put("ee", "ee");

			user user = new user("aa", "bb", "cc", "dd", "ee"); // 10w 312ms 50W
																// 1250ms  100w 2500ms

			list.add(user); // Arrays.asList(a)
		}

		writeHeader(file, new String[] { "11", "22", "33", "44", "55" });
		// ListToCsv(file, list);
		// MaptoCsv(file, list, new String[]{"aa","bb","cc","dd","ee"});
		//beanToCsv(file, list, new String[] { "aa", "bb", "cc", "dd", "ee" });
		InputStream in = cvsToInStream(list, new String[] { "aa", "bb", "cc", "dd", "ee" });
		byte[] b = new byte[1024];
		int len= 0;
		OutputStream out = new FileOutputStream(new File("d:\\aa.csv"));
		while((len = in.read(b, 0, b.length))!=-1){
			out.write(b, 0, len);
		}
		in.close();
		out.close();
		System.out.println(System.currentTimeMillis() - i);
	}

	public static class user {
		private String aa;
		private String bb;
		private String cc;
		private String dd;
		private String ee;

		public user() {
		}

		public user(String aa, String bb, String cc, String dd, String ee) {
			super();
			this.aa = aa;
			this.bb = bb;
			this.cc = cc;
			this.dd = dd;
			this.ee = ee;
		}

		public String getAa() {
			return aa;
		}

		public void setAa(String aa) {
			this.aa = aa;
		}

		public String getBb() {
			return bb;
		}

		public void setBb(String bb) {
			this.bb = bb;
		}

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public String getDd() {
			return dd;
		}

		public void setDd(String dd) {
			this.dd = dd;
		}

		public String getEe() {
			return ee;
		}

		public void setEe(String ee) {
			this.ee = ee;
		}
	}

}
