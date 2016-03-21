package fr.obeo.tools.stuart.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import fr.obeo.tools.stuart.Post;

public class GitLogger {

	private static final String GIT_ICON = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Git_icon.svg/50px-Git_icon.svg.png";
	private File cacheFolder;

	private HashFunction cloneFolderName = Hashing.sha256();

	public GitLogger(File clonesCacheFolder) {
		this.cacheFolder = clonesCacheFolder;
	}

	public Collection<Post> getMergedCommits(Date daysAgo, String remoteURL, String webCommitURL) {
		Collection<RevCommit> allCommits = getAllCommits(remoteURL,true);

		List<Post> posts = new ArrayList<Post>();
		for (RevCommit commit : allCommits) {
			PersonIdent authorIdent = commit.getAuthorIdent();
			Date authorDate = authorIdent.getWhen();
			if (authorDate.after(daysAgo)) {
				StringBuffer body = new StringBuffer();
				boolean first = true;
				for (String line : Splitter.on('\n').omitEmptyStrings().split(commit.getFullMessage())) {
					if (line.contains("Change-Id:")) {

					} else if (line.contains("Bug: ")) {

					} else if (line.contains("Signed-off-by:")) {

					} else {
						if (!first) {
							body.append(line);
							body.append('\n');
						}
					}
					first = false;
				}

				// [some thing] dflgkj,fdlg [sss]
				// [1248]
				// [ 1248 ]

				String url = webCommitURL + commit.getId().name();
				String title = commit.getShortMessage();
				title = detectBugzillaLink(title);
				Post newPost = Post.createPostWithSubject(url, title, body.toString(), authorIdent.getName(), GIT_ICON,
						authorDate);
				newPost.addURLs(url);
				posts.add(newPost);
			}
		}

		return posts;
	}

	public Collection<RevCommit> getAllCommits(String remoteURL,boolean update) {
		Collection<RevCommit> allCommits = Lists.newArrayList();
		File cloneDir = new File(this.cacheFolder.getPath() + File.separator + cloneFolderName.hashString(remoteURL));
		try {
			Files.createParentDirs(cloneDir);
			Git repo = null;
			TextProgressMonitor monitor = new TextProgressMonitor();
			if (!cloneDir.exists()) {
				repo = Git.cloneRepository().setURI(remoteURL).setBare(true).setDirectory(cloneDir)
						.setProgressMonitor(monitor).call();
			} else {
				repo = Git.open(cloneDir);
				repo.fetch().setProgressMonitor(monitor).call();
			}
			allCommits = Lists.newArrayList(repo.log().all().call());
		} catch (InvalidRemoteException e) {
			throw new RuntimeException(e);
		} catch (TransportException e) {
			throw new RuntimeException(e);
		} catch (GitAPIException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return allCommits;
	}

	public static String detectBugzillaLink(String title) {
		title = title.replaceFirst("\\[([0-9]+)\\]", "[[$1](https://bugs.eclipse.org/bugs/show_bug.cgi?id=$1)]");
		return title;
	}

}
