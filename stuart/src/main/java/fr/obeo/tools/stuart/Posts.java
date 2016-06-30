package fr.obeo.tools.stuart;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class Posts {

	public static Set<String> getThreadIds(List<Post> postsBefore) {
		Set<String> threadIDs = Sets.newLinkedHashSet();
		for (Post post : postsBefore) {
			if (post.getThreadID() != null) {
				threadIDs.add(post.getThreadID());
			}
		}
		return threadIDs;
	}

	public static Multimap<String, Post> groupByThread(Collection<Post> pPosts) {
		Multimap<String, Post> byThread = HashMultimap.create();
		for (Post post : pPosts) {
			byThread.put(post.getThreadID(), post);
		}
		return byThread;
	}

}
