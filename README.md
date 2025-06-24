# 🛒 JB-Commerce

**JB-Commerce** é uma aplicação web de e-commerce desenvolvida com Java e Spring Boot. O sistema oferece funcionalidades completas de autenticação, gerenciamento de produtos, carrinho de compras, e integração com pagamentos via API do Mercado Pago.

## 📷 Demonstrações

### Tela de Login
![Login](./screenshots/login.png)

### Carrinho de Compras
![Carrinho](./screenshots/carrinho.png)

### Finalização de Pagamento (Mercado Pago)
![Pagamento](./screenshots/pagamento.png)

## 🚀 Funcionalidades

- ✅ Cadastro e login de usuários
- ✅ Recuperação de senha
- ✅ Página de listagem de produtos
- ✅ Adição de produtos ao carrinho
- ✅ Remoção e edição de itens no carrinho
- ✅ Cálculo do valor total da compra
- ✅ Integração com a API do Mercado Pago (pagamento por Pix ou Cartão)
- ✅ Painel administrativo para cadastro e gerenciamento de produtos
- ✅ Dashboard com navegação lateral

## 🛠️ Tecnologias Utilizadas

| Camada         | Tecnologia                      |
|----------------|----------------------------------|
| Backend        | Java 17, Spring Boot             |
| Frontend       | HTML, CSS, JavaScript, Thymeleaf |
| Banco de Dados | MySQL                            |
| Pagamentos     | Mercado Pago API (sandbox)       |
| IDE            | IntelliJ IDEA                    |
| Build Tool     | Maven                            |

## 📦 Como Executar o Projeto Localmente

### Pré-requisitos

- Java 17+
- Maven
- MySQL
- IntelliJ IDEA ou Eclipse

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/GabrieldeJb/jb-commerce.git
cd jb-commerce

# 2. Configure o banco de dados
# Crie um schema no MySQL e atualize src/main/resources/application.properties

# 3. Execute o projeto
mvn spring-boot:run
