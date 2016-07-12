package fr.obeo.tools.stuart.mattermost.bot.internal;

import java.util.Map;

import com.google.common.collect.Maps;

import fr.obeo.tools.stuart.mattermost.bot.MPost;

public class MessagesWrapper {

	private Map<String, MPost> posts = Maps.newLinkedHashMap();

	public Map<String, MPost> getPosts() {
		return posts;
	}

}
