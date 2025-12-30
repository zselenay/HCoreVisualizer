package graph;

import java.util.*;

public class HMedianCalculator {

    private final Graph graph;
    private final HIndexCalculator hIndexCalc;

    public HMedianCalculator(Graph graph) {
        this.graph = graph;
        this.hIndexCalc = new HIndexCalculator(graph);
    }

   
    public Set<String> computeHCoreForNode(String node) {

        List<String> citingPapers = graph.getReferencedBy(node);

        if (citingPapers.isEmpty()) {
            return Collections.emptySet();
        }

        Map<String, Integer> inCounts = new HashMap<>();
        for (String candidate : citingPapers) {
            int inDegree = graph.getInDegree(candidate);
            inCounts.put(candidate, inDegree);
        }

        List<Integer> values = new ArrayList<>(inCounts.values());
        Collections.sort(values, Collections.reverseOrder());

        int h = 0;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }

        Set<String> hcore = new LinkedHashSet<>();
        if (h > 0) {
   
            List<Map.Entry<String, Integer>> sortedEntries = 
                new ArrayList<>(inCounts.entrySet());
            sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            for (int i = 0; i < h && i < sortedEntries.size(); i++) {
                hcore.add(sortedEntries.get(i).getKey());
            }
        }

        return hcore;
    }

    public int computeHMedian(String node) {
        Set<String> hcore = computeHCoreForNode(node);

        if (hcore.isEmpty()) {
            return 0;
        }

        List<Integer> citationCounts = new ArrayList<>();
        for (String n : hcore) {
            int inDegree = graph.getInDegree(n);
            citationCounts.add(inDegree);
        }

        Collections.sort(citationCounts);
        int size = citationCounts.size();

        if (size == 0) return 0;
        
        if (size % 2 == 1) {

            return citationCounts.get(size / 2);
        } else {
            int a = citationCounts.get(size / 2 - 1);
            int b = citationCounts.get(size / 2);
            return (a + b) / 2;
        }
    }
    public HCoreAndMedian computeHCoreAndMedian(String node) {
        Set<String> hcore = computeHCoreForNode(node);
        int median = computeHMedian(node);
        int hIndex = hIndexCalc.computeHIndex(node);
        return new HCoreAndMedian(hIndex, hcore, median);
    }
    public static class HCoreAndMedian {
        public final int hIndex;
        public final Set<String> hcore;
        public final int median;

        public HCoreAndMedian(int hIndex, Set<String> hcore, int median) {
            this.hIndex = hIndex;
            this.hcore = hcore;
            this.median = median;
        }

        @Override
        public String toString() {
            return "HCoreAndMedian{" +
                    "hIndex=" + hIndex +
                    ", hcore=" + hcore +
                    ", median=" + median +
                    '}';
        }
    }
    public double toplamHmedian(){
        Set <String> tumDugumler=graph.getNodes();
        if(tumDugumler.isEmpty()){
            return 0.0;
        }
        double toplamHmedian=0;
        for(String dugum:tumDugumler){
            toplamHmedian+=computeHMedian(dugum);
        }
        return toplamHmedian/tumDugumler.size();
    }
}
