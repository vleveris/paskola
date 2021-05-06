package paskola;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import paskola.vaizdavimas.lentele;

import java.math.BigDecimal;

public class Main extends Application {
    /**
     * @author Vytautas Leveris, VU MIF PS 1 k., 5 gr.
     */

    RadioButton lin = new RadioButton("Linijinis");
    RadioButton anuitet = new RadioButton("Anuitetinis");
    private int menSk;
    private double metinisPr;
    private double sumaValue;
    private String error;
    private final TextField sumaEdit = new TextField();
    private final TextField metEdit = new TextField();
    private final TextField menEdit = new TextField();
    private final TextField procEdit = new TextField();
    private final CheckBox atostogos = new CheckBox("Mokejimo atostogos");
    private final CheckBox atidetiPalukanas = new CheckBox("Atideti palukanas (max. 1/3 laikotarpio)");
    private final RadioButton visi = new RadioButton("Rodyti visu menesiu mokejimus");
    private final RadioButton kaiKurie = new RadioButton("Pasirinkti rodymo intervala");
    private final TextField atostogosNuoEdit = new TextField();
    private final TextField atostogosIkiEdit = new TextField();
    private final TextField palNuoEdit = new TextField();
    private final TextField palIkiEdit = new TextField();
    private final TextField palPalEdit = new TextField();
    private int atNuo = -1;
    private int atIki = -1;
    private int palNuo = -1;
    private int palIki = -1;
    private double palPal = 0;
    private final TextField kaiKurieNuoEdit = new TextField();
    private final TextField kaiKurieIkiEdit = new TextField();
    private int kaiNuo = 1;
    private int kaiIki;
    private boolean Anuitet;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Text suma = new Text("Suma: ");
        Text metai = new Text("Met\u0173 skai\u010dius: ");
        Text men = new Text("Menesi\u0173 skai\u010dius: ");
        Text proc = new Text("Metin\u0117s pal\u016bkanos: ");
        Text atostogosNuo = new Text("Nuo (m\u0117n.): ");
        Text atostogosIki = new Text("Iki (m\u0117n.): ");
        Text palukanosNuo = new Text("Nuo (m\u0117n.): ");
        Text palukanosIki = new Text("Iki (m\u0117n.): ");
        Text palukanosPal = new Text("Naujos pal\u016bkanos: ");
        atostogosNuoEdit.setVisible(false);
        atostogosIkiEdit.setVisible(false);
        atostogosNuo.setVisible(false);
        atostogosIki.setVisible(false);
        palNuoEdit.setVisible(false);
        palIkiEdit.setVisible(false);
        palPalEdit.setVisible(false);
        palukanosNuo.setVisible(false);
        palukanosIki.setVisible(false);
        palukanosPal.setVisible(false);
        Button mygtukas = new Button("_Skai\u010diuoti");
        ToggleGroup group = new ToggleGroup();
        anuitet.setToggleGroup(group);
        lin.setToggleGroup(group);
        ToggleGroup menRodymas = new ToggleGroup();
        visi.setToggleGroup(menRodymas);
        kaiKurie.setToggleGroup(menRodymas);
        Text kaiKurieNuo = new Text("Nuo (m\u0117n.): ");
        Text kaiKurieIki = new Text("Iki (m\u0117n.): ");
        kaiKurieNuo.setVisible(false);
        kaiKurieNuoEdit.setVisible(false);
        kaiKurieIki.setVisible(false);
        kaiKurieIkiEdit.setVisible(false);
        Button failas = new Button("\u012era\u0161yti \u012f _fail\u0105");
        Button valyti = new Button("_Valyti visus laukus");
        valyti.setOnAction(e -> {
            atostogos.setSelected(false);
            visi.setSelected(true);
            atidetiPalukanas.setSelected(false);
            sumaEdit.setText("");
            menEdit.setText("");
            metEdit.setText("");
            procEdit.setText("");
            lin.setSelected(true);
            atostogosNuoEdit.setText("");
            atostogosIkiEdit.setText("");
            palNuoEdit.setText("");
            palIkiEdit.setText("");
            palPalEdit.setText("");
            kaiKurieNuoEdit.setText("");
            kaiKurieIkiEdit.setText("");
        });
        Button grafikas = new Button("Rodyti _grafik\u0105");
        grafikas.setOnAction(e -> {
            boolean teisingiLaukai = checkValidity();
            if (!teisingiLaukai)
                new rodytiInfo(error).isvesti();
            else {
                paskola Paskola = new paskola(new BigDecimal(metinisPr), menSk, new BigDecimal(sumaValue), Anuitet);
                lentele Lentele = new lentele(Paskola, atNuo, atIki, palNuo, palIki, new BigDecimal(palPal), kaiNuo, kaiIki, stage);
                Lentele.rodytiGrafika();
            }
        });
        failas.setOnAction(e -> {
            boolean teisingiLaukai = checkValidity();
            if (!teisingiLaukai)
                new rodytiInfo(error).isvesti();
            else {
                paskola Paskola = new paskola(new BigDecimal(metinisPr), menSk, new BigDecimal(sumaValue), Anuitet);
                lentele Lentele = new lentele(Paskola, atNuo, atIki, palNuo, palIki, new BigDecimal(palPal), kaiNuo, kaiIki, stage);
                Lentele.uzpildytiLentele(true, false);
            }
        });
        mygtukas.setOnAction(e -> {
            boolean teisingiLaukai = checkValidity();
            if (!teisingiLaukai)
                new rodytiInfo(error).isvesti();
            else {
                paskola Paskola = new paskola(new BigDecimal(metinisPr), menSk, new BigDecimal(sumaValue), Anuitet);
                lentele Lentele = new lentele(Paskola, atNuo, atIki, palNuo, palIki, new BigDecimal(palPal), kaiNuo, kaiIki, stage);
                Lentele.rodyti();
            }
        });
        GridPane gridas = new GridPane();
        gridas.setMinSize(400, 200);
        gridas.setPadding(new Insets(10, 10, 10, 10));
        gridas.setVgap(5);
        gridas.setHgap(5);
        gridas.setAlignment(Pos.CENTER);
        gridas.add(suma, 0, 0);
        gridas.add(sumaEdit, 1, 0);
        sumaEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{1,8}([.]\\d{0,2})?)?"))
                sumaEdit.setText(oldValue);
        });
        gridas.add(metai, 0, 1);
        gridas.add(metEdit, 1, 1);
        metEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,2})?"))
                metEdit.setText(oldValue);
        });
        gridas.add(men, 0, 2);
        gridas.add(menEdit, 1, 2);
        menEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                menEdit.setText(oldValue);
        });
        gridas.add(proc, 0, 3);
        gridas.add(procEdit, 1, 3);
        procEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{1,8}([.]\\d{0,8})?)?"))
                procEdit.setText(oldValue);
        });
        gridas.add(lin, 0, 4);
        gridas.add(anuitet, 1, 4);
        lin.setSelected(true);
        gridas.add(atostogos, 0, 5);
        atostogos.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                atostogosNuo.setVisible(true);
                atostogosNuoEdit.setVisible(true);
                atostogosIki.setVisible(true);
                atostogosIkiEdit.setVisible(true);
            } else {
                atostogosNuo.setVisible(false);
                atostogosNuoEdit.setVisible(false);
                atostogosIki.setVisible(false);
                atostogosIkiEdit.setVisible(false);
                atNuo = -1;
                atIki = -1;
            }
        });
        gridas.add(atostogosNuo, 1, 5);
        gridas.add(atostogosNuoEdit, 2, 5);
        atostogosNuoEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                atostogosNuoEdit.setText(oldValue);
        });
        gridas.add(atostogosIki, 3, 5);
        gridas.add(atostogosIkiEdit, 4, 5);
        atostogosIkiEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                atostogosIkiEdit.setText(oldValue);
        });
        gridas.add(atidetiPalukanas, 0, 6);
        atidetiPalukanas.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                palukanosNuo.setVisible(true);
                palNuoEdit.setVisible(true);
                palukanosIki.setVisible(true);
                palIkiEdit.setVisible(true);
                palukanosPal.setVisible(true);
                palPalEdit.setVisible(true);
            } else {
                palukanosNuo.setVisible(false);
                palNuoEdit.setVisible(false);
                palukanosIki.setVisible(false);
                palIkiEdit.setVisible(false);
                palukanosPal.setVisible(false);
                palPalEdit.setVisible(false);
                palNuo = -1;
                palIki = -1;
                palPal = 0;
            }
        });
        gridas.add(palukanosNuo, 1, 6);
        gridas.add(palNuoEdit, 2, 6);
        palNuoEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                palNuoEdit.setText(oldValue);
        });
        gridas.add(palukanosIki, 3, 6);
        gridas.add(palIkiEdit, 4, 6);
        palIkiEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                palIkiEdit.setText(oldValue);
        });
        gridas.add(palukanosPal, 5, 6);
        gridas.add(palPalEdit, 6, 6);
        palPalEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{1,8}([\\.]\\d{0,8})?)?"))
                palPalEdit.setText(oldValue);
        });
        gridas.add(visi, 0, 7);
        gridas.add(kaiKurie, 1, 7);
        kaiKurie.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                kaiKurieNuo.setVisible(true);
                kaiKurieNuoEdit.setVisible(true);
                kaiKurieIki.setVisible(true);
                kaiKurieIkiEdit.setVisible(true);
            } else {
                kaiNuo = 1;
                kaiKurieNuo.setVisible(false);
                kaiKurieNuoEdit.setVisible(false);
                kaiKurieIki.setVisible(false);
                kaiKurieIkiEdit.setVisible(false);
            }
        });
        visi.setSelected(true);
        gridas.add(kaiKurieNuo, 2, 7);
        gridas.add(kaiKurieNuoEdit, 3, 7);
        kaiKurieNuoEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                kaiKurieNuoEdit.setText(oldValue);
        });
        gridas.add(kaiKurieIki, 4, 7);
        gridas.add(kaiKurieIkiEdit, 5, 7);
        kaiKurieIkiEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{0,4})?"))
                kaiKurieIkiEdit.setText(oldValue);
        });
        gridas.add(mygtukas, 1, 10);
        gridas.add(failas, 2, 10);
        gridas.add(grafikas, 3, 10);
        gridas.add(valyti, 4, 10);
        mygtukas.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        failas.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        grafikas.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        metai.setStyle("-fx-font: normal bold 20px 'serif' ");
        men.setStyle("-fx-font: normal bold 20px 'serif' ");
        proc.setStyle("-fx-font: normal bold 20px 'serif' ");
        atostogosNuo.setStyle("-fx-font: normal bold 20px 'serif' ");
        atostogosIki.setStyle("-fx-font: normal bold 20px 'serif' ");
        palukanosNuo.setStyle("-fx-font: normal bold 20px 'serif' ");
        palukanosIki.setStyle("-fx-font: normal bold 20px 'serif' ");
        palukanosPal.setStyle("-fx-font: normal bold 20px 'serif' ");
        kaiKurieNuo.setStyle("-fx-font: normal bold 20px 'serif' ");
        kaiKurieIki.setStyle("-fx-font: normal bold 20px 'serif' ");
        gridas.setStyle("-fx-background-color: BEIGE;");
        Scene scene = new Scene(gridas);
        stage.setTitle("Paskol\u0173 skai\u010diuotuvas");
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkValidity() {
        Anuitet = anuitet.isSelected();
        String valueString = sumaEdit.getText();
        int taskas = valueString.indexOf(".");
        if (taskas > -1)
            if (valueString.length() - 1 == taskas) {
                valueString = valueString.substring(0, valueString.length() - 1);
                sumaEdit.setText(valueString);
            }
        valueString = procEdit.getText();
        taskas = valueString.indexOf(".");
        if (taskas > -1)
            if (valueString.length() - 1 == taskas) {
                valueString = valueString.substring(0, valueString.length() - 1);
                procEdit.setText(valueString);
            }
        if (sumaEdit.getText().length() == 0) {
            error = "Sumos laukas tu\u0161\u010dias. ";
            return false;
        }
        sumaValue = Double.parseDouble(sumaEdit.getText());
        if (procEdit.getText().length() == 0) {
            error = "Procent\u0173 laukas tu\u0161\u010dias. ";
            return false;
        }
        metinisPr = Double.parseDouble(procEdit.getText());
        menSk = 0;
        if (metEdit.getText().length() != 0)
            menSk = Integer.parseInt(metEdit.getText()) * 12;
        if (menEdit.getText().length() != 0)
            menSk += Integer.parseInt(menEdit.getText());
        if (menSk == 0) {
            error = "Nulinis ir/ar neigiamas laikotarpis nepriimtinas.";
            return false;
        }
        int met, men;
        met = menSk / 12;
        men = menSk % 12;
        metEdit.setText(Integer.toString(met));
        menEdit.setText(Integer.toString(men));
        kaiIki = menSk;
        if (atostogos.isSelected()) {
            if (atostogosNuoEdit.getText().length() != 0)
                atNuo = Integer.parseInt(atostogosNuoEdit.getText());
            else {
                error = "Nenurodytas atostog\u0173 prad\u017eios m\u0117nesis.";
                return false;
            }
            if (atostogosIkiEdit.getText().length() != 0)
                atIki = Integer.parseInt(atostogosIkiEdit.getText());
            else {
                error = "Nenurodytas atostog\u0173 pabaigos m\u0117nesis.";
                return false;
            }
            if (atNuo > atIki || atNuo == 0) {
                error = "Klaidingai nurodytos atostogos.";
                return false;
            }
            if (menSk - (atIki - atNuo + 1) < 1) {
                error = "Atostogos negali vir\u0161yti ar b\u016bti lygios mok\u0117jimo laikotarpiui.";
                return false;
            }
        }
        if (atidetiPalukanas.isSelected()) {
            if (palNuoEdit.getText().length() != 0)
                palNuo = Integer.parseInt(palNuoEdit.getText());
            else {
                error = "Nenurodytas pal\u016bkan\u0173 atid\u0117jimo prad\u017eios m\u0117nesis.";
                return false;
            }
            if (palIkiEdit.getText().length() != 0)
                palIki = Integer.parseInt(palIkiEdit.getText());
            else {
                error = "Nenurodytas pal\u016bkan\u0173 atid\u0117jimo pabaigos m\u0117nesis.";
                return false;
            }
            valueString = palPalEdit.getText();
            taskas = valueString.indexOf(".");
            if (taskas > -1)
                if (valueString.length() - 1 == taskas) {
                    valueString = valueString.substring(0, valueString.length() - 1);
                    palPalEdit.setText(valueString);
                }
            if (palPalEdit.getText().length() == 0) {
                error = "Nenurodytos atid\u0117tosios pal\u016bkanos.";
                return false;
            }
            palPal = Double.parseDouble(palPalEdit.getText());
            if (palNuo > palIki || palNuo == 0) {
                error = "Klaidingai nurodyta pal\u016bkan\u0173 atid\u0117jimo data.";
                return false;
            }
            if ((palIki - palNuo + 1) > menSk / 3) {
                error = "Atid\u0117ti pal\u016bkanas leid\u017eiama tik 1/3 viso mok\u0117jimo laikotarpio.";
                return false;
            }
        }
        if (kaiKurie.isSelected()) {
            if (kaiKurieNuoEdit.getText().length() != 0)
                kaiNuo = Integer.parseInt(kaiKurieNuoEdit.getText());
            else {
                error = "Nenurodytas rodymo intervalo pradinis m\u0117nuo.";
                return false;
            }
            if (kaiKurieIkiEdit.getText().length() != 0)
                kaiIki = Integer.parseInt(kaiKurieIkiEdit.getText());
            else {
                error = "Nenurodytas rodymo intervalo galinis m\u0117nuo";
                return false;
            }
            if (kaiNuo < 1 || kaiIki > menSk) {
                error = "Intervalas nesutampa su mok\u0117jimo intervalu.";
                return false;
            }
        }
        return true;
    }
}