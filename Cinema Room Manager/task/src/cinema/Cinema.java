package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {
    final static Scanner sc = new Scanner(System.in);
    private static String[][] cinema;
    private static int purchasedTicket = 0;
    private static int currentIncome = 0;

    private static boolean status;

    public static void main(String[] args) {
        System.out.println("Enter the number of rows:");
        String rowStr = sc.next();
        System.out.println("Enter the number of seats in each row:");
        String colStr = sc.next();

        int rows = string2Int(rowStr);
        int cols = string2Int(colStr);

        cinema = createCinema(rows, cols);

        String option;

        do {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            option = sc.next();
            cinemaService(cinema, option);
        } while (!option.equals("0"));
    }

    public static void displayCinema(String[][] cinema) {
        System.out.println("Cinema:");
        final int ROWS = cinema.length;
        final int COLS = cinema[0].length;
        for (int i = 0; i <= ROWS; ++i) {
           if (i == 0) {
               for (int j = 0; j <= COLS; ++j) {
                   if (j == 0) {
                       System.out.print("  ");
                   }
                   else {
                       System.out.printf("%d ", j);
                   }
               }
               System.out.println();
           }
           else {
               System.out.printf("%d ", i);
               for (int j = 0; j < COLS; ++j) {
                   System.out.printf("%s ", cinema[i-1][j]);
               }
               System.out.println();
           }
        }
    }

    public static void displayTicketPrice(int price) {
        System.out.printf("Ticket price: $%d\n", price);
    }

    public static void seatOrderController(String[][] cinema) {
        while (!status) {
            seatOrder(cinema);
        }

        status = false;
    }
    public static boolean seatOrder(String[][] cinema) {
        System.out.println("Enter a row number:");
        String orderedRowStr = sc.next();
        System.out.println("Enter a seat number in that row:");
        String orderedSeatStr = sc.next();

        int orderedRow = string2Int(orderedRowStr);
        int orderedSeat = string2Int(orderedSeatStr);

        if (orderedRow > getTotalRows() || orderedSeat > getSeatInEachRow()) {
            System.out.println("Wrong input!");
            status =  false;
            return false;
        }

        if (!checkAvailableSeat(orderedRow, orderedSeat)) {
            System.out.println("That ticket has already been purchased!");
            status = false;
            return false;
        }

        int price = calculateTicketPrice(cinema, orderedRow);

        boolean isSuccess = updateOrderedSeat(orderedRow, orderedSeat);
        if (isSuccess) {
            updatePurchasedTicket(1);
            updateCurrentIncome(price);
            displayTicketPrice(price);
            status = true;
            return true;
        }
        else {
            status = false;
            return false;
        }
    }

    public static boolean updatePurchasedTicket(int quantity) {
        purchasedTicket += quantity;
        return true;
    }

    public static int calculateTicketPrice(String[][] cinema, int rows) {
        int totalSeat = getTotalSeat();
        int frontHalf = cinema.length/2;

        if (totalSeat > 60 && rows > frontHalf) {
            return 8;
        }

        return 10;
    }

    public static boolean updateCurrentIncome(int amount) {
        currentIncome += amount;
        return true;
    }

    public static int getTotalIncome(String[][] cinema) {
        int totalIncome;
        int totalRows = cinema.length;
        int seatInEachRow = cinema[0].length;

        if (totalRows * seatInEachRow > 60) {
            int front = totalRows/2;
            int back = totalRows - front;
            totalIncome = front * seatInEachRow * 10 + back * seatInEachRow * 8;
        }
        else {
            totalIncome = totalRows * seatInEachRow * 10;
        }

        return totalIncome;
    }

    public static int string2Int(String obj) {
        return Integer.parseInt(obj);
    }

    public static String[][] createCinema(int rows, int cols) {
        String[][] cinema = new String[rows][cols];
        for (String[] row: cinema) {
            Arrays.fill(row, "S");
        }
        return cinema;
    }

    public static boolean checkAvailableSeat(int rowNum, int colNum) {
        boolean ordered = cinema[rowNum - 1][colNum-1].equals("B");
        return !ordered;
    }

    public static boolean updateOrderedSeat(int rowNum, int colNum) {
        cinema[rowNum-1][colNum-1] = "B";
        return true;
    }

    public static int getTotalSeat() {
        int totalRows = getTotalRows();
        int seatInEachRow = getSeatInEachRow();

        return totalRows * seatInEachRow;
    }

    public static int getPurchasedTicket() {
        return purchasedTicket;
    }

    public static double getPercentage() {
        return 100.0 * getPurchasedTicket()/getTotalSeat();
    }

    public static void statistic(String[][] cinema) {
        System.out.printf("Number of purchased tickets: %d\n", getPurchasedTicket());
        System.out.printf("Percentage: %.2f%%\n", getPercentage());
        System.out.printf("Current income: $%d\n", getCurrentIncome());
        System.out.printf("Total income: $%d\n", getTotalIncome(cinema));
    }

    public static int getCurrentIncome() {
        return currentIncome;
    }

    public static int getTotalRows() {
        return cinema.length;
    }

    public static int getSeatInEachRow() {
        return cinema[0].length;
    }

    public static void cinemaService(String[][] cinema, String option) {
        switch (option) {
            case "1":
                displayCinema(cinema);
                break;
            case "2":
                seatOrderController(cinema);
                break;
            case "3":
                statistic(cinema);
                break;
            case "0":
                break;
            default:
                System.out.println("Unknown service");
                break;
        }
    }
}