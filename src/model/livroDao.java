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
		String query = "INSERT INTO livros (Titulo, Status, idEditora, idAutor) VALUES (?, ?, ?, ?)";

		try (PreparedStatement psmt = connection.prepareStatement(query)) {
			psmt.setString(1, livros.getTitulo());
			psmt.setString(2, livros.getStatus());
			psmt.setInt(3, livros.getIdEditora());
			psmt.setInt(4, livros.getIdAutor());

			int rowsAffected = psmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<livroBean> listarTodosLivros(String search) {
	    List<livroBean> livros = new ArrayList<>();
	    String query = "SELECT l.Id, Titulo, idEditora, idAutor, l.Status, e.RazaoSocial, a.Nome\r\n"
	    		+ "FROM livros as l\r\n"
	    		+ "JOIN editora as e ON e.id = l.idEditora\r\n"
	    		+ "JOIN autores as a ON a.id = l.idAutor\r\n"
	    		+ "WHERE Titulo LIKE ? AND l.Excluidos = 0 AND l.Status <> 'Inativo';\r\n"
	    		+ "";

	    try (PreparedStatement psmt = connection.prepareStatement(query)) {
	        psmt.setString(1, "%" + search + "%");
	        try (ResultSet rs = psmt.executeQuery()) {
	            while (rs.next()) {
	                int Id = rs.getInt("Id");
	                String Titulo = rs.getString("Titulo");
	                int idEditora = rs.getInt("idEditora");
	                int idAutor = rs.getInt("idAutor");
	                String Status = rs.getString("Status");
	                String nomeEditora = rs.getString("RazaoSocial");
	                String nomeAutor = rs.getString("Nome");
	                livros.add(new livroBean(Id, Titulo, Status, idEditora, idAutor, false).setNomeEditora(nomeEditora).setNomeAutor(nomeAutor));
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
	
	public List<livroBean> mostrarInativos() {
		
		 List<livroBean> livros = new ArrayList<>();
		    String query = "SELECT l.Id, Titulo, idEditora, idAutor, l.Status, e.RazaoSocial, a.Nome FROM livros as l join editora as e on e.id = l.idEditora join autores as a on a.id = l.idAutor WHERE l.Status = 'inativo' AND l.Excluidos = 0";
		    try (PreparedStatement psmt = connection.prepareStatement(query)) {
		        try (ResultSet rs = psmt.executeQuery()) {
		            while (rs.next()) {
		                int Id = rs.getInt("Id");
		                String Titulo = rs.getString("Titulo");
		                int idEditora = rs.getInt("idEditora");
		                int idAutor = rs.getInt("idAutor");
		                String Status = rs.getString("Status");
		                String nomeEditora = rs.getString("RazaoSocial");
		                String nomeAutor = rs.getString("Nome");
		                livros.add(new livroBean(Id, Titulo, Status, idEditora, idAutor, false).setNomeEditora(nomeEditora).setNomeAutor(nomeAutor));
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return livros;
	}

}