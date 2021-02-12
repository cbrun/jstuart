package fr.obeo.tools.stuart.mattermost.bot.user;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class MUserGetUsersByIdsParam {

	private List<String> userIds = Lists.newArrayList();

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return userIds.toString();
	}

	public String getJson() {
		String json = userIds.stream().map(id -> {
			return "\"" + id + "\"";
		}).collect(Collectors.joining(",", "[", "]"));
		return json;
	}
}
