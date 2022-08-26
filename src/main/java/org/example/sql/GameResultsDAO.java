package org.example.sql;

import org.example.model.GameResults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emiel Bloem
 * <p>
 * Adds and get data from the MySQL DB.
 */
public class GameResultsDAO extends AbstractDAO{

    public GameResultsDAO(DBAccess dbAccess) {
        super(dbAccess);
    }

    public void saveScore(GameResults gameResults){
        String sql = "INSERT INTO QuizGameTrueOrFalse.scores(`name`, `score`, `category`) VALUES (?, ?, ?)";
        try {
            setupPreparedStatement(sql);
            preparedStatement.setString(1, gameResults.getName());
            preparedStatement.setInt(2, gameResults.getScore());
            preparedStatement.setString(3, gameResults.getCategory());
            executeManipulateStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GameResults> getAllScores(){
        String sql = "SELECT * FROM quizgametrueorfalse.scores order by `id` DESC;";
        List<GameResults> gameResultsList = new ArrayList<>();
        try {
            setupPreparedStatement(sql);
            ResultSet resultSet = executeSelectStatement();
            while (resultSet.next()) {
                gameResultsList.add(new GameResults(resultSet.getString(2), resultSet.getString(4),
                        resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameResultsList;
    }
}
