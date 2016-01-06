package fr.obeo.tools.stuart.mattermost;

import java.io.IOException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.obeo.tools.stuart.MattermostPost;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

		// ConnectionSpec spec = new
		// ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2)
		// .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
		// CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
		// CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
		// .build();
		this.client = new OkHttpClient();
		// this.client.setConnectionSpecs(Collections.singletonList(spec));
	}

	public void accept(MattermostPost post) {
		RequestBody body = new FormBody.Builder().add("payload", gson.toJson(post)).build();

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
			throw new RuntimeException(e);
		}

	}

}
