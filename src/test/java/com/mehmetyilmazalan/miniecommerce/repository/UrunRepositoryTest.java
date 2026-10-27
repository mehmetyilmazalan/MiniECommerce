package com.mehmetyilmazalan.miniecommerce.repository;

import com.mehmetyilmazalan.miniecommerce.model.Urun;
import com.mehmetyilmazalan.miniecommerce.repository.UrunRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UrunRepositoryTest {
    @Test
    void eklenenUrunIdIleGetirilebilmeli() {

        UrunRepository urunRepository = new UrunRepository();

        Urun urun = new Urun(101,"Kağıt", BigDecimal.valueOf(100),10);

        urunRepository.ekle(urun);

        assertSame(urun,urunRepository.getir(101));
    }

     @Test
    void ayniIdIleUrunEklenememeliVeIlkKayitKorunmali(){

         UrunRepository urunRepository = new UrunRepository();

         Urun urun1 = new Urun(101,"Elma", BigDecimal.valueOf(100),10);
         Urun urun2 = new Urun(101,"Armut", BigDecimal.valueOf(100),10);

         urunRepository.ekle(urun1);

         assertThrows(IllegalArgumentException.class , () -> urunRepository.ekle(urun2));
         assertSame(urun1,urunRepository.getir(101));

     }

     @Test
    void kayitliOlmayanUrunGetirilmekIstendigindeHataVermeli(){

        UrunRepository urunRepository = new UrunRepository();

        assertThrows(IllegalArgumentException.class , ()-> urunRepository.getir(999));

     }

     @Test
    void  kayitliUrunGuncellenebilmeli(){

         UrunRepository urunRepository = new UrunRepository();

         Urun urun1 = new Urun(101,"Elma", BigDecimal.valueOf(100),10);
         urunRepository.ekle(urun1);

         Urun urun2 = new Urun(101,"Yeşil Elma", BigDecimal.valueOf(100),10);
         urunRepository.guncelle(urun2);

         assertSame(urun2,urunRepository.getir(101));

     }

    @Test
    void kayitliOlmayanUrunGuncellenememeli() {
        UrunRepository urunRepository = new UrunRepository();

        Urun urun = new Urun(
                101, "Elma", BigDecimal.valueOf(100), 10
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urunRepository.guncelle(urun)
        );

        assertFalse(urunRepository.varMi(101));
    }

    @Test
    void kayitliUrunIcinVarMiTrueDonmeli() {
        UrunRepository urunRepository = new UrunRepository();

        Urun urun = new Urun(
                101, "Elma", BigDecimal.valueOf(100), 10
        );

        urunRepository.ekle(urun);

        assertTrue(urunRepository.varMi(101));
    }

    @Test
    void kayitliOlmayanUrunIcinVarMiFalseDonmeli() {
        UrunRepository urunRepository = new UrunRepository();

        assertFalse(urunRepository.varMi(999));
    }

    @Test
    void urunYokkenBosListeDonmeli() {
        UrunRepository urunRepository = new UrunRepository();

        assertEquals(0, urunRepository.listele().size());
    }

    @Test
    void kayitliTumUrunlerListelenebilmeli() {
        UrunRepository urunRepository = new UrunRepository();

        Urun urun1 = new Urun(
                101, "Elma", BigDecimal.valueOf(100), 10
        );
        Urun urun2 = new Urun(
                102, "Armut", BigDecimal.valueOf(50), 5
        );

        urunRepository.ekle(urun1);
        urunRepository.ekle(urun2);

        List<Urun> urunler = urunRepository.listele();

        assertEquals(2, urunler.size());
        assertTrue(urunler.contains(urun1));
        assertTrue(urunler.contains(urun2));
    }

    @Test
    void donenListeyiBosaltmakRepositoryKayitlariniSilmemeli() {
        UrunRepository urunRepository = new UrunRepository();

        Urun urun = new Urun(
                101, "Elma", BigDecimal.valueOf(100), 10
        );

        urunRepository.ekle(urun);

        List<Urun> urunler = urunRepository.listele();
        urunler.clear();

        assertEquals(0, urunler.size());
        assertEquals(1, urunRepository.listele().size());
        assertSame(urun, urunRepository.getir(101));
    }

}
