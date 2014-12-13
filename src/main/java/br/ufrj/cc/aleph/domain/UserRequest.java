package br.ufrj.cc.aleph.domain;

public class UserRequest {

	private String email;

	private String name;

	private int numFiles;

	private String folder;

	private boolean aleph;

	@Override
	public String toString() {

		return "UserRequest [email=" + email + ", name=" + name + ", numFiles="
				+ numFiles + ", folder=" + folder + "]";
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

	public UserRequest( final String email, final String name,
			final int numFiles, final String folder, final boolean aleph ) {

		this.email = email;
		this.name = name;
		this.numFiles = numFiles;
		this.folder = folder;
		this.aleph = aleph;
	}

	public int getNumFiles() {

		return numFiles;
	}

	public void setNumFiles( final int numFiles ) {

		this.numFiles = numFiles;
	}

	public String getFolder() {

		return folder;
	}

	public void setFolder( String folder ) {

		this.folder = folder;
	}

	public boolean isAleph() {
		return aleph;
	}

	public void setAleph( boolean aleph ) {
		this.aleph = aleph;
	}

}
