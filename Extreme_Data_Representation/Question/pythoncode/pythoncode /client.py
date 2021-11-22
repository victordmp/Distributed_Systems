import socket;
import addressbook_pb2;

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 7000))

# instanciar e preencher a estrutura
person = addressbook_pb2.Person()
person.id = 234
person.name = "Rodrigo_Campiolo"
person.email = "rcampilo@ibest.com.br"

print(person)

# marshalling
msg = person.SerializeToString()
print(msg)
size = len(msg)

client_socket.send((str(size) + "\n").encode())
client_socket.send(msg)

client_socket.close()