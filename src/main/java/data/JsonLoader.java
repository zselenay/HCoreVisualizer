package data;

import model.Makale;
import graph.Graph;
import java.io.*;
import java.util.*;

public class JsonLoader {
    
    public static List<Makale> load() throws IOException {
        return load("/data.json");
    }
    
    public static List<Makale> load(String resourcePath) throws IOException {
        try {
            InputStream inputStream = JsonLoader.class.getResourceAsStream(resourcePath);
            
            if (inputStream == null) {
                throw new IOException(" JSON dosyası bulunamadı: " + resourcePath);
            }
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
           
            return parseJsonArray(json.toString());
            
        } catch (IOException e) {
            System.err.println("❌ JSON okuma hatası: " + e.getMessage());
            throw e;
        }
    }
    
    private static List<Makale> parseJsonArray(String json) {
        List<Makale> makaleler = new ArrayList<>();
        
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new IllegalArgumentException("Geçersiz JSON array");
        }
       
        json = json.substring(1, json.length() - 1).trim();
      
        List<String> objects = splitJsonObjects(json);
        
        for (String objStr : objects) {
            Makale makale = parseJsonObject(objStr);
            if (makale != null) {
                makaleler.add(makale);
            }
        }
        
        System.out.println("✅ " + makaleler.size() + " makale başarıyla yüklendi.");
        return makaleler;
    }
    
    private static List<String> splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{') {
                braceCount++;
                current.append(c);
            } else if (c == '}') {
                current.append(c);
                braceCount--;
                
                if (braceCount == 0) {
                    objects.add(current.toString().trim());
                    current = new StringBuilder();
                }
            } else if (braceCount > 0) {
                current.append(c);
            }
        }
        
        return objects;
    }
    
    private static Makale parseJsonObject(String objStr) {
        try {
        
            objStr = objStr.trim();
            if (objStr.startsWith("{")) objStr = objStr.substring(1);
            if (objStr.endsWith("}")) objStr = objStr.substring(0, objStr.length() - 1);
            
            String id = null;
            String doi = null;
            String title = null;
            int year = 0;
            List<String> authors = new ArrayList<>();
            List<String> references = new ArrayList<>();
            
            // Alanları parse et
            Map<String, String> fields = parseFields(objStr);
            
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                switch (key) {
                    case "id":
                        id = cleanString(value);
                        break;
                    case "doi":
                        doi = cleanString(value);
                        break;
                    case "title":
                        title = cleanString(value);
                        break;
                    case "year":
                        try {
                            year = Integer.parseInt(value.trim());
                        } catch (NumberFormatException e) {
                            year = 0;
                        }
                        break;
                    case "authors":
                        authors = parseArray(value);
                        break;
                    case "referenced_works":
                        references = parseArray(value);
                        break;
                }
            }
            
            return new Makale(id, doi, authors, title, year, references);
            
        } catch (Exception e) {
            System.err.println("⚠️ Makale parse hatası: " + e.getMessage());
            return null;
        }
    }
    
    private static Map<String, String> parseFields(String objStr) {
        Map<String, String> fields = new HashMap<>();
        
        int i = 0;
        while (i < objStr.length()) {
            
            int keyStart = objStr.indexOf('"', i);
            if (keyStart == -1) break;
            
            int keyEnd = objStr.indexOf('"', keyStart + 1);
            String key = objStr.substring(keyStart + 1, keyEnd);
            
            int colonPos = objStr.indexOf(':', keyEnd);
            i = colonPos + 1;
            
            while (i < objStr.length() && Character.isWhitespace(objStr.charAt(i))) i++;
            
            if (i >= objStr.length()) break;
            
            String value;
            if (objStr.charAt(i) == '"') {
            
                int valueStart = i;
                int valueEnd = objStr.indexOf('"', i + 1);
                value = objStr.substring(valueStart, valueEnd + 1);
                i = valueEnd + 1;
            } else if (objStr.charAt(i) == '[') {
      
                int bracketCount = 0;
                int valueStart = i;
                while (i < objStr.length()) {
                    if (objStr.charAt(i) == '[') bracketCount++;
                    if (objStr.charAt(i) == ']') bracketCount--;
                    i++;
                    if (bracketCount == 0) break;
                }
                value = objStr.substring(valueStart, i);
            } else {
            
                int valueStart = i;
                while (i < objStr.length() && objStr.charAt(i) != ',' && objStr.charAt(i) != '}') {
                    i++;
                }
                value = objStr.substring(valueStart, i).trim();
            }
            
            fields.put(key, value);
            
            while (i < objStr.length() && (objStr.charAt(i) == ',' || Character.isWhitespace(objStr.charAt(i)))) {
                i++;
            }
        }
        
        return fields;
    }
    
    private static List<String> parseArray(String arrayStr) {
        List<String> result = new ArrayList<>();
        
        arrayStr = arrayStr.trim();
        if (!arrayStr.startsWith("[") || !arrayStr.endsWith("]")) {
            return result;
        }
        
        arrayStr = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (arrayStr.isEmpty()) return result;
        
        int i = 0;
        while (i < arrayStr.length()) {
            while (i < arrayStr.length() && Character.isWhitespace(arrayStr.charAt(i))) i++;
            
            if (i >= arrayStr.length()) break;
            
            if (arrayStr.charAt(i) == '"') {
                int start = i + 1;
                int end = arrayStr.indexOf('"', start);
                if (end != -1) {
                    result.add(arrayStr.substring(start, end));
                    i = end + 1;
                }
            }
           
            while (i < arrayStr.length() && (arrayStr.charAt(i) == ',' || Character.isWhitespace(arrayStr.charAt(i)))) {
                i++;
            }
        }
        
        return result;
    }
    
    private static String cleanString(String str) {
        if (str == null) return null;
        str = str.trim();
        if (str.startsWith("\"")) str = str.substring(1);
        if (str.endsWith("\"")) str = str.substring(0, str.length() - 1);
        return str;
    }
    
    public static Graph grafOlustur() throws IOException {
        List<Makale> makaleler = load();
        Graph graf = new Graph();
        
        for (Makale makale : makaleler) {
            graf.addArticle(makale);
        }
        
        for (Makale makale : makaleler) {
            for (String referans : makale.getReferences()) {
                if (graf.hasNode(referans)) {
                    graf.addEdge(makale.getId(), referans);
                }
            }
        }
        
        return graf;
    }
}
