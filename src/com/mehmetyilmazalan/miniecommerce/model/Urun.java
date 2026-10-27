package com.mehmetyilmazalan.miniecommerce.model;


import java.math.BigDecimal;

public class Urun {
    private final int urunId;
    private String urunAdi;
    private BigDecimal urunFiyati;
    private int urunStok;


    public Urun(int urunId,String urunAdi,BigDecimal urunFiyati,int urunStok){

        if(urunStok<0){
            throw new IllegalArgumentException("Stok miktarı negatif olamaz.");
        }

        if(urunAdi==null || urunAdi.isBlank()){
            throw new IllegalArgumentException("Ürün adı null, boş veya yalnızca boşluklardan oluşamaz.");
        }

        if(urunFiyati==null || urunFiyati.compareTo(BigDecimal.ZERO)<=0  ){
            throw new IllegalArgumentException("Ürün fiyatı null,sıfır veya negatf olamaz.");

        }

        if(urunId<=0){
            throw new IllegalArgumentException("Ürün Id si 0 veya negatif olamaz.");
        }

        this.urunId=urunId;
        this.urunAdi=urunAdi;
        this.urunFiyati=urunFiyati;
        this.urunStok=urunStok;
    }

    public int getUrunId() {
        return urunId;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public BigDecimal getUrunFiyati() {
        return urunFiyati;
    }

    public int getUrunStok() {
        return urunStok;
    }

    public void adGuncelle(String yeniAd) {
        if (yeniAd == null || yeniAd.isBlank()) {
            throw new IllegalArgumentException(
                    "Ürün adı null, boş veya yalnızca boşluklardan oluşamaz."
            );
        }

        this.urunAdi = yeniAd;
    }

    public void fiyatGuncelle(BigDecimal yeniFiyat){
        if(yeniFiyat == null || yeniFiyat.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Yeni fiyat boş , sıfır veya negatif olamaz.");
        }
        this.urunFiyati=yeniFiyat;
    }

    public void stokArttir(int miktar){
        if(miktar <=0){
            throw new IllegalArgumentException("Artırılacak miktar sıfır veya negatif olamaz.");
        }

        this.urunStok += miktar;
    }

    public void stokAzalt(int miktar){
        if(urunStok < miktar ||  miktar<=0){
            throw new IllegalArgumentException("Azaltılacak miktar ürün stoğundan büyük ,sıfır veya negatif olamaz.");
        }

        this.urunStok -= miktar;
    }












}
