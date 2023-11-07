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

import controller.amigosController;
import model.amigosBean;
import model.amigosBean;
import model.livroBean;
import model.mySqlDAO;

public class amigosGUI extends JFrame {
    private JTable amigosTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton; 
    private JButton deleteButton; 
    private JButton editButton;
    private boolean showInativos = false;
    private amigosController amigosController;
	protected Connection connection;

    public amigosGUI(amigosController controller) {
        this.amigosController = controller;
        setTitle("Amigos");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Pesquisar");
        addButton = new JButton("Novo"); 
        deleteButton = new JButton("Excluir");
        editButton = new JButton("Editar");

        searchButton.addActionListener(e -> {
        	String search = searchField.getText();
            List<amigosBean> amigos = amigosController.listarTodosAmigo(search);
            updateTable(amigos);
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
                        int option = JOptionPane.showConfirmDialog(amigosGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String status = (String) statusComboBox.getSelectedItem();
                            boolean success = amigosController.adicionarAmigo(nome, documento, status);
                            if (success) {
                                JOptionPane.showMessageDialog(amigosGUI.this, "Novo Autor adicionado com sucesso.");
                                List<amigosBean> amigos = amigosController.listarTodosAmigo("");
                                updateTable(amigos);
                            } else {
                                JOptionPane.showMessageDialog(amigosGUI.this, "Erro ao adicionar o novo autor.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(amigosGUI.this, "Você cancelou a edição.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(amigosGUI.this, "O documento não pode ser nulo ou vazio.");
                    }
                } else {
                    JOptionPane.showMessageDialog(amigosGUI.this, "O nome não pode ser nulo ou vazio.");
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = amigosTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) amigosTable.getValueAt(selectedRow, 0);
                    boolean success = amigosController.marcarComoExcluido(id);
                    
                    if (success) {
                        amigosTable.setValueAt("1", selectedRow, 2); 
                    } else {
                        JOptionPane.showMessageDialog(amigosGUI.this, "Erro ao marcar o registro como excluído.");
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = amigosTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) amigosTable.getValueAt(selectedRow, 0);
                    String novoNome = (String) JOptionPane.showInputDialog("Novo Nome:");
                    if (novoNome != null && !novoNome.isEmpty()) {
                        String novoDocumento = (String) JOptionPane.showInputDialog("Novo documento:");
                        if (novoDocumento != null && !novoDocumento.isEmpty()) {
                            JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
                            int option = JOptionPane.showConfirmDialog(amigosGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                            if (option == JOptionPane.OK_OPTION) {
                                String novoStatus = (String) statusComboBox.getSelectedItem();
                                boolean success = amigosController.atualizarAmigos(id, novoNome, novoDocumento, novoStatus);
                                if (success) {
                                    JOptionPane.showMessageDialog(amigosGUI.this, "Amigo editado com sucesso.");
                                    List<amigosBean> autores = amigosController.listarTodosAmigo("");
                                    updateTable(autores);
                                } else {
                                    JOptionPane.showMessageDialog(amigosGUI.this, "Erro ao atualizar o amigo.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(amigosGUI.this, "Você cancelou a edição.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(amigosGUI.this, "O novo documento não pode ser nulo ou vazio.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(amigosGUI.this, "O novo nome não pode ser nulo ou vazio.");
                    }
                }
            }
        });
		

        searchPanel.add(new JLabel("Nome do amigo: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(deleteButton);
        searchPanel.add(editButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Documento", "Status"}, 0);
        amigosTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(amigosTable);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JCheckBox showInativosCheckBox = new JCheckBox("Mostrar Inativos");
        searchPanel.add(showInativosCheckBox);
				
        showInativosCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean showInativos = showInativosCheckBox.isSelected();
                if (showInativos) {
                    List<amigosBean> amigos = amigosController.mostrarInativos();
                    updateTable(amigos);
                } else {
                    List<amigosBean> amigos = amigosController.listarTodosAmigo("");
                    updateTableInativos(amigos);
                }
            }
        });
    }
    

    private void updateTable(List<amigosBean> amigos) {
        tableModel.setRowCount(0);
        for (amigosBean amigo : amigos) {
            if (!amigo.isExcluido()) {
                tableModel.addRow(new Object[]{amigo.getId(), amigo.getNome(), amigo.getDocumento(), amigo.getStatus()});
            }
        }
    }
    
    private void updateTableInativos(List<amigosBean> amigos) {
        tableModel.setRowCount(0);
        for (amigosBean amigo : amigos) {
            if (!amigo.isExcluido( )&& (showInativos || !amigo.getStatus().equalsIgnoreCase("Inativo"))){
                tableModel.addRow(new Object[]{amigo.getId(), amigo.getNome(), amigo.getDocumento(), amigo.getStatus()});
            }
        }
    }

    public static void main(String[] args) {
    	
    	 Connection connection = mySqlDAO.getConnection();
    	 amigosController amigosController = new amigosController(connection);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                amigosGUI amigosGUI = new amigosGUI(amigosController);
               amigosGUI.setVisible(true);
            }
        });
    }
}
