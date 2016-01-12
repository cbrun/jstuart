package fr.obeo.tools.stuart.mattermost;

import java.io.IOException;

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

		// ConnectionSpec spec = new
		// ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2)
		// .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
		// CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
		// CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
		// .build();
		this.client = new OkHttpClient();
		// this.client.setConnectionSpecs(Collections.singletonList(spec));
		this.client.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		client.setCertificatePinner(new CertificatePinner.Builder()
			       .add("mattermost-test.eclipse.org", "sha1/+1YwGEPdk4OT2oj03RBvB0+xSBA=")
			       .add("92.51.162.68", "sha1/+1YwGEPdk4OT2oj03RBvB0+xSBA=")
			       .add("Let's Encrypt Authority X1, O=Let's Encrypt", "sha1/2ptSqHcRadMTGKVn4dybH0S1s1w=")
			       
			       .build());
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
			throw new RuntimeException(e);
		}

	}

}
