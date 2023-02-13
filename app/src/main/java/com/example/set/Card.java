package com.example.set;

public class Card {
    private int cardColor;
    private int numberOfShapes;
    private int shading;
    private int shape;
    public final static Card[] deck=formDeck();

    private static Card[] formDeck() {
        Card[]deck=new Card[81];
        int i =0;
        for (int c=1; c<=3;c++){
            for (int n=1;n<=3;n++){
                for (int shad=1;shad<=3;shad++){
                    for (int sh=1;sh<=3;sh++){
                        deck[i]=new Card(c,n,shad,sh);
                        i++;
                    }
                }
            }
        }
        return deck;
    }


    public Card(int color, int numberOfShapes, int shading, int shape) {
        this.cardColor = color;
        this.numberOfShapes = numberOfShapes;
        this.shading = shading;
        this.shape = shape;
    }

    @Override
    public String toString() {
        return  cardColor +""+ numberOfShapes + shading + shape;
    }

    public int getCardColor() {
        return cardColor;
    }

    public int getNumberOfShapes() {
        return numberOfShapes;
    }

    public int getShading() {
        return shading;
    }

    public int getShape() {
        return shape;
    }
}
