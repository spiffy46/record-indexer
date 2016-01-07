package record_indexer.gui.controllers;

import java.awt.Component;
import java.util.List;

import record_indexer.gui.misc.SaveState;
import record_indexer.gui.views.ImagePanel;
import record_indexer.shared.communication.*;

public interface IController {

	void initialize();

	ValidateUserResult login();
	
	void search(List<String> fields, String values);

	void displayBatch(ImagePanel _imagePanel, String selectedValue);

	void quit();

	void logout();

	GetProjectsResult getProjects();

	Component getView();

	DownloadBatchResult downloadBatch(String projectNum);

	SaveState getSaveState();

	GetSampleImageResult downloadSampleImage(String projectNum);

	SubmitBatchResult submitBatch(String[][] myValues);

	void load();

	void save();
	
}