package model;

import com.mehmetyilmazalan.miniecommerce.model.Sepet;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SepetTest {
    @Test
    void ayniUrundenEklenirseMevcutKalemeEklenmeli(){
        Urun urun = new Urun(1,"Kitap", BigDecimal.valueOf(100),10);

        Sepet sepet= new Sepet();

        sepet.urunEkle(urun,2);
        sepet.urunEkle(urun,3);


        assertEquals(1,sepet.sepetiListele().size());
        assertEquals(5,sepet.sepetiListele().get(0).getUrunAdedi());

    }

    @Test
    void farkliUrunlerinTutarlariToplanmali(){
        Urun urun1 = new Urun(1,"defter", BigDecimal.valueOf(100),10);
        Urun urun2= new Urun(2,"silgi", BigDecimal.valueOf(50),10);

        Sepet sepet = new Sepet();

        sepet.urunEkle(urun1,2);
        sepet.urunEkle(urun2,3);

        assertEquals(BigDecimal.valueOf(350),sepet.tutar());

    }

    @Test
    void sepeteUrunEklemekStoguDegistirmemeli(){
        Urun urun1 = new Urun(101,"tişört", BigDecimal.valueOf(100),10);

        Sepet sepet = new Sepet();
        sepet.urunEkle(urun1,5);

        assertEquals(10,urun1.getUrunStok());
    }

    @Test
    void sepetBosaltildigindaKalemKalmamali(){
        Urun urun1 = new Urun(101,"tişört", BigDecimal.valueOf(100),10);

        Sepet sepet = new Sepet();
        sepet.urunEkle(urun1,5);

        sepet.sepetBosalt();

        assertEquals(0,sepet.sepetiListele().size());
    }

    @Test
    void sepetteOlmayanUrunCikarilmakIstendigindeHataVermeli() {
        Sepet sepet = new Sepet();

        assertThrows(
                IllegalArgumentException.class,
                () -> sepet.urunCikar(999)
        );
    }
}
