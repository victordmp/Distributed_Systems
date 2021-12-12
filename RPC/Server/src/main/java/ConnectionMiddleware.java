/*
    Descrição:
        Essa é classe responsavel por gerenciar as requisições. É recebe a 
        requisição do cliente, decodifica o tipo de requesição e chama a 
        função do controller referente ao tipo de requisição solicitada. Essa 
        classe também é responsavel por startar a conexão com o banco de dados.
        Basicamente ela é função principal, por onde a comunicação entre o cliente
        e o servidor ocorrem.
    
    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 5/12/2021
    Data de atualização: 5/12/2021
*/

import java.io.*;
import java.sql.*;
import java.net.*;
import io.grpc.stub.StreamObserver;

public class ConnectionMiddleware extends ConnectionMiddlewareGrpc.ConnectionMiddlewareImplBase {
    static Connection connection;

    @Override
    public void comunication(Request request, StreamObserver<Reply> responseObserver) {
        // Cria conexão com o banco de dados SQL
        connection = DatabaseConnection.connect(); 

        // Recebe e printa o codigo da opção enviada pelo cliente.
        System.out.println("Recived: " + request.getOption());

        // Cria uma resposta
        Reply.Builder response = Reply.newBuilder();

        // Switch responsavel por escolher a resposta de acordo com a opção 
        // enviada pelo client. Dependendo da opção requerida ele chama e 
        // envia os dados recebidos para uma dos metodos implementados no 
        // Controller.java que ira retornar de volta a resposta já formatada 
        // para ser enviada para o cliente.
        switch (request.getOption()) {
            case ADDMATRICULA:
                Controller.add_matricula(request, response, connection);
                break;
            case PUTNOTA:
                Controller.updateNota(request, response, connection);                
                break;
            case PUTFALTAS:
                Controller.updateFaltas(request, response, connection);          
                break;  
            case GETALUNOS:
                Controller.getAlunos(request, response, connection);        
                break;
            case GETBOLETIM:
                Controller.getBoletim(request, response, connection);           
                break;              
            default:
                response.setMessage("Error: invalid command!");
                break;
        }
        
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
