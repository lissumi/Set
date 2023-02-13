package com.example.set;

import static com.example.set.R.color.ImageButtonPressed;
import static com.example.set.R.color.ImageButtonShadow;
import static com.example.set.R.color.RightSet;
import static com.example.set.R.color.WrongSet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;

import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GameActivity extends AppCompatActivity {
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
        newGame();
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
}