package gorsellestirme;
import graph.Graph;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.*;
import model.Makale;
import graph.HIndexCalculator;
import graph.HMedianCalculator;

public class Cizim {
    private Map<String, Double> bcSkorlar = new HashMap<>();
    private boolean bcGosterim = false;
    private Graph graf;
    private Canvas canvas;
    private GraphicsContext grafik;
    private Map<String, Point2D> konumlar = new HashMap<>();
    private Random random=new Random(); 
    //bilgi kartı gösterilecek dugum
    private String gosterilecekDugum=null;
    private String sontiklananDugum=null;
    private Map<String, Boolean> Dugumler = new HashMap<>();
    private Set<String> gorunurDugumler = new HashSet<>();
    private String kokDugum = null;
    private boolean hCoreGosterim = false;
    private Set<String> hCoreDugumler = new HashSet<>();
    private int suankiHIndex = 0;
    private int suankiHMedian = 0;
    private Set<String> kCoreDugumler = new HashSet<>();
    private int mevcutK = 0;
    public Cizim(Graph graf,Canvas canvas){
        this.graf=graf;
        this.canvas=canvas;
        this.grafik=canvas.getGraphicsContext2D();
        mouseDugum();
        tiklama();
    }
    private void hCoreHesapla() {
    hCoreDugumler.clear();
    if (kokDugum == null) {
        System.out.println("kok dugum secilemedi");
        return;
    }
    HIndexCalculator hiCalc=new HIndexCalculator(graf);
    suankiHIndex=hiCalc.computeHIndex(kokDugum);
    HMedianCalculator hmCalc=new HMedianCalculator(graf);
    suankiHMedian=hmCalc.computeHMedian(kokDugum);
    hCoreDugumler=hmCalc.computeHCoreForNode(kokDugum);
}
   private void hCoreGorsellestir() {
    if (hCoreDugumler == null || hCoreDugumler.isEmpty() || kokDugum == null) {
        return;
    }
    
    Color anaMor = Color.rgb(138, 43, 226); 
    Color parlamaMor = Color.rgb(138, 43, 226, 0.4);

    for (String hCoreDugum : hCoreDugumler) {
        if (!gorunurDugumler.contains(hCoreDugum)) continue;
        if (hCoreDugum.equals(kokDugum)) continue; 
        
        List<String> komsular = graf.getNeighbors(hCoreDugum);
        if (komsular == null || !komsular.contains(kokDugum)) continue;
    
        Point2D p1 = konumlar.get(hCoreDugum);
        Point2D p2 = konumlar.get(kokDugum);
        if (p1 == null || p2 == null) continue;
        
        double x1 = p1.getX() + 10;
        double y1 = p1.getY() + 10;
        double x2 = p2.getX() + 10;
        double y2 = p2.getY() + 10;
    
        grafik.setStroke(parlamaMor);
        grafik.setLineWidth(8);
        grafik.strokeLine(x1, y1, x2, y2);
        
        grafik.setStroke(anaMor);
        grafik.setLineWidth(3);
        grafik.strokeLine(x1, y1, x2, y2);

        double aci = Math.atan2(y2 - y1, x2 - x1);
        double okGeriX = x2 - 12 * Math.cos(aci); 
        double okGeriY = y2 - 12 * Math.sin(aci);
        cizOkBasi(okGeriX, okGeriY, x2, y2, anaMor);
    }
}
    public void yenidenciz(){
        ciz();
    }
    private Set<String> secilenDugumler = new HashSet<>();
    public void grafBaslat(String kokDugumId) {
    if (kokDugumId==null||kokDugumId.trim().isEmpty()){
        System.out.println("dugum id girin lutfen");
        return;
    }
    if (!graf.getNodes().contains(kokDugumId)) {
        System.out.println("dugum bulanamadi");
        return;
    }
    kokDugum = kokDugumId;
    gorunurDugumler.clear();
    konumlar.clear();
    secilenDugumler.clear();
    hCoreDugumler.clear();
    hCoreGosterim = false;
    suankiHIndex = 0;
    suankiHMedian = 0;
    gorunurDugumler.add(kokDugum);
    double merkezX=canvas.getWidth()/2;
    double merkezY=canvas.getHeight()/2;
    konumlar.put(kokDugum,new Point2D(merkezX, merkezY));
    ciz();
}
//shifte basılı tutarak tıklarsan düğüm ekler.ctrl+tık yaparsan hcore ekler
 private void tiklama() {
    canvas.setOnMouseClicked(event -> {
        if (kokDugum == null) return;
        double mouseX = event.getX();
        double mouseY = event.getY();
        String tiklananDugum = DugumBul(mouseX, mouseY);       
        if (tiklananDugum != null) {
            if (event.isShiftDown()) {
                System.out.println("Normal Genişletiliyor: " + tiklananDugum);
                genislet(tiklananDugum);
            } 
            else if (event.isControlDown() || event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                System.out.println("H-Core Analizi ve Genişletme: " + tiklananDugum);
                hCoreAnalizVeGenislet(tiklananDugum);
            } 
            else {
                if (secilenDugumler.contains(tiklananDugum)) {
                    secilenDugumler.remove(tiklananDugum);
                    sontiklananDugum = null;
                } else {
                    secilenDugumler.add(tiklananDugum);
                    sontiklananDugum = tiklananDugum;
                }
            }
            ciz();
        }
    });
}
 private void hCoreAnalizVeGenislet(String dugumId) {
    kokDugum = dugumId;
    hCoreGosterim = true;
    hCoreHesapla(); 
    if (hCoreDugumler == null || hCoreDugumler.isEmpty()) {
        System.out.println("Bu düğüm için H-Core bulunamadı.");
        return;
    }
    Point2D merkezKonum = konumlar.get(dugumId);
    if (merkezKonum == null) return;
    List<String> yeniHCoreDugumler = new ArrayList<>();
    for (String hId : hCoreDugumler) {
        if (!gorunurDugumler.contains(hId)) {
            yeniHCoreDugumler.add(hId);
        }
    }
    double yaricap = 200; 
    double aciArtis = 2 * Math.PI / Math.max(1, yeniHCoreDugumler.size());
    for (int i = 0; i < yeniHCoreDugumler.size(); i++) {
        String komsu = yeniHCoreDugumler.get(i);
        double aci = i * aciArtis; 
        double x = merkezKonum.getX() + yaricap * Math.cos(aci);
        double y = merkezKonum.getY() + yaricap * Math.sin(aci);
        x = Math.max(50, Math.min(canvas.getWidth() - 300, x));
        y = Math.max(50, Math.min(canvas.getHeight() - 50, y));
        konumlar.put(komsu, new Point2D(x, y));
        gorunurDugumler.add(komsu);
    }
   
}
private void genislet(String dugum) {
    System.out.println("Düğüm: " + dugum);
    List<String> komsular = graf.getNeighbors(dugum);
    System.out.println("Bu makaleye referans verenler: " + komsular);
    if (komsular == null || komsular.isEmpty()) {
        System.out.println("BU MAKALEYE KİMSE REFERANS VERMEMİŞ!");
        return;
    }
    Point2D merkezKonum = konumlar.get(dugum);
    if (merkezKonum == null) {
        System.out.println("KONUM BULUNAMADI!");
        return;
    }
    List<String> yeniKomsular = new ArrayList<>();
    for (String komsu : komsular) {
        if (!gorunurDugumler.contains(komsu)) {
            yeniKomsular.add(komsu);
        }
    }
    System.out.println("Yeni komşular: " + yeniKomsular);
    if (yeniKomsular.isEmpty()) {
        System.out.println("TÜM KOMŞULAR ZATEN GÖRÜNÜR!");
        return;
    }
    yeniKomsular.sort((a, b) -> {
        try {
            return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    });
    double yaricap = 150; 
    double aciArtis = 2 * Math.PI / yeniKomsular.size();
    for (int i = 0; i < yeniKomsular.size(); i++) {
        String komsu = yeniKomsular.get(i);
        double aci = i * aciArtis; 
        double x = merkezKonum.getX() + yaricap * Math.cos(aci);
        double y = merkezKonum.getY() + yaricap * Math.sin(aci);
        x = Math.max(50, Math.min(canvas.getWidth() - 50, x));
        y = Math.max(50, Math.min(canvas.getHeight() - 50, y));
        konumlar.put(komsu, new Point2D(x, y));
        gorunurDugumler.add(komsu);
    }
} 
    private String DugumBul(double mouseX, double mouseY){
        for (String dugum : gorunurDugumler) {
        Point2D konum = konumlar.get(dugum);
        if (konum == null) continue;
        double merkezX = konum.getX() + 10;
        double merkezY = konum.getY() + 10;
        double dx = mouseX - merkezX;
        double dy = mouseY - merkezY;
        double mesafe = Math.sqrt(dx * dx + dy * dy);
        if (mesafe < 10) {
            return dugum;
        }
    }
    return null;
    }
    private void mouseDugum(){
        //Mouse hareketini ayarladım
        canvas.setOnMouseMoved(event->{
            //Mouse konumunu aldım.
            double mouseX=event.getX();
            double mouseY=event.getY();
            gosterilecekDugum=DugumBul(mouseX, mouseY);
            ciz();
        });
        canvas.setOnMouseExited(event->{
            gosterilecekDugum=null;
            ciz();
        });
    }
    public void ciz(){
        // Arka plan
        grafik.setFill(Color.web("#1a2332"));
        grafik.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        cizyildiz();
        cizkar();
        if (kokDugum != null) {
            yesilokciz();
            cizKenar();
             if (hCoreGosterim) {
            hCoreGorsellestir();
        }
            cizDugum();
           
        }
        hIndexVeHMedianKutusu();
        istatistikCiz();
        if (gosterilecekDugum!=null) {
            BilgiKartiCiz(gosterilecekDugum);
        }
        if (sontiklananDugum!=null) {
            tiklananDugumTablo(sontiklananDugum);
        }
        if (bcGosterim && !bcSkorlar.isEmpty()) {
            bcSonuclariniCiz();
        }
        if (kokDugum == null) {
            grafik.setFill(Color.rgb(255, 255, 255, 0.9));
            grafik.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            grafik.fillText("Başlamak için bir düğüm ID girin ve 'Başlat' butonuna tıklayın", 
                canvas.getWidth() / 2 - 300, canvas.getHeight() / 2);
        }
    }
private void cizKenar(){
    for (String dugum1:gorunurDugumler) {
        List<String> komsular=graf.getNeighbors(dugum1);
        if (komsular==null) continue;
        
        for (String dugum2:komsular) {
            if (!gorunurDugumler.contains(dugum2)) continue;
            
            // K-core kenarlarını kontrol et
            boolean kCoreKenar = (mevcutK > 0 && 
                                 kCoreDugumler.contains(dugum1) && 
                                 kCoreDugumler.contains(dugum2));
            
            Point2D p1=konumlar.get(dugum1);
            Point2D p2=konumlar.get(dugum2);
            if (p1==null || p2==null) continue;
            
            double x1=p1.getX()+10;
            double y1=p1.getY()+10;
            double x2=p2.getX()+10;
            double y2=p2.getY()+10;
            double mesafe=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
            
            if (kCoreKenar) {
                grafik.setStroke(Color.rgb(255, 140, 0, 0.8));
                grafik.setLineWidth(5);
                grafik.strokeLine(x1, y1, x2, y2);
            } else if (mesafe > 150) {
                cizKavisliSariOk(x1, y1, x2, y2, mesafe);
            } else {
                grafik.setStroke(Color.rgb(255,215,0,0.3));
                grafik.setLineWidth(4);
                grafik.strokeLine(x1,y1,x2,y2);
                grafik.setStroke(Color.rgb(255,223,186));
                grafik.setLineWidth(1);
                grafik.strokeLine(x1,y1,x2,y2);
                double aci = Math.atan2(y2 - y1, x2 - x1);
                double okX = x2 - 12 * Math.cos(aci);
                double okY = y2 - 12 * Math.sin(aci);
                cizOkBasi(okX, okY, x2, y2, Color.rgb(255, 223, 186));
            }
        }
    }
}
private void cizKavisliSariOk(double x1, double y1, double x2, double y2, double mesafe) {
    double ortaX=(x1+x2)/2;
    double ortaY=(y1+y2)/2;
    double kavisMiktari=mesafe*0.2;
    double kontrolX=ortaX;
    double kontrolY=ortaY-kavisMiktari;
    int sayi= 30;
    double oncekiX=x1;
    double oncekiY=y1;
    
    for(int i=1;i<=sayi;i++){
        double a=(double)i/sayi;
        double suankiX=Math.pow(1-a,2)*x1+2*(1-a)*a*kontrolX+ Math.pow(a,2)*x2;
        double suankiY=Math.pow(1-a,2)*y1+2*(1-a)*a*kontrolY+Math.pow(a, 2)*y2;
        grafik.setStroke(Color.rgb(255, 215, 0, 0.3));
        grafik.setLineWidth(4);
        grafik.strokeLine(oncekiX, oncekiY, suankiX, suankiY);
        grafik.setStroke(Color.rgb(255, 223, 186));
        grafik.setLineWidth(1);
        grafik.strokeLine(oncekiX, oncekiY, suankiX, suankiY);
        oncekiX = suankiX;
        oncekiY = suankiY;
    }
    double ok=0.95;
    double okX=Math.pow(1-ok, 2)*x1+2*(1-ok)*ok*kontrolX+Math.pow(ok, 2)*x2;
    double okY=Math.pow(1-ok, 2)*y1+2*(1-ok)*ok*kontrolY+Math.pow(ok, 2)*y2;
    cizOkBasi(okX, okY, x2, y2, Color.rgb(255, 223, 186));
}
    private void cizDugum(){
    Color[] dugumRenkleri = {
        Color.rgb(220,20,60)
    };
    int renkIndex=0;
    for (String dugum:gorunurDugumler) {
        Point2D p=konumlar.get(dugum);
        if (p==null) continue;
        
        Color renkSecim;
        if (kCoreDugumler.contains(dugum) && mevcutK > 0) {
           
            renkSecim = Color.rgb(255, 215, 0);
        }
        else if (hCoreGosterim && hCoreDugumler.contains(dugum)) {
          
            renkSecim = Color.rgb(30, 144, 255);
        }
        else if (dugum.equals(kokDugum)) {
           
            renkSecim = Color.rgb(220, 20, 60); 
        } 
        else if (secilenDugumler.contains(dugum)) {
        
            renkSecim = Color.DEEPPINK; 
        } 
        else {
           
            renkSecim = Color.rgb(220, 20, 60); 
        }
        // Düğüme parlama efekti
        grafik.setFill(Color.rgb(
            (int)(renkSecim.getRed()*255),
            (int)(renkSecim.getGreen()*255),
            (int)(renkSecim.getBlue()*255),
            0.4));
        grafik.fillOval(p.getX()-5,p.getY()- 5,30,30);
        // Ana düğüm
        grafik.setFill(renkSecim);
        grafik.fillOval(p.getX(), p.getY(), 20, 20);
        // Işık efekti
        grafik.setFill(Color.rgb(255, 255, 255, 0.6));
        grafik.fillOval(p.getX() + 4, p.getY() + 4, 8, 8);
        // Üstteki çizgi
        grafik.setStroke(Color.rgb(184, 134, 11));
        grafik.setLineWidth(2);
        grafik.strokeLine(p.getX() + 10, p.getY() - 5, p.getX() + 10, p.getY());
        // Düğüm ID yazısı
        grafik.setFill(Color.rgb(0, 0, 0));
        grafik.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        String idText = urldenSonKisim(dugum);
        if (idText.length() > 5) {
            idText = idText.substring(0, 5);
        }
        grafik.fillText(idText, p.getX() + 1, p.getY() + 13);
        renkIndex++;
    }
}
    private void ledCiz(double x,double y,Color renk){
        //ledlerin dışına parlaklık ekledim
        grafik.setFill(Color.rgb(
            (int)(renk.getRed() * 255),
            (int)(renk.getGreen() * 255),
            (int)(renk.getBlue() * 255),
            0.3));
        grafik.fillOval(x - 8, y - 8, 16, 16);
        //ledin rengi
        grafik.setFill(renk);
        grafik.fillOval(x - 5, y - 5, 10, 10);
        //ledlerin üstüne ışık efekti
        grafik.setFill(Color.rgb(255, 255, 255, 0.7));
        grafik.fillOval(x - 2, y - 2, 4, 4);
    }
    private void cizyildiz(){
        //her defasında aynı yıldızları çizmek için
        random.setSeed(12345);
        for(int i=0;i<100;i++){
            double x = random.nextDouble() * canvas.getWidth();
            double y = random.nextDouble() * canvas.getHeight();
            double boyut = random.nextDouble() * 2 + 1;
            grafik.setFill(Color.rgb(255, 255, 255, random.nextDouble() * 0.5 + 0.3));
            grafik.fillOval(x, y, boyut, boyut);
        }
    }
    private void cizkar(){
        random.setSeed(54321);
        for(int i=0;i<100;i++){
            double x = random.nextDouble() * canvas.getWidth();
            double y = random.nextDouble() * canvas.getHeight();
            double boyut = random.nextDouble() * 4 + 2;
            grafik.setFill(Color.rgb(255, 255, 255, random.nextDouble() * 0.6 + 0.4));
            grafik.fillOval(x, y, boyut, boyut);
            if(boyut > 3){
                grafik.setFill(Color.rgb(200, 230, 255, 0.8));
                grafik.fillOval(x + boyut/3, y + boyut/3, boyut/2, boyut/2);
            }
        }
    }
    private void ledler(double kutuX, double kutuY, double kutuGenislik, double kutuYukseklik){
        Color[] renkler ={
            Color.rgb(255, 50, 50),    
            Color.rgb(50, 255, 50),    
            Color.rgb(255, 215, 0),    
            Color.rgb(100, 149, 237),  
            Color.rgb(255, 255, 255),  
            Color.rgb(255, 140, 0)     
        };
        //üst kenar için ledler
        int ledSayi=12;
        double aralik=kutuGenislik/(ledSayi-1);
        for(int i=0;i<ledSayi;i++){
            double x = kutuX + (i * aralik);
            //sin ile dalga efekti vermiş oldu
            double y=kutuY-2+Math.sin(i*0.5)*3;
            Color renk =renkler[i%renkler.length];
            ledCiz(x, y, renk);
        }
        //sağ kenar için ledler
        int ledSayisag=8;
        double sagAralik = kutuYukseklik / ( ledSayisag- 1);
        for(int i=0;i<ledSayisag;i++){
            double x = kutuX + kutuGenislik + 2 + Math.sin(i * 0.5) * 3;
            double y = kutuY + (i * sagAralik);
            Color renk = renkler[(i + 3) % renkler.length];
            ledCiz(x, y, renk);
        }
        //alt kenar için ledler
        int ledSayiAlt=12;
        for(int i=0;i<ledSayiAlt;i++){
            double x = kutuX + kutuGenislik - (i * aralik);
            double y = kutuY + kutuYukseklik + 2 + Math.sin(i * 0.5) * 3;
            Color renk = renkler[(i + 2) % renkler.length];
            ledCiz(x, y, renk);
        }
        //sol kenar için ledler
        int ledSayiSol=8;
        for(int i=0;i<ledSayiSol;i++){
            double x = kutuX - 2 + Math.sin(i * 0.5) * 3;
            double y = kutuY + kutuYukseklik - (i * sagAralik);
            Color renk = renkler[(i + 4) % renkler.length];
            ledCiz(x, y, renk);
        }
        grafik.setStroke(Color.rgb(80, 80, 80));
        grafik.setLineWidth(1);
        grafik.strokeRect(kutuX, kutuY, kutuGenislik, kutuYukseklik);   
    }
    private void istatistikCiz(){
        double kutuX=15.0;
        double kutuY=10.0;
        double kutuGenislik=280;
        double kutuYukseklik=180;
        ledler(kutuX, kutuY, kutuGenislik, kutuYukseklik);
        //kutu arka plan
        grafik.setFill(Color.rgb(240, 248, 255, 0.95));
        grafik.fillRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
        // Kutu kenarlığı 
        grafik.setStroke(Color.rgb(218, 165, 32));
        grafik.setLineWidth(3);
        grafik.strokeRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
        // Başlık -
        grafik.setFill(Color.rgb(178, 34, 34));
        grafik.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        grafik.fillText("GRAF İSTATİSTİKLERİ", kutuX + 15, kutuY + 25);
        // Ayırıcı çizgi 
        grafik.setStroke(Color.rgb(34, 139, 34));
        grafik.setLineWidth(2);
        grafik.strokeLine(kutuX + 10, kutuY + 35, kutuX + kutuGenislik - 10, kutuY + 35);
        // İstatistikler
        grafik.setFill(Color.rgb(139, 0, 0));
        grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        double y=kutuY+50;
        double satirAraligi=20;
        grafik.fillText("Toplam Makale Sayısı: " + graf.getTotalNodes(), kutuX + 15, y);
        y += satirAraligi;
        grafik.fillText("Toplam Referans Sayısı: " + graf.getTotalEdges(), kutuX + 15, y);
        y += satirAraligi;
        grafik.fillText("Toplam Alınan Referans: " + graf.getTotalReferencesReceived(), kutuX + 15, y);
        y += satirAraligi;
        grafik.fillText("Toplam Verilen Referans: " + graf.getTotalReferencesGiven(), kutuX + 15, y);
        y += satirAraligi + 5;
        // İkinci ayırıcı çizgi
        grafik.setStroke(Color.rgb(34, 139, 34));
        grafik.setLineWidth(2);
        grafik.strokeLine(kutuX + 10, y - 5, kutuX + kutuGenislik - 10, y - 5);
        // En çok referans alan/veren
        grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        grafik.fillText("En Çok Referans Alan:"+urldenSonKisim(graf.getMostCitedArticle()), kutuX + 15, y + 10);
        y += satirAraligi + 10;
        grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        grafik.fillText("En Çok Referans Veren:"+urldenSonKisim(graf.getMostReferencingArticle()), kutuX + 15, y + 10);  
    }
    private String urldenSonKisim(String url) {
    if (url == null) return "";
    int sonSlash = url.lastIndexOf('/');
    if (sonSlash != -1 && sonSlash + 1 < url.length()) {
        return url.substring(sonSlash + 1); 
    }
    return url; 
}
    private void BilgiKartiCiz(String dugum){
        final int yazar= 40;
        final int baslik= 45;
        Makale makale=graf.getArticle(dugum);
        if(makale==null){
            return;
        }
        int atifSayisi=graf.getInDegree(dugum);
        //bilgi kartı boyutları
        double kartGenislik = 280;
        double kartYukseklik = 140;
        Point2D dugumKonum=konumlar.get(dugum);
        if(dugumKonum==null){
            return;
        }
        //kartın yerleşim yeri
        double bilgikartX=dugumKonum.getX()+30;
        double bilgikartY=dugumKonum.getY();
        //ekrandan taşma durumunu engellemek için
        if(bilgikartX + kartGenislik > canvas.getWidth()){
        bilgikartX = dugumKonum.getX() - kartGenislik - 10;
        }
        if(bilgikartY < 0){
        bilgikartY = 10;
        }
        if(bilgikartY+kartYukseklik > canvas.getHeight()){
        bilgikartY=canvas.getHeight()-kartYukseklik-10;
        }
        //kartın arkası için gölgelendirme 
        grafik.setFill(Color.rgb(0, 0, 0, 0.3));
        grafik.fillRoundRect(bilgikartX + 4, bilgikartY + 4, kartGenislik, kartYukseklik, 15, 15);
        //kartın arka plan rengi
        grafik.setFill(Color.rgb(250, 250, 245, 0.98));
        grafik.fillRoundRect(bilgikartX, bilgikartY, kartGenislik, kartYukseklik, 15, 15);
        //kartın kenarlığı
        grafik.setLineWidth(4);
        grafik.setStroke(Color.rgb(0, 100, 0));
        grafik.strokeRoundRect(bilgikartX + 4 / 2, bilgikartY + 4 / 2, kartGenislik - 4, kartYukseklik - 4, 12, 12);
        //kurdelenin dikine olan kısmı
        double dikKurdele=bilgikartX+kartGenislik/2;
        grafik.setFill(Color.rgb(150, 0, 20, 0.85));
        grafik.fillRect(dikKurdele +80, bilgikartY, 16, kartYukseklik);
        //kurdelenin yatay kısmı
        double yatayKurdeleY=bilgikartY+30;
        grafik.fillRect(bilgikartX, yatayKurdeleY - 10, kartGenislik, 16);
        //kurdeleler için gölge
        grafik.setFill(Color.rgb(0, 0, 0, 0.2));
        grafik.fillRect(dikKurdele +80, bilgikartY, 5, kartYukseklik);
        grafik.fillRect(bilgikartX, yatayKurdeleY - 10, kartGenislik, 5);
        //fiyonk çizimi
        double fiyonkX = dikKurdele;
        double fiyonkY = yatayKurdeleY;
        grafik.setFill(Color.rgb(190, 0, 30));
        //fiyonkun sol kısmı
        grafik.fillOval(fiyonkX +65, fiyonkY - 15, 25, 30);
        //fiyonkun sağ kısmı
        grafik.fillOval(fiyonkX +86, fiyonkY - 15, 25, 30);
        //fiyonkun ortası
        grafik.setFill(Color.rgb(255, 215, 0));
        grafik.fillOval(fiyonkX+80, fiyonkY-7, 14, 14);
        grafik.setFill(Color.rgb(255, 255, 255, 0.6));
        grafik.fillOval(fiyonkX+82, fiyonkY - 3, 6, 6);
        //metin kısmı
        grafik.setFill(Color.rgb(20, 30, 70));
        double textX=bilgikartX+15;
        double textY=bilgikartY+50;
        double aralik=18;
        grafik.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        grafik.fillText("Düğüm ID:"+urldenSonKisim(dugum), textX, textY);
        textY+= aralik;
        grafik.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 13));
        String yazarlar="";
        if(makale.getAuthors()!=null&&!makale.getAuthors().isEmpty()){
            yazarlar=String.join(",", makale.getAuthors());
        }
        String kirpyazar=metniKirp(yazarlar, yazar);
        grafik.fillText("Yazarlar:"+kirpyazar, textX, textY);
        textY+=aralik;
        String kirpbaslik=metniKirp(makale.getTitle(), yazar);
        grafik.fillText("Başlık:"+kirpbaslik, textX, textY);
        textY+=aralik;
        grafik.fillText("Yıl:"+makale.getYear(), textX, textY);
        textY+=aralik;
        grafik.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        grafik.setFill(Color.rgb(180, 0, 0));
        grafik.fillText("Atıf Sayısı:"+atifSayisi, textX, textY);
    }
    private String metniKirp(String metin, int maxKarakter) {
    if (metin == null || metin.length() <= maxKarakter) {
        return metin;
    }
    return metin.substring(0, maxKarakter - 3) + "...";
}
    private void tiklananDugumTablo(String Dugum){
        double kutuX=15.0;
        double kutuY=210.0;
        double kutuGenislik=280;
        double kutuYukseklik=180;
        int indegree=graf.getInDegree(Dugum);
        int outdegree=graf.getOutDegree(Dugum);
        HIndexCalculator hi = new HIndexCalculator(graf);
        int hindex = hi.computeHIndex(Dugum);
        HMedianCalculator hm=new HMedianCalculator(graf);
        int hmsayi=hm.computeHMedian(Dugum);
        //arka plan rengi
        grafik.setFill(Color.rgb(51, 25, 0, 1.0));
        grafik.fillRect(kutuX, kutuY, kutuGenislik, kutuYukseklik);
        //kutu kenarlığı
        grafik.setLineWidth(3);
        grafik.setStroke(Color.rgb(51, 0, 0));
        grafik.strokeRect(kutuX, kutuY, kutuGenislik, kutuYukseklik);
        //başlık kısmına aydınlatma
        double basyukseklik=35;
        grafik.setFill(Color.rgb(255, 255, 255, 0.2));
        grafik.fillRect(kutuX + 1, kutuY + 1, kutuGenislik - 2, basyukseklik);
        cizCorap(kutuX + 10, kutuY + 5, Color.rgb(178, 34, 34), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 50, kutuY + 5,Color.rgb(34, 139, 34), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 90, kutuY + 5,Color.rgb(0, 191, 255), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 130, kutuY + 5,Color.rgb(204, 0, 204), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 170, kutuY + 5,Color.rgb(255, 0, 255), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 210, kutuY + 5,Color.rgb(255, 128, 0), Color.rgb(255, 255, 255));
        cizCorap(kutuX + 250, kutuY + 5,Color.rgb(0, 0, 102), Color.rgb(255, 255, 255));
        double satir=28;
        double yBaslangic = kutuY + basyukseklik ;
        grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        grafik.setFill(Color.rgb(255, 255, 255));
        grafik.fillText("Makale ID:" + urldenSonKisim(Dugum), kutuX + 10, yBaslangic+satir);
        yBaslangic+=satir;
        grafik.fillText("Atıf Sayısı (In-degree):" + indegree, kutuX + 10, yBaslangic +satir);
        yBaslangic += satir;
        grafik.fillText("Referans Verdiği (Out-degree):" + outdegree, kutuX + 10, yBaslangic + satir);
        yBaslangic+= satir;
        grafik.fillText("H-Index:" + hindex, kutuX + 10, yBaslangic + satir);
        yBaslangic+= satir;
        grafik.fillText("H-Median:" + hmsayi, kutuX + 10, yBaslangic + satir);
    }
    public void cizCorap(double x,double y,Color renk1,Color renk2){
        double genislik = 15;
        double yukseklik = 20;
        //çorabın ana kısmı
        grafik.setFill(renk1);
        grafik.fillRoundRect(x,y + 5,genislik,yukseklik,8,8);
        grafik.fillOval(x +genislik / 2, y + 18, 10, 10);
        //çorabın bilek kısmı
        grafik.setFill(renk2);
        grafik.fillRect(x-2,y,genislik+4,7);
        //asmak için ip
        grafik.setStroke(Color.rgb(255, 215, 0));
        grafik.setLineWidth(1);
        grafik.strokeLine(x+genislik/2,y,x+genislik/2,y-5);
    }
public void yesilokciz(){
    List<String> dugumListesi = new java.util.ArrayList<>(konumlar.keySet()); 
    dugumListesi.sort((a, b) -> {
        try {
            return Integer.compare(Integer.parseInt(a),Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    });
    for(int i = 0; i < dugumListesi.size() - 1; i++){
        String dugum1 = dugumListesi.get(i);
        String dugum2 = dugumListesi.get(i + 1);
        Point2D p1 = konumlar.get(dugum1);
        Point2D p2 = konumlar.get(dugum2);
        if (p1 == null || p2 == null) continue;
        double x1 = p1.getX() + 10;
        double y1 = p1.getY() + 10;
        double x2 = p2.getX() + 10;
        double y2 = p2.getY() + 10;
        double ortaX = (x1 + x2) / 2;
        double ortaY = (y1 + y2) / 2;
        double mesafe = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double kavisMiktari = mesafe * 0.3;
        double kontrolX = ortaX;
        double kontrolY = ortaY - kavisMiktari;
        int adimSayisi = 50;
        double oncekiX = x1;
        double oncekiY = y1;     
        for(int j = 1; j <= adimSayisi; j++){
             double t = (double)j / adimSayisi;
             double suankix = Math.pow(1-t, 2) * x1 + 2 * (1-t) * t * kontrolX + Math.pow(t, 2) * x2;
             double suankiy = Math.pow(1-t, 2) * y1 + 2 * (1-t) * t * kontrolY + Math.pow(t, 2) * y2;
             grafik.setStroke(Color.rgb(50, 205, 50, 0.3));
             grafik.setLineWidth(6);
             grafik.strokeLine(oncekiX,oncekiY,suankix,suankiy);
             grafik.setStroke(Color.rgb(50, 255, 50));
             grafik.setLineWidth(2);
             grafik.strokeLine(oncekiX,oncekiY,suankix,suankiy);
             oncekiX=suankix;
             oncekiY=suankiy;
        }
        double okbasi = 0.95; 
        double x = Math.pow(1-okbasi, 2) * x1 +  2 * (1-okbasi) * okbasi * kontrolX + Math.pow(okbasi, 2) * x2;
        double y = Math.pow(1-okbasi, 2) * y1 + 2 * (1-okbasi) * okbasi * kontrolY + Math.pow(okbasi, 2) * y2;      
        cizOkBasi(x, y, x2, y2, Color.rgb(50, 255, 50));
    }
}
    public void cizOkBasi(double x1, double y1, double x2, double y2, Color renk){
        double okUzunluk=12;
        double okAci=Math.toRadians(25);
        double aci=Math.atan2(y2 - y1, x2 - x1);
        double okX=x2-15*Math.cos(aci);
        double okY=y2-15*Math.sin(aci);
        double sol1X=okX-okUzunluk*Math.cos(aci-okAci);
        double sol1Y=okY-okUzunluk*Math.sin(aci-okAci);
        double sag1X=okX-okUzunluk*Math.cos(aci+okAci);
        double sag1Y=okY-okUzunluk*Math.sin(aci+okAci);
        grafik.setStroke(renk);
        grafik.setLineWidth(2);
        grafik.strokeLine(okX, okY, sol1X, sol1Y);
        grafik.strokeLine(okX, okY, sag1X, sag1Y);

    }
    private void hIndexVeHMedianKutusu(){
    double kutuX = 15.0;
    double kutuY = canvas.getHeight() - 120;
    double kutuGenislik = 200;
    double kutuYukseklik = 100;
    // LED ışıkları ekle
    ledler(kutuX, kutuY, kutuGenislik, kutuYukseklik);
    //kutu arka plan
    grafik.setFill(Color.rgb(240, 248, 255, 0.95));
    grafik.fillRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
    // Kutu kenarlığı 
    grafik.setStroke(Color.rgb(218, 165, 32));
    grafik.setLineWidth(3);
    grafik.strokeRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
    // Başlık -
    grafik.setFill(Color.rgb(178, 34, 34));
    grafik.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    grafik.fillText("GRAF GENEL METRİKLERİ", kutuX + 15, kutuY + 22);
    // Ayırıcı çizgi 
    grafik.setStroke(Color.rgb(34, 139, 34));
    grafik.setLineWidth(2);
    grafik.strokeLine(kutuX + 10, kutuY + 30, kutuX + kutuGenislik - 10, kutuY + 30);
    grafik.setFill(Color.rgb(139, 0, 0));
    HIndexCalculator hiCalc = new HIndexCalculator(graf);
    int toplamHIndex = hiCalc.toplamHindex();
    HMedianCalculator hmCalc = new HMedianCalculator(graf);
    double toplamHMedian = hmCalc.toplamHmedian();
    grafik.setFill(Color.rgb(0, 0, 0));
    grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
    double y = kutuY + 50;
    double satirAraligi = 25;   
    grafik.fillText("Toplam H-Index: " + toplamHIndex, kutuX + 15, y);
    y += satirAraligi;    
    grafik.fillText("Ortalama H-Median: " + String.format("%.2f", toplamHMedian), kutuX + 15, y);
}
   public void kCoreHesapla(int k) {
    if (gorunurDugumler.isEmpty()) {
        System.out.println("bir graf olusturun");
        return;
    }
    Set<String> yonsuzDugumler = new HashSet<>(gorunurDugumler);
    mevcutK = k;
    kCoreDugumler = graf.kCoreDecomposition(yonsuzDugumler, k);
    ciz();
}
public void kCoreSifirla() {
    mevcutK = 0;
    kCoreDugumler.clear();
    ciz();
}
public void betweennessCentralityHesapla() {
    if (gorunurDugumler.isEmpty()) {
        System.out.println("once bir graf olusturun");
        return;
    }
    bcGosterim = true;
    bcSkorlar = graf.computeBetweennessCentrality(gorunurDugumler);
    List<Map.Entry<String, Double>> siraliSkorlar = new ArrayList<>(bcSkorlar.entrySet());
    
    siraliSkorlar.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
    for (int i = 0; i < Math.min(10, siraliSkorlar.size()); i++) {
        Map.Entry<String, Double> entry = siraliSkorlar.get(i);
        System.out.printf("%2d. %s: %.4f%n", 
            (i + 1), 
            urldenSonKisim(entry.getKey()), 
            entry.getValue());
    }
    ciz();
}
public void bcTemizle() {
    bcGosterim = false;
    bcSkorlar.clear();
    System.out.println("Betweenness Centrality temizlendi");
    ciz();
}
private void bcSonuclariniCiz() {
    double kutuX = canvas.getWidth() - 320;
    double kutuY = 200;
    double kutuGenislik = 300;
    double kutuYukseklik = 250;
    grafik.setFill(Color.rgb(240, 248, 255, 0.95));
    grafik.fillRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
    grafik.setStroke(Color.rgb(155, 89, 182));
    grafik.setLineWidth(3);
    grafik.strokeRoundRect(kutuX, kutuY, kutuGenislik, kutuYukseklik, 15, 15);
    grafik.setFill(Color.rgb(155, 89, 182));
    grafik.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    grafik.fillText("BETWEENNESS CENTRALITY", kutuX + 15, kutuY + 25);
    grafik.setStroke(Color.rgb(155, 89, 182));
    grafik.setLineWidth(2);
    grafik.strokeLine(kutuX + 10, kutuY + 35, kutuX + kutuGenislik - 10, kutuY + 35);
    List<Map.Entry<String, Double>> sirali = new ArrayList<>(bcSkorlar.entrySet());
    sirali.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
    grafik.setFill(Color.rgb(0, 0, 0));
    grafik.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
    double y = kutuY + 55;
    double aralik = 20;   
    for (int i = 0; i < Math.min(10, sirali.size()); i++) {
        Map.Entry<String, Double> entry = sirali.get(i);
        String id = urldenSonKisim(entry.getKey());
        if (id.length() > 12) {
            id = id.substring(0, 12) + "...";
        }
        String text = String.format("%2d. %s: %.3f", (i + 1), id, entry.getValue());
        grafik.fillText(text, kutuX + 15, y);
        y += aralik;
    }
}
}
