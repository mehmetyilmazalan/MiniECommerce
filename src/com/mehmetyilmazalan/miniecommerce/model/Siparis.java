package com.mehmetyilmazalan.miniecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Siparis {

    private final int siparisId;
    private SiparisDurumu siparisDurumu;
    private final Map<Integer,SiparisKalemi> siparisKalemleri = new HashMap<>();
    private final LocalDateTime olusturulmaZamani;

    public Siparis(int siparisId, Map<Integer, SiparisKalemi> siparisKalemleri) {
        if (siparisId <= 0) {
            throw new IllegalArgumentException(
                    "Sipariş ID'si sıfırdan büyük olmalıdır."
            );
        }

        if (siparisKalemleri == null || siparisKalemleri.isEmpty()) {
            throw new IllegalArgumentException(
                    "Sipariş kalemleri null veya boş olamaz."
            );
        }

        this.siparisId = siparisId;
        this.siparisDurumu = SiparisDurumu.OLUSTURULDU;
        this.siparisKalemleri.putAll(siparisKalemleri); //Son satırdaki this.siparisKalemleri siparişin kendi Map’i,
        // sağdaki ise parametre olarak gelen Map. putAll, gelen kayıtları kendi Map’imize aktarır.

        this.olusturulmaZamani = LocalDateTime.now();
    }

    public BigDecimal tutar(){
        BigDecimal toplam = BigDecimal.valueOf(0);
        for(SiparisKalemi siparisler : siparisKalemleri.values()){
            toplam = toplam.add(siparisler.siparisKalemToplam());
        }
        return toplam;
    }

    public List<SiparisKalemi> siparisleriListele(){
        ArrayList<SiparisKalemi> siparisKalemiArrayListesi = new ArrayList<>(siparisKalemleri.values());
        return  siparisKalemiArrayListesi;
    }

    public void siparisIptalEt(){
        if(siparisDurumu==SiparisDurumu.IPTAL_EDILDI){
            throw new IllegalStateException("İptal etmek istediğiniz sipariş zaten iptal durumunda görünüyor.");
        }
        siparisDurumu=SiparisDurumu.IPTAL_EDILDI;
    }

    public int getSiparisId() {
        return siparisId;
    }

    public SiparisDurumu getSiparisDurumu() {
        return siparisDurumu;
    }
    public LocalDateTime getOlusturulmaZamani() {
        return olusturulmaZamani;
    }
}
