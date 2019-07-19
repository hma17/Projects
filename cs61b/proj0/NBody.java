public class NBody {
    public static double readRadius(String filename) {
        double sunRadius = 2.50E11;
        In in = new In(filename);
        double first = in.readDouble();
        double second = in.readDouble();
        return second;
    }
/** erros included nullpointer exception, mismatchexception althouh that was misdiagnosed*/

    public static Body[] readBodies(String filename) {
        In in = new In(filename);
        int numOfbodies = in.readInt();
        double sunRadius = in.readDouble();
        Body[] bodies = new Body[numOfbodies];
        for (int i = 0; i < numOfbodies; i++) {
            double xPos = in.readDouble();
            double yPos = in.readDouble();
            double xVel = in.readDouble();
            double yVel = in.readDouble();
            double mass = in.readDouble();
            String bodyImagename = in.readString();
            Body bi = new Body(xPos, yPos, xVel, yVel, mass, bodyImagename);

            bodies[i] = bi;
        }
        return bodies;
    }
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double uniRadius = readRadius(filename);
        Body[] bodies = readBodies(filename);

        StdDraw.setXscale(-uniRadius, uniRadius);
        StdDraw.setYscale(-uniRadius, uniRadius);
        String imageToDraw = "images/starfield.jpg";
        StdDraw.picture(0.0, 0.0, imageToDraw);
        StdDraw.enableDoubleBuffering();

        double time = 0;
        while (time <= T) {
            Double[] xForces = new Double[bodies.length];
            Double[] yForces = new Double[bodies.length];
            int counter = bodies.length - 1;
            for (Body b: bodies) {
                xForces[counter] = b.calcNetForceExertedByX(bodies);
                yForces[counter] = b.calcNetForceExertedByY(bodies);
                counter -= 1;
            }
            int counter2 = bodies.length - 1;
            for (Body b: bodies) {
                b.update(dt, xForces[counter2], yForces[counter2]);
                counter2 -= 1;
            }
            StdDraw.picture(0.0, 0.0, imageToDraw);
            for (Body b: bodies) {
                b.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;

        }
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", uniRadius);
        for (int i = 0; i < bodies.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                    bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
        }
    }
}
