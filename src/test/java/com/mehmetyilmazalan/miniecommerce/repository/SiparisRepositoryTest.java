package com.mehmetyilmazalan.miniecommerce.repository;

import com.mehmetyilmazalan.miniecommerce.model.Siparis;
import com.mehmetyilmazalan.miniecommerce.model.SiparisKalemi;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import com.mehmetyilmazalan.miniecommerce.repository.SiparisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SiparisRepositoryTest {

    private SiparisRepository siparisRepository;
    private Siparis siparis;

    @BeforeEach
    void hazirla() {
        siparisRepository = new SiparisRepository();

        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi siparisKalemi = new SiparisKalemi(urun, 2);

        Map<Integer, SiparisKalemi> siparisKalemleri = new HashMap<>();
        siparisKalemleri.put(urun.getUrunId(), siparisKalemi);

        siparis = new Siparis(901, siparisKalemleri);
    }

    @Test
    void eklenenSiparisIdIleGetirilebilmeli() {
        siparisRepository.ekle(siparis);

        assertSame(siparis, siparisRepository.getir(901));
    }

    @Test
    void ayniIdIleSiparisEklenememeliVeIlkKayitKorunmali() {
        siparisRepository.ekle(siparis);

        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        SiparisKalemi ikinciKalem = new SiparisKalemi(ikinciUrun, 3);

        Map<Integer, SiparisKalemi> ikinciSiparisKalemleri = new HashMap<>();
        ikinciSiparisKalemleri.put(ikinciUrun.getUrunId(), ikinciKalem);

        Siparis ikinciSiparis = new Siparis(901, ikinciSiparisKalemleri);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisRepository.ekle(ikinciSiparis)
        );

        assertSame(siparis, siparisRepository.getir(901));
        assertEquals(1, siparisRepository.listele().size());
    }

    @Test
    void kayitliOlmayanSiparisGetirilmekIstendigindeHataVermeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisRepository.getir(999)
        );
    }

    @Test
    void kayitliSiparisGuncellenebilmeli() {
        siparisRepository.ekle(siparis);

        Urun yeniUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        SiparisKalemi yeniKalem = new SiparisKalemi(yeniUrun, 3);

        Map<Integer, SiparisKalemi> yeniSiparisKalemleri = new HashMap<>();
        yeniSiparisKalemleri.put(yeniUrun.getUrunId(), yeniKalem);

        Siparis yeniSiparis = new Siparis(901, yeniSiparisKalemleri);

        siparisRepository.guncelle(yeniSiparis);

        assertSame(yeniSiparis, siparisRepository.getir(901));
        assertEquals(1, siparisRepository.listele().size());
    }

    @Test
    void kayitliOlmayanSiparisGuncellenememeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisRepository.guncelle(siparis)
        );

        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void kayitliSiparisIcinVarMiTrueDonmeli() {
        siparisRepository.ekle(siparis);

        assertTrue(siparisRepository.varMi(901));
    }

    @Test
    void kayitliOlmayanSiparisIcinVarMiFalseDonmeli() {
        assertFalse(siparisRepository.varMi(999));
    }

    @Test
    void siparisYokkenBosListeDonmeli() {
        assertEquals(0, siparisRepository.listele().size());
    }

    @Test
    void kayitliTumSiparislerListelenebilmeli() {
        siparisRepository.ekle(siparis);

        Map<Integer, SiparisKalemi> ikinciSiparisKalemleri =
                new HashMap<>();

        for (SiparisKalemi kalem : siparis.siparisleriListele()) {
            ikinciSiparisKalemleri.put(kalem.getUrunId(), kalem);
        }

        Siparis ikinciSiparis = new Siparis(902, ikinciSiparisKalemleri);
        siparisRepository.ekle(ikinciSiparis);

        List<Siparis> siparisler = siparisRepository.listele();

        assertEquals(2, siparisler.size());
        assertTrue(siparisler.contains(siparis));
        assertTrue(siparisler.contains(ikinciSiparis));
    }

    @Test
    void donenListeyiBosaltmakRepositoryKayitlariniSilmemeli() {
        siparisRepository.ekle(siparis);

        List<Siparis> siparisler = siparisRepository.listele();
        siparisler.clear();

        assertEquals(0, siparisler.size());
        assertEquals(1, siparisRepository.listele().size());
        assertSame(siparis, siparisRepository.getir(901));
    }
}