package views;

import javax.swing.*;

import controller.Coordinator;
import model.dto.PetDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class WindowPet extends JFrame implements ActionListener {
    private JTextField txtDocument, txtRace, txtName;
    private JButton btnRegister, btnQuery, btnUpdate, btnDelete, btnQueryList, btnNext;
    private JTable tablaNombres;
    private JLabel lblSeleccion;
    private JScrollPane scrollTabla;
    private Coordinator myCoordinator;
    private ArrayList<PetDTO> petOfOwner;
    private JComboBox<String> sex;
    private int currentIndex = 0;

    public WindowPet() {
        setTitle("Gestionar Mascotas");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Mascotas", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(80, 10, 250, 30);
        add(lblTitulo);

        JLabel lblDocument = new JLabel("ID Dueño:");
        lblDocument.setBounds(20, 50, 80, 25);
        add(lblDocument);

        txtDocument = new JTextField();
        txtDocument.setBounds(100, 50, 100, 25);
        add(txtDocument);

        JLabel lblRace = new JLabel("Raza:");
        lblRace.setBounds(210, 50, 60, 25);
        add(lblRace);

        txtRace = new JTextField();
        txtRace.setBounds(270, 50, 100, 25);
        add(txtRace);

        JLabel lblName = new JLabel("Nombre:");
        lblName.setBounds(20, 85, 80, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(100, 85, 100, 25);
        add(txtName);

        JLabel lblSex = new JLabel("Sexo:");
        lblSex.setBounds(210, 85, 80, 25);
        add(lblSex);

        sex = new JComboBox<>(new String[] { "Macho", "Hembra" });
        sex.setBounds(270, 85, 100, 25);
        add(sex);

        btnRegister = new JButton("Registrar");
        btnRegister.setBounds(40, 120, 120, 30);
        add(btnRegister);
        btnRegister.addActionListener(this);

        btnQuery = new JButton("Consultar");
        btnQuery.setBounds(200, 120, 120, 30);
        add(btnQuery);
        btnQuery.addActionListener(this);

        btnUpdate = new JButton("Actualizar");
        btnUpdate.setBounds(40, 160, 120, 30);
        add(btnUpdate);
        btnUpdate.addActionListener(this);

        btnDelete = new JButton("Eliminar");
        btnDelete.setBounds(200, 160, 120, 30);
        add(btnDelete);
        btnDelete.addActionListener(this);

        btnQueryList = new JButton("ConsultarLista");
        btnQueryList.setBounds(40, 200, 280, 30);
        add(btnQueryList);
        btnQueryList.addActionListener(this);

        tablaNombres = new JTable();
        scrollTabla = new JScrollPane(tablaNombres);
        scrollTabla.setBounds(20, 240, 350, 100);
        add(scrollTabla);
        lblSeleccion = new JLabel();
        lblSeleccion.setBounds(260, 270, 200, 20);
        add(lblSeleccion);

        btnNext = new JButton("Siguiente");
        btnNext.setBounds(40, 350, 280, 30);
        btnNext.setEnabled(false);
        btnNext.addActionListener(this);
        add(btnNext);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            try {
                registerPet();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnQuery) {
            queryPet();
        }
        if (e.getSource() == btnUpdate) {
            try {
                updatePet();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnDelete) {
            deletePet();
        }
        if (e.getSource() == btnQueryList) {
            try {
                queryPetsList();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnNext) {
            if (petOfOwner != null && currentIndex + 1 < petOfOwner.size()) {
                currentIndex++;
                showPet(petOfOwner.get(currentIndex));
                if (currentIndex == petOfOwner.size() - 1) {
                    btnNext.setEnabled(false);
                }
            }
        }
    }

    private boolean registerPet() throws SQLException {
        if (!valFields()) {
            return false;
        }
        PetDTO pet = new PetDTO();
        pet.setIdOwner(txtDocument.getText());
        pet.setName(txtName.getText());
        pet.setRace(txtRace.getText());
        pet.setSex(sex.getSelectedItem().toString());
        boolean res = myCoordinator.registerPet(pet);
        if (res) {
            JOptionPane.showMessageDialog(this, "Pet successfully registered.");
        }
        return res;
    }

    private void queryPet() {
        try {
            String idowner = txtDocument.getText();
            petOfOwner = myCoordinator.queryPets(idowner);
            currentIndex = 0;

            if (petOfOwner != null && !petOfOwner.isEmpty()) {
                showPet(petOfOwner.get(currentIndex));
                btnNext.setEnabled(petOfOwner.size() > 1);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron mascotas.");
                btnNext.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en la consulta.");
        }
    }

    private void updatePet() throws SQLException {
        int selectedRow = tablaNombres.getSelectedRow();
        if (selectedRow >= 0 && petOfOwner != null && selectedRow < petOfOwner.size()) {
            PetDTO currentPet = petOfOwner.get(selectedRow);
            currentPet.setName(txtName.getText());
            currentPet.setRace(txtRace.getText());
            currentPet.setSex(sex.getSelectedItem().toString());
            String originalName = (String) tablaNombres.getValueAt(selectedRow, 2);
            String res = myCoordinator.updatePet(currentPet, originalName);
            if ("ok".equals(res)) {
                JOptionPane.showMessageDialog(this, "Pet successfully updated.");
                queryPetsList();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating pet.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a pet to update.");
        }
    }

   private void deletePet() {
    int selectedRow = tablaNombres.getSelectedRow();
    if (selectedRow >= 0 && petOfOwner != null && selectedRow < petOfOwner.size()) {
        PetDTO pet = petOfOwner.get(selectedRow);
        String resp = myCoordinator.deletePet(pet.getIdOwner(), pet.getName());
        if ("ok".equals(resp)) {
            JOptionPane.showMessageDialog(this, "Pet successfully deleted.");
            try {
                queryPetsList();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating pet list.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting pet.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Select a pet to delete.");
    }
}

    private void queryPetsList() throws SQLException {
        petOfOwner = myCoordinator.queryPets(null);
        if (petOfOwner.size() > 0) {
            fillTable(petOfOwner);
        } else {
            lblSeleccion.setText("No hay mascotas registradas");
        }
    }

    private void fillTable(ArrayList<PetDTO> petsList) {
        String titulos[] = { "Nombre_Dueño", "Documento_Dueño", "Nombre", "Raza", "Sexo" };
        String info[][] = new String[petsList.size()][5];

        for (int x = 0; x < petsList.size(); x++) {
            info[x][0] = petsList.get(x).getNameOwner();
            info[x][1] = petsList.get(x).getIdOwner();
            info[x][2] = petsList.get(x).getName();
            info[x][3] = petsList.get(x).getRace();
            info[x][4] = petsList.get(x).getSex();
        }

        tablaNombres = new JTable(info, titulos);
        scrollTabla.setViewportView(tablaNombres);
        int[] anchos = { 100, 100, 100, 100, 50 };
        for (int i = 0; i < tablaNombres.getColumnCount(); i++) {
            tablaNombres.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private boolean valFields() {
        boolean camposValidos = true;

        String documento = txtDocument.getText().trim();
        if (documento.isEmpty()) {
            txtDocument.setBorder(BorderFactory.createLineBorder(Color.RED));
            camposValidos = false;
        } else if (!documento.matches("\\d+")) {
            txtDocument.setBorder(BorderFactory.createLineBorder(Color.RED));
            camposValidos = false;
            JOptionPane.showMessageDialog(this, "El ID del dueño debe contener solo números.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } else {
            txtDocument.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (txtName.getText().trim().isEmpty()) {
            txtName.setBorder(BorderFactory.createLineBorder(Color.RED));
            camposValidos = false;
        } else {
            txtName.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (txtRace.getText().trim().isEmpty()) {
            txtRace.setBorder(BorderFactory.createLineBorder(Color.RED));
            camposValidos = false;
        } else {
            txtRace.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (!camposValidos) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.", "Campos inválidos", JOptionPane.WARNING_MESSAGE);
        }

        return camposValidos;
    }

    private void showPet(PetDTO pet) {
        txtDocument.setText(pet.getIdOwner());
        txtName.setText(pet.getName());
        txtRace.setText(pet.getRace());
        sex.setSelectedItem(pet.getSex());
    }

    public void setCoordinator(Coordinator coordinator) {
        this.myCoordinator = coordinator;
    }
}