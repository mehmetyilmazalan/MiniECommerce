package service;

import com.mehmetyilmazalan.miniecommerce.model.*;
import com.mehmetyilmazalan.miniecommerce.repository.SiparisRepository;
import com.mehmetyilmazalan.miniecommerce.repository.UrunRepository;
import com.mehmetyilmazalan.miniecommerce.service.SiparisServisi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SiparisServisiTest {

    private UrunRepository urunRepository;
    private SiparisRepository siparisRepository;
    private SiparisServisi siparisServisi;
    private Sepet sepet;
    private Urun urun;

    @BeforeEach
    void hazirla() {
        urunRepository = new UrunRepository();
        siparisRepository = new SiparisRepository();

        siparisServisi = new SiparisServisi(
                urunRepository,
                siparisRepository
        );

        urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        urunRepository.ekle(urun);

        sepet = new Sepet();
        sepet.urunEkle(urun, 3);
    }

    @Test
    void siparisOlusturuldugundaStokAzalmaliVeSepetBosalmali() {
        Siparis siparis = siparisServisi.olustur(901, sepet);

        assertEquals(901, siparis.getSiparisId());
        assertEquals(SiparisDurumu.OLUSTURULDU, siparis.getSiparisDurumu());
        assertEquals(BigDecimal.valueOf(300), siparis.tutar());

        assertEquals(7, urun.getUrunStok());
        assertEquals(0, sepet.sepetiListele().size());

        assertTrue(siparisRepository.varMi(901));
    }

    @Test
    void birdenFazlaUrunleSiparisOlusturulabilmeli() {
        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunRepository.ekle(ikinciUrun);
        sepet.urunEkle(ikinciUrun, 2);

        Siparis siparis = siparisServisi.olustur(901, sepet);

        assertEquals(2, siparis.siparisleriListele().size());
        assertEquals(BigDecimal.valueOf(400), siparis.tutar());

        assertEquals(7, urun.getUrunStok());
        assertEquals(3, ikinciUrun.getUrunStok());
        assertEquals(0, sepet.sepetiListele().size());
    }

    @Test
    void stokunTamamiSiparisEdilebilmeli() {
        sepet.urunAdediGuncelle(101, 10);

        Siparis siparis = siparisServisi.olustur(901, sepet);

        assertEquals(0, urun.getUrunStok());
        assertEquals(BigDecimal.valueOf(1000), siparis.tutar());
        assertTrue(siparisRepository.varMi(901));
    }

    @Test
    void nullSiparisIdReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(null, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, sepet.sepetiListele().size());
        assertEquals(0, siparisRepository.listele().size());
    }

    @Test
    void sifirSiparisIdReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(0, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, sepet.sepetiListele().size());
        assertEquals(0, siparisRepository.listele().size());
    }

    @Test
    void negatifSiparisIdReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(-1, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, sepet.sepetiListele().size());
        assertEquals(0, siparisRepository.listele().size());
    }

    @Test
    void nullSepetleSiparisOlusturulamamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, null)
        );

        assertEquals(10, urun.getUrunStok());
        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void bosSepetleSiparisOlusturulamamali() {
        Sepet bosSepet = new Sepet();

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, bosSepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void ayniIdIleIkinciSiparisOlusturulamamali() {
        Siparis ilkSiparis = siparisServisi.olustur(901, sepet);

        Sepet ikinciSepet = new Sepet();
        ikinciSepet.urunEkle(urun, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, ikinciSepet)
        );

        assertEquals(7, urun.getUrunStok());
        assertEquals(1, ikinciSepet.sepetiListele().size());
        assertEquals(2, ikinciSepet.sepetiListele().get(0).getUrunAdedi());

        assertEquals(1, siparisRepository.listele().size());
        assertSame(ilkSiparis, siparisRepository.getir(901));
    }

    @Test
    void yetersizStoktaSiparisOlusturulamamali() {
        sepet.urunAdediGuncelle(101, 11);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, sepet.sepetiListele().size());
        assertEquals(11, sepet.sepetiListele().get(0).getUrunAdedi());

        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void urunlerdenBirininStoguYetmezseHicbirStokAzalmamali() {
        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 1
        );

        urunRepository.ekle(ikinciUrun);
        sepet.urunEkle(ikinciUrun, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, ikinciUrun.getUrunStok());

        assertEquals(2, sepet.sepetiListele().size());
        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void kayitliOlmayanUrunleSiparisOlusturulamamali() {
        Urun kayitsizUrun = new Urun(
                999, "Kulaklık", BigDecimal.valueOf(200), 5
        );

        sepet.urunEkle(kayitsizUrun, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, sepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(5, kayitsizUrun.getUrunStok());

        assertEquals(2, sepet.sepetiListele().size());
        assertFalse(siparisRepository.varMi(901));
    }

    @Test
    void urunFiyatiDegisseBileOlusturulmusSiparisinTutariKorunmali() {
        Siparis siparis = siparisServisi.olustur(901, sepet);

        urun.fiyatGuncelle(BigDecimal.valueOf(150));

        assertEquals(BigDecimal.valueOf(150), urun.getUrunFiyati());
        assertEquals(BigDecimal.valueOf(300), siparis.tutar());

        assertEquals(
                BigDecimal.valueOf(100),
                siparis.siparisleriListele().get(0).getBirimFiyati()
        );
    }

    @Test
    void siparisIptalEdildigindeStokGeriEklenmeli() {
        Siparis siparis = siparisServisi.olustur(901, sepet);

        assertEquals(7, urun.getUrunStok());

        siparisServisi.iptalEt(901);

        assertEquals(10, urun.getUrunStok());
        assertEquals(SiparisDurumu.IPTAL_EDILDI, siparis.getSiparisDurumu());

        assertTrue(siparisRepository.varMi(901));
        assertEquals(BigDecimal.valueOf(300), siparis.tutar());
    }

    @Test
    void iptalEdildigindeTumUrunlerinStoklariGeriEklenmeli() {
        Urun ikinciUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 5
        );

        urunRepository.ekle(ikinciUrun);
        sepet.urunEkle(ikinciUrun, 2);

        Siparis siparis = siparisServisi.olustur(901, sepet);

        assertEquals(7, urun.getUrunStok());
        assertEquals(3, ikinciUrun.getUrunStok());

        siparisServisi.iptalEt(901);

        assertEquals(10, urun.getUrunStok());
        assertEquals(5, ikinciUrun.getUrunStok());
        assertEquals(SiparisDurumu.IPTAL_EDILDI, siparis.getSiparisDurumu());
    }

    @Test
    void ayniSiparisIkinciKezIptalEdilememeli() {
        Siparis siparis = siparisServisi.olustur(901, sepet);
        siparisServisi.iptalEt(901);

        assertThrows(
                IllegalStateException.class,
                () -> siparisServisi.iptalEt(901)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(SiparisDurumu.IPTAL_EDILDI, siparis.getSiparisDurumu());
    }

    @Test
    void olmayanSiparisIptalEdilememeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.iptalEt(999)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(0, siparisRepository.listele().size());
    }

    @Test
    void iptalEdilmisSiparisinIdsiTekrarKullanilamamali() {
        Siparis ilkSiparis = siparisServisi.olustur(901, sepet);
        siparisServisi.iptalEt(901);

        Sepet yeniSepet = new Sepet();
        yeniSepet.urunEkle(urun, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.olustur(901, yeniSepet)
        );

        assertEquals(10, urun.getUrunStok());
        assertEquals(1, yeniSepet.sepetiListele().size());

        assertSame(ilkSiparis, siparisRepository.getir(901));
        assertEquals(
                SiparisDurumu.IPTAL_EDILDI,
                siparisRepository.getir(901).getSiparisDurumu()
        );
    }

    @Test
    void kayitliSiparisIdIleGetirilebilmeli() {
        Siparis olusturulanSiparis = siparisServisi.olustur(901, sepet);

        Siparis getirilenSiparis = siparisServisi.siparisGetir(901);

        assertSame(olusturulanSiparis, getirilenSiparis);
    }

    @Test
    void olmayanSiparisGetirilmekIstendigindeHataVermeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.siparisGetir(999)
        );
    }

    @Test
    void kayitliSiparislerListelenebilmeli() {
        Siparis ilkSiparis = siparisServisi.olustur(901, sepet);

        Sepet ikinciSepet = new Sepet();
        ikinciSepet.urunEkle(urun, 2);

        Siparis ikinciSiparis = siparisServisi.olustur(902, ikinciSepet);

        List<Siparis> siparisler = siparisServisi.siparisListele();

        assertEquals(2, siparisler.size());
        assertTrue(siparisler.contains(ilkSiparis));
        assertTrue(siparisler.contains(ikinciSiparis));
    }

    @Test
    void siparisYokkenBosListeDonmeli() {
        assertEquals(0, siparisServisi.siparisListele().size());
    }

    @Test
    void siparisListesiniBosaltmakRepositoryKayitlariniSilmemeli() {
        siparisServisi.olustur(901, sepet);

        List<Siparis> siparisler = siparisServisi.siparisListele();
        siparisler.clear();

        assertEquals(0, siparisler.size());
        assertEquals(1, siparisServisi.siparisListele().size());
        assertTrue(siparisRepository.varMi(901));
    }

    @Test
    void iptaldeBirUrunBulunamazsaHicbirStokDegismemeli() {
        UrunRepository testUrunRepository = new UrunRepository();
        SiparisRepository testSiparisRepository = new SiparisRepository();

        SiparisServisi testServis = new SiparisServisi(
                testUrunRepository,
                testSiparisRepository
        );

        Urun kayitliUrun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 7
        );
        Urun kayitsizUrun = new Urun(
                102, "Mouse", BigDecimal.valueOf(50), 8
        );

        testUrunRepository.ekle(kayitliUrun);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(101, new SiparisKalemi(kayitliUrun, 3));
        kalemler.put(102, new SiparisKalemi(kayitsizUrun, 2));

        Siparis siparis = new Siparis(901, kalemler);
        testSiparisRepository.ekle(siparis);

        assertThrows(
                IllegalArgumentException.class,
                () -> testServis.iptalEt(901)
        );

        assertEquals(7, kayitliUrun.getUrunStok());
        assertEquals(8, kayitsizUrun.getUrunStok());
        assertEquals(
                SiparisDurumu.OLUSTURULDU,
                siparis.getSiparisDurumu()
        );
    }

    @Test
    void olusturulduDurumundakiSiparislerListelenmeli() {
        Siparis ilkSiparis = siparisServisi.olustur(901, sepet);

        Sepet ikinciSepet = new Sepet();
        ikinciSepet.urunEkle(urun, 2);

        Siparis ikinciSiparis = siparisServisi.olustur(902, ikinciSepet);
        siparisServisi.iptalEt(902);

        List<Siparis> sonuc =
                siparisServisi.durumaGoreListele(SiparisDurumu.OLUSTURULDU);

        assertEquals(1, sonuc.size());
        assertSame(ilkSiparis, sonuc.get(0));
        assertFalse(sonuc.contains(ikinciSiparis));
    }

    @Test
    void iptalEdilmisSiparislerListelenmeli() {
        Siparis ilkSiparis = siparisServisi.olustur(901, sepet);

        Sepet ikinciSepet = new Sepet();
        ikinciSepet.urunEkle(urun, 2);

        Siparis ikinciSiparis = siparisServisi.olustur(902, ikinciSepet);
        siparisServisi.iptalEt(902);

        List<Siparis> sonuc =
                siparisServisi.durumaGoreListele(SiparisDurumu.IPTAL_EDILDI);

        assertEquals(1, sonuc.size());
        assertSame(ikinciSiparis, sonuc.get(0));
        assertFalse(sonuc.contains(ilkSiparis));
    }

    @Test
    void istenenDurumdaSiparisYoksaBosListeDonmeli() {
        siparisServisi.olustur(901, sepet);

        List<Siparis> sonuc =
                siparisServisi.durumaGoreListele(SiparisDurumu.IPTAL_EDILDI);

        assertTrue(sonuc.isEmpty());
    }

    @Test
    void nullSiparisDurumuReddedilmeli() {
        assertThrows(
                IllegalArgumentException.class,
                () -> siparisServisi.durumaGoreListele(null)
        );
    }
}