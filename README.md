# AviationAI

🚀 **Projeto Java Spring Boot** para gerenciamento e consumo de APIs relacionadas a aeroportos.

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9.1-blue)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

## **⚙️ Como Rodar o Projeto**

**1.** Clone o repositório:

```bash
git clone https://github.com/seu-usuario/aviationai.git
cd aviationai

Certifique-se de ter o Java 17 e o Maven instalados.

Comandos utilizados: 
* Instalação Java:
  sudo apt-get install openjdk-17-jdk openjdk-17-jdk-headless
* Instalação Maven:
  sudo apt-get install maven

Compile e rode o projeto:

mvn clean install
mvn spring-boot:run

O servidor estará disponível em: http://localhost:8080.
```
---

## **🧪 Como Rodar os Testes**

**2.** Execute os testes automatizados:

```bash
Execute os testes automatizados com o Maven:

mvn test

Os resultados no console e os relatórios na pasta target/surefire-reports.
```
---

## **🗂 Estrutura do Projeto**

📦 **controller/**
- **Explicação:** Contém as classes que recebem requisições HTTP (endpoints REST).
- **Função:** Traduz chamadas externas em ações do sistema, chamando serviços da camada de `application`.

📦 **application/**
- **Explicação:** Camada de serviço com a lógica de negócio.
- **Função:** Processar dados, aplicar regras de negócio e orquestrar chamadas entre controllers e infraestrutura.

📦 **infrastructure/**
- **Explicação:** Responsável por integrações externas e persistência.
- **Função:** Comunicação com APIs externas, bancos de dados ou sistemas de arquivos.

📦 **shared/**
- **Explicação:** Classes e componentes compartilhados entre várias partes do projeto.
- **Função:** Evitar duplicação de código, centralizar constantes, enums, DTOs e helpers genéricos.

📦 **utils/**
- **Explicação:** Funções utilitárias que não pertencem a nenhuma camada específica.
- **Função:** Operações comuns, como formatação de datas, validações ou parsing de strings.

📦 **exception/**
- **Explicação:** Tratamento de erros e exceções customizadas.
- **Função:** Definir exceções do sistema e fornecer respostas consistentes via `@ControllerAdvice`.
---