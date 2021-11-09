/*
    Descrição:
        Esse codigo é referente a parte do servidor da atividade de sockets TCP - Questão 2.
        Neste arquivo esta implementado toda a logica de comunicação da parte do servidor. 
        A ideia é criar um protocolo na parte do servidor. E implementar um cliente que use esse 
        protoco para se comunicar com o servidor. Neste caso a comunicação e feita trocando 
        bytes. E tanto a requisição quanto a resposta respeitam um formato que está especificado
        no Arquivo './TCP/Atividade 01 - TCP - v2.pdf'.

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
import java.nio.file.Files;
import java.nio.*;

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

            var c: ClientThread2 = ClientThread2(clientSocket);

            c.start();
        }
    } catch (e: IOException) {
        println("Listen socket:" + e.message);
    }
}

class ClientThread2 : Thread {
    lateinit var input: DataInputStream;
    lateinit var output: DataOutputStream;
    lateinit var clientSocket: Socket;
    lateinit var clientAddress: SocketAddress;
    lateinit var file: File;
    lateinit var dir: File;
    lateinit var files: Array<File>;
    lateinit var fileWriter: FileWriter;
    lateinit var log: BufferedWriter;
    lateinit var bis: BufferedInputStream;
    lateinit var mybytearray: ByteArray;
    var number: Int? = null;
    var commandCode: Int? = null;
    var statusCode: Int? = null;
    var numFiles: Int? = null;
    var listFiles: Array<String>? = null;

    /* Constructor */
    // inicialização das variaveis como input, output, socket do cleinte e etc.
    constructor(clientSocket: Socket) {
        try {
            this.clientSocket = clientSocket;
            this.clientAddress = clientSocket.remoteSocketAddress;
            this.input = DataInputStream(clientSocket.inputStream);
            this.output = DataOutputStream(clientSocket.outputStream);
            this.file = File("logs.txt");
            this.dir = File("./Server-Files");
            this.fileWriter = FileWriter(this.file, true);
            this.log = BufferedWriter(this.fileWriter);
        } catch (ioe: IOException) {
            println("Connection:" + ioe.message);
        }
    }

    // Função que recebe o código do resultado e retorna um string 
    // que se refere ao resultado da ação 
    fun result(number: Int): String {
        when (number) {
            2 -> {
                return "SUCCESS";
            }
            else -> {
                return "ERROR";
            }
        }
    }

    // Função que recee o código da opção enviada pelo cliente 
    // e retorna uma string que se refere ao método invocado pelo cliente
    fun option(number: Int): String {
        when (number) {
            1 -> {
                return "ADDFILE";
            }
            2 -> {
                return "DELETE";
            }
            3 -> {
                return "GETFILESLIST";
            }
            4 -> {
                return "GETFILE";
            }
            else -> {
                return "ERROR";
            }
        }
    }

    // Função responsavel por receber e decodificar os dados recebido do cliente
    fun dataInputs(command: String) { 
        when (command) {
            "ADDFILE" -> {
                // Recebe os cada o o tamanho do arquivo e usa essa infomação para 
                // receber byte a byte converter em char e depois juntar em uma string
                // formando assim o filename do arquivo enviado pelo cliente
                var filenameChar: Char;
                var filename: String = "";
                var size: Int = input.readByte().toInt();
                for (i in 0..size-1) {
                    filenameChar = input.readByte().toChar();
                    filename += filenameChar;
                }

                // Execulta o comando que cria o arquivo de acordo 
                // com o nome enviado pelo cliente
                commandShell("touch ./Server-Files/" + filename);

                log.append("Customer said => [" + clientAddress + "]: " + command + " " + filename);
                log.newLine();
            }
            "DELETE" -> {
                // Recebe os cada o o tamanho do arquivo e usa essa informação para 
                // receber byte a byte converter em char e depois juntar em uma string
                // formando assim o filename do arquivo enviado pelo cliente
                var filenameChar: Char;
                var filename: String = "";
                var size: Int = input.readByte().toInt();
                for (i in 0..size-1) {
                    filenameChar = input.readByte().toChar();
                    filename += filenameChar;
                }

                // Execulta o comando que exclui o arquivo de acordo 
                // com o nome enviado pelo cliente
                commandShell("rm ./Server-Files/"+filename);

                // Salva no log
                log.append("Customer said => [" + clientAddress + "]: " + " " + command + " " + filename);
                log.newLine();
            }
            "GETFILESLIST" -> {
                // Recebe os cada o o tamanho do arquivo e usa essa infomação para 
                // receber byte a byte converter em char e depois juntar em uma string
                // formando assim o filename do arquivo enviado pelo cliente
                var filenameChar: Char;
                var filename: String = "";
                var size: Int = input.readByte().toInt();
                for (i in 0..size-1) {
                    filenameChar = input.readByte().toChar();
                    filename += filenameChar;
                }

                // Faz a logica de pegar a lista de arquivos presentes 
                // no diretório "./Server-Files"
                try {
                    this.files =  this.dir.listFiles();
                    this.numFiles = this.files.size;
                    this.statusCode = 2;
                } catch (ioe: IOException) {
                    this.statusCode = 1;
                    println("IO: " + ioe);
                }
                
                // Salva no log
                log.append("Customer said => [" + clientAddress + "]: " + " " + command);
                log.newLine();
            }
            "GETFILE" -> {
                // Recebe os cada o o tamanho do arquivo e usa essa infomação para 
                // receber byte a byte converter em char e depois juntar em uma string
                // formando assim o filename do arquivo enviado pelo cliente
                var filenameChar: Char;
                var filename: String = "";
                var size: Int = input.readByte().toInt();
                for (i in 0..size-1) {
                    filenameChar = input.readByte().toChar();
                    filename += filenameChar;
                }

                // Recbe o nome do arquivo pelo cliente e então cria um intancia de File
                // pega o arquivo quie o cliente solicitou e tranforma em um array de bytes.
                try {
                    var myfile: File = File("./Server-Files/" + filename);
                    mybytearray = Files.readAllBytes(myfile.toPath());
                    var fis: FileInputStream = FileInputStream(myfile);
                    this.bis = BufferedInputStream(fis);
                    this.number = myfile.length().toInt();
                    statusCode = 2;
                } catch (ioe: IOException) {
                    statusCode = 1;
                    println("IO: " + ioe);
                }

                // Salva no log
                log.append("Customer said => [" + clientAddress + "]: " + " " + command);
                log.newLine();
            }
        }
    }

    // Função responsavel por montar os estrutura que será enviada 
    // para o cliente de acordo com o método solicitado pelo cliente
    fun dataOutputs(command: String) {
        when (command) {
            "ADDFILE", "DELETE" -> {
                // Monta a estrutura de saída que será enviada para o cliente e envia byte a byte
                for (i in 0..2) {
                    if (i == 0) {
                        output.writeByte(2.toInt());
                    } else if (i == 1) {
                        output.writeByte(commandCode!!.toInt());
                    } else {
                        output.writeByte(statusCode!!.toInt());
                    }
                }

                // Salva no log
                log.append("Server response: " + result(statusCode!!));
                log.newLine();
            }
            "GETFILESLIST" -> {
                // Monta a estrutura de saída que será enviada para o cliente e envia byte a byte
                for (i in 0..3) {
                    if (i == 0) {
                        output.writeByte(2.toInt());
                    } else if (i == 1) {
                        output.writeByte(commandCode!!.toInt());
                    } else if (i == 2) {
                        output.writeByte(statusCode!!.toInt());
                    } else {
                        output.writeByte(numFiles!!.toInt());
                    }
                }
                
                // Percorre o array de files e envai primeiro o tamanho do filename 
                // e depois o proprio filename para o cliente, ele faz isso para cada file
                // e os dados são tranmitidos byte a byte
                var filenameByte: ByteArray;
                for (i in 0..numFiles!!-1) {
                    filenameByte = files[i].name.toByteArray();
                    output.writeByte(filenameByte.size.toInt());
                    for (j in 0..filenameByte.size-1) {
                        output.writeByte(filenameByte[j].toInt());
                    }
                }

                // Salva no log
                log.append("Server response: " + result(statusCode!!));
                log.newLine();
            }
            "GETFILE" -> {
                // Monta a estrutura de saída que será enviada para o cliente e envia byte a byte
                for (i in 0..2) {
                    if (i == 0) {
                        output.writeByte(2.toInt());
                    } else if (i == 1) {
                        output.writeByte(commandCode!!.toInt());
                    } else {
                        output.writeByte(statusCode!!.toInt());
                    }
                }

                // Separa o tamanho do arquivo em 4 bytes que são envias antes do arquivo
                // pois é nessesario o tamanho arquivo para usar fazer o outro lado 
                // receber certo os bytes
                var num: Int = number!!;
                var array: ByteArray = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(num).array();
                for (i in 0..array.size-1) {
                    output.writeByte(array[i].toInt());
                }

                // Envia o arquivo byte a byte
                for (i in 0..num-1) {
                    output.writeByte(mybytearray[i].toInt());
                }

                // Salva no log
                log.append("Server response: " + result(statusCode!!));
                log.newLine();
            }
        }
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
            
            // SUCCESS
            statusCode = 2;
        } catch (e: Exception) {
            // ERROR
            statusCode = 1;
            println(e.message);
        }
    
        return result;
    }

    override
    fun run() {
        try {
            var command: String;
            
            while(true) {
                // Recebe o primeiro byte e como não é de muita utilidade, esse byte não é guradado.
                input.readByte();
                // Recebe o codigo do comando que é transmitido no segundo byte.
                commandCode = input.readByte().toInt();
                
                // Envia o codigo do comando para a função options
                // recebendo de volta um string contendo o comando.
                command = option(commandCode!!);
                // Envia o comando para a função que irá decodificar oque resta da 
                // mesnagem enviada pelo cliente.
                dataInputs(command);

                println("Customer said => [" + clientAddress + "]: " + command);

                // Envia o comando para função que irá codificar e enviar a mensagem 
                // de volta para o cliente
                dataOutputs(command);
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
                fileWriter.close();
                log.close();
            } catch (ioe: IOException) {
                println("IOE: " + ioe);
            }
        }
        println("Client => [" + clientAddress + "] => communication thread closed.")
    }
}