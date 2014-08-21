package br.ufrj.cc.aleph.controller.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class BeaconForm {

	private String email;

	private String name;

	private CommonsMultipartFile arqb;

	private CommonsMultipartFile[] arqpos;

	private CommonsMultipartFile[] arqneg;

	private CommonsMultipartFile[] arqopt;

	@Override
	public String toString() {

		return "BeaconForm [email=" + email + ", name=" + name + "]";
	}

	public String getEmail() {

		return email;
	}

	public void setEmail( final String email ) {

		this.email = email;
	}

	public String getName() {

		return name;
	}

	public void setName( final String name ) {

		this.name = name;
	}

	public CommonsMultipartFile getArqb() {

		return arqb;
	}

	public void setArqb( CommonsMultipartFile arqb ) {

		this.arqb = arqb;
	}

	public CommonsMultipartFile[] getArqpos() {

		return arqpos;
	}

	public void setArqpos( CommonsMultipartFile[] arqpos ) {

		this.arqpos = arqpos;
	}

	public CommonsMultipartFile[] getArqneg() {

		return arqneg;
	}

	public void setArqneg( CommonsMultipartFile[] arqneg ) {

		this.arqneg = arqneg;
	}

	public CommonsMultipartFile[] getArqopt() {

		return arqopt;
	}

	public void setArqopt( CommonsMultipartFile[] arqopt ) {

		this.arqopt = arqopt;
	}

}
