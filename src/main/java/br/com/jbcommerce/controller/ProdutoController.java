package br.com.jbcommerce.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.jbcommerce.dao.ProdutoDAO;
import br.com.jbcommerce.model.Produto;



@Controller
@Path("produto")
public class ProdutoController {
	
	@Inject ProdutoDAO produtoDao;
	@Inject Result result;
		//get /produto listagem de produtos	
	    //get /produto/1 obter o produto id 1
	    //post /produto criar um novo produto ${"nome"}
	    //put /produto/ 1 ${"nome"}
	    //delete /produto/ 1
	
	@Get("")
	public void listagemDeProdutos() {
		List<Produto> produtos = produtoDao.selectAll();
		
		result.use(Results.json())
		.withoutRoot()
		.from(produtos)
		.include("imagem")
		.serialize();
	}
	
	
	
	
	
}
