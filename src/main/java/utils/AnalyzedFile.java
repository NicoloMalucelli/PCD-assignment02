package utils;

public class AnalyzedFile implements Comparable<AnalyzedFile> {

    @Override
    public int compareTo(AnalyzedFile o) {
        return Integer.compare(o.lines, this.lines);
    }

    private String filePath;
    private int lines;

    public AnalyzedFile(String filePath, int lines) {
        this.filePath = filePath;
        this.lines = lines;
    }

    public String filePath() {
        return filePath;
    }

    public int lines() {
        return lines;
    }

    @Override
    public String toString() {
        return filePath + "has " + lines + "lines";
    }
}
