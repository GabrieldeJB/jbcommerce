package br.com.jbcommerce.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.IncludeParameters;
import br.com.caelum.vraptor.validator.Validator;
import br.com.jbcommerce.dao.ProdutoDAO;
import br.com.jbcommerce.interceptors.SomenteLogado;
import br.com.jbcommerce.model.Categoria;
import br.com.jbcommerce.model.Produto;
import br.com.olimposistema.aipa.dao.DAO;

@Controller
@Path("formproduto")
public class FormProdutoController {
	@Inject Result result;
	@Inject ProdutoDAO produtoDao; 
	@Inject Validator validator;
	@Inject DAO<Categoria> categoriaDao;
	
	@Get("") @SomenteLogado
	public void formproduto() {
		result.include("categorias",categoriaDao.selectAll());
		}
	
	
	@IncludeParameters
	@SomenteLogado
	@Post("salvaProduto")
	public void salvaProduto(@Valid Produto produto){
		validator.onErrorRedirectTo(this).formproduto();
		produtoDao.insertOrUpdate(produto); 
		result.redirectTo(ProdutosController.class).produtos(null);

	}
}
