/*
    Descrição:
        É um servidor que recebe requisições do cliente via TCP 
        usando o protobuffer para serealização e descerealização. 
        O banco de dados usado e sqlite. Essa implemntação e rerente 
        atividade de representação extrema de dados. Todas as especificações 
        estão contidas no pdf atividade_red_v3.pdf.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 19/11/2021
    Data de atualização: 26/11/2021
*/

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    public static void main(String args[]) {
        try {
            int serverPort = 7000; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                ClientThread c = new ClientThread(clientSocket);

                /* inicializa a thread */
                c.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

class ClientThread extends Thread {
    private static Connection connection;
    private static Statement statement;
    private static int serverPort = 7000;
    private static ServerSocket listenSocket;
    private static Socket clientSocket;
    private static DataInputStream inClient;
    private static DataOutputStream outClient;

    public ClientThread(Socket clientSocket) {
        try {
            connect();
            this.clientSocket = clientSocket;
            // De acordo com o cliente cria um canal de comunicação 
            // para input e output entre o servidor e o cliente.
            inClient = new DataInputStream(clientSocket.getInputStream());
            outClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    private static void connect() {
        try {
            // Faz a comunicação com o banco de dados sqlite
            String urlBD = "jdbc:sqlite:../../data/database_com_dados-contrib-Daniel-Farina.db";
            connection = DriverManager.getConnection(urlBD);
            System.out.println("Banco de Dados Conectado!!!!");

            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Função que faz a logica de decodificar e enviar as respostas de volta para o cliente.
    private static void output(int type) {
        // Criar uma menssagem
        String message = "Requisição realizada com sucesso!";
        // Cria uma mesagem do tipo MsgReply que esta definida no protobuffer
        NoteManagement.MsgReply.Builder msgReply = NoteManagement.MsgReply.newBuilder();

        try {
            switch (type) {
                case 1:
                    try {
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        // Recebe a messagem do cliente
                        inClient.read(buffer);

                        // Cria uma intancia do tipo matricula que esta definida no protobuffer.
                        // Ela vai receber o parse de matricula recebida pelo cliente.
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        // Faz uma consulta na tabela de Aluno verificando se o RA existe.
                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero o aluno existe.
                        // Se for igual a zero o aluno não existe então retorna um erro.
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();
                        
                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero existe a discplina .
                        // Se for igual a zero a displina não existe então retorna um erro.
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

                        // Faz um select all na tabela de matricula
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();

                        // Printa matricula por matricula
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
                        // Recebe a messagem do cliente
                        inClient.read(buffer);

                        // Cria uma intancia do tipo matricula que esta definida no protobuffer.
                        // Ela vai receber o parse de matricula recebida pelo cliente.
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        // Faz uma consulta na tabela de Aluno verificando se o RA existe.
                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero o aluno existe.
                        // Se for igual a zero o aluno não existe então retorna um erro.
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero existe a discplina .
                        // Se for igual a zero a displina não existe então retorna um erro.
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
                        
                        // Faz um select all na tabela de matricula
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();

                        // Printa matricula por matricula
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
                        // Recebe a messagem do cliente
                        inClient.read(buffer);

                        // Cria uma intancia do tipo matricula que esta definida no protobuffer.
                        // Ela vai receber o parse de matricula recebida pelo cliente.
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
                        PreparedStatement stmt = connection.prepareStatement("select * from Aluno where RA = " 
                                                                                    + Integer.toString(matricula.getRA()));
                        ResultSet resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero o aluno existe.
                        // Se for igual a zero o aluno não existe então retorna um erro.
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "RA não encontrado. Requisição falhou!";
                            break;
                        }

                        // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
                        stmt = connection.prepareStatement("select * from Disciplina where codigo = '" 
                                                                + matricula.getCodDisciplina() + "'");
                        resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero existe a discplina .
                        // Se for igual a zero a displina não existe então retorna um erro.
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

                        // Faz um select all na tabela de matricula
                        stmt = connection.prepareStatement("select * from Matricula");
                        resultSet = stmt.executeQuery();
                        
                        // Printa matricula por matricula
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
                        // Recebe a messagem do cliente
                        inClient.read(buffer);

                        // Cria uma intancia do tipo matricula que esta definida no protobuffer.
                        // Ela vai receber o parse de matricula recebida pelo cliente.
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
                        // com o código da disciplina, ano e semestre passados pelo cliente.
                        PreparedStatement stmt = connection.prepareStatement("select * from Matricula where cod_disciplina = '" 
                                                                                    + matricula.getCodDisciplina() 
                                                                                    + "' and ano = " + matricula.getAno()
                                                                                    + " and semestre = " + matricula.getSemestre());
                        ResultSet resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero existe alguma Matricula com esses dados.
                        // Se for igual a zero a displina não existe nenhuma Matricula com esses dados então retorna um erro.                       
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Nenhuma matricula com esses dados encontrada! Nenhum aluno encontrado!";
                            break;
                        }

                        // Cria uma mesagem do tipo MsgReply que esta definida no protobuffer
                        NoteManagement.MsgReply msgReply2 = NoteManagement.MsgReply.newBuilder().setMessage("OK").build();
                        // Envia uma mesagem dizendo que não ocorreu nenhum erro
                        outClient.write(msgReply2.toByteArray());

                        // Faz uma consulta para pegar os alunos (RA, nome, período) de 
                        // uma disciplina informado a disciplina, ano e semestre.
                        stmt = connection.prepareStatement("select RA, nome, periodo from Aluno where RA in (select RA from Matricula where cod_disciplina = '" + matricula.getCodDisciplina() 
                                                                                                    + "' and ano = " + matricula.getAno()
                                                                                                    + " and semestre = " + matricula.getSemestre()
                                                                                                    + ")");
                        resultSet = stmt.executeQuery();
                        
                        // Cria uma mesagem do tipo NumChair que esta definida no protobuffer.
                        // Esse mansagem será responsavel por passar quantas linhas foram geradas 
                        // da minha consulta.
                        NoteManagement.NumChair.Builder numChair = NoteManagement.NumChair.newBuilder();
                        qtd = 0;
                        while (resultSet.next()) qtd++;
                        outClient.write((numChair.setNumChair(qtd).build()).toByteArray());

                        // Creia uma mesagem do tipo aluno, seta os dados do aluno e envia para o cliente.
                        // Isso é feito para cada linha gerada da consulta.
                        resultSet = stmt.executeQuery();
                        while (resultSet.next()) {
                            NoteManagement.Aluno aluno = NoteManagement.Aluno.newBuilder()
                                                                            .setRA(resultSet.getInt("RA"))
                                                                            .setNome(resultSet.getString("nome"))
                                                                            .setPeriodo(resultSet.getInt("periodo"))
                                                                            .build();
                            System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                + " - " + resultSet.getString("nome") 
                                                + " - " + Integer.toString(resultSet.getInt("periodo")));
                            // Por algum motivo alguns dados não estavam chegando no servidor 
                            // ou estavam chegando em ordens errados. Por esse motivo tive que 
                            // usar uma gambiarra, no caso um sleep de 200 milisegundos.
                            Thread.currentThread().sleep(200);
                            // Envia a messagem contendo o aluno
                            outClient.write(aluno.toByteArray());
                            // outClient.flush();
                        }
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try{
                        String valueStr = inClient.readLine();
                        int sizeBuffer = Integer.valueOf(valueStr);
                        byte[] buffer = new byte[sizeBuffer];
                        // Recebe a messagem do cliente
                        inClient.read(buffer);

                        // Cria uma intancia do tipo matricula que esta definida no protobuffer.
                        // Ela vai receber o parse de matricula recebida pelo cliente.
                        NoteManagement.Matricula matricula = NoteManagement.Matricula.parseFrom(buffer);

                        // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
                        // com o código da disciplina, ano e semestre passados pelo cliente.
                        PreparedStatement stmt = connection.prepareStatement("select * from Matricula where RA = '" 
                                                                                    + matricula.getRA() 
                                                                                    + "' and ano = " + matricula.getAno()
                                                                                    + " and semestre = " + matricula.getSemestre());
                        ResultSet resultSet = stmt.executeQuery();

                        // Conta quantas linhas foram retonadas da consulta.
                        // Se for maior que zero existe alguma Matricula com esses dados.
                        // Se for igual a zero a displina não existe nenhuma Matricula com esses dados então retorna um erro.              
                        int qtd = 0;
                        while (resultSet.next()) qtd++;
                        if(qtd == 0) {
                            message = "Nenhuma matricula com esses dados encontrada!";
                            break;
                        }
                        
                        // Cria uma mesagem do tipo MsgReply que esta definida no protobuffer
                        NoteManagement.MsgReply msgReply2 = NoteManagement.MsgReply.newBuilder().setMessage("OK").build();
                        // Envia uma mesagem dizendo que não ocorreu nenhum erro
                        outClient.write(msgReply2.toByteArray());
                        
                        // Faz uma consulta para pegar os alunos (RA, nome, período) de 
                        // uma disciplina informado a disciplina, ano e semestre.
                        stmt = connection.prepareStatement("select *, D.nome As nome_dis from Matricula M, Aluno A, Disciplina D where M.RA = " + matricula.getRA() 
                                                                    + " and M.ano = " + matricula.getAno()
                                                                    + " and M.semestre = " + matricula.getSemestre()
                                                                    + " and A.RA = M.RA and D.codigo = M.cod_disciplina");
                        resultSet = stmt.executeQuery();
                        
                        // Cria uma mesagem do tipo NumChair que esta definida no protobuffer.
                        // Esse mansagem será responsavel por passar quantas linhas foram geradas 
                        // da minha consulta.
                        NoteManagement.NumChair.Builder numChair = NoteManagement.NumChair.newBuilder();
                        qtd = 0;
                        while (resultSet.next()){
                            if (qtd == 0) {
                                NoteManagement.Aluno aluno = NoteManagement.Aluno.newBuilder()
                                                                                .setRA(resultSet.getInt("RA"))
                                                                                .setNome(resultSet.getString("nome"))
                                                                                .build();
                                System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                                    + " - " + resultSet.getString("nome"));
                                Thread.currentThread().sleep(200);
                                outClient.write(aluno.toByteArray());
                            }
                            qtd++;
                        } 
                        outClient.write((numChair.setNumChair(qtd).build()).toByteArray());

                        // Cria uma mesagem do tipo matricula e do tipo disciplina, 
                        // seta os dados e envia para o cliente. Isso é feito para 
                        // cada linha gerada da consulta.
                        resultSet = stmt.executeQuery();
                        while (resultSet.next()) {
                            NoteManagement.Matricula m1 = NoteManagement.Matricula.newBuilder()
                                                                            .setNota(resultSet.getFloat("nota"))
                                                                            .setFaltas(resultSet.getInt("faltas"))
                                                                            .build();
                            NoteManagement.Disciplina d1 = NoteManagement.Disciplina.newBuilder()
                                                                            .setNome(resultSet.getString("nome_dis"))
                                                                            .build();
                            System.out.println("" + resultSet.getString("nome_dis")
                                                + " - " + resultSet.getFloat("nota")
                                                + " - " + Integer.toString(resultSet.getInt("faltas")));
                            // Envia a messagem contendo o matricula
                            outClient.write(m1.toByteArray());

                            // Por algum motivo alguns dados não estavam chegando no servidor 
                            // ou estavam chegando em ordens errados. Por esse motivo tive que 
                            // usar uma gambiarra, no caso um sleep de 200 milisegundos.
                            Thread.currentThread().sleep(200);
                            // Envia a messagem contendo o disciplina
                            outClient.write(d1.toByteArray());

                            // Por algum motivo alguns dados não estavam chegando no servidor 
                            // ou estavam chegando em ordens errados. Por esse motivo tive que 
                            // usar uma gambiarra, no caso um sleep de 200 milisegundos.
                            Thread.currentThread().sleep(200);
                        }
                    } catch (SQLException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        message = "Requisição falhou! " + e.getMessage();
                        System.out.println(e.getMessage());
                    } catch (InterruptedException ex) {
                        message = "Requisição falhou! " + ex.getMessage();
                        System.out.println(ex.getMessage());
                    }
                    break;
                default:
                    System.out.println("Ocorreu algom erro!");
                    break;
            }
            outClient.write((msgReply.setMessage(message).build()).toByteArray());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            while(true) {
                String valueStr = inClient.readLine();
                int sizeBuffer = Integer.valueOf(valueStr);
                byte[] buffer = new byte[sizeBuffer];
                // Recebe uma mesagem do cliente contendo o tipo.
                inClient.read(buffer);

                // Decodifica a mesagem de topo recebida do cliente.
                NoteManagement.TypeMessage type = NoteManagement.TypeMessage.parseFrom(buffer);
                // Envia o tipo para a função responsavel por decodificar a 
                // requisição e responder para o cliente.
                output(type.getType());
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                inClient.close();
                outClient.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    }
}