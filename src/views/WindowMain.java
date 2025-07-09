package views;

import javax.swing.*;

import controller.Coordinator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowMain extends JFrame implements ActionListener{
	private Coordinator myCoordinator;
    private JButton btnManagerPerson, btnManagerPets;
    private JLabel lblImagen;

    public WindowMain() {
        setTitle("Veterinaria ABC");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("VETERINARIA ABC", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(100, 10, 300, 30);
        add(lblTitulo);

        iniciarComponentes();
        
    
    }

    private void iniciarComponentes() {

        lblImagen = new JLabel();
        lblImagen.setBounds(50,70,400,330);
        lblImagen.setIcon(new ImageIcon(getClass().
        		getResource("/image/background.png")));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        

        btnManagerPerson = new JButton("Gestionar Personas");
        btnManagerPerson.setBounds(80, 280, 150, 30);
        add(btnManagerPerson);
        btnManagerPerson.addActionListener(this);

        btnManagerPets = new JButton("Gestionar Mascotas");
        btnManagerPets.setBounds(260, 280, 150, 30);
        add(btnManagerPets);
        add(lblImagen);
        btnManagerPets.addActionListener(this);
	}

	public void setCoordinator(Coordinator myCoordinator) {
		this.myCoordinator=myCoordinator;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnManagerPerson) {
			myCoordinator.showWindowPersons();
		}
		if(e.getSource()==btnManagerPets) {
			myCoordinator.showWindowPets();
		}
		
	}
}