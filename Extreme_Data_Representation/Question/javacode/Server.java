import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private static Connection connection;
    private static Statement statement;
    private static int serverPort = 7000;
    private static ServerSocket listenSocket;
    private static Socket clientSocket;
    private static DataInputStream inClient;
    private static DataOutputStream outClient;

    private static void connect() {
        try {
            String urlBD = "jdbc:sqlite:/Users/victor/Documents/SD/Sockets/Distributed_Systems/Extreme_Data_Representation/data/database_com_dados-contrib-Daniel-Farina.db";
            connection = DriverManager.getConnection(urlBD);
            System.out.println("Banco de Dados Conectado!!!!");

            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Faz a query faz a listagem de disciplinas, faltas e notas (RA, nome, nota, faltas) 
    // de um aluno informado o ano e semestre.
    private static void output(int type) {
        String message = "Requisição realizada com sucesso!";
        NoteManagement.MsgReply.Builder msgReply = NoteManagement.MsgReply.newBuilder();

        try {
            switch (type) {
                case 1:
                    try {
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        inClient.read(buffer);
                        
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();
                        qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Disciplina não encontrado. Requisição falhou!";
                            break;
                        }

                        // Inserindo um regitro de Matricula
                        statement.execute("INSERT INTO Matricula(RA, cod_disciplina, ano, semestre, nota, faltas) VALUES ("
                                                + Integer.toString(matricula.getRA()) 
                                                + ", '" + matricula.getCodDisciplina() 
                                                + "', " + Integer.toString(matricula.getAno()) 
                                                + ", " + Integer.toString(matricula.getSemestre()) 
                                                + ", " + 0.0 
                                                + ", " + Integer.toString(0) + ")");
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();
                        
                        while (resultSet.next()) {
                            System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                + " - " + resultSet.getString("cod_disciplina") 
                                                + " - " + Integer.toString(resultSet.getInt("ano")) 
                                                + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                                + " - " + resultSet.getFloat("nota") 
                                                + " - " + Integer.toString(resultSet.getInt("faltas")));
                        }
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        inClient.read(buffer);
                        
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();
                        qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Disciplina não encontrado. Requisição falhou!";
                            break;
                        }

                        // Atualiza a nota de Matricula
                        statement.execute("update Matricula set nota = " + matricula.getNota() 
                                            + " where RA = " + Integer.toString(matricula.getRA()) 
                                            + " and cod_disciplina = '" + matricula.getCodDisciplina() + "'");
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();
                        
                        while (resultSet.next()) {
                            System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                + " - " + resultSet.getString("cod_disciplina") 
                                                + " - " + Integer.toString(resultSet.getInt("ano")) 
                                                + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                                + " - " + resultSet.getFloat("nota") 
                                                + " - " + Integer.toString(resultSet.getInt("faltas")));
                        }
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        inClient.read(buffer);
                        
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();
                        qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Disciplina não encontrado. Requisição falhou!";
                            break;
                        }

                        // Atualiza a faltas de Matricula
                        statement.execute("update Matricula set faltas = " + matricula.getFaltas() 
                                            + " where RA = " + Integer.toString(matricula.getRA()) 
                                            + " and cod_disciplina = '" + matricula.getCodDisciplina() + "'");
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();
                        
                        while (resultSet.next()) {
                            System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                + " - " + resultSet.getString("cod_disciplina") 
                                                + " - " + Integer.toString(resultSet.getInt("ano")) 
                                                + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                                + " - " + resultSet.getFloat("nota") 
                                                + " - " + Integer.toString(resultSet.getInt("faltas")));
                        }
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        inClient.read(buffer);
                        
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        PreparedStatement stmt = connection.prepareStatement("select * from Matricula where cod_disciplina = '" 
                                                                                    + matricula.getCodDisciplina() 
                                                                                    + "' and ano = " + matricula.getAno()
                                                                                    + " and semestre = " + matricula.getSemestre());
                        ResultSet resultSet = stmt.executeQuery();
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Nenhuma matricula com esses dados encontrada! Nenhum aluno encontrado!";
                            break;
                        }

                        stmt = connection.prepareStatement("select RA, nome, periodo from Aluno where RA in (select RA from Matricula where cod_disciplina = '" + matricula.getCodDisciplina() 
                                                                                                    + "' and ano = " + matricula.getAno()
                                                                                                    + " and semestre = " + matricula.getSemestre()
                                                                                                    + ")");
                        resultSet = stmt.executeQuery();
                        
                        // NoteManagement.NumChair.Builder numChair = NoteManagement.NumChair.newBuilder();
                        // qtd = 0;
                        // while (resultSet.next()) qtd++;
                        // System.out.println(qtd);
                        // // outClient.write((numChair.setNumChair(qtd).build()).toByteArray());
                        // outClient.write((numChair.setNumChair(qtd).build()).toByteArray());

                        // resultSet = stmt.executeQuery();
                        while (resultSet.next()) {
                            NoteManagement.Aluno aluno = NoteManagement.Aluno.newBuilder()
                                                                            .setRA(resultSet.getInt("RA"))
                                                                            .setNome(resultSet.getString("nome"))
                                                                            .setPeriodo(resultSet.getInt("periodo"))
                                                                            .build();
                            System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                + " - " + resultSet.getString("nome") 
                                                + " - " + Integer.toString(resultSet.getInt("periodo")));
                            // Thread.currentThread().sleep(100);
                            outClient.write(aluno.toByteArray());
                        }
                        System.out.println("Agora vai!");
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    // catch (InterruptedException ex) {
                    //     message = "Requisição falhou! " + ex.getMessage();
                    //     System.out.println(ex.getMessage());
                    // }
                    break;
                // case TypesMsg.LIST_DISCIPLINA:
                
                //     break;
                default:
                    System.out.println("DEU RUIM!!");
                    break;
            }
            outClient.write((msgReply.setMessage(message).build()).toByteArray());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            serverPort = 7000;
            listenSocket = new ServerSocket(serverPort);
            connect();

            while(true) {
                clientSocket = listenSocket.accept();
                inClient = new DataInputStream(clientSocket.getInputStream());
                outClient = new DataOutputStream(clientSocket.getOutputStream());

                String valueStr = inClient.readLine();
                int sizeBuffer = Integer.valueOf(valueStr);
                byte[] buffer = new byte[sizeBuffer];
                inClient.read(buffer);

                NoteManagement.TypeMessage type = NoteManagement.TypeMessage.parseFrom(buffer);
                
                output(type.getType());
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}