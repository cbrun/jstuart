package fr.obeo.tools.stuart;

import java.util.Calendar;
import java.util.Date;

public class Dates {

	public static Date getDateXDaysAgo(int nbDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -nbDays);
		Date daysAgo = cal.getTime();
		return daysAgo;
	}

}
