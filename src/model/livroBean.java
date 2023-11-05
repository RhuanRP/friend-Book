package model;

public class livroBean {
    private int id;
    private String titulo;
    private String status;
    private int idEditora;
    private String nomeEditora;
    private String nomeAutor;
    private int idAutor;
    private boolean excluido;

    public livroBean(int id, String titulo, String status, int idEditora, int idAutor, boolean excluido) {
        this.id = id;
        this.titulo = titulo;
        this.idEditora = idEditora;
        this.idAutor = idAutor;
        this.status = status;
        this.excluido = excluido;
    }
    
    public livroBean(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }

    public int getIdEditora() {
        return idEditora;
    }

    public void setIdEditora(int idEditora) {
        this.idEditora = idEditora;
    }
    
    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

	public String getNomeEditora() {
		return nomeEditora;
	}

	public livroBean setNomeEditora(String nomeEditora) {
		this.nomeEditora = nomeEditora;
		return this;
	} 

	public String getNomeAutor() {
		return nomeAutor;
	}

	public livroBean setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
		return this;
	}
}
