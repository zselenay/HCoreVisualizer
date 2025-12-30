package com.mycompany.hcorevisualizer;

import graph.Graph;
import graph.HIndexCalculator;
import graph.HMedianCalculator;
import data.JsonLoader;
import model.Makale;

import java.util.List;
import java.util.Set;

public class HCoreVisualizer {
    public static void main(String[] args) {
        try {
            List<Makale> makaleler = JsonLoader.load();
            Graph g = new Graph();
            for (Makale m : makaleler) {
                g.addArticle(m);
            }
            int edgeCount = 0;
            for (Makale m : makaleler) {
                for (String ref : m.getReferences()) {
                    if (g.hasNode(ref)) { 
                        g.addEdge(m.getId(), ref);
                        edgeCount++;
                    }else {
    System.out.println("️ Referans grafta yok: " + ref + " (from " + m.getId() + ")");
}
                }
            }

            HIndexCalculator hCalc = new HIndexCalculator(g);
            HMedianCalculator medianCalc = new HMedianCalculator(g);
            for (Makale m : makaleler) {
                String id = m.getId();
                int hIndex = hCalc.computeHIndex(id);
                Set<String> hcore = medianCalc.computeHCoreForNode(id);
                int hmedian = medianCalc.computeHMedian(id);
            }

            if (!makaleler.isEmpty()) {
                String testId = makaleler.get(0).getId();
                System.out.println("Test düğümü: " + testId);
                
                Set<String> hcore = medianCalc.computeHCoreForNode(testId);
                g.expandNode(testId, hcore);
                
            }

        } catch (Exception e) {
            System.err.println("\n HATA: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}

