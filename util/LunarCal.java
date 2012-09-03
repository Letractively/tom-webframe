package webFrame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LunarCal {
	HashMap<String, String> hashmap = new HashMap();

	String lunarDate = "";
	private int year;
	private int month;
	private int day;
	private boolean leap;
	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	int[] monthlyDate = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private static long[] lunarInfo = { 19416L, 19168L, 42352L, 21717L, 53856L, 55632L, 91476L, 22176L, 39632L, 21970L,
			19168L, 42422L, 42192L, 53840L, 119381L, 46400L, 54944L, 44450L, 38320L, 84343L, 18800L, 42160L, 46261L,
			27216L, 27968L, 109396L, 11104L, 38256L, 21234L, 18800L, 25958L, 54432L, 59984L, 28309L, 23248L, 11104L,
			100067L, 37600L, 116951L, 51536L, 54432L, 120998L, 46416L, 22176L, 107956L, 9680L, 37584L, 53938L, 43344L,
			46423L, 27808L, 46416L, 86869L, 19872L, 42448L, 83315L, 21200L, 43432L, 59728L, 27296L, 44710L, 43856L,
			19296L, 43748L, 42352L, 21088L, 62051L, 55632L, 23383L, 22176L, 38608L, 19925L, 19152L, 42192L, 54484L,
			53840L, 54616L, 46400L, 46496L, 103846L, 38320L, 18864L, 43380L, 42160L, 45690L, 27216L, 27968L, 44870L,
			43872L, 38256L, 19189L, 18800L, 25776L, 29859L, 59984L, 27480L, 21952L, 43872L, 38613L, 37600L, 51552L,
			55636L, 54432L, 55888L, 30034L, 22176L, 43959L, 9680L, 37584L, 51893L, 43344L, 46240L, 47780L, 44368L,
			21977L, 19360L, 42416L, 86390L, 21168L, 43312L, 31060L, 27296L, 44368L, 23378L, 19296L, 42726L, 42208L,
			53856L, 60005L, 54576L, 23200L, 30371L, 38608L, 19415L, 19152L, 42192L, 118966L, 53840L, 54560L, 56645L,
			46496L, 22224L, 21938L, 18864L, 42359L, 42160L, 43600L, 111189L, 27936L, 44448L };

	private static boolean isLeapYear(int year) {
		boolean flag = false;
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
			flag = true;
		}
		return flag;
	}

	private static final int yearDays(int y) {
		int sum = 348;
		for (int i = 32768; i > 8; i >>= 1) {
			if ((lunarInfo[(y - 1900)] & i) != 0L)
				sum++;
		}
		return sum + leapDays(y);
	}

	private static final int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[(y - 1900)] & 0x10000) != 0L) {
				return 30;
			}
			return 29;
		}
		return 0;
	}

	private static final int leapMonth(int y) {
		return (int) (lunarInfo[(y - 1900)] & 0xF);
	}

	private static final int monthDays(int y, int m) {
		if ((lunarInfo[(y - 1900)] & 65536 >> m) == 0L) {
			return 29;
		}
		return 30;
	}

	private void hashGen(int y) throws Exception {
		if (isLeapYear(y)) {
			this.monthlyDate[1] = 29;
		}

		for (int i = 1; i <= 12; i++) {
			for (int j = 1; j <= this.monthlyDate[(i - 1)]; j++) {
				Calendar today = Calendar.getInstance();
				String ymd = String.valueOf(y) + "-" + String.valueOf(i) + "-" + String.valueOf(j);

				today.setTime(chineseDateFormat.parse(ymd));
				sun2Lunar(today);

				String nularYmd = String.valueOf(this.year) + "-" + String.valueOf(this.month) + "-"
						+ String.valueOf(this.day);

				put(nularYmd, ymd);
			}

		}

		y++;
		if (isLeapYear(y)) {
			this.monthlyDate[1] = 29;
		}

		for (int i = 1; i <= 3; i++)
			for (int j = 1; j <= this.monthlyDate[(i - 1)]; j++) {
				Calendar today = Calendar.getInstance();
				String ymd = String.valueOf(y) + "-" + String.valueOf(i) + "-" + String.valueOf(j);

				today.setTime(chineseDateFormat.parse(ymd));
				sun2Lunar(today);

				String nularYmd = String.valueOf(this.year) + "-" + String.valueOf(this.month) + "-"
						+ String.valueOf(this.day);

				put(nularYmd, ymd);
			}
	}

	private void sun2Lunar(Calendar cal) {
		int leapMonth = 0;
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse("1900-1-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		int monCyl = 14;

		int daysOfYear = 0;
		int iYear = 1900;
		for (; (iYear < 2050) && (offset > 0); iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
			monCyl += 12;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
			monCyl -= 12;
		}

		this.year = iYear;

		leapMonth = leapMonth(iYear);
		this.leap = false;

		int daysOfMonth = 0;
		int iMonth = 1;
		for (; (iMonth < 13) && (offset > 0); iMonth++) {
			if ((leapMonth > 0) && (iMonth == leapMonth + 1) && (!this.leap)) {
				iMonth--;
				this.leap = true;
				daysOfMonth = leapDays(this.year);
			} else {
				daysOfMonth = monthDays(this.year, iMonth);
			}
			offset -= daysOfMonth;

			if ((this.leap) && (iMonth == leapMonth + 1))
				this.leap = false;
			if (!this.leap) {
				monCyl++;
			}
		}
		if ((offset == 0) && (leapMonth > 0) && (iMonth == leapMonth + 1)) {
			if (this.leap) {
				this.leap = false;
			} else {
				this.leap = true;
				iMonth--;
				monCyl--;
			}
		}

		if (offset < 0) {
			offset += daysOfMonth;
			iMonth--;
			monCyl--;
		}
		this.month = iMonth;
		this.day = (offset + 1);
	}

	private void put(String key, String value) {
		if ((!this.hashmap.containsKey(key)) && (!this.hashmap.containsValue(value)))
			this.hashmap.put(key, value);
	}

	public static String getSun(String _date) throws Exception {
		int y = 0;
		int m = 0;
		int d = 0;
		y = Integer.parseInt(_date.substring(0, 4));
		m = Integer.parseInt(_date.substring(5, 7));
		d = Integer.parseInt(_date.substring(8, 10));

		LunarCal lc = new LunarCal();
		lc.hashGen(y);
		lc.lunarDate = (String.valueOf(y) + "-" + String.valueOf(m) + "-" + String.valueOf(d));
		if ((lc.lunarDate == null) || (lc.lunarDate.trim().equals(""))) {
			return "";
		}
		String s = lc.hashmap.get(lc.lunarDate);
		String[] col = s.split("-");
		col[1] = (col[1].length() == 1 ? "0" + col[1] : col[1]);
		col[2] = (col[2].length() == 1 ? "0" + col[2] : col[2]);
		return col[0] + "-" + col[1] + "-" + col[2];
	}

	public static String getLunar(String _date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(chineseDateFormat.parse(_date));

		LunarCal lc = new LunarCal();
		lc.sun2Lunar(cal);
		return lc.year + "-" + (lc.month < 10 ? "0" + lc.month : Integer.valueOf(lc.month)) + "-"
				+ (lc.day < 10 ? "0" + lc.day : Integer.valueOf(lc.day));
	}
}
