package com.example.sandbox;

public class Card {

    String question;
    String answer;
    int level;

    Card(int level, String question, String answer) {
        this.level = level;
        this.question = question;
        this.answer = answer;
    }
}
