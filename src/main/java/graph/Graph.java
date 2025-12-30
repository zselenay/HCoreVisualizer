package graph;

import model.Makale;
import java.util.*;

public class Graph {

    private Map<String, Makale> articles = new HashMap<>();

    private Map<String, List<String>> adjacencyList = new HashMap<>();

    public void addArticle(Makale makale) {
        if (makale == null || makale.getId() == null) {
            System.err.println(" Geçersiz makale, eklenmedi!");
            return;
        }
        this.articles.put(makale.getId(), makale);
        addNode(makale.getId());
    }

    public void addNode(String id) {
        adjacencyList.putIfAbsent(id, new ArrayList<>());
    }

    public void addEdge(String from, String to) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
       
        if (!adjacencyList.get(from).contains(to)) {
            adjacencyList.get(from).add(to);
        }
    }

    public Makale getArticle(String id) {
        return articles.get(id);
    }

    public List<String> getNeighbors(String id) {
        return adjacencyList.getOrDefault(id, Collections.emptyList());
    }

    public Set<String> getNodes() {
        return adjacencyList.keySet();
    }

    public int getInDegree(String node) {
        int count = 0;
        for (String other : adjacencyList.keySet()) {
            if (adjacencyList.get(other).contains(node)) {
                count++;
            }
        }
        return count;
    }

    public int getOutDegree(String node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList()).size();
    }

    public List<String> getReferencedBy(String node) {
        List<String> result = new ArrayList<>();
        for (String other : adjacencyList.keySet()) {
            if (adjacencyList.get(other).contains(node)) {
                result.add(other);
            }
        }
        return result;
    }
    public int getTotalNodes() {
        return adjacencyList.size();
    }

    public int getTotalEdges() {
        int count = 0;
        for (List<String> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        return count;
    }

    public int getTotalReferencesGiven() {
        return getTotalEdges();
    }

    public int getTotalReferencesReceived() {
        return getTotalEdges();
    }

    public String getMostCitedArticle() {
        String maxNode = null;
        int maxCount = -1;
        
        for (String node : adjacencyList.keySet()) {
            int inDegree = getInDegree(node);
            if (inDegree > maxCount) {
                maxCount = inDegree;
                maxNode = node;
            }
        }
        
        if (maxNode == null) return "None (0)";
        return maxNode + " (" + maxCount + " citations)";
    }

    public String getMostReferencingArticle() {
        String maxNode = null;
        int maxCount = -1;
        
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            int outDegree = entry.getValue().size();
            if (outDegree > maxCount) {
                maxCount = outDegree;
                maxNode = entry.getKey();
            }
        }
        
        if (maxNode == null) return "None (0)";
        return maxNode + " (" + maxCount + " references)";
    }

    public void expandNode(String nodeId, Set<String> hCore) {
        System.out.println("   H-core: " + hCore);
        
        if (hCore.isEmpty()) {
            System.out.println("H-core boş, genişletme yapılmadı.");
            return;
        }

        int addedNodes = 0;
        int addedEdges = 0;

        for (String newNode : hCore) {
            if (!adjacencyList.containsKey(newNode)) {
                addNode(newNode);
                addedNodes++;
            }
        }

        for (String node : hCore) {
            Makale m = articles.get(node);
            if (m != null) {
                for (String ref : m.getReferences()) {
                    if (hasNode(ref) && !hasEdge(node, ref)) {
                        addEdge(node, ref);
                        addedEdges++;
                    }
                }
            }
        }

        Set<String> allNodes = new HashSet<>(adjacencyList.keySet());
        for (String existingNode : allNodes) {
            if (!hCore.contains(existingNode)) {
                Makale m = articles.get(existingNode);
                if (m != null) {
                    for (String ref : m.getReferences()) {
                        if (hCore.contains(ref) && !hasEdge(existingNode, ref)) {
                            addEdge(existingNode, ref);
                            addedEdges++;
                        }
                    }
                }
            }
        }
    }

    private boolean hasEdge(String from, String to) {
        List<String> neighbors = adjacencyList.get(from);
        return neighbors != null && neighbors.contains(to);
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    public boolean hasNode(String id) {
        return adjacencyList.containsKey(id);
    }

    public Set<String> kCoreDecomposition(Set<String> dugumler, int k) {
        if (k <= 0 || dugumler.isEmpty()) {
            return new HashSet<>(dugumler);
        }
        
        Map<String, Integer> derece = new HashMap<>();
        
        for (String dugum : dugumler) {
            int deg = 0;
            for (String diger : dugumler) {
                if (!dugum.equals(diger)) {
                    if (adjacencyList.get(dugum).contains(diger) || 
                        adjacencyList.get(diger).contains(dugum)) {
                        deg++;
                    }
                }
            }
            derece.put(dugum, deg);
        }
        
        Set<String> kCore = new HashSet<>(dugumler);
        boolean degisti = true;
        
        while (degisti) {
            degisti = false;
            Set<String> cikarilacaklar = new HashSet<>();
            
            for (String dugum : kCore) {
                if (derece.get(dugum) < k) {
                    cikarilacaklar.add(dugum);
                    degisti = true;
                }
            }
            
            for (String cikarilan : cikarilacaklar) {
                kCore.remove(cikarilan);
                
                for (String komsu : kCore) {
                    if (adjacencyList.get(cikarilan).contains(komsu) || 
                        adjacencyList.get(komsu).contains(cikarilan)) {
                        int eskiDerece = derece.get(komsu);
                        derece.put(komsu, eskiDerece - 1);
                    }
                }
            }
        }
        
        return kCore;
    }
    public Set<String> getUndirectedNeighbors(String node) {
        Set<String> neighbors = new HashSet<>();
        neighbors.addAll(adjacencyList.getOrDefault(node, Collections.emptyList()));
        for (String other : adjacencyList.keySet()) {
            if (adjacencyList.get(other).contains(node)) {
                neighbors.add(other);
            }
        }       
        return neighbors;
    }

    private List<String> bfsShortestPath(String start, String end, Set<String> nodes) {
        if (start.equals(end)) {
            return Arrays.asList(start);
        }
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        parent.put(start, null);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) {
                List<String> path = new ArrayList<>();
                String node = end;
                while (node != null) {
                    path.add(0, node);
                    node = parent.get(node);
                }
                return path;
            }
            for (String neighbor : getUndirectedNeighbors(current)) {
                if (nodes.contains(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }

    public Map<String, Double> computeBetweennessCentrality(Set<String> nodes) {
        System.out.println("Betweenness Centrality Hesaplama Başladı");
        System.out.println("Düğüm sayısı: " + nodes.size()); 
        Map<String, Double> bcScores = new HashMap<>();
        for (String node : nodes) {
            bcScores.put(node, 0.0);
        }
        List<String> nodeList = new ArrayList<>(nodes);
        int totalPairs = (nodeList.size() * (nodeList.size() - 1)) / 2;
        int processedPairs = 0;
        System.out.println("Toplam düğüm çifti: " + totalPairs);
        for (int i = 0; i < nodeList.size(); i++) {
            String source = nodeList.get(i); 
            for (int j = i + 1; j < nodeList.size(); j++) {
                String target = nodeList.get(j);
                processedPairs++;
                if (processedPairs % 50 == 0) {
                    
                }
                List<String> path = bfsShortestPath(source, target, nodes);
                if (path != null && path.size() > 2) {
                    for (int k = 1; k < path.size() - 1; k++) {
                        String intermediateNode = path.get(k);
                        bcScores.put(intermediateNode, bcScores.get(intermediateNode) + 1.0);
                    }
                }
            }
        }
        if (totalPairs > 0) {
            for (String node : nodes) {
                bcScores.put(node, bcScores.get(node) / totalPairs);
            }
        }
        return bcScores;
    }
}

