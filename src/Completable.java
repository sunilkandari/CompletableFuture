import com.sun.tools.corba.se.idl.constExpr.Or;
import com.sun.tools.internal.xjc.reader.gbind.OneOrMore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Completable {

    private ASINResponse2 asinResponse2;

    public static void main(String []args) throws ExecutionException, InterruptedException {


        List<Order> orders = new ArrayList<>();


        Long start =  System.currentTimeMillis();

        Order O1 = new Order();
        O1.orderId = "orderId_1";
        List<ASIN> O1AsinList = new ArrayList<>();
        O1.orderAsins = O1AsinList;

        ASIN o1a1 = new ASIN();
        o1a1.asin = "O1A1";
        ASIN o1a2 = new ASIN();
        o1a2.asin = "O1A2";
        ASIN o1a3 = new ASIN();
        o1a3.asin = "O1A3";

        O1AsinList.add(o1a1);
        O1AsinList.add(o1a2);
        O1AsinList.add(o1a3);


        Order O2 = new Order();
        O2.orderId = "orderId_2";
        List<ASIN> O2AsinList = new ArrayList<>();

        O2.orderAsins = O2AsinList;
        ASIN o2a1 = new ASIN();
        o2a1.asin = "O2A1";
        ASIN o2a2 = new ASIN();
        o2a2.asin = "O2A2";
        ASIN o2a3 = new ASIN();
        o2a3.asin = "O2A3";

        O2AsinList.add(o2a1);
        O2AsinList.add(o2a2);
        O2AsinList.add(o2a3);


        Order O3 = new Order();
        O3.orderId = "orderId_3";
        List<ASIN> O3AsinList = new ArrayList<>();

        O3.orderAsins = O3AsinList;
        ASIN o3a1 = new ASIN();
        o3a1.asin = "O3A1";
        ASIN o3a2 = new ASIN();
        o3a2.asin = "O3A2";
        ASIN o3a3 = new ASIN();
        o3a3.asin = "O3A3";

        O3AsinList.add(o3a1);
        O3AsinList.add(o3a2);
        O3AsinList.add(o3a3);

        System.out.println("Here");

        orders.add(O1);
        orders.add(O2);
        orders.add(O3);

        List<CompletableFuture<OrderResponse>> ordersFuture = orders.stream()
                .map(order -> getOrderDetails(order))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                ordersFuture.toArray(new CompletableFuture[ordersFuture.size()])
        );

        CompletableFuture<List<OrderResponse>> allOrdersFutures = allFutures.thenApply(v -> {
            return ordersFuture.stream()
                    .map(pageContentFuture -> pageContentFuture.join())
                    .collect(Collectors.toList());
        });


        Long end1 =  System.currentTimeMillis();

        System.out.println("Total time taken Here  : "+(end1 -start));

/*
        for(OrderResponse orderResponse : allOrdersFutures.join()){
            System.out.println("Order Response Id : "+orderResponse.orderIdResponse);
            for (ASINResponse asinResponse : orderResponse.asins){
                System.out.println("ASIN Response : "+asinResponse.asinResponse);
            }
        }
*/

        allOrdersFutures.join();

        Long end2 =  System.currentTimeMillis();



        System.out.println("Total time taken END: "+(end2 -start));




/*
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Retrieving weight.");
            return 65.0;
        });


        CompletableFuture<String> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Retrieving height.");
            return "kg";
        });

        System.out.println("Calculating BMI.");
        CompletableFuture<String> combinedFuture1 = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    System.out.println("Combine 1.");
                    return weightInKg + heightInCm;
                });


        CompletableFuture<Double> weightInKgFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Retrieving weight 2.");
            return 75.0;
        });


        CompletableFuture<String> heightInCmFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Retrieving height 2.");
            return "KG";
        });


        CompletableFuture<String> combinedFuture2 = weightInKgFuture2
                .thenCombine(heightInCmFuture2, (weightInKg, heightInCm) -> {

                    System.out.println("Combine 2.");

                    return weightInKg + heightInCm;

                });



        CompletableFuture<String> completableFuture = combinedFuture1
                .thenCombine(combinedFuture2,(combined1,combined2) ->{
                   return combined1 +combined2;
                });


        System.out.println("Your BMI is - " + completableFuture.join());
*/

    }


    public static CompletableFuture<OrderResponse> getOrderDetails(Order order){


        Executor executor = Executors.newFixedThreadPool(32);
        return CompletableFuture.supplyAsync(() -> {
               // System.out.println("Executing ... " + order.orderId);
                OrderResponse orderResponse = new OrderResponse();

                orderResponse.orderIdResponse = order.orderId;

                List<ASIN> asins =  order.orderAsins;
                List<CompletableFuture<ASINResponse>> asinFuture = asins.stream()
                        .map(asin -> getASINDetails(asin.asin))
                        .collect(Collectors.toList());


                CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                        asinFuture.toArray(new CompletableFuture[asinFuture.size()])
                );

                CompletableFuture<List<ASINResponse>> allasinFutures = allFutures.thenApply(v -> {
                    return asinFuture.stream()
                            .map(pageContentFuture -> pageContentFuture.join())
                            .collect(Collectors.toList());
                });

                orderResponse.asins  = allasinFutures.join();

                //System.out.println("Done "+ order.orderId);
                return orderResponse;
            },executor);
    }

    public static CompletableFuture<ASINResponse> getASINDetails(String asin){

        Executor executor = Executors.newFixedThreadPool(30);
                return CompletableFuture.supplyAsync(() ->{
                    //System.out.println("Executing asin Details ... "+asin);
                    CompletableFuture<ASINResponse1> asinResponse1CompletableFuture =  getDetailsFromSlapshot1(asin);
                    CompletableFuture<ASINResponse2> asinResponse2CompletableFuture =  getDetailsFromSlapshot2(asin);
                    CompletableFuture<ASINResponse3> asinResponse3CompletableFuture =  getDetailsFromSlapshot3(asin);

                    ASINResponse asinResponse =  new ASINResponse();
                    CompletableFuture.allOf(asinResponse1CompletableFuture,asinResponse2CompletableFuture,asinResponse3CompletableFuture).join();

                    asinResponse.asinResponse = asinResponse1CompletableFuture.join().detail1 + asinResponse2CompletableFuture.join().detail2 + asinResponse3CompletableFuture.join().detail3;

                    return asinResponse;
                },executor);
    }


    public static CompletableFuture<ASINResponse1> getDetailsFromSlapshot1(String asin){

        Executor executor = Executors.newFixedThreadPool(1);
        return CompletableFuture.supplyAsync(() ->{

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }


            //System.out.println("Executing slapshot 1 ...  "  + asin);
            ASINResponse1 asinResponse1 =  new ASINResponse1();
            asinResponse1.detail1 = "detail.1";
            System.out.println("getDetailsFromSlapshot 1 :: Done : "+ asin);
            return asinResponse1;
        },executor);
    }


    public static CompletableFuture<ASINResponse2> getDetailsFromSlapshot2(String asin){
        Executor executor = Executors.newFixedThreadPool(30);
        return CompletableFuture.supplyAsync(() ->{
            //System.out.println("Executing slapshot 2 ...  "  + asin);
            ASINResponse2 asinResponse2 =  new ASINResponse2();
            asinResponse2.detail2 = "detail.2";
            System.out.println("getDetailsFromSlapshot 2 :: Done : "+ asin);
            return asinResponse2;
        },executor);
    }

    public static CompletableFuture<ASINResponse3> getDetailsFromSlapshot3(String asin){
        Executor executor = Executors.newFixedThreadPool(30);
        return CompletableFuture.supplyAsync(() ->{
            //System.out.println("Executing slapshot 3 ...  "  + asin);
            ASINResponse3 asinResponse3 =  new ASINResponse3();
            asinResponse3.detail3 = "detail.3";

            System.out.println("getDetailsFromSlapshot 3 :: Done : "+ asin);

            return asinResponse3;
        },executor);
    }
}

