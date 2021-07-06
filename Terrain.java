public class Terrain{
  public static void main(String[] args) {
    StdDraw.setCanvasSize(950, 950);
    StdDraw.setScale(-1,1);
    //Diamond Terrain Initial Shape
    double[][][]xyz={
      {
        {0,0.75,0},
      {1,0,0}
    },//[][][0] means x, [][][1] means y, [][][2] means z.
      {
        {-1,0,0},
        {0,-0.75,0}
      },
    };
    TDraw(0,8,xyz,1.5);
  }

  //Recursive Function
  public static void TDraw(int n,int finalN,double[][][]xyz,double hurst){
    System.out.println(n);
  StdDraw.enableDoubleBuffering();
    //Basecase, if reached num of iterations => stop.
    if(n==finalN)
      return;

    //Timing and Clearing stuffs
    StdDraw.pause(750);
    //StdIn.readString();//Debug - Stops between Iterations
    StdDraw.clear();
    StdDraw.pause(150);

    //Array for new points and for midpoints
    double newxyz[][][]=
      new double[xyz.length+(xyz.length-1)][xyz[0].length+(xyz[0].length-1)][3];
    boolean newxyzBool[][]=
      new boolean[xyz.length+(xyz.length-1)][xyz[0].length+(xyz[0].length-1)];

    //Trasfers old coords/values while leaving space for new ones.
    int xcount=0;
    int ycount=0;
    for(double[][]i:xyz){
      for(double[]j:i){
        System.out.println(j[0]);
        System.out.println(j[1]);
        System.out.println(j[2]);
        newxyz[xcount][ycount][0]=j[0];
        newxyz[xcount][ycount][1]=j[1];
        newxyz[xcount][ycount][2]=j[2];
        newxyzBool[xcount][ycount]=true;
        ycount+=2;
      }
      xcount+=2;
      ycount=0;
    }

    //Calculate y and/or z deviation and such maths
    double dev=Math.sqrt((1.0)/(Math.pow(2,(2*hurst*(n+1))))*Math.pow(1,2));
    double randomShift=StdRandom.gaussian(0,dev);

    //Finds Midpoint (Middle of 4 corners)
    for(int i=1;i<newxyz.length-1;i+=2){
      for(int j=1;j<newxyz[i].length-1;j+=2){
        newxyz[i][j][0]=(newxyz[i-1][j-1][0]+newxyz[i+1][j+1][0])/2;
        newxyz[i][j][1]=(newxyz[i+1][j-1][1]+newxyz[i-1][j+1][1])/2;
        //Recalc randomShift
        randomShift=StdRandom.gaussian(0,dev);
        //Assign z
        newxyz[i][j][2]=(
          (newxyz[i+1][j+1][2])+
          (newxyz[i+1][j-1][2])+
          (newxyz[i-1][j+1][2])+
          (newxyz[i-1][j-1][2])
        )/4+randomShift;
        //Draw lines from 4 corners to midpoint
        StdDraw.line(newxyz[i-1][j-1][0],newxyz[i-1][j-1][1]+newxyz[i-1][j-1][2],
          newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
        StdDraw.line(newxyz[i+1][j-1][0],newxyz[i+1][j-1][1]+newxyz[i+1][j-1][2],
          newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
        StdDraw.line(newxyz[i-1][j+1][0],newxyz[i-1][j+1][1]+newxyz[i-1][j+1][2],
          newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
        StdDraw.line(newxyz[i+1][j+1][0],newxyz[i+1][j+1][1]+newxyz[i+1][j+1][2],
          newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
        //Sets assigned indexes in array to true to not recalculate on
        newxyzBool[i][j]=true;
      }
    }

    //Finds Side Midpoints
    for(int i=0;i<newxyz.length;i+=1){
      for(int j=0;j<newxyz[i].length;j+=1){
        if(!newxyzBool[i][j]){
          //Recalc randomShift
          randomShift=StdRandom.gaussian(0,dev);
          if(i%2==1){//Point on "Vertical" Line
            newxyz[i][j][0]=(newxyz[i-1][j][0]+newxyz[i+1][j][0])/2;
            newxyz[i][j][1]=(newxyz[i-1][j][1]+newxyz[i+1][j][1])/2;
            //Special Case for 4 or only 3 adjacent points
            if(j!=0&&j!=newxyz[i].length-1){
              newxyz[i][j][2]=(newxyz[i][j-1][2]+
                              newxyz[i][j+1][2]+
                              newxyz[i-1][j][2]+
                              newxyz[i+1][j][2]
                              )/4+randomShift;
            }
            else if(j==0){
              newxyz[i][j][2]=(newxyz[i][j+1][2]+
                              newxyz[i-1][j][2]+
                              newxyz[i+1][j][2]
                              )/3+randomShift;
            }
            else{
              newxyz[i][j][2]=(newxyz[i][j-1][2]+
                              newxyz[i-1][j][2]+
                              newxyz[i+1][j][2]
                              )/3+randomShift;
            }
            //Draw lines from corners to midpoints of sides
            StdDraw.line(newxyz[i-1][j][0],newxyz[i-1][j][1]+newxyz[i-1][j][2],
              newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
            StdDraw.line(newxyz[i+1][j][0],newxyz[i+1][j][1]+newxyz[i+1][j][2],
              newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
          }
          else{//Points on "Horizontal" Line
            newxyz[i][j][0]=(newxyz[i][j-1][0]+newxyz[i][j+1][0])/2;
            newxyz[i][j][1]=(newxyz[i][j-1][1]+newxyz[i][j+1][1])/2;
            //Special Case for 4 or only 3 adjacent points
            if(i!=0&&i!=newxyz.length-1){
              newxyz[i][j][2]=(newxyz[i][j-1][2]+
                              newxyz[i][j+1][2]+
                              newxyz[i-1][j][2]+
                              newxyz[i+1][j][2]
                              )/4+randomShift;
            }
            else if(i==0){
              newxyz[i][j][2]=(newxyz[i][j-1][2]+
                              newxyz[i][j+1][2]+
                              newxyz[i+1][j][2]
                              )/3+randomShift;
            }
            else{
              newxyz[i][j][2]=(newxyz[i][j-1][2]+
                              newxyz[i][j+1][2]+
                              newxyz[i-1][j][2]
                              )/3+randomShift;
            }
            //Draw lines from corners to midpoints of sides
            StdDraw.line(newxyz[i][j-1][0],newxyz[i][j-1][1]+newxyz[i][j-1][2],
              newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
            StdDraw.line(newxyz[i][j+1][0],newxyz[i][j+1][1]+newxyz[i][j+1][2],
              newxyz[i][j][0],newxyz[i][j][1]+newxyz[i][j][2]);
          }
        }
      }
    }
    //Show drawn lines and recursively call
    StdDraw.show();
    TDraw(n+1,finalN,newxyz,hurst);
  }
}
