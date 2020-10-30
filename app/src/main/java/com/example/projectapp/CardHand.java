package com.example.projectapp;

import java.util.ArrayList;

public class CardHand {
    int[] cards;
    int qntCard;

    public CardHand() {
        this.cards = new int[] {0, 0, 0, 0, 0, 0};
        this.qntCard = 0;
    }

    public int qntCardInHand(){
        return this.qntCard;
    }

    public void move(int position){
        for (int i=position; i<5; i++){
            this.cards[i]=this.cards[i+1];
        }
        this.cards[5]=0;
    }

    public int removeByNum(int num){
        int card=this.cards[num];
        move(num);
        return card;
    }

    public void removeById(int card){
        for (int i=0; i<6; i++){

        }

    }

    public void add(int position, ArrayList<Integer> arr){

    }
}
