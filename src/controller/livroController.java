package controller;

import model.livroBean;
import model.livroDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class livroController {
    private static final Logger LOGGER = Logger.getLogger(livroController.class.getName());

    private livroDao livroDao;

    private Connection connection;


    public livroController(Connection connection) {
        this.livroDao = new livroDao(connection);
    }

    public boolean adicionarLivro( String Titulo, int idEditora, int idAutor, String Status) {
        livroBean livro = new livroBean(0, Titulo, idEditora, idAutor, Status, false);
        boolean success = livroDao.adicionarLivro(livro);
        if (success) {
            LOGGER.log(Level.INFO, "Livro adicionada com sucesso: " + Titulo);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao adicionar o livro: " + Titulo);
        }
        return success;
    }
    
	public List<Integer> listarEditorasIDs() {
		return livroDao.listarEditorasIDs();
	}
    
    public List<livroBean> listarTodosLivros(String search) {
        List<livroBean> livros = livroDao.listarTodosLivros(search);
        return livros;
    }

    public boolean atualizarLivros(int id, String Titulo, int Editora, int Autor, String Status) {
        livroBean livro = new livroBean(id, Titulo, Editora, Autor, Status, false);
        boolean success = livroDao.atualizarLivros(livro);
        if (success) {
            LOGGER.log(Level.INFO, "Livro atualizado com sucesso: " + Titulo);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar o livro: " + Titulo);
        }
        return success;
    }


    public boolean marcarComoExcluido(int id) {
    	livroBean livro = new livroBean(id);
        boolean success = livroDao.marcarComoExcluido(livro);
        if (success) {
            LOGGER.log(Level.INFO, "Livro excluida com sucesso: ");
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao excluir a Livro: ");
        }
        return success;

    }

}
