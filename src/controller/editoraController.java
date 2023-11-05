package controller;

import model.editoraBean;
import model.editoraDAO;
import model.livroBean;
import view.EditoraGUI;
import view.autorGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class editoraController {
    private static final Logger LOGGER = Logger.getLogger(editoraController.class.getName());

    private editoraDAO editoraDAO;

    private Connection connection;


    public editoraController(Connection connection) {
        this.editoraDAO = new editoraDAO(connection);
    }

    public boolean adicionarEditora(String RazaoSocial, String Status) {
        editoraBean editora = new editoraBean(0, RazaoSocial, Status, false);
        boolean success = editoraDAO.adicionarEditora(editora);
        if (success) {
            LOGGER.log(Level.INFO, "Editora adicionada com sucesso: " + RazaoSocial);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao adicionar a editora: " + RazaoSocial);
        }
        return success;
    }

    public List<editoraBean> listarTodasEditoras(String search) {
        List<editoraBean> editoras = editoraDAO.listarTodasEditoras(search);
        return editoras;
    }

    public boolean atualizarEditora(int id, String RazaoSocial, String Status) {
        editoraBean editora = new editoraBean(id, RazaoSocial, Status, false);
        boolean success = editoraDAO.atualizarEditora(editora);
        if (success) {
            LOGGER.log(Level.INFO, "Editora atualizada com sucesso: " + RazaoSocial);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar a editora: " + RazaoSocial);
        }
        return success;
    }

    public boolean marcarComoExcluido(int id) {
    	editoraBean editora = new editoraBean(id);
        boolean success = editoraDAO.marcarComoExcluido(editora);
        if (success) {
            LOGGER.log(Level.INFO, "Editora excluida com sucesso: ");
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao excluir a editora: ");
        }
        return success;
    }
    
    public List<editoraBean> mostrarInativos() {
   	 List<editoraBean> editora = editoraDAO.mostrarInativos();
        return editora;    	
   }

}