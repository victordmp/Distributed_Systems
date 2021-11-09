/*
    Descrição:
        Esse codigo é referente a parte do cliente da atividade de sockets TCP - Questão 2.
        Neste arquivo esta implementado toda a logica de comunicação da parte do cliente. 
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
import java.util.*;
import java.security.*;
import java.nio.charset.*;

// Varivel global que guarda o nome do arquivo do qual será feito o download
var filenameD: String = "";

// Recebe o comando e retorna somente o primeior argumento, que é método escolhido.
fun split(command: String): String {
    var commandMain: Array<String> = command.split(" ").toTypedArray();

    return commandMain[0];
}

// Recebe o comando e retorna somente o segundo argumento, que é o FILENAME.
fun splitFilename(command: String): String {
    var commandMain: Array<String> = command.split(" ").toTypedArray();

    return if(commandMain.size == 2) commandMain[1] else "";
}

// Verifica se o comando possui dois argumentos,
// sendo primeiro argumento o a método e o segundo o arquivo.
// Se possuir dois  argumentos o retorno é verdadeiro caso contrario é falso.
// OBS.: Essa é um pequena verificação, porem não cobre todos os casos 
// é apenas para um situação especifica.
fun commandIsValid(command: String): Boolean {
    var res: Array<String> = command.split(" ").toTypedArray();

    return if (res.size == 2) true else false;
}

// Função que recebe o código de 0 a 4 e retorna uma string 
// que se refere ao método invocado correspondente a esse codigo
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

// Função que recee o código do resultado e retorna um string 
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

// É responsavel por criar umontar a mesagem que é enviada para o servidor
fun dataOutput(command: String, output: DataOutputStream) {
    var filename: String = splitFilename(command);

    when(split(command)) {
        "ADDFILE" -> {
            // Array de bytes contendo os 3 primeiros argumentos que são (tipo, método escolhido, tamanho do nome)
            var array: ByteArray = byteArrayOf(1.toByte(), 1.toByte(), filename.length.toByte());
            // Array de bytes criado somente para o filename
            var filenameByte: ByteArray = filename.toByteArray();
            // Envia byte a byte os 3 primeios argumentos
            for (i in 0..array.size-1) {
                output.writeByte(array[i].toInt());
            }
            // Envia byte a byte os filename
            for (i in 0..filenameByte.size-1) {
                output.writeByte(filenameByte[i].toInt());
            }
        }
        "DELETE" -> {
            // Array de bytes contendo os 3 primeiros argumentos que são (tipo, método escolhido, tamanho do nome)
            var array: ByteArray = byteArrayOf(1.toByte(), 2.toByte(), filename.length.toByte());
            // Array de bytes criado somente para o filename
            var filenameByte: ByteArray = filename.toByteArray();
            // Envia byte a byte os 3 primeios argumentos
            for (i in 0..array.size-1) {
                output.writeByte(array[i].toInt());
            }
            // Envia byte a byte os filename
            for (i in 0..filenameByte.size-1) {
                output.writeByte(filenameByte[i].toInt());
            }
        }
        "GETFILESLIST" -> {
            // Array de bytes contendo os 3 primeiros argumentos que são (tipo, método escolhido, tamanho do nome)
            // Como nesse caso o filename não é necessario então não passamos nenhuma valor para ele
            // nesse caso assumimos que p tamanho dele é 0.
            var array: ByteArray = byteArrayOf(1.toByte(), 3.toByte(), filename.length.toByte());
            for (i in 0..array.size-1) {
                output.writeByte(array[i].toInt());
            }
        }
        "GETFILE" -> {
            // Array de bytes contendo os 3 primeiros argumentos que são (tipo, método escolhido, tamanho do nome)
            var array: ByteArray = byteArrayOf(1.toByte(), 4.toByte(), filename.length.toByte());
            // Array de bytes criado somente para o filename
            var filenameByte: ByteArray = filename.toByteArray();
            // Envia byte a byte os 3 primeios argumentos
            for (i in 0..array.size-1) {
                output.writeByte(array[i].toInt());
            }
            // Envia byte a byte os filename
            for (i in 0..filenameByte.size-1) {
                output.writeByte(filenameByte[i].toInt());
            }
            filenameD = filename;
        }
    }
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

        var command: String;

        while (true) {
            print("Message: ");
            // Recebe o comando
            command = reader.nextLine();
            // Caso sejá digitado exit fecha a comunicação e encerra o programa
            if (command.equals("EXIT")) break;

            if (split(command) != "GETFILESLIST" && split(command) != "GETFILE" && commandIsValid(command)) {    
                // Chama a função que é responsavel por criar e envair a mensagem para o servidor
                dataOutput(command, output);
                // Recebe byte a byte a resposta do servidor
                for (i in 0..2) {
                    if (i == 2) {
                        println("Server said: " + result(input.readByte().toInt()));
                    } else {
                        input.readByte();
                    }
                }
            } else if (split(command) == "GETFILESLIST") {
                // Chama a função que é responsavel por criar e envair a mensagem para o servidor
                dataOutput(command, output);
                // Recebe byte a byte a resposta do servidor
                for (i in 0..2) {
                    if (i == 2) {
                        println("Server said: " + result(input.readByte().toInt()));
                    } else {
                        input.readByte();
                    }
                }

                // Quantidades de arquivos que serão recebidos
                var numFiles = input.readByte().toInt();

                // Faz um laço que recebe uma lista de arquivos do servidor, e printa isso na tela
                for (i in 0..numFiles-1) {
                    // Recebe byte a byte, converte cada byte em char e depois junta eles em uma string
                    // fromando assim o nome do arquivo. Isso é feito toda vez para cada um dor arquivos 
                    // recebidos do servidor
                    var filenameChar: Char;
                    var filename: String = "";
                    var size: Int = input.readByte().toInt();
                    for (j in 0..size-1) {
                        filenameChar = input.readByte().toChar();
                        filename += filenameChar;
                    }
                    println(filename);
                }
            } else if (split(command) == "GETFILE" && commandIsValid(command)) {
                // Chama a função que é responsavel por criar e envair a mensagem para o servidor
                dataOutput(command, output);
                // Recebe byte a byte a resposta do servidor
                for (i in 0..2) {
                    if (i == 2) {
                        println("Server said: " + result(input.readByte().toInt()));
                    } else {
                        input.readByte();
                    }
                }

                // É responsavel por guradar o valor do tamamnho do arquivo
                var sizeFile: Int = 0; 
                
                // Pega os quatro bytes referentes ao tamanho do arquivo e junta soma eles
                // obtendo assim o tamanho real do arquivo
                for (i in 0..3) {
                    sizeFile += input.readByte().toInt();
                }

                // Responsavel por criar uma "copia" do arquivo recebido byte a byte do servidor
                var myfile: File = File("./Client-Files/" + filenameD);
                var fos: FileOutputStream = FileOutputStream(myfile);
                for (i in 0..sizeFile-1) {
                    fos.write(input.readByte().toInt());
                }
            } else {
                println("ERROR => This Command is invalid! Type Again!");
            }
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