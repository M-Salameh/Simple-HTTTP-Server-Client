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
        //List<CompletableFuture<String>> temp = new ArrayList<>();
        for (String workerAddr : workersAddresses)
        {
            for (String task : tasks)
            {
                try
                {
                    responses.add(webClient.sendTask(workerAddr, task.getBytes()).get()+ " via " + workerAddr);
                }
                catch (Exception e)
                {
                    System.out.println("Cannot Connect To Server " + workerAddr);
                    break;
                }
            }
        }
        /*for (CompletableFuture<String> resp : temp)
        {
            try {
                responses.add(resp.get());
            }
            catch (Exception e)
            {
                System.out.println("Connection Problem");
            }
        }*/
        //throw new UnsupportedOperationException();
        return responses;
    }

    /*send task to list of workers*/
    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks, String headers) throws ExecutionException, InterruptedException {
        List<String> responses = new ArrayList<>();
        //List<CompletableFuture<String>> temp = new ArrayList<>();
        for (String workerAddr : workersAddresses)
        {
            for (String task : tasks)
            {
                try
                {
                    responses.add(webClient.sendTask(workerAddr , task.getBytes() , headers).get()+" via "+workerAddr);
                }
                catch (Exception e)
                {
                    System.out.println("Cannot Connect To Server " + workerAddr);
                    break;
                }
            }
        }

        /*for (CompletableFuture<String> resp : temp)
        {
            try {
                responses.add(resp.get());
            }
            catch (Exception e)
            {
                System.out.println("Connection Problem");
            }
        }*/
        return responses;
    }
}
