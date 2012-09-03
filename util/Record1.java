package webFrame.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public final class Record1 implements Serializable {
	private static final long serialVersionUID = 1L;
	public LinkedHashMap<String, Object> recMap = null;

	public Record1() {
		this.recMap = new LinkedHashMap<String, Object>();
	}

	public Record1(int _size) {
		this.recMap = new LinkedHashMap<String, Object>(_size);
	}

	public String get(String _key) {
		return this.recMap.get(_key) == null ? "" : this.recMap.get(_key).toString();
	}

	public int getInt(String _key) {
		try {
			return (int) getDouble(_key);
		} catch (Exception e) {
		}
		return 0;
	}

	public double getDouble(String _key) {
		try {
			String s = this.recMap.get(_key) == null ? "" : this.recMap.get(_key).toString();
			if (s.trim().length() == 0) {
				return 0.0D;
			}
			return Double.parseDouble(s);
		} catch (Exception e) {
		}
		return 0.0D;
	}

	public BigDecimal getBigDecimal(String _key) {
		try {
			String s = this.recMap.get(_key) == null ? "" : this.recMap.get(_key).toString();
			if (s.trim().length() == 0) {
				return new BigDecimal("0");
			}
			return new BigDecimal(s);
		} catch (Exception e) {
		}
		return new BigDecimal(0);
	}

	public Date getDate(String _key) {
		try {
			String sDate = this.recMap.get(_key) == null ? "" : this.recMap.get(_key).toString();
			sDate = sDate.replaceAll("-", "");
			return new SimpleDateFormat("yyyyMMdd").parse(sDate);
		} catch (Exception e) {
		}
		return null;
	}

	public Object getObject(Object _key) {
		try {
			return this.recMap.get(_key);
		} catch (Exception e) {
		}
		return null;
	}

	public void put(String _key, Object _value) {
		this.recMap.put(_key, _value);
	}

	public int size() {
		return this.recMap.size();
	}

	public String[] getColumn() {
		Object[] key = this.recMap.keySet().toArray();
		String[] result = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			result[i] = key[i].toString();
		}
		return result;
	}

	public boolean hasColumn(String _key) {
		return this.recMap.containsKey(_key);
	}

	public boolean hasValue(Object _value) {
		return this.recMap.containsValue(_value);
	}

	@Override
	public Record1 clone() {
		Record1 rec = new Record1();
		rec.recMap = (LinkedHashMap<String, Object>) this.recMap.clone();
		return rec;
	}

	public void remove(String _key) {
		this.recMap.remove(_key);
	}
}
