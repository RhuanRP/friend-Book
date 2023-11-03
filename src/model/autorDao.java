package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class autorDao {

	private Connection connection;

	public autorDao(Connection connection) {
		this.connection = connection;
	}

	public boolean adicionarAutor(autorBean autores) {
		String query = "INSERT INTO autores (Nome, Documento, Status) VALUES (?, ?, ?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, autores.getNome());
			psmt.setString(2, autores.getDocumento());
			psmt.setString(3, autores.getStatus());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<autorBean> listarTodosAutores(String search) {
		List<autorBean> autores = new ArrayList<>();
		String query = "SELECT * FROM autores WHERE Excluidos = 0 AND Nome like '%" + search + "%'";

		try (PreparedStatement psmt = connection.prepareStatement(query); ResultSet rs = psmt.executeQuery()) {
			while (rs.next()) {
				int Id = rs.getInt("Id");
				String Nome = rs.getString("Nome");
				String Status = rs.getString("Status");
				String Documento = rs.getString("Documento");
				boolean excluido = rs.getBoolean("Excluidos");
				autores.add(new autorBean(Id, Nome, Documento, Status, excluido));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return autores;
	}

	public boolean atualizarAutor(autorBean autores) {
		String query = "UPDATE autores SET Nome = ?, Documento = ?, Status = ? WHERE Id = ?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, autores.getNome());
			psmt.setString(2, autores.getStatus());
			psmt.setString(3, autores.getDocumento());
			psmt.setInt(4, autores.getId());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean marcarComoExcluido(autorBean autores) {
		String query = "UPDATE autores SET Excluidos = " + true + " WHERE Id = " + autores.getId();
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

}