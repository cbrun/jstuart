package fr.obeo.tools.stuart.mattermost.bot;

public class MChannel {

	private String id;

	private String displayName;

	private String purpose;

	private String header;

	private long createAt;

	private long updateAt;

	private long deleteAt;

	private long lastPostAt;

	private String type;

	private int totalMsgCount;

	@Override
	public String toString() {
		return "Channel [id=" + id + ", displayName=" + displayName + ", purpose=" + purpose + ", header=" + header
				+ ", createdAt=" + createAt + ", uplongdAt=" + updateAt + ", deletedAt=" + deleteAt + ", lastPostAt="
				+ lastPostAt + ", type=" + type + ", totalMsgCount=" + totalMsgCount + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createdAt) {
		this.createAt = createdAt;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(long uplongdAt) {
		this.updateAt = uplongdAt;
	}

	public long getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(long deletedAt) {
		this.deleteAt = deletedAt;
	}

	public long getLastPostAt() {
		return lastPostAt;
	}

	public void setLastPostAt(long lastPostAt) {
		this.lastPostAt = lastPostAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTotalMsgCount() {
		return totalMsgCount;
	}

	public void setTotalMsgCount(int totalMsgCount) {
		this.totalMsgCount = totalMsgCount;
	}

}
