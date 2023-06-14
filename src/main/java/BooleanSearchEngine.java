import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> pageEntryMap = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] fileList = pdfsDir.listFiles();
        for (File pdf : fileList) {
            var document = new PdfDocument(new PdfReader(pdf));

            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                PdfPage page = document.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{Isalphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (var word : freqs.keySet()) {
                    var pageEntry = new PageEntry(pdf.getName(), i, freqs.get(word));
                    if (pageEntryMap.containsKey(word)) {
                        List<PageEntry> pageEntryList = new ArrayList<>(pageEntryMap.get(word));
                        pageEntryList.add(pageEntry);
                        pageEntryMap.put(word, pageEntryList);
                    } else {
                        pageEntryMap.put(word, Arrays.asList(pageEntry));
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (pageEntryMap.containsKey(word)) {
            List<PageEntry> pageEntryList = pageEntryMap.get(word);
            Collections.sort(pageEntryList);
            return pageEntryList;
        } else {
            return Collections.emptyList();
        }
    }
}
