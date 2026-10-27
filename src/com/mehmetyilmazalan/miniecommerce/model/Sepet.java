package com.mehmetyilmazalan.miniecommerce.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sepet {

    private Map<Integer,SepetKalemi> sepet = new HashMap<>();

    public void urunEkle(Urun urun,Integer adet){
        if(urun==null){
            throw new IllegalArgumentException("Ürün null olamaz.");
        }
        if(sepet.containsKey(urun.getUrunId())){
            sepet.get(urun.getUrunId()).adetArttir(adet);
        }else{
            SepetKalemi sepetKalemi = new SepetKalemi(urun,adet);
            sepet.put(urun.getUrunId(),sepetKalemi);
        }
    }

    public BigDecimal tutar(){
        BigDecimal toplam = BigDecimal.valueOf(0);

        for(SepetKalemi kalemler : sepet.values()){
           toplam = toplam.add(kalemler.tutar());
        }

        return toplam;
    }

    public void urunCikar(Integer urunId){
        if(!sepet.containsKey(urunId)){
            throw new IllegalArgumentException("Çıkarmak istediğiniz ürün sepette bulunamadı!");
        }
        sepet.remove(urunId);
    }


    public void urunAdediGuncelle(Integer urunId , Integer adet){
        if(urunId==null){
            throw new IllegalArgumentException("ÜÜrün ID’si null olamaz.");
        }
        if(!sepet.containsKey(urunId)){
            throw new IllegalArgumentException("Ürün sepette bulunamadı");
        }else{
            sepet.get(urunId).adetGuncelle(adet);
        }

    }

    public List<SepetKalemi> sepetiListele(){
        ArrayList<SepetKalemi> sepetListesi = new ArrayList<>(sepet.values());
        return sepetListesi;
    }

    public void sepetBosalt(){
        sepet.clear();
    }


}
