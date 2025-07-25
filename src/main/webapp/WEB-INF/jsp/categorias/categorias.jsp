<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib tagdir="/WEB-INF/tags" prefix= "tag"%>

<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>JB-Commerce - Categorias
        </title>
         <link rel="icon" type="image/x-icon" href="assets/img/favicon1.ico" />
        <!-- Font Awesome icons (free version)-->
        <script src="https://use.fontawesome.com/releases/v5.15.1/js/all.js" crossorigin="anonymous"></script>
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
        <link href="https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic,700italic" rel="stylesheet" type="text/css" />
        <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="css/styles.css" rel="stylesheet" />
    </head>
    <body id="page-top">
    <tag:menu-superior></tag:menu-superior>
        <!-- Masthead-->
        <header class="masthead" id="login">
            <div class="container">
               
            </div>
        </header>
        <!-- categorias-->
        <section class="page-section" id="categorias">
            <div class="container">
                <div class="text-center">
                    <h2 class="section-heading text-uppercase mb-3">Categorias</h2>
                </div>
                <c:if test="${usuarioLogado.perfil == 'ADMIN'}">
                <button title="Editar" onclick="window.location.href='formcategoria'" class="btn btn-success mb-3"  type="button">
                    <i class="fas fa-plus mr 1"></i> 
                    Nova Categoria  
                </button>
                
                </c:if>
                <table class="table table-hover">
                    <thead>
                      <tr>
                        <th scope="col">#</th>
                        <th scope="col">Nome</th>
                        <th scope="col">Ações</th>
                      </tr>
                    </thead>
                    
                    <tbody>
                    
                    <c:forEach var="categoria" items="${categorias}">
                    
                     <tr>
                        <th scope="row">${categoria.id}</th>
                        <td>${categoria.nome}</td>
                        <td>
                        <c:if test="${usuarioLogado.perfil == 'ADMIN'}">
                            <button title="Editar" onclick="window.location.href='formcategoria?categoria.id=${categoria.id}'" class="btn btn-info" type="button">
                                <i class="fas fa-edit"></i>   
                            </button>
                            <button title="Excluir" onclick="window.location.href='deletacategoria/${categoria.id}'"class="btn btn-danger" type="button">
                                <i class="fas fa-trash"></i>    
                            </button>
                            </c:if>
                        </td>
                      </tr>
                               
                    </c:forEach>    
                        
                    </tbody>
                    
                  </table>
            </div>
        </section>
    
        <!-- Footer-->
          <tag:footer></tag:footer>
          
        <!-- Bootstrap core JS-->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Third party plugin JS-->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
        <!-- Contact form JS-->
        <script src="assets/mail/jqBootstrapValidation.js"></script>
        <script src="assets/mail/contact_me.js"></script>
        <!-- Core theme JS-->
        <script src="js/scripts.js"></script>
        <script>

        </script>
    </body>
</html>
