/*
    Descrição:
        Essa é classe responsavel por fazer estabelecer a conexão 
        com o banco de dados. Basicamente quando se precisa estabelelecer
        uma conexão com banco, basta chamar essa classe que ira retornar 
        um connection.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 5/12/2021
    Data de atualização: 5/12/2021
*/

import java.sql.*;

public class DatabaseConnection {
    static Connection connection;
    
    public static Connection connect() {
        try {
            // Faz a comunicação com o banco de dados sqlite
            String urlBD = "jdbc:sqlite:../database/database_com_dados-contrib-Daniel-Farina.db";
            connection = DriverManager.getConnection(urlBD);
            System.out.println("Database connected successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}