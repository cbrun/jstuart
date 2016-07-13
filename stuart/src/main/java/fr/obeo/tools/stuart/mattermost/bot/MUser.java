package fr.obeo.tools.stuart.mattermost.bot;

public class MUser {
	String email;

	String firstName;

	String id;

	String lastName;

	String teamId;

	String username;

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}