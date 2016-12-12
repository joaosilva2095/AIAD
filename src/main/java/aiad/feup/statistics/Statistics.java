package aiad.feup.statistics;

import aiad.feup.agents.Board;
import aiad.feup.agents.RemoteAgent;
import aiad.feup.behaviours.player.investor.MakeOffer;
import aiad.feup.models.GameState;
import aiad.feup.models.PlayerType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Statistics {

    /**
     * Create statistics for the game
     */
    public static void generateStatistics() throws IOException {
        FileOutputStream out = new FileOutputStream(new File("statistics.xlsx"));

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        List<List<Object>> statistics = new ArrayList<>();

        Board board = Board.getInstance();

        List<Object> currentGame = new ArrayList<>();

        currentGame.add(board.getNumberManagers());
        currentGame.add(board.getNumberInvestors());
        currentGame.add(Board.NUMBER_ROUNDS);
        currentGame.add(Board.ROUND_DURATION);
        currentGame.add(MakeOffer.TICK_PERIOD);
        addWinners(currentGame);

        statistics.add(currentGame);

        makeGameStatistics(workbook, statistics);

        workbook.write(out);
        out.close();
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
     * Prints the statistics to an Excel file.
     *
     * @param workbook   workbook to save the statistics
     * @param statistics information about each game number
     */
    private static void makeGameStatistics(XSSFWorkbook workbook, List<List<Object>> statistics) {
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Game");

        makeGameFirstLine(sheet);

        int i;

        for (i = 0; i < statistics.size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell cell = row.createCell(0); //Game number
            cell.setCellValue(i + 1);

            List<Object> gameStats = statistics.get(i);
            for (int j = 0; j < gameStats.size(); j++) {
                cell = row.createCell(j + 1);

                Object stat = gameStats.get(j);
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
    }

    /**
     * Write first line of Excel file.
     *
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
}