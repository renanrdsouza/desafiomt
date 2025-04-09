# Technical Documentation

## 1. Overview
Este documento descreve as decisões técnicas tomadas durante o desenvolvimento do projeto, as bibliotecas utilizadas e as possíveis melhorias futuras.

---

## 2. Decisões Técnicas

### 2.1 Estrutura do Projeto
- **Framework**: Utilizei o Spring Boot devido à sua robustez, facilidade de configuração e suporte a microsserviços.
- **Gerenciador de Dependências**: Gradle foi escolhido por sua performance e flexibilidade em projetos Java.
- **Java 21**: Escolhido por ser a versão LTS mais recente, garantindo suporte a longo prazo e acesso a novos recursos.

### 2.2 Integração com HubSpot
- **Motivação**: A integração com a API do HubSpot foi feita para atender aos requisitos do desafio técnico.
- **OAuth 2.0**: Implementado para autenticação segura com a API do HubSpot.
- **Resilience4j**: Utilizado para implementar rate limiting e evitar exceder os limites de requisição da API.

### 2.3 Mensageria com Kafka
- **Motivação**: Kafka foi escolhido para exemplificar como seria o processamento de eventos de forma assíncrona e escalável.
- **Kafdrop**: Adicionado para facilitar a visualização e monitoramento dos tópicos Kafka.

### 2.4 Segurança
- **Spring Security**: Implementado para proteger endpoints sensíveis, como o de criação de contatos.
- **Basic Auth**: Escolhido para simplificar a autenticação no contexto do desafio técnico.

### 2.5 Documentação
- **Swagger**: Integrado para fornecer uma interface de documentação interativa da API, facilitando o entendimento e uso da mesma.
- **Postman**: Uma coleção de requisições foi criada para facilitar os testes e a interação com a API caso não queira usar o swagger.

---

## 3. Bibliotecas Utilizadas

### 3.1 Dependências Principais
- **Spring Boot**: Framework principal para desenvolvimento do projeto.
- **Spring Security**: Para autenticação e autorização.
- **Spring Kafka**: Para integração com o Kafka.
- **Resilience4j**: Para implementar rate limiting.
- **Lombok**: Para reduzir o boilerplate de código.
- **Swagger**: Para documentação da API.

### 3.2 Dependências de Teste
- **JUnit 5**: Framework de testes unitários.
- **Mockito**: Para mockar dependências durante os testes.
- **Spring MockMvc**: Para testes de controladores.

---

## 4. Possíveis Melhorias Futuras

1. **Melhorar a Segurança**:
    - Implementar OAuth 2.0 para autenticação de usuários finais.
    - Adicionar suporte a HTTPS para comunicação segura.

2. **Escalabilidade**:
    - Configurar múltiplos brokers Kafka para maior resiliência.
    - Implementar um sistema de monitoramento mais robusto, como Prometheus e Grafana.

3. **Documentação da API**:
    - Melhorar a documentação com Swagger/OpenAPI.

4. **Testes**:
    - Expandir a cobertura de testes para incluir testes de integração e de carga.

5. **Deploy**:
    - Configurar pipelines CI/CD para automação de build, testes e deploy.

6. **Aplicar boas práticas de Clean Architecture**:
    - Aplicar o padrão de design Ports and Adapters (Hexagonal Architecture) para desacoplar a lógica de negócios da infraestrutura.
    - Separar melhor as camadas de domínio, aplicação e infraestrutura.
    - Implementar testes de unidade e integração mais abrangentes.

---

## 5. Como Executar o Projeto
Consulte o arquivo `README.md` para instruções detalhadas sobre como configurar e executar o projeto.

---

## 6. Conclusão
Este projeto foi desenvolvido com foco em boas práticas de desenvolvimento e escalabilidade. As decisões técnicas foram tomadas com base nos requisitos do desafio e nas melhores ferramentas disponíveis.