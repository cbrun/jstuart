package fr.obeo.tools.stuart.mattermost.bot;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class RespondWithGiphyAnimation implements ReactOnMessage {

	private String apiKey;

	private OkHttpClient client = new OkHttpClient();

	private Gson gson = new GsonBuilder().disableHtmlEscaping()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	public RespondWithGiphyAnimation() {
		/*
		 * this is the public api key given by Giphy through
		 * http://api.giphy.com/submit.
		 */
		this.apiKey = "dc6zaTOxFJmzC";
	}

	public RespondWithGiphyAnimation(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public void onMessage(MMBot bot, MPost p) throws IOException {
		if (!p.isFromWebhook() && !bot.getUser().getId().equals(p.getUserId())) {
			if (p.getMessage() != null && p.getMessage().trim().startsWith("gif:")) {
				String searchTerms = p.getMessage().trim().substring(4);
				if (searchTerms != null && searchTerms.trim().length() > 0) {
					HttpUrl url = new HttpUrl.Builder().scheme("http").host("api.giphy.com").addPathSegment("v1")
							.addPathSegment("gifs").addPathSegment("translate").addQueryParameter("s", searchTerms)
							.addQueryParameter("api_key", this.apiKey).addQueryParameter("rating", "g").build();
					Request r = new Request.Builder().url(url).get().build();
					Response response = client.newCall(r).execute();
					try (ResponseBody body = response.body()) {
						if (response.isSuccessful()) {

							TranslateResponse result = gson.fromJson(body.string(), TranslateResponse.class);
							if (result != null && result.getData() != null
									&& result.getData().getImages().get("original") != null) {
								GiphyImg original = result.getData().getImages().get("original");
								if (original != null) {
									bot.respond(p, "![](" + original.getUrl() + ")");
								}
							}
						}
					}
				}
			}
		}

	}

	class TranslateResponse {

		private GiphyMsg data;

		public GiphyMsg getData() {
			return data;
		}

		public void setData(GiphyMsg data) {
			this.data = data;
		}

	}

	class GiphyMsg {

		private String rating;

		private String type;

		private Map<String, GiphyImg> images = Maps.newLinkedHashMap();

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Map<String, GiphyImg> getImages() {
			return images;
		}

		public void setImages(Map<String, GiphyImg> images) {
			this.images = images;
		}

	}

	class GiphyImg {

		private String url;

		private int width;

		private int height;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

	}

}
