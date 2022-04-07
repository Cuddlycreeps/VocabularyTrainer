package com.mygdx.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.UI.*;

public class Backend{
	static ArrayList<String> questionsFileStrings = scanFolder("questions");
	static ArrayList<String> answersFileStrings = scanFolder("answers");
	static ArrayList<Integer> wrongIndexList = new ArrayList<>();



	static{
		initializeWrongIndexList();
	}

	static public void reinitialize(){
		questionsFileStrings = scanFolder("questions");
		answersFileStrings = scanFolder("answers");
		initializeWrongIndexList();
		generateQuestionList();
		generateAnswerList();
	}

	static public void initializeWrongIndexList(){
		questionsFileStrings = scanFolder("questions");
		answersFileStrings = scanFolder("answers");
		wrongIndexList = new ArrayList<>();
		for(int i = 0; i < questionsFileStrings.size(); i++) {
			wrongIndexList.add(i);
		}
	}

	static public int generateRandomIndexFromWrong(){
		if (wrongIndexList.size() != questionsFileStrings.size()){
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


	static public void generateActiveQuestionList(){ //TODO
		/*
		questionListLabel.clear();
		StringBuilder output = new StringBuilder();
		for (int j = 0 ; j < questionsFileStrings.size() ; j++){
			if (wrongIndexList.contains(j)) {
				output.append(questionsFileStrings.get(j), 0, questionsFileStrings.get(j).length() - 4);
				output.append("\r\n");
			}
		}
*/
	}

	static public void generateQuestionList(){
		String[] questions = new String[questionsFileStrings.size()];
		for (int j = 0 ; j < questionsFileStrings.size() ; j++){
			questions[j] = questionsFileStrings.get(j).substring(0, questionsFileStrings.get(j).length() - 4);
		}
		UI.questionList.clear();
		UI.questionList.setItems(questions);
	}

	static public void generateAnswerList(){
		String[] answers = new String[answersFileStrings.size()];
		for (int j = 0 ; j < answersFileStrings.size() ; j++){
			answers[j] = answersFileStrings.get(j).substring(0, answersFileStrings.get(j).length() - 4);
		}
		UI.answerList.clear();
		UI.answerList.setItems(answers);
	}

	static public ArrayList<String> scanFolder(String folderName) {
		File folder = new File(UI.relativePath + folderName);
		//File folder = new File("./" + folderName);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> listOfFilenames = new ArrayList<>();
		for (File file : listOfFiles){
			if (file.isFile()){
				listOfFilenames.add(file.getName());
			}
		}
		return listOfFilenames;
	}

	/** adds an Image either to the "answer" or "question" folder hence the parameterstring used */
	static public void addImage(String type) {
		Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
		{
			try {
				BufferedImage img =(BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
				ImageIO.write(img, "png",
						new File(UI.relativePath +
								type + "s/"
								+ scanFolder(type + "s").size()
						+ type + ".png"));
			} catch (IOException e) {
				e.getStackTrace();
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			}
		}
	}
}
