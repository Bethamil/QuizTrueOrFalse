package org.example.model;

import com.google.gson.Gson;
import org.jsoup.Jsoup;

/**
 * @author Emiel Bloem
 * <p>
 * Gets a Json file from the opentdb and parses it in a jsonOpenTDB object. Also check if the quiz answer was right etc.
 */
public class Game {
    private int score, currentQuestionNumber;
    private JsonOpenTDB jsonOpenTDB;
    private String url, currentQuestion;
    private boolean rightAnswer;


    public Game(String url) throws Exception {
        this.url = url;
        fillQuestionsAnswers();
    }

    private void fillQuestionsAnswers() throws Exception {
        String json = JsonOpenTDB.readUrl(url);
        Gson gson = new Gson();
        jsonOpenTDB = gson.fromJson(json, JsonOpenTDB.class);
    }

    public void nextQuestion() {
        currentQuestionNumber++;
    }

    public boolean checkAnswer(boolean userAnswer) {
        rightAnswer = Boolean.parseBoolean(jsonOpenTDB.results.get(currentQuestionNumber).correct_answer);
        if (userAnswer == rightAnswer) {
            score++;
            return true;
        }
        return false;
    }

    public String getCurrentQuestion() {
        currentQuestion = Jsoup.parse(jsonOpenTDB.results.get(currentQuestionNumber).question).text();
        return currentQuestion;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

    public int getScore() {
        return score;
    }
}
