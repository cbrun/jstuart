package fr.obeo.tools.stuart.mattermost.bot;

public class MFileInfos {

	String id;

	String userId;

	long createAt;

	long updateAt;

	long deleteAt;

	String name;
	String extension;

	int size;

	String MimeType;

	String charset;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public long getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(long deleteAt) {
		this.deleteAt = deleteAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getMimeType() {
		return MimeType;
	}

	public void setMimeType(String mimeType) {
		MimeType = mimeType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		return "MFileInfos [id=" + id + ", userId=" + userId + ", createAt=" + createAt + ", updateAt=" + updateAt
				+ ", deleteAt=" + deleteAt + ", name=" + name + ", extension=" + extension + ", size=" + size
				+ ", MimeType=" + MimeType + ", charset=" + charset + "]";
	}

}
