package fr.obeo.tools.stuart.mattermost.bot.internal;

import java.io.IOException;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.ws.WebSocket;
import com.squareup.okhttp.ws.WebSocketListener;

import fr.obeo.tools.stuart.mattermost.bot.MMBot;
import okio.Buffer;

public class BotSocketListener implements WebSocketListener {

	private MMBot bot;
	private Gson gson;

	public BotSocketListener(Gson gson, MMBot mmBot) {
		this.gson = gson;
		this.bot = mmBot;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		System.out.println("MMBot.getPosts(...).new WebSocketListener() {...}.onClose()");
	}

	@Override
	public void onFailure(IOException arg0, Response arg1) {
		System.out.println("MMBot.getPosts(...).new WebSocketListener() {...}.onFailure() " + arg1);
		System.err.println("Reconnecting...");
		bot.listen();
	}

	@Override
	public void onMessage(ResponseBody arg1) throws IOException {

	}

	@Override
	public void onOpen(WebSocket arg0, Response arg1) {
		System.err.println(arg0);

	}

	@Override
	public void onPong(Buffer arg0) {
		System.out.println("MMBot.getPosts(...).new WebSocketListener() {...}.onPong()");
	}

}
