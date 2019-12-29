package com.minsait.sopadeletras.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.minsait.sopadeletras.GUITablero;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUISopaDeLetrasController implements Initializable {

    List<String> palabrasIngresadas = new ArrayList<>(); //Lista que contiene todas las palabras que se ingresarán al tablero
    int tamanioTablero = 7; //estable un valor por defecto del tamaño del tablero
    int numeroPalabras = 6;//estable un valor por defecto de la cantidad de palabras
    int contadorPalabrasAgregadas = 0; //contador global que va incremenetando conforme el usuario ingrese palabras manualmente

    @FXML
    private Label label;
    @FXML
    private JFXRadioButton radioButtonFacil;
    @FXML
    private JFXRadioButton radioButtonMedio;
    @FXML
    private JFXRadioButton radioButtonDificil;
    @FXML
    private JFXComboBox<Integer> comboBoxTamanioTablero;
    @FXML
    private JFXComboBox<Integer> comboBoxNumeroPalabras;
    @FXML
    private JFXCheckBox checkBoxManual;
    @FXML
    private JFXCheckBox checkBoxMixta;
    @FXML
    private JFXCheckBox checkBoxAleatorio;
    @FXML
    private JFXTextField textFieldIngresarPalabra;
    @FXML
    private JFXButton buttonGuardarPalabra;
    @FXML
    private JFXButton button;

    /**
     * Rutina que setea valores iniciales por defecto 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setVisible(false);
        comboBoxTamanioTablero.getItems().addAll(7, 8, 9);
        comboBoxNumeroPalabras.getItems().addAll(6, 7, 8);
        radioButtonFacil.setSelected(true);
        checkBoxAleatorio.setSelected(true);
        textFieldIngresarPalabra.setDisable(true);
        buttonGuardarPalabra.setDisable(true);
    }

    /**
     * Evento que inicializa el proceso de la creación del sopa de letras
     *
     */
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        GUITablero guiTablero = new GUITablero();
        tamanioTablero = comboBoxTamanioTablero.getValue(); //obtiene el valor que ingresó el usuario referente al tamaño del tablero
        numeroPalabras = comboBoxNumeroPalabras.getValue();//obtiene el valor que ingresó el usuario referente al numero de palabras
        guiTablero.crearTablero(tamanioTablero); //crea un tablero con las dimensiones especificadas por el usuario

        if (devolverModoPalabras().equals("al")) {//Si el usuario seleccionó que desea generar las palabras aleatoriamente
            palabrasIngresadas = generarPalabras(numeroPalabras, tamanioTablero);//Se llena la lista con las palabras aleatorias. 
        }

        switch (devolverDificultad()) {
            case "f"://Si el usuario seleccionó el modo fácil
                guiTablero.empezarProceso(palabrasIngresadas, 'f'); //empieza el proceso en modo fácil
                break;
            case "m":
                 guiTablero.empezarProceso(palabrasIngresadas, 'm');
                break;
            case "d":
                 guiTablero.empezarProceso(palabrasIngresadas, 'd');
                break; 
        }
        
        try {
            guiTablero.start(new Stage());
        } catch (Exception ex) {
            Logger.getLogger(GUISopaDeLetrasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        guiTablero.lanzarVista();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    /**
     * Evento que gestiona la creación dinamica de una lista con las palabras
     * que el usuario ingrese
     *
     */
    @FXML
    private void buttonGuardarPalabraEvent(ActionEvent event) {
        //valida que un contador sea menor o igual al numero que el usuario solicitó ingresar manualmete
        if (contadorPalabrasAgregadas <= numeroPalabras) {
            if (!textFieldIngresarPalabra.getText().trim().equals("")) {
                label.setText(textFieldIngresarPalabra.getText() + " añadida");
                palabrasIngresadas.add(textFieldIngresarPalabra.getText());
                contadorPalabrasAgregadas++; // incrementamos el contador
            } else {
                label.setText("Ingrese una palabra válida");
            }
        } else {
            label.setText("Se han agregado todas las palabras");
        }

    }

    /**
     * Método que genera una lista con palabras aleatorias
     *
     * @param numeroPalabras cantidad de palabras a generar
     * @param tamanioTablero el tamaño del tablero que contendrá las palabras
     *
     * @return Devuelve List, con palabras generadas aleatoriamente
     */
    List<String> generarPalabras(int numeroPalabras, int tamanioTablero) throws FileNotFoundException, IOException {
        List<String> listaCompleta = new ArrayList<>();
        List<String> listaAleatoria = new ArrayList<>();
        BufferedReader bf = new BufferedReader(
                new InputStreamReader(new FileInputStream("palabras.txt"), "UTF-8"));
        String sCadena;
        while ((sCadena = bf.readLine()) != null) {
            listaCompleta.add(sCadena);
        }
        for (int i = 0; i < numeroPalabras; i++) {
            int valorEntero = (int) Math.floor(Math.random() * listaCompleta.size());
            if (listaCompleta.get(valorEntero).length() <= tamanioTablero - 2) { //Las palabras que se almacenarán serán del tamaño del tablero - 2
                String cadenaNormalizada = listaCompleta.get(valorEntero);
                cadenaNormalizada = Normalizer.normalize(cadenaNormalizada, Normalizer.Form.NFD);
                cadenaNormalizada = cadenaNormalizada.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                listaAleatoria.add(cadenaNormalizada.toLowerCase()); //Limpia la cadena a minusculas
            } else {
                i--;
            }
        }
        listaAleatoria.sort((s1, s2) -> s2.length() - s1.length()); //Ordena la lista de mayor a menor
        //listaAleatoria.stream().forEach(System.out::println);
        return listaAleatoria;
    }

    /**
     * Método que devuelve la dificultad que seleccionó el usuario
     *
     * @return Devuelve String, indica la dificultad (f=facil, m=medio,
     * d=dificil)
     */
    String devolverDificultad() {
        if (radioButtonFacil.isSelected()) {
            comboBoxTamanioTablero.getItems().clear();
            comboBoxTamanioTablero.getItems().addAll(
                    6, 7, 8
            );
            
            return "f";
        }
        if (radioButtonMedio.isSelected()) {
            comboBoxTamanioTablero.getItems().clear();
            comboBoxTamanioTablero.getItems().addAll(
                    9, 10, 11
            );
            
            return "m";
        }
        if (radioButtonDificil.isSelected()) {
            comboBoxTamanioTablero.getItems().clear();
            comboBoxTamanioTablero.getItems().addAll(
                    12, 13, 14
            );
            
            return "d";
        }
        return null;
    }

    /**
     * Método que devuelve el modo en el que se generarán las palabras del
     * tablero
     *
     * @return Devuelve String, indica la dificultad (ma=manual, mi=mixto,
     * al=aleatorio)
     */
    String devolverModoPalabras() {
        if (checkBoxManual.isSelected()) {
            return "ma";
        }
        if (checkBoxMixta.isSelected()) {
            return "mi";
        }
        if (checkBoxAleatorio.isSelected()) {
            return "al";
        }
        return null;
    }

    @FXML
    private void radioButtonFacilEvent(ActionEvent event) {
        if (devolverDificultad() == null) {
            radioButtonFacil.setSelected(true);
        }
        if (radioButtonMedio.isSelected()) {
            radioButtonMedio.setSelected(false);
        }
        if (radioButtonDificil.isSelected()) {
            radioButtonDificil.setSelected(false);
        }
    }

    @FXML
    private void radioButtonMedioEvent(ActionEvent event) {
        if (devolverDificultad() == null) {
            radioButtonMedio.setSelected(true);
        }
        if (radioButtonFacil.isSelected()) {
            radioButtonFacil.setSelected(false);
        }
        if (radioButtonDificil.isSelected()) {
            radioButtonDificil.setSelected(false);
        }
        devolverDificultad();
    }

    @FXML
    private void radioButtonDificilEvent(ActionEvent event) {
        if (devolverDificultad() == null) {
            radioButtonDificil.setSelected(true);
        }
        if (radioButtonMedio.isSelected()) {
            radioButtonMedio.setSelected(false);
        }
        if (radioButtonFacil.isSelected()) {
            radioButtonFacil.setSelected(false);
        }
        devolverDificultad();
    }

    @FXML
    private void checkBoxManualEvent(ActionEvent event) {
        if (devolverModoPalabras() == null) {
            checkBoxManual.setSelected(true);
        }
        textFieldIngresarPalabra.setDisable(false);
        buttonGuardarPalabra.setDisable(false);
        if (checkBoxMixta.isSelected()) {
            checkBoxMixta.setSelected(false);
        }
        if (checkBoxAleatorio.isSelected()) {
            checkBoxAleatorio.setSelected(false);
        }
    }

    @FXML
    private void checkBoxMixtaEvent(ActionEvent event) {
        if (devolverModoPalabras() == null) {
            checkBoxMixta.setSelected(true);
        }
        textFieldIngresarPalabra.setDisable(false);
        buttonGuardarPalabra.setDisable(false);
        if (checkBoxManual.isSelected()) {
            checkBoxManual.setSelected(false);
        }
        if (checkBoxAleatorio.isSelected()) {
            checkBoxAleatorio.setSelected(false);
        }

    }

    @FXML
    private void checkBoxAleatorioEvent(ActionEvent event) {
        if (devolverModoPalabras() == null) {
            checkBoxAleatorio.setSelected(true);
        }
        textFieldIngresarPalabra.setDisable(true);
        buttonGuardarPalabra.setDisable(true);
        if (checkBoxMixta.isSelected()) {
            checkBoxMixta.setSelected(false);
        }
        if (checkBoxManual.isSelected()) {
            checkBoxManual.setSelected(false);
        }
    }
    
    @FXML
    private void comboBoxTamanioTableroEvent(ActionEvent event) {
        
       if(comboBoxTamanioTablero.getValue()==6){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    4, 5, 6
            );
       }
       if(comboBoxTamanioTablero.getValue()==7){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    5, 6, 7
            );
       }
       if(comboBoxTamanioTablero.getValue()==8){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    6, 7, 8
            );
       }
       if(comboBoxTamanioTablero.getValue()==9){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    7, 8, 9
            );
       }
       if(comboBoxTamanioTablero.getValue()==10){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    8, 9, 10
            );
       }
       if(comboBoxTamanioTablero.getValue()==11){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    9, 10, 11
            );
       }
       if(comboBoxTamanioTablero.getValue()==12){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    10, 11, 12
            );
       }
       if(comboBoxTamanioTablero.getValue()==13){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    11, 12, 13
            );
       }
       if(comboBoxTamanioTablero.getValue()==14){
          comboBoxNumeroPalabras.getItems().clear();
            comboBoxNumeroPalabras.getItems().addAll(
                    12, 13, 14
            );
       }
       
    }
}
