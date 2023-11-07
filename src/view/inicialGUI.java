package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.amigosController;
import controller.autorController;
import controller.editoraController;
import controller.livroController;
import model.mySqlDAO;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;


import view.autorGUI;
import view.EditoraGUI;

public class inicialGUI extends JFrame {

    public inicialGUI() {
        setTitle("FriendBook");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        JLabel titleLabel = new JLabel("FriendBook", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        titleLabel.setForeground(Color.BLACK);
        getContentPane().add(titleLabel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();


        JMenu menuEditora = new JMenu("Editora");
        menuEditora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem listarEditoras = new JMenuItem("Listar Editoras");

        listarEditoras.addActionListener(e -> {
        	//o que faz a tela funcionar.
            Connection connection = mySqlDAO.getConnection();
       	 editoraController editoraController = new editoraController(connection);

           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   EditoraGUI editoraGUI = new EditoraGUI(editoraController);
                   editoraGUI.setVisible(true);
               }
           });
        });

        menuEditora.add(listarEditoras);


        JMenu menuAutores = new JMenu("Autores");
        menuAutores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem listarAutores = new JMenuItem("Listar Autores");

        listarAutores.addActionListener(e -> {
        	 Connection connection = mySqlDAO.getConnection();
        	 autorController autorController = new autorController(connection);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    autorGUI autorGUI = new autorGUI(autorController);
                   autorGUI.setVisible(true);
                }
            });
        });

        menuAutores.add(listarAutores);

        JMenu menuLivros = new JMenu("Livros");
        menuLivros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem listarLivros = new JMenuItem("Listar Livros");

        listarLivros.addActionListener(e -> {
        	Connection connection = mySqlDAO.getConnection();
       	 livroController livroController = new livroController(connection);

           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   livroGUI livroGUI = new livroGUI(livroController);
                  livroGUI.setVisible(true);
               }
           });

        });

        menuLivros.add(listarLivros);

        JMenu menuAmigos = new JMenu("Amigos");
        menuAmigos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem listarAmigos = new JMenuItem("Listar Amigos");

        listarAmigos.addActionListener(e -> {
        	Connection connection = mySqlDAO.getConnection();
        	amigosController amigosController = new amigosController(connection);
        	
        	SwingUtilities.invokeLater(new Runnable() {
        		public void run() {
        			amigosGUI amigosGUI = new amigosGUI(amigosController);
        			amigosGUI.setVisible(true);
        		}
        	});
        });

        menuAmigos.add(listarAmigos);
        

        JMenu menuEmprestimos = new JMenu("Empréstimos/Devolução");
        menuEmprestimos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem listarEmprestimos = new JMenuItem("Listar Empréstimos");

        listarEmprestimos.addActionListener(e -> {
        	//listar emprestimos
        });

        menuEmprestimos.add(listarEmprestimos);

        menuBar.add(menuEditora);
        menuBar.add(menuAutores);
        menuBar.add(menuLivros); 
        menuBar.add(menuAmigos); 
        menuBar.add(menuEmprestimos);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            inicialGUI inicialGUI = new inicialGUI();
            inicialGUI.setVisible(true);
        });
    }
}
