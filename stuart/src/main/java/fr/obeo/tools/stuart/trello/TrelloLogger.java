package fr.obeo.tools.stuart.trello;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.obeo.tools.stuart.Post;

public class TrelloLogger {
	private static final String TRELLO_ICON = "https://imgur.com/SGJvIQv.png";
	private String baseURL = "https://api.trello.com";
	// Trello key @see https://trello.com/app-key
	private String key;
	// Trello token @see https://trello.com/app-key
	private String token;
	// Trello board id. xxxxxx is the board id to get the ids of all the elements of
	// a Trello board see : https://trello.com/b/xxxxxx/reports.json
	private String boardId;

	public TrelloLogger(String boardId, String key, String token) {
		this.key = key;
		this.token = token;
		this.boardId = boardId;
	}

	public Collection<Post> get(String filterListCriteria) throws MalformedURLException {

		// Create the board
		TrelloBoard trelloBoard = new TrelloBoard();
		trelloBoard.setId(boardId);

		// Get all the lists of the board
		List<TrelloList> trelloLists = new ArrayList<>();
		String openListsQuery = baseURL + "/1/boards/" + boardId + "?key=" + key + "&token=" + token
				+ "&lists=open&list_fields=id,name";
		URL openListsURL = new URL(openListsQuery);
		Optional<String> listsJson = readFromTheURL(openListsURL);
		if (listsJson.isPresent()) {
			JsonObject boardJsonObject = new JsonParser().parse(listsJson.get()).getAsJsonObject();
			JsonArray listsJsonArray = boardJsonObject.getAsJsonArray("lists");
			for (JsonElement listJsonElement : listsJsonArray) {
				JsonObject listJsonObject = listJsonElement.getAsJsonObject();
				String trelloListName = listJsonObject.get("name").getAsString();
				// Filter to keep only the expected lists
				if (trelloListName.contains(filterListCriteria)) {
					TrelloList trelloList = new TrelloList();
					trelloList.setId(listJsonObject.get("id").getAsString());
					trelloList.setName(trelloListName);
					trelloLists.add(trelloList);

					// Get cards from the list
					List<TrelloCard> trelloCards = new ArrayList<>();
					String openCardsQuery = baseURL + "/1/lists/" + trelloList.getId() + "/cards?key=" + key + "&token="
							+ token + "&members=true&member_fields=fullName&fields=id,name,desc,due,url&filter=open";
					URL openCardsURL = new URL(openCardsQuery);
					Optional<String> cardsJson = readFromTheURL(openCardsURL);
					if (cardsJson.isPresent()) {
						JsonArray cardsJsonArray = new JsonParser().parse(cardsJson.get()).getAsJsonArray();
						for (JsonElement cardJsonElement : cardsJsonArray) {
							JsonObject cardJsonObject = cardJsonElement.getAsJsonObject();
							TrelloCard trelloCard = new TrelloCard();
							trelloCard.setId(cardJsonObject.get("id").getAsString());
							trelloCard.setName(cardJsonObject.get("name").getAsString());
							trelloCard.setDesc(cardJsonObject.get("desc").getAsString());
							trelloCard.setDue(cardJsonObject.get("due").getAsString());
							JsonArray membersJsonArray = cardJsonObject.get("members").getAsJsonArray();
							if (membersJsonArray.size() > 0) {
								trelloCard.setMember(
										membersJsonArray.get(0).getAsJsonObject().get("fullName").getAsString());
							}
							trelloCard.setUrl(cardJsonObject.get("url").getAsString());
							trelloCards.add(trelloCard);
						}
					}
					trelloList.setCards(trelloCards);
				}
			}
			trelloBoard.setLists(trelloLists);
		}

		List<Post> posts = new ArrayList<Post>();
		trelloBoard.getLists().forEach(list -> list.getCards().forEach(card -> {
			Post newPost;
			try {
				newPost = Post.createPostWithSubject(card.getUrl(), card.getName(), card.getDesc(), card.getMember(),
						TRELLO_ICON, new SimpleDateFormat("yyyy-MM-dd")
								.parse(card.getDue().substring(0, card.getDue().indexOf("T"))));
				newPost.mightBeTruncated(false);
				newPost.addURLs(card.getUrl());

				posts.add(newPost);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}));
		return posts;
	}

	private Optional<String> readFromTheURL(URL url) {
		try {
			Scanner scan = new Scanner(url.openStream());
			String json = new String();
			while (scan.hasNext())
				json += scan.nextLine();
			scan.close();

			System.out.println(json);

			return Optional.of(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	// Trello board, list, card POJOs
	private class TrelloBoard {

		private String id;
		private String name;
		private List<TrelloList> lists;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<TrelloList> getLists() {
			return lists;
		}

		public void setLists(List<TrelloList> lists) {
			this.lists = lists;
		}
	}

	private class TrelloList {

		private String id;
		private String name;
		private List<TrelloCard> cards;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<TrelloCard> getCards() {
			return cards;
		}

		public void setCards(List<TrelloCard> cards) {
			this.cards = cards;
		}
	}

	private class TrelloCard {

		private String id;
		private String name;
		private String desc;
		private String due;
		private String member;
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getDue() {
			return due;
		}

		public void setDue(String due) {
			this.due = due;
		}

		public String getMember() {
			return member;
		}

		public void setMember(String member) {
			this.member = member;
		}
	}
}
