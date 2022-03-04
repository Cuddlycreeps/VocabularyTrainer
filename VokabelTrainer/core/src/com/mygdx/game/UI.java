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
import java.util.ArrayList;

public class UI extends ApplicationAdapter {

    private Skin skin;
    private Stage stage;
    private Table tableLeft;
    private ImageButton rightButton, wrongButton, showSolutionButton;
    private TextButton readMeButton;
    private Image question, answer;
    private ArrayList<Texture> questionsTextures = new ArrayList<>();
    private ArrayList<Texture> answersTextures = new ArrayList<>();
    private SpriteDrawable rightSprite, rightSpritePushed, wrongSprite, wrongSpritePushed, solutionSprite, solutionSpritePushed;
    private Image uiBackground;
    private Label readMeLabel;
    public static Label questionListLabel;
    private String readMe = "test";
    static int rngIndex;
    private Window readMeWindow;




    @Override
    public void create() {
        //fonts
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        //stage
        stage = new Stage(new ScreenViewport());
        //buttonsprites
        rightSprite = new SpriteDrawable(new Sprite(new Texture("./assets/right.png")));
        rightSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/right_pushed.png")));
        wrongSprite = new SpriteDrawable(new Sprite(new Texture("./assets/wrong.png")));
        wrongSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/wrong_pushed.png")));
        solutionSprite = new SpriteDrawable(new Sprite(new Texture("./assets/solution.png")));
        solutionSpritePushed = new SpriteDrawable(new Sprite(new Texture("./assets/solution_pushed.png")));
        //readme
        FileHandle handle = new FileHandle("./assets/readme.txt");
        readMe = handle.readString();
        //background
        uiBackground = new Image(new Texture("./assets/uibackground.png"));
        uiBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(uiBackground);


        for(String fileName : Backend.questionsFileStrings){
            questionsTextures.add(new Texture("./assets/questions/" + fileName));
        }
        for(String fileName : Backend.answersFileStrings){
            answersTextures.add(new Texture("./assets/answers/" + fileName));
        }



        //leftTable
        tableLeft = new Table();
        tableLeft.setWidth(stage.getWidth()/8);
        tableLeft.align(Align.center | Align.top);
        tableLeft.setPosition(0, Gdx.graphics.getHeight());
        tableLeft.padTop(50);

        //label for the questionlist
        questionListLabel = new Label("", skin);
        questionListLabel.setBounds(25,25,Gdx.graphics.getWidth()/8f, Gdx.graphics.getHeight()*(1f/3f));
        Backend.generateQuestionList();
        stage.addActor(questionListLabel);

        //readme
        readMeLabel = new Label(readMe, skin);

        readMeWindow = new Window("Instructions", skin);
        readMeWindow.setBounds(50, 50 ,  Gdx.graphics.getWidth()/6f,  Gdx.graphics.getHeight()/1.5f);
        readMeWindow.add(readMeLabel);
        readMeWindow.setVisible(false);

        readMeButton = new TextButton("Read Me", skin);

        readMeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                readMeWindow.setVisible(!readMeWindow.isVisible());
            }
        });

        //Buttons
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

        tableLeft.add(rightButton).padBottom(20);
        tableLeft.add(wrongButton).padBottom(20).padLeft(20);

        tableLeft.row();

        showSolutionButton = new ImageButton(solutionSprite, solutionSpritePushed);
        showSolutionButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
                answer.setVisible(true);
                rightButton.setVisible(true);
                wrongButton.setVisible(true);
           }
        });
        tableLeft.add(showSolutionButton);

        tableLeft.row();
        tableLeft.add(readMeButton).padTop(30f);

        showNextQuestion();

        stage.addActor(tableLeft);
        stage.addActor(readMeWindow);

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

    private void showNextQuestion(){
        if (question != null) {
            question.remove();
        }
        rngIndex = Backend.generateRandomIndexFromWrong();
        System.out.println(rngIndex);
        question = new Image(questionsTextures.get(rngIndex));
        question.setName("questionActor");
        question.setPosition(((Gdx.graphics.getWidth() + Gdx.graphics.getWidth() / 8f)/2f) - (questionsTextures.get(rngIndex).getWidth()/2f),
                Gdx.graphics.getHeight() - (questionsTextures.get(rngIndex).getHeight() + questionsTextures.get(rngIndex).getHeight()*0.1f));
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
        stage.addActor(answer);
    }
}
