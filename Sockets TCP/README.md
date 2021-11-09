Autores:
        Victor Daniel Manfrini Pires
        Emica Costa

OBS: O codigo é produzido usando kotlin


# Neste link está presente um tutorial de como instalar o koltin
Link => https://kotlinlang.org/docs/command-line.html#install-the-compiler

## MODO DE USO
==> OBS: Para cada um das questões o modo de compilação e de execução é o mesmo. 
Mas lembre de acessear o diretorio da questão 1 ou 2 para netão compilar ou execultar. 

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

OBS: Primeiro de tudo execulte o servidor e depois o clinte.

# Biblitecas usadas
As bibliotecas são todas do java. Não foi adicionada nenhuma biblioteca extra.

# Exemplo de uso
===> Question-1
==> client.jar
ADDFILE print.txt

===> Question-2
==> client.jar
CONNECT victordmp,12459