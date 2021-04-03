public class LoomTest {
    public static void main(String[] args){
        try {
            var a = Thread.startVirtualThread(() -> {
                System.out.println("Hello, Loom!");
            });
            a.join();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
