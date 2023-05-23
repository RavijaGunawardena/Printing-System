//Represents the laser printer resource, which is shared within the printing system

public class LaserPrinter implements ServicePrinter{

    private String printerId;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;
    private ThreadGroup students;

    public LaserPrinter(String printerId, int currentPaperLevel, int currentTonerLevel, ThreadGroup students) {
        this.printerId = printerId;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numberOfDocumentsPrinted = 0;
        this.students = students;
    }

    // Method to print a document
    @Override
    public synchronized void printDocument(Document document) {

        String studentId = document.getUserId();
        String documentName = document.getNameOfTheDocument();
        int numberOfPages = document.getNumberOfPages();

        System.out.printf(FontColor.CYAN + "[%s] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                        " Request to print a document\n" + FontColor.RESET,
                studentId);

        // Checks if Student can proceed to print the document
        while (numberOfPages > currentPaperLevel || numberOfPages > currentTonerLevel) {

            if(numberOfPages > currentPaperLevel && numberOfPages > currentTonerLevel) {
                System.out.printf(FontColor.CYAN + "[%s] -" + FontColor.RESET +
                                "[%s][%dpg] - Current Paper Level is %d and Toner Level is %d. Therefore, Cannot proceed to print the document\n",
                        studentId, documentName, numberOfPages, currentPaperLevel, currentTonerLevel);
            }
            else if(numberOfPages > currentPaperLevel) {
                System.out.printf(FontColor.CYAN + "[%s] -" + FontColor.RESET +
                                " [%s][%dpg] - No papers to print. Current Paper Level is %d.\n",
                        studentId, documentName, numberOfPages, currentPaperLevel);
            }
            else {
                System.out.printf(FontColor.CYAN + "[%s] -" + FontColor.RESET +
                                " [%s][%dpg] - Out of toner. Current Toner Level is %d.\n",
                        studentId, documentName, numberOfPages, currentTonerLevel);
            }

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }

        // Print the document if there is available Toner and Papers
        // Update Student, Printer information
        System.out.print(FontColor.CYAN + "Document is printing... please wait... \n" + FontColor.RESET);
        currentPaperLevel -= numberOfPages;
        currentTonerLevel -= numberOfPages;
        numberOfDocumentsPrinted++;
        System.out.printf(FontColor.CYAN + "[%s] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                        " Successfully printed the document " + FontColor.MAGENTA + "[%s]" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                        " with page length " + FontColor.MAGENTA + "[%dpg]." + FontColor.RESET +  FontColor.GREEN_BOLD_BRIGHT +
                        " Available Paper Level is %d and Toner Level is %d.\n" + FontColor.RESET,
                studentId, documentName, numberOfPages, currentPaperLevel, currentTonerLevel);

        notifyAll();
    }

    // Method to update the toner cartridge of the printer
    @Override
    public synchronized void replaceTonerCartridge() {

        System.out.print(FontColor.YELLOW + "[TONER TECHNICIAN] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                        " Came to replace the Toner Cartridge\n" + FontColor.RESET);

        while (currentTonerLevel >= MINIMUM_TONER_LEVEL) {
            System.out.printf(FontColor.YELLOW + "[TONER TECHNICIAN] -" +  FontColor.RESET +
                            " No need of replacing the Toner at this time. Current Toner Level is %d\n", currentTonerLevel);
            try {
                if(students.activeCount() > 1) {
                    wait(5000);
                }
                else{
                    return;
                }

            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        // Allow Toner technician to refill the Toner
        System.out.print(FontColor.YELLOW + "Replacing the Toner Cartridge... please wait...\n" + FontColor.RESET);
        currentTonerLevel = PAGES_PER_TONER;
        System.out.printf(FontColor.YELLOW + "[TONER TECHNICIAN] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT+
                " Toner has been replaced successfully. New Toner Level is %d.\n" + FontColor.RESET, currentTonerLevel);

        notifyAll();

    }

    // PAPER TECHNICIAN to refill the paper
    @Override
    public synchronized void refillPaper() {

        System.out.print(FontColor.BLUE + "[PAPER TECHNICIAN] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                " Is about to refill the paper tray\n" + FontColor.RESET);

        while ((currentPaperLevel + SHEETS_PER_PACK) > MAXIMUM_SHEETS_OF_PAPER) {
            System.out.printf(FontColor.BLUE + "[PAPER TECHNICIAN] -" + FontColor.RESET +
                    "Exceeds maximum paper level. Current paper level is %d\n", currentPaperLevel);

            try {

                if(students.activeCount() > 1) {
                    wait(5000);
                }
                else{
                    return;
                }

            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        // Allow paper technician to refill paper
        System.out.print(FontColor.BLUE + "Refill the paper tray... please wait...\n" + FontColor.RESET);
        currentPaperLevel += SHEETS_PER_PACK;
        System.out.printf(FontColor.BLUE + "[PAPER TECHNICIAN] -" + FontColor.RESET + FontColor.GREEN_BOLD_BRIGHT +
                " Has been Refilled tray with pack of paper. New paper level is: %d.\n" + FontColor.RESET, currentPaperLevel);

        notifyAll();

    }

    @Override
    public synchronized String toString() {
        return "LaserPrinter{" +
                "Printer ID: '" + printerId + '\'' + ", " +
                "Paper Level: " + currentPaperLevel + ", " +
                "Toner Level: " + currentTonerLevel + ", " +
                "Documents Printed: " + numberOfDocumentsPrinted +
                '}';
    }

}
