package fr.obeo.tools.stuart.mattermost.bot.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MUserGetUsersByIdParam {

	private final List<String> userIds;

	public MUserGetUsersByIdParam(List<String> userIds) {
		this.userIds = Collections.unmodifiableList(userIds);
	}

	public String asJson() {
		String json = userIds.stream().map(id -> {
			return "\"" + id + "\"";
		}).collect(Collectors.joining(",", "[", "]"));
		return json;
	}

	@Override
	public String toString() {
		return this.userIds.toString();
	}
}
