package fr.obeo.tools.stuart.mattermost.bot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class RespondSelfDiagnostic implements ReactOnMessage {

	public RespondSelfDiagnostic() {
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			if (p.getMessage().contains("!botstatus")) {
				
				
				
				bot.respond(p, "Starting self diagnostic...");
			
				try {
					MPost response = bot.respond(p, "Updating Message every second");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 6");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 5");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 4");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 3");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 2");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - 1");
					Thread.sleep(1000);
					bot.update(response, "Updating Message every second - X");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				bot.respond(p, "Uploading File");
				File f = File.createTempFile("stuart", ".txt");
				Files.write("Hello World!\n    - Stuart", f, Charset.defaultCharset());
				MFileUpload uploaded = bot.uploadFile(p.getTeamId(), p.getChannelId(), f);
				List<String> filIds = Lists.newArrayList();
				for (MFileInfos fInfo : uploaded.getFileInfos()) {
					filIds.add(fInfo.getId());
				}
				bot.respond(p, "Here is the file.", filIds);
				bot.sendPost(MPost.createFrom(p,
						"Giphy ? - !diagnose gif:hello"));
				bot.sendPost(MPost.createFrom(p,
						"Tweet ? - !diagnose https://twitter.com/bruncedric/status/832285073777033217"));
				bot.sendPost(MPost.createFrom(p,
						"Gerrit ? - !diagnose https://git.eclipse.org/r/#/c/127896/"));
				bot.sendPost(MPost.createFrom(p,
						"Github Pull Requests ? - !diagnose https://github.com/ObeoNetwork/UML-Designer/pull/941"));
			
				bot.sendPost(MPost.createFrom(p,
						"Bugzilla Comments ? - !diagnose https://bugs.eclipse.org/bugs/show_bug.cgi?id=300500#c77"));
				
				bot.sendPost(MPost.createFrom(p,
						"Bugzilla ? - !diagnose https://bugs.eclipse.org/bugs/show_bug.cgi?id=300500"));
				

			}
		}

	}

}
