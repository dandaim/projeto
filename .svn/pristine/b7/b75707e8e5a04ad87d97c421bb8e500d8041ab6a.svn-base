package br.ufrj.cc.aleph.helper;

public enum MailContentEnum {
	
	REQUEST( "Caro %s,\n\nRecebemos sua requisição.\nSeus arquivos serão processados" +
			" em breve." ),
	END( "Caro %s,\n\nSeus arquivos já foram processados.\n" +
			"Os resultados estão anexados ao e-mail." ),
	INIT( "Caro %s,\n\nSua requisição será processada a partir de agora.\n" +
			"Em breve notificaremos o status de seu processamento." ),
	PROCESSING( "Caro %s,\n\n" );
	
	private String msg;
	
	MailContentEnum( final String msg ) {
		
		this.setMsg(msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg( final String msg ) {
		this.msg = msg;
	}
	
}
