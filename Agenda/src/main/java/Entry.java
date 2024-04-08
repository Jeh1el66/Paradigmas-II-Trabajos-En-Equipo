import javax.print.attribute.Attribute;
import javax.swing.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;

public class Entry {
    private JTextField nombreField;
    private JFormattedTextField telefonoField;
    private JList<String> listaContactos;
    private DefaultListModel<String> modeloLista;

    private final String FILE_NAME = "contactos.txt";

    public Entry() {
        JFrame frame = new JFrame("Agenda Telefónica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        modeloLista = new DefaultListModel<>();
        listaContactos = new JList<>(modeloLista);

        nombreField = new JTextField(10);
        CharacterLimit(nombreField,13);
        nombreField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateNameColors();
            }
            public void removeUpdate(DocumentEvent e) {
                updateNameColors();
            }
            public void changedUpdate(DocumentEvent e) {
                updateNameColors();
            }
        });

        try {
            MaskFormatter formatter = new MaskFormatter("(###) #######");
            telefonoField = new JFormattedTextField(formatter);
            telefonoField.setColumns(10);
            telefonoField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    updateFieldColors();
                }
                public void removeUpdate(DocumentEvent e) {
                    updateFieldColors();
                }
                public void changedUpdate(DocumentEvent e) {
                    updateFieldColors();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String telefono = telefonoField.getText();
                modeloLista.addElement(nombre + " : " + telefono);
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaContactos.getSelectedIndex()!= -1) {
                    modeloLista.remove(listaContactos.getSelectedIndex());
                }
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveContacts();
                JOptionPane.showMessageDialog(frame, "Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        loadContacts();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Teléfono:"));
        panel.add(telefonoField);
        panel.add(addButton);
        panel.add(deleteButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(listaContactos), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void updateFieldColors(){
        if(isValidNumber(telefonoField.getText())){
            telefonoField.setBackground(Color.WHITE);
            telefonoField.setForeground(Color.RED);
        }else{
            telefonoField.setBackground(Color.RED);
            telefonoField.setForeground(Color.WHITE);
        }
    }

    public void updateNameColors(){
        if(isValidName(nombreField.getText())){
            nombreField.setBackground(Color.WHITE);
            nombreField.setForeground(Color.RED);
        }else{
            nombreField.setBackground(Color.RED);
            nombreField.setForeground(Color.WHITE);
        }
    }


    public boolean isValidNumber(String phoneNumber){
        return phoneNumber.replaceAll("[^*\\d]","").length() == 10;
    }
    public boolean isValidName(String nombre){
        return nombre.matches("[a-zA-Z]{1,13}");
    }

    private void saveContacts(){
        try(PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))){
            for(int i =0; i<modeloLista.size();i++){
                writer.println(modeloLista.getElementAt(i));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts(){
        File file = new File(FILE_NAME);
        if(file.exists()){
            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                String line;
                while((line = reader.readLine()) != null){
                    modeloLista.addElement(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void CharacterLimit(JTextField textField, int limit){
        textField.setDocument(new PlainDocument(){
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
                if(str == null)
                    return;

                if((getLength() + str.length()) <= limit && str.matches("[a-zA-Z]")){
                    super.insertString(offs,str,a);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Entry();
    }
}