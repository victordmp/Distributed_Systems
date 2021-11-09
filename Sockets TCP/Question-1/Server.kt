/*
    Descrição:
        Esse codigo é referente a parte do servidor da atividade de sockets TCP - Questão 1.
        Neste arquivo esta implementado toda a logica de comunicação da parte do servidor. 
        A ideia é criar um protocolo na parte do servidor. E implemnetar um cliente que use 
        esses protoco para se comunicar com o servidor. Neste caso a comunicação e feita 
        trocando Strings em UTF-8. O enucioando da questão esta no arquivo 
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
import java.lang.*;
import java.util.*;
import java.security.*;
import java.nio.charset.*;

// Estabelece uma conexão com um cliente
// e crira um thread que mantera essa comunição com o cliente
// assim podendo estabelecer mais de um conexão e com clientes diferentes
fun main() {
    try {
        var serverPort: Int = 6666;

        var listenSocket: ServerSocket = ServerSocket(serverPort);

        while (true) {
            println("Server wating for connection...");

            var clientSocket: Socket = listenSocket.accept();

            println("Client connected... Creating thread...");

            var c: ClientThread = ClientThread(clientSocket);

            c.start();
        }
    } catch (e: IOException) {
        println("Listen socket:" + e.message);
    }
}

class ClientThread : Thread {
    lateinit var input: DataInputStream;
    lateinit var output: DataOutputStream;
    lateinit var clientSocket: Socket;

    // Construtor
    constructor(clientSocket: Socket) {
        try {
            this.clientSocket = clientSocket;
            this.input = DataInputStream(clientSocket.inputStream);
            this.output = DataOutputStream(clientSocket.outputStream);
        } catch (ioe: IOException) {
            println("Connection:" + ioe.message);
        }
    }

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

    // Função que verifica se o usuário é valido ou não
    fun userIsValid(logIn: LogIn): Boolean {
        for (item in users) {
            // Verifica se o usuário exite no array de users
            if (logIn.user.equals(item.user)) {
                /* Check if password exist in array users */
                // Verifica se a senha exit no array de user, para checar isso eu faço uma 
                // coparação entre as duas senhas.
                // OBS: As duas senhas são encriptadas com HASH - SHA-512 para funcionar.
                if (logIn.password.equals(getSecurePassword(item.password))) {
                    return true;
                }
            }
        }

        return false;
    } 

    // Recebe o comando e retorna somente o primeior argumento, que é método escolhido.
    fun splitCommandMain(command: String): String {
        var commandMain: Array<String> = command.split(" ").toTypedArray();
        return commandMain[0];
    }
    
    // Recebe o comando e retorna um LogIn contendo o usario e senha.
    fun splitComand(command: String): LogIn? {
        var commandMain: Array<String> = command.split(" ").toTypedArray();
    
        var user = commandMain[1].split(",").toTypedArray();
    
        var logIn = LogIn(user[0], user[1])
    
        return logIn;
    }

    // Recebe um comando shell, execulta e retorna o resultado
    fun commandShell(cmd: String): String {
        var result: String = "";

        val processBuilder = ProcessBuilder();
        processBuilder.command("bash", "-c", cmd);
    
        try {
            val process: Process = processBuilder.start();
            val reader = BufferedReader(InputStreamReader(process.inputStream));
            val line = reader.readText();

            process.waitFor();
            
            result = line;
        } catch (e: Exception) {
            println(e.message);
        }
    
        return result;
    }

    // Função que recebe um comando e de acrodo com o comando executa uma determinada ação.
    fun options(command: String): String {
        lateinit var value: String;

        when (splitCommandMain(command)) {
            "CONNECT" -> {
                // Valida se o usuário existe ou não
                val logIn: LogIn? = splitComand(command);
                value = if(userIsValid(logIn!!)) "SUCCESS" else "ERROR";
            }
            "PWD" -> {
                // Retorna o resposta do comando pwd no shell
                value = "PWD => " + commandShell("pwd");
            }
            "CHDIR" -> {
                // Acessa um determinada pasta e retorna se deu certo ou não
                var cmd: Array<String> = command.split(" ").toTypedArray();
                if (cmd.size == 2) {
                    if (commandShell("cd " + cmd[1]).length > 0) {
                        value = "ERROR";
                    } else {
                        value = "SUCCESS";
                    }
                } else {
                    value = "ERROR";
                }
            }
            "GETFILES" -> {
                // Retorna o número de arquivos e o o nome de cada arquivo
                value = "GETFILES => NUM FILES => " + 
                    commandShell("ls -Rl | grep " + "^-" + " -c") +
                    "FILES => " + commandShell("ls -Rl | grep " + "^-"); 
            }
            "GETDIRS" -> {
                // Retorna o número de diretórios e o nome de cada diretório
                value = "GETDIRS => NUM DIRS => " + 
                    commandShell("ls -Rl | grep " + "^d" +" -c") +
                    "DIRS => " + commandShell("ls -Rl | grep " + "^d");
            }
            "EXIT" -> {
                value = "EXIT"
            }
            else -> {
                value = "COMAND NOT EXISTS"
            }
        }

        return value;
    }

    override
    fun run() {
        try {
            var buffer: String;
            while(true) {
                buffer = input.readUTF();
                
                println("Customer said: " + buffer);

                buffer = options(buffer)
                if (buffer.equals("EXIT")) break;

                buffer += ":OK";
                output.writeUTF(buffer);
            } 
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        } finally {
            try {
                input.close();
                output.close();
                clientSocket.close(); 
            } catch (ioe: IOException) {
                println("IOE: " + ioe);
            }
        }
        println("Client communication thread closed.")
    }
}

// Array de usuarios registrados
var users: Array<LogIn> = arrayOf<LogIn>(
    LogIn("victordmp", "12459"),
    LogIn("mkdir", "jocarioca"),
    LogIn("neide", "ffj123"),
    LogIn("luffy", "onepiece"),
    LogIn("goku", "dragon"),
);

// Modelo LogIn com usruário e senha
class LogIn {
    val user: String;
    val password: String;

    constructor(user: String, password: String) {
        this.user = user;
        this.password = password;
    }
}