package fr.obeo.tools.stuart;

public interface UserRequest {

	long getNbDaysSinceLastAnswer();

	long getNbMinutesSinceLastAnswer();

	String getSummary();

	String getLastAuthorName();

	String getReporterName();

	String getUrl();

}
