# Marketplace de Artesões - Projeto P1

## 📜 Sobre o Projeto

O **Marketplace de Artesões** é uma plataforma de e-commerce desenvolvida como parte da avaliação P1 da disciplina de Programação Web. O principal objetivo do projeto é criar um ambiente online onde artesãos de todo o Brasil possam criar suas lojas virtuais para expor e vender seus produtos, e os clientes possam descobrir e comprar peças artesanais únicas.

A aplicação simula um fluxo completo de e-commerce, incluindo:
-   Dois tipos de perfis de usuário: **Clientes** e **Artesãos**.
-   Cadastro e login unificado para ambos os tipos de usuário.
-   Visualização de produtos em uma vitrine principal.
-   Página de detalhes para cada produto.
-   Funcionalidade de carrinho de compras persistente por sessão.
-   Fluxo de finalização de compra (checkout) para clientes logados.
-   Dashboard para o artesão visualizar e cadastrar novos produtos em sua loja.

Atualmente, o projeto utiliza listas em memória para simular a persistência de dados, com foco na implementação das regras de negócio e na interação entre os componentes do backend e a interface do usuário.

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando o ecossistema Spring e tecnologias modernas de desenvolvimento Java:

-   **Backend:**
    -   Java 17
    -   Spring Boot 3.5.4
    -   Spring Web (para a construção de controllers e APIs RESTful)
    -   Spring Data JPA / Hibernate (para o mapeamento objeto-relacional das entidades)
-   **Frontend:**
    -   Thymeleaf (motor de templates para renderizar as páginas HTML dinamicamente)
    -   HTML5
    -   CSS3
-   **Banco de Dados (Desenvolvimento):**
    -   H2 Database (banco de dados em memória/arquivo, ideal para desenvolvimento e testes)
-   **Gerenciamento de Dependências e Build:**
    -   Apache Maven

## 🚀 Como Executar o Projeto

Para clonar, compilar e executar esta aplicação em seu ambiente local, siga os passos abaixo.

### Pré-requisitos

Antes de começar, certifique-se de que você tem os seguintes softwares instalados em sua máquina:
-   **Java Development Kit (JDK) 17 ou superior.**
-   **Apache Maven 3.6 ou superior.**
-   **Git** (para clonar o repositório).

### Passo a Passo

**1. Clone o Repositório**

Abra seu terminal ou Git Bash e utilize o seguinte comando para clonar o projeto:

```bash
git clone https://github.com/AlvaroIFF/MarketplaceArtesoes.git
cd MarketplaceArtesoes
```

**2. Compile o Projeto com Maven**

Navegue até a pasta raiz do projeto (onde o arquivo `pom.xml` está localizado) e execute o comando abaixo para que o Maven baixe todas as dependências e compile o código:

```bash
mvn clean install
```
Este comando irá gerar um arquivo `.jar` na pasta `target/`.

**3. Execute a Aplicação**

Você pode executar a aplicação de duas maneiras:

**Opção A: Diretamente pelo Maven**

Este é o método mais simples para desenvolvimento. Na pasta raiz do projeto, execute:

```bash
mvn spring-boot:run
```

**Opção B: Utilizando o arquivo JAR gerado**

Após compilar o projeto (passo 2), você pode executar o arquivo JAR diretamente:

```bash
java -jar target/marketplaceartesoes-0.0.1-SNAPSHOT.jar
```

**4. Acesse a Aplicação**

Após iniciar o servidor, abra seu navegador de internet e acesse as seguintes URLs:

-   **Página Inicial:** [http://localhost:8080/](http://localhost:8080/)
-   **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    -   **JDBC URL:** `jdbc:h2:file:./data/exemplo`
    -   **User Name:** `sa` 
    -   **Password:** `password`
-   **Documentação Swagger:** [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

A aplicação estará rodando e pronta para ser utilizada!
