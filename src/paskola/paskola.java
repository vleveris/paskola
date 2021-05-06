package paskola;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class paskola {
    private BigDecimal metinisPr;
    private int menSk;
    private final BigDecimal suma;
    private final boolean anuitet;
    private BigDecimal menPalukanosAnuitet;
    private BigDecimal isVisoPalukanosLin;
    private BigDecimal menSkBig;

    public paskola(BigDecimal metinisPr, int menSk, BigDecimal suma, boolean anuitet) {
        this.metinisPr = metinisPr;
        this.menSk = menSk;
        this.suma = suma;
        this.anuitet = anuitet;
        menPalukanosAnuitet = metinisPr.divide(new BigDecimal(1200), 10, RoundingMode.HALF_UP);
        menSkBig = new BigDecimal(menSk);
        isVisoPalukanosLin = menSkBig.multiply(suma).multiply(menPalukanosAnuitet);
    }

    public BigDecimal getSuma() {
        return suma;
    }

    public BigDecimal getMetinisPr() {
        return metinisPr;
    }

    public void setMetinisPr(BigDecimal metinisPr) {
        this.metinisPr = metinisPr;
        menPalukanosAnuitet = metinisPr.divide(new BigDecimal(1200), 10, RoundingMode.HALF_UP);
        isVisoPalukanosLin = menSkBig.multiply(suma).multiply(menPalukanosAnuitet);
    }

    public BigDecimal moketiKasMen(int menNr) {
        if (anuitet) {
            BigDecimal keltiLaipsniu = (new BigDecimal(1).add(menPalukanosAnuitet)).pow(menSk);
            return (menPalukanosAnuitet.multiply(keltiLaipsniu)).divide((keltiLaipsniu.subtract(new BigDecimal(1))), 10, RoundingMode.HALF_UP).multiply(suma);
        }
        return suma.divide(menSkBig, 10, RoundingMode.HALF_UP).add(((menSkBig.add(new BigDecimal(1)).subtract(new BigDecimal(menNr)))).multiply(isVisoPalukanosLin).divide((menSkBig.multiply(menSkBig)), 10, RoundingMode.HALF_UP));
    }

    public BigDecimal getMenPalukanos(int menNr, BigDecimal sumaPriesMokejima) {
        if (anuitet)
            return sumaPriesMokejima.multiply(menPalukanosAnuitet);
        return ((menSkBig.add(new BigDecimal(1)).subtract(new BigDecimal(menNr)))).multiply(isVisoPalukanosLin).divide((menSkBig.multiply(menSkBig)), 10, RoundingMode.HALF_UP);
    }

    public int getMenSk() {
        return menSk;
    }

    public void setMenSk(int menSk) {
        this.menSk = menSk;
        menSkBig = new BigDecimal(menSk);
        isVisoPalukanosLin = menSkBig.multiply(suma).multiply(menPalukanosAnuitet);
    }

}
