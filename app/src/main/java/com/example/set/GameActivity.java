package com.example.set;

import static com.example.set.R.color.ImageButtonPressed;
import static com.example.set.R.color.ImageButtonShadow;
import static com.example.set.R.color.RightSet;
import static com.example.set.R.color.WrongSet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.os.Bundle;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GameActivity extends AppCompatActivity {
    public static final String NEW_GAME_PRESSED = "newGamePressed";
    //indexes of cards in GameDeck
    Set<Integer> inGameDeck = new HashSet<>();
    //key is number if imageButton, value is cards index
    HashMap<Integer,Integer> onTable = new HashMap<>();
    //indexes of chosen imageButtons
    Set<Integer> chosenCards = new HashSet<>();
    int foundSetsCount;
    int[] hintCard = new int[2];
    int hintCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setSizes();
        boolean newGamePressed = getIntent().getBooleanExtra(NEW_GAME_PRESSED,false);
        if (newGamePressed){
            newGame();
        } else {
            resumeGame();
        }

    }

    private void resumeGame() {

        FileInputStream fis;
        try {
            fis = openFileInput("saveGame");

        } catch (FileNotFoundException e) {
            newGame();
            return;
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            hintCount= Integer.parseInt(line);
            for (int i=1; i<=12; i++){
                line = reader.readLine();
                int value = Integer.parseInt(line);
                if (value!=-1)onTable.put(i,value);
            }
            inGameDeck.clear();
            line = reader.readLine();
            while (line != null) {
                inGameDeck.add(Integer.parseInt(line));
                line = reader.readLine();
            }
            foundSetsCount=(81-inGameDeck.size())/3;
            showCardsOnTable();
        } catch (IOException e) {
            newGame();
        }
    }

    private void setSizes() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Button buttonCheckSet = findViewById(R.id.buttonCheckSet);
        setButtonSize(buttonCheckSet,height,width);
        Button buttonHint = findViewById(R.id.buttonHint);
        setButtonSize(buttonHint,height,width);
        Button buttonShuffle = findViewById(R.id.buttonShuffle);
        setButtonSize(buttonShuffle,height,width);
        Button buttonRules = findViewById(R.id.buttonRules);
        setButtonSize(buttonRules,height,width);
        Button buttonNewGame= findViewById(R.id.buttonNewGame);
        setButtonSize(buttonNewGame,height,width);
        for(int i=1; i<=12; i++){
            String imageButtonName = "imageButton"+ i;
            int buttonID = getResources().getIdentifier(imageButtonName, "id",getPackageName());
            ImageButton cardButton = findViewById(buttonID);
            setCardButtonSize(cardButton,height,width);
        }
        TextView textViewDeck = findViewById(R.id.textViewDeck);
        textViewDeck.setTextSize((float) (height*0.0125));
        TextView textViewSets = findViewById(R.id.textFoundSets);
        textViewSets.setTextSize((float) (height*0.0125));
    }

    private void setCardButtonSize(ImageButton cardButton, int height, int width) {
        android.view.ViewGroup.LayoutParams params = cardButton.getLayoutParams();
        params.height = (int) (height*0.13);
        params.width = (int) (width*0.3);
        cardButton.setLayoutParams(params);

    }

    private void setButtonSize(Button button, int height, int width) {

        button.setTextSize((int) (width/74));
        button.setWidth((int) (width*0.28));
        button.setHeight((int) (height*0.08));
    }

    private void newGame() {
        hintCount=3;
        inGameDeck.clear();
        onTable.clear();
        foundSetsCount=0;
        Button hintButton = findViewById(R.id.buttonHint);
        hintButton.setVisibility(View.GONE);
        Button shuffleButton = findViewById(R.id.buttonShuffle);
        shuffleButton.setVisibility(View.GONE);
        for (int i = 0; i<=80;i++)inGameDeck.add(i);
        int i=1;
        Random rand = new Random();
        while (i<=12){
            int cardIndex=rand.nextInt(81);
            if (!onTable.containsValue(cardIndex)){
                onTable.put(i,cardIndex);
                i++;
            }
        }
        showCardsOnTable();
    }

    @SuppressLint("SetTextI18n")
    private void showCardsOnTable() {
        Button hintButton = findViewById(R.id.buttonHint);
        hintButton.setVisibility(View.GONE);
        Button shuffleButton = findViewById(R.id.buttonShuffle);
        shuffleButton.setVisibility(View.GONE);
        for (int i=1; i<=12; i++){
                String imageButtonName = "imageButton"+ i;
                int buttonID = getResources().getIdentifier(imageButtonName, "id",getPackageName());
                ImageButton cardButton = findViewById(buttonID);
                String imageName;
            if (onTable.containsKey(i)) {
                imageName = "card_" + Card.deck[onTable.get(i)].toString();
            }else{
                imageName = "card_empty";
            }
                int resID = getResources().getIdentifier(imageName,
                        "drawable", getPackageName());
                cardButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(ImageButtonShadow)));
                cardButton.setImageResource(resID);


        }
        TextView textDeck = findViewById(R.id.textViewDeck);
        textDeck.setText(getString(R.string.deck_colon)+inGameDeck.size()+ getString(R.string.cards_dot));
        TextView textSet = findViewById(R.id.textFoundSets);
        textSet.setText(getString(R.string.found_colon) + foundSetsCount + getString(R.string.sets_dot) );
        Button button = findViewById(R.id.buttonCheckSet);
        button.setText(R.string.check_set);
    }


    public void NewGameClickGameActivity(View view) {
        newGame();
    }

    @SuppressLint("ResourceAsColor")




    private void ImageButtonClick(int i) {
        if (chosenCards.size()<3 && onTable.containsKey(i)){
            String imageButtonName = "imageButton"+ i;
            int buttonID = getResources().getIdentifier(imageButtonName, "id",getPackageName());
            ImageButton clickedButton = findViewById(buttonID);
            if(chosenCards.contains(i)){
                clickedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(ImageButtonShadow)));
                chosenCards.remove(i);
            }else{
                clickedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(ImageButtonPressed)));
                chosenCards.add(i);
            }
            if(chosenCards.size()==3)checkIfSet();
        }

    }

    private void checkIfSet() {
        Button hintButton = findViewById(R.id.buttonHint);
        hintButton.setVisibility(View.GONE);
        int colorSum=0, numberSum=0,shadingSum=0,shapeSum=0;
        for (int i:chosenCards){
            colorSum+=Card.deck[onTable.get(i)].getCardColor();
            numberSum+=Card.deck[onTable.get(i)].getNumberOfShapes();
            shadingSum+=Card.deck[onTable.get(i)].getShading();
            shapeSum+=Card.deck[onTable.get(i)].getShape();
        }
        if(colorSum%3==0 && numberSum%3==0 && shadingSum%3==0 && shapeSum%3==0){
            for (int i:chosenCards){
                String imageButtonName = "imageButton"+ i;
                int buttonID = getResources().getIdentifier(imageButtonName, "id",getPackageName());
                ImageButton clickedButton = findViewById(buttonID);
                clickedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(RightSet)));
                inGameDeck.remove(onTable.get(i));
                dealNewCard(i);
            }
            foundSetsCount++;

        }else {
            for (int i:chosenCards){
                String imageButtonName = "imageButton"+ i;
                int buttonID = getResources().getIdentifier(imageButtonName, "id",getPackageName());
                ImageButton clickedButton = findViewById(buttonID);
                clickedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(WrongSet)));
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            chosenCards.clear();
            showCardsOnTable();
        }, 2000);


    }

    private void dealNewCard(int i) {
        boolean needToDeal=true;
        if (inGameDeck.size()<12) {
            needToDeal = false;
            onTable.remove(i);
        }
        List<Integer> deckList = new ArrayList<>(inGameDeck);
        Random rand = new Random();
        while (needToDeal) {

            int cardIndex = deckList.get(rand.nextInt(deckList.size()));
            if (!onTable.containsValue(cardIndex)) {
                onTable.put(i, cardIndex);
                needToDeal = false;
            }
        }
    }

    public void onImageButton1Click(View view) {
        ImageButtonClick(1);
    }
    public void onImageButton2Click(View view) {
        ImageButtonClick(2);
    }

    public void onImageButton3Click(View view) {
        ImageButtonClick(3);
    }

    public void onImageButton4Click(View view) {
        ImageButtonClick(4);
    }

    public void onImageButton5Click(View view) {
        ImageButtonClick(5);
    }

    public void onImageButton6Click(View view) {
        ImageButtonClick(6);
    }

    public void onImageButton7Click(View view) {
        ImageButtonClick(7);
    }

    public void onImageButton8Click(View view) {
        ImageButtonClick(8);
    }

    public void onImageButton9Click(View view) {
        ImageButtonClick(9);
    }

    public void onImageButton10Click(View view) {
        ImageButtonClick(10);
    }

    public void onImageButton11Click(View view) {
        ImageButtonClick(11);
    }

    public void onImageButton12Click(View view) {
        ImageButtonClick(12);
    }

    public void onShuffleClick(View view) {
        onTable.clear();
        chosenCards.clear();
        int imageButtonIndex = 1;
        List<Integer> deckList = new ArrayList<>(inGameDeck);
        Random rand = new Random();
        while (imageButtonIndex<=12 && imageButtonIndex <= inGameDeck.size()){
            int index = rand.nextInt(deckList.size());
            int cardIndex = deckList.get(index);
            onTable.put(imageButtonIndex,cardIndex);
            deckList.remove(index);
            imageButtonIndex++;
        }
        showCardsOnTable();
    }

    @SuppressLint("SetTextI18n")
    public void onCheckSetClick(View view) {
        Button button = findViewById(R.id.buttonCheckSet);
        if(checkSetsOnTable()){
            button.setText(R.string.there_is_a_set);
            if (hintCount>0){
                Button hintButton = findViewById(R.id.buttonHint);
                hintButton.setText(getString(R.string.hint_bracket) + hintCount + getString(R.string.bracket));
                hintButton.setVisibility(View.VISIBLE);
            }
        }else {
            button.setText(R.string.no_sets);
            Button shuffleButton = findViewById(R.id.buttonShuffle);
            shuffleButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkSetsOnTable() {
        int colorSum=0, numberSum=0,shadingSum=0,shapeSum=0;
        for(int i = 1; i<=10; i++){
            if (onTable.containsKey(i)){
                colorSum+=Card.deck[onTable.get(i)].getCardColor();
                numberSum+=Card.deck[onTable.get(i)].getNumberOfShapes();
                shadingSum+=Card.deck[onTable.get(i)].getShading();
                shapeSum+=Card.deck[onTable.get(i)].getShape();
                for (int j=i+1; j<=11; j++){
                    if(onTable.containsKey(j)){
                        colorSum+=Card.deck[onTable.get(j)].getCardColor();
                        numberSum+=Card.deck[onTable.get(j)].getNumberOfShapes();
                        shadingSum+=Card.deck[onTable.get(j)].getShading();
                        shapeSum+=Card.deck[onTable.get(j)].getShape();
                        for (int k=j+1; k<=12;k++){
                            if(onTable.containsKey(k)){
                                colorSum+=Card.deck[onTable.get(k)].getCardColor();
                                numberSum+=Card.deck[onTable.get(k)].getNumberOfShapes();
                                shadingSum+=Card.deck[onTable.get(k)].getShading();
                                shapeSum+=Card.deck[onTable.get(k)].getShape();
                                if(colorSum%3==0 && numberSum%3==0 && shadingSum%3==0 && shapeSum%3==0){
                                    hintCard[0]=i;
                                    hintCard[1]=k;
                                    return true;
                                }
                                colorSum-=Card.deck[onTable.get(k)].getCardColor();
                                numberSum-=Card.deck[onTable.get(k)].getNumberOfShapes();
                                shadingSum-=Card.deck[onTable.get(k)].getShading();
                                shapeSum-=Card.deck[onTable.get(k)].getShape();
                            }

                        }
                        colorSum-=Card.deck[onTable.get(j)].getCardColor();
                        numberSum-=Card.deck[onTable.get(j)].getNumberOfShapes();
                        shadingSum-=Card.deck[onTable.get(j)].getShading();
                        shapeSum-=Card.deck[onTable.get(j)].getShape();
                    }

                }
                colorSum-=Card.deck[onTable.get(i)].getCardColor();
                numberSum-=Card.deck[onTable.get(i)].getNumberOfShapes();
                shadingSum-=Card.deck[onTable.get(i)].getShading();
                shapeSum-=Card.deck[onTable.get(i)].getShape();
            }

        }
        return false;
    }

    public void onHintClick(View view) {
        chosenCards.clear();
        showCardsOnTable();
        ImageButtonClick(hintCard[0]);
        ImageButtonClick(hintCard[1]);
        hintCount-=1;


    }

    public void onRulesClick(View view) {
        Intent intent = new Intent(GameActivity.this,RulesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveGame();
    }

    private void saveGame() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hintCount).append('\n');
        for (int i=1; i<=12; i++){
            if (onTable.containsKey(i)){
                stringBuilder.append(onTable.get(i).toString()).append('\n');
            }else {
                stringBuilder.append(onTable.get(-1).toString()).append('\n');
            }

        }
        for (int i: inGameDeck){
            stringBuilder.append(i).append('\n');
        }
        String fileContents = stringBuilder.toString();
        try (FileOutputStream fos = openFileOutput("saveGame", Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}