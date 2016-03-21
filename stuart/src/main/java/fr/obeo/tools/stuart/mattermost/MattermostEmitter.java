package fr.obeo.tools.stuart.mattermost;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import fr.obeo.tools.stuart.MattermostPost;

public class MattermostEmitter {

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
		this.client.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});	
	}

	public void accept(MattermostPost post) {
		// gson.toJson(post)
		RequestBody body = new FormEncodingBuilder().add("payload", gson.toJson(post)).build();

		HttpUrl url = new HttpUrl.Builder().scheme(scheme).host(host).addPathSegment("hooks").addPathSegment(channel)
				.build();
		System.out.println(url);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response;
		try {
			Call call = client.newCall(request);
			response = call.execute();
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response + " canceled ?:" + call.isCanceled());
			}
			System.out.println(response.message());
			response.body().close();
		} catch (IOException e) {
			System.out.println("Message was length:" + post.getText().length() + "\n" + post);
			throw new RuntimeException(e);
		}

	}

}
