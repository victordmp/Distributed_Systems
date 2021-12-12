Autores:
        Victor Daniel Manfrini Pires
        Emica Costa

OBS: O codigo foi produzido usando java para implementação do Servidor e Python no Cliente.

## MODO DE USO
==> OBS: Acesse a pasta Question e depois em dois terminais separados acesse em uma pasta javacode onde está o código do servidor  e no outro terminal acesse a pasta python code onde está o código do cliente. 

# Para compilar
=> Para o cliente
Neste a caso por estarmos usando python ele não precisa ser compilado apenas execultado.

=> Para o servidor
javac -classpath .:protobuf-java-3.19.1.jar:sqlite-jdbc-3.36.0.3.jar Server.java

OBS: Os dois arquivos .jar(rotobuf-java-3.19.1.jar e sqlite-jdbc-3.36.0.3.jar) já estão a pasta javacode.
# Para executar 
=> Para o cliente
python3 client.py

=> Para o servidor
java -classpath .:protobuf-java-3.19.1.jar:sqlite-jdbc-3.36.0.3.jar Server    

OBS: Primeiro execulte o servidor e depois o clinte. Por usar threads na implementação do servidor, ele pode se conectar com multiplos clientes.

# Biblitecas usadas
As bibliotecas são todas do java e do python. Duas ferramentas extras foram adicionadas entre elas o protobuf e sqlite.

# Exemplo de uso
==> client
Ao execultar o programa ira aparecer perguntas sobre que ação você deseja tomar.
Na primeira pergunta responda: 3.
Na segunda perguta responda: 2.
Complete os campos requeridos com os seguintes dados:
RA: 23
ANO: 2019
SEMESTRE: 7

Exemplo de Requisição do Cliente:
```
Deseja fazer uma Inserção (1), Atualização (2), Consulta (3) ou Sair (0): 3

Deseja fazer a Consulta de alunos (1), Boletim de um aluno (2) ou Sair (0): 2

CLIENTE:

Digite o RA do aluno: 23
Digite o ANO da disciplina: 2019
Digite o SEMESTRE da disciplina: 7
```

Exemplo de Resposta do Servidor:
```
SERVIDOR:

Aluno:
RA: 23
Nome: Tammera
--
Nome da Disciplina: Compiladores (IC6A_CM)
Nota: 9.5
Faltas: 5
--
--
Nome da Disciplina: MECANICA GERAL 1 (ID3A_CM)
Nota: 7.5
Faltas: 12
--
Requisição realizada com sucesso!
```
