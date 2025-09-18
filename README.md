# ✈️ AviationAI

🚀 **Projeto Java Spring Boot** para gerenciamento e consumo de APIs relacionadas a aeroportos.

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9.1-blue)](https://maven.apache.org/)
[![Postgres](https://img.shields.io/badge/Postgres-16-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

## ⚙️ Como Rodar o Projeto

**1. Clone o repositório:**

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

## **🐘 Banco de Dados (Postgres via Docker)**
**2.** Suba o container do PostgreSQL 16 com:

```bash
docker run --name aviation-postgres \
  -e POSTGRES_DB=aviationdb \
  -e POSTGRES_USER=aviation \
  -e POSTGRES_PASSWORD=aviation123 \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  -d postgres:16
  
  
 📌 Configurações padrão:
    Banco: aviationdb
    Usuário: aviation
    Senha: aviation123
    Porta: 5432
```
---

## **🧪 Como Rodar os Testes**
**3.** Execute os testes automatizados:

```bash
Execute os testes automatizados com o Maven:

mvn test

Os resultados no console e os relatórios na pasta target/surefire-reports.
```
---

## **🗂 Estrutura do Projeto**


- **Organização em camadas (Clean Architecture)** → separação clara entre API, Aplicação, Infraestrutura e Shared, facilitando manutenção e evolução.

- **Ports & Adapters** → promove baixo acoplamento, facilitando troca de implementações (ex.: mudar banco ou API externa).


### 📦 API
- 🌐 **controllers/** → Controladores REST (`AirportController.java`)
- 📑 **dtos/** → Objetos de transferência de dados (`AirportRequest`, `AirportResponse`)

### 📦 Application
- ⚙️ **services/** → Lógica de negócio (`AirportService`, `CacheCleanupService`)
- 🔌 **ports/** → Interfaces de contrato (`AirportClientPort`, `AirportCachePort`)

### 📦 Infrastructure
- 🌍 **clients/** → Integração com APIs externas (`AirportClient`, `AviationApiAirportResponse`)
- 💾 **persistence/**
    - 🗄️ **entities/** → Entidades JPA (`AirportCacheEntity`)
    - 📚 **repositories/** → Interfaces Spring Data (`AirportCacheRepository`)
    - 🔄 **adapters/** → Conexão entre ports e repositories (`AirportCacheRepositoryAdapter`)

### 📦 Shared
- 🚨 **exceptions/** → Tratamento global de erros (`GlobalExceptionHandler`)
- ✅ **validation/** → Utilitários de validação (`ValidationUtils`)
---