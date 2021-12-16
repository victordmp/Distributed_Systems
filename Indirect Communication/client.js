#!/usr/bin/env node
/*
    Descrição:
        Esse parte é referente ao cliente, ele é responsavel por esclher quais topicos 
        ele deseja assinar entre male, female e brand. Depois de escolhido ele recebera 
        sempre que um tweet dos tipos que ele escolher for colocado na fila desse tipo.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 12/11/2021
    Data de atualização: 15/11/2021
*/

var amqp = require('amqplib/callback_api');

const readline = require('readline-sync');

// lista de nomes das filas
names_queue = [];

// Verifica quais assinaturas o cliente deseja
let req_type = -1;
while(req_type != 0) {
  req_type = readline.question('\nSobre qual dos genêneros de auteres você deja ser notificado sobre os tweets Femininos (1), Masculinos (2), Não individuais (3) ou Concluido(0): ');
  addTypeTweets(req_type);
}

// Função que recebe o topico de tweet escolhido e da um append na lista
function addTypeTweets(type) {
  if(parseInt(type) != 0) {
    if(parseInt(type) == 1 && !names_queue.includes('feminino_queue')){
        names_queue.push('feminino_queue');
    } else if(parseInt(type) == 2 && !names_queue.includes('masculino_queue')){
      names_queue.push('masculino_queue'); 
    } else if(parseInt(type) == 3 && !names_queue.includes('nao_individual_queue')) {
      names_queue.push('nao_individual_queue');
    } else {
      console.log("Tipo Inválido(Não pertence as opções oferecidas ou já está marcado como assinado)! Tente novamente!\n");
    }
  }
}

// Cria a conexão
amqp.connect('amqp://localhost', function(error0, connection) {
  if (error0) {
    throw error0;
  }
  connection.createChannel(function(error1, channel) {
    if (error1) {
      throw error1;
    }

    // Essa é uma opção setada para que cada que o classificador
    // receba somente uma messagem por vez.
    names_queue.map((queue) => {
      channel.assertExchange('logs', 'direct');
      
      channel.assertQueue('', {
        exclusive: false
      }, function(error2, q) {
        if (error2) {
          throw error2;
        }

        console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
        // Vincula o q.queue com as assinaturas do cliente que é o queue
        channel.bindQueue(q.queue, 'logs', queue);

        // Reponsavel por ouvir as modificações na fila que ele deseja ouvir
        channel.consume(q.queue, function(msg) {
          if(msg.content) {
            // Faz o parser do tweet, tranformando o JSON em um objeto 
            // onde podemos ter acesso ao seus atributos.
            var tweet = JSON.parse(msg.content);
    
            // Printa o tweet
            console.log("\n [x] Received => \n----------------\n Name: " + tweet.name 
                            + "\nGender: " + tweet.gender 
                            + "\nTexto: " + tweet.text 
                            + "\nCriado: " + tweet.created 
                            + "\n----------------\n");
          }    
        }, {
          // envia um ack como resposta que recebeu a msg
          noAck: false
        });
      });
    });
  });
});