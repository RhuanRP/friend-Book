package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class editoraDAO {

	private Connection connection;

	public editoraDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean adicionarEditora(editoraBean editora) {
		String query = "INSERT INTO editora (RazaoSocial, Status) VALUES (?, ?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, editora.getRazaoSocial());
			psmt.setString(2, editora.getStatus());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<editoraBean> listarTodasEditoras(String search) {
		List<editoraBean> editora = new ArrayList<>();
		String query = "SELECT * FROM editora WHERE Excluidos = 0 AND status <> 'Inativo' AND razaoSocial like '%" + search + "%'";

		try (PreparedStatement psmt = connection.prepareStatement(query); ResultSet rs = psmt.executeQuery()) {
			while (rs.next()) {
				int Id = rs.getInt("Id");
				String RazaoSocial = rs.getString("RazaoSocial");
				String Status = rs.getString("Status");
				boolean excluido = rs.getBoolean("Excluidos");
				editora.add(new editoraBean(Id, RazaoSocial, Status, excluido));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return editora;
	}
	

	public boolean atualizarEditora(editoraBean editora) {
		String query = "UPDATE editora SET RazaoSocial = ?, Status = ? WHERE Id = ?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, editora.getRazaoSocial());
			psmt.setString(2, editora.getStatus());
			psmt.setInt(3, editora.getId());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean marcarComoExcluido(editoraBean editora) {
		String query = "UPDATE editora SET Excluidos = " + true + " WHERE Id = " + editora.getId();
		try {
			PreparedStatement psmt = connection.prepareStatement(query);

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<editoraBean> mostrarInativos() {
		
		 List<editoraBean> editora = new ArrayList<>();
		    String query = "SELECT Id, RazaoSocial, Status FROM editora WHERE Status = 'inativo' AND Excluidos = 0";

		    try (PreparedStatement psmt = connection.prepareStatement(query)) {
		        try (ResultSet rs = psmt.executeQuery()) {
		            while (rs.next()) {
		                int Id = rs.getInt("Id");
		                String RazaoSocial = rs.getString("RazaoSocial");
		                String Status = rs.getString("Status");
		                editora.add(new editoraBean(Id, RazaoSocial, Status, false));
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return editora;
	}

}