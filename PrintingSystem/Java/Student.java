import java.util.Random;

public class Student extends Thread{

    Document document;
    private Printer printer;
    int MINIMUM_SLEEPING_TIME = 1000;
    int MAXIMUM_SLEEPING_TIME = 5000;

    public Student(ThreadGroup threadGroup, Printer printer, String name) {
        super(threadGroup, name);
        this.printer = printer;
    }

    public void run() {
        Random random = new Random();
        int documentsPerStudent = 5; // The number of documents that the student can print

        for (int i = 1; i <= documentsPerStudent; i++) {

            String documentName = "Doc_" + i;

            document = new Document(this.getName(), documentName, generateRandomPageNumbers());
            printer.printDocument(document);

            // Sleep for a random amount of time between each printing request
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.printf(FontColor.CYAN + "%s finished printing: Documents: %d, Pages: %d.\n" + FontColor.RESET,
                this.getName(), documentsPerStudent, document.getNumberOfPages());
    }

    // Generates a random page count, within the range of 1 to 10 (inclusive)
    private int generateRandomPageNumbers() {
        return new Random().nextInt(10) + 1; // Adding 1 to ensure document is at least one page in length
    }

}
