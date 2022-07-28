import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class P3D_Cellular_Automata extends PApplet {

int[][] n_rules = {
  {-1,-1,-1},
  {-1,-1, 0},
  {-1, 0, -1},  
  {-1, +1,+1},
  {-1, +1, 0},
  {-1, 0, +1},
  {-1, 0, 0},
  {-1, +1, -1},
  {-1, -1, +1},
  {+1,-1,-1},
  {+1,-1, 0},
  {+1, 0, -1},  
  {+1, +1,+1},
  {+1, +1, 0},
  {+1, 0, +1},
  {+1, 0, 0},
  {+1, +1, -1},
  {+1, -1, +1},
  {0,-1,-1},
  {0,-1, 0},
  {0, 0, -1},  
  {0, +1,+1},
  {0, +1, 0},
  {0, 0, +1},
  {0, +1, -1},
  {0, -1, +1}
};

int x_bounds = 32;
int y_bounds = 32;
int z_bounds = 32;
int speed = 10;
Table main_table;

PShape cubie;

public void setup(){
  //fullScreen();
  main_table = new Table(x_bounds,y_bounds,z_bounds);
  
  frameRate(30);
  rotateCam();
  cubie = createShape(BOX, 1);
  cubie.setStrokeWeight(1);
  //hint(DISABLE_DEPTH_MASK);
}




public void draw(){
  
  background(0);
  translate(0,0,0);
  rotateCam();
  drawMainBox();
  
  drawScene();
  if(frameCount % speed == 0){
    applyRules(main_table);
    main_table.updateTable();
  }
  
}

int rotation = 0;
public void rotateCam(){
  float orbitRadius= 90;
  float ypos= 10;
  float xpos= cos(radians(rotation))*orbitRadius;
  float zpos= sin(radians(rotation))*orbitRadius;
  
  rotation++;
  camera(xpos, ypos, zpos, 0,0,0,0,-1,0);
}

float angle = 0;
public void drawMainBox(){
  noFill();
  
  stroke(255);
  strokeWeight(2);
  box(x_bounds);
  
  angle += .01f;
}

float offset = .5f;
int cl = 0;
int c;
public void draw_cubie(int x, int y, int z){
  pushMatrix();
  //c = color(color_lerp(x), color_lerp(y), color_lerp(z));
  //fill(c);
  //strokeWeight(1);
  cubie.setFill(color(color_lerp(x), color_lerp(y), color_lerp(z)));
  translate(x + offset, y+ offset,z + offset);
  shape(cubie);
  
  //box(1);
  popMatrix();
  
}

public int color_lerp(int v){
  return v*5 + 100;
}

public boolean inBounds(int x, int y, int z){
  boolean flag = true;
  if(x > (x_bounds - 1) || x < 0){
    flag = false;
  }
  
  if(y > (y_bounds - 1) || y < 0){
    flag = false;
  }
  
  if(z > (z_bounds - 1) || z < 0){
    flag = false;
  }
  
  return flag;
}

public int countN(int x, int y, int z){
  int n = 0;
  int grid[][][] = main_table.getTable();
  
  for(int[] offset : n_rules)
  {
    int _x = x + offset[0];
    int _y = y + offset[1];
    int _z = z + offset[2];
    if(inBounds(_x,_y,_z) && grid[_x][_y][_z] == 1)
    {
      n++;
    }
  }
  

  
  return n;
}

public void applyRules(Table t)
{
  int grid[][][] = t.getTable();
  int r = t.getRows();
  int c = t.getColumns();
  int d = t.getDepth();
  int n = 0;
  
  for(int i = 0; i < r; i++){
    for(int j = 0; j < c; j++){
      for(int k = 0; k < d; k++){
        n = countN(i,j,k);
        
        //APPLY RULES FOR BIT SET 1
        if(main_table.isAlive(i,j,k))
        {
          if(true)
          {
            main_table.setProtoCell(i,j,k,0);
          } else
          {
            main_table.setProtoCell(i,j,k,0);
          }
          
          
        } else{ //APPLY RULES FOR BIT SET 0
          if( n == 1 || n == 7){
            main_table.setProtoCell(i,j,k,1);
          }
          
        }
      }
    }
  }
}

public void drawScene(){
  int[][][] grid = main_table.getTable();
  int center_offset = -(x_bounds/2);
  int r = main_table.getRows();
  int c = main_table.getColumns();
  int d = main_table.getDepth();
  for(int i = 0; i < r; i++){
    for(int j = 0; j < c; j++){
      for(int k = 0; k < d; k++){
        if(main_table.isAlive(i,j,k))
        {
          draw_cubie(i + center_offset,j + center_offset,k + center_offset);
        }
        
      }
    }
  }
}
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
  public void settings() {  size(600,600, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "P3D_Cellular_Automata" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
