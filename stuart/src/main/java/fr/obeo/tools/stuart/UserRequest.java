package fr.obeo.tools.stuart;

public interface UserRequest {

	long getNbDaysSinceLastAnswer();

	String getSummary();

	String getLastAuthorName();

	String getReporterName();

	String getUrl();

}
