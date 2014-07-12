package br.ufrj.cc.aleph.controller.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class TripletsForm {

	private String option;
	private String url;
	private CommonsMultipartFile file;

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile( CommonsMultipartFile file ) {
		this.file = file;
	}

	public String getOption() {
		return option;
	}

	public void setOption( String option ) {
		this.option = option;
	}

}
