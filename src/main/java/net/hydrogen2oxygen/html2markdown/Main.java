package net.hydrogen2oxygen.html2markdown;

import org.apache.commons.io.FileUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static final String UTF_8 = "UTF-8";
    private static Set<String> urls = new HashSet<>();
    private static String baseUrl;
    private static File targetDir = new File("target");

    /**
     * Insert as first parameter the base url of the website you want to convert into markdown,
     * as a second optional parameter the targetDirectory.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String [] args) throws IOException {

        System.out.println("HTML 2 MARKDOWN");

        if (args.length == 0) {
            System.out.println("SYNTAX: <baseUrl> <targetDir - optional>");
            System.exit(0);
        }

        System.out.println("Converting ...");

        baseUrl = args[0];

        if (args.length > 1) {
            targetDir = new File(args[1]);
        }

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        Document doc = Jsoup.connect(baseUrl).get();
        FileUtils.write(new File(targetDir.getAbsolutePath() + "/index.html"), doc.html(), UTF_8);
        System.out.println("ENTERING " + doc.title());

        Elements links = doc.select("a");
        extract(links);

        System.out.println("... FINISHED!");
    }

    private static void extract(Elements links) throws IOException {

        for (Element link : links) {
            String url = link.attr("href").trim();

            if (url.length() < 2) continue;
            if (url.endsWith("pdf") || url.endsWith("jpg") || url.endsWith("png") || url.endsWith("zip") || url.endsWith("xml")) continue;

            // don't follow external links
            if (url.startsWith("http") && !url.contains(baseUrl)) {
                continue;
            }

            if (urls.contains(url)) continue;
            if (url.startsWith("mailto:")) continue;

            System.out.println(url + " -> " + link.text());
            urls.add(url);

            String collectUrl = url;

            if (!collectUrl.startsWith(baseUrl)) {

                if (!collectUrl.startsWith("/")) {
                    collectUrl = "/" + collectUrl;
                }

                collectUrl = baseUrl + "/" + collectUrl;
            }

            Document doc = null;
            String fileUrl = url;

            if (!fileUrl.endsWith(".htm")) {
                fileUrl = fileUrl.replace(baseUrl, "");
                File newTargetDir = new File(targetDir.getAbsolutePath() + "/" + fileUrl);
                if (!newTargetDir.exists()) {
                    newTargetDir.mkdirs();
                }
                fileUrl += "/index.html";
            }

            File file = new File(targetDir.getAbsolutePath() + "/" + fileUrl);

            if (file.exists() && file.isFile()) {
                doc = Jsoup.parse(file,UTF_8);
            } else {

                try {
                    try {
                        doc = Jsoup.connect(collectUrl).get();
                    } catch (HttpStatusException e) {
                        // try again, this time with lowercase ... which is stupid, but this is a common error
                        doc = Jsoup.connect(collectUrl.toLowerCase()).get();
                    }
                } catch (org.jsoup.HttpStatusException e) {
                    e.printStackTrace();
                    continue;
                }

            }

            if (!file.exists()) {
                FileUtils.write(file, doc.html(), UTF_8);
            }

            System.out.println("ENTERING " + doc.title());

            Elements moreLinks = doc.select("a");
            extract(moreLinks);
        }
    }
}
