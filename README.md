# 🛒 JB-Commerce

**JB-Commerce** é uma aplicação web de e-commerce desenvolvida com Java e Spring Boot. O sistema oferece funcionalidades completas de autenticação, gerenciamento de produtos, carrinho de compras, e integração com pagamentos via API do Mercado Pago.

## 📷 Demonstrações

### Tela de Login
![Login](https://github.com/GabrieldeJB/jbcommerce/blob/main/src/main/webapp/assets/img/screenshots/Login.png?raw=true)

### Carrinho de Compras
![Carrinho](https://github.com/GabrieldeJB/jbcommerce/blob/main/src/main/webapp/assets/img/screenshots/Carrinho.png?raw=true)

### Pagamento
![Pagamento](https://github.com/GabrieldeJB/jbcommerce/blob/main/src/main/webapp/assets/img/screenshots/Pagamento.png?raw=true)

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
