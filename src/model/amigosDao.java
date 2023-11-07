package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class amigosDao {

	private Connection connection;

	public amigosDao(Connection connection) {
		this.connection = connection;
	}

	public boolean adicionarAmigo(amigosBean amigos) {
		String query = "INSERT INTO amigos (Nome, Documento, Status) VALUES (?, ?, ?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, amigos.getNome());
			psmt.setString(2, amigos.getDocumento());
			psmt.setString(3, amigos.getStatus());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<amigosBean> listarTodosAmigo(String search) {
		List<amigosBean> amigos = new ArrayList<>();
		String query = "SELECT * FROM amigos WHERE Excluidos = 0 AND status <> 'Inativo' AND Nome like '%" + search + "%'";

		try (PreparedStatement psmt = connection.prepareStatement(query); ResultSet rs = psmt.executeQuery()) {
			while (rs.next()) {
				int Id = rs.getInt("Id");
				String Nome = rs.getString("Nome");
				String Documento = rs.getString("Documento");
				String Status = rs.getString("Status");
				boolean excluido = rs.getBoolean("Excluidos");
				amigos.add(new amigosBean(Id, Nome, Documento, Status, excluido));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return amigos;
	}

	public boolean atualizarAmigos(amigosBean amigos) {
		String query = "UPDATE amigos SET Nome = ?, Documento = ?, Status = ? WHERE Id = ?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, amigos.getNome());
			psmt.setString(2, amigos.getDocumento());
			psmt.setString(3, amigos.getStatus());
			psmt.setInt(4, amigos.getId());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean marcarComoExcluido(amigosBean amigos) {
		String query = "UPDATE amigos SET Excluidos = " + true + " WHERE Id = " + amigos.getId();
		System.out.println(query);

		try {
			PreparedStatement psmt = connection.prepareStatement(query);

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<amigosBean> mostrarInativos() {
		
		 List<amigosBean> autor = new ArrayList<>();
		    String query = "SELECT Id, Nome, Documento, Status FROM amigos WHERE Status = 'inativo' AND Excluidos = 0";

		    try (PreparedStatement psmt = connection.prepareStatement(query)) {
		        try (ResultSet rs = psmt.executeQuery()) {
		            while (rs.next()) {
		                int Id = rs.getInt("Id");
		                String Nome = rs.getString("Nome");
		                String Documento = rs.getString("Documento");
		                String Status = rs.getString("Status");
		                autor.add(new amigosBean(Id, Nome, Documento,Status, false));
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return autor;
	}

}