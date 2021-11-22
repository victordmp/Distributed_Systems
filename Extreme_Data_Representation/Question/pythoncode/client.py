import socket
import noteManagement_pb2;
import time

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Conecta o socket a porta onde o servidor está escutando
server_address = ['localhost', 7000]
# server_address[0] = input('IP adress: ')
# server_address[1] = int(input('Port: '))
client_socket.connect(tuple(server_address))

# # instanciar e preencher a estrutura
# type = noteManagement_pb2.TypeMessage()
# type.type = 1

# matricula = noteManagement_pb2.Matricula()
# matricula.RA = 13
# matricula.cod_disciplina = "LM31A"
# matricula.ano = 2020
# matricula.semestre = 2
# matricula.nota = 0
# matricula.faltas = 0

# print(type)
# print(matricula)

# # marshalling
# msgType = type.SerializeToString()
# size = len(msgType)

# client_socket.send((str(size) + "\n").encode())
# client_socket.send(msgType)

# msg = matricula.SerializeToString()
# size = len(msg)

# client_socket.send((str(size) + "\n").encode())
# client_socket.send(msg)

# reply = noteManagement_pb2.MsgReply()
# reply.ParseFromString(client_socket.recv(1024))
# print("\nSERVIDOR:\n" + reply.message)

# # instanciar e preencher a estrutura
# type = noteManagement_pb2.TypeMessage()
# type.type = 2

# matricula = noteManagement_pb2.Matricula()
# matricula.RA = 13
# matricula.cod_disciplina = "BCC36"
# matricula.nota = 10.0
# matricula.faltas = 6

# print(type)
# print(matricula)

# # marshalling
# msgType = type.SerializeToString()
# size = len(msgType)

# client_socket.send((str(size) + "\n").encode())
# client_socket.send(msgType)

# msg = matricula.SerializeToString()
# size = len(msg)

# client_socket.send((str(size) + "\n").encode())
# client_socket.send(msg)

# reply = noteManagement_pb2.MsgReply()
# reply.ParseFromString(client_socket.recv(1024))
# print("\nSERVIDOR:\n" + reply.message)

# client_socket.close()

# instanciar e preencher a estrutura
type = noteManagement_pb2.TypeMessage()
type.type = 4

matricula = noteManagement_pb2.Matricula()
matricula.cod_disciplina = "BCC36B"
matricula.ano = 2020
matricula.semestre = 2

print(type)
print(matricula)

# marshalling
msgType = type.SerializeToString()
size = len(msgType)

client_socket.send((str(size) + "\n").encode())
client_socket.send(msgType)

msg = matricula.SerializeToString()
size = len(msg)

client_socket.send((str(size) + "\n").encode())
client_socket.send(msg)

numAl = 5

print(numAl)
print("\nSERVIDOR:\n")
for i in range(numAl):
    aluno = noteManagement_pb2.Aluno()
    aluno.ParseFromString(client_socket.recv(1024))
    print(f'--\nRA: {aluno.RA}')
    print(f'Nome: {aluno.nome}')
    print(f'Periodo: {aluno.periodo}\n--')

reply = noteManagement_pb2.MsgReply()
reply.ParseFromString(client_socket.recv(1024))
print(reply.message)

client_socket.close()