package fr.obeo.tools.stuart.mattermost;

import java.io.IOException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import fr.obeo.tools.stuart.MattermostPost;

public class MattermostEmitter {

	public static final MediaType JSON = MediaType.parse("application/json");

	private String scheme;
	private String host;
	private String channel;
	private Gson gson;
	private OkHttpClient client;

	public MattermostEmitter(String scheme, String host, String channel) {
		this.scheme = scheme;
		this.host = host;
		this.channel = channel;
		this.gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting()
				.serializeNulls().create();

		this.client = new OkHttpClient();
	}

	public void accept(MattermostPost post) {
		String jsonBody = gson.toJson(post);
		RequestBody body = RequestBody.create(JSON, jsonBody);

		HttpUrl url = new HttpUrl.Builder().scheme(scheme).host(host).addPathSegment("hooks").addPathSegment(channel)
				.addQueryParameter("payload", jsonBody).build();

		Request request = new Request.Builder().url(url).post(body).build();
		Response response;
		try {
			response = client.newCall(request).execute();
			response.body().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
