package fr.obeo.tools.stuart.mattermost.bot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.ws.WebSocketCall;

import fr.obeo.tools.stuart.mattermost.bot.internal.BotSocketListener;
import fr.obeo.tools.stuart.mattermost.bot.internal.ChannelsWrapper;
import fr.obeo.tools.stuart.mattermost.bot.internal.MessagesWrapper;

class LoginMsg {

	private String loginId;

	private String password;

	public LoginMsg(String email, String password) {
		super();
		this.loginId = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginMsg [email=" + loginId + "]";
	}

}

public class MMBot {

	public static MMBot logIn(OkHttpClient client, String host, String userMail, String pwd) throws IOException {
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
		String body = gson.toJson(new LoginMsg(userMail, pwd));
		String url = host + "api/v3/users/login";
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(MediaType.parse("application/json"), body)).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);

		String token = response.header("Token");
		String returnedBody = response.body().string();
		MUser u = gson.fromJson(returnedBody, MUser.class);
		if (token != null && u != null) {
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			client.setCookieHandler(cookieManager);

			MMBot bot = new MMBot(token, u, host, client, gson);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

				@Override
				public void run() {
					if (bot != null) {
						try {
							bot.logOut();
						} catch (IOException e) {
							/*
							 * could not log out but the JVM is being shot down
							 * anyway.
							 */
						}
					}

				}
			}));
			return bot;
		}
		return null;
	}

	private OkHttpClient client;

	private Gson gson;

	private String host;

	private List<ReactOnMessage> reactors = Lists.newArrayList();

	private AtomicBoolean stop = new AtomicBoolean(false);

	private String token;

	private MUser u;

	private WebSocketCall websocket;

	public MMBot(String token, MUser u, String host, OkHttpClient client, Gson gson) {
		this.token = token;
		this.host = host;
		this.u = u;
		this.client = client;
		this.gson = gson;
	}

	private CompletableFuture<String> post(String url, String payload) throws IOException {
		Request request = auth(new Request.Builder()).url(url)
				.post(RequestBody.create(MediaType.parse("application/json"), payload)).build();
		final Response response = client.newCall(request).execute();
		return CompletableFuture.supplyAsync(() -> {
			if (response.isSuccessful()) {
				try {
					String result = response.body().string();
					response.body().close();
					return result;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				response.body().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});

	}

	private Builder auth(Builder builder) {
		// builder.addHeader("X-Version-Id", "1.4.0.1454499231");
		// builder.addHeader("X-MM-TokenIndex", "0");
		// builder.addHeader("X-Requested-With", "XMLHttpRequest");
		builder.addHeader("Authorization", " Bearer " + this.token);
		builder.addHeader("Cookie", "MMTOKEN=" + this.token);
		return builder;
	}

	private Request getRequest(String url) {
		Request r = new Request.Builder().url(url).header("Authorization", " Bearer " + this.token)
				.addHeader("Cookie", "MMTOKEN=" + this.token).get().build();
		return r;
	}

	public MUser getUser() {
		return this.u;
	}

	public void listen() {

		/*
		 * we might be called by the websocket listener in case of failure in
		 * order to reconnect.
		 */
		if (!stop.get()) {
			// Create request for remote resource.
			// HTTPS => wss, HTTP => WS
			String wsURL = this.host.replace("https", "wss").replace("http", "ws")
					+ "api/v3/users/websocket?session_token_index=0&1";
			System.out.println(wsURL);
			Request request = getRequest(wsURL);

			if (websocket != null) {
				websocket.cancel();
			}

			client.setReadTimeout(0, TimeUnit.SECONDS);
			// Execute the request and retrieve the response.

			websocket = WebSocketCall.create(client, request);
			BotSocketListener listener = new BotSocketListener(gson, this) {

				@Override
				public void onMessage(ResponseBody response) throws IOException {
					String data = response.string();

					System.out.println(data);
					MPost msg = gson.fromJson(data, MPost.class);
					if (msg.getId() == null) {
						/*
						 * the json format changed with Mattermost 3.3 with a
						 * generic "event" which might hold data.
						 */
						MEvent event = gson.fromJson(data, MEvent.class);
						if (event.getData().get("post") != null) {
							MPost p = gson.fromJson(event.getData().get("post"), MPost.class);
							if (p.getTeamId() == null) {
								p.setTeamId(event.getTeamId());
							}
							if (event.getData().get("team_id") != null && p.getTeamId() == null) {
								p.setTeamId(event.getData().get("team_id"));
							}
							/*
							 * with 3.5 we get the channel ID directly with the
							 * post whereas previously we got it in the event
							 * itself.
							 */
							if (p.getChannelId() == null) {
								p.setChannelId(event.getChannelId());
							}
							transmitPost(p);
						} else if (event.getData().get("message") != null) {
							System.out.println("MMBot.listen().new BotSocketListener() {...}.onMessage()");
						}

					} else {
						if (msg.getProps() != null && msg.getProps().get("post") != null) {

							MPost p = gson.fromJson(msg.getProps().get("post"), MPost.class);
							p.setTeamId(msg.getTeamId());
							transmitPost(p);

						} else if (msg.getAction() == ChannelAction.typing) {
							System.out.println(msg.getUserId() + " is typîng");
						} else {

							System.out.println("unknown :\n" + data);
						}
					}
					response.close();
				}

				public void transmitPost(MPost p) {
					for (ReactOnMessage listener : reactors) {
						try {
							listener.onMessage(MMBot.this, p);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

			};
			websocket.enqueue(listener);
		}

	}

	public void logOut() throws IOException {
		stopListening();
		String url = host + "logout";
		Request r = auth(new Request.Builder()).url(url)
				.post(RequestBody.create(MediaType.parse("application/json"), "")).build();
		Response response = client.newCall(r).execute();
		System.out.println("logout HTTP:" + response.code());
	}

	public void onMessage(ReactOnMessage r) {
		reactors.add(r);
	}

	public MPost respond(MPost p, String message) throws IOException {
		MPost toSend = new MPost();
		toSend.setChannelId(p.getChannelId());
		toSend.setCreateAt(p.getCreateAt() + 100);
		toSend.setUserId(this.u.getId());
		toSend.setMessage(message);
		toSend.setTeamId(p.getTeamId());
		toSend.setParentId(p.getId());
		toSend.setRootId(p.getId());
		return sendPost(toSend);
	}

	private <T> T getResource(String url, Class<T> classOfT) throws IOException {
		return getResource(url, (Type) classOfT);
	}

	private <T> T getResource(String url, Type typeOfT) throws IOException {
		Response response = client.newCall(getRequest(url)).execute();
		try (ResponseBody body = response.body()) {
			if (response.isSuccessful()) {
				T target = (T) gson.fromJson(body.string(), typeOfT);
				return target;
			}
		}
		return null;
	}

	public List<MChannel> getChannels(String teamId) throws IOException {
		ChannelsWrapper r = getResource(host + "api/v3/teams/" + teamId + "/channels/", ChannelsWrapper.class);
		if (r != null && r.getChannels() != null) {
			return r.getChannels();
		}
		return Lists.newArrayList();
	}

	public MChannel getChannel(String teamId, String channelId) throws IOException {
		String url = host + "api/v3/teams/" + teamId + "/channels/" + channelId + "/";
		Response response = client.newCall(getRequest(url)).execute();
		try (ResponseBody body = response.body()) {
			if (response.isSuccessful()) {
				Map<String, MChannel> allChannels = gson.fromJson(body.string(),
						new TypeToken<Map<String, MChannel>>() {
						}.getType());
				return allChannels.get("channel");
			}
		}
		return null;
	}

	public Map<String, MTeam> getTeams() throws IOException {
		return getResource(host + "api/v3/teams/all", new TypeToken<Map<String, MTeam>>() {
		}.getType());
	}

	public Map<String, MPost> getPostsSince(String teamId, String channelId, long since) throws IOException {
		MessagesWrapper r = getResource(
				host + "api/v3/teams/" + teamId + "/channels/" + channelId + "/posts/since/" + since,
				MessagesWrapper.class);
		if (r != null && r.getPosts() != null) {
			return r.getPosts();
		}
		return Maps.newLinkedHashMap();
	}

	public Map<String, MPost> getPosts(String teamId, String channelId, int offset, int limit) throws IOException {
		MessagesWrapper r = getResource(
				host + "api/v3/teams/" + teamId + "/channels/" + channelId + "/posts/page/" + offset + "/" + limit,
				MessagesWrapper.class);
		if (r.getPosts() != null) {
			return r.getPosts();
		}
		return Maps.newLinkedHashMap();
	}

	public MPost sendPost(MPost toSend) throws IOException {
		String url = host + "api/v3/teams/" + toSend.getTeamId() + "/channels/" + toSend.getChannelId()
				+ "/posts/create";
		String dataToSend = gson.toJson(toSend);
		Request r = auth(new Request.Builder()).url(url)
				.post(RequestBody.create(MediaType.parse("application/json"), dataToSend)).build();
		Response response = client.newCall(r).execute();
		try (ResponseBody body = response.body()) {
			if (response.isSuccessful()) {
				MPost msg = gson.fromJson(body.string(), MPost.class);
				msg.setTeamId(toSend.getTeamId());
				return msg;
			}
		}
		return null;
	}

	public void stopListening() {
		if (websocket != null) {
			websocket.cancel();
		}
		this.stop.set(true);
	}

	public void update(MPost msg, String message) throws IOException {
		String url = host + "api/v3/teams/" + msg.getTeamId() + "/channels/" + msg.getChannelId() + "/posts/update";
		MPost toSend = new MPost();
		toSend.setChannelId(msg.getChannelId());
		toSend.setId(msg.getId());
		toSend.setMessage(message);

		String dataToSend = gson.toJson(toSend);
		Request r = auth(new Request.Builder()).url(url)
				.post(RequestBody.create(MediaType.parse("application/json"), dataToSend)).build();
		Response response = client.newCall(r).execute();
		response.body().close();
	}

	public MFileUpload uploadFile(String teamId, String channelId, File file) throws IOException {
		String url = host + "api/v3/teams/" + teamId + "/files/upload";
		MultipartBuilder bodyBuilder = new MultipartBuilder().type(MediaType.parse("multipart/form-data"))
				.addFormDataPart("channel_id", channelId).addFormDataPart("files", file.getName(),
						RequestBody.create(MediaType.parse("application/octet-stream"), file));
		Request r = auth(new Request.Builder()).url(url).post(bodyBuilder.build()).build();
		Response response = client.newCall(r).execute();
		try (ResponseBody body = response.body()) {
			if (response.isSuccessful()) {
				MFileUpload files = gson.fromJson(body.string(), MFileUpload.class);
				return files;
			}
		}
		return null;
	}

}
