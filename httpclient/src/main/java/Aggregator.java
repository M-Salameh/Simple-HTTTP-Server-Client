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
                temp.add(webClient.sendTask(workerAddr , task.getBytes()));
            }
        }
        for (CompletableFuture<String> resp : temp)
        {
            responses.add(resp.get());
        }
        //throw new UnsupportedOperationException();
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
                temp.add(webClient.sendTask(workerAddr , task.getBytes() , headers));
            }
        }
        for (CompletableFuture<String> resp : temp)
        {
            responses.add(resp.get());
        }
        return responses;
    }
}
