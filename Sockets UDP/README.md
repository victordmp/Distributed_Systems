Autores:
        Victor Daniel Manfrini Pires
        Emica Costa

OBS: O codigo é produzido usando kotlin


# Neste link está presente um tutorial de como instalar o koltin
Link => https://kotlinlang.org/docs/command-line.html#install-the-compiler

## MODO DE USO
==> OBS: Na questão 1 só compilar e executar o cliente, já na questão 2 deve compilar e executar o cliente e servidor. 
Mas lembre de acessear o diretorio da questão 1 ou 2 para compilar ou execultar. 

# Para compilar
=> Generico
kotlinc {nome do arquivo}.kt -include-runtime -d {nome escolhido para o .jar}.jar

=> Para o cliente
kotlinc Client.kt -include-runtime -d client.jar

=> Para o servidor
kotlinc Server.kt -include-runtime -d server.jar

# Para executar 
=> Generico
java -jar {nome do .jar gerado após a compilação}.jar

=> Para o cliente
java -jar client.jar

=> Para o servidor
java -jar server.jar

OBS: Na questão 2 primeiro execulte o servidor e depois o clinte.

OBS: Na questão 1 como se trata de um chat vc deve executar apenas o cliente e duas vezes.

# Biblitecas usadas
As bibliotecas são todas do java. Não foi adicionada nenhuma biblioteca extra.

# Exemplo de uso
===> Question-1
==> client.jar
(Escreva qualquer mensagem de um para o outro).

===> Question-2
==> TESTE 1
==> client.jar
UPLOAD TESTE-FILES/test.pdf

==> TESTE 2
==> client.jar
UPLOAD TESTE-FILES/README.md