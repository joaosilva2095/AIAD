package aiad.feup.statistics;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.models.PlayerType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Statistics {

    /**
     * Save statistics for the test environment
     */
    public static void saveStatistics() throws IOException {
        File statsFile = new File("statistics.xlsx");
        XSSFWorkbook workbook;
        if(statsFile.exists()) {
            workbook = new XSSFWorkbook(new FileInputStream(statsFile));
        } else {
            workbook = new XSSFWorkbook();
        }

        List<Object> statistics = new ArrayList<>();

        Board board = Board.getInstance();

        // Fill in statistics
        statistics.add(board.getNumberManagers());
        statistics.add(board.getNumberInvestors());
        statistics.add(Board.NUMBER_ROUNDS);
        statistics.add(Board.ROUND_DURATION);
        statistics.add(MakeOffer.TICK_PERIOD);
        addWinners(statistics);

        // General statistics
        makeGameStatistics(workbook, statistics);

        // Balances statistics
        Map<String, Double> balancesTemp = board.getBalances();
        Map<String, Double> balances = new HashMap<>();
        for(Map.Entry<String, Double> entry : balancesTemp.entrySet())
            balances.put(entry.getKey().split("@")[0], entry.getValue());
        makeBalancesStatistics(workbook, balances);

        FileOutputStream outFile = new FileOutputStream(statsFile);
        workbook.write(outFile);
        outFile.close();
    }

    /**
     * Add winners to the stats
     *
     * @param currentGame current game statistics
     */
    private static void addWinners(List<Object> currentGame) {
        RemoteAgent winnerManager = null, winnerInvestor = null;
        double maxManagerBalance = Integer.MIN_VALUE, maxInvestorBalance = Integer.MIN_VALUE, balance;
        Board board = Board.getInstance();
        for (RemoteAgent agent : board.getPlayers()) {
            balance = board.getBalance(agent.getName());
            if (board.getType(agent.getName()) == PlayerType.INVESTOR) {
                if (balance < maxInvestorBalance)
                    continue;
                maxInvestorBalance = balance;
                winnerInvestor = agent;
            } else {
                if (balance < maxManagerBalance)
                    continue;
                maxManagerBalance = balance;
                winnerManager = agent;
            }
        }
        if (winnerInvestor == null || winnerManager == null)
            throw new IllegalStateException("Winners are null");
        DecimalFormat df = new DecimalFormat("#0.00");
        currentGame.add(df.format(maxInvestorBalance) + "€");
        currentGame.add(winnerInvestor.getName().split("_")[0]);
        currentGame.add(df.format(maxManagerBalance) + "€");
        currentGame.add(winnerManager.getName().split("_")[0]);
    }

    /**
     * Prints the game statistics to an Excel file.
     * @param workbook   workbook to save the statistics
     * @param statistics information about each game number
     */
    private static void makeGameStatistics(XSSFWorkbook workbook, List<Object> statistics) {
        XSSFSheet sheet = workbook.getSheet("General");
        if(sheet == null) {
            sheet = workbook.createSheet("General");
            makeGameFirstLine(sheet);
        }

        int lastColumn = sheet.getLastRowNum();

        Row row = sheet.createRow(lastColumn + 1);

        Cell cell = row.createCell(0); //Game number
        cell.setCellValue(lastColumn + 1);
        for (int i = 0; i < statistics.size(); i++) {
            cell = row.createCell(i + 1);

            Object stat = statistics.get(i);
            if (stat instanceof String)
                cell.setCellValue((String) stat);
            else if (stat instanceof Double)
                cell.setCellValue((Double) stat);
            else if (stat instanceof Integer)
                cell.setCellValue((Integer) stat);
            else if (stat instanceof Long)
                cell.setCellValue((Long) stat);
        }
    }

    /**
     * Prints the balances statistics to an Excel file
     * @param workbook workbook to save the statistics
     * @param balances balances of the players
     */
    private static void makeBalancesStatistics(XSSFWorkbook workbook, Map<String, Double> balances) {
        XSSFSheet sheet = workbook.getSheet("Balances");
        if(sheet == null) {
            sheet = workbook.createSheet("Balances");
            makeBalancesFirstLine(sheet, balances.keySet());
        }

        int lastColumn = sheet.getLastRowNum();

        Row row = sheet.createRow(lastColumn + 1);

        Cell cell = row.createCell(0); //Game number
        cell.setCellValue(lastColumn + 1);

        // Fill in balances
        Row headerRow = sheet.getRow(0);
        String playerName;
        DecimalFormat df = new DecimalFormat("#0.00");
        for(int i = 1; i < headerRow.getLastCellNum() - 1; i++) {
            cell = row.createCell(i);
            playerName = headerRow.getCell(i).getStringCellValue();
            cell.setCellValue(df.format(balances.get(playerName)) + "€");
        }
    }

    /**
     * Write first line of Excel file.
     * @param sheet sheet of Excel
     */
    private static void makeGameFirstLine(XSSFSheet sheet) {
        Row row = sheet.createRow(0);

        int i = 0;

        Cell nextCell = row.createCell(i++);

        nextCell.setCellValue("Game Number");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Number Managers");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Number Investors");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Number Rounds");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Round Duration");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Tick Per Offer");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Best Investor (Balance)");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Best Investor (Type)");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Best Manager (Balance)");

        nextCell = row.createCell(i++);

        nextCell.setCellValue("Best Manager (Type)");
    }

    /**
     * Write first line of balances of Excel file.
     * @param sheet sheet of Excel
     * @param players players to add at header
     */
    private static void makeBalancesFirstLine(XSSFSheet sheet, Set<String> players) {
        Row row = sheet.createRow(0);

        int i = 0;

        Cell nextCell = row.createCell(i++);

        nextCell.setCellValue("Game Number");

        nextCell = row.createCell(i++);

        for(String player : players) {
            nextCell.setCellValue(player);

            nextCell = row.createCell(i++);
        }
    }
}