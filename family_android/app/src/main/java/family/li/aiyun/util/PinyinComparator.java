package family.li.aiyun.util;


import family.li.aiyun.bean.CountryCode;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CountryCode> {

	@Override
	public int compare(CountryCode arg0, CountryCode arg1) {
		// TODO Auto-generated method stub
		if (arg0.getLetter().equals("@") || arg1.getLetter().equals("#")) {
			return -1;     
		} else if (arg0.getLetter().equals("#") || arg1.getLetter().equals("@")) {
			return 1;
		} else {
			return arg0.getLetter().compareTo(arg1.getLetter()); // 升序
		//    return arg1.getLetter().compareTo(arg0.getLetter()); // 降序
		}
	}
}
