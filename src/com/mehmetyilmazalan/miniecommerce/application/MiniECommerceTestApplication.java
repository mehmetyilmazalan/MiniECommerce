package com.mehmetyilmazalan.miniecommerce.application;

import com.mehmetyilmazalan.miniecommerce.model.*;
import com.mehmetyilmazalan.miniecommerce.repository.SiparisRepository;
import com.mehmetyilmazalan.miniecommerce.repository.UrunRepository;
import com.mehmetyilmazalan.miniecommerce.service.SiparisServisi;
import com.mehmetyilmazalan.miniecommerce.service.UrunServisi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniECommerceTestApplication {
    public static void main(String[] args) {


        Urun urun1=new Urun(1,"Bot", BigDecimal.valueOf(30000),5);

        urun1.stokAzalt(3);
        System.out.println("Ürün stok adedi : " + urun1.getUrunStok());

        try {
            urun1.stokAzalt(3);
        } catch (IllegalArgumentException  exception) {
            System.out.println("HATA : "+ exception.getMessage()+ "\n" +"Kalan ürün adedi : " + urun1.getUrunStok() );
        }

        try {
            urun1.stokAzalt(0);
        } catch (IllegalArgumentException  exception) {
            System.out.println("HATA : "+ exception.getMessage()+ "\n" +"Kalan ürün adedi : " + urun1.getUrunStok() );
        }


        // Fiyat güncelleme testleri
        urun1.fiyatGuncelle(BigDecimal.valueOf(35_000));
        System.out.println("Güncel fiyat: " + urun1.getUrunFiyati());

// Sıfır fiyat
        try {
            urun1.fiyatGuncelle(BigDecimal.ZERO);
        } catch (IllegalArgumentException exception) {
            System.out.println("HATA: " + exception.getMessage());
        }
        System.out.println("Sıfır fiyat denemesinden sonra: " + urun1.getUrunFiyati());

// Negatif fiyat
        try {
            urun1.fiyatGuncelle(BigDecimal.valueOf(-100));
        } catch (IllegalArgumentException exception) {
            System.out.println("HATA: " + exception.getMessage());
        }
        System.out.println("Negatif fiyat denemesinden sonra: " + urun1.getUrunFiyati());

// Null fiyat
        try {
            urun1.fiyatGuncelle(null);
        } catch (IllegalArgumentException exception) {
            System.out.println("HATA: " + exception.getMessage());
        }
        System.out.println("Null fiyat denemesinden sonra: " + urun1.getUrunFiyati());


//Urun Repository Testleri

        UrunRepository urunRepository = new UrunRepository();
        Urun urun2 =new Urun(2,"Sneaker", BigDecimal.valueOf(50000),5);

        urunRepository.ekle(urun2);

        Urun urun3 =new Urun(2,"Çizme", BigDecimal.valueOf(70000),5);

        urunRepository.guncelle(urun3);
        System.out.println("ürünün fiyatı : "+ urunRepository.getir(2).getUrunFiyati());


        Urun urun4 =new Urun(2,"Tişört", BigDecimal.valueOf(70000),5);

        try {
            urunRepository.ekle(urun4);
        }catch (IllegalArgumentException exception){
            System.out.println("HATA : " + exception.getMessage());
        }

        Urun urun5 =new Urun(3,"Pantolon", BigDecimal.valueOf(70000),5);

        try {
            urunRepository.guncelle(urun5);
        }catch (IllegalArgumentException exception){
            System.out.println("HATA : "+ exception.getMessage());
        }

        urunRepository.ekle(urun5);

        List<Urun> urunler = urunRepository.listele();
        urunler.clear();
        System.out.println("Urun Test Listesi boyutu : " + urunler.size());

        System.out.println("Urun Repository Listesi Boyutu: "+ urunRepository.listele().size());

        UrunServisi urunServisi = new UrunServisi(urunRepository);

        Urun urun6 =new Urun(6,"Ceket", BigDecimal.valueOf(70000),5);
        urunRepository.ekle(urun6);

        urunServisi.stokArttir(6,3);
        System.out.println("Arttırılmış Ürün stoku : " + urunRepository.getir(6).getUrunStok());

        urunServisi.stokAzalt(6,2);
        System.out.println("Azaltılmış Ürün stoku : " + urunRepository.getir(6).getUrunStok());

        urunServisi.stokAzalt(6,6);
        System.out.println("Azaltılmış Ürün stoku : " + urunRepository.getir(6).getUrunStok());

        Urun urun7 =new Urun(7,"Ayakkabı", BigDecimal.valueOf(500),5);
        Urun urun8 =new Urun(8,"Pijama", BigDecimal.valueOf(500),5);
        Urun urun9 =new Urun(9,"Şapka", BigDecimal.valueOf(500),5);

        SepetKalemi sepetKalemi1=new SepetKalemi(urun7,3);
        System.out.println("Sepet Kalemi Tutarı : " + sepetKalemi1.tutar() + "$");

        try {
            SepetKalemi sepetKalemi2=new SepetKalemi(null,1);

        }catch (IllegalArgumentException exception){
            System.out.println("HATA : " + exception.getMessage());
        }

        try {
            SepetKalemi sepetKalemi3=new SepetKalemi(urun7,0);

        }catch (IllegalArgumentException exception){
            System.out.println("HATA : " + exception.getMessage());
        }

        // Sepet ekleme ve toplam tutar testleri
        Sepet testSepeti = new Sepet();

        Urun sepetTestUrun1 = new Urun(
                101, "Fare", BigDecimal.valueOf(500), 10
        );

        Urun sepetTestUrun2 = new Urun(
                102, "Klavye", BigDecimal.valueOf(200), 5
        );

// Boş sepet
        System.out.println("Boş sepet tutarı: " + testSepeti.tutar());

// İlk üründen 2 adet ekleme
        testSepeti.urunEkle(sepetTestUrun1, 2);
        System.out.println("İlk eklemeden sonra: " + testSepeti.tutar());

// Aynı üründen 3 adet daha ekleme
        testSepeti.urunEkle(sepetTestUrun1, 3);
        System.out.println("Aynı ürünü tekrar ekledikten sonra: " + testSepeti.tutar());

// Farklı üründen 1 adet ekleme
        testSepeti.urunEkle(sepetTestUrun2, 1);
        System.out.println("Farklı ürünü ekledikten sonra: " + testSepeti.tutar());

// Mağaza stoklarının korunması
        System.out.println(
                "Birinci ürünün stoku korundu mu? "
                        + (sepetTestUrun1.getUrunStok() == 10)
        );

        System.out.println(
                "İkinci ürünün stoku korundu mu? "
                        + (sepetTestUrun2.getUrunStok() == 5)
        );


        // Sepet adet güncelleme ve ürün çıkarma testleri
        Sepet adetTestSepeti = new Sepet();

        Urun adetTestUrunu = new Urun(
                201, "Ayakkabı", BigDecimal.valueOf(500), 10
        );

// 5 adet ekleme
        adetTestSepeti.urunEkle(adetTestUrunu, 5);
        System.out.println(
                "5 adet eklendi, toplam: " + adetTestSepeti.tutar()
        );

// Adedi 2 olarak güncelleme
        adetTestSepeti.urunAdediGuncelle(201, 2);
        System.out.println(
                "Adet 2 yapıldı, toplam: " + adetTestSepeti.tutar()
        );

// Geçersiz adetle güncelleme
        try {
            adetTestSepeti.urunAdediGuncelle(201, 0);
        } catch (IllegalArgumentException exception) {
            System.out.println("HATA: " + exception.getMessage());
        }

        System.out.println(
                "Geçersiz güncellemeden sonra toplam: " + adetTestSepeti.tutar()
        );

// Ürünü sepetten çıkarma
        adetTestSepeti.urunCikar(201);
        System.out.println(
                "Ürün çıkarıldı, toplam: " + adetTestSepeti.tutar()
        );

// Aynı ürünü tekrar çıkarma
        try {
            adetTestSepeti.urunCikar(201);
        } catch (IllegalArgumentException exception) {
            System.out.println("HATA: " + exception.getMessage());
        }

// Mağaza stokunu kontrol etme
        System.out.println(
                "Mağaza stoku korundu mu? "
                        + (adetTestUrunu.getUrunStok() == 10)
        );



        Urun siparisTestUrunu1 = new Urun(
                301, "Şişe", BigDecimal.valueOf(500), 10
        );

        SiparisKalemi siparisKalemi1=new SiparisKalemi(siparisTestUrunu1,2);
        System.out.println("Sipariş Kalemi toplamı : "+ siparisKalemi1.siparisKalemToplam());

        siparisTestUrunu1.fiyatGuncelle(BigDecimal.valueOf(700));
        System.out.println("Sipariş edilen ürünün güncel fiyatı : "+ siparisTestUrunu1.getUrunFiyati());

        System.out.println("Sipariş Kalemi toplamı Test : "+ siparisKalemi1.siparisKalemToplam());

        System.out.println("Sipariş edilen ürünün birim fiyatı : "+ siparisKalemi1.getBirimFiyati());



        //SİPARİŞ İPTAL TESTLERİ

        Urun siparisIptalTestUrunu1 = new Urun(
                401, "Borcam", BigDecimal.valueOf(500), 10
        );

        SiparisKalemi siparisKalemi2 = new SiparisKalemi(siparisIptalTestUrunu1,5);
        Map<Integer,SiparisKalemi> siparisKalemiMap =new HashMap<>();
        siparisKalemiMap.put(401,siparisKalemi2);
        Siparis siparis=new Siparis(501, siparisKalemiMap);

        try {
            System.out.println("Sipariş Aktif Mi ? : "+ siparis.getSiparisDurumu());
        }catch(IllegalStateException exception){
            System.out.println("Sipariş Durumu : "+ exception.getMessage());
        }

        try {
            siparis.siparisIptalEt();
        }catch (IllegalStateException exception){
            System.out.println("Sipariş Durumu : "+ exception.getMessage());
        }

        try {
            siparis.siparisIptalEt();
        }catch (IllegalStateException exception){
            System.out.println(siparis.getSiparisDurumu());
            System.out.println("Sipariş Durumu : "+ exception.getMessage());
        }


        //SİPARİŞ SERVİSİ TEST
        Urun siparisServisiTestUrun1=new Urun(601,"Pet şişe", BigDecimal.valueOf(500),10);

        UrunRepository testServisUrunRepository=new UrunRepository();
        SiparisRepository testServisSiparisRepository=new SiparisRepository();

        Sepet testServisSepet=new Sepet();

        testServisUrunRepository.ekle(siparisServisiTestUrun1);
        testServisSepet.urunEkle(siparisServisiTestUrun1,5);



        SiparisServisi siparisServisi=new SiparisServisi(testServisUrunRepository,testServisSiparisRepository);

        Siparis olusturulanTestSiparisi= siparisServisi.olustur(901,testServisSepet);


        System.out.println("Ürün stok : "+testServisUrunRepository.getir(601).getUrunStok());
        System.out.println("Sipariş Toplamı: " + olusturulanTestSiparisi.tutar());
        System.out.println("Sipariş Durumu : " + olusturulanTestSiparisi.getSiparisDurumu());
        System.out.println("Sepetteki kalem sayısı : " + testServisSepet.sepetiListele().size());
        System.out.println("Sipariş Kaydedilmiş Mi : "+ testServisSiparisRepository.varMi(901));


        // Yetersiz stokta sipariş oluşturma testi
        Urun yeterliStokluUrun = new Urun(701, "Bardak", BigDecimal.valueOf(500), 10);

        Urun yetersizStokluUrun = new Urun(702, "Tabak", BigDecimal.valueOf(400), 1);

        testServisUrunRepository.ekle(yeterliStokluUrun);
        testServisUrunRepository.ekle(yetersizStokluUrun);

        Sepet yetersizStokTestSepeti = new Sepet();
        yetersizStokTestSepeti.urunEkle(yeterliStokluUrun, 3);
        yetersizStokTestSepeti.urunEkle(yetersizStokluUrun, 2);

        try {
            siparisServisi.olustur(902, yetersizStokTestSepeti);
            System.out.println("HATA: Yetersiz stoğa rağmen sipariş oluşturuldu!");
        } catch (IllegalArgumentException exception) {
            System.out.println("Sipariş oluşturulamadı: " + exception.getMessage());
        }

        System.out.println("Birinci ürünün stoku: " + testServisUrunRepository.getir(701).getUrunStok());

        System.out.println("İkinci ürünün stoku: " + testServisUrunRepository.getir(702).getUrunStok());

        System.out.println("Sepetteki kalem sayısı: "+ yetersizStokTestSepeti.sepetiListele().size());

        System.out.println("902 numaralı sipariş kayıtlı mı? " + testServisSiparisRepository.varMi(902));


        // Sipariş servisi iptal testleri
        System.out.println("İptal öncesi stok: "+ testServisUrunRepository.getir(601).getUrunStok());

        // İlk iptal
        siparisServisi.iptalEt(901);

        System.out.println("İptal sonrası stok: " + testServisUrunRepository.getir(601).getUrunStok());

        System.out.println("İptal sonrası sipariş durumu: " + testServisSiparisRepository.getir(901).getSiparisDurumu());

// Aynı siparişi tekrar iptal etme
        try {
            siparisServisi.iptalEt(901);
            System.out.println("HATA: Sipariş ikinci kez iptal edildi!");
        } catch (IllegalStateException exception) {
            System.out.println("Tekrar iptal engellendi: " + exception.getMessage());
        }

        System.out.println("Tekrar iptal denemesinden sonra stok: "+ testServisUrunRepository.getir(601).getUrunStok());

        System.out.println("Son sipariş durumu: "+ testServisSiparisRepository.getir(901).getSiparisDurumu());


        // Aynı sipariş ID'siyle tekrar sipariş oluşturma testi
        Urun tekrarSiparisUrunu = testServisUrunRepository.getir(601);
        int tekrarTestOncekiStok = tekrarSiparisUrunu.getUrunStok();

        Siparis oncekiSiparisKaydi = testServisSiparisRepository.getir(901);

        Sepet tekrarSiparisSepeti = new Sepet();
        tekrarSiparisSepeti.urunEkle(tekrarSiparisUrunu, 2);

        try {
            siparisServisi.olustur(901, tekrarSiparisSepeti);
            System.out.println("HATA: Aynı ID ile tekrar sipariş oluşturuldu!");
        } catch (IllegalArgumentException exception) {
            System.out.println("Tekrar sipariş engellendi: " + exception.getMessage());
        }

        System.out.println(
                "Ürün stoku korundu mu? "
                        + (testServisUrunRepository.getir(601).getUrunStok()
                        == tekrarTestOncekiStok)
        );

        System.out.println(
                "Sepetteki kalem sayısı: "
                        + tekrarSiparisSepeti.sepetiListele().size()
        );

        System.out.println(
                "Sepet toplamı: " + tekrarSiparisSepeti.tutar()
        );

        System.out.println(
                "Mevcut sipariş kaydı korundu mu? "
                        + (testServisSiparisRepository.getir(901)
                        == oncekiSiparisKaydi)
        );

        System.out.println(
                "Mevcut sipariş durumu: "
                        + testServisSiparisRepository.getir(901).getSiparisDurumu()
        );


    }






}
