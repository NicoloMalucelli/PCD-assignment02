package executors.model;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Folder {
    private final String path;
    private final List<Folder> subFolders;
    private final List<Document> documents;

    private Folder(String path, List<Folder> subFolders, List<Document> documents) {
        this.subFolders = subFolders;
        this.documents = documents;
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

    public List<Folder> getSubFolders() {
        return this.subFolders;
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public static Folder fromDirectory(File dir) throws IOException {
        List<Document> documents = new LinkedList<Document>();
        List<Folder> subFolders = new LinkedList<Folder>();
        for (File entry : dir.listFiles()) {
            if (entry.isDirectory()) {
                subFolders.add(Folder.fromDirectory(entry));
            } else if (entry.getName().endsWith("java")){
                documents.add(Document.fromFile(entry));
            }
        }
        return new Folder(dir.getPath(), subFolders, documents);
    }
}

