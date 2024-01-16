import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Aggregator {

    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    /*send task to list of workers*/
    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) throws ExecutionException, InterruptedException {
        List<String> responses = new ArrayList<>();
        List<CompletableFuture<String>> temp = new ArrayList<>();
        for (String workerAddr : workersAddresses)
        {
            for (String task : tasks)
            {
                try
                {
                    temp.add(webClient.sendTask(workerAddr, task.getBytes()));
                }
                catch (Exception e)
                {
                    System.out.println("Cannot Connect To Server " + workerAddr);
                    break;
                }
            }
        }
        responses = joinFutures(temp);
        return responses;
    }

    /*send task to list of workers*/
    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks, String headers) throws ExecutionException, InterruptedException {
        List<String> responses = new ArrayList<>();
        List<CompletableFuture<String>> temp = new ArrayList<>();

        for (String workerAddr : workersAddresses)
        {
            for (String task : tasks)
            {
                try
                {
                    temp.add(webClient.sendTask(workerAddr , task.getBytes() , headers));
                }
                catch (Exception e)
                {
                    System.out.println("Cannot Connect To Server " + workerAddr);
                    break;
                }
            }
        }

        responses = joinFutures(temp);
        return responses;
    }

    private List<String> joinFutures(List<CompletableFuture<String>> futures)
    {
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try
        {
            allFutures.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }

        List<String> results = new ArrayList<>();
        for (CompletableFuture<String> future : futures)
        {
            try
            {
                String result = future.get();
                results.add(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
