package com.mehmetyilmazalan.miniecommerce.repository;

import com.mehmetyilmazalan.miniecommerce.model.Siparis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiparisRepository implements Repository<Siparis,Integer>{

        private final Map<Integer, Siparis> siparisKayitlari = new HashMap<>();

        @Override
        public void ekle(Siparis siparis){
            if(siparisKayitlari.containsKey(siparis.getSiparisId())){
                throw new IllegalArgumentException("Bu id ile kayıtlı bir sipariş zaten var.");
            }
            siparisKayitlari.put(siparis.getSiparisId(),siparis);
        }

        @Override
        public Siparis getir(Integer siparisId){
            if(!siparisKayitlari.containsKey(siparisId)){
                throw new IllegalArgumentException("Bu Id ile kayıtlı bir sipariş bulunmamaktadır.");
            }
            return siparisKayitlari.get(siparisId);
        }

        @Override
        public void guncelle(Siparis siparis) {
        if(!siparisKayitlari.containsKey(siparis.getSiparisId())){
            throw new IllegalArgumentException("Bu Id ile kayıtlı bir sipariş bulunamadı.");
        }
        siparisKayitlari.replace(siparis.getSiparisId(),siparis);
        }

        @Override
        public List<Siparis> listele(){
            ArrayList<Siparis> siparisListesi = new ArrayList<>(siparisKayitlari.values());
            return siparisListesi;
        }

    @Override
    public boolean varMi(Integer siparisId){
        if(siparisKayitlari.containsKey(siparisId)){
            return true;
        }
        return false;
    }

}
