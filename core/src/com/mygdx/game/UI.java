package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;
import java.util.ArrayList;

public class UI extends ApplicationAdapter {

    private Skin skin;
    private Stage stage;
    private Table leftTable, addCardsButtonTable, addCardsItemsTable;
    private ImageButton rightButton, wrongButton, showSolutionButton;
    private TextButton addCardsButton, readMeButton, addQuestionButton, addAnswerButton, closeAddCardsButton, readMeButtonMenu;
    private Image question, answer;
    private ArrayList<Texture> questionsTextures = new ArrayList<>();
    private ArrayList<Texture> answersTextures = new ArrayList<>();
    private SpriteDrawable rightSprite, rightSpritePushed, wrongSprite, wrongSpritePushed, solutionSprite, solutionSpritePushed;
    private Image uiBackground;
    private Label readMeLabel;
    private ScrollPane questionScrollPane, answerScrollPane;
    public static List questionList, answerList;
    private String readMe;
    static int rngIndex;
    private Window readMeWindow, addCardsWindow;
    static String relativePath;

    static {
        if (new File("./assets").exists()) {
            relativePath = "./assets/";
        } else {
            relativePath = "./core/assets/";
        }
    }



    @Override
    public void create() {
        //fonts
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        //stage
        stage = new Stage(new ScreenViewport());
        //buttonsprites
        //rightSprite = new SpriteDrawable(new Sprite(new Texture("./assets/right.png")));
        rightSprite = new SpriteDrawable(new Sprite(new Texture(relativePath + "right.png")));
        //rightSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/right_pushed.png")));
        rightSpritePushed = new SpriteDrawable(new Sprite(new Texture(relativePath + "right_pushed.png")));
        //wrongSprite = new SpriteDrawable(new Sprite(new Texture("./assets/wrong.png")));
        wrongSprite = new SpriteDrawable(new Sprite(new Texture(relativePath + "wrong.png")));
        //wrongSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/wrong_pushed.png")));
        wrongSpritePushed = new SpriteDrawable(new Sprite(new Texture( relativePath + "wrong_pushed.png")));
        //solutionSprite = new SpriteDrawable(new Sprite(new Texture("./assets/solution.png")));
        solutionSprite = new SpriteDrawable(new Sprite(new Texture(relativePath + "solution.png")));
        //solutionSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/solution_pushed.png")));
        solutionSpritePushed = new SpriteDrawable(new Sprite(new Texture(relativePath + "solution_pushed.png")));

        //readme
        //FileHandle handle = new FileHandle("./assets/readme.txt");
        FileHandle handle = new FileHandle(relativePath + "readme.txt");
        readMe = handle.readString();

        //background
        //uiBackground = new Image(new Texture("./assets/uibackground.png"));
        uiBackground = new Image(new Texture(relativePath + "uibackground.png"));
        uiBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(uiBackground);

        loadTextures();

        //leftTable
        leftTable = new Table();
        leftTable.setWidth(stage.getWidth()/8);
        leftTable.align(Align.center | Align.top);
        leftTable.setPosition(0, Gdx.graphics.getHeight());
        leftTable.pad(20);

        // *** ADD CARDS TABLES AND WINDOW***
        addCardsButtonTable = new Table();
        addCardsButtonTable.setWidth(stage.getWidth()/8);
        addCardsButtonTable.align(Align.top);
        addCardsButtonTable.setPosition(0,0);
        addCardsButtonTable.padTop(50f);

        addCardsItemsTable = new Table();
        addCardsItemsTable.setWidth(stage.getWidth()/8);
        addCardsItemsTable.align(Align.top);
        addCardsItemsTable.setPosition(0,0);
        addCardsItemsTable.padTop(50f);

        addCardsWindow = new Window("Add cards", skin);
        addCardsWindow.setBounds(Gdx.graphics.getWidth()/6f, 0,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/6f, Gdx.graphics.getHeight());
        addCardsWindow.add(addCardsButtonTable).align(Align.top);
        addCardsWindow.row();
        addCardsWindow.add(addCardsItemsTable).align(Align.bottom);
        addCardsWindow.setMovable(false);
        addCardsWindow.setVisible(false);





        // *** README ***
        readMeLabel = new Label(readMe, skin);

        readMeWindow = new Window("Instructions", skin);
        readMeWindow.setBounds(0, 0 ,  Gdx.graphics.getWidth()/6f,  Gdx.graphics.getHeight()/1.5f);
        readMeWindow.add(readMeLabel);
        readMeWindow.setMovable(false);
        readMeWindow.setVisible(false);

        readMeButton = new TextButton("Read Me", skin);
        readMeButton.pad(15f,20f,15f,20f);
        readMeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                readMeWindow.setVisible(!readMeWindow.isVisible());
            }
        });

        readMeButtonMenu = new TextButton("Read Me", skin);
        readMeButtonMenu.pad(20f,100f,20f,100f);
        readMeButtonMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                readMeWindow.setVisible(!readMeWindow.isVisible());
            }
        });
        addCardsButtonTable.add(readMeButtonMenu);


        // *** BUTTONS ***
        rightButton = new ImageButton(rightSprite, rightSpritePushed);
        rightButton.setVisible(false);
        rightButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Backend.wrongIndexList.remove(rngIndex);
                rightButton.setVisible(false);
                wrongButton.setVisible(false);
                showNextQuestion();
            }
        });


        wrongButton = new ImageButton(wrongSprite, wrongSpritePushed);
        wrongButton.setVisible(false);
        wrongButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rightButton.setVisible(false);
                wrongButton.setVisible(false);
                showNextQuestion();
            }
        });

        leftTable.add(rightButton).padBottom(20);
        leftTable.add(wrongButton).padBottom(20).padLeft(20);

        leftTable.row();

        showSolutionButton = new ImageButton(solutionSprite, solutionSpritePushed);
        showSolutionButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
                answer.setVisible(true);
                rightButton.setVisible(true);
                wrongButton.setVisible(true);
           }
        });
        leftTable.add(showSolutionButton);

        leftTable.row();

        addCardsButton = new TextButton("Add cards", skin);
        addCardsButton.pad(15f,20f,15f,20f);
        addCardsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCardsWindow.setVisible(!addCardsWindow.isVisible());
                question.setVisible(!question.isVisible());
                answer.setVisible(false);
                if (question.isVisible()){
                    showNextQuestion();
                    Backend.reinitialize();
                }
            }
        });
        leftTable.add(addCardsButton).padTop(5f);
        leftTable.add(readMeButton).padTop(5f);

        addQuestionButton = new TextButton("Add question" , skin);
        addQuestionButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Backend.addImage("question");
                addTextureTo("question");
            }
        });
        addQuestionButton.pad(20f,100f,20f,100f);
        addCardsButtonTable.add(addQuestionButton);

        addAnswerButton = new TextButton("Add answer" , skin);
        addAnswerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Backend.addImage("answer");
                addTextureTo("answer");
            }
        });
        addAnswerButton.pad(20f,100f,20f,100f);
        addCardsButtonTable.add(addAnswerButton);

        closeAddCardsButton = new TextButton("Done adding", skin);
        closeAddCardsButton.pad(20f,100f,20f,100f);
        closeAddCardsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                addCardsWindow.setVisible(false);
                readMeWindow.setVisible(false);
                showNextQuestion();
                Backend.reinitialize();
            }
        });
        addCardsButtonTable.add(closeAddCardsButton);




        // *** LISTS ***
        questionList = new List<String>(skin);
        Backend.generateQuestionList();

        answerList = new List<String>(skin);
        Backend.generateAnswerList();


        // *** SCROLLPANES ***
        questionScrollPane = new ScrollPane(questionList);
        addCardsItemsTable.add(questionScrollPane).pad(50);

        answerScrollPane = new ScrollPane(answerList);
        addCardsItemsTable.add(answerScrollPane).pad(50);


        showNextQuestion();

        stage.addActor(leftTable);
        stage.addActor(readMeWindow);
        stage.addActor(addCardsWindow);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.258f, 0.258f, 0.258f, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        for (Texture t : questionsTextures){
            t.dispose();
        }
        for (Texture t : answersTextures){
            t.dispose();
        }
    }

    private void loadTextures(){
        for(String fileName : Backend.questionsFileStrings){
                    //questionsTextures.add(new Texture("./assets/questions/" + fileName));
                    questionsTextures.add(new Texture(relativePath + "questions/" + fileName));
        }

        for(String fileName : Backend.answersFileStrings){
                    //answersTextures.add(new Texture("./assets/questions/" + fileName));
                    answersTextures.add(new Texture(relativePath + "answers/" + fileName));
        }
    }

    private void addTextureTo(String type){
        if (type.equals("question")){
            questionsTextures.add(new Texture(relativePath + "questions/" +
                    Backend.questionsFileStrings.get(Backend.questionsFileStrings.size() -1)));
        }
        if (type.equals("answer")){
            answersTextures.add(new Texture(relativePath + "answers/" +
                    Backend.answersFileStrings.get(Backend.answersFileStrings.size() -1)));
        }
        Backend.reinitialize();
    }

    private void showNextQuestion(){

        rightButton.setVisible(false);
        wrongButton.setVisible(false);
        if (question != null) {
            question.remove();
        }
        rngIndex = Backend.generateRandomIndexFromWrong();
        question = new Image(questionsTextures.get(rngIndex));
        question.setName("questionActor");
        question.setPosition(((Gdx.graphics.getWidth() + Gdx.graphics.getWidth() / 8f)/2f) - (questionsTextures.get(rngIndex).getWidth()/2f),
                Gdx.graphics.getHeight() - (questionsTextures.get(rngIndex).getHeight() + Gdx.graphics.getHeight() *0.1f));
        stage.addActor(question);

        if(answer != null){
            answer.remove();
        }
        answer = new Image(answersTextures.get(rngIndex));
        answer.setName("answerActor");
        answer.setPosition(((Gdx.graphics.getWidth() + Gdx.graphics.getWidth() / 8f)/2f) - (answersTextures.get(rngIndex).getWidth()/2f),
                Gdx.graphics.getHeight() * 0.05f);
        answer.setVisible(false);
        Backend.generateQuestionList();
        Backend.generateAnswerList();
        stage.addActor(answer);
    }
}