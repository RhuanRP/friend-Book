package controller;

import model.autorBean;
import model.autorDao;
import model.editoraBean;
import view.autorGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class autorController {
    private static final Logger LOGGER = Logger.getLogger(autorController.class.getName());

    private autorDao autorDao;

    private Connection connection;


    public autorController(Connection connection) {
        this.autorDao = new autorDao(connection);
    }

    public boolean adicionarAutor(String Nome, String Documento, String Status) {
        autorBean autor = new autorBean(0, Nome, Documento, Status, false);
        boolean success = autorDao.adicionarAutor(autor);
        if (success) {
            LOGGER.log(Level.INFO, "Autor adicionada com sucesso: " + Nome);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao adicionar a autor: " + Nome);
        }
        return success;
    }

    public List<autorBean> listarTodosAutores(String search) {
        List<autorBean> autores = autorDao.listarTodosAutores(search);
        return autores;
    }

    public boolean atualizarAutor(int id, String Nome, String Documento, String Status) {
        autorBean autor = new autorBean(id, Nome, Documento, Status, false);
        boolean success = autorDao.atualizarAutor(autor);
        if (success) {
            LOGGER.log(Level.INFO, "Autor atualizada com sucesso: " + Nome);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar a autor: " + Nome);
        }
        return success;
    }

    public boolean marcarComoExcluido(int id) {
    	autorBean autor = new autorBean(id);
        boolean success = autorDao.marcarComoExcluido(autor);
        if (success) {
            LOGGER.log(Level.INFO, "autor excluida com sucesso: ");
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao excluir a autor: ");
        }
        return success;

    }
    
    public List<autorBean> mostrarInativos() {
      	 List<autorBean> autor = autorDao.mostrarInativos();
           return autor;    	
      }

}