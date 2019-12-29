package com.minsait.sopadeletras.controller;

/**
 *
 * @author Daniel Cruz
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class GUITableroController implements Initializable {

    public char tableroMatriz[][]; //matriz (tablero)
    public GridPane grid; //cuadricula UI tablero

    @FXML
    private BorderPane principalPane;
    @FXML
    private Button botonSalir;
    @FXML
    private Button botonGuardar;
    @FXML
    private Label labelPalabrasABuscar;

    /**
     * Rutina que setea valores iniciales por defecto
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);
        principalPane.setCenter(grid);
    }

    /**
     * Método que inicializa una matriz del tamaño pasado como parámetro
     *
     * @param tamanioTablero tamaño del que se generará el tablero
     *
     */
    public void inicializarTablero(int tamanioTablero) {
        tableroMatriz = new char[tamanioTablero][tamanioTablero]; //inicializa el tablero de consola
        for (int i = 0; i < tamanioTablero; i++) {
            for (int j = 0; j < tamanioTablero; j++) {
                tableroMatriz[i][j] = '-'; //rellena el tablero con guiones medios
                //Label letra = new Label(tableroMatriz[i][j] + "   ");
                //letra.setFont(new Font("Arial", 24));
                //GridPane.setConstraints(letra, j, i);
                //grid.getChildren().addAll(letra);
            }
        }
    }

    /**
     * Método que llena los casillas vacías del tablero (de consola y UI) con
     * letras aleatorias.
     *
     */
    public void rellenaVacios() {
        for (int i = 0; i < tableroMatriz.length; i++) {
            for (int j = 0; j < tableroMatriz.length; j++) {
                Random valorRandom = new Random();
                int valorLetra = (valorRandom.nextInt(25) + 97);
                if (tableroMatriz[i][j] == '-') {
                    tableroMatriz[i][j] = (char) (valorLetra); // llena los espacios del tablero de consola
                    Label letra = new Label(tableroMatriz[i][j] + "   ");
                    letra.setFont(new Font("Arial", 24));
                    GridPane.setConstraints(letra, j, i);
                    grid.getChildren().addAll(letra); // llena los espacios del tablero de la interfaz de usuario
                }
            }
        }
    }

    /**
     * Método imprime en consola una matriz (tablero)
     *
     */
    public void imprimeTablero() {
        for (int i = 0; i < tableroMatriz.length; i++) {
            for (int j = 0; j < tableroMatriz.length; j++) {
                System.out.print(tableroMatriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Método realiza el proceso de situar todas las palabras en el tablero, el
     * proceso varía en función de la dificultas que se pase como parámetro
     *
     * @param dificultad, char que modifica el tipo de movimientos en el que se
     * situarán las palabras
     * @param palabras, lista de palabras que se situarán en el tablero
     *
     * @return boolean true si logró situar las palabras en el tablero false en
     * caso contrario
     */
    public boolean colocarPalabras(char dificultad, List<String> palabras) {
        List<String> palabrasColocadas = new ArrayList<>(); // lista que almacena las palabras que se han logrado colocar en el tablero

        if (dificultad == 'f') { //si el usuario seleccionó modo fácil, solo se intentará colocar las palabras de forma horizontal y vertical
            for (String palabra : palabras) { //for each que recorre toda la lista de palabras que se ingresarán al tablero
                Paint color = Color.color(Math.random(), Math.random(), Math.random()); //Asigna un color aleatorio por cada palabra a colocar
                palabraColocada: //BLOQUE que validará si es posible colocar la palabra en el tablero
                {
                    for (int i = 0; i < tableroMatriz.length; i++) {
                        for (int j = 0; j < tableroMatriz.length; j++) {
                            if (colocarHorizontal(palabra, i, j, color)) {
                                System.out.println(palabra + " colocada horizontalmente en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada; // si se logró colocar la palabra de forma horizontal, salimos de los fors y continua con la siguiente palabra
                            } else if (colocarVertical(palabra, i, j, color)) {
                                System.out.println(palabra + " colocada verticalmente en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada; // si se logró colocar la palabra de forma horizontal, salimos de los fors y continua con la siguiente palabra
                            }
                        }
                    }
                }
            }
        }

        if (dificultad == 'm') {
            for (String palabra : palabras) {
                Paint color = Color.color(Math.random(), Math.random(), Math.random()); //Asigna un color aleatorio por cada palabra a colocar
                palabraColocada: //BLOQUE que validará si es posible colocar la palabra en el tablero
                {
                    for (int i = 0; i < tableroMatriz.length; i++) {
                        for (int j = 0; j < tableroMatriz.length; j++) {
                            /*if (i >= tableroMatriz.length && i >= 0) {
                                if (colocarVerticalInv(palabra, i, j, color)) { //NOTA QUE PRIMERO SE PASA J, DESPUES I
                                    System.out.println(palabra + " colocada diagonalmente en " + i + "," + j);
                                    palabrasColocadas.add(palabra);
                                    break palabraColocada;
                                }else if (j >= palabra.length() && i >= 0) {
                                colocarDiagonalSuperiorDerecha(palabra, i, j, color);
                                System.out.println(palabra + " colocada diagonal superior derecha en" + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada;

                            }*/
                            if (i >= palabra.length() && j >= palabra.length()) {
                                if (colocarDiagonalInferiorDerecha(palabra, i, j, color)) {
                                    System.out.println(palabra + " colocada diagonal inf derecha en " + i + "," + j);
                                    palabrasColocadas.add(palabra);
                                    break palabraColocada;
                                }

                            }
                            /*else if (i >= 0 && j >= tableroMatriz.length) {
                                colocarDiagonalInferiorIzquierda(palabra, i, j, color);
                                System.out.println(palabra + " colocada diagonal inf derecha en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada;
                                
                            } else if (colocarDiagonalSuperiorIzquierda(palabra, j, i, color)) { //NOTA QUE PRIMERO SE PASA J, DESPUES I
                                System.out.println(palabra + " colocada diagonal superior izquierda en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada;

                            } else if (colocarVertical(palabra, i, j, color)) {
                                j = tableroMatriz.length;
                                i = tableroMatriz.length;
                                System.out.println(palabra + " colocada verticalmente en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada; // si se logró colocar la palabra de forma horizontal, salimos de los fors y continua con la siguiente palabra

                            } else if (colocarHorizontal(palabra, i, j, color)) {
                                System.out.println(palabra + " colocada horizontal en " + i + "," + j);
                                palabrasColocadas.add(palabra);
                                break palabraColocada; // si se logró colocar la palabra de forma horizontal, salimos de los fors y continua con la siguiente palabra
                            }*/
                        }
                    }
                }
            }
        }
        labelPalabrasABuscar.setText("Palabras a encontrar: " + palabrasColocadas.toString()); // muestra las palabras que se lograron colocar en el UI
        return true;
    }

    /**
     * Método que situa una letra (carácter) en las coordenas especificadas y le
     * asigna el color mandado como parámetro
     *
     * @param charLetra carácter a colocar
     * @param renglon cordenada y de la matriz en el que se situará el caracter
     * @param columna cordenada x de la matriz en el que se situará el caracter
     * @param color color del que se pintará el carácter en la interfaz grafica
     *
     * @return boolean true si logró situar el carácter false en caso contrario
     */
    boolean colocarCaracter(char charLetra, int renglon, int columna, Paint color) {
        Label letra = new Label(charLetra + "  ");
        letra.setTextFill(color);
        letra.setFont(new Font("Arial", 24));
        GridPane.setConstraints(letra, columna, renglon);
        tableroMatriz[renglon][columna] = charLetra; // situa el carácter en el tablero (de consola)
        grid.getChildren().addAll(letra);// situa el carácter en el tablero (interfaz de usuario)
        return true;

    }

    /**
     * Método que situa una palabra de forma horizontal en una matriz pasada
     * como parámetro.
     *
     * @param palabra palabra a colocar
     * @param posicionRenglonFijo coordenada y de la matriz en el que se
     * comenzará a situar la palabra
     * @param posicionColumnaComienzo coordenada x de la matriz en el que se
     * comenzará a situar la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarHorizontal(String palabra, int posicionRenglonFijo, int posicionColumnaComienzo, Paint color) {
        if (posicionColumnaComienzo + palabra.length() <= tableroMatriz.length) {
            for (int posicionColumnaRecorrido = posicionColumnaComienzo; posicionColumnaRecorrido < palabra.length() + posicionColumnaComienzo; posicionColumnaRecorrido++) {
                if (tableroMatriz[posicionRenglonFijo][posicionColumnaRecorrido] == '-'
                        || tableroMatriz[posicionRenglonFijo][posicionColumnaRecorrido] == palabra.charAt(posicionColumnaRecorrido - posicionColumnaComienzo)) {
                    colocarCaracter(palabra.charAt(posicionColumnaRecorrido - posicionColumnaComienzo), posicionRenglonFijo, posicionColumnaRecorrido, color);
                    return colocarHorizontal(palabra.substring(1), posicionRenglonFijo, posicionColumnaRecorrido + 1, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra horizontalmente en las coordenadas: " + posicionRenglonFijo + "," + posicionColumnaComienzo);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma horizontal en una matriz pasada
     * como parámetro.
     *
     * @param palabra palabra a colocar
     * @param posicionRenglonComienzo coordenada y de la matriz en el que se
     * comenzará a situar la palabra
     * @param posicionColumnaFija coordenada x de la matriz en el que se
     * comenzará a situar la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarVertical(String palabra, int posicionColumnaFija, int posicionRenglonComienzo, Paint color) {
        if (posicionRenglonComienzo + palabra.length() <= tableroMatriz.length) {
            for (int posicionRenglonRecorrido = posicionRenglonComienzo; posicionRenglonRecorrido < palabra.length() + posicionRenglonComienzo; posicionRenglonRecorrido++) {
                if (tableroMatriz[posicionRenglonRecorrido][posicionColumnaFija] == '-'
                        || tableroMatriz[posicionRenglonRecorrido][posicionColumnaFija] == palabra.charAt(posicionRenglonRecorrido - posicionRenglonComienzo)) {
                    colocarCaracter(palabra.charAt(posicionRenglonRecorrido - posicionRenglonComienzo), posicionRenglonRecorrido, posicionColumnaFija, color);
                    return colocarVertical(palabra.substring(1), posicionColumnaFija, posicionRenglonComienzo + 1, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra verticalmente en las coordenadas: " + posicionRenglonComienzo + "," + posicionColumnaFija);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma diagonal (de la esquina superior
     * izquierda a la inferior derecha) en una matriz pasada como parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarDiagonalSuperiorIzquierda(String palabra, int renglon, int columna, Paint color) {
        if (renglon + palabra.length() <= tableroMatriz.length) {
            if (palabra.length() > 0) {
                if (tableroMatriz[renglon][columna] == '-' || tableroMatriz[renglon][columna] == palabra.charAt(0)) {
                    colocarCaracter(palabra.charAt(0), renglon, columna, color);
                    return colocarDiagonalSuperiorIzquierda(palabra.substring(1), renglon += 1, columna += 1, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra verticalmente en las coordenadas: " + renglon + "," + columna);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma diagonal (de la esquina superior
     * derecha a la inferior izquierda) en una matriz pasada como parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarDiagonalSuperiorDerecha(String palabra, int renglon, int columna, Paint color) {

        int r = renglon;

        if (r + palabra.length() <= tableroMatriz.length) {
            if (renglon <= palabra.length()) {
                if (tableroMatriz[renglon][columna] == '-' || tableroMatriz[renglon][columna] == palabra.charAt(0)) {
                    colocarCaracter(palabra.charAt(0), renglon, columna, color);
                    return colocarDiagonalSuperiorDerecha(palabra.substring(1), renglon++, columna--, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra en las coordenadas especificadas" + renglon + "," + columna);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma diagonal inferior (de la esquina
     * inferior derecha a la superior izquierda) en una matriz pasada como
     * parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarDiagonalInferiorDerecha(String palabra, int renglon, int columna, Paint color) {

        int renglonFijo = renglon;

        if (renglonFijo - palabra.length() >= 0) {
            if (renglon > renglonFijo - palabra.length()) {
                if (tableroMatriz[renglon][columna] == '-' || tableroMatriz[renglon][columna] == palabra.charAt(0)) {
                    colocarCaracter(palabra.charAt(0), renglon, columna, color);
                    return colocarDiagonalInferiorDerecha(palabra.substring(1), renglon--, columna--, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra en las coordenadas especificadas " + renglon + "," + columna);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma diagonal inferior(de la esquina
     * inferior izquierda a la superior derecha) en una matriz pasada como
     * parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarDiagonalInferiorIzquierda(String palabra, int renglon, int columna, Paint color) {

        int columnaFija = columna;
        int tamanioPalabra = 0;

        if (columnaFija + palabra.length() <= tableroMatriz.length) {
            if (tamanioPalabra < palabra.length()) {
                if (tableroMatriz[renglon][columna] == '-' || tableroMatriz[renglon][columna] == palabra.charAt(0)) {
                    colocarCaracter(palabra.charAt(0), renglon, columna, color);
                    tamanioPalabra++;
                    return colocarDiagonalInferiorIzquierda(palabra.substring(1), renglon--, columna++, color);
                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra en las coordenadas especificadas" + renglon + "," + columna);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma Horizontal inverso en una matriz
     * pasada como parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarHorizontalInv(String palabra, int renglon, int columna, Paint color) {

        int tamanioPalabra = palabra.length();

        if (columna + palabra.length() <= tableroMatriz.length) {
            for (int posicionColumnaRecorrido = columna; posicionColumnaRecorrido < palabra.length() + columna; posicionColumnaRecorrido++) {
                System.out.println("entra a fors");
                if (tamanioPalabra >= 0) {
                    System.out.println("entre primer if");
                    if (tableroMatriz[renglon][posicionColumnaRecorrido] == '-' || tableroMatriz[renglon][posicionColumnaRecorrido] == palabra.charAt(0)) {
                        colocarCaracter(palabra.charAt(0), renglon, posicionColumnaRecorrido, color);
                        tamanioPalabra--;
                        System.out.println(renglon + " " + posicionColumnaRecorrido-- + " " + tamanioPalabra);

                        return colocarHorizontalInv(palabra.substring(1), renglon, posicionColumnaRecorrido--, color);
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra en las coordenadas especificadas" + renglon + "," + columna);
            return false;
        }
        return true;
    }

    /**
     * Método que situa una palabra de forma Vertical inferior en una matriz
     * pasada como parámetro.
     *
     * @param palabra palabra a colocar
     * @param renglon coordenada y de la matriz en el que se comenzará a situar
     * la palabra
     * @param columna coordenada x de la matriz en el que se comenzará a situar
     * la palabra
     * @param color color del que se pintará la palabra en la interfaz grafica
     *
     * @return boolean true si logró situar la palabra false en caso contrario
     */
    public boolean colocarVerticalInv(String palabra, int posicionColumnaFija, int posicionRenglonComienzo, Paint color) {

        int tamanioPalabra = palabra.length() - 1;

        if (posicionRenglonComienzo + palabra.length() <= tableroMatriz.length) {
            for (int posicionRenglonRecorrido = posicionRenglonComienzo; posicionRenglonRecorrido < palabra.length() + posicionRenglonComienzo; posicionRenglonRecorrido++) {
                if (tamanioPalabra >= 0) {
                    if (tableroMatriz[posicionRenglonRecorrido][posicionColumnaFija] == '-' || tableroMatriz[posicionRenglonRecorrido][posicionColumnaFija] == palabra.charAt(0)) {
                        colocarCaracter(palabra.charAt(0), posicionRenglonRecorrido, posicionColumnaFija, color);
                        tamanioPalabra--;
                        return colocarHorizontalInv(palabra.substring(1), posicionRenglonRecorrido--, posicionColumnaFija, color);
                    } else {
                        return false;
                    }
                }
            }
        } else {
            System.err.println("No se puede colocar la palabra en las coordenadas especificadas" + posicionRenglonComienzo + "," + posicionColumnaFija);
            return false;
        }
        return true;
    }

    @FXML
    private void botonSalirEvent(ActionEvent event) {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void botonGuardarEvent(ActionEvent event) {
        WritableImage image = principalPane.getCenter().snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Imagen");
        // Ajustamos el tipo en el que se guardara la imagen
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        // Muestra el dialog para guardar el archivo
        File file = fileChooser.showSaveDialog(botonGuardar.getScene().getWindow());
        if (file != null) {
            // Corroborar la extension de la imagen
            if (!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                //Arrojar mensaje de error
            }
        }
    }
}
