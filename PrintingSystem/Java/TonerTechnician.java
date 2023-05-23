import java.util.Random;

public class TonerTechnician extends Thread{

    private ServicePrinter printer;

    public TonerTechnician(ThreadGroup threadGroup, ServicePrinter printer, String name) {
        super(threadGroup, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfRefills = 3;

        for (int i = 1; i <= numberOfRefills; i++) {

            printer.replaceTonerCartridge();

            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print(FontColor.YELLOW + "[TONER TECHNICIAN] - finished attempts to refill the printer with paper packs.\n" + FontColor.RESET);
    }

}
