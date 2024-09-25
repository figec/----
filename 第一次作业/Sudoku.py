# coding=gb2312
import math

class Grid:
    # BOX_SIZE = 0
    # GRID_SIZE = 0

    def __init__(self, grid,box_size,grid_size):
        """初始化网格，grid是一个二维数组"""
        self.grid = grid
        self.BOX_SIZE = box_size # 每个小九宫格的边长
        self.GRID_SIZE = grid_size # 整个数独的边长
    
    def get_grid_size(self):
        return self.GRID_SIZE

    def get_row(self, row):
        """获取指定行的值"""
        return self.grid[row]

    def get_column(self, col):
        """获取指定列的值"""
        return [self.grid[i][col] for i in range(self.GRID_SIZE)]

    def get_box(self, row, col):
        """获取指定单元格所在的小九宫格的值"""
        box = []
        box_row_start = (row // self.BOX_SIZE) * self.BOX_SIZE
        box_col_start = (col // self.BOX_SIZE) * self.BOX_SIZE

        for r in range(box_row_start, box_row_start + self.BOX_SIZE):
            for c in range(box_col_start, box_col_start + self.BOX_SIZE):
                box.append(self.grid[r][c])
        return box


class Sudoku(Grid):
    def __init__(self, grid_str,box_size,grid_size):
        """从字符串初始化数独实例"""
        # 输入合法性判断
        if grid_size != math.pow(box_size,2):
            raise ValueError("Input length is not valid.")
        

        # 将字符串转换为二维数组

        grid = [[int(grid_str[i * grid_size + j]) for j in range(grid_size)]
                for i in range(grid_size)]
        super().__init__(grid,box_size,grid_size)

    def get_inference(self):
        """推理每个空单元格的候选值"""
        candidates = [[set() for _ in range(self.GRID_SIZE)] for _ in range(self.GRID_SIZE)]

        # 遍历每个单元格
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] != 0:
                    # 如果单元格已填，则候选值为已填的数字
                    candidates[row][col].add(self.grid[row][col])
                else:
                    # 空单元格，候选值为1到9
                    for num in range(1, 10):
                        candidates[row][col].add(num)

        # 移除已存在于行、列和小九宫格的数字
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] == 0:
                    existing_numbers = set(self.get_row(row)) | set(self.get_column(col)) | set(self.get_box(row, col))
                    candidates[row][col] -= existing_numbers  # 从候选值中移除已存在的数字

        return candidates

# 测试代码
if __name__ == "__main__":
    input_str = "017903600000080000900000507072010430000402070064370250701000065000030000005601720"
    length = len(input_str)
    # print("length:",int(pow(length,0.5)))
    # print(int(math.pow(length,0.25)))
    # 创建数独实例
    sudoku = Sudoku(input_str,int(math.pow(length,0.25)),int(pow(length,0.5)))  
    # 获取推理结果
    inference = sudoku.get_inference()  

    # 输出每个单元格的候选值
    for row in range(sudoku.get_grid_size()):
        print(f"Row {row}: {inference[row]}")
