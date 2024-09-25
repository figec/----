# coding=gb2312
import math

class Grid:
    # BOX_SIZE = 0
    # GRID_SIZE = 0

    def __init__(self, grid,box_size,grid_size):
        """��ʼ������grid��һ����ά����"""
        self.grid = grid
        self.BOX_SIZE = box_size # ÿ��С�Ź���ı߳�
        self.GRID_SIZE = grid_size # ���������ı߳�
    
    def get_grid_size(self):
        return self.GRID_SIZE

    def get_row(self, row):
        """��ȡָ���е�ֵ"""
        return self.grid[row]

    def get_column(self, col):
        """��ȡָ���е�ֵ"""
        return [self.grid[i][col] for i in range(self.GRID_SIZE)]

    def get_box(self, row, col):
        """��ȡָ����Ԫ�����ڵ�С�Ź����ֵ"""
        box = []
        box_row_start = (row // self.BOX_SIZE) * self.BOX_SIZE
        box_col_start = (col // self.BOX_SIZE) * self.BOX_SIZE

        for r in range(box_row_start, box_row_start + self.BOX_SIZE):
            for c in range(box_col_start, box_col_start + self.BOX_SIZE):
                box.append(self.grid[r][c])
        return box


class Sudoku(Grid):
    def __init__(self, grid_str,box_size,grid_size):
        """���ַ�����ʼ������ʵ��"""
        # ����Ϸ����ж�
        if grid_size != math.pow(box_size,2):
            raise ValueError("Input length is not valid.")
        

        # ���ַ���ת��Ϊ��ά����

        grid = [[int(grid_str[i * grid_size + j]) for j in range(grid_size)]
                for i in range(grid_size)]
        super().__init__(grid,box_size,grid_size)

    def get_inference(self):
        """����ÿ���յ�Ԫ��ĺ�ѡֵ"""
        candidates = [[set() for _ in range(self.GRID_SIZE)] for _ in range(self.GRID_SIZE)]

        # ����ÿ����Ԫ��
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] != 0:
                    # �����Ԫ��������ѡֵΪ���������
                    candidates[row][col].add(self.grid[row][col])
                else:
                    # �յ�Ԫ�񣬺�ѡֵΪ1��9
                    for num in range(1, 10):
                        candidates[row][col].add(num)

        # �Ƴ��Ѵ������С��к�С�Ź��������
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] == 0:
                    existing_numbers = set(self.get_row(row)) | set(self.get_column(col)) | set(self.get_box(row, col))
                    candidates[row][col] -= existing_numbers  # �Ӻ�ѡֵ���Ƴ��Ѵ��ڵ�����

        return candidates

# ���Դ���
if __name__ == "__main__":
    input_str = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    length = len(input_str)
    # print("length:",int(pow(length,0.5)))
    # print(int(math.pow(length,0.25)))
    # ��������ʵ��
    sudoku = Sudoku(input_str,int(math.pow(length,0.25)),int(pow(length,0.5)))  
    # ��ȡ������
    inference = sudoku.get_inference()  

    # ���ÿ����Ԫ��ĺ�ѡֵ
    for row in range(sudoku.get_grid_size()):
        print(f"Row {row}: {inference[row]}")
