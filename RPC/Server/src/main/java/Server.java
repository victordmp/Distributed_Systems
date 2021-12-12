/*
    Descrição:
        Essa é classe responsavel startar o servidor e 
        configurar os serços a ele. Basicamente essa é 
        a classe mãe, pois apartir daqui as demais classes
        serão chamadas.
    
    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 5/12/2021
    Data de atualização: 5/12/2021
*/

import io.grpc.ServerBuilder;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
            io.grpc.Server server = ServerBuilder
                    .forPort(7777)
                    .addService(new ConnectionMiddleware())
                    .build();      
        try {
            server.start();
            System.out.println("Server started.");
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e);
        }
        
    }
}
