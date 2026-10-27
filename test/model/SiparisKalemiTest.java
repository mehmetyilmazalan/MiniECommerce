package model;

import com.mehmetyilmazalan.miniecommerce.model.SiparisKalemi;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SiparisKalemiTest {
    @Test
    void urunFiyatiDegisseBileSiparisKalemininFiyatiKorunmali() {
        Urun urun = new Urun(
                1, "Klavye", BigDecimal.valueOf(100), 5
        );
        SiparisKalemi siparisKalemi = new SiparisKalemi(urun, 2);

        urun.fiyatGuncelle(BigDecimal.valueOf(150));

        assertEquals(
                BigDecimal.valueOf(100),
                siparisKalemi.getBirimFiyati()
        );
        assertEquals(
                BigDecimal.valueOf(200),
                siparisKalemi.siparisKalemToplam()
        );


    }
    @Test
    void urunAdiDegisseBileSiparisKalemindekiAdKorunmali() {
        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        SiparisKalemi siparisKalemi = new SiparisKalemi(urun, 2);

        urun.adGuncelle("Mekanik Klavye");

        assertEquals("Mekanik Klavye", urun.getUrunAdi());
        assertEquals("Klavye", siparisKalemi.getUrunAdi());
    }

}
