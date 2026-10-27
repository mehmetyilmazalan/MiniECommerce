package com.mehmetyilmazalan.miniecommerce.application;

import com.mehmetyilmazalan.miniecommerce.model.Sepet;
import com.mehmetyilmazalan.miniecommerce.model.SepetKalemi;
import com.mehmetyilmazalan.miniecommerce.model.Siparis;
import com.mehmetyilmazalan.miniecommerce.model.SiparisDurumu;
import com.mehmetyilmazalan.miniecommerce.model.SiparisKalemi;
import com.mehmetyilmazalan.miniecommerce.model.Urun;
import com.mehmetyilmazalan.miniecommerce.service.SiparisServisi;
import com.mehmetyilmazalan.miniecommerce.service.UrunServisi;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class KonsolUygulamasi {

    private static final boolean RENKLER_AKTIF = true;
    private static final int EKRAN_GENISLIGI = 88;

    private static final String SIFIRLA = "\u001B[0m";
    private static final String MAVI = "\u001B[94m";
    private static final String YESIL = "\u001B[92m";
    private static final String SARI = "\u001B[93m";
    private static final String KIRMIZI = "\u001B[91m";
    private static final String GRI = "\u001B[90m";
    private static final String KALIN = "\u001B[1m";

    private final UrunServisi urunServisi;
    private final SiparisServisi siparisServisi;
    private final Sepet sepet;

    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat paraFormati;

    private final DateTimeFormatter tarihFormati =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public KonsolUygulamasi(
            UrunServisi urunServisi,
            SiparisServisi siparisServisi,
            Sepet sepet
    ) {
        this.urunServisi = urunServisi;
        this.siparisServisi = siparisServisi;
        this.sepet = sepet;

        paraFormati = NumberFormat.getNumberInstance(
                Locale.forLanguageTag("tr")
        );

        paraFormati.setMinimumFractionDigits(2);
        paraFormati.setMaximumFractionDigits(2);
    }


    public void baslat() {
        try {
            karsilamaGoster();
            anaMenu();
        } catch (GirisSonlandiException exception) {
            bilgi("Konsol girişi sonlandırıldı.");
        }

        System.out.println();
        bilgi("Vitrin'den ayrıldınız. Yeniden görüşmek üzere!");
    }

    private void karsilamaGoster() {
        System.out.println();
        System.out.println(renklendir(
                ortala("V İ T R İ N"),
                MAVI + KALIN
        ));
        System.out.println(ortala(
                "Keşfet. Sepetine ekle. Siparişini takip et."
        ));
        System.out.println();

        bilgi("Vitrin'e hoş geldiniz.");
        bilgi("Mağaza örnek ürünlerle açılır.");
        bilgi("Veriler uygulama kapanınca silinir.");
    }


    private void anaMenu() {
        while (true) {
            baslik("MAĞAZA ANA SAYFASI");

            System.out.printf(
                    "  Katalog: %d ürün   |   Sepetim: %d kalem"
                            + "   |   Siparişlerim: %d%n",
                    urunServisi.urunListele().size(),
                    sepet.sepetiListele().size(),
                    siparisServisi.siparisListele().size()
            );

            System.out.println();

            secenek(1, "Katalog ve ürün yönetimi");
            secenek(2, "Alışveriş sepetim");
            secenek(3, "Siparişlerim");
            secenek(0, "Mağazadan çık");

            switch (menuSecimi()) {
                case 1 -> urunMenusu();
                case 2 -> sepetMenusu();
                case 3 -> siparisMenusu();
                case 0 -> {
                    if (onayAl(
                            "Mağazadan çıkmak istiyor musunuz? "
                                    + "Oturumdaki kayıtlar silinecek."
                    )) {
                        return;
                    }
                }
                default -> gecersizSecim();
            }
        }
    }


    private void urunMenusu() {
        while (true) {
            baslik("KATALOG VE ÜRÜN YÖNETİMİ");

            menuGrubu("KATALOĞU KEŞFEDİN");
            secenek(1, "Bütün ürünleri görüntüle");
            secenek(2, "Ürün ara");
            secenek(3, "Ürün detayını görüntüle");
            secenek(4, "Ürünleri sırala");

            menuGrubu("MAĞAZAYI YÖNETİN");
            secenek(5, "Yeni ürün ekle");
            secenek(6, "Ürün adını güncelle");
            secenek(7, "Ürün fiyatını güncelle");
            secenek(8, "Stok işlemleri");
            secenek(9, "Düşük stoklu ürünleri görüntüle");

            secenek(0, "Ana sayfaya dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(() ->
                        urunTablosuGoster(
                                urunServisi.urunListele(),
                                "Bütün ürünler"
                        )
                );
                case 2 -> islemYap(this::urunAra);
                case 3 -> islemYap(this::urunDetayiGoster);
                case 4 -> siralamaMenusu();
                case 5 -> islemYap(this::urunEkle);
                case 6 -> islemYap(this::urunAdiGuncelle);
                case 7 -> islemYap(this::urunFiyatiGuncelle);
                case 8 -> stokMenusu();
                case 9 -> islemYap(this::dusukStokListele);
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }

    private void siralamaMenusu() {
        while (true) {
            baslik("KATALOĞU SIRALA");

            secenek(1, "Fiyat: düşükten yükseğe");
            secenek(2, "Fiyat: yüksekten düşüğe");
            secenek(3, "Ürün adı: alfabetik");
            secenek(0, "Katalog menüsüne dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(() ->
                        urunTablosuGoster(
                                urunServisi.fiyataGoreArtanListele(),
                                "Fiyat: düşükten yükseğe"
                        )
                );
                case 2 -> islemYap(() ->
                        urunTablosuGoster(
                                urunServisi.fiyataGoreAzalanListele(),
                                "Fiyat: yüksekten düşüğe"
                        )
                );
                case 3 -> islemYap(() ->
                        urunTablosuGoster(
                                urunServisi.adaGoreListele(),
                                "Ürün adı: alfabetik"
                        )
                );
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }

    private void stokMenusu() {
        while (true) {
            baslik("STOK İŞLEMLERİ");

            secenek(1, "Stok artır");
            secenek(2, "Stok azalt");
            secenek(0, "Katalog menüsüne dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(() -> stokDegistir(true));
                case 2 -> islemYap(() -> stokDegistir(false));
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }


    private void urunAra() {
        islemBasligi("Ürün arama");

        String arananAd = metinAl("Aramak istediğiniz ürün");

        urunTablosuGoster(
                urunServisi.urunAra(arananAd),
                "Arama sonuçları: " + arananAd
        );
    }

    private void urunDetayiGoster() {
        islemBasligi("Ürün detayı");

        int urunId = tamSayiAl("Ürün numarası", 1);
        Urun urun = urunBul(urunId);

        System.out.println();
        bilgiSatiri("Ürün numarası", urun.getUrunId());
        bilgiSatiri("Ürün adı", urun.getUrunAdi());
        bilgiSatiri("Birim fiyat", para(urun.getUrunFiyati()));
        bilgiSatiri("Mevcut stok", urun.getUrunStok());

        if (urun.getUrunStok() == 0) {
            uyari("Bu ürün şu anda tükenmiş.");
        }
    }

    private void urunEkle() {
        islemBasligi("Yeni ürün ekleme");

        String ad = metinAl("Ürün adı");
        BigDecimal fiyat = pozitifFiyatAl("Birim fiyat");
        int stok = tamSayiAl("Başlangıç stoğu", 0);

        int urunId = siradakiUrunId();

        Urun yeniUrun = new Urun(urunId, ad, fiyat, stok);

        urunServisi.urunEkle(yeniUrun);

        basari(
                "Ürün kataloğa eklendi. "
                        + "Ürün numarası: " + urunId
        );
    }

    private void urunAdiGuncelle() {
        islemBasligi("Ürün adını güncelleme");

        int urunId = tamSayiAl("Ürün numarası", 1);
        Urun urun = urunBul(urunId);

        bilgiSatiri("Mevcut ad", urun.getUrunAdi());

        String yeniAd = metinAl("Yeni ürün adı");

        urunServisi.adGuncelle(urunId, yeniAd);

        basari("Ürün adı güncellendi: " + urun.getUrunAdi());
    }

    private void urunFiyatiGuncelle() {
        islemBasligi("Ürün fiyatını güncelleme");

        int urunId = tamSayiAl("Ürün numarası", 1);
        Urun urun = urunBul(urunId);

        bilgiSatiri("Ürün", urun.getUrunAdi());
        bilgiSatiri("Mevcut fiyat", para(urun.getUrunFiyati()));

        BigDecimal yeniFiyat = pozitifFiyatAl("Yeni fiyat");

        urunServisi.fiyatGuncelle(urunId, yeniFiyat);

        basari("Yeni fiyat: " + para(urun.getUrunFiyati()));
    }

    private void stokDegistir(boolean arttir) {
        islemBasligi(arttir ? "Stok artırma" : "Stok azaltma");

        int urunId = tamSayiAl("Ürün numarası", 1);
        Urun urun = urunBul(urunId);

        bilgiSatiri("Ürün", urun.getUrunAdi());
        bilgiSatiri("Mevcut stok", urun.getUrunStok());

        int miktar = tamSayiAl(
                arttir ? "Eklenecek miktar" : "Azaltılacak miktar",
                1
        );

        if (arttir) {
            urunServisi.stokArttir(urunId, miktar);
        } else {
            urunServisi.stokAzalt(urunId, miktar);
        }

        basari(
                "Stok güncellendi. Yeni stok: "
                        + urun.getUrunStok()
        );
    }

    private void dusukStokListele() {
        islemBasligi("Düşük stoklu ürünler");

        bilgi("Tükenmiş ürünleri görmek için stok sınırını 0 giriniz.");

        int sinir = tamSayiAl("Stok sınırı", 0);

        urunTablosuGoster(
                urunServisi.dusukStokluUrunleriListele(sinir),
                "Stoğu " + sinir + " ve altında olan ürünler"
        );
    }

    private Urun urunBul(int urunId) {
        for (Urun urun : urunServisi.urunListele()) {
            if (urun.getUrunId() == urunId) {
                return urun;
            }
        }

        throw new IllegalArgumentException(
                "Bu numarayla kayıtlı bir ürün bulunamadı: " + urunId
        );
    }


    private void sepetMenusu() {
        while (true) {
            baslik("ALIŞVERİŞ SEPETİM");

            System.out.println(
                    "  " + sepet.sepetiListele().size() + " kalem"
                            + "   |   Sepet toplamı: "
                            + para(sepet.tutar())
            );

            System.out.println();

            secenek(1, "Sepeti görüntüle");
            secenek(2, "Sepete ürün ekle");
            secenek(3, "Ürün adedini değiştir");
            secenek(4, "Sepetten ürün çıkar");
            secenek(5, "Sepeti boşalt");
            secenek(6, "Siparişi oluştur");
            secenek(0, "Ana sayfaya dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(this::sepetGoster);
                case 2 -> islemYap(this::sepeteUrunEkle);
                case 3 -> islemYap(this::sepetAdediGuncelle);
                case 4 -> islemYap(this::sepettenUrunCikar);
                case 5 -> islemYap(this::sepetBosalt);
                case 6 -> islemYap(this::siparisOlustur);
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }


    private void sepeteUrunEkle() {
        List<Urun> urunler = urunServisi.urunListele();

        if (urunler.isEmpty()) {
            bilgi("Katalog henüz boş. Önce bir ürün ekleyiniz.");
            return;
        }

        urunTablosuGoster(urunler, "Alışveriş kataloğu");

        islemBasligi("Sepete ürün ekleme");

        int urunId = tamSayiAl("Ürün numarası", 1);
        Urun urun = urunBul(urunId);

        bilgiSatiri("Ürün", urun.getUrunAdi());
        bilgiSatiri("Birim fiyat", para(urun.getUrunFiyati()));
        bilgiSatiri("Mevcut stok", urun.getUrunStok());

        int adet = tamSayiAl("Eklenecek adet", 1);

        sepet.urunEkle(urun, adet);

        basari(
                adet + " adet " + urun.getUrunAdi()
                        + " sepete eklendi."
        );

        bilgi("Sepet toplamı: " + para(sepet.tutar()));
        bilgi("Stok uygunluğu sipariş oluşturulurken kontrol edilir.");
    }

    private void sepetAdediGuncelle() {
        if (sepetBosMu()) {
            return;
        }

        sepetGoster();
        islemBasligi("Sepetteki adedi değiştirme");

        int urunId = tamSayiAl("Ürün numarası", 1);
        SepetKalemi kalem = sepetKalemiBul(urunId);

        bilgiSatiri("Ürün", kalem.getUrun().getUrunAdi());
        bilgiSatiri("Mevcut adet", kalem.getUrunAdedi());

        int yeniAdet = tamSayiAl("Yeni toplam adet", 1);

        sepet.urunAdediGuncelle(urunId, yeniAdet);

        basari("Ürün adedi güncellendi.");
        bilgi("Sepet toplamı: " + para(sepet.tutar()));
    }

    private void sepettenUrunCikar() {
        if (sepetBosMu()) {
            return;
        }

        sepetGoster();
        islemBasligi("Sepetten ürün çıkarma");

        int urunId = tamSayiAl("Ürün numarası", 1);
        SepetKalemi kalem = sepetKalemiBul(urunId);

        if (!onayAl(
                kalem.getUrun().getUrunAdi()
                        + " sepetten tamamen çıkarılsın mı?"
        )) {
            bilgi("İşlemden vazgeçildi.");
            return;
        }

        sepet.urunCikar(urunId);

        basari("Ürün sepetten çıkarıldı.");
    }

    private void sepetBosalt() {
        if (sepetBosMu()) {
            return;
        }

        sepetGoster();

        if (!onayAl("Sepetteki bütün ürünler kaldırılsın mı?")) {
            bilgi("İşlemden vazgeçildi. Sepetiniz korundu.");
            return;
        }

        sepet.sepetBosalt();

        basari("Sepet boşaltıldı.");
    }

    private void siparisOlustur() {
        if (sepetBosMu()) {
            return;
        }

        sepetGoster();

        if (!onayAl("Bu sepetle sipariş oluşturmak istiyor musunuz?")) {
            bilgi("Sipariş oluşturulmadı. Sepetiniz korundu.");
            return;
        }

        int siparisId = siradakiSiparisId();

        Siparis siparis = siparisServisi.olustur(siparisId, sepet);

        basari(
                "Siparişiniz oluşturuldu. Sipariş numarası: "
                        + siparisId
        );

        siparisDetayiYazdir(siparis);
    }

    private boolean sepetBosMu() {
        if (sepet.sepetiListele().isEmpty()) {
            bilgi("Sepetiniz şu anda boş.");
            return true;
        }

        return false;
    }

    private SepetKalemi sepetKalemiBul(int urunId) {
        for (SepetKalemi kalem : sepet.sepetiListele()) {
            if (kalem.getUrun().getUrunId() == urunId) {
                return kalem;
            }
        }

        throw new IllegalArgumentException(
                "Bu ürün sepetinizde bulunmuyor: " + urunId
        );
    }


    private void siparisMenusu() {
        while (true) {
            baslik("SİPARİŞLERİM");

            secenek(1, "Bütün siparişleri görüntüle");
            secenek(2, "Siparişleri durumuna göre filtrele");
            secenek(3, "Sipariş detayını görüntüle");
            secenek(4, "Sipariş iptal et");
            secenek(0, "Ana sayfaya dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(() ->
                        siparisTablosuGoster(
                                siparisServisi.siparisListele(),
                                "Bütün siparişler"
                        )
                );
                case 2 -> siparisFiltreMenusu();
                case 3 -> islemYap(this::siparisDetayiGoster);
                case 4 -> islemYap(this::siparisIptalEt);
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }

    private void siparisFiltreMenusu() {
        while (true) {
            baslik("SİPARİŞLERİ FİLTRELE");

            secenek(1, "Oluşturulan siparişler");
            secenek(2, "İptal edilen siparişler");
            secenek(0, "Siparişlerim menüsüne dön");

            switch (menuSecimi()) {
                case 1 -> islemYap(() ->
                        siparisTablosuGoster(
                                siparisServisi.durumaGoreListele(
                                        SiparisDurumu.OLUSTURULDU
                                ),
                                "Oluşturulan siparişler"
                        )
                );
                case 2 -> islemYap(() ->
                        siparisTablosuGoster(
                                siparisServisi.durumaGoreListele(
                                        SiparisDurumu.IPTAL_EDILDI
                                ),
                                "İptal edilen siparişler"
                        )
                );
                case 0 -> {
                    return;
                }
                default -> gecersizSecim();
            }
        }
    }


    private void siparisDetayiGoster() {
        islemBasligi("Sipariş detayı");

        int siparisId = tamSayiAl("Sipariş numarası", 1);

        Siparis siparis = siparisServisi.siparisGetir(siparisId);

        siparisDetayiYazdir(siparis);
    }

    private void siparisIptalEt() {
        islemBasligi("Sipariş iptali");

        int siparisId = tamSayiAl("Sipariş numarası", 1);

        Siparis siparis = siparisServisi.siparisGetir(siparisId);

        siparisDetayiYazdir(siparis);

        if (siparis.getSiparisDurumu() == SiparisDurumu.IPTAL_EDILDI) {
            uyari("Bu sipariş zaten iptal edilmiş.");
            return;
        }

        if (!onayAl(
                "Sipariş iptal edilsin ve ürünler stoğa geri eklensin mi?"
        )) {
            bilgi("İşlemden vazgeçildi.");
            return;
        }

        siparisServisi.iptalEt(siparisId);

        basari("Sipariş iptal edildi. Ürünler stoğa geri eklendi.");
    }


    private void urunTablosuGoster(
            List<Urun> urunler,
            String tabloBasligi
    ) {
        islemBasligi(tabloBasligi);

        if (urunler.isEmpty()) {
            bilgi("Gösterilecek ürün bulunamadı.");
            return;
        }

        System.out.printf(
                "  %-7s %-30s %17s %10s%n",
                "NO", "ÜRÜN", "FİYAT", "STOK"
        );

        inceAyirici();

        for (Urun urun : urunler) {
            String stok = urun.getUrunStok() == 0
                    ? "Tükendi"
                    : String.valueOf(urun.getUrunStok());

            System.out.printf(
                    "  %-7d %-30s %17s %10s%n",
                    urun.getUrunId(),
                    kisalt(urun.getUrunAdi(), 30),
                    para(urun.getUrunFiyati()),
                    stok
            );
        }

        System.out.println();
        bilgi(urunler.size() + " ürün gösteriliyor.");
    }

    private void sepetGoster() {
        islemBasligi("Sepet özeti");

        List<SepetKalemi> kalemler =
                new ArrayList<>(sepet.sepetiListele());

        if (kalemler.isEmpty()) {
            bilgi("Sepetiniz şu anda boş.");
            return;
        }

        kalemler.sort(
                Comparator.comparingInt(
                        kalem -> kalem.getUrun().getUrunId()
                )
        );

        kalemTablosuBasligi();

        long toplamAdet = 0;

        for (SepetKalemi kalem : kalemler) {
            Urun urun = kalem.getUrun();
            toplamAdet += kalem.getUrunAdedi();

            kalemSatiri(
                    urun.getUrunId(),
                    urun.getUrunAdi(),
                    urun.getUrunFiyati(),
                    kalem.getUrunAdedi(),
                    kalem.tutar()
            );
        }

        System.out.println();

        bilgi(
                kalemler.size() + " farklı ürün"
                        + " • " + toplamAdet + " adet"
        );

        toplamGoster("SEPET TOPLAMI", sepet.tutar());

        bilgi("Sepet tutarı ürünlerin güncel fiyatlarıyla hesaplanır.");
    }

    private void siparisTablosuGoster(
            List<Siparis> siparisler,
            String tabloBasligi
    ) {
        islemBasligi(tabloBasligi);

        if (siparisler.isEmpty()) {
            bilgi("Gösterilecek sipariş bulunamadı.");
            return;
        }

        List<Siparis> siraliSiparisler =
                new ArrayList<>(siparisler);

        siraliSiparisler.sort(
                Comparator.comparing(
                        Siparis::getOlusturulmaZamani
                ).reversed()
        );

        System.out.printf(
                "  %-8s %-21s %-18s %17s%n",
                "NO", "TARİH", "DURUM", "TOPLAM"
        );

        inceAyirici();

        for (Siparis siparis : siraliSiparisler) {
            System.out.printf(
                    "  %-8d %-21s %-18s %17s%n",
                    siparis.getSiparisId(),
                    siparis.getOlusturulmaZamani().format(tarihFormati),
                    durumMetni(siparis.getSiparisDurumu()),
                    para(siparis.tutar())
            );
        }

        System.out.println();
        bilgi(siparisler.size() + " sipariş gösteriliyor.");
    }

    private void siparisDetayiYazdir(Siparis siparis) {
        islemBasligi("Sipariş #" + siparis.getSiparisId());

        bilgiSatiri(
                "Oluşturulma zamanı",
                siparis.getOlusturulmaZamani().format(tarihFormati)
        );

        bilgiSatiri(
                "Sipariş durumu",
                durumMetni(siparis.getSiparisDurumu())
        );

        System.out.println();

        List<SiparisKalemi> kalemler =
                new ArrayList<>(siparis.siparisleriListele());

        kalemler.sort(
                Comparator.comparingInt(SiparisKalemi::getUrunId)
        );

        kalemTablosuBasligi();

        for (SiparisKalemi kalem : kalemler) {
            kalemSatiri(
                    kalem.getUrunId(),
                    kalem.getUrunAdi(),
                    kalem.getBirimFiyati(),
                    kalem.getUrunAdedi(),
                    kalem.siparisKalemToplam()
            );
        }

        System.out.println();

        toplamGoster("SİPARİŞ TOPLAMI", siparis.tutar());

        bilgi("Ürün adları ve fiyatları sipariş anındaki kayıtlardır.");
    }

    private void kalemTablosuBasligi() {
        System.out.printf(
                "  %-7s %-25s %15s %6s %17s%n",
                "NO", "ÜRÜN", "BİRİM FİYAT", "ADET", "TUTAR"
        );

        inceAyirici();
    }

    private void kalemSatiri(
            int urunId,
            String urunAdi,
            BigDecimal birimFiyat,
            int adet,
            BigDecimal tutar
    ) {
        System.out.printf(
                "  %-7d %-25s %15s %6d %17s%n",
                urunId,
                kisalt(urunAdi, 25),
                para(birimFiyat),
                adet,
                para(tutar)
        );
    }


    private int menuSecimi() {
        while (true) {
            System.out.println();
            bilgi("Yapmak istediğiniz işlemin numarasını giriniz.");
            System.out.print("  İşlem numarası › ");

            String giris = satirOku().strip();

            try {
                int secim = Integer.parseInt(giris);

                if (secim >= 0) {
                    return secim;
                }
            } catch (NumberFormatException exception) {
                // Geçersiz giriş uyarısı aşağıda gösterilir.
            }

            uyari("Menüdeki seçeneklerden birinin numarasını giriniz.");
        }
    }

    private String islemGirdisiAl(String alanAdi) {
        System.out.println();

        System.out.println(renklendir(
                "  -1  ‹  İşlemi iptal et ve önceki menüye dön",
                GRI
        ));

        System.out.println();
        System.out.println(renklendir("  " + alanAdi, KALIN));
        System.out.print("  › ");

        String giris = satirOku().strip();

        if (giris.equals("-1")) {
            throw new GeriDonException();
        }

        return giris;
    }

    private String metinAl(String alanAdi) {
        while (true) {
            String giris = islemGirdisiAl(alanAdi);

            if (!giris.isBlank()) {
                return giris;
            }

            uyari("Bu alan boş bırakılamaz.");
        }
    }

    private int tamSayiAl(String alanAdi, int enAz) {
        while (true) {
            String giris = islemGirdisiAl(alanAdi);

            try {
                int deger = Integer.parseInt(giris);

                if (deger >= enAz) {
                    return deger;
                }

                uyari("En az " + enAz + " değerini giriniz.");
            } catch (NumberFormatException exception) {
                uyari("Geçerli bir tam sayı giriniz.");
            }
        }
    }

    private BigDecimal pozitifFiyatAl(String alanAdi) {
        while (true) {
            String giris = islemGirdisiAl(
                    alanAdi + " — örnek: 1499,90"
            );

            try {
                BigDecimal fiyat = new BigDecimal(
                        giris.replace(',', '.')
                );

                if (fiyat.compareTo(BigDecimal.ZERO) > 0) {
                    return fiyat;
                }

                uyari("Fiyat sıfırdan büyük olmalıdır.");
            } catch (NumberFormatException exception) {
                uyari(
                        "Geçerli bir fiyat giriniz. "
                                + "Binlik ayırıcı kullanmayınız."
                );
            }
        }
    }

    private boolean onayAl(String mesaj) {
        System.out.println();
        System.out.println(renklendir("  " + mesaj, KALIN));
        System.out.println();

        secenek(1, "Onayla ve devam et");
        secenek(0, "Vazgeç");

        while (true) {
            System.out.print("\n  İşlem numarası › ");

            String cevap = satirOku().strip();

            if (cevap.equals("1")) {
                return true;
            }

            if (cevap.equals("0")) {
                return false;
            }

            uyari("Onaylamak için 1, vazgeçmek için 0 giriniz.");
        }
    }

    private String satirOku() {
        if (!scanner.hasNextLine()) {
            throw new GirisSonlandiException();
        }

        return scanner.nextLine();
    }


    private void islemYap(Runnable islem) {
        try {
            islem.run();
        } catch (GeriDonException exception) {
            bilgi("İşlem iptal edildi. Önceki menüye dönülüyor.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            hata(exception.getMessage());
        }
    }

    private int siradakiUrunId() {
        int enBuyukId = 0;

        for (Urun urun : urunServisi.urunListele()) {
            enBuyukId = Math.max(enBuyukId, urun.getUrunId());
        }

        if (enBuyukId == Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "Yeni ürün için kullanılabilir numara kalmadı."
            );
        }

        return enBuyukId + 1;
    }

    private int siradakiSiparisId() {
        int enBuyukId = 0;

        for (Siparis siparis : siparisServisi.siparisListele()) {
            enBuyukId = Math.max(
                    enBuyukId,
                    siparis.getSiparisId()
            );
        }

        if (enBuyukId == Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "Yeni sipariş için kullanılabilir numara kalmadı."
            );
        }

        return enBuyukId + 1;
    }


    private void baslik(String bolum) {
        System.out.println();
        System.out.println();

        System.out.println(renklendir(
                "─".repeat(EKRAN_GENISLIGI),
                MAVI
        ));

        System.out.println(renklendir(
                ortala("V İ T R İ N"),
                MAVI + KALIN
        ));

        System.out.println(renklendir(
                ortala(bolum),
                KALIN
        ));

        System.out.println(renklendir(
                "─".repeat(EKRAN_GENISLIGI),
                MAVI
        ));

        System.out.println();
    }

    private void menuGrubu(String metin) {
        System.out.println();
        System.out.println(renklendir("  " + metin, GRI));
        System.out.println();
    }

    private void secenek(int numara, String metin) {
        if (numara == 0) {
            System.out.println();

            System.out.println(renklendir(
                    "   0  ‹  " + metin,
                    GRI
            ));

            return;
        }

        System.out.printf(
                "  %s     %s%n",
                renklendir(
                        String.format("%2d", numara),
                        MAVI + KALIN
                ),
                metin
        );
    }

    private void islemBasligi(String metin) {
        System.out.println();
        System.out.println(renklendir("  " + metin, KALIN));

        System.out.println(renklendir(
                "  " + "─".repeat(36),
                GRI
        ));

        System.out.println();
    }

    private void bilgiSatiri(String etiket, Object deger) {
        System.out.printf(
                "  %-20s : %s%n",
                etiket,
                deger
        );
    }

    private void inceAyirici() {
        System.out.println(renklendir(
                "  " + "·".repeat(EKRAN_GENISLIGI - 4),
                GRI
        ));
    }

    private void toplamGoster(String etiket, BigDecimal toplam) {
        System.out.println(renklendir(
                "  " + etiket + "   " + para(toplam),
                KALIN
        ));

        System.out.println();
    }

    private void basari(String mesaj) {
        System.out.println();

        System.out.println(renklendir(
                "  ✓ İŞLEM TAMAMLANDI",
                YESIL + KALIN
        ));

        System.out.println("    " + mesaj);
    }

    private void bilgi(String mesaj) {
        System.out.println(renklendir("  " + mesaj, GRI));
    }

    private void uyari(String mesaj) {
        System.out.println();
        System.out.println(renklendir("  ! " + mesaj, SARI));
    }

    private void hata(String mesaj) {
        System.out.println();

        System.out.println(renklendir(
                "  ! İŞLEM TAMAMLANAMADI",
                KIRMIZI + KALIN
        ));

        System.out.println(renklendir(
                "    " + mesaj,
                SARI
        ));

        System.out.println();

        bilgi("Aşağıdaki menüden başka bir işlem seçebilirsiniz.");
    }

    private void gecersizSecim() {
        uyari("Bu menüde böyle bir seçenek bulunmuyor.");
    }

    private String durumMetni(SiparisDurumu durum) {
        return switch (durum) {
            case OLUSTURULDU -> "Oluşturuldu";
            case IPTAL_EDILDI -> "İptal edildi";
        };
    }

    private String para(BigDecimal tutar) {
        return paraFormati.format(tutar) + " $";
    }

    private String ortala(String metin) {
        int boslukSayisi = Math.max(
                0,
                (EKRAN_GENISLIGI - metin.length()) / 2
        );

        return " ".repeat(boslukSayisi) + metin;
    }

    private String kisalt(String metin, int uzunluk) {
        if (metin.length() <= uzunluk) {
            return metin;
        }

        return metin.substring(0, uzunluk - 1) + "…";
    }

    private String renklendir(String metin, String renk) {
        if (!RENKLER_AKTIF) {
            return metin;
        }

        return renk + metin + SIFIRLA;
    }

    private static class GeriDonException extends RuntimeException {
    }

    private static class GirisSonlandiException extends RuntimeException {
    }
}