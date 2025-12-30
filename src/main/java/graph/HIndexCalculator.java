package graph;

import java.util.*;

public class HIndexCalculator {
    private Graph graph;

    public HIndexCalculator(Graph graph) {
        this.graph = graph;
    }
    public int computeHIndex(String node) {
        List<String> citingPapers = graph.getReferencedBy(node);

        if (citingPapers.isEmpty()) {
            return 0;
        }
        List<Integer> citationCounts = new ArrayList<>();
        for (String paper : citingPapers) {
            int inDegree = graph.getInDegree(paper);
            citationCounts.add(inDegree);
        }
        Collections.sort(citationCounts, Collections.reverseOrder());
        int h = 0;
        for (int i = 0; i < citationCounts.size(); i++) {
            if (citationCounts.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }
    public HIndexDetails computeHIndexDetails(String node) {
        List<String> citingPapers = graph.getReferencedBy(node);

        if (citingPapers.isEmpty()) {
            return new HIndexDetails(0, Collections.emptyList(), Collections.emptyList());
        }
        List<PaperCitation> citations = new ArrayList<>();
        for (String paper : citingPapers) {
            int inDegree = graph.getInDegree(paper);
            citations.add(new PaperCitation(paper, inDegree));
        }
        Collections.sort(citations, (a, b) -> b.citationCount - a.citationCount);
        int h = 0;
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < citations.size(); i++) {
            counts.add(citations.get(i).citationCount);
            if (citations.get(i).citationCount >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }

        return new HIndexDetails(h, citingPapers, counts);
    }

    private static class PaperCitation {
        String paperId;
        int citationCount;

        PaperCitation(String paperId, int citationCount) {
            this.paperId = paperId;
            this.citationCount = citationCount;
        }
    }

    public static class HIndexDetails {
        public final int hIndex;
        public final List<String> citingPapers;
        public final List<Integer> citationCounts;

        public HIndexDetails(int hIndex, List<String> citingPapers, List<Integer> citationCounts) {
            this.hIndex = hIndex;
            this.citingPapers = citingPapers;
            this.citationCounts = citationCounts;
        }

        @Override
        public String toString() {
            return "HIndexDetails{" +
                    "hIndex=" + hIndex +
                    ", citingPapers=" + citingPapers +
                    ", citationCounts=" + citationCounts +
                    '}';
        }
    }
    public int toplamHindex(){
        Set<String>tumDugumler=graph.getNodes();
        if(tumDugumler.isEmpty()){
            return 0;
        }
        List<Integer>yapilanatiflar=new ArrayList<>();
        for(String dugum:tumDugumler){
            int atif=graph.getInDegree(dugum);
            yapilanatiflar.add(atif);
        }
        Collections.sort(yapilanatiflar,Collections.reverseOrder());
        int hhesap=0;
        for (int i=0;i<yapilanatiflar.size();i++) {
            if (yapilanatiflar.get(i)>=i+1) {
                hhesap=i+1;
            } else {
                break;
            }
        }
        return hhesap;
    }
}
