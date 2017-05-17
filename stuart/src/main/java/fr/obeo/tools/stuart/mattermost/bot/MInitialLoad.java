package fr.obeo.tools.stuart.mattermost.bot;

import java.util.List;

import com.google.common.collect.Lists;


public class MInitialLoad {
	
	private List<MTeam> teams = Lists.newArrayList();

	public List<MTeam> getTeams() {
		return teams;
	}

	@Override
	public String toString() {
		return "MInitialLoad [teams=" + teams + "]";
	}

}
