import java.util.ArrayList;
public class Body {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static Double gee = 6.67E-11;
/** Why does compiling the two classes together
 *  this work*/
    public Body(
            double xP,
            double yP,
            double xV,
            double yV,
            double m,
            String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Body(Body b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }
    public double calcDistance(Body b) {
        double xDistance = Math.abs((b.xxPos) - (xxPos));
        double yDistance = Math.abs((b.yyPos) - (yyPos));
        double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        return distance;
    }
    public double calcForceExertedBy(Body b) {
        double fourCe = (gee * mass * b.mass) / Math.pow(calcDistance(b), 2);
        return fourCe;
    }
    public double calcForceExertedByX(Body b) {
        double xDistance = b.xxPos - xxPos;
        if (calcDistance(b) != 0) {
            double fourCe = (calcForceExertedBy(b) * (xDistance)) / (calcDistance(b));
            return fourCe;
        }
        return 0;
    }
    public double calcForceExertedByY(Body b) {
        double yDistance = b.yyPos - yyPos;
        if (calcDistance(b) != 0) {
            double fourCe = (calcForceExertedBy(b) * (yDistance)) / (calcDistance(b));
            return fourCe;
        }
        return 0;
    }
    public double calcNetForceExertedByX(Body[] bodyArray) {
        /**Syntax redundant*/

        ArrayList<Double> fourceList = new ArrayList();
        for (Body body : bodyArray) {
            double fourCe = calcForceExertedByX(body);
            fourceList.add(fourCe);
        }
        double netFource = 0;
        for (double fourCe: fourceList) {
            netFource += fourCe;
        }
        return netFource;
    }
    public double calcNetForceExertedByY(Body[] bodyArray) {
        /**Syntax redundant*/

        ArrayList<Double> fourceList = new ArrayList();
        for (Body body : bodyArray) {
            double fourCe = calcForceExertedByY(body);
            fourceList.add(fourCe);
        }
        double netFource = 0;
        for (double fourCe: fourceList) {
            netFource += fourCe;
        }
        return netFource;
    }
    public void update(double time, double xFource, double yFource) {
        double accX = xFource / mass;
        double accY = yFource / mass;
        xxVel = xxVel + (time * accX);
        yyVel = yyVel + (time * accY);
        xxPos = xxPos + (time * xxVel);
        yyPos = yyPos + (time * yyVel);

    }
    public void draw() {
        double startposX = xxPos;
        double startposY = yyPos;
        StdDraw.picture(startposX, startposY, "images/" + imgFileName);
    }
}
