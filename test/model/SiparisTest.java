package model;

import com.mehmetyilmazalan.miniecommerce.model.Siparis;
import com.mehmetyilmazalan.miniecommerce.model.SiparisDurumu;
import com.mehmetyilmazalan.miniecommerce.model.SiparisKalemi;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SiparisTest {
    @Test
    void kaynakMapBosaltilsaBileSiparisKalemleriKorunmali() {

        Urun urun = new Urun(101,"Elmas", BigDecimal.valueOf(100),10);

        SiparisKalemi siparisKalemi=new SiparisKalemi(urun,2);

        Map<Integer,SiparisKalemi> siparisKalemiMap = new HashMap<>();
        siparisKalemiMap.put(101,siparisKalemi);

        Siparis siparis= new Siparis(1,siparisKalemiMap);

        siparisKalemiMap.clear();

        assertEquals(1,siparis.siparisleriListele().size());

    }

    @Test
    void donenListeBosaltilsaBileSiparisKalemleriKorunmali(){

        Urun urun = new Urun(101,"Elmas", BigDecimal.valueOf(100),10);

        SiparisKalemi siparisKalemi=new SiparisKalemi(urun,2);

        Map<Integer,SiparisKalemi> siparisKalemiMap = new HashMap<>();
        siparisKalemiMap.put(101,siparisKalemi);

        Siparis siparis= new Siparis(1,siparisKalemiMap);
        siparis.siparisleriListele().clear();

        assertEquals(1,siparis.siparisleriListele().size());

    }

    @Test
    void iptalEdilenSiparisinDurumuIptalEdildiOlmali(){

        Urun urun = new Urun(101,"Elmas", BigDecimal.valueOf(100),10);

        SiparisKalemi siparisKalemi=new SiparisKalemi(urun,2);

        Map<Integer,SiparisKalemi> siparisKalemiMap = new HashMap<>();
        siparisKalemiMap.put(101,siparisKalemi);

        Siparis siparis= new Siparis(1,siparisKalemiMap);

        siparis.siparisIptalEt();

        assertEquals(SiparisDurumu.IPTAL_EDILDI,siparis.getSiparisDurumu());

    }

    @Test
    void iptalEdilmisSiparisTekrarIptalEdilememeli() {

        Urun urun = new Urun(101,"Elmas", BigDecimal.valueOf(100),10);

        SiparisKalemi siparisKalemi= new SiparisKalemi(urun,2);

        Map<Integer,SiparisKalemi> siparisKalemiMap = new HashMap<>();
        siparisKalemiMap.put(101,siparisKalemi);

        Siparis siparis = new Siparis(1,siparisKalemiMap);

        siparis.siparisIptalEt();
        assertThrows(IllegalStateException.class, ()-> siparis.siparisIptalEt());

        assertEquals(SiparisDurumu.IPTAL_EDILDI,siparis.getSiparisDurumu());

    }

    @Test
    void yeniSiparisinDurumuOlusturulduOlmali() {
        Urun urun = new Urun(
                101, "Elmas", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi kalem = new SiparisKalemi(urun, 2);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(urun.getUrunId(), kalem);

        Siparis siparis = new Siparis(1, kalemler);

        assertEquals(
                SiparisDurumu.OLUSTURULDU,
                siparis.getSiparisDurumu()
        );
    }

    @Test
    void siparisIdDogruDonmeli() {
        Urun urun = new Urun(
                101, "Elmas", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi kalem = new SiparisKalemi(urun, 2);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(urun.getUrunId(), kalem);

        Siparis siparis = new Siparis(901, kalemler);

        assertEquals(901, siparis.getSiparisId());
    }

    @Test
    void siparisTutariTumKalemlerinToplamiOlmali() {
        Urun urun1 = new Urun(
                101, "Defter", BigDecimal.valueOf(100), 10
        );
        Urun urun2 = new Urun(
                102, "Silgi", BigDecimal.valueOf(50), 10
        );

        SiparisKalemi kalem1 = new SiparisKalemi(urun1, 2);
        SiparisKalemi kalem2 = new SiparisKalemi(urun2, 3);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(urun1.getUrunId(), kalem1);
        kalemler.put(urun2.getUrunId(), kalem2);

        Siparis siparis = new Siparis(1, kalemler);

        assertEquals(BigDecimal.valueOf(350), siparis.tutar());
    }

    @Test
    void sifirIdIleSiparisOlusturulamamali() {
        Urun urun = new Urun(
                101, "Elmas", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi kalem = new SiparisKalemi(urun, 2);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(urun.getUrunId(), kalem);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Siparis(0, kalemler)
        );
    }

    @Test
    void negatifIdIleSiparisOlusturulamamali() {
        Urun urun = new Urun(
                101, "Elmas", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi kalem = new SiparisKalemi(urun, 2);

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(urun.getUrunId(), kalem);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Siparis(-1, kalemler)
        );
    }

    @Test
    void nullKalemMapiIleSiparisOlusturulamamali() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Siparis(1, null)
        );
    }

    @Test
    void bosKalemMapiIleSiparisOlusturulamamali() {
        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Siparis(1, kalemler)
        );
    }

    @Test
    void siparisOlusturulurkenZamanOtomatikKaydedilmeli() {
        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(101, new SiparisKalemi(urun, 2));

        LocalDateTime once = LocalDateTime.now();

        Siparis siparis = new Siparis(901, kalemler);

        LocalDateTime sonra = LocalDateTime.now();

        assertNotNull(siparis.getOlusturulmaZamani());
        assertFalse(siparis.getOlusturulmaZamani().isBefore(once));
        assertFalse(siparis.getOlusturulmaZamani().isAfter(sonra));
    }

    @Test
    void siparisIptalEdilseBileOlusturulmaZamaniDegismemeli() {
        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        Map<Integer, SiparisKalemi> kalemler = new HashMap<>();
        kalemler.put(101, new SiparisKalemi(urun, 2));

        Siparis siparis = new Siparis(901, kalemler);
        LocalDateTime ilkZaman = siparis.getOlusturulmaZamani();

        siparis.siparisIptalEt();

        assertEquals(ilkZaman, siparis.getOlusturulmaZamani());
    }






}




























