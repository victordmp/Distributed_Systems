/*
    Descrição:
        Esse é o código resposansavel por toda a parte de lógica de consultas no 
        banco e etc.além de montar a resposta e enviar para o cliente.
        Basicamente existem um metodo implementado para cada tipo de requisição 
        aceita pelo servidor. Todos os metodos recebem request(contem os dados da 
        requisição enviada pelo cliente), response(mesagem de tipo Reply criada 
        anterior que ainda precisa ser setada antes de enviar de volta para o 
        cliente) e connection(Que representa comunicação com o baco de dados SQLite).
        Essa classe Controller implementa todos métodos fornecido pelo servidor.

    Autores: 
        Victor Daniel Manfrini Pires 
        Emica Costa

    Data de criação: 5/12/2021
    Data de atualização: 5/12/2021
*/

import java.sql.*;

// O Controller implementa todos metodos fornecido pelo servidor.
// Todos os metodos recebem request(contem os dados da requisição enviada pelo cliente), 
// response(mesagem de tipo Reply criada anterior que ainda precisa 
// ser setada antes de enviar de volta para o cliente) e connection(Que 
// representa comunicação com o baco de dados SQLite).
public class Controller {
    // Método responsavel por criar uma nova matricula.
    public static void add_matricula(Request request, Reply.Builder response, Connection connection) {
        // Dados recebidos na requisição, como serão usados varias vezes.
        // Para facilitar a vizualização, crimaos e inicializamos cada dado aqui.
        // Por ser inserção de nota por padrão a nota e as faltas ficarão zeradas.
        int RA = request.getMatricula().getRA();
        String codDisciplina = request.getMatricula().getCodDisciplina();
        int ano = request.getMatricula().getAno();
        int semestre = request.getMatricula().getSemestre();
        double nota = 0.0; 
        int faltas = 0;

        // Querys
        String select_all_matriculas = "select * from Matricula";
        String select_aluno = "select * from Aluno where RA = " + RA;
        String select_disciplina = "select * from Disciplina where codigo = '" + codDisciplina + "'";
        String insert_matricula = "INSERT INTO Matricula(RA, cod_disciplina, ano, semestre, nota, faltas) VALUES (" + RA + ", '" 
            + codDisciplina + "', " + ano + ", " + semestre + ", " + nota + ", " + faltas + ")";
        
        try{
            Statement statement = connection.createStatement();

            // Faz uma consulta na tabela de Aluno verificando se o RA existe.
            ResultSet resultSet = statement.executeQuery(select_aluno);
            // Verifica se encontrou o RA na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que o RA não foi encontrado. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("RA not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
            resultSet = statement.executeQuery(select_disciplina);
            // Verifica se encontrou o Codigo da displina na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("Discipline not found. Request failed!");
                return;
            }

            // Inserindo um regitro de Matricula
            statement.execute(insert_matricula);

            // Faz um select all na tabela de matricula
            resultSet = statement.executeQuery(select_all_matriculas);

            // Printa todas as matriculas para vizualização.
            // A ultima Matricula deve ser a que acabamos de inserir. 
            System.out.println("\n----RESULT QUERY MATRICULAS-----\n");
            while (resultSet.next()) {
                System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                    + " - " + resultSet.getString("cod_disciplina") 
                                    + " - " + Integer.toString(resultSet.getInt("ano")) 
                                    + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                    + " - " + resultSet.getFloat("nota") 
                                    + " - " + Integer.toString(resultSet.getInt("faltas")));
            }
            System.out.println("\n-------------------------\n");
            // Envia uma resposta dizendo que a requisição foi realizada com sucesso
            response.setMessage("Request completed successfully!");
        } catch (SQLException e) {
            response.setMessage ("Request failed! " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    // Método reponsavel por atualizar a nota de uma matricula
    public static void updateNota(Request request, Reply.Builder response, Connection connection) {
        // Dados recebidos na requisição, como serão usados varias vezes.
        // Para facilitar a vizualização, criamos e inicializamos cada dado aqui.
        // Por ser um update de nota, não precisamos de todas os dados de matricula.
        int RA = request.getMatricula().getRA();
        String codDisciplina = request.getMatricula().getCodDisciplina();
        int ano = request.getMatricula().getAno();
        int semestre = request.getMatricula().getSemestre();
        double nota = request.getMatricula().getNota(); 

        // Querys
        String select_all_matriculas = "select * from Matricula";
        String select_aluno = "select * from Aluno where RA = " + RA;
        String select_disciplina = "select * from Disciplina where codigo = '" + codDisciplina + "'";
        String select_matricula = "select * from Matricula where RA = " + RA + " and cod_disciplina = '" + codDisciplina 
            + "' and ano = " + ano + " and semestre = " + semestre;
        String update_nota_matricula = "update Matricula set nota = " + nota + " where RA = " + RA 
            + " and cod_disciplina = '" + codDisciplina + "' and  semestre = " + semestre + " and ano = " + ano;

        try{
            Statement statement = connection.createStatement();

            // Faz uma consulta na tabela de Aluno verificando se o RA existe.
            ResultSet resultSet = statement.executeQuery(select_aluno);
            // Verifica se encontrou o RA na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que o RA não foi encontrado. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("RA not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
            resultSet = statement.executeQuery(select_disciplina);
            // Verifica se encontrou o Codigo da displina na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("Discipline not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
            // com o código da disciplina, RA, ano e semestre passados pelo cliente.
            resultSet = statement.executeQuery(select_matricula);
            // Verifica se encontrou algum resultado na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("No registration with these data found!");
                return;
            }

            // Atualiza a nota de uma Matricula
            statement.execute(update_nota_matricula);

            // Faz um select all na tabela de matricula
            resultSet = statement.executeQuery(select_all_matriculas);

            // Printa todas as matriculas para vizualização.
            // A ultima Matricula deve ser a que acabamos de atualizar. 
            System.out.println("\n----RESULT QUERY MATRICULAS-----\n");
            while (resultSet.next()) {
                System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                    + " - " + resultSet.getString("cod_disciplina") 
                                    + " - " + Integer.toString(resultSet.getInt("ano")) 
                                    + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                    + " - " + resultSet.getFloat("nota") 
                                    + " - " + Integer.toString(resultSet.getInt("faltas")));
            }
            System.out.println("\n-------------------------\n");
            // Envia uma resposta dizendo que a requisição foi realizada com sucesso.
            response.setMessage("Request completed successfully!");
        } catch (SQLException e) {
            response.setMessage ("Request failed! " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    // Método reponsavel por atualizar as faltas de uma matricula
    public static void updateFaltas(Request request, Reply.Builder response, Connection connection) {
        // Dados recebidos na requisição, como serão usados varias vezes.
        // Para facilitar a vizualização, criamos e inicializamos cada dado aqui.
        // Por ser um update de faltas, não precisamos de todas os dados de matricula.
        int RA = request.getMatricula().getRA();
        String codDisciplina = request.getMatricula().getCodDisciplina();
        int ano = request.getMatricula().getAno();
        int semestre = request.getMatricula().getSemestre();
        int faltas = request.getMatricula().getFaltas(); 

        // Querys
        String select_all_matriculas = "select * from Matricula";
        String select_aluno = "select * from Aluno where RA = " + RA;
        String select_disciplina = "select * from Disciplina where codigo = '" + codDisciplina + "'";
        String select_matricula = "select * from Matricula where RA = " + RA + " and cod_disciplina = '" + codDisciplina 
            + "' and ano = " + ano + " and semestre = " + semestre;
        String update_faltas_matricula = "update Matricula set faltas = " + faltas + " where RA = " + RA 
            + " and cod_disciplina = '" + codDisciplina + "' and  semestre = " + semestre + " and ano = " + ano;

        try{
            Statement statement = connection.createStatement();

            // Faz uma consulta na tabela de Aluno verificando se o RA existe.
            ResultSet resultSet = statement.executeQuery(select_aluno);
            // Verifica se encontrou o RA na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que o RA não foi encontrado. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("RA not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
            resultSet = statement.executeQuery(select_disciplina);
            // Verifica se encontrou o Codigo da displina na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("Discipline not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
            // com o código da disciplina, RA, ano e semestre passados pelo cliente.
            resultSet = statement.executeQuery(select_matricula);
            // Verifica se encontrou algum resultado na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("No registration with these data found!");
                return;
            }

            // Atualiza as faltas de uma Matricula
            statement.execute(update_faltas_matricula);

            // Faz um select all na tabela de matricula
            resultSet = statement.executeQuery(select_all_matriculas);

            // Printa todas as matriculas para vizualização.
            // A ultima Matricula deve ser a que acabamos de atualizar. 
            System.out.println("\n----RESULT QUERY MATRICULAS-----\n");
            while (resultSet.next()) {
                System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                    + " - " + resultSet.getString("cod_disciplina") 
                                    + " - " + Integer.toString(resultSet.getInt("ano")) 
                                    + " - " + Integer.toString(resultSet.getInt("semestre")) 
                                    + " - " + resultSet.getFloat("nota") 
                                    + " - " + Integer.toString(resultSet.getInt("faltas")));
            }
            System.out.println("\n-------------------------\n");
            // Envia uma resposta dizendo que a requisição foi realizada com sucesso.
            response.setMessage("Request completed successfully!");
        } catch (SQLException e) {
            response.setMessage ("Request failed! " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    // Método responsavel por retornar a uma Listagem de alunos (RA, nome, período) 
    // de uma disciplina informado a disciplina, ano e semestre.
    public static void getAlunos(Request request, Reply.Builder response, Connection connection) {
        // Dados recebidos na requisição, como serão usados varias vezes.
        // Para facilitar a vizualização, crimaos e inicializamos cada dado aqui.
        String codDisciplina = request.getMatricula().getCodDisciplina();
        int ano = request.getMatricula().getAno();
        int semestre = request.getMatricula().getSemestre();
        
        // Querys
        String select_disciplina = "select * from Disciplina where codigo = '" + codDisciplina + "'";
        String select_matricula = "select * from Matricula where cod_disciplina = '" + codDisciplina 
            + "' and ano = " + ano + " and semestre = " + semestre;
        String select_alunos_sem_ano_disc = "select RA, nome, periodo from Aluno where RA in (select RA from Matricula where cod_disciplina = '" + codDisciplina 
            + "' and ano = " + ano + " and semestre = " + semestre + ")";

        try {
            Statement statement = connection.createStatement();

            // Faz uma consulta na tabela de disciplana verificando se o codigo da displina existe.
            ResultSet resultSet = statement.executeQuery(select_disciplina);
            // Verifica se encontrou o Codigo da displina na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("Discipline not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
            // com o código da disciplina, ano e semestre passados pelo cliente.
            resultSet = statement.executeQuery(select_matricula);
            // Verifica se encontrou algum resultado na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("No registration with these data found! No students found!");
                return;
            }

            // Faz uma consulta para pegar os alunos (RA, nome, período) de 
            // uma disciplina informado a disciplina, ano e semestre.
            resultSet = statement.executeQuery(select_alunos_sem_ano_disc);

            // Monta e envia a respota da query e também printa na tela
            System.out.println("\n----RESULT QUERY-----\n");
            while (resultSet.next()) {
                // Cria aluno
                Aluno.Builder aluno = Aluno.newBuilder();

                // Seta dados em alunos
                aluno.setRA(resultSet.getInt("RA"));
                aluno.setNome(resultSet.getString("nome"));
                aluno.setPeriodo(resultSet.getInt("periodo"));

                // Printa os dados do aluno
                System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                    + " - " + resultSet.getString("nome") 
                                    + " - " + Integer.toString(resultSet.getInt("periodo")));

                // Adiciona o aluno no response
                response.addStudent(aluno);
            }
            System.out.println("\n-------------------------\n");
            // Envia uma resposta dizendo que a requisição foi realizada com sucesso.
            response.setMessage("Request completed successfully!");
        } catch (SQLException e) {
            response.setMessage ("Request failed! " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    // Método resposavel por retornar uma Listagem de disciplinas, faltas e notas 
    // (RA, nome, nota, faltas) de um aluno informado o ano e semestre.
    public static void getBoletim(Request request, Reply.Builder response, Connection connection) {
        // Dados recebidos na requisição, como serão usados varias vezes.
        // Para facilitar a vizualização, crimaos e inicializamos cada dado aqui.
        int RA = request.getMatricula().getRA();
        int ano = request.getMatricula().getAno();
        int semestre = request.getMatricula().getSemestre();
        
        // Querys
        String select_aluno = "select * from Aluno where RA = " + RA;
        String select_matricula = "select * from Matricula where RA = '" + RA + "' and ano = " + ano + " and semestre = " + semestre;
        String select_boletim_aluno = "select *, D.nome As nome_dis from Matricula M, Aluno A, Disciplina D where M.RA = " + RA 
            + " and M.ano = " + ano + " and M.semestre = " + semestre + " and A.RA = M.RA and D.codigo = M.cod_disciplina";

        try {
            Statement statement = connection.createStatement();

            // Faz uma consulta na tabela de Aluno verificando se o RA existe.
            ResultSet resultSet = statement.executeQuery(select_aluno);
            // Verifica se encontrou o RA na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que o RA não foi encontrado. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("RA not found. Request failed!");
                return;
            }

            // Faz uma consulta na tabela de Matricula verificando se existe algum registro 
            // com o código da disciplina, ano e semestre passados pelo cliente.
            resultSet = statement.executeQuery(select_matricula);
            // Verifica se encontrou algum resultado na consulta. Caso não tenha encontrado, retorna 
            // um erro dizendo que a disciplina não foi encontrada. Caso tenha encontrado, continua 
            // a execução normal.
            if (!resultSet.isBeforeFirst()) {
                response.setMessage("No registration with these data found! No students found!");
                return;
            }

            // Faz uma consulta para pegar os alunos (RA, nome, período) de 
            // uma disciplina informado a disciplina, ano e semestre.
            resultSet = statement.executeQuery(select_boletim_aluno);

            // Monta e envia a respota da query e também printa na tela
            System.out.println("\n----RESULT QUERY-----\n");

            // Varivel auxiliar que vai ajudar a saber em qual linha da respota estamos
            int qtd = 0;
            while (resultSet.next()) {
                // Verifica se estamos na prmeira linha da resposta.
                // Se sim, cria uma mensagem do tipo aluno e adiciona ao response.
                // So criremos um registro do tipo aluno, pois esse dado nunca muda
                // pois é uma consulta em cima de um aluno especifico. Portanto só precisamos 
                // passar esse dado uma vez.
                if (qtd == 0) {
                    // Cria um aluno
                    Aluno.Builder aluno = Aluno.newBuilder();

                    // Seta os dados do aluno
                    aluno.setRA(resultSet.getInt("RA"));
                    aluno.setNome(resultSet.getString("nome"));

                    // Printa o aluno
                    System.out.println("" + Integer.toString(resultSet.getInt("RA")) 
                                        + " - " + resultSet.getString("nome"));

                    // Adiciona o aluno no response
                    response.addStudent(aluno);
                }

                // Cria mensagem do tipo DiscBoletimAluno contendo nome da disciplina, nota e faltas 
                DiscBoletimAluno.Builder register = DiscBoletimAluno.newBuilder();

                // Seta dados ao register
                register.setNomeDisc(resultSet.getString("nome_dis"));
                register.setNota(resultSet.getFloat("nota"));
                register.setFaltas(resultSet.getInt("faltas"));

                // Printa os dados
                System.out.println("" + resultSet.getString("nome_dis")
                                    + " - " + resultSet.getFloat("nota")
                                    + " - " + Integer.toString(resultSet.getInt("faltas")));
                // Adiciona o register no response
                response.addReportCard(register);

                // Incrementa a varivel auxiliar
                qtd++;
            }
            System.out.println("\n-------------------------\n");
            // Envia uma resposta dizendo que a requisição foi realizada com sucesso.
            response.setMessage("Request completed successfully!");
        } catch (SQLException e) {
            response.setMessage ("Request failed! " + e.getMessage());
            System.out.println(e.getMessage());
        }  
    }
}