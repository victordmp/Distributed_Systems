/*
    Descrição:
        É um servidor do sistema de upload de arquivos via UDP. 
        Receber as partes dos arquivos(1024 bytes) e verificar ao 
        final a integridade via um checksum (SHA-1) e armazenar o 
        arquivo em uma pasta padrão.

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

fun main(){ 
    var dgramSocket: DatagramSocket? = null; // cria um socket datagrama em uma porta especifica
    
    try{
        dgramSocket = DatagramSocket(6666);

        var aux = true;
        
        while(aux){
            var buffer: ByteArray = ByteBuffer.allocate(1000).array(); // cria um buffer para receber requisições
        
            /* cria um pacote vazio */
            var dgramPacket: DatagramPacket = DatagramPacket(buffer, buffer.size);
            dgramSocket.receive(dgramPacket);  // aguarda a chegada de datagramas

            /* 
                Cria um intância da classe Received e chama a fução run da classe 
                que é responvel por toda lógica de recebbimento do arquivo. 
            */
            var res = Received(dgramSocket, dgramPacket).run(); 

            /* Envia uma resposta para o cliente */
            var array: ByteArray = res.toByteArray();
            var reply: DatagramPacket = DatagramPacket(array,
                    array.size, dgramPacket.getAddress(), dgramPacket.getPort()); // cria um pacote com os dados
            dgramSocket.send(reply); // envia o pacote

            aux =  if (res.equals("EXIT")) false else true
        } //while
    } catch (e: SocketException){
        println("Socket: " + e.message);
    } catch (e: IOException) {
        println("IO: " + e.message);
    } finally {
        dgramSocket!!.close();
    } //finally
}

class Received {
    var dgramSocket: DatagramSocket;
    var dgramPacket: DatagramPacket;

    /* construtor: Recebe e seta dados como porta de destino, socket e etc. */
    constructor(dgramSocket: DatagramSocket, dgramPacket: DatagramPacket) {
        this.dgramSocket = dgramSocket;
        this.dgramPacket = dgramPacket;
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
        Função responsável por receber e remontar o arquivo recebido.
    */
    fun downloadFile(filename: String, sizefile: Int): String {
        try {
            // Responsavel por criar uma "copia" do arquivo recebido byte a byte do servidor
            var myfile: File = File("./UPLOAD/" + filename);
            var fos: FileOutputStream = FileOutputStream(myfile);

            var aux = sizefile;

            do {
                var buffer: ByteArray;
                aux -= 1024;
                if (aux < 0) {
                    buffer = ByteBuffer.allocate(aux+1024).array();
                } else {
                    buffer = ByteBuffer.allocate(1024).array();
                }
                var receiveFile = DatagramPacket(buffer, buffer.size)
                dgramSocket.receive(receiveFile);
                fos.write(receiveFile.getData());
            } while(aux >= 0);


            return "./UPLOAD/" + filename;
        } catch (ioe: IOException) {
            println("IO: " + ioe);
            return "";
        }
    }

    /* 
        Função responsável por receber e decodificar o datagrama header.
    */
    fun receivedDatagram(): String {
        /* Pega do datagrama o tipo de mensagem */
        var type: Int = dgramPacket.getData()[0].toInt();

        when(type) {
            Option.UPLOAD.ordinal -> {
                /* Pega o tamanho do nome do arquivo */
                var sizefilename = dgramPacket.getData()[1].toInt();
                /* Pega o nome do arquivo */
                var filename = String(dgramPacket.getData(), 2, sizefilename); 
                /* Pega o tamanho do arquivo */
                var sizefile = read4BytesFromBuffer(dgramPacket.getData(), sizefilename+2);
                /* Pega o tamanho do cheksum do arquivo */
                var sizechecksum = dgramPacket.getData()[sizefilename+6].toInt();
                /* Pega o cheksum do arquivo */
                var checksum = String(dgramPacket.getData(), sizefilename+7, sizechecksum);

                /* Chama a função que remonta o arquivo apartir dos datagramas recebidos. */
                var ret = downloadFile(filename.toString(), sizefile);

                println("Dowload OK? " + checksum.equals(cheksumSHA1(ret)));
                return if (checksum.equals(cheksumSHA1(ret))) "Upload: ok" else "Upload: error";
            }
            Option.EXIT.ordinal -> {
                println("EXIT");
                return "EXIT";
            }
            else -> {
                println("ERROR: INVALID COMMAND");
                return "ERROR";
            }
        }
    }
     
    fun run(): String {
        try{
            /* 
                Chama a função responsável por receber e remontar o arquivo recebido.
            */
            return receivedDatagram(); // transforma a mensagem em bytes
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
            return "ERROR";
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
            return "ERROR";
        }
    };
}

enum class Option {
    UPLOAD,
    EXIT,
}