package fr.obeo.tools.stuart;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class MattermostPost {

	private String text;

	private String username;

	private String iconUrl;

	public MattermostPost(String text, String username, String iconUrl) {
		super();
		this.text = text;
		this.username = username;
		this.iconUrl = iconUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public static MattermostPost fromGenericPost(Post in) {

		StringBuffer cleanedBody = new StringBuffer();

		if (in.mightTruncate()) {
			for (String line : Splitter.on('\n').omitEmptyStrings().trimResults().split(in.getMarkdownBody())) {
				if (in.isQuote()) {
					cleanedBody.append('>');
				}
				cleanedBody.append(line);
				cleanedBody.append('\n');
			}
		} else {
			cleanedBody.append(in.getMarkdownBody());
		}

		Iterable<String> cleanedHrefs = Iterables.transform(in.getURLs(), new Function<String, String>() {

			public String apply(String arg0) {
				String result = arg0;
				if (arg0 != null) {
					if (arg0.contains("gerrit")) {
						result = "[->review](" + arg0 + ")";
					}
					if (arg0.contains("bugs") || arg0.contains("jira")) {
						result = "[->bug](" + arg0 + ")";
					}
					if (arg0.contains("jenkins") || arg0.contains("hudson")) {
						result = "[->build](" + arg0 + ")";
					}
					if (arg0.contains("git")) {
						result = "[->commit](" + arg0 + ")";
					}
					if (arg0.contains("forums")) {
						result = "[->forum](" + arg0 + ")";
					}
				}
				return result;

			}
		});

		String urls = Joiner.on("\n").join(cleanedHrefs);

		String tags = Joiner.on(" ").join(Iterables.transform(in.getTags(), new Function<String, String>() {

			public String apply(String tag) {
				return "#" + tag;
			}
		}));

		StringBuffer message = new StringBuffer();
		String bodyString = cleanedBody.toString().trim();
		if (bodyString.length() > 250 && in.mightTruncate()) {
			bodyString = bodyString.substring(0, 250);
			bodyString += "(...)";
		}
		if (in.getSubject() != null) {
			message.append("##### ");
			message.append(in.getSubject().trim());
			message.append(' ');
			message.append(tags);
			message.append('\n');
			message.append(bodyString);
			embedMedias(in, message);
			message.append('\n');
			message.append(urls);
		} else {
			if (tags.trim().length() > 0) {
				message.append("##### ");
				message.append(tags);
				message.append('\n');
			}
			message.append(bodyString);
			embedMedias(in, message);
			message.append('\n');
			message.append(urls);
		}
		return new MattermostPost(message.toString(), in.getAuthor(), in.getIconURL());
	}

	private static void embedMedias(Post in, StringBuffer message) {
		for (String mediaURL : in.getMediaURLs()) {
			message.append("\n [![pic](");
			message.append(mediaURL);
			message.append(")](");
			message.append(mediaURL);
			message.append(")");
		}
	}

}
