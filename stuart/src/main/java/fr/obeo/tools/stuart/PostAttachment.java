package fr.obeo.tools.stuart;

public class PostAttachment {

	private String url;

	private String name;

	private String mimeType;

	private String localFileName;
	
	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PostAttachments [url=" + url + ", name=" + name + ", mimeType=" + mimeType + "]";
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
