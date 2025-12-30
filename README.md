# 📚 Academic Citation Network Analysis & Visualization System

Akademik makalelerin atıf ağlarını analiz etmek ve görselleştirmek için geliştirilmiş JavaFX tabanlı kapsamlı graf analiz sistemi.

---

## 📑 İçindekiler

- [Genel Bakış](#-genel-bakış)
- [Özellikler](#-özellikler)
- [Sistem Gereksinimleri](#-sistem-gereksinimleri)
- [Kurulum](#-kurulum)
- [Kullanım Kılavuzu](#-kullanım-kılavuzu)
- [Sınıf Yapısı](#-sınıf-yapısı)
- [Graf Metrikleri](#-graf-metrikleri)
- [Algoritma Detayları](#-algoritma-detayları)
- [Görselleştirme](#-görselleştirme)
- [API Dokümantasyonu](#-api-dokümantasyonu)
- [Performans](#-performans)
- [Bilinen Sorunlar](#-bilinen-sorunlar)
- [Gelecek Geliştirmeler](#-gelecek-geliştirmeler)
- [Katkıda Bulunma](#-katkıda-bulunma)
- [Lisans](#-lisans)

---

## 🎯 Genel Bakış

Bu proje, akademik makalelerin atıf ağlarını **interaktif** olarak görselleştirerek araştırmacıların:

✅ Makaleler arası ilişkileri keşfetmesini  
✅ Etkili makaleleri ve araştırma topluluklarını belirlemesini  
✅ H-Index, H-Median, Betweenness Centrality gibi metrikleri hesaplamasını  
✅ K-Core ve H-Core analizleriyle çekirdek yapıları bulmasını  

sağlayan kapsamlı bir araçtır.

### 🎨 Arayüz Özellikleri
```
🎄 Noel Temalı Görselleştirme
├── ✨ LED ışıklandırmalı bilgi kutuları
├── 🎁 Hediye paketi tasarımlı bilgi kartları
├── 🧦 Noel çorapları ile süslenmiş istatistik paneli
├── ⭐ Yıldızlı uzay arka planı
└── 🎨 Renkli düğüm ve kenar gösterimleri
```

---

## 🚀 Özellikler

### Graf Analiz Metrikleri

#### 📊 Temel Metrikler
- **In-Degree**: Bir makalenin aldığı atıf sayısı
- **Out-Degree**: Bir makalenin verdiği referans sayısı
- **Toplam Düğüm/Kenar İstatistikleri**: Graf genelinde sayısal veriler

#### 🎓 Akademik Metrikler
- **H-Index**: Bir makalenin akademik etkisini ölçen standart metrik
- **H-Median**: H-Core düğümlerinin medyan atıf sayısı
- **H-Core**: Bir makaleyi en çok etkileyen çekirdek makaleler

#### 🌐 Ağ Metrikleri
- **Betweenness Centrality**: Bir düğümün ağdaki "köprü" rolü
- **K-Core Decomposition**: Sıkı bağlantılı alt grupları bulma

### Görselleştirme Özellikleri

#### 🎨 İnteraktif Kontroller
```
🖱️ Mouse Kontrolleri:
├── Hover: Makale bilgilerini görüntüle
├── Tek Tık: Düğüm seç/seçimi kaldır
├── Shift + Tık: Normal genişletme (komşuları ekle)
└── Ctrl + Tık / Sağ Tık: H-Core analizi ve genişletme
```

#### 🎨 Renk Kodlaması
```
Düğüm Renkleri:
├── 🔴 Kırmızı: Normal düğümler
├── 🔵 Mavi: H-Core düğümleri
├── 🟡 Sarı: K-Core düğümleri
├── 🟥 Crimson: Kök düğüm
└── 💗 DeepPink: Seçili düğümler

Kenar Renkleri:
├── 🟡 Altın: Normal referans kenarları
├── 🟣 Mor: H-Core bağlantıları
├── 🟠 Turuncu: K-Core bağlantıları
└── 🟢 Yeşil: Sıralı bağlantılar
```

---

## 💻 Sistem Gereksinimleri

### Minimum Gereksinimler
```
☑️ Java Development Kit (JDK) 11+
☑️ JavaFX 11+
☑️ Minimum 4GB RAM
☑️ 100MB disk alanı
```

### Önerilen Gereksinimler
```
✨ JDK 17 veya üzeri
✨ 8GB+ RAM (büyük graflar için)
✨ Çok çekirdekli işlemci
```

---

## 📦 Kurulum

### 1. Projeyi İndirin
```bash
git clone https://github.com/yourusername/citation-network-analysis.git
cd citation-network-analysis
```

### 2. Bağımlılıkları Yükleyin

#### Maven kullanıyorsanız:
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>17.0.2</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-graphics</artifactId>
    <version>17.0.2</version>
</dependency>
```

#### Gradle kullanıyorsanız:
```gradle
implementation 'org.openjfx:javafx-controls:17.0.2'
implementation 'org.openjfx:javafx-graphics:17.0.2'
```

### 3. Derleyin ve Çalıştırın
```bash
# Maven
mvn clean install
mvn javafx:run

# Gradle
gradle build
gradle run

# Manuel
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.graphics Main.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.graphics Main
```

---

## 📖 Kullanım Kılavuzu

### Başlangıç

#### 1. Graf Oluşturma
```java
// Graf nesnesi oluştur
Graph graph = new Graph();

// Makale ekle
Makale makale1 = new Makale("1", "Paper Title", 2023, 
    Arrays.asList("Author1", "Author2"), 
    Arrays.asList("ref1", "ref2"));
graph.addArticle(makale1);

// Kenar ekle (referans ilişkisi)
graph.addEdge("1", "ref1"); // makale1 -> ref1 referansı
```

#### 2. Görselleştirmeyi Başlat
```java
Canvas canvas = new Canvas(1200, 800);
Cizim cizim = new Cizim(graph, canvas);

// Kök düğümle başlat
cizim.grafBaslat("1");
```

### Temel İşlemler

#### 🔍 Graf Keşfi
```java
// Normal genişletme (komşuları göster)
// Shift + Tık ile kullanılır
cizim.genislet("dugumId");

// H-Core analizi ve genişletme
// Ctrl + Tık veya Sağ Tık ile kullanılır
cizim.hCoreAnalizVeGenislet("dugumId");
```

#### 📊 Metrik Hesaplama
```java
// H-Index hesapla
HIndexCalculator hCalc = new HIndexCalculator(graph);
int hIndex = hCalc.computeHIndex("dugumId");

// H-Median hesapla
HMedianCalculator hmCalc = new HMedianCalculator(graph);
int hMedian = hmCalc.computeHMedian("dugumId");

// Betweenness Centrality
cizim.betweennessCentralityHesapla();

// K-Core decomposition
cizim.kCoreHesapla(3); // k=3 için
```

### Klavye ve Mouse Kontrolleri
```
🖱️ Mouse İşlemleri:
├── Hover: Düğüm bilgilerini göster
├── Tek Tık: Düğüm seç/kaldır
├── Shift + Tık: Komşuları ekle
└── Ctrl/Sağ Tık: H-Core ekle

⌨️ Klavye Kısayolları:
├── ESC: Seçimleri temizle
├── Delete: Seçili düğümü gizle
└── Space: Yeniden çiz
```

---

## 🏗️ Sınıf Yapısı

### 📂 Proje Dizin Yapısı
```
src/
├── graph/
│   ├── Graph.java                 # Ana graf sınıfı
│   ├── HIndexCalculator.java      # H-Index hesaplamaları
│   └── HMedianCalculator.java     # H-Median & H-Core
├── gorsellestirme/
│   └── Cizim.java                 # JavaFX görselleştirme
├── model/
│   └── Makale.java                # Makale veri modeli
└── Main.java                      # Uygulama başlangıcı
```

### 📊 Sınıf Diyagramı
```
┌─────────────────┐
│     Graph       │
├─────────────────┤
│ - articles      │
│ - adjacencyList │
├─────────────────┤
│ + addArticle()  │
│ + addEdge()     │
│ + getInDegree() │
│ + kCore()       │
│ + betweenness() │
└────────┬────────┘
         │
         │ uses
         ▼
┌─────────────────────┐
│  HIndexCalculator   │
├─────────────────────┤
│ + computeHIndex()   │
│ + toplamHindex()    │
└─────────────────────┘
         │
         │ extends
         ▼
┌─────────────────────┐
│ HMedianCalculator   │
├─────────────────────┤
│ + computeHCore()    │
│ + computeHMedian()  │
└─────────────────────┘
```

---

## 📈 Graf Metrikleri

### 1️⃣ H-Index

#### Tanım
```
H-Index = h
Eğer bir makalenin h tane alıntılayanı varsa ve
bu h makale en az h kez alıntılanmışsa
```

#### Hesaplama Örneği
```
Makale X'i alıntılayan makaleler ve atıf sayıları:
[15, 10, 8, 5, 3, 1]

h=1: 6 makale ≥1 atıf ✅
h=2: 5 makale ≥2 atıf ✅
h=3: 4 makale ≥3 atıf ✅
h=4: 3 makale ≥4 atıf ✗

H-Index = 3
```

#### Kod Kullanımı
```java
HIndexCalculator calc = new HIndexCalculator(graph);
int hIndex = calc.computeHIndex("paper123");

// Detaylı sonuç
HIndexDetails details = calc.computeHIndexDetails("paper123");
System.out.println("H-Index: " + details.hIndex);
System.out.println("Alıntılayanlar: " + details.citingPapers);
```

### 2️⃣ H-Core

#### Tanım
```
H-Core = Bir makaleyi alıntılayan makaleler arasından
         en çok atıf alan ilk h tane makale
```

#### Hesaplama Örneği
```
Makale X'i alıntılayanlar:
├── A: 20 atıf
├── B: 15 atıf
├── C: 10 atıf
├── D: 5 atıf
└── E: 2 atıf

H-Index = 4 olduğuna göre
H-Core = {A, B, C, D} (ilk 4 makale)
```

#### Kod Kullanımı
```java
HMedianCalculator calc = new HMedianCalculator(graph);
Set<String> hCore = calc.computeHCoreForNode("paper123");

// H-Core'u grafa ekle
graph.expandNode("paper123", hCore);
```

### 3️⃣ H-Median

#### Tanım
```
H-Median = H-Core düğümlerinin atıf sayılarının medyanı
```

#### Hesaplama Örneği
```
H-Core atıf sayıları: [20, 15, 10, 5]

Medyan hesabı:
- Sıralı: [5, 10, 15, 20]
- Çift sayıda eleman: (10 + 15) / 2 = 12.5

H-Median = 12 (tam sayı)
```

#### Kod Kullanımı
```java
HMedianCalculator calc = new HMedianCalculator(graph);
int hMedian = calc.computeHMedian("paper123");
```

### 4️⃣ Betweenness Centrality

#### Tanım
```
BC(v) = Σ (v'den geçen en kısa yol sayısı) / (toplam düğüm çifti)
```

#### Algoritma
```
1. Her düğüm çifti için (O(n²))
2. BFS ile en kısa yolu bul (O(n+m))
3. Yolun ara düğümlerine +1 puan
4. Toplam çift sayısına böl (normalize)

Zaman Karmaşıklığı: O(n² × (n+m)) ≈ O(n³)
```

#### Hesaplama Örneği
```
Graf: A → B → C → D

Yollar:
├── A-C: A→[B]→C     (B'ye +1)
├── A-D: A→[B]→[C]→D (B'ye +1, C'ye +1)
└── B-D: B→[C]→D     (C'ye +1)

Ham skorlar: B=2, C=2
Normalize (6 çift): B=0.33, C=0.33
```

#### Kod Kullanımı
```java
// Manuel hesaplama
Map<String, Double> bcScores = 
    graph.computeBetweennessCentrality(nodes);

// Görselleştirmede
cizim.betweennessCentralityHesapla();
```

### 5️⃣ K-Core Decomposition

#### Tanım
```
k-Core = Derecesi en az k olan düğümlerin
         maksimal bağlantılı alt kümesi
```

#### Algoritma
```
1. Her düğümün derecesini hesapla
2. Derecesi < k olanları çıkar
3. Komşuların derecelerini güncelle
4. Tekrar et (değişiklik kalmayana kadar)
```

#### Hesaplama Örneği
```
k=2 için K-Core:

Başlangıç dereceler:
A: 3, B: 2, C: 2, D: 1

İterasyon 1: D'yi çıkar (derece=1)
A: 2, B: 2, C: 2

İterasyon 2: Değişiklik yok
2-Core = {A, B, C}
```

#### Kod Kullanımı
```java
Set<String> kCore = graph.kCoreDecomposition(nodes, 3);

// Görselleştirmede
cizim.kCoreHesapla(3);
cizim.kCoreSifirla(); // Temizle
```

---

## ⚙️ Algoritma Detayları

### BFS (Breadth-First Search)
```java
private List<String> bfsShortestPath(String start, String end) {
    Queue<String> queue = new LinkedList<>();
    Map<String, String> parent = new HashMap<>();
    
    queue.add(start);
    parent.put(start, null);
    
    while (!queue.isEmpty()) {
        String current = queue.poll();
        
        if (current.equals(end)) {
            // Yolu geri yapılandır
            return reconstructPath(parent, end);
        }
        
        for (String neighbor : getNeighbors(current)) {
            if (!parent.containsKey(neighbor)) {
                parent.put(neighbor, current);
                queue.add(neighbor);
            }
        }
    }
    
    return null; // Yol yok
}
```

**Zaman Karmaşıklığı:** O(V + E)  
**Kullanım:** Betweenness Centrality hesaplamasında

### K-Core Algoritması
```java
public Set<String> kCoreDecomposition(Set<String> nodes, int k) {
    // 1. Derece hesaplama
    Map<String, Integer> degrees = computeDegrees(nodes);
    
    Set<String> kCore = new HashSet<>(nodes);
    boolean changed = true;
    
    while (changed) {
        changed = false;
        Set<String> toRemove = new HashSet<>();
        
        // 2. Düşük dereceli düğümleri işaretle
        for (String node : kCore) {
            if (degrees.get(node) < k) {
                toRemove.add(node);
                changed = true;
            }
        }
        
        // 3. Çıkar ve dereceleri güncelle
        for (String node : toRemove) {
            kCore.remove(node);
            updateNeighborDegrees(node, degrees, kCore);
        }
    }
    
    return kCore;
}
```

**Zaman Karmaşıklığı:** O(V + E) × iterasyon sayısı  
**En Kötü Durum:** O(V × E)

---

## 🎨 Görselleştirme

### Canvas Layout
```
┌────────────────────────────────────────────────────────┐
│  ⭐ Yıldızlı Arka Plan                                  │
│                                                          │
│  ┌─────────────────┐                   ┌──────────────┐│
│  │ Graf İstatistik │                   │ BC Sonuçları ││
│  │ ├─ Düğüm: 150   │                   │ 1. Node: 0.4 ││
│  │ ├─ Kenar: 300   │        🔴         │ 2. Node: 0.3 ││
│  │ └─ H-Index: 25  │       / \         │ 3. Node: 0.2 ││
│  └─────────────────┘      🔴─🔴        └──────────────┘│
│                             │                            │
│  ┌─────────────────┐       🔴                           │
│  │ Seçili Düğüm    │                                    │
│  │ 🧦🧦🧦🧦🧦🧦🧦    │                                    │
│  │ ID: paper123    │                                    │
│  │ In-deg: 15      │                                    │
│  │ H-Index: 5      │                                    │
│  └─────────────────┘                                    │
│                                                          │
│  ┌─────────────────┐                                    │
│  │ Genel Metrikler │                                    │
│  │ H-Index: 45     │                                    │
│  │ H-Median: 12.5  │                                    │
│  └─────────────────┘                                    │
└────────────────────────────────────────────────────────┘
```

### Düğüm Yerleşimi

#### Dairesel Yerleşim (Circular Layout)
```java
// Merkez düğüm etrafında dairesel dizilim
double radius = 150;
double angleStep = 2 * Math.PI / neighborCount;

for (int i = 0; i < neighbors.size(); i++) {
    double angle = i * angleStep;
    double x = centerX + radius * Math.cos(angle);
    double y = centerY + radius * Math.sin(angle);
    positions.put(neighbor, new Point2D(x, y));
}
```

### Kenar Çizimi

#### Kavisli Ok Çizimi (Bézier Curve)
```java
// Uzak düğümler için kavisli kenar
double controlX = (x1 + x2) / 2;
double controlY = (y1 + y2) / 2 - distance * 0.2;

// Quadratic Bézier eğrisi
for (double t = 0; t <= 1; t += 0.02) {
    double x = (1-t)² * x1 + 2*(1-t)*t * controlX + t² * x2;
    double y = (1-t)² * y1 + 2*(1-t)*t * controlY + t² * y2;
    // Çiz
}
```

### Noel Teması Elementleri

#### LED Işıkları
```java
private void ledCiz(double x, double y, Color color) {
    // Parlama efekti
    gc.setFill(color.deriveColor(0, 1, 1, 0.3));
    gc.fillOval(x - 8, y - 8, 16, 16);
    
    // Ana LED
    gc.setFill(color);
    gc.fillOval(x - 5, y - 5, 10, 10);
    
    // Işık efekti
    gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
    gc.fillOval(x - 2, y - 2, 4, 4);
}
```

#### Noel Çorapları
```java
private void cizCorap(double x, double y, Color color1, Color color2) {
    // Çorap gövdesi
    gc.setFill(color1);
    gc.fillRoundRect(x, y + 5, 15, 20, 8, 8);
    
    // Topuk kısmı
    gc.fillOval(x + 7.5, y + 18, 10, 10);
    
    // Bilek
    gc.setFill(color2);
    gc.fillRect(x - 2, y, 19, 7);
    
    // Asma ipi
    gc.setStroke(Color.GOLD);
    gc.strokeLine(x + 7.5, y, x + 7.5, y - 5);
}
```

---

## 📚 API Dokümantasyonu

### Graph Sınıfı

#### Temel Metodlar
```java
// Düğüm/Kenar ekleme
public void addArticle(Makale makale)
public void addNode(String id)
public void addEdge(String from, String to)

// Sorgulama
public Makale getArticle(String id)
public List<String> getNeighbors(String id)
public Set<String> getNodes()

// Metrikler
public int getInDegree(String node)
public int getOutDegree(String node)
public List<String> getReferencedBy(String node)

// İstatistikler
public int getTotalNodes()
public int getTotalEdges()
public String getMostCitedArticle()
public String getMostReferencingArticle()
```

#### Gelişmiş Metodlar
```java
// K-Core
public Set<String> kCoreDecomposition(Set<String> nodes, int k)

// Betweenness Centrality
public Map<String, Double> computeBetweennessCentrality(Set<String> nodes)

// Graf genişletme
public void expandNode(String nodeId, Set<String> hCore)

// Yardımcı metodlar
public Set<String> getUndirectedNeighbors(String node)
public boolean hasNode(String id)
public boolean isEmpty()
```

### HIndexCalculator Sınıfı
```java
// Temel H-Index
public int computeHIndex(String node)

// Detaylı H-Index
public HIndexDetails computeHIndexDetails(String node)

// Toplam H-Index
public int toplamHindex()

// İç sınıf: Sonuç nesnesi
public static class HIndexDetails {
    public final int hIndex;
    public final List<String> citingPapers;
    public final List<Integer> citationCounts;
}
```

### HMedianCalculator Sınıfı
```java
// H-Core hesaplama
public Set<String> computeHCoreForNode(String node)

// H-Median hesaplama
public int computeHMedian(String node)

// Kombine sonuç
public HCoreAndMedian computeHCoreAndMedian(String node)

// Ortalama H-Median
public double toplamHmedian()

// İç sınıf: Sonuç nesnesi
public static class HCoreAndMedian {
    public final int hIndex;
    public final Set<String> hcore;
    public final int median;
}
```

### Cizim Sınıfı
```java
// Graf başlatma
public void grafBaslat(String kokDugumId)

// Genişletme
public void genislet(String dugum)
public void hCoreAnalizVeGenislet(String dugumId)

// Metrik hesaplama
public void kCoreHesapla(int k)
public void betweennessCentralityHesapla()

// Temizleme
public void kCoreSifirla()
public void bcTemizle()

// Yeniden çizim
public void yenidenciz()
public void ciz()
```

---

## ⚡ Performans

### Zaman Karmaşıklıkları

| İşlem | Karmaşıklık | 100 Düğüm | 1000 Düğüm |
|-------|-------------|-----------|------------|
| **addNode** | O(1) | <1ms | <1ms |
| **addEdge** | O(1) | <1ms | <1ms |
| **getInDegree** | O(V×E) | ~10ms | ~1s |
| **H-Index** | O(n log n) | ~5ms | ~50ms |
| **H-Core** | O(n log n) | ~5ms | ~50ms |
| **K-Core** | O(V×E) | ~50ms | ~5s |
| **Betweenness** | O(V³) | ~500ms | ~8min |

### Bellek Kullanımı
```
Graf Bellek Kullanımı:
├── Düğümler: ~200 bytes/düğüm
├── Kenarlar: ~100 bytes/kenar
├── Pozisyonlar: ~50 bytes/düğüm
└── Toplam: ~350 bytes/düğüm + 100 bytes/kenar

Örnek:
├── 1000 düğüm, 5000 kenar
└── ~350KB + 500KB = ~850KB
```

### Optimizasyon İpuçları

#### 1. In-Degree Cache
```java
// ❌ Yavaş: Her seferinde hesapla
public int getInDegree(String node) {
    int count = 0;
    for (String other : nodes) {
        if (adjacencyList.get(other).contains(node)) count++;
    }
    return count;
}

// ✅ Hızlı: Cache kullan
private Map<String, Integer> inDegreeCache = new HashMap<>();

public int getInDegree(String node) {
    return inDegreeCache.computeIfAbsent(node, this::computeInDegree);
}
```

#### 2. Paralel Hesaplama
```java
// Betweenness için paralel stream
bcScores = nodes.parallelStream()
    .collect(Collectors.toMap(
        node -> node,
        node -> computeBC(node)
    ));
```

#### 3. Sampling (Büyük Graflar)
```java
// Tüm çiftler yerine rastgele örnekleme
int sampleSize = Math.min(1000, totalPairs);
Random rand = new Random();

for (int i = 0; i < sampleSize; i++) {
    String source = randomNode();
    String target = randomNode();
    // BC hesapla
}
```

---

## ⚠️ Bilinen Sorunlar

### 1. Betweenness Centrality Performansı
```
Problem: O(n³) karmaşıklık, 1000+ düğümde çok yavaş
Çözüm: 
├── Brandes algoritmasını implementasyon (O(nm))
├── Paralel hesaplama
└── Sampling ile yaklaşık sonuç
```

### 2. Çoklu En Kısa Yollar
```
Problem: Sadece tek en kısa yol dikkate alınıyor
Durum:
    A
   / \
  B   C
   \ /
    D
    
A→D için 2 yol: A→B→D ve A→C→D
Kod sadece birini sayıyor

Çözüm: Brandes algoritması ile tüm yolları say
```

### 3. In-Degree Hesaplama
```
Problem: Her çağrıda O(V×E) hesaplama
Çözüm:
└── In-degree cache mekanizması ekle
```
### 4. Directed/Undirected Karışıklığı
```
Problem: Graf directed ama bazı hesaplamalar undirected
Örnek:
├── kCoreDecomposition: Yönsüz derece kullanıyor
└── getUndirectedNeighbors: Her iki yönü de dikkate alıyor
Çözüm: Graf türünü parametre olarak al
---

## 🔮 Gelecek Geliştirmeler

### Kısa Vadeli (v1.1)

- [ ] **In-Degree Cache**: Performans optimizasyonu
- [ ] **Progress Bar**: Uzun hesaplamalar için ilerleme göstergesi
- [ ] **Export/Import**: Graf verilerini JSON/GraphML formatında kaydetme
- [ ] **Zoom & Pan**: Canvas üzerinde yakınlaştırma ve kaydırma
- [ ] **Undo/Redo**: İşlemleri geri alma

### Orta Vadeli (v2.0)

- [ ] **Brandes Algoritması**: Daha hızlı BC hesaplama
- [ ] **PageRank**: Web graf analizi için
- [ ] **Community Detection**: Louvain, Girvan-Newman algoritmaları
- [ ] **Clustering Coefficient**: Yerel kümelenme katsayısı
- [ ] **Shortest Path Visualization**: En kısa yolu görsel olarak vurgulama

### Uzun Vadeli (v3.0)

- [ ] **3D Görselleştirme**: JavaFX 3D ile
- [ ] **Temporal Analysis**: Zaman içinde değişen graflar
- [ ] **Machine Learning Integration**: Graf embedding (Node2Vec)
- [ ] **Web Interface**: JavaFX yerine web tabanlı arayüz
- [ ] **Database Integration**: Neo4j, OrientDB desteği
```
## 📄 Lisans

Bu proje MIT Lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakınız.
MIT License
Copyright (c) 2024 [Zeynep Selenay Ökten]
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
