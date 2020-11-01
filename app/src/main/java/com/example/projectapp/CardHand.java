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

    public int[] getCards(){
        return this.cards;
    }

    public int getCardByNum(int num){
        return this.cards[num];
    }

    public String getString(){
        String res="";
        for (int i=0; i<6; i++){
            res+=this.cards[i]+"+";
        }
        return res;
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

    public void move(int position){
        for (int i=position; i<5; i++){
            this.cards[i]=this.cards[i+1];
        }
        this.cards[5]=0;
    }

    public int removeByNum(int num){
        int card=this.cards[num];
        move(num);
        this.qntCard--;
        return card;
    }

    public int removeById(int card){
        for (int i=0; i<6; i++){
            if (card==this.cards[i]){
                move(i);
                this.qntCard--;
                return i;
            }
        }
        return -1;
    }

    public void removeMasId(int[] mas){
        for (int i=0; i<3; i++){
            if (mas[i]!=0){
                removeById(mas[i]);
            }
        }
    }

    public int takeCards(int position, ArrayList<Integer> arr){
        /*
        int stop=0;
        if ((arr.size()-position) > (6-this.qntCard)){
            stop=6-this.qntCard;
        }else{
            stop=arr.size()-position;
        }
        for (int i=this.qntCard; i<stop; i++){
            this.cards[i]=arr.get(i+position);
            this.qntCard++;
        }
        return position+stop;
              */
        int iterator=0;
        try{
            for (iterator=this.qntCard; iterator<6; iterator++){
                this.cards[iterator]=arr.get(iterator+position);
                this.qntCard++;
            }
        } catch(Exception e){

        }finally {
            return position+iterator;
        }
    }
}
