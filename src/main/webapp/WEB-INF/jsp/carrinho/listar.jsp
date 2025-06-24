<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="tag"%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <meta name="description" content="" />
  <meta name="author" content="" />
  <title>JB-Commerce - Carrinho</title>
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon1.ico" />
  
  <script src="https://use.fontawesome.com/releases/v5.15.1/js/all.js" crossorigin="anonymous"></script>
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" />
  <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>

<body id="page-top">
 <!-- Navigation-->
  <tag:menu-superior></tag:menu-superior>
  <!-- Menu -->
<header class="masthead" id="login">
    <div class="container"></div>
  </header>

  <!-- Conteúdo -->
  <section class="page-section bg-light" id="carrinho">
    <div class="container">
      <h2 class="section-heading text-uppercase text-center mb-4">Carrinho de Compras</h2>

      <c:if test="${empty itens}">
        <p class="text-center">Seu carrinho está vazio.</p>
      </c:if>

      <div class="row">
        <c:forEach var="produto" items="${itens}">
          <div class="col-lg-4 col-sm-6 mb-4">
            <div class="portfolio-item">
              <img class="img-fluid" src="${pageContext.request.contextPath}/img/get/${produto.imagem.nome}" alt="Produto" />
              <div class="portfolio-caption">
                <div class="portfolio-caption-heading">${produto.valorMoney}</div>
                <div class="portfolio-caption-subheading text-muted">${produto.nome}</div>
               <form method="post" action="${pageContext.request.contextPath}/carrinho/remover">
				    <input type="hidden" name="id" value="${produto.id}" />
				    <button type="submit" class="btn btn-danger mt-2">
				        <i class="fas fa-trash-alt"></i> Remover
				    </button>
				</form>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <h3 class="text-center mt-4">Total: R$ ${total}</h3>

      <div class="text-center mt-3">
        <form method="post" action="${pageContext.request.contextPath}/carrinho/limpar">
          <button class="btn btn-warning"><i class="fas fa-trash"></i> Limpar Carrinho</button>
        </form>
      </div>
    </div>
    
    
    <div class="text-center mt-3">
<form action="${pageContext.request.contextPath}/pagamento" method="get">
    <button class="btn btn-success">
        <i class="fas fa-credit-card"></i> Finalizar Pagamento
    </button>
</form>

</div>
    
  </section>

  <!-- Footer -->
  <tag:footer />

  <!-- Scripts -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="js/scripts.js"></script>
</body>

</html>
