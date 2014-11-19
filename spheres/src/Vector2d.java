//this is a class for 2d double vectors


public class Vector2d {
	
	double x;
	double y;

    public Vector2d(double x, double y)
    {
      this.x = x;
      this.y = y;
    }
    
    //dot product
    public double dot (Vector2d v)
    {
      return (this.x*v.x + this.y*v.y);
    }
    
    public double length()
    {
        return (double) Math.sqrt(this.x*this.x + this.y*this.y);
    }
    
    public void add (Vector2d v)
    {
        this.x += v.x;
        this.y += v.y;
    }
    
    public void sub (Vector2d v)
    {
        this.x -= v.x;
        this.y -= v.y;
    }
    
    public void scale (double s)
    {
    	this.x *= s;
    	this.y *= s;
    }
    
    public String toString()
    {
         return("(" + this.x + ", " + this.y + ")");
    }
    
    public Object clone() {
    	Vector2d temp = new Vector2d(0,0);
    	temp.x = this.x;
    	temp.y = this.y;
    	return temp;
    }
    
    public void normalize()
    {
        double norm;

        norm = (double)
               (1.0/Math.sqrt(this.x*this.x + this.y*this.y));
        this.x *= norm;
        this.y *= norm;
    }
}
