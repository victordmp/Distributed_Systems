/*
    Descrição:
        Uma implemetção de um chat P2P que possibilita os clientes trocarem mensagens entre si.
        
        As mensagens possuem o formato:
            - tipo de mensagem [1 byte]
            - tipo (REQUEST/RESPONSE)
            - tamanho apelido (tam_apl) [1 byte]
            - apelido [tam_apl (1 a 64) bytes ]
            - tamanho mensagem (tam_msg) [1 byte]
            - mensagem [tam_msg bytes]

        Os tipos de mensagem aceitas são:
            1: mensagem normal
            2: emoji
            3: URL
            4: ECHO (envia e recebe a mesma mensagem para indicar que usuário está ativo).

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 04/11/2021
    Data de atualização: 08/11/2021
*/

package question1;

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
    var reader: Scanner = Scanner(System.`in`);

    try {
        /* Recebe e armazena a porta de origem */
        print("Qual sua porta? ");
        var origPort: Int = Integer.parseInt(reader.nextLine());

        dgramSocket = DatagramSocket(origPort); //cria um socket datagrama com a porta de origem
        
        /* Recebe e armazena o IP de destino */
        print("IP Destino: ");
        var dstIP: String = reader.nextLine();

        /* Recebe e armazena a porta de destino */
        print("Porta Destino? ");
        var dstPort: Int = Integer.parseInt(reader.nextLine());
        
        /* Recebe e armazena o apelido */
        print("Apelido: ");
        var nickname: String = reader.nextLine();

        /* Armazena o as informações do destino */
        var serverAddr: InetAddress = InetAddress.getByName(dstIP);
        var serverPort: Int = dstPort; // porta do servidor
        
        try {
            /* Cria e da start na thread resposável por eviar os datagrmas de mensagens */
            ThreadSend(serverAddr, serverPort, dgramSocket, nickname).start();
            /* Cria e da start na thread resposável por receber e decodificar os datagrmas de mensagens */
            ThreadReceived(serverAddr, serverPort, dgramSocket, nickname).start(); 
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        }

    } catch (e: SocketException) {
        println("Socket: " +  e.message);
    } catch (e: IOException) {
        println("IO: " + e.message);
    }
}

class ThreadSend: Thread {
    var serverAddr: InetAddress;
    var serverPort: Int = 0;
    var dgramSocket: DatagramSocket;
    var nickname: String;
    var reader: Scanner = Scanner(System.`in`);

    /* construtor: Recebe e seta dados como apelido, porta de destino, socket e etc. */
    constructor(serverAddr: InetAddress, serverPort: Int, dgramSocket: DatagramSocket, nickname: String) {
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
        this.dgramSocket = dgramSocket;
        this.nickname = nickname;
    }

    /* 
        Função resposável por decodificar a mensagem e indetificar de qual tipo ela é.
        Retorna um inteiro que idetifica o tipo da mensagem.   
    */
    fun decodeType(msg: String): Int {
        val regex = """(http(s)?://.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)""".toRegex();
        if (regex.matches(msg)) {
            return Options.URL.ordinal;
        } else if (false) {
            return Options.EMOJI.ordinal;
        } else if (msg.toUpperCase().equals("ECHO")) {
            return Options.ECHO.ordinal;
        } else {
            return Options.MESSAGE.ordinal;
        }
    }

    /* 
        Função resposável por montar e rotornar um array de bytes que será eviado via um datagrama.  
    */
    fun sendDatagram(msg: String): ByteArray {
        /* Tipo da mensagem */
        var type: Int =  decodeType(msg);

        /* 
            Cria um array de bytes:
                - Tipo da mensagem
                - Tipo (REQUEST/RESPONSE)
                - Tamanho do apelido
                - Apelido
                - Tamanho da mensagem
                - Mensagem
        */
        var datagram: ByteArray = byteArrayOf(type.toByte());
        datagram += Type.REQUEST.ordinal.toByte();
        datagram += nickname.length.toByte();
        datagram += nickname.toByteArray();
        datagram += msg.length.toByte(); 
        datagram += msg.toByteArray();
       
        return datagram;
    }
    
    override 
    fun run( ) {
        try{
            while(true) {
                var msg: String = reader.nextLine();
    
                var m: ByteArray = sendDatagram(msg); // Envia mensagem para função que transforma a mensagem em bytes
    
                /* cria um pacote datagrama com array de bytes montado na linha anterior */
                var request: DatagramPacket = DatagramPacket(m, m.size, serverAddr, serverPort);
    
                /* envia o pacote */
                dgramSocket.send(request);
            }
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        }
    };
}
    
class ThreadReceived: Thread {
    var serverAddr: InetAddress;
    var serverPort: Int = 0;
    var dgramSocket: DatagramSocket;
    var nickname: String;

    /* construtor: Recebe e seta dados como apelido, porta de destino, socket e etc. */
    constructor(serverAddr: InetAddress, serverPort: Int, dgramSocket: DatagramSocket, nickname: String) {
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
        this.dgramSocket = dgramSocket;
        this.nickname = nickname;
    }

    /* 
        Função resposável por decodificar datagrama e printar a mensagem.  
    */
    fun receivedDatagram(reply: DatagramPacket) {
        /* Pega o tipo de mensagem do datagrama UDP que é o primeiro byte */
        var type: Int =  reply.getData()[0].toInt();

        /* Pega o tamanho de apelido que é terceiro byte */
        var nicknameSize: Int = reply.getData()[2].toInt();
        /* Pega 0 apelido apartir do quarto byte até o tamaho do apelido */
        var nickname = String(reply.getData(), 3, nicknameSize);

        /* Pega o tamanho da mensagem */
        var messageSize: Int = reply.getData()[nicknameSize].toInt();
        /* Pega a mensagem */
        var message = String(reply.getData(), nicknameSize+4, messageSize);

        /* De acordo com o tipo da mensagem ele toma ações diferentes */
        when(type) {
            Options.MESSAGE.ordinal -> {
                /* Mensagem normal então só printa o apelido e a mensagem */
                println("$nickname: " + message);
            }
            Options.EMOJI.ordinal -> {
                /* Como é um emoji então printa um tag junto com a mensagem que identifica que é um emoji */
                println("$nickname: EMOJI => " + message);
            }
            Options.URL.ordinal -> {
                /* Como é uma url então printa um tag junto com a mensagem que identifica que é uma url */
                println("$nickname: URL => " + message);
            }
            Options.ECHO.ordinal -> {
                /* Pega o tipo do datagrama UDP que é o segundo byte */
                var typeEcho: Int = reply.getData()[1].toInt();

                when (typeEcho) {
                    Type.REQUEST.ordinal -> {
                        /* 
                            Se for do tipo REQUEST então envia um datagrama do tipo RESPOSE como resposta.
                            Isso serve com uma confirmação para o outro lada de que ainda está ativo.
                        */

                        /* 
                            Cria um array de bytes:
                                - Tipo da mensagem ECHO
                                - Tipo RESPONSE
                                - Tamanho do apelido
                                - Apelido
                                - Tamanho da mensagem
                                - Mensagem
                        */
                        var datagram: ByteArray = byteArrayOf(type.toByte());
                        datagram += Type.RESPONSE.ordinal.toByte();
                        datagram += this.nickname.length.toByte();
                        datagram += this.nickname.toByteArray();
                        datagram += message.length.toByte(); 
                        datagram += message.toString().toByteArray();

                        /* Cria um pacote datagrama com array de bytes montado nas linhas anteriores */
                        var request: DatagramPacket = DatagramPacket(datagram, datagram.size, serverAddr, serverPort);
    
                        /* Envia o datagrama */
                        dgramSocket.send(request);
                    }
                    Type.RESPONSE.ordinal -> {
                        /* Como é um tipo ECHO e RESPONSE então prita a mesagem que o cliente está conectado */
                        println("O cliente $nickname está ativo.");
                    }
                }
            }
        }
    }

    override 
    fun run() {
        try {
            while(true) {
                /* cria um buffer vazio para receber datagramas */
                var buffer: ByteArray = ByteBuffer.allocate(1000).array();
                var reply: DatagramPacket = DatagramPacket(buffer, buffer.size);
                
                /* aguarda datagramas */
                dgramSocket.receive(reply);
                
                /* Enviar datagram recebido para a função responsável por decodificar a mensagem */
                receivedDatagram(reply);
            }
        } catch (eof: EOFException) {
            println("EOF: " + eof.message);
        } catch (ioe: IOException) {
            println("IOE: " + ioe.message);
        }
    };
}

/* Enum de tipos de mensagens */
enum class Options {
    MESSAGE,
    EMOJI,
    URL,
    ECHO,
}

/* Enum de tipos do datagrama se é requisição ou resposta */
enum class Type {
    REQUEST,
    RESPONSE,
}