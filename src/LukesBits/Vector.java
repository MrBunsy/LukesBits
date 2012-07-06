package LukesBits;



import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Luke
 */
public class Vector  implements Serializable{
    
    public double x,y,z;
    
    public Vector(double _x, double _y){
        this(_x,_y,0);
    }
    
    public Vector(double _x, double _y, double _z){
        x=_x;
        y=_y;
        z=_z;
    }
    
    public boolean equals(Vector v){
        return v!=null && v.x==x && v.y==y && v.z ==z;
    }
    
    public double get(int i){
        if(i==0){
            return x;
        }
        if(i==1){
            return y;
        }else{
            return z;
        }
        
    }
    
    public double getX(){
        return x;
    }
    
    public int getRoundedX(){
        return (int)Math.round(x);
    }
    
    public double getY(){
        return y;
    }
    
    public int getRoundedY(){
        return (int)Math.round(y);
    }
    
    public double getZ(){
        return z;
    }
    
    public int getRoundedZ(){
        return (int)Math.round(z);
    }
    
    public double getMagnitude(){
        
        double d = (x*x)+(y*y)+(z*z);
        
        //return SquareRootHack.sqrt((float)d);
        //return SquareRoot.sqrt((x*x)+(y*y)+(z*z));
        return Math.sqrt(d);
    }
    
    public double getMagnitudeSqrd(){
        return (x*x)+(y*y)+(z*z);
    }
    
    public double get2DAngle(){
        return Math.atan2(y, x);
    }
    
    public Vector getUnit(){
        //TODO check if mag equals one?  if it does, leave be.
        double mag = getMagnitude();
        if(mag==0){
            return new Vector(0,0,0);
        }
        double invSize = 1/mag;
        
        double newX=x*invSize;
        double newY=y*invSize;
        double newZ=z*invSize;
        
        return new Vector(newX,newY,newZ);
    }
    
    public Vector add(Vector a){
        return new Vector(x+a.x,y+a.y,z+a.z);
    }
    
    public Vector add(Vector a,double coef){ 
        return new Vector(x+coef*a.x,y+coef*a.y,z+coef*a.z);
    }
    
    public Vector subtract(Vector a,double coef){
        return add(a,-coef);
    }
    
    public Vector subtract(Vector a){
        return new Vector(x-a.x,y-a.y,z-a.z);
    }

    public double dot(Vector a){
        return a.x*x+a.y*y+a.z*z;
    }
    
    public Vector cross(Vector b){
        return new Vector(y*b.z-z*b.y , z*b.x-x*b.z, x*b.y - y*b.x);
    }
    
    public Vector twoDNormal(){
        return cross(new Vector(0,0,1).getUnit());
    }
    
    public Vector multiply(Double s){
        return new Vector(x*s,y*s,z*s);
    }
    
    //return a vector which is normal to vector a, and in a random direction.
    public Vector randomNormal(){
        Random r = new Random();
        
        return randomNormal(r);
    }
    
    //TODO test this
    public Vector rotate(Vector torque){
        return rotate(torque.getUnit(),torque.getMagnitude());
    }
    
     
   /**
    * http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
    * @return 
    */
   public Matrix crossProductMatrix(){
       double[][] newM = new double[3][3];
       
       for(int i=0;i<3;i++){
           for(int j=0;j<3;j++){
               newM[i][j]=0;
           }
       }
       
       newM[0][1]=-z;
       newM[0][2]=y;
       
       newM[1][0]=z;
       newM[1][2]=-x;
       
       newM[2][0]=-y;
       newM[2][1]=x;
       
       return new Matrix(newM);
   }
    
   public Matrix tensorProduct(){
       double[][] newM = new double[3][3];
       
       for(int i=0;i<3;i++){
           for(int j=0;j<3;j++){
               newM[i][j]=get(i)*get(j);
           }
       }

       
       return new Matrix(newM);
   }
   
   public static Matrix rotationMatrix(Vector axis, double angle){
        Vector u=axis.getUnit();
        
        Matrix R = Matrix.identity(3, 3).times(Math.cos(angle));
        
        R.plusEquals(u.crossProductMatrix().times(Math.sin(angle)));
        
        R.plusEquals(u.tensorProduct().times(1-Math.cos(angle)));
        
        return R;
   }
   
    //rotate by angle around axis
    public Vector rotate(Vector axis, double angle){
        Matrix R = rotationMatrix(axis,angle);
        return rotate(R);
    }
    
    //rotate using a rotation matrix
    public Vector rotate(Matrix R){
         Matrix v = new Matrix(3,1);
        v.set(0, 0, x);
        v.set(1, 0, y);
        v.set(2, 0, z);
        
        //answer
        Matrix ans = R.times(v);
        
        return new Vector(ans.get(0, 0),ans.get(1, 0),ans.get(2, 0));
    }
    
    public Vector randomNormal(Random r){// throws Exception{
        double Nx,Ny,Nz;
        
        do{
            Nx=Ny=Nz=0;
            //nextDouble is 0-1 inclusive 0, exclusive 1.  so to get answers strictly > 0, we're doing 1-ans
            //scrap that, forgot about -ve numbers needed
            switch(r.nextInt(3)){
            //switch(0){
                case 0:
                    Nx=1 - r.nextDouble()*2;
                    break;
                case 1:
                    Ny=1-r.nextDouble()*2;
                    break;
                case 2:
                    Nz=1-r.nextDouble()*2;
                    break;
            }
        }while(!validNormalSelection(Nx,Ny,Nz));
        
        //now either x,y or z is non zero
        
        if(Nx!=0){
            //x is now known
            if(z!=0){
                Ny = 1 - r.nextDouble()*2;
                Nz = -(x*Nx + y*Ny)/z;
            }else if(y!=0){
                Nz = 1 - r.nextDouble()*2;
                Ny = -(x*Nx + z*Nz)/y;
            }else{
                //throw new Exception("help");
                //panic
            }
            
            //Ny = (-2*x*Nx*y + Math.sqrt(x*x*Nx*Nx*y*y + (z*z+y*y)*(1-Nx*Nx)*z*z )  ) / (x*x + y*y);
            
            //Nz = (-2*x*Nx*z + Math.sqrt(x*x*Nx*Nx*z*z + (y*y+z*z)*(1-Nx*Nx)*y*y )  ) / (x*x + z*z);
        }else if(Ny !=0){
            //y is now known
            if(z!=0){
                Nx = 1 - r.nextDouble()*2;
                Nz = -(x*Nx + y*Ny)/z;
            }else if(x!=0){
                Nz = 1 - r.nextDouble()*2;
                Nx = -(y*Ny + z*Nz)/x;
            }else{
                //throw new Exception("help");
                //panic
            }
        }else if(Nz !=0){
            //y is now known
            if(y!=0){
                Nz = 1 - r.nextDouble()*2;
                Ny = -(x*Nx + z*Nz)/y;
            }else if(x!=0){
                Nz = 1 - r.nextDouble()*2;
                Nx = -(y*Ny + z*Nz)/x;
            }else{
                //throw new Exception("help");
                //panic
            }
        }else{
            //panic further
        }
        
        Vector n = new Vector(Nx,Ny,Nz);
        
        return n.getUnit();
    }
    
    public Vector predictableNormal(){
        if(!(x==1 && y==0 && z==0)){
            Vector n = new Vector(1,0,0);
            return cross(n).getUnit();
        }else{
            Vector n = new Vector(0,1,0);
            return cross(n).getUnit();
        }
    }
    
    private boolean validNormalSelection(double Nx, double Ny, double Nz){
        if(Nx!=0 && y==0 && z==0){
            //cannot create a true normal in this direction
            return false;
        }
        
        if(x==0 && Ny!=0 && z==0){
            //cannot create a true normal in this direction
            return false;
        }
        
        if(x==0 && y==0 && Nz!=0){
            //cannot create a true normal in this direction
            return false;
        }
        
        return true;
    }
    
    public Vector copy(){
        return new Vector(x,y,z);
    }
    
    @Override
    public String toString(){
        return toString(false);
    }
    
    public String toString(boolean twoD){
        if(twoD){
            return "("+Math.round(x*100.0)/100.0+","+Math.round(y*100.0)/100.0+")";
        }
        return "("+Math.round(x*100.0)/100.0+","+Math.round(y*100.0)/100.0+","+Math.round(z*100.0)/100.0+")";
    }
}
