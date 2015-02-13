
import echoclient.EchoClient;
import echoclient.EchoListener;
import echoserver.EchoServer;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Lars Mortensen
 */
public class TestClient {

    private String testResult = "";
    private CountDownLatch lock;

    public TestClient() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EchoServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        EchoServer.stopServer();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void sendMessage() throws IOException, InterruptedException {
        lock = new CountDownLatch(1);
        testResult = "";
        EchoClient tester = new EchoClient();
        tester.connect("localhost", 9090);

        tester.registerEchoListener(new EchoListener() {
            @Override
            public void messageArrived(String data) {
                testResult = data;
                lock.countDown();

            }
        });
        tester.send("Hello");
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertEquals("HELLO", testResult);
        tester.stopEcho();
    }

}


