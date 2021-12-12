# /*
#     Descrição:
#         É uo cliente que envia requisições para o servidor via TCP 
#         usando o protobuffer para serealização e descerealização. 
#         O banco de dados usado e sqlite. Essa implemntação e rerente 
#         atividade de representação extrema de dados. Todas as especificações 
#         estão contidas no pdf atividade_red_v3.pdf.

#     Autores: 
#         Victor Daniel Manfrini Pires 
#         Emica Costa

#     Data de criação: 19/11/2021
#     Data de atualização: 26/11/2021
# */
import socket
import noteManagement_pb2;

# Função responsavel por pega o RA do aluno.
# Retorna um inteiro referente ao RA do aluno. EXEMPLO: 
# RA: 201234.
def getRA():
    return int(input("Digite o RA do aluno: "))

# Função responsavel por pegar o codigo da displina.
# Retorna uma string referente ao codigo da disciplina. EXEMPLO: 
# cod_disciplina: LM31A.
def getCodDisciplina():
    return input("Digite o CÓDIGO da disciplina: ")

# Função responsavel por pegar o ano.
# Retorna um inteiro referente ao ano. EXEMPLO: 
# ano: 2020.
def getAno():
    return int(input("Digite o ANO da disciplina: "))

# Função responsavel por pegar o semestre.
# Retorna um inteiro referente ao semestre. EXEMPLO: 
# semestre: 2.
def getSemestre():
    return int(input("Digite o SEMESTRE da disciplina: "))

# Função responsavel por pegar a nota do aluno.
# Retorna um ponto flutuante pois se trata de uma 
# nota que por ser por exemplo 6.43.
def getNota():
    return float(input("Digite a NOTA do aluno: "))

# Função responsavel por pegar a quantidade de faltas do aluno.
# Retorna um inteiro poius se refere ao número de faltas
def getFaltas():
    return int(input("Digite o NÚMERO de faltas do aluno: "))

# Função que cria uma nova matricula
def createMatricula():
    # Monta e envia o tipo da mensagem que é um número de 1 a 5
    # representando a ação requerida, neste caso é o tipo 1
    # O tipo de mensagem também esta definido no protobuf
    # com o nome TypeMessage.
    type = noteManagement_pb2.TypeMessage()
    type.type = 1
    msgType = type.SerializeToString()
    size = len(msgType)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msgType)

    # Monta a estrutura de matricula para enviar, a 
    # estrura de matricula foi gerada no protobuf com 
    # o nome Matricula. Neste caso enviaremos todas 
    # as informações de matricula pois estamos querendo 
    # criar uma nova matricula. As unicas informações que 
    # não são enviadas nesse caso são NOTA e FALTAS. Isso 
    # ocorre porqque o valor padrão na hora criação de uma 
    # nova matricula é 0 para os dois campos.
    print("\nCLIENTE:\n")
    matricula = noteManagement_pb2.Matricula()
    matricula.RA = getRA()
    matricula.cod_disciplina = getCodDisciplina()
    matricula.ano = getAno()
    matricula.semestre = getSemestre()
    msg = matricula.SerializeToString()
    size = len(msg)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebe a resposta do servidor. Se a ação der certo ele 
    # retorna que a ação funcionou caso contrario 
    # retorna o erro encotrado.
    reply = noteManagement_pb2.MsgReply()
    reply.ParseFromString(client_socket.recv(1024))
    print("\nSERVIDOR:\n" + reply.message)

# Função responsavel por atualizar a nota
def updateNotas():
    # Monta e envia o tipo da mensagem que é um número de 1 a 5
    # representando a ação requerida, neste caso é o tipo 2
    # O tipo de mensagem também esta definido no protobuf
    # com o nome TypeMessage.
    type = noteManagement_pb2.TypeMessage()
    type.type = 2
    msgType = type.SerializeToString()
    size = len(msgType)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msgType)

    # Monta a estrutura de matricula para enviar, a 
    # estrura de matricula foi gerada no protobuf com 
    # o nome Matricula. Neste caso não enviaremos todas 
    # as informações de matricula pois não é necessario.
    # Precisamos somente do RA, CÓDIGO DA DISCIPLINA e 
    # NOTA(valor que deseja atualizar).
    print("\nCLIENTE:\n")
    matricula = noteManagement_pb2.Matricula()
    matricula.RA = getRA()
    matricula.cod_disciplina = getCodDisciplina()
    matricula.nota = getNota()
    msg = matricula.SerializeToString()
    size = len(msg)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebe a resposta do servidor. Se a ação der certo ele 
    # retorna que a ação funcionou caso contrario 
    # retorna o erro encotrado.
    reply = noteManagement_pb2.MsgReply()
    reply.ParseFromString(client_socket.recv(1024))
    print("\nSERVIDOR:\n" + reply.message)

# Função responsavel por atualizar a Faltas
def updateFaltas():
    # Monta e envia o tipo da mensagem que é um número de 1 a 5
    # representando a ação requerida, neste caso é o tipo 3
    # O tipo de mensagem também esta definido no protobuf
    # com o nome TypeMessage.
    type = noteManagement_pb2.TypeMessage()
    type.type = 3
    msgType = type.SerializeToString()
    size = len(msgType)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msgType)

    # Monta a estrutura de matricula para enviar, a 
    # estrura de matricula foi gerada no protobuf com 
    # o nome Matricula. Neste caso não enviaremos todas 
    # as informações de matricula pois não é necessario.
    # Precisamos somente do RA, CÓDIGO DA DISCIPLINA e 
    # FALTAS(valor que deseja atualizar).
    print("\nCLIENTE:\n")
    matricula = noteManagement_pb2.Matricula()
    matricula.RA = getRA()
    matricula.cod_disciplina = getCodDisciplina()
    matricula.faltas = getFaltas()
    msg = matricula.SerializeToString()
    size = len(msg)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebe a resposta do servidor. Se a ação der certo ele 
    # retorna que a ação funcionou caso contrario 
    # retorna o erro encotrado.
    reply = noteManagement_pb2.MsgReply()
    reply.ParseFromString(client_socket.recv(1024))
    print("\nSERVIDOR:\n" + reply.message)

# Função responsavel por listar alunos de uma disciplina 
# informado a disciplina, ano e semestre.
def getAlunos():
    # Monta e envia o tipo da mensagem que é um número de 1 a 5
    # representando a ação requerida, neste caso é o tipo 4
    # O tipo de mensagem também esta definido no protobuf
    # com o nome TypeMessage.
    type = noteManagement_pb2.TypeMessage()
    type.type = 4
    msgType = type.SerializeToString()
    size = len(msgType)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msgType)

    # Monta a estrutura de matricula para enviar, a 
    # estrura de matricula foi gerada no protobuf com 
    # o nome Matricula. Neste caso não enviaremos todas 
    # as informações de matricula pois não é necessario.
    # Precisamos somente do CÓDIGO DA DISCIPLINA, ANO e 
    # SEMESTRE.
    print("\nCLIENTE:\n")
    matricula = noteManagement_pb2.Matricula()
    matricula.cod_disciplina = getCodDisciplina()
    matricula.ano = getAno()
    matricula.semestre = getSemestre()
    msg = matricula.SerializeToString()
    size = len(msg)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebe uma resposta do servidor para verificar se ouve algum problema.
    reply = noteManagement_pb2.MsgReply()
    reply.ParseFromString(client_socket.recv(1024))
    if reply.message == "OK" :
        # Recebe do servidor o número alunos encotrdos na consulta
        # isso será usado logo abaixo no laço de repetição. Pois os
        # alunos serçao enviados um a um. 
        numAl = noteManagement_pb2.NumChair()
        numAl.ParseFromString(client_socket.recv(1024))

        # Recebe do servidor os alunos um por um
        print("\nSERVIDOR:\n")
        for i in range(numAl.numChair):
            # Mota a estrutura do protobuf para receber 
            # um tipo aluno.
            aluno = noteManagement_pb2.Aluno()
            # Recebe uma string com os dados do 
            # aluno e decodifica ela.
            aluno.ParseFromString(client_socket.recv(1024))
            # Mostra os dados de aluno recebido.
            print(f'--\nRA: {aluno.RA}')
            print(f'Nome: {aluno.nome}')
            print(f'Periodo: {aluno.periodo}\n--')

        # Recebe a resposta do servidor. Se a ação der certo ele 
        # retorna que a ação funcionou caso contrario 
        # retorna o erro encotrado.
        reply = noteManagement_pb2.MsgReply()
        reply.ParseFromString(client_socket.recv(1024))
        print(reply.message)
    else:
        print("\nSERVIDOR:\n" + reply.message)

# Função responsavel por listar alunos de uma disciplina 
# informado a disciplina, ano e semestre.
def getAlunoInfo():
    # Monta e envia o tipo da mensagem que é um número de 1 a 5
    # representando a ação requerida, neste caso é o tipo 4
    # O tipo de mensagem também esta definido no protobuf
    # com o nome TypeMessage.
    type = noteManagement_pb2.TypeMessage()
    type.type = 5
    msgType = type.SerializeToString()
    size = len(msgType)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msgType)

    # Monta a estrutura de matricula para enviar, a 
    # estrura de matricula foi gerada no protobuf com 
    # o nome Matricula. Neste caso não enviaremos todas 
    # as informações de matricula pois não é necessario.
    # Precisamos somente do CÓDIGO DA DISCIPLINA, ANO e 
    # SEMESTRE.
    print("\nCLIENTE:\n")
    matricula = noteManagement_pb2.Matricula()
    matricula.RA = getRA()
    matricula.ano = getAno()
    matricula.semestre = getSemestre()
    msg = matricula.SerializeToString()
    size = len(msg)
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebe uma resposta do servidor para verificar se ouve algum problema.
    reply = noteManagement_pb2.MsgReply()
    reply.ParseFromString(client_socket.recv(1024))
    if reply.message == "OK":
        # Recebe do servidor os alunos um por um
        print("\nSERVIDOR:\n")
        # Recebe do servidor o aluno do qual desejasse ver o boletim
        resAluno = noteManagement_pb2.Aluno()
        resAluno.ParseFromString(client_socket.recv(1024))
        # Mostra os dados de aluno recebido.
        print('\nAluno:')
        print(f'RA: {resAluno.RA}')
        print(f'Nome: {resAluno.nome}')
        # Recebe do servidor o número alunos encotrados na consulta
        # isso será usado logo abaixo no laço de repetição. Pois os
        # alunos serçao enviados um a um. 
        numAl = noteManagement_pb2.NumChair()
        numAl.ParseFromString(client_socket.recv(1024))

        for i in range(numAl.numChair):
            # Monta a estrutura do protobuf para receber 
            # um tipo matricula.
            matricula = noteManagement_pb2.Matricula()
            # Recebe uma string com os dados d 
            # matricula e decodifica ela.
            matricula.ParseFromString(client_socket.recv(1024))
            # Monta a estrutura do protobuf para receber 
            # um tipo disciplina.
            disciplina = noteManagement_pb2.Disciplina()
            # Recebe uma string com os dados d 
            # disciplina e decodifica ela.
            disciplina.ParseFromString(client_socket.recv(1024))
            # Mostra os dados recebidos.
            print(f'--\nNome da Disciplina: {disciplina.nome}')
            print(f'Nota: {matricula.nota}')
            print(f'Faltas: {matricula.faltas}\n--')

        # Recebe a resposta do servidor. Se a ação der certo ele 
        # retorna que a ação funcionou caso contrario 
        # retorna o erro encotrado.
        reply = noteManagement_pb2.MsgReply()
        reply.ParseFromString(client_socket.recv(1024))
        print(reply.message)
    else:
        print("\nSERVIDOR:\n" + reply.message)

if __name__ == '__main__':
    # Cria o socket TCP/IP
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Conecta o socket a porta onde o servidor rodando
    client_socket.connect(("localhost", 7000))
    while True:
        # Fornece os tipos de requisição para o cliente escolher
        request_type = int(input('\nDeseja fazer uma Inserção (1), Atualização (2), Consulta (3) ou Sair (0): '))
        print(request_type)
        while request_type < 0 or request_type > 3:
            print("Comando incorreto. Selecione um comando válido.\n")
            request_type = int(input('\nDeseja fazer uma Inserção (1), Atualização (2), Consulta (3) ou Sair (0): '))
        
        if request_type == 0:
            client_socket.close()
            break
        elif request_type == 1:
            createMatricula()
        elif request_type == 2:
            request_type_insert = int(input('\nDeseja fazer uma Atualização de nota (1), faltas (2) ou Sair (0): '))
            while request_type_insert < 0 or request_type_insert > 2:
                print("Comando incorreto. Selecione um comando válido.\n")
                request_type_insert = int(input('\nDeseja fazer uma Atualização de nota (1), faltas (2) ou Sair (0): '))
            if request_type_insert == 1:
                updateNotas()
            elif request_type_insert == 2:
                updateFaltas()
        elif request_type == 3:
            request_type_get = int(input('\nDeseja fazer a Consulta de alunos (1), Boletim de um aluno (2) ou Sair (0): '))
            while request_type_get < 0 or request_type_get > 2:
                print("Comando incorreto. Selecione um comando válido.\n")
                request_type_get = int(input('\nDeseja fazer a Consulta de alunos (1), Boletim de um aluno (2) ou Sair (0): '))
            if request_type_get == 1:
                getAlunos()
            elif request_type_get == 2:
                getAlunoInfo()    