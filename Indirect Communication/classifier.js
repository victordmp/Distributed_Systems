#!/usr/bin/env node
/*
    Descrição:
        Esse parte é referente ao classicador, ele é responsavel por classificar
        os dados do tipo male, female, brand. Ele também insere esses dados nas filas 
        referentes ao tipo dele.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 12/11/2021
    Data de atualização: 15/11/2021
*/

var amqp = require('amqplib/callback_api');

// Cria a conexão
amqp.connect('amqp://localhost', function(error0, connection) {
  if (error0) {
    throw error0;
  }
  connection.createChannel(function(error1, channel) {
    if (error1) {
      throw error1;
    }
    // nome da fila que irá gurdar todos os tweets
    var queue = 'tweets_all_queue';

    channel.assertQueue(queue, {
      durable: false
    });
    // Essa é uma opção setada para que cada que o classificador
    //  receba somente uma messagem por vez.
    channel.prefetch(1);
    console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", queue);
    channel.consume(queue, function(msg) {
        // Faz o parser do tweet, tranformando o JSON em um objeto 
        // onde podemos ter acesso ao seus atributos.
        var tweet = JSON.parse(msg.content);
        // Id do tweet
        var id = tweet._unit_id;
        // Genêro do autor(a) do tweet
        var authors_gender = tweet.gender;

        // nome da fila
        var queue_name = '';

        // Switch que verifica e separa em filas por genêro
        switch (authors_gender) {
            case 'male':
                queue_name = "masculino_queue";
                break;
            case 'female':
                queue_name = "feminino_queue";
                break;
            case 'brand':
                queue_name = "nao_individual_queue";
                break;
            default:
                break;
        }

        // Verifica se o tweet pertence a algum dos tipos.
        // Caso não prertença a string vai estar vazia.
        if (queue_name.length > 0) {
          channel.assertExchange('logs', 'direct');
          // Envia para fila tweets_all_queue
          channel.publish ('logs', queue_name, Buffer.from(msg.content));
        }
        
        // Printa o id da linha recebida
        console.log(" [x] Received [%s] => Gender: %s", id, authors_gender);
        
        // envia um ack como resposta que recebeu a msg
        var secs = msg.content.toString();
        setTimeout(function() {
            channel.ack(msg);
        }, secs * 1000);
    }, {
      noAck: false
    });
  });
});