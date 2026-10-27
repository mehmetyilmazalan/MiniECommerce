# Vitrin | Mini E-Commerce 🛍️

Java ile geliştirilen konsol tabanlı bir e-ticaret uygulaması. Ürün, sepet ve sipariş yönetimini katmanlı bir yapı üzerinden sunar.

## Özellikler

- Ürün ekleme, arama ve listeleme
- Ürün adı, fiyat ve stok güncelleme
- Fiyat ve ürün adına göre sıralama
- Düşük stoklu ürünleri görüntüleme
- Sepete ürün ekleme, çıkarma ve adet değiştirme
- Sepet ve sipariş toplamı hesaplama
- Stok kontrolüyle sipariş oluşturma
- Sipariş iptali ve stok iadesi
- Sipariş anındaki ürün adı ve fiyatını koruma
- Sipariş detayları, oluşturulma zamanı ve durum filtreleme
- Renkli konsol menüleri ve hatalı giriş kontrolü

## Teknolojiler

Java 17 · Maven · JUnit 5 · Java Collections · BigDecimal

## Proje Yapısı

- `model`: Modeller ve iş kuralları
- `repository`: Bellekte veri saklama
- `service`: Ürün ve sipariş işlemleri
- `application`: Konsol arayüzü ve başlangıç sınıfı
- `test`: JUnit testleri

## Çalıştırma

JDK 17 veya üzeri gereklidir. Projeyi Maven projesi olarak açıp `MiniECommerceApplication` sınıfını çalıştırın.

Terminal üzerinden:

```bash
mvn compile
java -cp target/classes com.mehmetyilmazalan.miniecommerce.application.MiniECommerceApplication
```

## Testler

```bash
mvn test
```

Testler; model kurallarını, repository işlemlerini, sepet hesaplamalarını, sipariş oluşturma ve iptal senaryolarını kapsar.

## Kullanım

- Alt menülerde `0`: Bir üst menüye döner.
- Bilgi girişinde `-1`: İşlemi iptal eder.
- Onay ekranında `1`: Onaylar, `0`: Vazgeçer.

Uygulama örnek ürünlerle başlar. Veriler bellekte tutulur ve uygulama kapanınca silinir. Fiyatlar dolar ($) olarak gösterilir; gerçek ödeme işlemi yapılmaz.

## Geliştirici

[Mehmet Yılmaz Alan](https://github.com/mehmetyilmazalan)