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
		generateQuestionTextArea();
		generateAnswerTextArea();
		UI.resetStatistics();
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
		if (wrongIndexList.size() == 0){
			initializeWrongIndexList();
		}
		int k = new Random().nextInt((wrongIndexList.size()));
		if (k == rngIndex && wrongIndexList.size() > 1){
			return generateRandomIndexFromWrong();
		}
		else{
			return wrongIndexList.get(k);
		}
	}


	static public void generateActiveQuestionList(){
		activeQuestionTextArea.clear();
		StringBuilder activeQuestions = new StringBuilder();
		for (int j = 0 ; j < questionsFileStrings.size() ; j++){
			if (wrongIndexList.contains(j)) {
				activeQuestions.append(questionsFileStrings.get(j), 0, questionsFileStrings.get(j).length() - 4);
				activeQuestions.append("\n");
			}
		}
		activeQuestionTextArea.setText(activeQuestions.toString());
	}

	static public void generateQuestionTextArea(){
		questionTextArea.clear();
		StringBuilder questions = new StringBuilder();
		for (String questionsFileString : questionsFileStrings) {
			questions.append(questionsFileString, 0, questionsFileString.length() - 4);
			questions.append("\n");
		}
		questionTextArea.setText(questions.toString());
	}

	static public void generateAnswerTextArea(){
		answerTextArea.clear();
		StringBuilder answers = new StringBuilder();
		for (String answersFileString : answersFileStrings) {
			answers.append(answersFileString, 0, answersFileString.length() - 4);
			answers.append("\n");
		}
		answerTextArea.setText(answers.toString());
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
