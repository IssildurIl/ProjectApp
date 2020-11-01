package com.example.projectapp;

import java.util.Arrays;
import java.util.HashSet;

public class CardTable {
    int[] cards;
    int[] symbols;
    int qntCard;

    public CardTable() {
        this.cards = new int[] {0, 0, 0};
        this.symbols = new int[] {0, 0, 0};
        this.qntCard = 0;
    }

    public int getCardByNum(int num){
        return cards[num];
    }

    public void setCards(String[] mas){
        this.qntCard=0;
        for (int i=0; i<mas.length; i++){
            this.cards[i]=Integer.parseInt(mas[i]);
            if (this.cards[i]!=0){
                this.qntCard++;
            }
        }
    }

    public int layOutTable(int ... v){
        int res=0;
        for (int i = 0; i < v.length; i++) {
            if (Constants.I_MAIN_CARDS.contains(v[i])){
                this.cards[0]=v[i];
                res++;
                break;
            }
        }
        for (int i = 0; i < v.length; i++) {
            if (Constants.K_MAIN_CARDS.contains(v[i])){
                this.cards[1]=v[i];
                res++;
                break;
            }
        }
        for (int i = 0; i < v.length; i++) {
            if (Constants.D_MAIN_CARDS.contains(v[i])){
                this.cards[2]=v[i];
                res++;
                break;
            }
        }
        this.qntCard=res;
        return res;
    }

    public int addCard2Table(int card){
        if (Constants.I_MAIN_CARDS.contains(card)){
            this.cards[0]=card;
            this.qntCard++;
            return this.qntCard;
        }
        if (Constants.K_MAIN_CARDS.contains(card)){
            this.cards[1]=card;
            this.qntCard++;
            return this.qntCard;
        }
        if (Constants.D_MAIN_CARDS.contains(card)){
            this.cards[2]=card;
            this.qntCard++;
            return this.qntCard;
        }
        return this.qntCard;
    }

    public int qntCardOnTable(){
        return this.qntCard;
    }

    public void cleanTable(){
        this.cards[0]=Constants.I_CARD;
        this.cards[1]=Constants.K_CARD;
        this.cards[2]=Constants.D_CARD;
        for (int i=0; i<3; i++){
            this.symbols[i]=0;
        }
        this.qntCard=0;
    }

    public int[] getCardTable(){
        return this.cards;
    }

    public String getString(){
        String res="";
        for (int i=0; i<3; i++){
            res+=""+this.cards[i]+"*";
        }
        return res;
    }

    public int[] getCardTableWith0(){
        int tempCard[] = new int[3];
        if (this.cards[0]==Constants.I_CARD){
            tempCard[0]=0;
        }else{
            tempCard[0]=this.cards[0];
        }

        if (this.cards[1]==Constants.K_CARD){
            tempCard[1]=0;
        }else{
            tempCard[1]=this.cards[1];
        }

        if (this.cards[2]==Constants.D_CARD){
            tempCard[2]=0;
        }else{
            tempCard[2]=this.cards[2];
        }
        return tempCard;
    }

    public void defAllSymbols(){
        for (int i=0; i<3; i++) {
            this.symbols[i]=defSymbol(this.cards[i]);
        }
    }

    public int defSymbol(int card){
        if (Constants.DARKNESS_MAS.contains(card)){
            return 1;
        }else if (Constants.ELEMENT_MAS.contains(card)){
            return 2;
        }else if (Constants.ILLUSION_MAS.contains(card)){
            return 3;
        }else if (Constants.NATURE_MAS.contains(card)){
            return 4;
        }else if (Constants.SECRET_MAS.contains(card)){
            return 5;
        }else{
            return 0;
        }
    }

    public int countDifSymbol(){
        int res=0;
        if (this.symbols[0]!=0 && this.symbols[0]!=this.symbols[1]){
            res++;
        }
        if (this.symbols[1]!=0 && this.symbols[1]!=this.symbols[1]){
            res++;
        }
        if (this.symbols[2]!=0 && this.symbols[2]!=this.symbols[0]){
            res++;
        }
        return res;
    }

    public int countSymbol(int symbol){
        int res=0;
        for (int i=0; i<3; i++){
            if (this.symbols[0]==symbol){
                res++;
            }
        }
        return res;
    }
}
