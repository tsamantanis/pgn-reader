import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class PgnReader {

    private static String[][][] chessboard = new String[8][8][2];
    private static String[] array = new String[7];
    private static boolean cross = false;

    public static String tagValue(String tagName, String game) {
        Scanner scanner = new Scanner(game);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains(tagName)) {
                return line.substring(line.indexOf("\"") + 1
                    , line.lastIndexOf("\""));
            }
        }
        return "NOT GIVEN";
    }

    public static void chessboard() {
        int m = 7;
        for (int i = 0; i < chessboard.length; i++) {
            chessboard[i][0][0] = "a";
            chessboard[i][0][0] += m + 1;
            chessboard[i][1][0] = "b";
            chessboard[i][1][0] += m + 1;
            chessboard[i][2][0] = "c";
            chessboard[i][2][0] += m + 1;
            chessboard[i][3][0] = "d";
            chessboard[i][3][0] += m + 1;
            chessboard[i][4][0] = "e";
            chessboard[i][4][0] += m + 1;
            chessboard[i][5][0] = "f";
            chessboard[i][5][0] += m + 1;
            chessboard[i][6][0] = "g";
            chessboard[i][6][0] += m + 1;
            chessboard[i][7][0] = "h";
            chessboard[i][7][0] += m + 1;
            m--;
        }

        chessboard[0][0][1] = "r";
        chessboard[0][7][1] = "r";
        chessboard[7][0][1] = "R";
        chessboard[7][7][1] = "R";
        chessboard[0][1][1] = "n";
        chessboard[0][6][1] = "n";
        chessboard[7][1][1] = "N";
        chessboard[7][6][1] = "N";
        chessboard[0][2][1] = "b";
        chessboard[0][5][1] = "b";
        chessboard[7][2][1] = "B";
        chessboard[7][5][1] = "B";
        chessboard[0][3][1] = "q";
        chessboard[7][3][1] = "Q";
        chessboard[0][4][1] = "k";
        chessboard[7][4][1] = "K";

        for (int i = 0; i < chessboard.length; i++) {
            chessboard[1][i][1] = "p";
            chessboard[6][i][1] = "P";
        }

        for (int i = 0; i < chessboard.length; i++) {
            for (int k = 2; k < 6; k++) {
                chessboard[k][i][1] = " ";
            }
        }
    }

    public static String[] scanmoves(String game) {
        Scanner scanner = new Scanner(game);
        String singleline = null;
        int commandcount = 0;
        String[] moves = {};
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            boolean pgnsplit = false;
            if (line.length() == 0) {
                pgnsplit = true;
            }
            if (pgnsplit) {
                while (scanner.hasNextLine()) {
                    line = line + scanner.nextLine();
                }
                for (int k = 0; k < line.length(); k++) {
                    if ((line.charAt(k) == '.') && (commandcount < 9)) {
                        if (k - 2 < 0) {
                            line = line.substring(k + 1, line.length());
                        } else {
                            line = line.substring(0, k - 2)
                                 + line.substring(k + 1, line.length());
                        }
                        commandcount++;
                    } else if ((line.charAt(k) == '.')
                        && (commandcount > 8)) {
                        if (k - 3 < 0) {
                            line = line.substring(k + 1, line .length());
                        } else {
                            line = line.substring(0, k - 3)
                                 + line.substring(k + 1, line.length());
                        }
                    }
                }
                if ((line.length() > 0) && (line.charAt(0) == ' ')) {
                    line = line.substring(1, line.length());
                }
                moves = line.split(" ");
            }
        }
        return moves;
    }

    public static void positions(String game) {
        String[] moves = scanmoves(game);
        int quantity = 0;
        if ((moves.length % 2) == 0) {
            quantity = (moves.length / 2);
        } else {
            quantity = (moves.length / 2) + 1;
        }
        String[] whitemoves = new String[quantity];
        String[] blackmoves = new String[moves.length / 2];

        if (moves.length > 0) {
            if ((moves.length % 2) == 0) {
                int k = 0;
                for (int i = 0; i < moves.length; i = i + 2) {
                    if (moves[i].charAt(1) == 'x') {
                        whitemoves[k] = removex(moves, 1, i);
                        k++;
                        cross = true;
                    } else if ((moves[i].length() > 2)
                        && (moves[i].charAt(2) == 'x')) {
                        whitemoves[k] = removex(moves, 2, i);
                        k++;
                        cross = true;
                    } else {
                        whitemoves[k] = moves[i];
                        k++;
                    }
                }
            } else {
                int k = 0;
                for (int i = 0; i < moves.length; i = i + 2) {
                    if ((moves[i].length() > 2)
                        && (moves[i].charAt(2) == 'x')) {
                        whitemoves[k] = removex(moves, 2, i);
                        k++;
                        cross = true;
                    } else if (moves[i].charAt(1) == 'x') {
                        whitemoves[k] = removex(moves, 1, i);
                        k++;
                        cross = true;
                    } else {
                        whitemoves[k] = moves[i];
                        k++;
                    }
                }
            }
            if ((moves.length % 2) == 0) {
                int j = 0;
                for (int i = 1; i < moves.length; i = i + 2) {
                    if ((moves[i].length() > 2)
                        && (moves[i].charAt(2) == 'x')) {
                        blackmoves[j] = removex(moves, 2, i);
                        j++;
                        cross = true;
                    } else if (moves[i].charAt(1) == 'x') {
                        blackmoves[j] = removex(moves, 1, i);
                        j++;
                        cross = true;
                    } else {
                        blackmoves[j] = moves[i];
                        j++;
                    }
                }
            } else {
                int j = 0;
                for (int i = 1; i < moves.length - 1; i = i + 2) {
                    if ((moves[i].length() > 2)
                        && (moves[i].charAt(2) == 'x')) {
                        blackmoves[j] = removex(moves, 2, i);
                        j++;
                        cross = true;
                    } else if (moves[i].charAt(1) == 'x') {
                        blackmoves[j] = removex(moves, 1, i);
                        j++;
                        cross = true;
                    } else {
                        blackmoves[j] = moves[i];
                        j++;
                    }
                }
            }
        }

        for (int i = 0; i < whitemoves.length; i++) {
            int dif = whitemoves.length - blackmoves.length;
            if (dif == 1) {
                if (i > 0) {
                    analyzemove(blackmoves[i - 1], false);
                    analyzemove(whitemoves[i], true);
                } else {
                    analyzemove(whitemoves[i], true);
                }
            } else {
                analyzemove(whitemoves[i], true);
                analyzemove(blackmoves[i], false);
            }
        }

    }

    public static String removex(String[] moves, int index, int i) {
        moves[i] = moves[i].substring(0, index)
            + moves[i].substring(index + 1, moves[i].length());
        return moves[i];
    }

    public static void analyzemove(String move, boolean white) {
        boolean becomequeen = false;
        if ((move.length() == 4) && ((move.charAt(3) == '+')
            || (move.charAt(3) == '#'))) {
            move = move.substring(0, 3);
        } else if ((move.length() == 6) && (move.charAt(3) == '=')) {
            becomequeen = true;
            move = move.substring(0, 3);
        }
        char[] specialpawns = {'R', 'N', 'B', 'Q', 'K'};
        if (contains(specialpawns, move.charAt(0))) {
            movespecialpawn(move, move.charAt(0), white);
        } else if (Character.isDigit(move.charAt(1))) {
            if (white) {
                if (move.length() > 0) {
                    int x = Character.getNumericValue(
                        findcoordinates(move).charAt(0));
                    int y = Character.getNumericValue(
                        findcoordinates(move).charAt(1));
                    int pawnx = fpawn('P', y);
                    pawnreplace(x, y, pawnx, y);
                }
            } else {
                if (move.length() > 0) {
                    int x = Character.getNumericValue(
                        findcoordinates(move).charAt(0));
                    int y = Character.getNumericValue(
                        findcoordinates(move).charAt(1));
                    int pawnx = fpawn('p', y);
                    pawnreplace(x, y, pawnx, y);
                }
            }
        } else if ((move.length() == 3) && (cross)
            && (Character.isDigit(move.charAt(2)))) {
            if (white) {
                move = move.substring(1);
                int x = Character.getNumericValue(
                    findcoordinates(move).charAt(0));
                int y = Character.getNumericValue(
                    findcoordinates(move).charAt(1));
                if ((chessboard[x + 1][y][1].equals("p"))
                    && (chessboard[x + 1][y + 1][1].equals("P"))) {
                    newQueen(x, y, becomequeen);
                    pawnreplace(x, y, x + 1, y + 1);
                    chessboard[x + 1][y][1] = " ";
                } else if ((chessboard[x + 1][y][1].equals("p"))
                    && (chessboard[x + 1][y - 1][1].equals("P"))) {
                    newQueen(x, y, becomequeen);
                    pawnreplace(x, y, x - 1, y + 1);
                    chessboard[x + 1][y][1] = " ";
                } else if (chessboard[x + 1][y - 1][1].equals("P")) {
                    newQueen(x + 1, y - 1, becomequeen);
                    pawnreplace(x, y, x + 1, y - 1);
                } else if (chessboard[x + 1][y + 1][1].equals("P")) {
                    newQueen(x + 1, y + 1, becomequeen);
                    pawnreplace(x, y, x + 1, y + 1);
                }

            } else {
                move = move.substring(1);
                int x = Character.getNumericValue(
                    findcoordinates(move).charAt(0));
                int y = Character.getNumericValue(
                    findcoordinates(move).charAt(1));
                if (chessboard[x - 1][y - 1][1].equals("p")) {
                    newQueen(x - 1, y - 1, becomequeen);
                    pawnreplace(x, y, x - 1, y - 1);
                } else if (chessboard[x - 1][y + 1][1].equals("p")) {
                    newQueen(x - 1, y + 1, becomequeen);
                    pawnreplace(x, y, x - 1, y + 1);
                }
            }
        } else if (move.equals("O-O")) {
            castling(move, white);
        } else if (move.equals("O-O-O")) {
            castling(move, white);
        }
        // Print Stuff
        // for (int i = 0; i < chessboard.length; i++) {
        //     for (int k = 0; k < 8; k++) {
        //         System.out.print(chessboard[i][k][0] + " ");
        //         if (k == 7) {
        //             System.out.println();
        //         }
        //     }
        // }
        // System.out.println();
        // for (int i = 0; i < chessboard.length; i++) {
        //     for (int k = 0; k < 8; k++) {
        //         System.out.print(chessboard[i][k][1] + "  ");
        //         if (k == 7) {
        //             System.out.println();
        //         }
        //     }
        // }
        // System.out.println("--------------");
    }

    private static boolean contains(char[] array2, char value) {
        boolean contains = false;
        for (char e: array2) {
            if (e == value) {
                contains = true;
            }
        }
        return contains;
    }

    public static void pawnreplace(int x, int y, int k, int j) {
        chessboard[x][y][1] = chessboard[k][j][1];
        chessboard[k][j][1] = " ";
    }

    public static void newQueen(int x, int y, boolean becomequeen) {
        if (becomequeen) {
            chessboard[x][y][1] = "Q";
        }
    }

    public static void movespecialpawn(String move, char pawn, boolean white) {
        if (!(white)) {
            pawn = Character.toLowerCase(pawn);
        }
        String[] allpawncoordinates = new String[4];
        String coordinates = "";
        if (move.length() == 4) {
            coordinates = fpawn2(pawn, move.charAt(1));
            move = move.substring(0, 1) + move.substring(2, 4);
        } else {
            coordinates = findpawn(pawn, white);
        }
        int count = 0;
        int q = 0;
        boolean moved = false;
        int j = coordinates.length() / 2;
        while (q < j) {
            allpawncoordinates[q] = "";
            if (coordinates.length() > 0) {
                allpawncoordinates[q] += coordinates.charAt(0);
                allpawncoordinates[q] += coordinates.charAt(1);
                coordinates = coordinates.substring(2, coordinates.length());
            }
            count++;
            q++;
        }
        int pawnnum = 0;
        while (!(moved) && (pawnnum < count)) {
            if (move.length() > 2) {
                move = move.substring(1, 3);
            }
            if ((isKnight(pawn))
                && (validknight(move,
                allpawncoordinates[pawnnum], white))) {
                moved = movepawn(move, allpawncoordinates, pawnnum);
            } else if ((isBishop(pawn))
                && (validatebishop(move,
                allpawncoordinates[pawnnum], white))) {
                moved = movepawn(move, allpawncoordinates, pawnnum);
            } else if ((isRook(pawn))
                && (validaterook(move,
                allpawncoordinates[pawnnum], white))) {
                moved = movepawn(move, allpawncoordinates, pawnnum);
            } else if (!((isKnight(pawn)) || (isBishop(pawn)) || (isRook(pawn)))
                && (around(move, pawn,
                allpawncoordinates[pawnnum], white))) {
                moved = movepawn(move, allpawncoordinates, pawnnum);
            }
            pawnnum++;
        }
    }

    public static boolean isKnight(char pawn) {
        if ((pawn == 'N') || (pawn == 'n')) {
            return true;
        }
        return false;
    }

    public static boolean isRook(char pawn) {
        if ((pawn == 'R') || (pawn == 'r')) {
            return true;
        }
        return false;
    }

    public static boolean isBishop(char pawn) {
        if ((pawn == 'B') || (pawn == 'b')) {
            return true;
        }
        return false;
    }

    public static boolean movepawn(String move, String[] allpawncoordinates,
        int pawnnum) {
        if (move.length() > 0) {
            int x = Character.getNumericValue(
                findcoordinates(move).charAt(0));
            int y = Character.getNumericValue(
                findcoordinates(move).charAt(1));
            int pawnx = Character.getNumericValue(
                allpawncoordinates[pawnnum].charAt(0));
            int pawny = Character.getNumericValue(
                allpawncoordinates[pawnnum].charAt(1));
            chessboard[x][y][1] = chessboard[pawnx][pawny][1];
            chessboard[pawnx][pawny][1] = " ";
            return true;
        }
        return false;
    }

    public static void castling(String move, boolean white) {
        if ((white) && (move.equals("O-O"))) {
            if ((isEmpty("75")) && (isEmpty("76"))
                && (chessboard[7][4][1].charAt(0) == 'K')
                && (chessboard[7][7][1].charAt(0) == 'R')) {
                chessboard[7][6][1] = chessboard[7][4][1];
                chessboard[7][5][1] = chessboard[7][7][1];
                chessboard[7][4][1] = " ";
                chessboard[7][7][1] = " ";
            }
        } else if ((white) && (move.equals("O-O-O"))) {
            if ((isEmpty("71")) && (isEmpty("72")) && (isEmpty("73"))
                && (chessboard[7][4][1].charAt(0) == 'K')
                && (chessboard[7][0][1].charAt(0) == 'R')) {
                chessboard[7][2][1] = chessboard[7][4][1];
                chessboard[7][3][1] = chessboard[7][0][1];
                chessboard[7][4][1] = " ";
                chessboard[7][0][1] = " ";
            }
        } else if (!(white) && (move.equals("O-O"))) {
            if ((isEmpty("05")) && (isEmpty("06"))
                && (chessboard[0][4][1].charAt(0) == 'k')
                && (chessboard[0][7][1].charAt(0) == 'r')) {
                chessboard[0][6][1] = chessboard[0][4][1];
                chessboard[0][5][1] = chessboard[0][7][1];
                chessboard[0][4][1] = " ";
                chessboard[0][7][1] = " ";
            }
        } else if (!(white) && (move.equals("O-O-O"))) {
            if ((isEmpty("01")) && (isEmpty("02")) && (isEmpty("03"))
                && (chessboard[0][4][1].charAt(0) == 'k')
                && (chessboard[0][0][1].charAt(0) == 'r')) {
                chessboard[0][2][1] = chessboard[0][4][1];
                chessboard[0][3][1] = chessboard[0][0][1];
                chessboard[0][4][1] = " ";
                chessboard[0][0][1] = " ";
            }
        }
    }

    public static boolean around(String move, char pawn, String coordinates,
        boolean white) {
        String coords = "";
        int k = 9;
        int i = 9;
        if (coordinates.length() > 0) {
            k = Character.getNumericValue(coordinates.charAt(0));
            i = Character.getNumericValue(coordinates.charAt(1));
        }
        int y = 0;
        int[] pawnmoves = {k - 1, i - 1, i + 1, k + 1, i - 1, i + 1
                , i + 1, k - 1, k + 1, i - 1, k - 1, k + 1};
        if (move.length() > 0) {
            y = Character.getNumericValue(
            findcoordinates(move).charAt(1));
        }
        for (int n = 0; n < pawnmoves.length; n = n + 3) {
            if ((pawnmoves[n] > -1) && (pawnmoves[n] < 8)) {
                if (n < 6) {
                    for (int j = 1; j < 3; j++) {
                        if ((pawnmoves[n + j] > -1)
                            && (pawnmoves[n + j] < 8)) {
                            coords += pawnmoves[n];
                            coords += pawnmoves[n + j];
                            if ((isEmpty(coords)) || (cross)) {
                                return true;
                            }
                            coords = "";
                        }
                    }
                } else {
                    for (int j = 1; j < 3; j++) {
                        if ((pawnmoves[n + j] > -1)
                            && (pawnmoves[n + j] < 8)) {
                            coords += pawnmoves[n + j];
                            coords += pawnmoves[n];
                            if ((isEmpty(coords)) || (cross)) {
                                return true;
                            }
                            coords = "";
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean validknight(String move, String coordinates,
        boolean white) {
        String coords = "";
        boolean foundempty = false;
        int k = 9;
        int i = 9;
        if (coordinates.length() > 0) {
            k = Character.getNumericValue(coordinates.charAt(0));
            i = Character.getNumericValue(coordinates.charAt(1));
        }

        int[] horsemoves = {k - 2, i - 1, i + 1, k + 2, i - 1, i + 1
                , i + 2, k - 1, k + 1, i - 2, k - 1, k + 1};
        int y = 0;
        if (move.length() > 2) {
            move = move.substring(1, 3);
        }
        if (move.length() > 0) {
            y = Character.getNumericValue(
            findcoordinates(move).charAt(1));
        }
        for (int n = 0; n < horsemoves.length; n = n + 3) {
            if ((horsemoves[n] > -1) && (horsemoves[n] < 8)) {
                if (n < 6) {
                    for (int j = 1; j < 3; j++) {
                        if ((horsemoves[n + j] > -1)
                            && (horsemoves[n + j] < 8)) {
                            coords += horsemoves[n];
                            coords += horsemoves[n + j];
                            if (((isEmpty(coords)) || (cross))
                                && (coords.equals(findcoordinates(move)))) {
                                return true;
                            }
                            coords = "";
                        }
                    }
                } else {
                    for (int j = 1; j < 3; j++) {
                        if ((horsemoves[n + j] > -1)
                            && (horsemoves[n + j] < 8)) {
                            coords += horsemoves[n + j];
                            coords += horsemoves[n];
                            if (((isEmpty(coords)) || (cross))
                                && (coords.equals(findcoordinates(move)))) {
                                return true;
                            }
                            coords = "";
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean validaterook(String move, String coordinates,
        boolean white) {
        int k = 9;
        int i = 9;
        String[] up = new String[7];
        String[] down = new String[7];
        String[] right = new String[7];
        String[] left = new String[7];

        if (coordinates.length() > 0) {
            k = Character.getNumericValue(coordinates.charAt(0));
            i = Character.getNumericValue(coordinates.charAt(1));
        }

        for (int j = 0; j < chessboard.length; j++) {
            if (k - (j + 1) > -1) {
                up[j] = "";
                up[j] += k - (j + 1);
                up[j] += i;
            }
            if (i - (j + 1) > -1) {
                left[j] = "";
                left[j] += k;
                left[j] += i - (j + 1);
            }
            if (k + (j + 1) < 8) {
                down[j] = "";
                down[j] += k + (j + 1);
                down[j] += i;
            }
            if (i + (j + 1) < 8) {
                right[j] = "";
                right[j] += k;
                right[j] += i + (j + 1);
            }
        }

        for (int n = 0; n < 7; n++) {
            if ((findcoordinates(move).equals(up[n]))
                || (findcoordinates(move).equals(down[n]))
                || (findcoordinates(move).equals(left[n]))
                || (findcoordinates(move).equals(right[n]))) {
                return true;
            }
        }
        return false;
    }

    public static boolean validatebishop(String move, String coordinates,
        boolean white) {
        int k = 9;
        int i = 9;
        String[] upright = new String[7];
        String[] upleft = new String[7];
        String[] downright = new String[7];
        String[] downleft = new String[7];

        if (coordinates.length() > 0) {
            k = Character.getNumericValue(coordinates.charAt(0));
            i = Character.getNumericValue(coordinates.charAt(1));
        }

        for (int j = 0; j < chessboard.length; j++) {
            if ((k - (j + 1) > -1) && (i + (j + 1) < 8)) {
                upright[j] = "";
                upright[j] += k - (j + 1);
                upright[j] += i + (j + 1);
            }
            if ((k - (j + 1) > -1) && (i - (j + 1) > -1)) {
                upleft[j] = "";
                upleft[j] += k - (j + 1);
                upleft[j] += i - (j + 1);
            }
            if ((k + (j + 1) < 8) && (i + (j + 1) < 8)) {
                downright[j] = "";
                downright[j] += k + (j + 1);
                downright[j] += i + (j + 1);
            }
            if ((k + (j + 1) < 8) && (i - (j + 1) > -1)) {
                downleft[j] = "";
                downleft[j] += k + (j + 1);
                downleft[j] += i - (j + 1);
            }
        }

        for (int n = 0; n < 7; n++) {
            if ((findcoordinates(move).equals(upright[n]))
                || (findcoordinates(move).equals(upleft[n]))
                || (findcoordinates(move).equals(downright[n]))
                || (findcoordinates(move).equals(downleft[n]))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String coordinates) {
        boolean empty = false;
        if (coordinates.equals("")) {
            return false;
        }
        int x2 = Character.getNumericValue(coordinates.charAt(0));
        int y2 = Character.getNumericValue(coordinates.charAt(1));
        if (chessboard[x2][y2][1].charAt(0) == ' ') {
            empty = true;
        }
        return empty;
    }

    public static String findcoordinates(String move) {
        String coordinates = "";
        for (int i = 0; i < chessboard.length; i++) {
            for (int k = 0; k < chessboard.length; k++) {
                if (move.equals(chessboard[k][i][0])) {
                    coordinates += k;
                    coordinates += i;
                }
            }
        }
        return coordinates;
    }

    public static String findpawn(char pawn, boolean white) {
        String coordinates = "";
        if (white) {
            for (int k = 0; k < chessboard.length; k++) {
                for (int i = 0; i < chessboard.length; i++) {
                    if (pawn == chessboard[k][i][1].charAt(0)) {
                        coordinates += k;
                        coordinates += i;
                    }
                }
            }
        } else {
            for (int k = 0; k < chessboard.length; k++) {
                for (int i = 0; i < chessboard.length; i++) {
                    if (pawn == chessboard[k][i][1].charAt(0)) {
                        coordinates += k;
                        coordinates += i;
                    }
                }
            }
        }
        return coordinates;
    }

    public static int fpawn(char pawn, int column) {
        int row = 0;
        for (int i = 0; i < chessboard.length; i++) {
            if (chessboard[i][column][1].charAt(0) == pawn) {
                return i;
            }
        }
        return row;
    }

    public static String fpawn2(char pawn, char columnletter) {
        int column = 0;
        for (int i = 0; i < chessboard.length; i++) {
            if (chessboard[0][i][0].charAt(0) == columnletter) {
                column = i;
            }
        }
        String coordinates = "";
        coordinates += fpawn(pawn, column);
        coordinates += column;
        return coordinates;
    }

    public static String finalPosition(String game) {
        chessboard();
        positions(game);
        String fen = "";
        int counter = 0;
        for (int k = 0; k < chessboard.length; k++) {
            for (int i = 0; i < chessboard.length; i++) {
                if (chessboard[k][i][1].equals(" ")) {
                    counter++;
                } else {
                    if (counter != 0) {
                        fen += counter;
                        fen += chessboard[k][i][1];
                        counter = 0;
                    } else {
                        fen += chessboard[k][i][1];
                    }
                }
                if (i == 7) {
                    if (counter != 0) {
                        fen += counter;
                        counter = 0;
                    }
                }
            }
            if (k < 7) {
                fen += "/";
            }
        }
        return fen;
    }

    public static String fileContent(String path) {
        Path file = Paths.get(path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String game = fileContent(args[0]);
        System.out.format("Event: %s%n", tagValue("Event", game));
        System.out.format("Site: %s%n", tagValue("Site", game));
        System.out.format("Date: %s%n", tagValue("Date", game));
        System.out.format("Round: %s%n", tagValue("Round", game));
        System.out.format("White: %s%n", tagValue("White", game));
        System.out.format("Black: %s%n", tagValue("Black", game));
        System.out.format("Result: %s%n", tagValue("Result", game));
        System.out.println("Final Position:");
        System.out.println(finalPosition(game) + "\n");
    }
}
