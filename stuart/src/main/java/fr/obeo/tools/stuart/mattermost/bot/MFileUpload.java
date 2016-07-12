package fr.obeo.tools.stuart.mattermost.bot;

import java.util.ArrayList;
import java.util.List;

public class MFileUpload {

	private List<String> filenames = new ArrayList();

	public List<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}

	@Override
	public String toString() {
		return "FileUploadMsg [filenames=" + filenames + "]";
	}

}
