# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução para dispositivos móveis para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java, usando Spring-boot, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

O foco dessa avaliação é a comunicação entre o backend e o aplicativo mobile. Essa comunicação é feita através de mensagens no formato JSON, onde essas mensagens serão interpretadas pelo cliente para montar as telas onde o usuário vai interagir com o sistema. A aplicação cliente não faz parte da avaliação, apenas os componentes do servidor. O formato padrão dessas mensagens será detalhado no anexo 1.

## Como proceder

Por favor, **CLONE** o repositório e implemente sua solução, ao final, notifique a conclusão e envie o link do seu repositório clonado no GitHub, para que possamos analisar o código implementado.

Lembre de deixar todas as orientações necessárias para executar o seu código.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
  - Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não.
  - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos
  - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação. Essa operação retorna resultados aleatórios, portanto um mesmo CPF pode funcionar em um teste e não funcionar no outro.

```
// CPF Ok para votar
{
    "status": "ABLE_TO_VOTE
}
// CPF Nao Ok para votar - retornar 404 no client tb
{
    "status": "UNABLE_TO_VOTE
}
```

Exemplos de retorno do serviço

### Tarefa Bônus 2 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de
  milhares de votos. Ela deve se comportar de maneira performática nesses
  cenários
- Testes de performance são uma boa maneira de garantir e observar como sua
  aplicação se comporta

### Tarefa Bônus 3 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits

## Dicas

- Teste bem sua solução, evite bugs
- Deixe o domínio das URLs de callback passiveis de alteração via configuração, para facilitar
  o teste tanto no emulador, quanto em dispositivos fisicos.
  Observações importantes
- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e
  deixe claro caso haja instruções especiais para execução do mesmo
  Classificação da informação: Uso Interno

## Anexo 1

### Introdução

A seguir serão detalhados os tipos de tela que o cliente mobile suporta, assim como os tipos de campos disponíveis para a interação do usuário.

### Tipo de tela – FORMULARIO

A tela do tipo FORMULARIO exibe uma coleção de campos (itens) e possui um ou dois botões de ação na parte inferior.

O aplicativo envia uma requisição POST para a url informada e com o body definido pelo objeto dentro de cada botão quando o mesmo é acionado. Nos casos onde temos campos de entrada
de dados na tela, os valores informados pelo usuário são adicionados ao corpo da requisição. Abaixo o exemplo da requisição que o aplicativo vai fazer quando o botão “Ação 1” for acionado:

```
POST http://seudominio.com/ACAO1
{
    “campo1”: “valor1”,
    “campo2”: 123,
    “idCampoTexto”: “Texto”,
    “idCampoNumerico: 999
    “idCampoData”: “01/01/2000”
}
```

Obs: o formato da url acima é meramente ilustrativo e não define qualquer padrão de formato.

### Tipo de tela – SELECAO

A tela do tipo SELECAO exibe uma lista de opções para que o usuário.

O aplicativo envia uma requisição POST para a url informada e com o body definido pelo objeto dentro de cada item da lista de seleção, quando o mesmo é acionado, semelhando ao funcionamento dos botões da tela FORMULARIO.

# Sistema de Votação para Cooperativas

Este projeto foi criado para resolver o desafio de gerenciar votações em assembleias de forma simples e eficiente. O foco foi entregar um código organizado, fácil de ler e que resolve todos os requisitos pedidos sem complicar o que é simples.

## Como o projeto foi pensado

Ao desenvolver a solução, foquei em alguns pontos que considero essenciais para um bom software:

- **Simplicidade**: O código faz exatamente o que precisa fazer. Evitei usar ferramentas ou padrões muito complexos que só dificultariam o entendimento.
- **Organização**: Dividi o projeto em pastas claras (onde ficam as regras, onde ficam os dados, onde ficam os caminhos da API). Isso facilita muito se outra pessoa precisar mexer no código depois.
- **Cuidado com os Dados**: Configurei o banco de dados para salvar as informações em um arquivo. Assim, se você desligar e ligar o sistema, nada do que foi votado é perdido.
- **Tratamento de Erros**: O sistema está preparado para avisar quando algo dá errado. Por exemplo, se alguém tentar votar duas vezes ou em uma votação que já fechou, o sistema retorna uma mensagem clara explicando o motivo.

## O que o sistema faz

- **Criação de Pautas**: Você pode cadastrar o assunto que será votado.
- **Abertura de Votação**: Você escolhe por quanto tempo a votação ficará aberta (se não escolher, o padrão é 1 minuto).
- **Votação Real**: O sistema recebe os votos ("Sim" ou "Não") e verifica se o associado pode votar através de uma consulta simulada.
- **Resultado**: O sistema conta os votos e diz quem ganhou ou se houve empate.

## Como Rodar

1.  Certifique-se de ter o **JDK 17** e o **Maven** instalados.
2.  Build do projeto:
    ```bash
    mvn clean install
    ```
3.  Execução:
    ```bash
    mvn spring-boot:run
    ```
4.  Acesse o Swagger em: `http://localhost:8080/swagger-ui.html` para testar os endpoints de forma interativa.

## Endpoints Principais

A API está dividida logicamente:

| Recurso | Método | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **Pauta** | POST | `/v1/pautas` | Cria uma nova pauta. |
| **Sessão** | POST | `/v1/pautas/{id}/sessao` | Abre a votação (default 1min). |
| **Voto** | POST | `/v1/pautas/{id}/votos` | Registra voto com validação de CPF. |
| **Resultado** | GET | `/v1/pautas/{id}/resultado` | Consolida a votação. |


## Qualidade e Testes

Criei testes automáticos para garantir que as regras principais não quebrem. Por exemplo, existem testes que garantem que ninguém vote depois do tempo ou que um associado não vote mais de uma vez.

---



# desafio-votacao
