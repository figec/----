import java.util.HashSet;
import java.util.Set;

class Grid {
    protected int[][] grid; // ��������
    protected int BOX_SIZE; // С�Ź���ı߳�
    protected int GRID_SIZE; // ���������ı߳�

    public Grid(int[][] grid, int boxSize, int gridSize) {
        this.grid = grid;
        this.BOX_SIZE = boxSize; // ÿ��С�Ź���ı߳�
        this.GRID_SIZE = gridSize; // ���������ı߳�
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

        // ����Ϸ����ж�
        if (gridSize != Math.pow(boxSize, 2)) {
            throw new IllegalArgumentException("Input length is not valid.");
        }

        // ���ַ���ת��Ϊ��ά����
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = Integer.parseInt(Character.toString(gridStr.charAt(i * gridSize + j)));
            }
        }
        // // ����γɵ�����
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

        // ��ʼ����ѡ��
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                candidates[i][j] = new HashSet<>();
            }
        }

        // ����ÿ����Ԫ��
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != 0) {
                    // �����Ԫ��������ѡֵΪ���������
                    candidates[row][col].add(grid[row][col]);
                } else {
                    // �յ�Ԫ�񣬺�ѡֵΪ1��9
                    for (int num = 1; num <= 9; num++) {
                        candidates[row][col].add(num);
                    }
                }
            }
        }

        // �Ƴ��Ѵ������С��к�С�Ź��������
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
                    candidates[row][col].removeAll(existingNumbers); // �Ӻ�ѡֵ���Ƴ��Ѵ��ڵ�����
                }
            }
        }

        return candidates;
    }

    public static void main(String[] args) {
        String inputStr = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
        int length = inputStr.length();

        // ��������ʵ��
        Sudoku sudoku = new Sudoku(inputStr, (int) Math.sqrt(length / 9), (int) Math.sqrt(length));

        // ��ȡ������
        Set<Integer>[][] inference = sudoku.getInference();

        // ���ÿ����Ԫ��ĺ�ѡֵ
        for (int row = 0; row < sudoku.GRID_SIZE; row++) {
            for (int col = 0; col < sudoku.GRID_SIZE; col++) {
                System.out.print(inference[row][col]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}
