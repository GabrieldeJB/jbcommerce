package br.com.jbcommerce.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.*;
import br.com.jbcommerce.dao.ProdutoDAO;
import br.com.jbcommerce.model.CarrinhoCompras;
import br.com.jbcommerce.model.Produto;

@Controller
@Path("/carrinho")
public class CarrinhoController {

    @Inject
    private Result result;

    @Inject
    private ProdutoDAO produtoDao;

    @Inject
    private HttpSession session;

    private CarrinhoCompras getCarrinho() {
        CarrinhoCompras carrinho = (CarrinhoCompras) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new CarrinhoCompras();
            session.setAttribute("carrinho", carrinho);
        }
        return carrinho;
    }

    @Get("/")
    public void index() {
        result.redirectTo(this).listar();
    }

    @Post("/adicionar/{id}")
    public void adicionar(int id) {
        Produto produto = produtoDao.buscaPorId(id);
        getCarrinho().adicionar(produto);
        result.redirectTo(this).listar();
    }


    @Get("/listar")
    public void listar() {
        result.include("itens", getCarrinho().getItens());
        result.include("total", getCarrinho().getTotal());
    }

    @Post("/remover")
    public void remover(int id) {
        Produto produto = produtoDao.buscaPorId(id);
        getCarrinho().remover(produto);
        result.redirectTo(this).listar();
    }




    @Post("/limpar")
    public void limpar() {
        getCarrinho().limpar();
        result.redirectTo(this).listar();
    }
} 
