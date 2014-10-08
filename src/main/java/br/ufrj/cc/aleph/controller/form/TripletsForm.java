package br.ufrj.cc.aleph.controller.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class TripletsForm {

	private String option;
	private String url;
	private String email;
	private String name;
	private CommonsMultipartFile file;
	private String target;

	public String getUrl() {

		return url;
	}

	@Override
	public String toString() {
		return "TripletsForm [option=" + option + ", url=" + url + ", email="
				+ email + ", name=" + name + ", file=" + file + ", target="
				+ target + "]";
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

	public String getEmail() {

		return email;
	}

	public void setEmail( String email ) {

		this.email = email;
	}

	public String getName() {

		return name;
	}

	public void setName( String name ) {

		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget( String target ) {
		this.target = target;
	}

}
