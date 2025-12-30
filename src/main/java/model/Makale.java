package model;

import java.util.List;
import java.util.ArrayList;

public class Makale {
    private String id;
    private String doi;
    private List<String> authors;
    private String title;
    private int year;
    private List<String> references;

    public Makale() {
        this.authors = new ArrayList<>();
        this.references = new ArrayList<>();
    }

    public Makale(String id, String doi, List<String> authors, String title, int year, List<String> references) {
        this.id = id;
        this.doi = doi;
        this.authors = authors != null ? authors : new ArrayList<>();
        this.title = title;
        this.year = year;
        this.references = references != null ? references : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getDoi() {
        return doi;
    }

    public List<String> getAuthors() {
        return authors != null ? authors : new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public List<String> getReferences() {
        return references != null ? references : new ArrayList<>();
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public String getAuthorsAsString() {
        if (authors == null || authors.isEmpty()) {
            return "Unknown";
        }
        return String.join(", ", authors);
    }

    public int getReferenceCount() {
        return references != null ? references.size() : 0;
    }

    @Override
    public String toString() {
        return "Makale{" +
                "id='" + id + '\'' +
                ", doi='" + doi + '\'' +
                ", authors=" + getAuthorsAsString() +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", references=" + references +
                '}';
    }
}
