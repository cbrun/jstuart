package fr.obeo.tools.stuart.mattermost.bot;

import java.util.ArrayList;
import java.util.List;

public class MFileUpload {

	private List<MFileInfos> fileInfos = new ArrayList();

	public List<MFileInfos> getFileInfos() {
		return fileInfos;
	}


	public void setFileInfos(List<MFileInfos> fileInfos) {
		this.fileInfos = fileInfos;
	}

	@Override
	public String toString() {
		return "MFileUpload [fileInfos=" + fileInfos + "]";
	}

}
