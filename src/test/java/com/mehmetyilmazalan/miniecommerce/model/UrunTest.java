package com.mehmetyilmazalan.miniecommerce.model;

import com.mehmetyilmazalan.miniecommerce.model.Urun;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrunTest {

    @Test
    void stokKadarAzaltildigindaStokSifirOlmali() {
       //Hazırla
        Urun urun = new Urun(
                1, "Ayakkabı", BigDecimal.valueOf(500), 5
        );

        //İşlem yap
        urun.stokAzalt(5);

        //KOntrol Et
        //assertEquals(bekledigimDeger, gercekteOlusanDeger);
        assertEquals(0, urun.getUrunStok());

    }

    @Test
    void eklenenKadarStokArttirilmali(){
        Urun urun=new Urun(3,"Bot",BigDecimal.valueOf(100),5);

        urun.stokArttir(3);

        assertEquals(8,urun.getUrunStok());
    }

    @Test
    void stoktanFazlaAzaltildigindaHataVermeliVeStokKorunmali() {
        Urun urun = new Urun(
                2, "Şişe", BigDecimal.valueOf(100), 5
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urun.stokAzalt(6)
        );

        assertEquals(5, urun.getUrunStok());
    }

    @Test
    void hataliStokAzaltmaEngellenmeliVeStokKorunmali(){
        Urun urun = new Urun(
                4, "Laptop", BigDecimal.valueOf(100), 5
        );

        assertThrows(IllegalArgumentException.class , () -> urun.stokAzalt(0));

        assertEquals(5,urun.getUrunStok());
    }

    @Test
    void sifirFiyatReddedilmeliVeEskiFiyatKorunmali() {
        Urun urun = new Urun(
                5, "Klavye", BigDecimal.valueOf(100), 5
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urun.fiyatGuncelle(BigDecimal.ZERO)
        );

        assertEquals(BigDecimal.valueOf(100), urun.getUrunFiyati());
    }

    @Test
    void gecerliFiyatVerildigindeUrunFiyatiGuncellenmeli(){
        Urun urun = new Urun(
                6, "Mouse", BigDecimal.valueOf(100), 5
        );

        urun.fiyatGuncelle(BigDecimal.valueOf(150));

        assertEquals(BigDecimal.valueOf(150),urun.getUrunFiyati());

    }
    @Test
    void gecerliAdVerildigindeUrunAdiGuncellenmeli() {
        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        urun.adGuncelle("Mekanik Klavye");

        assertEquals("Mekanik Klavye", urun.getUrunAdi());
    }

    @Test
    void gecersizAdlarReddedilmeliVeEskiAdKorunmali() {
        Urun urun = new Urun(
                101, "Klavye", BigDecimal.valueOf(100), 10
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> urun.adGuncelle(null)
        );
        assertEquals("Klavye", urun.getUrunAdi());

        assertThrows(
                IllegalArgumentException.class,
                () -> urun.adGuncelle("")
        );
        assertEquals("Klavye", urun.getUrunAdi());

        assertThrows(
                IllegalArgumentException.class,
                () -> urun.adGuncelle("   ")
        );
        assertEquals("Klavye", urun.getUrunAdi());
    }

}