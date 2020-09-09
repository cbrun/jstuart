package fr.obeo.tools.stuart.eclipseforum;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;

import fr.obeo.tools.stuart.Post;
import fr.obeo.tools.stuart.PostAttachment;

public class AttachmentsLogger {

	private Map<String, Document> alreadyLoadedDocuments = Maps.newLinkedHashMap();

	private int attachmentsFound = 0;

	public void doYourMagic(Post p) {
		String threadURL = p.getKey();
		String postID = threadURL.substring(threadURL.indexOf("#") + 1);
		p.setOriginalPostID(postID);
		// now let's digg to find other attachments

		Document fullDoc = alreadyLoadedDocuments.get(threadURL);
		if (fullDoc == null) {
			try {
				fullDoc = Jsoup.parse(new URL(threadURL), 2000);
				Elements parents = fullDoc.select("a[name$=" + postID + "]").parents();
				if (parents.size() > 3) {
					Element msgRoot = parents.get(3);
					p.setFullHTMLBody(msgRoot.toString());
					Elements list = msgRoot.select("ul.AttachmentsList li a");
					for (Element anchor : list) {
						String url = "https://www.eclipse.org/forums/" + anchor.attr("href");
						if (p.getKey().startsWith("https://polarsys.org/")) {
							url = "https://polarsys.org/forums/"  + anchor.attr("href");
						}
						String mimeType = anchor.select("img").attr("src");
						String name = anchor.text();
						PostAttachment attachment = new PostAttachment();
						attachment.setName(name);
						attachment.setMimeType(mimeType);
						attachment.setUrl(url);
						p.getAttachments().add(attachment);
						attachmentsFound++;
					}
				} else {
					System.err.println("Something weird here : " + threadURL + " for message " + postID);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alreadyLoadedDocuments.put(threadURL, fullDoc);
		}
	}

	public String summary() {
		return "Attachments found : " + attachmentsFound;
	}

}
