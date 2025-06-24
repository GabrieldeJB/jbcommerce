<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Pagamento Aprovado</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>
<body>
  <tag:menu-superior />

  <section class="page-section bg-light text-center">
    <div class="container">
      <h2 class="section-heading text-uppercase text-success">Pagamento realizado com sucesso!</h2>
      <p class="mt-3">Obrigado por comprar conosco. Seu pedido será processado em breve.</p>
      <a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-4">Voltar à loja</a>
    </div>
  </section>

  <tag:footer />
</body>
</html>
