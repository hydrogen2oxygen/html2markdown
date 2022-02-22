package net.hydrogen2oxygen.html2markdown;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException {
        System.out.println("HTML 2 MARKDOWN");
        System.out.println("Converting ...");

        // TODO load a list of HTML Files
        // TODO convert each file into MARKDOWN

        // Sample Code
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
        }

        System.out.println("... FINISHED!");
    }
}
