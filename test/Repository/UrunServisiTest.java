package Repository;

import com.mehmetyilmazalan.miniecommerce.model.Urun;
import com.mehmetyilmazalan.miniecommerce.repository.UrunRepository;
import com.mehmetyilmazalan.miniecommerce.service.UrunServisi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrunServisiTest {

    private UrunRepository urunRepository;
    private UrunServisi urunServisi;
    private Urun urun;

    @BeforeEach
    void hazirla() {
        urunRepository = new UrunRepository();
        urunServisi = new UrunServisi(urunRepository);

        urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        urunRepository.ekle(urun);
    }

    @Test
    void urunServisUzerindenEklenebilmeli() {
        Urun yeniUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(yeniUrun);

        assertSame(yeniUrun, urunRepository.getir(102));
    }

    @Test
    void ayniIdIleUrunEklenememeliVeIlkKayitKorunmali() {
        Urun ayniIdliUrun = new Urun(
                101, "Kulaklık", BigDecimal.valueOf(200), 5
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.urunEkle(ayniIdliUrun)
        );

        assertSame(urun, urunRepository.getir(101));
        assertEquals(1, urunRepository.listele().size());
    }

    @Test
    void urununFiyatiGuncellenebilmeli() {
        urunServisi.fiyatGuncelle(101, BigDecimal.valueOf(150));

        assertEquals(
                BigDecimal.valueOf(150),
                urunRepository.getir(101).getUrunFiyati()
        );
    }

    @Test
    void sifirFiyatReddedilmeliVeEskiFiyatKorunmali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.fiyatGuncelle(101, BigDecimal.ZERO)
        );

        assertEquals(BigDecimal.valueOf(100), urun.getUrunFiyati());
    }

    @Test
    void negatifFiyatReddedilmeliVeEskiFiyatKorunmali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.fiyatGuncelle(
                        101, BigDecimal.valueOf(-50)
                )
        );

        assertEquals(BigDecimal.valueOf(100), urun.getUrunFiyati());
    }

    @Test
    void nullFiyatReddedilmeliVeEskiFiyatKorunmali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.fiyatGuncelle(101, null)
        );

        assertEquals(BigDecimal.valueOf(100), urun.getUrunFiyati());
    }

    @Test
    void olmayanUrununFiyatiGuncellenememeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.fiyatGuncelle(
                        999, BigDecimal.valueOf(150)
                )
        );

        assertEquals(BigDecimal.valueOf(100), urun.getUrunFiyati());
        assertFalse(urunRepository.varMi(999));
    }

    @Test
    void urununStoguArttirilabilmeli() {
        urunServisi.stokArttir(101, 3);

        assertEquals(13, urunRepository.getir(101).getUrunStok());
    }

    @Test
    void sifirMiktardaStokArttirilmamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokArttir(101, 0)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void negatifMiktardaStokArttirilmamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokArttir(101, -3)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void olmayanUrununStoguArttirilamamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokArttir(999, 3)
        );

        assertEquals(10, urun.getUrunStok());
        assertFalse(urunRepository.varMi(999));
    }

    @Test
    void urununStoguAzaltilabilmeli() {
        urunServisi.stokAzalt(101, 3);

        assertEquals(7, urunRepository.getir(101).getUrunStok());
    }

    @Test
    void stokunTamamiAzaltilabilmeli() {
        urunServisi.stokAzalt(101, 10);

        assertEquals(0, urun.getUrunStok());
    }

    @Test
    void stoktanFazlaAzaltmaReddedilmeliVeStokKorunmali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokAzalt(101, 11)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void sifirMiktardaStokAzaltilmamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokAzalt(101, 0)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void negatifMiktardaStokAzaltilmamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokAzalt(101, -3)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void olmayanUrununStoguAzaltilamamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.stokAzalt(999, 3)
        );

        assertEquals(10, urun.getUrunStok());
        assertFalse(urunRepository.varMi(999));
    }

    @Test
    void kayitliUrunlerListelenebilmeli() {
        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(ikinciUrun);

        List<Urun> urunler = urunServisi.urunListele();

        assertEquals(2, urunler.size());
        assertTrue(urunler.contains(urun));
        assertTrue(urunler.contains(ikinciUrun));
    }

    @Test
    void urunYokkenBosListeDonmeli() {
        UrunRepository bosRepository = new UrunRepository();
        UrunServisi bosRepositoryServisi = new UrunServisi(bosRepository);

        assertEquals(0, bosRepositoryServisi.urunListele().size());
    }

    @Test
    void donenListeyiBosaltmakKayitlariSilmemeli() {
        List<Urun> urunler = urunServisi.urunListele();

        urunler.clear();

        assertEquals(0, urunler.size());
        assertEquals(1, urunServisi.urunListele().size());
        assertSame(urun, urunRepository.getir(101));
    }

    @Test
    void fiyatVeStokIslemleriYalnizcaSecilenUrunuEtkilemeli() {
        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(ikinciUrun);

        urunServisi.fiyatGuncelle(101, BigDecimal.valueOf(150));
        urunServisi.stokArttir(101, 3);
        urunServisi.stokAzalt(101, 2);

        assertEquals(BigDecimal.valueOf(150), urun.getUrunFiyati());
        assertEquals(11, urun.getUrunStok());

        assertEquals(BigDecimal.valueOf(50), ikinciUrun.getUrunFiyati());
        assertEquals(5, ikinciUrun.getUrunStok());
    }

    @Test
    void urunAdiServisUzerindenGuncellenebilmeli() {
        urunServisi.adGuncelle(101, "Mekanik Klavye");

        assertEquals(
                "Mekanik Klavye",
                urunRepository.getir(101).getUrunAdi()
        );
    }

    @Test
    void olmayanUrununAdiGuncellenememeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.adGuncelle(999, "Mekanik Klavye")
        );

        assertEquals("Klavye", urun.getUrunAdi());
        assertFalse(urunRepository.varMi(999));
    }
    @Test
    void urunAdininBirBolumuyleAramaYapilabilmeli() {
        Urun ikinciUrun = new Urun(
                102, "Mekanik Klavye", BigDecimal.valueOf(200), 5
        );
        Urun ucuncuUrun = new Urun(
                103, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(ikinciUrun);
        urunServisi.urunEkle(ucuncuUrun);

        List<Urun> sonuc = urunServisi.urunAra("klav");

        assertEquals(2, sonuc.size());
        assertTrue(sonuc.contains(urun));
        assertTrue(sonuc.contains(ikinciUrun));
        assertFalse(sonuc.contains(ucuncuUrun));
    }

    @Test
    void aramadaBuyukKucukHarfFarkiOnemliOlmamali() {
        List<Urun> sonuc = urunServisi.urunAra("KLAVYE");

        assertEquals(1, sonuc.size());
        assertSame(urun, sonuc.get(0));
    }

    @Test
    void aramadaTurkceHarflerDogruEslesmeli() {
        Urun turkceAdliUrun = new Urun(
                102, "Şişe", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(turkceAdliUrun);

        List<Urun> sonuc = urunServisi.urunAra("ŞİŞE");

        assertEquals(1, sonuc.size());
        assertSame(turkceAdliUrun, sonuc.get(0));
    }

    @Test
    void aramaMetnininBasindakiVeSonundakiBosluklarYokSayilmali() {
        List<Urun> sonuc = urunServisi.urunAra("  klavye  ");

        assertEquals(1, sonuc.size());
        assertSame(urun, sonuc.get(0));
    }

    @Test
    void eslesmeYoksaBosListeDonmeli() {
        List<Urun> sonuc = urunServisi.urunAra("Telefon");

        assertTrue(sonuc.isEmpty());
    }

    @Test
    void gecersizAramaMetinleriReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.urunAra(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.urunAra("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.urunAra("   ")
        );
    }

    @Test
    void aramaSonucunuBosaltmakKayitlariSilmemeli() {
        List<Urun> sonuc = urunServisi.urunAra("klavye");

        sonuc.clear();

        assertEquals(0, sonuc.size());
        assertSame(urun, urunRepository.getir(101));
        assertEquals(1, urunServisi.urunAra("klavye").size());
    }

    @Test
    void urunlerFiyataGoreUcuzdanPahaliyaSiralanmali() {
        Urun ucuzUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );
        Urun pahaliUrun = new Urun(
                103, "Monitör", BigDecimal.valueOf(200), 5
        );

        urunServisi.urunEkle(pahaliUrun);
        urunServisi.urunEkle(ucuzUrun);

        List<Urun> sonuc = urunServisi.fiyataGoreArtanListele();

        assertEquals(3, sonuc.size());
        assertSame(ucuzUrun, sonuc.get(0));
        assertSame(urun, sonuc.get(1));
        assertSame(pahaliUrun, sonuc.get(2));
    }

    @Test
    void urunlerFiyataGorePahalidanUcuzaSiralanmali() {
        Urun ucuzUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );
        Urun pahaliUrun = new Urun(
                103, "Monitör", BigDecimal.valueOf(200), 5
        );

        urunServisi.urunEkle(ucuzUrun);
        urunServisi.urunEkle(pahaliUrun);

        List<Urun> sonuc = urunServisi.fiyataGoreAzalanListele();

        assertEquals(3, sonuc.size());
        assertSame(pahaliUrun, sonuc.get(0));
        assertSame(urun, sonuc.get(1));
        assertSame(ucuzUrun, sonuc.get(2));
    }

    @Test
    void urunlerTurkceAlfabetikSirayaGoreListelenmeli() {
        Urun canta = new Urun(
                102, "Çanta", BigDecimal.valueOf(50), 5
        );
        Urun defter = new Urun(
                103, "Defter", BigDecimal.valueOf(50), 5
        );
        Urun sise = new Urun(
                104, "Şişe", BigDecimal.valueOf(50), 5
        );
        Urun zil = new Urun(
                105, "Zil", BigDecimal.valueOf(50), 5
        );

        urunServisi.urunEkle(zil);
        urunServisi.urunEkle(sise);
        urunServisi.urunEkle(defter);
        urunServisi.urunEkle(canta);

        List<Urun> sonuc = urunServisi.adaGoreListele();

        assertEquals(5, sonuc.size());
        assertSame(canta, sonuc.get(0));
        assertSame(defter, sonuc.get(1));
        assertSame(urun, sonuc.get(2));
        assertSame(sise, sonuc.get(3));
        assertSame(zil, sonuc.get(4));
    }

    @Test
    void urunYokkenSiralamaMetotlariBosListeDonmeli() {
        UrunRepository bosRepository = new UrunRepository();
        UrunServisi bosServis = new UrunServisi(bosRepository);

        assertTrue(bosServis.fiyataGoreArtanListele().isEmpty());
        assertTrue(bosServis.fiyataGoreAzalanListele().isEmpty());
        assertTrue(bosServis.adaGoreListele().isEmpty());
    }

    @Test
    void siralanmisListeyiBosaltmakKayitlariSilmemeli() {
        List<Urun> sonuc = urunServisi.fiyataGoreArtanListele();

        sonuc.clear();

        assertEquals(0, sonuc.size());
        assertEquals(1, urunServisi.urunListele().size());
        assertSame(urun, urunRepository.getir(101));
    }

    @Test
    void stokSinirinaEsitVeAltindakiUrunlerListelenmeli() {
        Urun sinirinAltindakiUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 3
        );
        Urun sinirdakiUrun = new Urun(
                103, "Kulaklık", BigDecimal.valueOf(200), 5
        );
        Urun tukenmisUrun = new Urun(
                104, "Monitör", BigDecimal.valueOf(500), 0
        );

        urunServisi.urunEkle(sinirinAltindakiUrun);
        urunServisi.urunEkle(sinirdakiUrun);
        urunServisi.urunEkle(tukenmisUrun);

        List<Urun> sonuc = urunServisi.dusukStokluUrunleriListele(5);

        assertEquals(3, sonuc.size());
        assertTrue(sonuc.contains(sinirinAltindakiUrun));
        assertTrue(sonuc.contains(sinirdakiUrun));
        assertTrue(sonuc.contains(tukenmisUrun));
        assertFalse(sonuc.contains(urun));
    }

    @Test
    void sifirStokSinirindaYalnizcaTukenmisUrunlerListelenmeli() {
        Urun tukenmisUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 0
        );
        Urun stokluUrun = new Urun(
                103, "Kulaklık", BigDecimal.valueOf(200), 1
        );

        urunServisi.urunEkle(tukenmisUrun);
        urunServisi.urunEkle(stokluUrun);

        List<Urun> sonuc = urunServisi.dusukStokluUrunleriListele(0);

        assertEquals(1, sonuc.size());
        assertSame(tukenmisUrun, sonuc.get(0));
    }

    @Test
    void dusukStokluUrunYoksaBosListeDonmeli() {
        List<Urun> sonuc = urunServisi.dusukStokluUrunleriListele(5);

        assertTrue(sonuc.isEmpty());
    }

    @Test
    void negatifStokSiniriReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> urunServisi.dusukStokluUrunleriListele(-1)
        );

        assertEquals(10, urun.getUrunStok());
    }

    @Test
    void urunYokkenDusukStokListesiBosDonmeli() {
        UrunRepository bosRepository = new UrunRepository();
        UrunServisi bosServis = new UrunServisi(bosRepository);

        List<Urun> sonuc = bosServis.dusukStokluUrunleriListele(5);

        assertTrue(sonuc.isEmpty());
    }
}