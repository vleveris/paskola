package paskola.vaizdavimas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import paskola.paskola;
import paskola.rodytiInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

public class lentele {
    private final TableView<duomenys> table = new TableView<>();
    private final ObservableList<duomenys> data = FXCollections.observableArrayList();
    private final paskola Paskola;
    private final int atNuo;
    private final int atIki;
    private final Stage stageParent;
    private final int palNuo;
    private final int palIki;
    private final BigDecimal palPal;
    private final int kaiKurieNuo;
    private final int kaiKurieIki;
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    XYChart.Series series = new XYChart.Series();

    public lentele(paskola Paskola, int atNuo, int atIki, int palNuo, int palIki, BigDecimal palPal, int kaiKurieNuo, int kaiKurieIki, Stage stageParent) {
        this.Paskola = Paskola;
        this.atNuo = atNuo;
        this.atIki = atIki;
        this.stageParent = stageParent;
        this.palNuo = palNuo;
        this.palIki = palIki;
        this.palPal = palPal;
        this.kaiKurieNuo = kaiKurieNuo;
        this.kaiKurieIki = kaiKurieIki;
    }

    public void rodytiGrafika() {
        xAxis.setLabel("M\u0117nesio numeris");
        yAxis.setLabel("M\u0117nesio \u012fmoka");
        LineChart<Number, Number> diagrama = new LineChart<>(xAxis, yAxis);
        diagrama.setTitle("Paskolos atidavimo grafikas");
        series.setName("M\u0117nesio \u012fmoka");
        uzpildytiLentele(false, true);
        Scene scene = new Scene(diagrama, 800, 600);
        diagrama.getData().add(series);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(stageParent);
        stage.setFullScreen(true);
        stage.show();
    }

    public void uzpildytiLentele(boolean failas, boolean gr) {
        BigDecimal menImoka;
        BigDecimal likutis = Paskola.getSuma();
        int menAtostogu = 0;
        int atostoguPalukanos = 0;
        int menSk = Paskola.getMenSk();
        boolean yraAtostogos = atIki > 0;
        Writer f = null;
// BufferedWriter out=null;
        BigDecimal mokejimuSuma = new BigDecimal(0);
        BigDecimal pradinesPalukanos = Paskola.getMetinisPr();
        String failoKlaida = "Failas paskolosRezultatai.txt s\u0117kmingai sukurtas.";
        if (failas)
            try {
                f = new OutputStreamWriter(new FileOutputStream("paskolosRezultatai.txt"), StandardCharsets.UTF_8);
                f.write("Paskol\u0173 skai\u010diuokl\u0117s sugeneruoti rezultatai:\r\n\r\nPaskolos suma: " + likutis.setScale(2, RoundingMode.HALF_UP).toString() + "\r\nGra\u017einimo laikotarpis: " + menSk + " m\u0117n.\r\nMetin\u0117s pal\u016bkanos: " + pradinesPalukanos.setScale(2, RoundingMode.HALF_UP).toString() + " %\r\n\r\n");
                if (atNuo > -1)
                    f.write("Mok\u0117jimo atostogos nuo " + atNuo + " iki " + atIki + " m\u0117n.\r\n");
                if (palNuo > -1)
                    f.write("Pasirinktas pal\u016bkan\u0173 atid\u0117jimas nuo " + palNuo + " iki " + palIki + " m\u0117n. su " + palPal.setScale(2, RoundingMode.HALF_UP).toString() + " % metin\u0117mis pal\u016bkanomis\r\n\r\n");
            } catch (IOException E) {
                failoKlaida = "Klaida sukuriant fail\u0105";
            }
        if (yraAtostogos)
            Paskola.setMenSk(Paskola.getMenSk() - (atIki - atNuo + 1));
        BigDecimal menPalukanos;
        BigDecimal tikMenImoka;
        yraAtostogos = false;
        BigDecimal palukanuSuma = new BigDecimal(0);
        boolean arRodyti = false;
        String dydisRound, menImokaRound, menPalukanosRound, likutisRound, tikMenImokaRound;
        for (int i = 1; i <= menSk; i++) {
            if (i == kaiKurieNuo)
                arRodyti = true;
            else if (i == kaiKurieIki + 1)
                arRodyti = false;
            if (i == palNuo)
                Paskola.setMetinisPr(palPal);
            else if (i == palIki + 1)
                Paskola.setMetinisPr(pradinesPalukanos.add(palPal.divide(new BigDecimal(3), 10, RoundingMode.HALF_UP)));
            if (i == atNuo) {
                yraAtostogos = true;
                atostoguPalukanos = i;
            } else if (i == atIki + 1) {
                yraAtostogos = false;
                menAtostogu = atIki - atNuo + 1;
            }
            if (!yraAtostogos) {
                menImoka = Paskola.moketiKasMen(i - menAtostogu);
                menPalukanos = Paskola.getMenPalukanos(i - menAtostogu, likutis);
            } else {
                menPalukanos = Paskola.getMenPalukanos(atostoguPalukanos, likutis);
                menImoka = menPalukanos;
            }
            tikMenImoka = menImoka.subtract(menPalukanos);
            palukanuSuma = palukanuSuma.add(menPalukanos);
            mokejimuSuma = mokejimuSuma.add(tikMenImoka);
            likutisRound = likutis.subtract(tikMenImoka).setScale(2, RoundingMode.HALF_UP).toString();
            dydisRound = likutis.setScale(2, RoundingMode.HALF_UP).toString();
            menImokaRound = menImoka.setScale(2, RoundingMode.HALF_UP).toString();
            tikMenImokaRound = tikMenImoka.setScale(2, RoundingMode.HALF_UP).toString();
            menPalukanosRound = menPalukanos.setScale(2, RoundingMode.HALF_UP).toString();
            if (failas)
                try {
                    f.write(i + ". Paskolos dydis: " + dydisRound + ", \u012fmoka: " + tikMenImokaRound + ", pal\u016bkanos: " + menPalukanosRound + ", i\u0161 viso per m\u0117nes\u012f: " + menImokaRound + ", liko nesumok\u0117ta: " + likutisRound + ".\r\n");
                } catch (IOException E) {
                    failoKlaida = "\u012evyko klaida \u012fra\u0161ant duomenis.";
                }
            else if (arRodyti)
                if (!gr)
                    data.add(new duomenys(Integer.toString(i), dydisRound, tikMenImokaRound, menPalukanosRound, menImokaRound, likutisRound));
                else
                    series.getData().add(new XYChart.Data(i, mokejimuSuma.add(palukanuSuma)));
            likutis = likutis.subtract(tikMenImoka);
        }
        if (failas) {
            try {
                assert f != null;
                f.write("\r\n\r\nI\u0161 viso sumok\u0117ta " + mokejimuSuma.add(palukanuSuma).setScale(2, RoundingMode.HALF_UP).toString() + " \u012fskaitant bendr\u0105 pal\u016bkan\u0173 sum\u0105 (" + palukanuSuma.setScale(2, RoundingMode.HALF_UP).toString() + "). Pal\u016bkanos sudaro " + palukanuSuma.multiply(new BigDecimal(100)).divide(mokejimuSuma.add(palukanuSuma), 10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString() + " % bendros sumos.");
                f.close();
            } catch (IOException E) {
                failoKlaida = "Klaida u\u017edarant fail\u0105";
            }
            new rodytiInfo("REZULTAT\u0172 FAILO \u012eRA\u0160YMAS", Alert.AlertType.INFORMATION, "Informacinis prane\u0161imas d\u0117l rezultat\u0173 failo suk\u016brimo", failoKlaida).isvesti();
        }
        if (kaiKurieNuo == 1 && kaiKurieIki == menSk)
            if (!gr)
                data.add(new duomenys("-", likutis.setScale(2, RoundingMode.HALF_UP).toString(), mokejimuSuma.setScale(2, RoundingMode.HALF_UP).toString(), palukanuSuma.setScale(2, RoundingMode.HALF_UP).toString(), palukanuSuma.add(mokejimuSuma).setScale(2, RoundingMode.HALF_UP).toString(), "-"));
    }

    public void rodyti() {
        Stage langas = new Stage();
        langas.initOwner(stageParent);
        langas.setFullScreen(true);
        langas.setTitle("M\u0117nesin\u0117s \u012fmokos");
        langas.setWidth(800);
        langas.setHeight(800);
        final Text antraste = new Text("Paskolos gra\u017einimo m\u0117nesi\u0173 suvestin\u0117");
        antraste.setFont(new Font("Arial", 20));
        TableColumn<duomenys, String> menesioNumerisCol = new TableColumn<>("M\u0117nesio numeris");
        menesioNumerisCol.setMinWidth(100);
        menesioNumerisCol.setCellValueFactory(new PropertyValueFactory<>("menesioNumeris"));

        TableColumn<duomenys, String> paskolosDydisCol = new TableColumn<>("Paskolos dydis");
        paskolosDydisCol.setMinWidth(100);
        paskolosDydisCol.setCellValueFactory(new PropertyValueFactory<>("paskolosDydis"));

        TableColumn<duomenys, String> imokaCol = new TableColumn<>("\u012emoka");
        imokaCol.setMinWidth(100);
        imokaCol.setCellValueFactory(new PropertyValueFactory<>("imoka"));

        TableColumn<duomenys, String> palukanosCol = new TableColumn<>("Pal\u016bkanos");
        palukanosCol.setMinWidth(100);
        palukanosCol.setCellValueFactory(new PropertyValueFactory<>("palukanos"));

        TableColumn<duomenys, String> isVisoPerMenCol = new TableColumn<>("I\u0161 viso");
        isVisoPerMenCol.setMinWidth(100);
        isVisoPerMenCol.setCellValueFactory(new PropertyValueFactory<>("isVisoPerMen"));

        TableColumn<duomenys, String> likoNesumoketaCol = new TableColumn<>("Liko nesumok\u0117ta");
        likoNesumoketaCol.setMinWidth(100);
        likoNesumoketaCol.setCellValueFactory(new PropertyValueFactory<>("likoNesumoketa"));

        uzpildytiLentele(false, false);
        table.setItems(data);
        table.getColumns().addAll(menesioNumerisCol, paskolosDydisCol, imokaCol, palukanosCol, isVisoPerMenCol, likoNesumoketaCol);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(antraste, table);
        Scene scene = new Scene(new Group(vbox));
        scene.getStylesheets().add("paskola/vaizdavimas/style/cellHeight.css");
        langas.setScene(scene);
        langas.show();
    }

    public static class duomenys {
        private final String menesioNumeris;
        private final String paskolosDydis;
        private final String imoka;
        private final String palukanos;
        private final String isVisoPerMen;
        private final String likoNesumoketa;

        private duomenys(String menesioNumeris, String paskolosDydis, String imoka, String palukanos, String isVisoPerMen, String likoNesumoketa) {
            this.menesioNumeris = menesioNumeris;
            this.paskolosDydis = paskolosDydis;
            this.imoka = imoka;
            this.palukanos = palukanos;
            this.isVisoPerMen = isVisoPerMen;
            this.likoNesumoketa = likoNesumoketa;
        }

        public String getMenesioNumeris() {
            return menesioNumeris;
        }

        public String getPaskolosDydis() {
            return paskolosDydis;
        }

        public String getImoka() {
            return imoka;
        }

        public String getPalukanos() {
            return palukanos;
        }

        public String getIsVisoPerMen() {
            return isVisoPerMen;
        }

        public String getLikoNesumoketa() {
            return likoNesumoketa;
        }
    }
}