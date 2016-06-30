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

	public static String prettyDelayFromMinutes(long minutes) {
		if (minutes < 60) {
			return minutes + " min";
		} else if (minutes < 60 * 24) {
			return minutes / 60 + " hrs";
		} else if (minutes < 60 * 24 * 7) {
			return minutes / 1440 + " days";
		} else if (minutes < 60 * 24 * 31) {
			return minutes / (60 * 24 * 7) + " wks";
		} else if (minutes < 60 * 24 * 31 * 12) {
			return minutes / (60 * 24 * 31) + " months";
		} else {
			return minutes / (60 * 24 * 31 * 12) + " years";
		}
	}
}
