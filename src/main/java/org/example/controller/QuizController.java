package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.model.Game;
import org.example.model.GameResults;
import org.example.sql.GameResultsDAO;

import java.net.URL;
import java.util.*;

/**
 * @author Emiel Bloem
 * <p>
 * With this JavaFX App you can take a quiz from multiple categories. Quiz data is sourced from opentdb
 */
public class QuizController implements Initializable {
    @FXML
    private TextField nameTextField;
    @FXML
    private Label finalScoreLabel;
    @FXML
    private VBox finalScoreBox;
    @FXML
    private ListView<String> listViewStats;
    @FXML
    private ChoiceBox<String> choiceBoxStats;
    @FXML
    private Label categoryLabel;
    @FXML
    private VBox startVBox;
    @FXML
    private ChoiceBox<String> selectCategoryComboBox;
    @FXML
    private VBox gameVbox;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label answersLabel;
    @FXML
    private Label questionNumberLabel;
    private StringBuilder answerEmojis;
    @FXML
    private Label questionLabel;

    private Game game;
    private GameResultsDAO gameResultsDAO;
    private List<GameResults> gameResultsList;
    private Map<String, Integer> categoryMap;

    @FXML
    private void startGame() throws Exception {
        String url = !selectCategoryComboBox.getValue().equals("Any Category")
                ? "https://opentdb.com/api.php?amount=10&category=" +
                categoryMap.get(selectCategoryComboBox.getValue())
                + "&type=boolean" : "https://opentdb.com/api.php?amount=10&type=boolean";

        game = new Game(url);
        questionLabel.setText(game.getCurrentQuestion());
        questionNumberLabel.setText("Question: " + (game.getCurrentQuestionNumber() + 1));
        answerEmojis = new StringBuilder();
        startVBox.setVisible(false);
        gameVbox.setVisible(true);
        questionLabel.setVisible(true);
        finalScoreBox.setVisible(false);
        categoryLabel.setText(selectCategoryComboBox.getValue());
        scoreLabel.setText("Score: " + game.getScore());
        answersLabel.setText("");
    }

    @FXML
    private void goBack() {
        gameVbox.setVisible(false);
        startVBox.setVisible(true);
    }

    @FXML
    private void trueButton() {
        goToNextQuestionAndCheck(true);
    }

    @FXML
    private void falseButton() {
        goToNextQuestionAndCheck(false);
    }

    private void goToNextQuestionAndCheck(boolean button) {
        if (game.getCurrentQuestionNumber() < 9) {
            if (game.checkAnswer(button)) {
                answerEmojis.append("✅ ");
                scoreLabel.setText("Score: " + game.getScore());
            } else {
                answerEmojis.append("❌ ");
            }
            answersLabel.setText(answerEmojis.toString());
            game.nextQuestion();
            questionNumberLabel.setText("Question: " + (game.getCurrentQuestionNumber() + 1));
            questionLabel.setText(game.getCurrentQuestion());
        } else {
            questionLabel.setVisible(false);
            finalScoreBox.setVisible(true);
            finalScoreLabel.setText("Final Score: " + game.getScore());
        }
    }

    @FXML
    private void addScore() {
        GameResults gameResults = new GameResults(nameTextField.getText(), selectCategoryComboBox.getValue(),
                game.getScore());
        try {
            App.getDbAccess().openConnection();
            gameResultsDAO.saveScore(gameResults);
            gameResultsList = gameResultsDAO.getAllScores();
            App.getDbAccess().closeConnection();
            listViewStats.getItems().clear();
            choiceBoxStats.getItems().clear();
            fillChoiceBoxStats();
            setTheList(gameResultsList);
        } catch (Exception e) {
            System.err.println("Error");
        }
        nameTextField.clear();
        gameVbox.setVisible(false);
        startVBox.setVisible(true);
    }

    public String[] fillMapAndArray() {
        categoryMap = new HashMap<>();
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Science & Nature", 17);
        categoryMap.put("Computers", 18);
        categoryMap.put("Mathematics", 19);
        categoryMap.put("Film", 11);
        categoryMap.put("Music", 12);
        categoryMap.put("Television", 14);
        categoryMap.put("Video Games", 15);
        categoryMap.put("Japanese Anime & Manga", 31);
        categoryMap.put("Cartoon & Animations", 32);
        categoryMap.put("Mythology", 20);
        categoryMap.put("Sports", 21);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);
        categoryMap.put("Politics", 24);
        categoryMap.put("Animals", 27);
        categoryMap.put("Vehicles", 28);
        String[] categories = new String[categoryMap.size() + 1];
        categories[0] = "Any Category";
        int count = 1;
        for (String s : categoryMap.keySet()) {
            categories[count] = s;
            count++;
        }
        return categories;
    }

    private void fillChoiceBoxStats() {
        List<String> checkIfInChoiceBoxList = new ArrayList<>();
        choiceBoxStats.getItems().add("All");
        for (GameResults gameResults : gameResultsList) {
            if (!checkIfInChoiceBoxList.contains(gameResults.getCategory())) {
                checkIfInChoiceBoxList.add(gameResults.getCategory());
                choiceBoxStats.getItems().add(gameResults.getCategory());
            }
        }
        choiceBoxStats.setValue("All");
    }

    private void setTheList(List<GameResults> gameResultsList) {
        String[] gameResultsArray = new String[gameResultsList.size()];
        int count = 0;
        for (int i = 0; i < gameResultsArray.length; i++) {
            gameResultsArray[i] = gameResultsList == this.gameResultsList ? gameResultsList.get(i).toString() :
                    ++count + ": " + gameResultsList.get(i).getName() +" | score: " + gameResultsList.get(i).getScore();
        }
        listViewStats.getItems().addAll(gameResultsArray);
    }

    private void showSelectedCategory() {
        choiceBoxStats.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            List<GameResults> selectedItemList = new ArrayList<>();
            listViewStats.getItems().clear();
            try {
                if (choiceBoxStats.getValue().equals("All")) {
                    setTheList(gameResultsList);
                } else {
                    for (GameResults gameResults : gameResultsList) {
                        if (gameResults.getCategory().equals(choiceBoxStats.getValue())) {
                            selectedItemList.add(gameResults);
                        }
                    }
                    Collections.sort(selectedItemList);
                    setTheList(selectedItemList);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectCategoryComboBox.getItems().addAll(fillMapAndArray());
        selectCategoryComboBox.setValue(fillMapAndArray()[0]);
        try {
            App.getDbAccess().openConnection();
            gameResultsDAO = new GameResultsDAO(App.getDbAccess());
            gameResultsList = gameResultsDAO.getAllScores();
            App.getDbAccess().closeConnection();
            setTheList(gameResultsList);
            fillChoiceBoxStats();
            showSelectedCategory();
        } catch (Exception e) {
            listViewStats.getItems().add("SQL Error: " + e);
        }
        startVBox.setVisible(true);
        gameVbox.setVisible(false);
    }
}
