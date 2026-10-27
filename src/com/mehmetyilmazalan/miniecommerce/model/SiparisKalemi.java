package com.mehmetyilmazalan.miniecommerce.model;

import java.math.BigDecimal;

public class SiparisKalemi {
    private final int urunId;
    private final String urunAdi;
    private final int urunAdedi;
    private final BigDecimal birimFiyati;

    public SiparisKalemi(Urun urun, int urunAdedi){
        if(urun==null){
            throw new IllegalArgumentException("Ürün null olamaz");
        }
        if(urunAdedi<=0){
            throw new IllegalArgumentException("Urun adedi negatif veya sıfır olamaz.");
        }

        this.urunId=urun.getUrunId();
        this.urunAdi=urun.getUrunAdi();
        this.birimFiyati=urun.getUrunFiyati();
        this.urunAdedi=urunAdedi;
    }

    public int getUrunId() {
        return urunId;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public int getUrunAdedi() {
        return urunAdedi;
    }

    public BigDecimal getBirimFiyati() {
        return birimFiyati;
    }

    public BigDecimal siparisKalemToplam(){
        BigDecimal toplam = BigDecimal.valueOf(urunAdedi).multiply(birimFiyati);
             return toplam;
    }
}
