import Utils.Utils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;
import thirdservice.PortTime;

import java.awt.image.renderable.RenderableImageProducer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Test {
    static volatile int  threadCount = 3;

    public static void main(String[] args) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("Loose %d").build();
        ExecutorService Executor = Executors.newFixedThreadPool(1, factory);
       /* for (int j = 0; j < 2; j++) {
            Run myrun = new Run();
            int a = (threadCount % 2) == 0 ? 2 : 1;
            for (int i = 0; i < a; i++) {
                Executor.execute(myrun);
                threadCount--;
            }
        }*/

        Run r = new Run();
        List<Integer> mylist = new ArrayList<>();
        mylist.add(5);
        mylist.add(10);
        System.out.println(mylist.stream().mapToInt(a->a).sum());
    }
}

class Run implements Runnable {
    AtomicInteger count = new AtomicInteger(58);
    volatile AtomicBoolean printStart = new AtomicBoolean(false);
    AtomicBoolean saveResult = new AtomicBoolean(false);
    volatile boolean kek = false, print = false;
    final static String[] nameThreads = {"Liquid 0", "Loose 0", "Container 0"};
    String string = "First my run";

    Run(String string) {
        this.string = string;
    }

    Run() {

    }

    @Override
    public void run() {
        if (!printStart.get()) {
            printStart.set(true);
            System.out.println("start");
        }

        while (count.get() > 0) {
            Utils.sleep(2);

            count.getAndDecrement();
        }
        System.out.println("End");
        printKek();
    }

    private void printKek(){
        String result = new String();
        result += "Kek" + Thread.currentThread().getName();
        result += " LOl";
        System.out.println(result);
    }
}
