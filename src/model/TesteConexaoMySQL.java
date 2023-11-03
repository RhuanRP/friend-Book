package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexaoMySQL {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/friendbook"; 
        String user = "root";
        String password = "123456";
        
        try {

            Connection connection = DriverManager.getConnection(url, user, password);
              
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
            

            connection.close();
        } catch (SQLException e) {
   
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
