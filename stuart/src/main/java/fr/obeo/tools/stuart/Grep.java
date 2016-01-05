package fr.obeo.tools.stuart;

import com.google.common.base.Predicate;

public class Grep implements Predicate<Post> {

	private boolean searchTitle = true;

	private boolean searchBody = true;

	private String[] terms;

	public Grep(String... terms) {
		this.terms = terms;
	}

	public boolean apply(Post input) {
		boolean found = false;
		if (searchTitle && input.getSubject().isPresent()) {
			for (String term : terms) {
				found = found || input.getSubject().get().contains(term);
			}
		}
		if (!found && searchBody && input.getMarkdownBody() != null) {
			for (String term : terms) {
				found = found || input.getMarkdownBody().contains(term);
			}
		}
		return found;
	}

	public static Predicate<Post> caseSensitiveFullSearch(String... terms) {
		return new Grep(terms);
	}

}
