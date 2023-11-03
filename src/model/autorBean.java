	package model;
	
	public class autorBean {
	    private int Id;
	    private String Nome;
	    private String Status;
	    private String Documento;
	    private boolean excluido;
	
	    public autorBean(int id, String nome, String documento, String status, boolean excluido) { 
	        this.setId(id);
	        this.setNome(nome);
	        this.setDocumento(documento);
	        this.setStatus(status);
	        this.setExcluido(excluido);
	    }
	    

		public autorBean(int id) { 
	        this.setId(id);
	      
	    }
	
	    public int getId() {
	        return Id;
	    }
	
	    public void setId(int id) {
	        this.Id = id;
	    }
	
	    public String getNome() {
	        return Nome;
	    }
	
	    public void setNome(String Nome) {
	        this.Nome = Nome;
	    }
	
	    public String getStatus() { 
	        return Status;
	    }
	
	    public void setStatus(String status) { 
	        this.Status = status;
	    }
	
		public boolean isExcluido() {
			return excluido;
		}
	
		public void setExcluido(boolean excluido) {
			this.excluido = excluido;
		}


		public String getDocumento() {
			return Documento;
		}


		public void setDocumento(String documento) {
			Documento = documento;
		}
	
	}
