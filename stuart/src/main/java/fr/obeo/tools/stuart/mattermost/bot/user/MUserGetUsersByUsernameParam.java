package fr.obeo.tools.stuart.mattermost.bot.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MUserGetUsersByUsernameParam {

	private final List<String> usernames;

	public MUserGetUsersByUsernameParam(List<String> usernames) {
		this.usernames = Collections.unmodifiableList(usernames);
	}

	public String asJson() {
		String json = usernames.stream().map(id -> {
			return "\"" + id + "\"";
		}).collect(Collectors.joining(",", "[", "]"));
		return json;
	}

	@Override
	public String toString() {
		return this.usernames.toString();
	}
}
