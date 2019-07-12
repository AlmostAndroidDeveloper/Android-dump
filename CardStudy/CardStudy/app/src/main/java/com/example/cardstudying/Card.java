package com.example.cardstudying;

public class Card {

    String question;
    String answer;
    int level;
    int id;

    Card(int level, String question, String answer, int id) {
        this.level = level;
        this.question = question;
        this.answer = answer;
        this.id = id;
    }
}
