/*
    Descrição:
        É o cliente do sistema de upload de arquivos via UDP. 
        Que envia as partes dos arquivos(1024 bytes) e um 
        checksum (SHA-1) que serve para o servidor comparar e 
        verificar a integridade do arquivo enviado.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 05/11/2021
    Data de atualização: 08/11/2021
*/
package question2;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.lang.*;
import java.security.*;
import java.nio.charset.*;
import java.nio.file.Files;
import javax.swing.JOptionPane;



fun main() {
    var dgramSocket: DatagramSocket;

    /* Armazena a porta de origem */
    var origPort: Int = 6665;

    dgramSocket = DatagramSocket(origPort); //cria um socket datagrama com a porta de origem

    try {
        
        /* Armazena o IP de destino */
        var dstIP: String = "127.0.0.1";

        /* Armazena a porta de destino */
        var dstPort: Int = 6666;

        /* Armazena o as informações do destino */
        var serverAddr: InetAddress = InetAddress.getByName(dstIP);
        var serverPort: Int = dstPort; // porta do servidor
        
        try {
            var aux = true;

            while(aux) {
                /* 
                    Cria um intância da classe Send e chama a fução run da classe 
                    que é responvel por toda lógica e envio do arquivo. 
                */
                var res = Send(serverAddr, serverPort, dgramSocket).run();
                
                var buffer: ByteArray = ByteBuffer.allocate(1000).array(); // cria um buffer para receber requisições

                /* cria um pacote vazio */
                var dgramPacket: DatagramPacket = DatagramPacket(buffer, buffer.size);
                dgramSocket.receive(dgramPacket);  // aguarda a chegada de datagramas

                /* imprime e envia o datagrama de volta ao cliente */
                println("Resposta: " + String(dgramPacket.getData(), 0, dgramPacket.getLength()));

                aux =  if (res.equals("EXIT")) false else true
            }
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        }
    } catch (e: SocketException) {
        println("Socket: " +  e.message);
    } catch (e: IOException) {
        println("IO: " + e.message);
    } finally {
        dgramSocket.close();
    } //finally
}

class Send {
    var serverAddr: InetAddress;
    var serverPort: Int = 0;
    var dgramSocket: DatagramSocket;
    var reader: Scanner = Scanner(System.`in`);

    /* construtor: Recebe e seta dados como porta de destino, socket e etc. */
    constructor(serverAddr: InetAddress, serverPort: Int, dgramSocket: DatagramSocket) {
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
        this.dgramSocket = dgramSocket;
    }
        
    // Recebe o comando e retorna somente o primeior argumento, que é método escolhido.
    fun split(command: String): String {
        var commandMain: Array<String> = command.split(" ").toTypedArray();

        return commandMain[0];
    }

    // Recebe o comando e retorna somente o segundo argumento, que é o FILENAME.
    fun splitFilepath(command: String): String {
        var commandMain: Array<String> = command.split(" ").toTypedArray();

        return if(commandMain.size == 2) commandMain[1] else "";
    }

    /* 
        Recebe o caminho do arquivo e cria um cheksum SHA-1 
        apartir dos bytes do arquivo. Ele será enviado junto
        com o arquivo para ser usado para checar se a integridade
        fo arquivo recebido.
    */
    fun cheksumSHA1(filepath: String): String {
        var md: MessageDigest = MessageDigest.getInstance("SHA-1");
        var dis: DigestInputStream = DigestInputStream(FileInputStream(filepath), md);
        try {
            while (dis.read() != -1);
            md = dis.messageDigest; 
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        }

        var result: StringBuilder = StringBuilder();
        for (i in md.digest()) {
            result.append(Integer.toString((i.toInt() and 0xff) + 0x100, 16).substring(1));
        }
    
        return result.toString();
    }

    /* 
        Função responsável por colocar traformar em 4 bytes 
        um int 32bits, que nesse caso é o tamanho do arquivo.
    */
    private fun write4BytesToBuffer(buffer: ByteArray, offset: Int, data: Int) {
        buffer[offset + 0] = (data shr 0).toByte()
        buffer[offset + 1] = (data shr 8).toByte()
        buffer[offset + 2] = (data shr 16).toByte()
        buffer[offset + 3] = (data shr 24).toByte()
    }

    /* 
        Função responsável por colocar decodificar os 4 bytes 
        um int 32bits, que nesse caso é o tamanho do arquivo.
    */
    private fun read4BytesFromBuffer(buffer: ByteArray, offset: Int): Int {
        return (buffer[offset + 3].toInt() shl 24) or
               (buffer[offset + 2].toInt() and 0xff shl 16) or
               (buffer[offset + 1].toInt() and 0xff shl 8) or
               (buffer[offset + 0].toInt() and 0xff)
    }

    /* 
        Função responsável por montar e enviar os datagramas
        apartir de um arquivo.
    */
    fun uploadFile(filepath: String): Boolean {
        try {
            /* 
                Recebe um caminho do arquivo e cria um File apartir 
                dele que contém as informações do arquivo.
            */
            var myfile: File = File(filepath);

            /* Pega o nome do arquivo */
            var filename: String = myfile.name; 
            /* Pega o arquivo do arquivo */
            var sizefile = myfile.length().toInt();
            
            /* Pega o checksum apartir do arquivo */
            var checksum: String = cheksumSHA1(filepath);
            /* Pega o tamanho do checksum */
            var sizechecksum = checksum.length;
            
            /* 
                Cria um array de bytes:
                    - Tipo da mensagem
                    - Tamanho do nome do arquivo
                    - Nome do arquivo
                    - Tamanho do arquivo
                    - Tamanho checksum
                    - Cheksum
            */
            var header: ByteArray = byteArrayOf(Options.UPLOAD.ordinal.toByte())
            header += byteArrayOf(filename.length.toByte());
            header += filename.toByteArray();
            header += ByteBuffer.allocate(4).array();

            /* 
                Chama a função responsável separar e colocar 
                um int 32 bits em 4 bytes. 
            */
            write4BytesToBuffer(header, filename.length+2, sizefile);
            
            header += sizechecksum.toByte();
            header += checksum.toByteArray();
            
            // Datagram packet header => (size filename, filename, size file, size checksum, checksum)
            var sendHeader = DatagramPacket(header, header.size, serverAddr, serverPort);

            /* Envia o datagrama para servidor */
            dgramSocket.send(sendHeader);

            /* Cria varios datagramas e envia para o servidor um por um */
            var fileByte: ByteArray = Files.readAllBytes(myfile.toPath());
            
            var aux = sizefile;
            var i = 0;
            var tamax = 1024;
            
            do {
                aux = aux - 1024;
                if (aux < 0) {
                    // Datagram packet file => (size filename, filename, size file)
                    var sendFile = DatagramPacket(fileByte, i, aux+1024, serverAddr, serverPort)
                    dgramSocket.send(sendFile);
                } else {
                    // Datagram packet file => (size filename, filename, size file)
                    var sendFile = DatagramPacket(fileByte, i, tamax, serverAddr, serverPort)
                    dgramSocket.send(sendFile);
                }
                i += 1024;
            } while(aux >= 0);

            return true;
        } catch (ioe: IOException) {
            println("IO: " + ioe);
            return false;
        }
    }

    /* 
        Função que decodifica qual tipo da mensagem de 
        acordo com a tipo recebido em string 
    */
    fun decodeOption(type: String): Int {
        when (type) {
            "UPLOAD" -> {
                return Options.UPLOAD.ordinal;
            }
            "EXIT" -> {
                return Options.UPLOAD.ordinal;
            }
            else -> {
                return -1;
            }
        }
    }

    /* 
        Função responsável por decodificar a mensagem 
        e montar o pacote que será enviado de acordo com a mensagem.
    */
    fun sendDatagram(msg: String) {  
        var type: Int = decodeOption(split(msg));

        var path: String = if ((msg.split(" ").toTypedArray()).size < 3 && (msg.split(" ").toTypedArray()).size <= 1) "ERROR" else splitFilepath(msg);

        when(type) {
            Options.UPLOAD.ordinal -> {
                uploadFile(path);
            }
            Options.EXIT.ordinal -> {
                println("EXIT");
            }
            else -> {
                println("ERROR: INVALID COMMAND");
            }
        }
    }

    fun run(): String {
        try{
            var msg: String = reader.nextLine();

            if (msg.equals("EXIT")) {
                var buffer: ByteArray = byteArrayOf(Options.EXIT.ordinal.toByte())
                // Datagram packet header => (size filename, filename, size file, size checksum, checksum)
                var req = DatagramPacket(buffer, buffer.size, serverAddr, serverPort)
                dgramSocket.send(req);
                return msg;
            } else {
                /* 
                    Chama a função responsável por decodificar a mensagem 
                    e montar o pacote que será enviado de acordo com a mensagem.
                */
                sendDatagram(msg);
                return "FOI";
            }
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
            return "ERROR";
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
            return "ERROR";
        }
    };
}

/* Enum de tipos de mensagens */
enum class Options {
    UPLOAD,
    EXIT,
}