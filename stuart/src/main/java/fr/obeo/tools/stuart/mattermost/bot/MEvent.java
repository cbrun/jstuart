package fr.obeo.tools.stuart.mattermost.bot;

import java.util.Map;

public class MEvent {

	@Override
	public String toString() {
		return "MEvent [channelId=" + channelId + ", userId=" + userId + ", teamId=" + teamId + ", event=" + event
				+ ", data=" + data + "]";
	}

	public String getChannelId() {
		return channelId;
	}

	public String getUserId() {
		return userId;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getEvent() {
		return event;
	}

	public Map<String, String> getData() {
		return data;
	}

	private String channelId;

	private String userId;

	private String teamId;

	private String event;

	private Map<String, String> data;

}
