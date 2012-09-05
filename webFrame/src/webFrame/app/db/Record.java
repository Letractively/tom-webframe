package webFrame.app.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Record<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    public LinkedHashMap<K, V> recMap = null;

    public Record() {
        this.recMap = new LinkedHashMap<K, V>();
    }

    public Record(int _size) {
        this.recMap = new LinkedHashMap<K, V>(_size);
    }

    public Record(Map<K, V> _map) {
        this.recMap = new LinkedHashMap<K, V>(_map);
    }

    public void put(K _key, V _value) {
        this.recMap.put(_key, _value);
    }

    public V getV(K _key) {
        return this.recMap.get(_key);
    }

    public String get(K _key) {
        try {
            return getV(_key) == null ? "" : getV(_key).toString();
        } catch (Exception e) {
        }
        return "";
    }

    public int getInt(K _key) {
        try {
            return (int) getDouble(_key);
        } catch (Exception e) {
        }
        return 0;
    }

    public double getDouble(K _key) {
        try {
            String s = get(_key);
            if (s.trim().length() == 0) {
                return 0.0D;
            }
            return Double.parseDouble(s);
        } catch (Exception e) {
        }
        return 0.0D;
    }

    public BigDecimal getBigDecimal(K _key) {
        try {
            String s = get(_key);
            if (s.trim().length() == 0) {
                return new BigDecimal("0");
            }
            return new BigDecimal(s);
        } catch (Exception e) {
        }
        return new BigDecimal(0);
    }

      public Date getDate(K _key) {
        Date sDate = null;
        try {
            sDate = new SimpleDateFormat("yyyy-MM-dd").parse(get(_key)) ;  // java.sql.Date
        } catch (Exception e) {
        }
        return sDate;
    }

    public Timestamp getTimestamp(K _key){
         Date sDate = null;
         try {
            sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(get(_key)) ;  // java.sql.Date
        } catch (Exception e) {
        }
        return new Timestamp(sDate!=null ?sDate.getTime(): new Date().getTime());
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

    public boolean hasColumn(K _key) {
        return this.recMap.containsKey(_key);
    }

    public boolean hasValue(K _value) {
        return this.recMap.containsValue(_value);
    }

    @Override
	public Record<K, V> clone() { //clone后操作数据不会影响原对象,避免引用传递带来的影响
        Record<K, V> rec = new Record<K, V>();
        rec.recMap = (LinkedHashMap<K, V>) this.recMap.clone();
        return rec;
    }

    public void remove(K _key) {
        this.recMap.remove(_key);
    }
}
