import java.util.HashSet;
import java.util.Set;

class Grid {
    protected int[][] grid; // 网格数据
    protected int BOX_SIZE; // 小九宫格的边长
    protected int GRID_SIZE; // 整个数独的边长

    public Grid(int[][] grid, int boxSize, int gridSize) {
        this.grid = grid;
        this.BOX_SIZE = boxSize; // 每个小九宫格的边长
        this.GRID_SIZE = gridSize; // 整个数独的边长
    }

    public int[] getRow(int row) {
        return grid[row];
    }

    public int[] getColumn(int col) {
        int[] column = new int[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            column[i] = grid[i][col];
        }
        return column;
    }

    public int[] getBox(int row, int col) {
        int[] box = new int[BOX_SIZE * BOX_SIZE];
        int index = 0;
        int boxRowStart = (row / BOX_SIZE) * BOX_SIZE;
        int boxColStart = (col / BOX_SIZE) * BOX_SIZE;

        for (int r = boxRowStart; r < boxRowStart + BOX_SIZE; r++) {
            for (int c = boxColStart; c < boxColStart + BOX_SIZE; c++) {
                box[index++] = grid[r][c];
            }
        }
        return box;
    }
}

class Sudoku extends Grid {
    public Sudoku(String gridStr, int boxSize, int gridSize) {
        super(new int[gridSize][gridSize], boxSize, gridSize);

        // 输入合法性判断
        if (gridSize != Math.pow(boxSize, 2)) {
            throw new IllegalArgumentException("Input length is not valid.");
        }

        // 将字符串转换为二维数组
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = Integer.parseInt(Character.toString(gridStr.charAt(i * gridSize + j)));
            }
        }
        // // 检查形成的数组
        // for (int i = 0; i < gridSize; i++) {
        // for (int j = 0; j < gridSize; j++) {
        // System.out.print(grid[i][j]);
        // System.out.print(" ");
        // }

        // System.out.println();
        // }

    }

    public Set<Integer>[][] getInference() {
        Set<Integer>[][] candidates = new HashSet[GRID_SIZE][GRID_SIZE];

        // 初始化候选集
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                candidates[i][j] = new HashSet<>();
            }
        }

        // 遍历每个单元格
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != 0) {
                    // 如果单元格已填，则候选值为已填的数字
                    candidates[row][col].add(grid[row][col]);
                } else {
                    // 空单元格，候选值为1到9
                    for (int num = 1; num <= 9; num++) {
                        candidates[row][col].add(num);
                    }
                }
            }
        }

        // 移除已存在于行、列和小九宫格的数字
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    Set<Integer> existingNumbers = new HashSet<>();
                    for (int num : getRow(row))
                        existingNumbers.add(num);
                    for (int num : getColumn(col))
                        existingNumbers.add(num);
                    for (int num : getBox(row, col))
                        existingNumbers.add(num);
                    candidates[row][col].removeAll(existingNumbers); // 从候选值中移除已存在的数字
                }
            }
        }

        return candidates;
    }

    public static void main(String[] args) {
        String inputStr = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
        int length = inputStr.length();

        // 创建数独实例
        Sudoku sudoku = new Sudoku(inputStr, (int) Math.sqrt(length / 9), (int) Math.sqrt(length));

        // 获取推理结果
        Set<Integer>[][] inference = sudoku.getInference();

        // 输出每个单元格的候选值
        for (int row = 0; row < sudoku.GRID_SIZE; row++) {
            for (int col = 0; col < sudoku.GRID_SIZE; col++) {
                System.out.print(inference[row][col]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}
