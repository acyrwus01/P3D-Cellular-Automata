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

void setup(){
  //fullScreen();
  main_table = new Table(x_bounds,y_bounds,z_bounds);
  size(600,600, P3D);
  frameRate(30);
  rotateCam();
  cubie = createShape(BOX, 1);
  cubie.setStrokeWeight(1);
  //hint(DISABLE_DEPTH_MASK);
}




void draw(){
  
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
void rotateCam(){
  float orbitRadius= 90;
  float ypos= 10;
  float xpos= cos(radians(rotation))*orbitRadius;
  float zpos= sin(radians(rotation))*orbitRadius;
  
  rotation++;
  camera(xpos, ypos, zpos, 0,0,0,0,-1,0);
}

float angle = 0;
void drawMainBox(){
  noFill();
  
  stroke(255);
  strokeWeight(2);
  box(x_bounds);
  
  angle += .01;
}

float offset = .5;
int cl = 0;
color c;
void draw_cubie(int x, int y, int z){
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

int color_lerp(int v){
  return v*5 + 100;
}

boolean inBounds(int x, int y, int z){
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

int countN(int x, int y, int z){
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

void applyRules(Table t)
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

void drawScene(){
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
