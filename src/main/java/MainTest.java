import com.hanhang.manage.ApplicationManager;

/**
 * @author hanhang
 */
public class MainTest {
    public static void main(String[] args){

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                ApplicationManager manager = ApplicationManager.getInstance();
                manager.init();
            }
        });

        t.start();

        while(true){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
