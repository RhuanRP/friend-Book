package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JDialog;

import controller.livroController;
import model.livroBean;
import model.mySqlDAO;
import controller.editoraController;
import model.editoraBean;
import model.editoraDAO;

public class livroGUI extends JFrame {
    private JTable livroTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton; 
    private JButton deleteButton; 
    private JButton editButton;
    private livroController livroController;
	protected Connection connection;

    public livroGUI(livroController controller) {
        this.livroController = controller;
        setTitle("Lista de livros");
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
            List<livroBean> livros = livroController.listarTodosLivros(search);
            updateTable(livros);
        });
        
        class CadastroLivroDialog extends JDialog {
			private static final long serialVersionUID = 1L;
			private JTextField tituloField;
            private JComboBox<String> editoraComboBox;
            private JComboBox<String> autorComboBox;
            private livroGUI cadastroLivro = new livroGUI(controller);

            public CadastroLivroDialog(Frame owner, String title, boolean modal) {
                super(owner, title, modal);
                
                Connection connection = mySqlDAO.getConnection();
                editoraController editoraController = new editoraController(connection);
                

                JPanel panel = new JPanel(new GridLayout(4, 2));

                JLabel tituloLabel = new JLabel("Título do Livro:");
                tituloField = new JTextField();
                
                JComboBox<String> editoraComboBox;
                JLabel editoraLabel = new JLabel("Editora");
                editoraComboBox = new JComboBox<>();
                DefaultComboBoxModel editoraComboBoxModel = (DefaultComboBoxModel)editoraComboBox.getModel();
                
                List<editoraBean> editoras = editoraController.listarTodasEditoras("");
                System.out.print(editoras);
                editoras.forEach((editoraBean editora) -> {
                	editoraComboBoxModel.addElement(editora.getRazaoSocial());
                });
                editoraComboBox.setModel(editoraComboBoxModel);

 
                JLabel autorLabel = new JLabel("Autor:");
                autorComboBox = new JComboBox<>();
                
                

                JButton cadastrarButton = new JButton("Cadastrar");
                cadastrarButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String titulo = tituloField.getText();
                        int editora = (int) editoraComboBox.getSelectedItem();
                        int autor = (int) autorComboBox.getSelectedItem();
                        dispose(); 
                    }
                });

                panel.add(tituloLabel);
                panel.add(tituloField);
                panel.add(editoraLabel);
                panel.add(editoraComboBox);
                panel.add(autorLabel);
                panel.add(autorComboBox);
                panel.add(cadastrarButton);

                add(panel);
                setSize(400, 200);
                setLocationRelativeTo(owner);
            }
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (livroController != null) {
                    CadastroLivroDialog dialog = new CadastroLivroDialog(livroGUI.this, "Cadastro de Livro", true);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(livroGUI.this, "Erro: livroController não inicializado.");
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = livroTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) livroTable.getValueAt(selectedRow, 0);
                    boolean success = livroController.marcarComoExcluido(id);
                    
                    if (success) {
                        livroTable.setValueAt("1", selectedRow, 2); 
                    } else {
                        JOptionPane.showMessageDialog(livroGUI.this, "Erro ao marcar o registro como excluído.");
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = livroTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) livroTable.getValueAt(selectedRow, 0);
                    String novoTitulo = JOptionPane.showInputDialog("Novo Título:");
                    if (novoTitulo != null && !novoTitulo.isEmpty()) {
                        String novaEditoraInput = JOptionPane.showInputDialog("Nova Editora:");
                        if (novaEditoraInput != null && !novaEditoraInput.isEmpty()) {
                            int novaEditora = Integer.parseInt(novaEditoraInput);
                            String novoAutorInput = JOptionPane.showInputDialog("Novo Autor:");
                            if (novoAutorInput != null && !novoAutorInput.isEmpty()) {
                                int novoAutor = Integer.parseInt(novoAutorInput);
                                String novoStatus = JOptionPane.showInputDialog("Novo Status:", "Ativo");
                                if (novoStatus != null && !novoStatus.isEmpty()) {
                                    boolean success = livroController.atualizarLivros(id, novoTitulo, novaEditora, novoAutor, novoStatus);
                                    if (success) {
                                        JOptionPane.showMessageDialog(livroGUI.this, "Livro editado com sucesso.");
                                        List<livroBean> livros = livroController.listarTodosLivros("");
                                        updateTable(livros);
                                    } else {
                                        JOptionPane.showMessageDialog(livroGUI.this, "Erro ao atualizar o livro.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(livroGUI.this, "O novo status não pode ser nulo ou vazio.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(livroGUI.this, "O novo Autor não pode ser nulo ou vazio.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(livroGUI.this, "A nova Editora não pode ser nula ou vazia.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(livroGUI.this, "O novo Título não pode ser nulo ou vazio.");
                    }
                }
            }
        });

					
        searchPanel.add(new JLabel("Titulo do livro: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(deleteButton);
        searchPanel.add(editButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Titulo", "Editora", "Autor", "Status"}, 0);
        livroTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(livroTable);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void updateTable(List<livroBean> livroes) {
        tableModel.setRowCount(0);
        for (livroBean livro : livroes) {
            if (!livro.isExcluido()) {
                tableModel.addRow(new Object[]{livro.getId(), livro.getTitulo(), livro.getIdEditora(), livro.getIdAutor(), livro.getStatus()});
            }
        }
    }

    public static void main(String[] args) {
    	
    	 Connection connection = mySqlDAO.getConnection();
    	 livroController livroController = new livroController(connection);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                livroGUI livroGUI = new livroGUI(livroController);
               livroGUI.setVisible(true);
            }
        });
    }
}

