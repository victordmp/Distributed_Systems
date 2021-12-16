#!/usr/bin/env node
/*
    Descrição:
        Esse parte é referente ao coletor, ele é responsavel por coletar os 
        dados no arquivo json e enviar para a fila principal de tweets.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 12/11/2021
    Data de atualização: 15/11/2021
*/

// Reposanvel por ler e fazer o parse do arquivo JSON
var fs = require('fs');
var data = fs.readFileSync('data/csvjson.json', 'utf8');
var datas = JSON.parse(data);

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
    // Nome da fila que irá receer todos os dados lidos do JSON
    var queue = 'tweets_all_queue';

    // Garante que a fila seja declarada antes de ser consumida
    channel.assertQueue(queue, {
      durable: false
    });
    // Enviar para fila linha dado por dado do JSON
    datas.map((line) => {
        // Envia para fila tweets_all_queue
        channel.publish ('', queue, Buffer.from(JSON.stringify(line)));
        // Printa o id referente dado que foi enviado
        console.log(" [x] Sent '%s'", JSON.stringify(line._unit_id));
    });
  });
  // Encerra a conexão
  setTimeout(function() {
    connection.close();
    process.exit(0)
  }, 500);
});