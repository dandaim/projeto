package br.ufrj.cc.aleph.helper;

public enum MailContentEnum {

	REQUEST(
			"Caro %s,\n\nRecebemos sua requisição no sistema DAHELE.\nSeus arquivos serão processados"
					+ " em breve." ), END(
			"Caro %s,\n\nSeus arquivos já foram processados no sistema DAHELE.\n"
					+ "Os resultados estão anexados ao e-mail." ), INIT(
			"Caro %s,\n\nSua requisição será processada a partir de agora.\n"
					+ "Em breve notificaremos o status de seu processamento." ), PROCESSING(
			"Caro %s,\n\n" ), ERROR(
			"Caro %s, \n\nSua requisição foi abortada por ocorrência de erros ao longo do processamento.\nAntes de retornar este e-mail, "
					+ " observe se sua entrada segue o padrão esperado pelo sistema." );

	private String msg;

	MailContentEnum( final String msg ) {

		this.setMsg( msg );
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg( final String msg ) {
		this.msg = msg;
	}

}
