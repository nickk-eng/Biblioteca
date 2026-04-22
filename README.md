# Sistema de Controle de Biblioteca UMC

## Observação importante sobre a evolução do projeto

Na primeira versão deste trabalho, eu havia desenvolvido um projeto muito simples, que não atendia de forma adequada aos requisitos da atividade. Ao revisar o enunciado, os diagramas e as funcionalidades esperadas, percebi que a modelagem anterior estava incompleta e não representava corretamente o fluxo real do sistema de biblioteca.

Por esse motivo, foi necessário **revisar os diagramas de classes, sequência e atividades**, ajustar a análise do sistema e **praticamente refazer o projeto do zero**, mantendo uma estrutura mais fiel ao que foi solicitado na disciplina. Essa reconstrução permitiu alinhar o sistema com o padrão MVC, com o uso de Spring Boot, Thymeleaf, MongoDB e regras de negócio mais consistentes para empréstimos, reservas, devoluções, multas e perfis de usuário.

Este repositório, portanto, representa a **versão corrigida e adequada** do projeto, já compatível com os requisitos da atividade.

---

## Descrição do projeto

Este projeto é uma aplicação web Java desenvolvida para o controle de uma biblioteca universitária da UMC. O sistema permite o gerenciamento de livros, usuários, empréstimos, devoluções, reservas e multas, com autenticação por perfil e persistência em banco de dados MongoDB Atlas.

A aplicação foi construída com arquitetura baseada em **MVC (Model-View-Controller)**, utilizando **Spring Boot**, **Thymeleaf**, **Spring Security** e **MongoDB**.

---

## Tecnologias utilizadas

- Java 17+
- Maven
- Spring Boot 3.x
- Spring Web
- Spring Data MongoDB
- Spring Security
- Thymeleaf
- Bootstrap 5
- MongoDB Atlas
- NetBeans

---

## Funcionalidades do sistema

### Funcionário/Admin
- Login no sistema
- Cadastro, edição e exclusão de livros
- Cadastro, edição e exclusão de usuários
- Registro de empréstimos
- Registro de devoluções
- Controle de estoque
- Controle de reservas
- Visualização de empréstimos e reservas
- Cálculo automático de multa por atraso

### Aluno/Professor
- Cadastro no sistema
- Login no sistema
- Visualização de livros emprestados
- Visualização de reservas
- Visualização de histórico
- Acompanhamento de empréstimos e reservas já finalizados

---

## Regras de negócio implementadas

- O empréstimo só pode ser realizado se o livro possuir estoque disponível.
- Ao realizar um empréstimo, o estoque do livro é reduzido em 1.
- Ao devolver um livro, o estoque é aumentado em 1.
- A multa é calculada em **R$ 2,00 por dia de atraso**.
- A reserva só pode ser feita quando o livro estiver sem estoque.
- O mesmo usuário não pode manter duas reservas ativas para o mesmo livro.
- Quando um livro devolvido volta ao estoque, a reserva ativa mais antiga pode ser atendida automaticamente.
- O perfil **FUNCIONARIO** possui acesso administrativo.
- Os perfis **ALUNO** e **PROFESSOR** possuem acesso apenas às próprias informações.

---

## Estrutura do projeto

```
Biblioteca/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/umc/biblioteca/
│   │   │   ├── BibliotecaApplication.java
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
```
Classes principais
Model
Usuario
Livro
Emprestimo
Reserva
Controller
CadastroController
LoginController
LivroController
UsuarioController
EmprestimoController
ReservaController
HistoricoController
Service
UsuarioService
LivroService
EmprestimoService
ReservaService
Repository
UsuarioRepository
LivroRepository
EmprestimoRepository
ReservaRepository
Como executar o projeto
Pré-requisitos

É necessário ter instalado no computador:

JDK 17 ou superior
Maven
NetBeans ou outra IDE Java
Conta no MongoDB Atlas
Configuração do banco de dados

No arquivo src/main/resources/application.properties, configure a URI do MongoDB Atlas:

spring.data.mongodb.uri=mongodb+srv://USUARIO:SENHA@CLUSTER/nomedb
spring.thymeleaf.cache=false
server.port=8080

Substitua:

USUARIO pelo usuário do banco
SENHA pela senha do banco
CLUSTER pelo nome do cluster
nomedb pelo nome do banco de dados
Como rodar no NetBeans
Abra o NetBeans.
Clique em File > Open Project.
Selecione a pasta do projeto.
Aguarde o Maven baixar as dependências.
Verifique se o application.properties está configurado corretamente.
Execute o projeto pelo botão Run.
Acesse no navegador:
http://localhost:8080
Login padrão

O sistema cria automaticamente um usuário administrador padrão:

Login: admin
Senha: admin123
Perfis de acesso
FUNCIONARIO

Pode acessar:

painel administrativo
livros
usuários
empréstimos
reservas
ALUNO / PROFESSOR

Pode acessar:

livros emprestados
minhas reservas
histórico

Não pode acessar diretamente as páginas administrativas.

Telas principais
Login
Cadastro
Painel administrativo
Gestão de livros
Gestão de usuários
Gestão de empréstimos
Gestão de reservas
Meus empréstimos
Minhas reservas
Histórico
Acesso negado
Diagramas do projeto

Este projeto foi remodelado com base em diagramas atualizados para refletir melhor os requisitos da atividade:

Diagrama de Classes
Diagrama de Sequência
Diagrama de Atividades

Esses diagramas foram ajustados após a revisão da análise inicial do sistema.

Objetivo acadêmico

Este projeto foi desenvolvido com finalidade acadêmica para demonstrar:

modelagem de sistema orientado a objetos
uso de UML
aplicação do padrão MVC
integração entre aplicação web e banco de dados
implementação de regras de negócio em Java com Spring Boot
Autor

Projeto desenvolvido para a disciplina de desenvolvimento de sistemas / programação orientada a objetos aplicada à web.

Observação final

Esta versão representa a implementação final revisada do sistema, já com correções estruturais, melhoria da modelagem e adequação aos critérios exigidos na atividade.
