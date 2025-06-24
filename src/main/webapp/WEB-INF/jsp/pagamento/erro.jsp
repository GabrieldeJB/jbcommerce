<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Erro no Pagamento</title>
  <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
</head>
<body>
  <tag:menu-superior />

  <section class="page-section bg-light text-center">
    <div class="container">
      <h2 class="section-heading text-uppercase text-danger">Erro ao processar o pagamento</h2>
      <p class="mt-3">Houve uma falha ao tentar processar o pagamento. Tente novamente mais tarde.</p>
      <a href="${pageContext.request.contextPath}/carrinho/listar" class="btn btn-warning mt-4">Voltar ao Carrinho</a>
    </div>
  </section>

  <tag:footer />
</body>
</html>
