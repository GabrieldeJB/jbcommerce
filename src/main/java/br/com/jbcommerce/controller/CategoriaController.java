package br.com.jbcommerce.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.jbcommerce.model.Categoria;
import br.com.olimposistema.aipa.dao.DAO;
import br.com.olimposistema.aipa.vraptorcrud.CrudRest;

@Controller
@Path("categoria")
public class CategoriaController extends CrudRest<Categoria>{
	@Inject Result result;
	private DAO<Categoria> dao;
	@Inject
	public CategoriaController(DAO<Categoria> dao) {
		super(Categoria.class, dao);
		this.dao = dao;

	}
	
	@Deprecated public CategoriaController() {this(null);
	
	}
}

























/*@Post("") @Consumes("application/json")
	public void adicionaCategoria(Categoria categoria) {
		Categoria categoriaInserida = categoriaDao.insertOrUpdate(categoria);
		
		result.use(Results.json())
		.withoutRoot()
		.from(categoriaInserida)
		.serialize();
		
	}*/