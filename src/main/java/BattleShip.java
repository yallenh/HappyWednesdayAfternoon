import java.awt.*;
import java.util.Scanner;

public class BattleShip {
    public static void main(String argv[]) {
        BattleShip battleShipGame = new BattleShip();
        int attempts = battleShipGame.startGame();
        System.out.println("\n\n\nBattleship Java game finished! You hit 7 ships in "+ attempts + " attempts");
        battleShipGame.showBoard();
    }

    private int N;
    private int[][] board;
    private Ship[] ships;
    private int attempts;
    private int shipsDestroied;

    BattleShip() {
        this(10);
    }
    BattleShip(int N) {
        this.N = N;
    }

    public int startGame() {
        this.board = initBoard();
        this.ships = initShips();
        showShips();
        this.attempts = 0;
        this.shipsDestroied = 0;
        do{
            showBoard();
            Point shootPosition = shoot();
            this.attempts++;

            this.board[shootPosition.x][shootPosition.y] = 0; // m (by default)
            for (Ship ship : this.ships) {
                if (ship.getLife() <= 0) {
                    continue;
                }
                int life = ship.hitByPosition(shootPosition);
                if (life < 0) {
                    continue;
                }
                if (life > 0) {
                    this.board[shootPosition.x][shootPosition.y] = 1; // h
                }
                else {
                    // life == 0
                    this.shipsDestroied++;
                    for (Point position : ship.coordinates()) {
                        this.board[position.x][position.y] = 2; // d
                    }
                }
                break; // ships do not intersect
            }
            System.out.println("Total " + this.ships.length + " ships, destroy " + this.shipsDestroied);
        } while(this.ships.length != this.shipsDestroied);
        return this.attempts;
    }

    public int[][] initBoard() {
        int[][] board = new int[this.N][this.N];
        for (int row = 0; row < this.N; row++) {
            for (int column = 0; column < this.N; column++) {
                board[row][column] = -1;
            }
        }
        return board;
    }

    public void showBoard() {
        System.out.println();
        for (int column = 0; column < this.N; column++) {
            System.out.print("\t" + column);
        }
        System.out.println("");
        for(int row = 0; row < this.N ; row++) {
            System.out.print(row + "");
            for (int grid : this.board[row]) {
                // -1: -
                //  0: m
                //  1: h
                //  2: d
                switch (grid) {
                    case 0: System.out.print("\tm");
                        break;
                    case 1: System.out.print("\th");
                        break;
                    case 2: System.out.print("\td");
                        break;
                    default: System.out.print("\t-");
                        break;
                }
            }
            System.out.println("");
        }
    }

    public Ship[] initShips() {
        Ship[] ships = new Ship[7];
        int count = 0;
        Ship.setBoundary(this.N);

        // Submarine (1 x 1) - 2 nos
        ships[count++] = Ship.createShipWithSize(1);
        ships[count++] = Ship.createShipWithSize(1);

        // Destroyer (2 x 1) - 2 nos
        ships[count++] = Ship.createShipWithSize(2);
        ships[count++] = Ship.createShipWithSize(2);

        // Cruiser (3 x 1) - 1 nos
        ships[count++] = Ship.createShipWithSize(3);

        // Battleship(4 x 1) - 1 nos
        ships[count++] = Ship.createShipWithSize(4);

        // Carrier (5 x 1) - 1 nos
        ships[count++] = Ship.createShipWithSize(5);

        return ships;
    }

    public void showShips () {
        System.out.println(this.N);
        for (Ship ship : this.ships) {
            System.out.println(ship);
        }
    }

    public Point shoot() {
        Scanner input = new Scanner(System.in);
        String[] splited;
        do {
            System.out.print("Hit coordinate: ");
            String inputString = input.nextLine();
            splited = inputString.split("\\s+");
        } while (splited.length != 2);
        return new Point(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]));
    }
}
