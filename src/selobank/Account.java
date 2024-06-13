package selobank;

import java.util.ArrayList;

public class Account {
    private String isim;
    private String soyIsim;
    private int hesapID;
    private long tcNo;
    private double bakiye;
    private int sifre;
    private ArrayList<String> işlemGeçmişi = new ArrayList<>();

    public Account(String isim, String soyIsim, int hesapID, long tcNo, double bakiye, int sifre) {
        this.isim = isim;
        this.soyIsim = soyIsim;
        this.hesapID = hesapID;
        this.tcNo = tcNo;
        this.bakiye = bakiye;
        this.sifre = sifre;
    }

    public String getIsim() {
        return isim;
    }

    public String getSoyIsim() {
        return soyIsim;
    }

    public int getHesapID() {
        return hesapID;
    }

    public long getTcNo() {
        return tcNo;
    }

    public double getBakiye() {
        return bakiye;
    }

    public int getSifre() {
        return sifre;
    }

    public void paraYatir(double miktar) {
        this.bakiye += miktar;
        işlemEkle(miktar + " TL yatırıldı");
    }

    public boolean paraCek(double miktar) {
        if (this.bakiye >= miktar) {
            this.bakiye -= miktar;
            işlemEkle(miktar + " TL çekildi");
            return true;
        }
        return false;
    }

    public boolean paraTransferi(Account hedefHesap, double miktar) {
        if (this.bakiye >= miktar) {
            this.bakiye -= miktar;
            hedefHesap.paraYatir(miktar);
            işlemEkle(miktar + " TL " + hedefHesap.getHesapID() + " hesap numarasına transfer edildi");
            return true;
        }
        return false;
    }

    public void işlemEkle(String işlem) {
        işlemGeçmişi.add(işlem);
    }

    public String getİşlemGeçmişi() {
        StringBuilder sb = new StringBuilder();
        for (String işlem : işlemGeçmişi) {
            sb.append(işlem).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<String> getİslemGecmisiList() {
        return işlemGeçmişi;
    }
}
