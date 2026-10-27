package com.mehmetyilmazalan.miniecommerce.service;

import com.mehmetyilmazalan.miniecommerce.model.*;
import com.mehmetyilmazalan.miniecommerce.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiparisServisi {

    private final Repository<Urun,Integer> urunRepository;
    private final Repository<Siparis,Integer> siparisRepository;

    public SiparisServisi(Repository<Urun, Integer> urunRepository, Repository<Siparis, Integer> siparisRepository) {
        this.urunRepository = urunRepository;
        this.siparisRepository = siparisRepository;
    }

    public Siparis olustur(Integer siparisId, Sepet sepet) {
        if (siparisId == null || siparisId <= 0) {
            throw new IllegalArgumentException("Sipariş ID’si null, sıfır veya negatif olamaz.");
        }
        if (sepet == null || sepet.sepetiListele().isEmpty()) {
            throw new IllegalArgumentException("Sepet null veya boş olamaz");
        }

        if(siparisRepository.varMi(siparisId)){
            throw new IllegalArgumentException("Oluşturmak istediğiniz sipariş önceden olulturulmuş.");
        }

        Map<Integer, SiparisKalemi> siparisKalemleri = new HashMap<>();

        for (SepetKalemi sepetKalemi : sepet.sepetiListele()) {
            Urun kayitliUrun = urunRepository.getir(sepetKalemi.getUrun().getUrunId());

            if (kayitliUrun.getUrunStok() < sepetKalemi.getUrunAdedi()) {
                throw new IllegalArgumentException("İstenen adet mevcut stoktan fazla!");
            }

            SiparisKalemi siparisKalemi = new SiparisKalemi(kayitliUrun, sepetKalemi.getUrunAdedi());
            siparisKalemleri.put(siparisKalemi.getUrunId(), siparisKalemi);
        }

        Siparis olusturulanSiparis = new Siparis(siparisId, siparisKalemleri);

        for (SepetKalemi sepetKalemi : sepet.sepetiListele()) {
            Urun kayitliUrun = urunRepository.getir(sepetKalemi.getUrun().getUrunId());
            kayitliUrun.stokAzalt(sepetKalemi.getUrunAdedi());
        }

        siparisRepository.ekle(olusturulanSiparis);
        sepet.sepetBosalt();

        return olusturulanSiparis;

    }

    public void iptalEt(Integer siparisId){
        Siparis iptalEdilecekSiparis= siparisRepository.getir(siparisId);

        if(iptalEdilecekSiparis.getSiparisDurumu()==SiparisDurumu.IPTAL_EDILDI){
            throw new IllegalStateException("İptal etmek istediğiniz sipariş zaten iptal edilmiş görünüyor.");
        }

        Map<Integer,Urun> iptalUrunMap  =new HashMap<>();
        for(SiparisKalemi kalem : iptalEdilecekSiparis.siparisleriListele()){
            Urun kayitliUrun = urunRepository.getir(kalem.getUrunId());
            iptalUrunMap.put(kayitliUrun.getUrunId(),kayitliUrun );

        }
        for(SiparisKalemi kalem : iptalEdilecekSiparis.siparisleriListele()){
            Urun kayitliUrun = iptalUrunMap.get(kalem.getUrunId());
            kayitliUrun.stokArttir(kalem.getUrunAdedi());
        }



        iptalEdilecekSiparis.siparisIptalEt();
    }

    public List<Siparis> siparisListele(){
        return siparisRepository.listele();
    }

    public List<Siparis> durumaGoreListele(SiparisDurumu durum) {
        if (durum == null) {
            throw new IllegalArgumentException(
                    "Sipariş durumu null olamaz."
            );
        }

        List<Siparis> bulunanSiparisler = new ArrayList<>();

        for (Siparis siparis : siparisRepository.listele()) {
            if (siparis.getSiparisDurumu() == durum) {
                bulunanSiparisler.add(siparis);
            }
        }

        return bulunanSiparisler;
    }
    public Siparis siparisGetir(Integer siparisId){
        return siparisRepository.getir(siparisId);
    }



}
