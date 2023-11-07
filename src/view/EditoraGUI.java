package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

import controller.editoraController;
import model.editoraBean;
import model.livroBean;
import model.mySqlDAO;

public class EditoraGUI extends JFrame {
    private JTable editoraTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton; 
    private JButton deleteButton; 
    private JButton editButton;
    private boolean showInativos = false;
    private editoraController editoraController;
	protected Connection connection;
	
	public EditoraGUI(editoraController controller, inicialGUI telaInicial) {

		addWindowListener(new WindowAdapter() {
        	@Override
            public void windowClosing(WindowEvent e) {
                setVisible(false); 
                telaInicial.setVisible(true); 
            }
        });
    }

    public EditoraGUI(editoraController controller) {
        this.editoraController = controller;
        setTitle("Lista de Editoras");
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
        	List<editoraBean> editoras = editoraController.listarTodasEditoras(search);
            updateTable(editoras);
        });
        
        String[] statusOptions = {"Ativo", "Inativo"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String razaoSocial = JOptionPane.showInputDialog("Informe a Razão Social:");
                if (razaoSocial != null && !razaoSocial.isEmpty()) {
                    JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
                    int option = JOptionPane.showConfirmDialog(EditoraGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String status = (String) statusComboBox.getSelectedItem();
                        boolean success = editoraController.adicionarEditora(razaoSocial, status);
                        if (success) {
                            JOptionPane.showMessageDialog(EditoraGUI.this, "Nova editora adicionada com sucesso.");
                            List<editoraBean> editoras = editoraController.listarTodasEditoras("");
                            updateTable(editoras);
                        } else {
                            JOptionPane.showMessageDialog(EditoraGUI.this, "Erro ao adicionar a nova editora.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(EditoraGUI.this, "Você cancelou a adição.");
                    }
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = editoraTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) editoraTable.getValueAt(selectedRow, 0);
                    boolean success = editoraController.marcarComoExcluido(id);
                    
                    if (success) {
                        editoraTable.setValueAt("1", selectedRow, 2); 
                    } else {
                        JOptionPane.showMessageDialog(EditoraGUI.this, "Erro ao marcar o registro como excluído.");
                    }
                }
            }
        });

        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = editoraTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) editoraTable.getValueAt(selectedRow, 0);
                    String novoNome = (String) JOptionPane.showInputDialog("Novo Nome:");
                    if (novoNome != null && !novoNome.isEmpty()) {
                        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
                        int option = JOptionPane.showConfirmDialog(EditoraGUI.this, statusComboBox, "Selecione o status:", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String novoStatus = (String) statusComboBox.getSelectedItem();
                            boolean success = editoraController.atualizarEditora(id, novoNome, novoStatus);
                            if (success) {
                                JOptionPane.showMessageDialog(EditoraGUI.this, "Editora atualizada com sucesso.");
                                List<editoraBean> editoras = editoraController.listarTodasEditoras("");
                                updateTable(editoras);
                            } else {
                                JOptionPane.showMessageDialog(EditoraGUI.this, "Erro ao atualizar a editora.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(EditoraGUI.this, "Você cancelou a edição.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(EditoraGUI.this, "O novo nome não pode ser nulo ou vazio.");
                    }
                }
            }
        });
				
        searchPanel.add(new JLabel("Nome da Editora: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(deleteButton);
        searchPanel.add(editButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Razao Social", "Status"}, 0);
        editoraTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(editoraTable);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JCheckBox showInativosCheckBox = new JCheckBox("Mostrar Inativos");
        searchPanel.add(showInativosCheckBox);
				
        showInativosCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean showInativos = showInativosCheckBox.isSelected();
                if (showInativos) {
                    List<editoraBean> editora = editoraController.mostrarInativos();
                    updateTable(editora);
                } else {
                    List<editoraBean> editora = editoraController.listarTodasEditoras("");
                    updateTableInativos(editora);
                }
            }
        });
    }

	private void updateTable(List<editoraBean> editoras) {
        tableModel.setRowCount(0);
        for (editoraBean editora : editoras) {
            if (!editora.isExcluido()) {
                tableModel.addRow(new Object[]{editora.getId(), editora.getRazaoSocial(), editora.getStatus()});
            }
        }
    }
	
	private void updateTableInativos(List<editoraBean> editoras) {
        tableModel.setRowCount(0);
        for (editoraBean editora : editoras) {
            if (!editora.isExcluido() && (showInativos || !editora.getStatus().equalsIgnoreCase("Inativo"))) {
                tableModel.addRow(new Object[]{editora.getId(), editora.getRazaoSocial(), editora.getStatus()});
            }
        }
    }

	public List<editoraBean> searchEditora() {
		String search = searchField.getText();
        List<editoraBean> editoras = editoraController.listarTodasEditoras("");
        return editoras;
	}

    public static void main(String[] args) {
    	
    	 Connection connection = mySqlDAO.getConnection();
    	 editoraController editoraController = new editoraController(connection);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EditoraGUI editoraGUI = new EditoraGUI(editoraController);
                editoraGUI.setVisible(true);
            }
        });
    }
}
