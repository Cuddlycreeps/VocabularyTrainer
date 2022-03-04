package com.mygdx.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.UI.questionListLabel;
import static com.mygdx.game.UI.rngIndex;

public class Backend{
	static ArrayList<String> questionsFileStrings = scanFolder("questions");
	static ArrayList<String> answersFileStrings = scanFolder("answers");
	static ArrayList<Integer> wrongIndexList = new ArrayList<>();

	static{
		initializeWrongIndexList();
	}

	static public void initializeWrongIndexList(){
		for(int i = 0; i < questionsFileStrings.size(); i++) {
			wrongIndexList.add(i);
		}
	}

	static public int generateRandomIndexFromWrong(){
		if (wrongIndexList.isEmpty()){
			initializeWrongIndexList();
		}
		int k = new Random().nextInt((wrongIndexList.size()));
		if (k == rngIndex && wrongIndexList.size() > 1){
			return generateRandomIndexFromWrong();
		}
		else{
			return k;
		}
	}

	static public void generateQuestionList(){
		StringBuilder output = new StringBuilder();
		for (int j = 0 ; j < questionsFileStrings.size() ; j++){
			if (wrongIndexList.contains(j)) {
				output.append(questionsFileStrings.get(j), 0, questionsFileStrings.get(j).length() - 4);
				output.append("\r\n");
			}
		}
		questionListLabel.setText(output.toString());
	}

	static public ArrayList<String> scanFolder(String folderName) {
		File folder = new File("./assets/" + folderName);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> listOfFilenames = new ArrayList<>();
		for (File file : listOfFiles){
			if (file.isFile()){
				listOfFilenames.add(file.getName());
			}
		}
		return listOfFilenames;
	}

}
