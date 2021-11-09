/*
    Descrição:
        Esse codigo é referente a parte do cliente da atividade de sockets TCP - Questão 1.
        Neste arquivo esta implementado toda a logica de comunicação da parte do cliente
        como por exemplo uma função que encripta as senha em HASH - SHA-512 e etc. A ideia
        é criar um protocolo na parte do servidor. E implemnetar um cliente que use esses 
        protoco para se comunicar com o servidor. Neste caso a comunicação e feita trocando 
        Strings em UTF-8. O enuncioando da questão esta no arquivo 
        './TCP/Atividade 01 - TCP - v2.pdf'.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica costa

    Data de criação: 17/10/2021
    Data de atualização: 25/10/2021
*/
package tcp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.nio.charset.*;

// Função que gera um Secure Password com HASH - SHA-512
fun getSecurePassword(password: String): String {
    var generatePassword: String;

    var md: MessageDigest = MessageDigest.getInstance("SHA-512");
    var digest: ByteArray = md.digest(password.toByteArray(StandardCharsets.UTF_8));
    var sb: StringBuilder = StringBuilder();
    for (i in digest) {
        sb.append(Integer.toString((i.toInt() and 0xff) + 0x100, 16).substring(1));
    }

    generatePassword = sb.toString();

    return generatePassword;
}

// Recebe o comando e retorna somente o primeior argumento, que é método escolhido.
fun splitComandMain(command: String): String {
    var commandMain: Array<String> = command.split(" ").toTypedArray();

    return commandMain[0];
}

// Recebe o comando e retorna comando fomatado, subtituindo inclusive a senha normal
// por uma encriptado em HASH - SHA-512
fun splitComand(command: String): String {
    var commandMain: Array<String> = command.split(" ").toTypedArray();

    var user = commandMain[1].split(",").toTypedArray();

    user[1] = getSecurePassword(user[1]);

    return commandMain[0] + " " + user.joinToString(",");
}

fun main() {
    var reader: Scanner = Scanner(System.`in`);
    var clientSocket: Socket? = null;

    try {
        // configurações do cliente
        var serverPort: Int = 6666;
        var serverAddr: InetAddress = InetAddress.getByName("127.0.0.1");

        // Cria a socket de comunicação com o servidor
        clientSocket = Socket(serverAddr, serverPort);

        // Input e Output
        var input: DataInputStream = DataInputStream(clientSocket.getInputStream());
        var output: DataOutputStream = DataOutputStream(clientSocket.getOutputStream());

        // Variavel que guarda o comando e a resposta do servidor
        var buffer: String;

        while (true) {
            print("Message: ");
            // Recebe o comando digitado pelo cliente
            buffer = reader.nextLine();

            // Se o comando for connect ele chama um função que ira formatar a corretamente a string
            // deixando no formato correto para ser enviado para o servidor. Essa função além de 
            // formatar ela também é responsavel por encriptar a senha antes do envio
            if (splitComandMain(buffer).equals("CONNECT")) buffer = splitComand(buffer);

            // Envia o comando presente no buffer para o servidor
            output.writeUTF(buffer);

            // Se o comando for EXIT a conexão e o progrma são encerrados
            if (buffer.equals("EXIT")) break;

            // Recebe a resposta do servidor
            buffer = input.readUTF();
            // Printa a resposta obtida do servidor
            println("Server said: " + buffer);
        }
    } catch (ue: UnknownHostException) {
        println("Socket: " + ue.message);
    } catch (eofe: EOFException) {
        println("EOF: " + eofe.message);
    } catch (ioe: EOFException) {
        println("IO: " + ioe.message);
    } finally {
        try {
            clientSocket!!.close();
        }  catch (ioe: IOException) {
            println("IO: " + ioe);
        }
    }
}