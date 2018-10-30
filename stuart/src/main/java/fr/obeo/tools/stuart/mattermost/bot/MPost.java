package fr.obeo.tools.stuart.mattermost.bot;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class MPost {

	private String channelId;

	private long createAt;

	private long deleteAt;

	private String id;

	private String message;

	private String originalId;

	private String parentId;

	private String rootId;

	private String teamId;

	private String type;

	private String pendingPostId;

	public long getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(long deleteAt) {
		this.deleteAt = deleteAt;
	}

	public String getPendingPostId() {
		return pendingPostId;
	}

	public void setPendingPostId(String pendingPostId) {
		this.pendingPostId = pendingPostId;
	}

	public String getHashtags() {
		return hashtags;
	}

	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> filenames) {
		this.fileIds = filenames;
	}

	private long updateAt;

	private String userId;

	private String hashtags;

	private List<String> fileIds = Lists.newArrayList();

	private Map<String, String> props;

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public String getChannelId() {
		return channelId;
	}

	public long getCreateAt() {
		return createAt;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public String getOriginalId() {
		return originalId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getRootId() {
		return rootId;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getType() {
		return type;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public String getUserId() {
		return userId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PostFromServer [id=" + id + ", message=" + message + ", channelId=" + channelId + ", userId=" + userId
				+ ", type=" + type + ", hashtags=" + hashtags + ", props=" + props + ", filenames=" + fileIds
				+ ", originalId=" + originalId + ", parentId=" + parentId + ", rootId=" + rootId + ", teamId=" + teamId
				+ ", pendingPostId=" + pendingPostId + ", updateAt=" + updateAt + ", deleteAt=" + deleteAt
				+ ", createAt=" + createAt + "]";
	}

	public boolean isFromWebhook() {
		return props != null && "true".equals(props.get("from_webhook"));
	}

	private ChannelAction action = ChannelAction.none;

	public ChannelAction getAction() {
		return action;
	}

	public void setAction(ChannelAction action) {
		this.action = action;
	}
	
	public static MPost createFrom(MPost original,String message) {
		MPost newOne = new MPost();
		newOne.setChannelId(original.getChannelId());
		newOne.setUserId(original.getUserId());
		newOne.setTeamId(original.getTeamId());
		newOne.setMessage(message);
		return newOne;
		
	}
}

enum ChannelAction {
	posted, typing, none
}
