Autores:
        Victor Daniel Manfrini Pires
        Emica Costa

OBS: O codigo é produzido usando javascript

## Sobre a base de dados
Pegamos uma base de dados onde classifica os tweets como sendo de male, female e brand.
Esses dados são pré-classsificados por pessoas normais, tendo como base a foto, 
conteudo do tweet e nome. No meio desses dados também temos alguns que não estão classificados em nenhum.
Nossa ideia foi basicamente oferecer ao cliente a opção de escolher entre tweets male, female e brand.
Nos temos um coletor que coleta nossos dados json e salva na fila principal. Temos um classificador que 
classifica os tweets de acordo com os generos apresentados anteriormente e salva em uma fila de acordo com 
o genero. Vale lembrar que nosso classificador também é responsavel por não deixar passar tweets que não sejam 
do genero male, female ou brand.


## MODO DE USO

# Para compilar

Antes de rodarmos o projeto, primeiro demovemos rodar comando 
`npm install`, ele ira instalar as dependencias do projeto.

# Para executar 
=> Para o Cliente
node client.js

=> Para o Classificador
node classifier.js

=> Para o Coletor
node collector.js

OBS: Rodar o colletor por ultimo. Por padrão começe executando o cliente, 
depois o classificador e por fim o coletor.

# Biblitecas usadas
Para esse trabalho utlizamos o node.js, RabbitMq.

# Exemplo de uso
===> client.js
Entrada
```
Sobre qual dos genêneros de auteres você deja ser notificado sobre os tweets Femininos (1), Masculinos (2), Não individuais (3) ou Concluido(0): 1
Sobre qual dos genêneros de auteres você deja ser notificado sobre os tweets Femininos (1), Masculinos (2), Não individuais (3) ou Concluido(0): 0
```
Saida:
```
 [x] Received => 
----------------
 Name: GardnerBetty2
Gender: female
Texto: The main strength relative to that be a elfdom shopper: KCK
Criado: 3/4/13 1:10
----------------


 [x] Received => 
----------------
 Name: gabbiwilliams9
Gender: female
Texto: I almost got ran over by the valet_���
Criado: 8/21/12 8:01
----------------
```


===> Question-2
==> TESTE 1
==> client.jar
UPLOAD TESTE-FILES/test.pdf

==> TESTE 2
==> client.jar
UPLOAD TESTE-FILES/README.md