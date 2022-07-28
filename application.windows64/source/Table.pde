class Table{
  private int[][][] table;
  private int[][][] proto_table;
  private int table_rows;
  private int table_columns;
  private int table_depth;
  
  public Table(int r, int c, int d){
   table = new int[r][c][d];
   proto_table = new int[r][c][d];
   table_rows = r;
   table_columns = c;
   table_depth = d;
   for(int i = 0; i < r; i++){
     for(int j = 0; j < c; j++){
       for(int k = 0; k < d; k++){
         table[i][j][k] = 0;
         proto_table[i][j][k] = 0;
       }
       
     }
   }
   
   table[10][10][10] = 1;
   table[10][10][9] = 1;
   table[10][10][8] = 1;
   table[10][9][10] = 1;
   table[10][8][10] = 1;
   table[9][10][10] = 1;
   table[8][10][10] = 1;
   table[11][10][10] = 1;
   table[12][10][10] = 1;
   table[10][11][10] = 1;
   table[10][12][10] = 1;
   table[10][10][11] = 1; 
   table[10][10][12] = 1; 
}
  
  public int[][][] getTable(){
    return table;
  }
  
  public int getRows(){
    return table_rows;
  }
  
  public int getColumns(){
    return table_columns;
  }
  
  public int getDepth(){
    return table_depth;
  }
  
  public boolean isAlive(int x, int y, int d){
    if(table[x][y][d] >= 1){
      return true;
    } else{
      return false;
    }
  }
  
  public boolean cell_exists(int offset_x, int offset_y, int current_cell_x, int current_cell_y){
    boolean flag = true;
    if(current_cell_x + offset_x < 0 || current_cell_x + offset_x >= table_rows)
    {
      flag = false;
    }
    
    if(current_cell_y + offset_y < 0 || current_cell_y + offset_y >= table_columns)
    {
      flag = false;
    }
    
    return flag;
  }
  
  public void decayCell(int x, int y,int z){
    if(table[x][y][z] > 0){
      proto_table[x][y][z] = table[x][y][z] - 1;
    }
  }
  
  public void setProtoCell(int x, int y, int d, int val){
    proto_table[x][y][d] = val;
  }
  
  public void updateTable(){
    for(int i = 0; i< table_rows; i++){
      for(int j = 0; j < table_columns; j++){
        for(int k = 0; k < table_depth; k++){
          table[i][j][k] = proto_table[i][j][k];
          proto_table[i][j][k] = 0;
        }
        
        
      }
    }
  }
}
