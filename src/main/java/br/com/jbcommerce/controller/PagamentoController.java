package br.com.jbcommerce.controller;


import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.jbcommerce.model.CarrinhoCompras;
import br.com.jbcommerce.model.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.exceptions.MPApiException;

@Controller
public class PagamentoController {
	@Inject
    private HttpSession session;
	
	@Get("/pagamento")
	public void pagamento(Result result) {
	    try {
	        // Define o token de acesso
	        MercadoPagoConfig.setAccessToken("TEST-6734676405667063-051819-0f32e3f17eefa65d70f3d09119153079-437375574");

	        CarrinhoCompras carrinho = (CarrinhoCompras) session.getAttribute("carrinho");

	        if (carrinho == null || carrinho.getItens().isEmpty()) {
	            result.include("mensagem", "O carrinho está vazio.");
	            result.redirectTo(CarrinhoController.class).listar();
	            return;
	        }

	        // Converte os produtos do carrinho em itens da preferência
	        List<PreferenceItemRequest> items = new ArrayList<>();
	        for (Produto produto : carrinho.getItens()) {
	            PreferenceItemRequest item = PreferenceItemRequest.builder()
	                .title(produto.getNome())
	                .quantity(1) // ajuste se quiser pegar quantidade real
	                .currencyId("BRL")
	                .unitPrice(new BigDecimal(produto.getValor()))
	                .build();
	            items.add(item);
	        }

	     // Cria a requisição de preferência
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

            // Cria o cliente e salva a preferência
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

	        String initPoint = preference.getInitPoint();

	        result.redirectTo(initPoint);

	    } catch (MPApiException | MPException e) {
	        e.printStackTrace();
	        result.include("mensagem", "Erro ao gerar pagamento.");
	        result.redirectTo(CarrinhoController.class).index();
	    }
	}}