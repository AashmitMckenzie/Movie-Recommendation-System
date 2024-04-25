package movie_recommenedation_system;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;
import javax.swing.JFrame;




public class ConnectionProvider {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dbmsaashmit";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "180869420";
    private Connection conn;

    public ConnectionProvider() {    
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection established successfully!");
        }
        catch (SQLException e) {
            throw new IllegalStateException("Unable to connect to the database. " + e.getMessage());
        } 
	}
    
    public void submitFeedback(String fName, String mName, String lName, String phoneNo, String subject, String feedback) {
    try {
        int maxNameLength = 25;
        int maxSubjectLength = 50;
        int maxFeedbackLength = 1000;

        if (fName.length() > maxNameLength || mName.length() > maxNameLength || lName.length() > maxNameLength) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "Error: Name fields must not exceed " + maxNameLength + " characters.");
            System.err.println("Error: Name fields must not exceed " + maxNameLength + " characters.");
            return;
        }

        if (subject.length() > maxSubjectLength) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "Error: Subject field must not exceed " + maxSubjectLength + " characters.");
            System.err.println("Error: Subject field must not exceed " + maxSubjectLength + " characters.");
            return;
        }

        if (feedback.length() > maxFeedbackLength) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "Error: Feedback field must not exceed " + maxFeedbackLength + " characters.");
            System.err.println("Error: Feedback field must not exceed " + maxFeedbackLength + " characters.");
            return;
        }

        if (phoneNo.length() != 10) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "Error: Phone number must be exactly 10 characters.");
            System.err.println("Error: Phone number must be exactly 10 characters.");
            return;
        }

        if (phoneNoExistsInDatabase(phoneNo)) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "Error: Phone number already exists in the database.");
            System.err.println("Error: Phone number already exists in the database.");
            return;
        }

        Statement cSt = conn.createStatement();
        String query = "INSERT INTO dbmsaashmit.feedback (first_name, middle_name, last_name, feedback_subject, feedback_text, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, fName);
        preparedStatement.setString(2, mName);
        preparedStatement.setString(3, lName);
        preparedStatement.setString(4, subject);
        preparedStatement.setString(5, feedback);
        preparedStatement.setString(6, phoneNo);
        preparedStatement.executeUpdate();

        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Feedback submitted successfully!");
        System.out.println("Feedback submitted successfully!");
    } catch (SQLIntegrityConstraintViolationException e) {

        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Error: Feedback already exists.");
        System.err.println("Error: Feedback already exists.");
    } catch (SQLException e) {

        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Error submitting feedback");
        System.err.println("Error submitting feedback: " + e.getMessage());
    }
}

    private boolean phoneNoExistsInDatabase(String phoneNo) {
 
    return false;
}
    public void deleteFeedback(String fName, String mName, String lName, String phoneNo) {
    try {

        PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM feedback WHERE first_name = ? AND middle_name = ? AND last_name = ? AND phone_number = ?");
        deleteStatement.setString(1, fName);
        deleteStatement.setString(2, mName);
        deleteStatement.setString(3, lName);
        deleteStatement.setString(4, phoneNo);

        int rowsDeleted = deleteStatement.executeUpdate();

        if (rowsDeleted > 0) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            JOptionPane.showMessageDialog(frame, "Feedback deleted successfully");
            System.out.println("Feedback deleted successfully");
        } else {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            JOptionPane.showMessageDialog(frame, "No such feedback exists");
            System.out.println("No such feedback exists");
        }
    } catch (SQLException e) {
        System.out.println(e);
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
        JOptionPane.showMessageDialog(frame, "Error deleting feedback");
        System.err.println("Error deleting feedback");
    }
}
    public void updateFeedback(String fName, String mName, String lName, String phoneNo, String newSubject, String newFeedback) {
    try {

        PreparedStatement updateStatement = conn.prepareStatement("UPDATE feedback SET feedback_subject = ?, feeback_text = ? WHERE first_name = ? AND middle_name = ? AND last_name = ? AND phone_number = ?");
        updateStatement.setString(1, newSubject);
        updateStatement.setString(2, newFeedback);
        updateStatement.setString(3, fName);
        updateStatement.setString(4, mName);
        updateStatement.setString(5, lName);
        updateStatement.setString(6, phoneNo);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            JOptionPane.showMessageDialog(frame, "Feedback updated successfully");
            System.out.println("Feedback updated successfully");
        } else {
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            JOptionPane.showMessageDialog(frame, "No such feedback exists");
            System.out.println("No such feedback exists");
        }
    } catch (SQLException e) {
        System.out.println(e);
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
        JOptionPane.showMessageDialog(frame, "Error updating feedback");
        System.err.println("Error updating feedback");
    }
}


    
    public String[][] getDetails(String genere)
    {
        
        String[][] results = new String[10][11];
        try{
            Statement cSt = conn.createStatement();
            ResultSet cRs = cSt.executeQuery("SELECT * FROM genre_" + genere.toLowerCase() +" order by avg_imdb_rt_score desc limit 10");
            
            int i = 0;
            while (cRs.next() && i < 10) {
                results[i][0] = cRs.getString("movie_title");
                results[i][1] = cRs.getString("release_date");
                results[i][2] = cRs.getString("director_name");
                results[i][3] = cRs.getString("actor1_name");
                results[i][4] = cRs.getString("actor2_name");
                results[i][5] = cRs.getString("actor3_name");
                results[i][6] = cRs.getString("gross_income");
                results[i][7] = cRs.getString("budget");
                results[i][8] = cRs.getString("movie_imdb_link");
                results[i][9] = cRs.getString("avg_imdb_rt_score");
                results[i][10] = cRs.getString("Images");
                i++;
            }
            for (int k = 0; k < results.length; k++) {
                for (int j = 0; j < results[k].length; j++) {
                    System.out.print(results[k][j] + " | ");
                }
                    System.out.println();
            }
        } catch (SQLException e)
        {
            System.out.println(e);
            JFrame frame = new JFrame("Swing Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
            JOptionPane.showMessageDialog(frame, "Error fetching database");
            System.err.println("Error fetching database");
        }
        return results;
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
                JFrame frame = new JFrame("Swing Tester");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
                JOptionPane.showMessageDialog(frame, "Error: Failed to close connection.");
                System.err.println("Error: Failed to close connection.");
                e.printStackTrace();
            }
        }
    }
}

