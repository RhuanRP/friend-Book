package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class livroDao {

	private Connection connection;

	public livroDao(Connection connection) {
		this.connection = connection;
	}

	public boolean adicionarLivro(livroBean livros) {
		String query = "INSERT INTO livros (Titulo, idEditora, idAutor, Status) VALUES (?, ?, ?, ?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, livros.getTitulo());
			psmt.setInt(2, livros.getIdEditora());
			psmt.setInt(3, livros.getIdAutor());
			psmt.setString(4, livros.getStatus());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Integer> listarEditorasIDs() {
	    List<Integer> editoraIDs = new ArrayList<>();
	    String query = "SELECT id FROM Editora";

	    try (PreparedStatement psmt = connection.prepareStatement(query); ResultSet rs = psmt.executeQuery()) {
	        while (rs.next()) {
	            int idEditora = rs.getInt("id");
	            editoraIDs.add(idEditora);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return editoraIDs;
	}


	public List<Integer> listarAutoresIDs() {
	    List<Integer> autorIDs = new ArrayList<>();
	    String query = "SELECT id FROM Autores";

	    try (PreparedStatement psmt = connection.prepareStatement(query); ResultSet rs = psmt.executeQuery()) {
	        while (rs.next()) {
	            int idAutor = rs.getInt("idAutor");
	            autorIDs.add(idAutor);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return autorIDs;
	}


	public List<livroBean> listarTodosLivros(String search) {
	    List<livroBean> livros = new ArrayList<>();
	    String query = "SELECT Id, Titulo, idEditora, idAutor, Status FROM livros WHERE Titulo LIKE ? AND Excluidos = 0";

	    try (PreparedStatement psmt = connection.prepareStatement(query)) {
	        psmt.setString(1, "%" + search + "%");
	        try (ResultSet rs = psmt.executeQuery()) {
	            while (rs.next()) {
	                int Id = rs.getInt("Id");
	                String Titulo = rs.getString("Titulo");
	                int idEditora = rs.getInt("idEditora");
	                int idAutor = rs.getInt("idAutor");
	                String Status = rs.getString("Status");
	                livros.add(new livroBean(Id, Titulo, idEditora, idAutor, Status, false));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return livros;
	}


	public boolean atualizarLivros(livroBean livros) {
		String query = "UPDATE livros SET Titulo = ?, Status = ? WHERE Id = ?";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, livros.getTitulo());
			psmt.setString(2, livros.getStatus());
			psmt.setInt(3, livros.getId());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean marcarComoExcluido(livroBean livros) {
		String query = "UPDATE livros SET Excluidos = " + true + " WHERE Id = " + livros.getId();
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