package com.mehmetyilmazalan.miniecommerce.model;

import java.math.BigDecimal;

public class SepetKalemi {
    private Urun urun;
    private int urunAdedi;

    public SepetKalemi(Urun urun, int urunAdedi) {
        if(urun==null || urunAdedi <=0){
            throw new IllegalArgumentException("Ürün null olamaz ve ürün adedi sıfırdan büyük olmalıdır.");
        }
        this.urun = urun;
        this.urunAdedi = urunAdedi;
    }

    public BigDecimal tutar(){
        BigDecimal toplam= urun.getUrunFiyati().multiply(BigDecimal.valueOf(urunAdedi));
        return toplam;
    }

    public void adetArttir(Integer adet){
        if(adet<=0){
            throw new IllegalArgumentException("adet sıfır veya nefatif olmamamlı.");
        }
        urunAdedi+=adet;
    }

    public void adetGuncelle(Integer adet){
        if(adet<=0){
            throw new IllegalArgumentException("adet sıfır veya nefatif olmamamlı.");
        }
        urunAdedi=adet;
    }


    public Urun getUrun() {
        return urun;
    }

    public int getUrunAdedi() {
        return urunAdedi;
    }
}
