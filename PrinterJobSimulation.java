package task1;

class PrinterJob {
    private int pagesInTray = 10; // Initial number of pages in the printer tray
    
    // Synchronized method to print pages
    public synchronized void printPages(int pagesToPrint) {
        while (pagesInTray < pagesToPrint) {
            try {
                // If not enough pages are in the tray, wait
                System.out.println("Not enough pages in tray. Waiting for more pages...");
                wait(); // Wait until pages are available
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Printing pages
        pagesInTray -= pagesToPrint;
        System.out.println("Printing " + pagesToPrint + " pages...");
        System.out.println("Remaining pages in tray: " + pagesInTray);
    }
    
    // Synchronized method to add pages to the tray
    public synchronized void addPages(int pagesToAdd) {
        pagesInTray += pagesToAdd;
        System.out.println("Added " + pagesToAdd + " pages to the tray.");
        notify(); // Notify waiting printer job thread that pages are now available
    }

    public int getPagesInTray() {
        return pagesInTray;
    }
}

class PrinterThread extends Thread {
    private PrinterJob printerJob;
    private int pagesToPrint;

    public PrinterThread(PrinterJob printerJob, int pagesToPrint) {
        this.printerJob = printerJob;
        this.pagesToPrint = pagesToPrint;
    }

    @Override
    public void run() {
        // Attempt to print the pages
        printerJob.printPages(pagesToPrint);
    }
}

class PageCalculatorThread extends Thread {
    private PrinterJob printerJob;
    private int pagesToAdd;

    public PageCalculatorThread(PrinterJob printerJob, int pagesToAdd) {
        this.printerJob = printerJob;
        this.pagesToAdd = pagesToAdd;
    }

    @Override
    public void run() {
        // Simulate adding pages to the printer tray
        try {
            Thread.sleep(2000); // Wait a bit before adding pages
            printerJob.addPages(pagesToAdd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PrinterJobSimulation {
    public static void main(String[] args) {
        // Create the shared PrinterJob object
        PrinterJob printerJob = new PrinterJob();
        
        // Create threads: One for printing job, one for adding pages to the tray
        PrinterThread printerThread = new PrinterThread(printerJob, 15); // Attempt to print 15 pages
        PageCalculatorThread pageCalculatorThread = new PageCalculatorThread(printerJob, 10); // Add 10 pages to tray
        
        // Start both threads
        printerThread.start();
        pageCalculatorThread.start();
        
        // Wait for threads to finish
        try {
            printerThread.join();
            pageCalculatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final pages in tray after all operations
        System.out.println("Final pages in tray: " + printerJob.getPagesInTray());
    }
}
