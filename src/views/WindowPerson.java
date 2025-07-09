package views;

import javax.swing.*;

import controller.Coordinator;
import model.dto.PersonDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class WindowPerson extends JFrame implements ActionListener {
    private JTextField txtDocument, txtName, txtNumberPhone;
    private JButton btnRegister, btnQuery, btnUpdate, btnDelete, btnQueryList;
    private Coordinator myCoordinator;
    private JTable tablesName;
    private JScrollPane scrollTabla;

    public WindowPerson() {
        setTitle("Gestionar Personas");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Personas", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(80, 10, 250, 30);
        add(lblTitulo);

        JLabel lblDocumento = new JLabel("Documento:");
        lblDocumento.setBounds(20, 50, 80, 25);
        add(lblDocumento);

        txtDocument = new JTextField();
        txtDocument.setBounds(100, 50, 100, 25);
        add(txtDocument);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(210, 50, 60, 25);
        add(lblTelefono);

        txtNumberPhone = new JTextField();
        txtNumberPhone.setBounds(270, 50, 100, 25);
        add(txtNumberPhone);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 85, 80, 25);
        add(lblNombre);

        txtName = new JTextField();
        txtName.setBounds(100, 85, 270, 25);
        add(txtName);

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

        tablesName = new JTable();
        scrollTabla = new JScrollPane(tablesName);
        scrollTabla.setBounds(20, 240, 350, 100);
        add(scrollTabla);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            try {
                registerPersons();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnQuery) {
            try {
                queryPerson();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnQueryList) {
            queryPersonsList();
        }
        if (e.getSource() == btnUpdate) {
            try {
                updatePerson();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == btnDelete) {
            try {
                deletePerson();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean validateFields() {
        boolean validFields = true;

        if (txtDocument.getText().trim().isEmpty()) {
            txtDocument.setBorder(BorderFactory.createLineBorder(Color.RED));
            validFields = false;
        } else if (!txtDocument.getText().matches("\\d+")) {
            txtDocument.setBorder(BorderFactory.createLineBorder(Color.RED));
            validFields = false;
            JOptionPane.showMessageDialog(this, "Solo se admiten números.", "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            txtDocument.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (txtName.getText().trim().isEmpty()) {
            txtName.setBorder(BorderFactory.createLineBorder(Color.RED));
            validFields = false;
        } else {
            txtName.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
        String numberPhone = txtNumberPhone.getText().trim();
        if (txtNumberPhone.getText().trim().isEmpty()) {
            txtNumberPhone.setBorder(BorderFactory.createLineBorder(Color.RED));
            validFields = false;
        } else if (!numberPhone.matches("\\d{10}")) {
            txtNumberPhone.setBorder(BorderFactory.createLineBorder(Color.RED));
            validFields = false;
            JOptionPane.showMessageDialog(this, "El teléfono debe tener 10 dígitos numéricos.", "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            txtNumberPhone.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (!validFields) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
        }

        return validFields;
    }

    private boolean registerPersons() throws SQLException {
        if (!validateFields()) {
            return false;
        }
        PersonDTO person = new PersonDTO();

        person.setDocument(txtDocument.getText());
        person.setNumberPhone(txtNumberPhone.getText());
        person.setName(txtName.getText());
        boolean res = myCoordinator.registerPerson(person);
        if (res) {
            JOptionPane.showMessageDialog(this, "Registro éxitoso.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrarse.");
        }
        return res;
    }

    private void queryPerson() throws SQLException {
        PersonDTO myPerson = myCoordinator.queryPerson(txtDocument.getText());

        if (myPerson != null) {
            txtName.setText(myPerson.getName());
            txtNumberPhone.setText(myPerson.getNumberPhone());
        } else {
            JOptionPane.showMessageDialog(null, "NO SE ENCUENTRA LA PERSONA", "NO REGISTRA", JOptionPane.ERROR_MESSAGE);
            txtName.setText("");
            txtNumberPhone.setText("");
        }
    }

    private void queryPersonsList() {
        ArrayList<PersonDTO> personsList = myCoordinator.queryPersonsList();
        if (personsList.size() > 0) {
            fillTable(personsList);
        }
    }

    private void fillTable(ArrayList<PersonDTO> personsList) {
        String titulos[] = { "Documento", "Nombre", "Teléfono" };
        String info[][] = new String[personsList.size()][3];

        for (int x = 0; x < info.length; x++) {
            info[x][0] = personsList.get(x).getDocument();
            info[x][1] = personsList.get(x).getName();
            info[x][2] = personsList.get(x).getNumberPhone();
        }

        tablesName = new JTable(info, titulos);

        int[] width = { 100, 150, 150 };
        for (int i = 0; i < tablesName.getColumnCount(); i++) {
            tablesName.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
        }

        scrollTabla.setViewportView(tablesName);

        scrollTabla.revalidate();
        scrollTabla.repaint();
    }

    private void updatePerson() throws SQLException {
        if (!validateFields()) {
            return;
        }
        PersonDTO newPerson = myCoordinator.queryPerson(txtDocument.getText());
        newPerson.setName(txtName.getText());
        newPerson.setNumberPhone(txtNumberPhone.getText());

        String res = myCoordinator.updatePerson(newPerson);

        if (res.equals("ok")) {
            JOptionPane.showMessageDialog(null, "Se actualiza exitosamente", "Actualizado",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePerson() throws SQLException {
        String res = myCoordinator.deletePerson(txtDocument.getText());
        if (res.equals("ok")) {
            JOptionPane.showMessageDialog(null, "Se elimina exitosamente", "Eliminado", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCoordinator(Coordinator myCoordinator) {
        this.myCoordinator = myCoordinator;
    }
}