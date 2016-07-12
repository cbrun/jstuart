package fr.obeo.tools.stuart.mattermost.bot.internal;

import java.util.List;

import com.google.common.collect.Lists;

import fr.obeo.tools.stuart.mattermost.bot.MChannel;

public class ChannelsWrapper {

	private List<MChannel> channels = Lists.newArrayList();

	public List<MChannel> getChannels() {
		return channels;
	}

	public void setChannels(List<MChannel> channels) {
		this.channels = channels;
	}

}
