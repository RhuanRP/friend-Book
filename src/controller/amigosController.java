package controller;

import model.amigosBean;
import model.amigosDao;
import view.amigosGUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class amigosController {
    private static final Logger LOGGER = Logger.getLogger(amigosController.class.getName());

    private amigosDao amigosDao;

    private Connection connection;


    public amigosController(Connection connection) {
        this.amigosDao = new amigosDao(connection);
    }

    public boolean adicionarAmigo(String Nome, String Documento, String Status) {
        amigosBean amigo = new amigosBean(0, Nome, Documento, Status, false);
        boolean success = amigosDao.adicionarAmigo(amigo);
        if (success) {
            LOGGER.log(Level.INFO, "amigo adicionada com sucesso: " + Nome);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao adicionar a amigo: " + Nome);
        }
        return success;
    }

    public List<amigosBean> listarTodosAmigo(String search) {
        List<amigosBean> amigo = amigosDao.listarTodosAmigo(search);
        return amigo;
    }

    public boolean atualizarAmigos(int id, String Nome, String Documento, String Status) {
        amigosBean amigo = new amigosBean(id, Nome, Documento, Status, false);
        boolean success = amigosDao.atualizarAmigos(amigo);
        if (success) {
            LOGGER.log(Level.INFO, "Amigo atualizada com sucesso: " + Nome);
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar a Amigo: " + Nome);
        }
        return success;
    }

    public boolean marcarComoExcluido(int id) {
    	amigosBean amigo = new amigosBean(id);
        boolean success = amigosDao.marcarComoExcluido(amigo);
        if (success) {
            LOGGER.log(Level.INFO, "Amigo excluida com sucesso: ");
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao excluir a Amigo: ");
        }
        return success;

    }
    
    public List<amigosBean> mostrarInativos() {
      	 List<amigosBean> amigo = amigosDao.mostrarInativos();
           return amigo;    	
      }

}