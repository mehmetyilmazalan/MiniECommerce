package com.mehmetyilmazalan.miniecommerce.application;

import com.mehmetyilmazalan.miniecommerce.model.Sepet;
import com.mehmetyilmazalan.miniecommerce.model.Siparis;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import com.mehmetyilmazalan.miniecommerce.repository.Repository;
import com.mehmetyilmazalan.miniecommerce.repository.SiparisRepository;
import com.mehmetyilmazalan.miniecommerce.repository.UrunRepository;
import com.mehmetyilmazalan.miniecommerce.service.SiparisServisi;
import com.mehmetyilmazalan.miniecommerce.service.UrunServisi;

import java.math.BigDecimal;

public class MiniECommerceApplication {

    public static void main(String[] args) {
        Repository<Urun, Integer> urunRepository =
                new UrunRepository();

        Repository<Siparis, Integer> siparisRepository =
                new SiparisRepository();

        UrunServisi urunServisi = new UrunServisi(urunRepository);

        SiparisServisi siparisServisi = new SiparisServisi(
                urunRepository,
                siparisRepository
        );

        ornekUrunleriEkle(urunServisi);

        KonsolUygulamasi uygulama = new KonsolUygulamasi(
                urunServisi,
                siparisServisi,
                new Sepet()
        );

        uygulama.baslat();
    }

    private static void ornekUrunleriEkle(UrunServisi urunServisi) {
        urunServisi.urunEkle(new Urun(
                101,
                "Mekanik Klavye",
                new BigDecimal("1499.90"),
                12
        ));

        urunServisi.urunEkle(new Urun(
                102,
                "Kablosuz Mouse",
                new BigDecimal("649.90"),
                8
        ));

        urunServisi.urunEkle(new Urun(
                103,
                "Bluetooth Kulaklık",
                new BigDecimal("2199.00"),
                4
        ));

        urunServisi.urunEkle(new Urun(
                104,
                "Laptop Çantası",
                new BigDecimal("799.50"),
                6
        ));

        urunServisi.urunEkle(new Urun(
                105,
                "USB-C Adaptör",
                new BigDecimal("349.90"),
                0
        ));
    }
}