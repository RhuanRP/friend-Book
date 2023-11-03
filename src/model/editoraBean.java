	package model;
	
	public class editoraBean {
	    private int Id;
	    private String RazaoSocial;
	    private String Status; 
	    private boolean excluido;
	
	    public editoraBean(int id, String razaoSocial, String status, boolean excluido) { 
	        this.setId(id);
	        this.setRazaoSocial(razaoSocial);
	        this.setStatus(status);
	        this.setExcluido(excluido);
	    }
	    
	    
	    public editoraBean(int id) { 
	        this.setId(id);
	      
	    }
	
	    public int getId() {
	        return Id;
	    }
	
	    public void setId(int id) {
	        this.Id = id;
	    }
	
	    public String getRazaoSocial() {
	        return RazaoSocial;
	    }
	
	    public void setRazaoSocial(String razaoSocial) {
	        this.RazaoSocial = razaoSocial;
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
	
	}
