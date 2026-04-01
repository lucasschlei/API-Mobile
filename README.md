API-Mobile | Sistema Mercado Dany

Integrantes:
Catarina Klein,
Lucas Schlei,
Murilo Kerschbaum

Sobre o Projeto:

O API-Mobile é o aplicativo Android do Sistema Mercado Dany, desenvolvido em Kotlin, responsável pela interação do usuário com o sistema.

O projeto surgiu a partir de uma discussão descontraída entre Murilo e o docente Vinícius sobre qual bairro possuía os melhores mercados: 
Jaraguá Esquerdo ou Corupá. A menção ao Mercado Dany motivou o desenvolvimento de um sistema completo, incluindo backend e aplicação mobile.

Este repositório representa a camada frontend mobile, responsável por consumir a API REST desenvolvida no backend.




Arquitetura do Aplicativo

O aplicativo foi desenvolvido utilizando boas práticas de organização, separando responsabilidades para facilitar manutenção e escalabilidade.




Camadas do App
UI (Interface do Usuário)

Responsável pela exibição dos dados e interação com o usuário.

Telas (Activities/Fragments ou Compose)
Componentes visuais
Navegação entre telas



ViewModel

Responsável por gerenciar os dados da interface.

Mantém os estados da UI
Evita perda de dados em mudanças de tela
Faz a ponte entre UI e lógica de negócio



Repository

Camada responsável por intermediar o acesso aos dados.

Decide de onde os dados vêm (API, cache, etc.)
Centraliza chamadas de dados
Facilita testes e manutenção



Consumo de API

O aplicativo se comunica com o backend por meio de uma API REST.

Requisições HTTP (GET, POST, PUT, DELETE)
Envio e recebimento de dados em formato JSON
Integração com endpoints do backend



Integração com Backend

O aplicativo consome os dados da API desenvolvida em:

Java + Spring Boot

A comunicação ocorre de forma estruturada, utilizando objetos compatíveis entre frontend e backend, garantindo consistência nos dados.



Usuário do Sistema

O sistema possui como principal usuário:

Cliente do mercado

Através do aplicativo, o usuário pode interagir com o sistema, visualizar informações e realizar operações disponíveis na API.



Versionamento

O projeto utiliza Git seguindo boas práticas de desenvolvimento colaborativo:



Branches
master: versão estável
develop: ambiente de desenvolvimento
feature/*: novas funcionalidades



Commits
Pequenos e frequentes
Mensagens claras e descritivas



Pull Requests (PR)
Revisão de código antes da integração
Melhoria na qualidade do projeto



Merge
Integração das alterações aprovadas nas branches principais



Tecnologias Utilizadas
Kotlin
Android Studio / IntelliJ IDEA
Consumo de API REST
Git e GitHub



Considerações Finais

O API-Mobile representa a camada de interação do usuário com o Sistema Mercado Dany, sendo responsável por transformar dados da API em uma experiência utilizável.

O projeto reforça conceitos importantes como:

Desenvolvimento mobile com Kotlin
Consumo de APIs REST
Organização em camadas
Versionamento com Git
