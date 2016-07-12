package fr.obeo.tools.stuart.mattermost.bot;

public class MTeam {

	// {"id":"86iotjfc8bdmdeq41ji99qgjja","create_at":1449690096514,"update_at":1449762125727,"delete_at":0,"display_name":"eclipse",
	// "name":"eclipse","email":"","type":"O","company_name":"","allowed_domains":"","invite_id":"g3364wnzqpbhjdedegobmp93mo",
	// "allow_open_invite":true}

	private String id;

	private long createAt;

	private long deleteAt;

	private String displayName;

	private String name;

	private String email;

	private String type;

	private String companyName;

	private String allowedDomains;

	private String inviteId;

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", type=" + type + ", createAt=" + createAt + ", deleteAt="
				+ deleteAt + ", displayName=" + displayName + ", email=" + email + ", companyName=" + companyName
				+ ", allowedDomains=" + allowedDomains + ", inviteId=" + inviteId + ", allowOpenInvite="
				+ allowOpenInvite + "]";
	}

	private boolean allowOpenInvite;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(long deleteAt) {
		this.deleteAt = deleteAt;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAllowedDomains() {
		return allowedDomains;
	}

	public void setAllowedDomains(String allowedDomains) {
		this.allowedDomains = allowedDomains;
	}

	public String getInviteId() {
		return inviteId;
	}

	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	public boolean isAllowOpenInvite() {
		return allowOpenInvite;
	}

	public void setAllowOpenInvite(boolean allowOpenInvite) {
		this.allowOpenInvite = allowOpenInvite;
	}

}
