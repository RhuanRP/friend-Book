package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;

import controller.autorController;
import model.autorBean;
import model.editoraBean;
import model.livroBean;
import model.mySqlDAO;

public class autorGUI extends JFrame {
    private JTable autorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton; 
    private JButton deleteButton; 
    private JButton editButton;
    private boolean showInativos = false;
    private autorController autorController;
	protected Connection connection;

    public autorGUI(autorController controller) {
        this.autorController = controller;
        setTitle("Lista de Autores");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Pesquisar");
        addButton = new JButton("Novo"); 
        deleteButton = new JButton("Excluir");
        editButton = new JButton("Editar");


        searchButton.addActionListener(e -> {
        	String search = searchField.getText();
            List<autorBean> autores = autorController.listarTodosAutores(search);
            updateTable(autores);
        });
        
        String[] statusOptions = {"Ativo", "Inativo"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = JOptionPane.showInputDialog("Informe o nome:");
                if (nome != null && !nome.isEmpty()) {
                    String documento = JOptionPane.showInputDialog("Informe o documento:");
                    if (documento != null && !documento.isEmpty()) {
                        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
                        int option = JOptionPane.showConfirmDialog(autorGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String status = (String) statusComboBox.getSelectedItem();
                            boolean success = autorController.adicionarAutor(nome, documento, status);
                            if (success) {
                                JOptionPane.showMessageDialog(autorGUI.this, "Novo Autor adicionado com sucesso.");
                                List<autorBean> autores = autorController.listarTodosAutores("");
                                updateTable(autores);
                            } else {
                                JOptionPane.showMessageDialog(autorGUI.this, "Erro ao adicionar o novo autor.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(autorGUI.this, "Você cancelou a adição.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(autorGUI.this, "O documento não pode ser nulo ou vazio.");
                    }
                } else {
                    JOptionPane.showMessageDialog(autorGUI.this, "O nome não pode ser nulo ou vazio.");
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = autorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) autorTable.getValueAt(selectedRow, 0);
                    boolean success = autorController.marcarComoExcluido(id);
                    
                    if (success) {
                        autorTable.setValueAt("1", selectedRow, 2); 
                    } else {
                        JOptionPane.showMessageDialog(autorGUI.this, "Erro ao marcar o registro como excluído.");
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = autorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) autorTable.getValueAt(selectedRow, 0);
                    String novoNome = (String) JOptionPane.showInputDialog("Novo Nome:");
                    if (novoNome != null && !novoNome.isEmpty()) {
                        String novoDocumento = (String) JOptionPane.showInputDialog("Novo documento:");
                        if (novoDocumento != null && !novoDocumento.isEmpty()) {
                            JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
                            int option = JOptionPane.showConfirmDialog(autorGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                            if (option == JOptionPane.OK_OPTION) {
                                String novoStatus = (String) statusComboBox.getSelectedItem();
                                boolean success = autorController.atualizarAutor(id, novoNome, novoDocumento, novoStatus);
                                if (success) {
                                    JOptionPane.showMessageDialog(autorGUI.this, "Autor editado com sucesso.");
                                    List<autorBean> autores = autorController.listarTodosAutores("");
                                    updateTable(autores);
                                } else {
                                    JOptionPane.showMessageDialog(autorGUI.this, "Erro ao atualizar o autor.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(autorGUI.this, "Você cancelou a edição.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(autorGUI.this, "O novo documento não pode ser nulo ou vazio.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(autorGUI.this, "O novo nome não pode ser nulo ou vazio.");
                    }
                }
            }
        });
		

        searchPanel.add(new JLabel("Nome do autor: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(deleteButton);
        searchPanel.add(editButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Documento", "Status"}, 0);
        autorTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(autorTable);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JCheckBox showInativosCheckBox = new JCheckBox("Mostrar Inativos");
        searchPanel.add(showInativosCheckBox);
				
        showInativosCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean showInativos = showInativosCheckBox.isSelected();
                if (showInativos) {
                    List<autorBean> autores = autorController.mostrarInativos();
                    updateTable(autores);
                } else {
                    List<autorBean> autores = autorController.listarTodosAutores("");
                    updateTableInativos(autores);
                }
            }
        });
    }
    

    private void updateTable(List<autorBean> autores) {
        tableModel.setRowCount(0);
        for (autorBean autor : autores) {
            if (!autor.isExcluido()) {
                tableModel.addRow(new Object[]{autor.getId(), autor.getNome(), autor.getDocumento(), autor.getStatus()});
            }
        }
    }
    
    private void updateTableInativos(List<autorBean> autores) {
        tableModel.setRowCount(0);
        for (autorBean autor : autores) {
            if (!autor.isExcluido( )&& (showInativos || !autor.getStatus().equalsIgnoreCase("Inativo"))){
                tableModel.addRow(new Object[]{autor.getId(), autor.getNome(), autor.getDocumento(), autor.getStatus()});
            }
        }
    }

    public static void main(String[] args) {
    	
    	 Connection connection = mySqlDAO.getConnection();
    	 autorController autorController = new autorController(connection);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                autorGUI autorGUI = new autorGUI(autorController);
               autorGUI.setVisible(true);
            }
        });
    }
}
