package br.com.jbcommerce.rn;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ConverteDataEnParaCalendar {

	public Calendar executar(String data) {
		//
		String[] dataParts = data.split("-");
		
		Integer ano = Integer.parseInt(dataParts[0]);
		Integer mes = Integer.parseInt(dataParts[1]);
		Integer dia = Integer.parseInt(dataParts[2]);
		
		Calendar calendar = new GregorianCalendar(ano, (mes-1), dia);
		return calendar;

	}

}
