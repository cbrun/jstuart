package fr.obeo.tools.stuart.eclipseforum;

import java.util.Collection;
import java.util.Date;

import fr.obeo.tools.stuart.Post;

public interface ForumLogger {

	public Collection<Post> collectPosts(int forumNumber, Date daysAgo);
}
