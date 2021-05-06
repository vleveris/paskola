package paskola;

import javafx.scene.control.Alert;

class infoPranesimas {
    public String pavadinimas = "KLAIDA";
    public Alert.AlertType ikona = Alert.AlertType.ERROR;
    public String headeris = null;
    public String turinys;

    public infoPranesimas(String pavadinimas, Alert.AlertType ikona, String headeris, String turinys) {
        this.pavadinimas = pavadinimas;
        this.ikona = ikona;
        this.headeris = headeris;
        this.turinys = turinys;
    }

    public infoPranesimas(String turinys) {
        this.turinys = turinys;
    }

    public void isvesti() {
        Alert info = new Alert(ikona);
        info.setTitle(pavadinimas);
        info.setHeaderText(headeris);
        info.setContentText(turinys);
        info.showAndWait();
    }
}

public class rodytiInfo extends infoPranesimas {
    public rodytiInfo(String pavadinimas, Alert.AlertType ikona, String headeris, String turinys) {
        super(pavadinimas, ikona, headeris, turinys);
    }

    public rodytiInfo(String turinys) {
        super(turinys);
    }
}