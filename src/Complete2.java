import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Complete2 {

    public static void abc(String args[]){



        List<String> webPageLinks = Arrays.asList("String 1", "String 2", "String 3","String 4");


        Long start = System.currentTimeMillis();

        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(webPageLink -> downloadWebPage(webPageLink))
                .collect(Collectors.toList());



// Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()])
        );

        // When all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> {
            return pageContentFutures.stream()
                    .map(pageContentFuture -> pageContentFuture.join())
                    .collect(Collectors.toList());
        });

        allFutures.join();

        Long end = System.currentTimeMillis();
        System.out.println("Time "+(end-start));



    }

    public static CompletableFuture<String> downloadWebPage(String pageLink) {
        Executor executor = Executors.newFixedThreadPool(30);
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Executing ... " +pageLink);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Done ... "+ pageLink);
            return "Download : "+ pageLink;
        },executor);
    }


}
