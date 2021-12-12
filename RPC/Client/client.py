# /*
#     Descrição:
#         É o cliente que envia requisições para o servidor usando o qRPC 
#         e usando o protobuffer para representação extrema de dados. 
#         O banco de dados usado e sqlite. Basicamente é aqui onde o cliente
#         ira escolher o tipo da requisição, vai enviar para o cliente e então
#         mpstrar a resposta. 

#     Autores: 
#         Victor Daniel Manfrini Pires 
#         Emica Costa

#     Data de criação: 05/12/2021
#     Data de atualização: 05/12/2021
# */

import grpc

import noteManagement_pb2
import noteManagement_pb2_grpc

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
def createMatricula(stub):
  # Cria uma mensagem de Request, que contem os dados da matricula 
  # mais o tipo da requisição que neste caso é ADDMATRICULA(Que é um 
  # enum referente a requisição de criar uma nova matricula).
  print("\nCLIENTE:\n")
  request = noteManagement_pb2.Request()
  request.option = noteManagement_pb2.Request().TypeReq.ADDMATRICULA
  request.matricula.RA = getRA()
  request.matricula.cod_disciplina = getCodDisciplina()
  request.matricula.ano = getAno()
  request.matricula.semestre = getSemestre()

  # Envia a requisição para o servidor e aguarada a resposta
  response = stub.Comunication(request)

  # Mostra a resposta do servidor
  print("\n\nSERVIDOR:\n\n" + response.message)

# Função responsavel por atualizar a nota
def updateNota(stub):
  # Cria uma mensagem de Request, que contem os dados da matricula 
  # mais o tipo da requisição que neste caso é PUTNOTA(Que é um 
  # enum referente a requisição de ataluizar nota de uma matricula).
  print("\nCLIENTE:\n")
  request = noteManagement_pb2.Request()
  request.option = noteManagement_pb2.Request().TypeReq.PUTNOTA
  request.matricula.RA = getRA()
  request.matricula.cod_disciplina = getCodDisciplina()
  request.matricula.ano = getAno()
  request.matricula.semestre = getSemestre()
  request.matricula.nota = getNota()

  # Envia a requisição para o servidor e aguarada a resposta
  response = stub.Comunication(request)

  # Mostra a resposta do servidor
  print("\n\nSERVIDOR:\n\n" + response.message)

# Função responsavel por atualizar a Faltas
def updateFaltas(stub):
  # Cria uma mensagem de Request, que contem os dados da matricula 
  # mais o tipo da requisição que neste caso é PUTFALTAS(Que é um 
  # enum referente a requisição de ataluizar faltas de uma matricula).
  print("\nCLIENTE:\n")
  request = noteManagement_pb2.Request()
  request.option = noteManagement_pb2.Request().TypeReq.PUTFALTAS
  request.matricula.RA = getRA()
  request.matricula.cod_disciplina = getCodDisciplina()
  request.matricula.ano = getAno()
  request.matricula.semestre = getSemestre()
  request.matricula.faltas = getFaltas()

  # Envia a requisição para o servidor e aguarada a resposta
  response = stub.Comunication(request)

  # Mostra a resposta do servidor
  print("\n\nSERVIDOR:\n\n" + response.message)

# Função responsavel por listar alunos de uma disciplina 
# informado a disciplina, ano e semestre.
def getAlunos(stub):
  # Cria uma mensagem de Request, que contem os dados da matricula 
  # mais o tipo da requisição que neste caso é GETALUNOS(Que é um 
  # enum referente a requisição de pegar alunos de uma disciplina).
  print("\nCLIENTE:\n")
  request = noteManagement_pb2.Request()
  request.option = noteManagement_pb2.Request().TypeReq.GETALUNOS
  request.matricula.cod_disciplina = getCodDisciplina()
  request.matricula.ano = getAno()
  request.matricula.semestre = getSemestre()

  # Envia a requisição para o servidor e aguarada a resposta
  response = stub.Comunication(request)

  # Mostra a resposta do servidor
  print("\n\nSERVIDOR:\n\n" + response.message)

  # Mostra um a um da lista de alunos recebidos do servidor
  for aluno in response.student:
    print(f'--\nRA: {aluno.RA}')
    print(f'Nome: {aluno.nome}')
    print(f'Periodo: {aluno.periodo}\n--')

# Função responsavel por listar disciplinas, faltas e notas 
# (RA, nome, nota, faltas) de um aluno informado o ano e semestre.
def getBoletimAluno(stub):
  # Cria uma mensagem de Request, que contem os dados da matricula 
  # mais o tipo da requisição que neste caso é GETALUNOS(Que é um 
  # enum referente a requisição de pegar boletim do aluno).
  print("\nCLIENTE:\n")
  request = noteManagement_pb2.Request()
  request.option = noteManagement_pb2.Request().TypeReq.GETBOLETIM
  request.matricula.RA = getRA()
  request.matricula.ano = getAno()
  request.matricula.semestre = getSemestre()

  # Envia a requisição para o servidor e aguarda a resposta
  response = stub.Comunication(request)

  # Mostra a resposta do servidor
  print("\n\nSERVIDOR:\n\n" + response.message)

  print("\nALUNO:\n")

  # Mostra os ddados do aluno
  for aluno in response.student:
    print(f'--\nRA: {aluno.RA}')
    print(f'Nome: {aluno.nome}')
  
  print("\n--------BOLETIM:---------\n")
  # Mostra um a um da lista de launos recebidos do servidor
  for disciplina in response.report_card:
    print(f'--\nNome da Disciplina: {disciplina.nome_disc}')
    print(f'Nota: {disciplina.nota}')
    print(f'Faltas: {disciplina.faltas}\n--')
  print("\n--------------------------\n")


def client():
  #configura o canal de comunicacao
  channel = grpc.insecure_channel('localhost:7777')
  
  #inicializa e configura o stub
  stub = noteManagement_pb2_grpc.ConnectionMiddlewareStub(channel)

  #Oferece os métodos de requisição para o cliente escolher.
  while True:
      # Fornece os tipos de requisição para o cliente escolher
      request_type = int(input('\nDeseja fazer uma Inserção (1), Atualização (2), Consulta (3) ou Sair (0): '))
      while request_type < 0 or request_type > 3:
          print("Comando incorreto. Selecione um comando válido.\n")
          request_type = int(input('\nDeseja fazer uma Inserção (1), Atualização (2), Consulta (3) ou Sair (0): '))
      if request_type == 0:
          break
      elif request_type == 1:
          createMatricula(stub)
      elif request_type == 2:
          request_type_insert = int(input('\nDeseja fazer uma Atualização de nota (1), faltas (2) ou Sair (0): '))
          while request_type_insert < 0 or request_type_insert > 2:
              print("Comando incorreto. Selecione um comando válido.\n")
              request_type_insert = int(input('\nDeseja fazer uma Atualização de nota (1), faltas (2) ou Sair (0): '))
          if request_type_insert == 1:
              updateNota(stub)
          elif request_type_insert == 2:
              updateFaltas(stub)
      elif request_type == 3:
          request_type_get = int(input('\nDeseja fazer a Consulta de alunos (1), Boletim de um aluno (2) ou Sair (0): '))
          while request_type_get < 0 or request_type_get > 2:
              print("Comando incorreto. Selecione um comando válido.\n")
              request_type_get = int(input('\nDeseja fazer a Consulta de alunos (1), Boletim de um aluno (2) ou Sair (0): '))
          if request_type_get == 1:
              getAlunos(stub)
          elif request_type_get == 2:
              getBoletimAluno(stub)    

if __name__ == '__main__':
  client()