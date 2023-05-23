public class PrintingSystem {

    public static void main(String[] args) {

        // The groups that the users are involved with
        ThreadGroup studentsGroup = new ThreadGroup("Students");
        ThreadGroup techniciansGroup = new ThreadGroup("Technicians");

        ServicePrinter sharedPrinter = new LaserPrinter("Uni Printer", 10, 50, studentsGroup);

        Thread student1 = new Student(studentsGroup, sharedPrinter, "student1");
        Thread student2 = new Student(studentsGroup, sharedPrinter, "student2");
        Thread student3 = new Student(studentsGroup, sharedPrinter, "student3");
        Thread student4 = new Student(studentsGroup, sharedPrinter, "student4");

        Thread paperTechnician = new PaperTechnician(techniciansGroup, sharedPrinter, "paperTechnician");
        Thread tonerTechnician = new TonerTechnician(techniciansGroup, sharedPrinter, "tonerTechnician");

        // Start all the threads
        student1.start();
        student2.start();
        student3.start();
        student4.start();

        paperTechnician.start();
        tonerTechnician.start();

        try {

            student1.join();
            student2.join();
            student3.join();
            student4.join();
            paperTechnician.join();
            tonerTechnician.join();

        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.print("\n");
        System.out.println(FontColor.RED_UNDERLINED + "                  PRINTER SUMMARY                 \n" + FontColor.RESET);
        System.out.println(sharedPrinter);


    }

}
