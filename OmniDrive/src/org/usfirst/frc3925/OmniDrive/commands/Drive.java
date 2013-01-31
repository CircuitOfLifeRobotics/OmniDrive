// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.


package org.usfirst.frc3925.OmniDrive.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3925.OmniDrive.Robot;
import org.usfirst.frc3925.OmniDrive.RobotMap;

/**
 *
 */
public class  Drive extends Command {

    public Drive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
	
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.omniDriveSubsystem);
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double movement = Robot.oi.xbox.getRawAxis(2);
        double strafe = Robot.oi.xbox.getRawAxis(1);
        double spin = Robot.oi.xbox.getRawAxis(4);
        
        
        double[] speeds = getOmniSpeeds(movement, strafe, spin);
        
        RobotMap.omniDriveSubsystemTopLeftJag.set(speeds[0]);
        RobotMap.omniDriveSubsystemTopRightJag.set(speeds[1]);
        RobotMap.omniDriveSubsystemBottomLeftJag.set(speeds[2]);
        RobotMap.omniDriveSubsystemBottomRightJag.set(speeds[3]);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        RobotMap.omniDriveSubsystemTopLeftJag.set(0);
        RobotMap.omniDriveSubsystemTopRightJag.set(0);
        RobotMap.omniDriveSubsystemBottomLeftJag.set(0);
        RobotMap.omniDriveSubsystemBottomRightJag.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private static final double RAD_45_DEG = Math.PI/4;
    private static final double SPINSCALE = 0.5D;
    
    private double[] getOmniSpeeds(double movement, double strafe, double spin)
    {
        double[] speeds = new double[4];
        
        movement = trimDouble(movement);
        strafe = trimDouble(strafe);
        spin = trimDouble(spin);
        
        Vector basevec = Vector.vectorXY(strafe, movement);
        basevec.angle -= RAD_45_DEG;
        
        //create speeds from basevec
        speeds[0] = (speeds[3] = basevec.r*Math.cos(basevec.angle));
        speeds[1] = (speeds[2] = basevec.r*Math.sin(basevec.angle));
        
        //adjust the speeds based on rotation
        
        for (int i = 0; i < 4 ; i++){
            if (i == 1 || i == 2){
                speeds[i] -= spin * SPINSCALE;
            } else {
                speeds[i] += spin * SPINSCALE;
            }
            speeds[i] = trimDouble(speeds[i]);
        }
        
        
        
        return speeds;
    }
    
    private double trimDouble(double in)
    {
        if (in > 1.0d){ in = 1.0d;}
        if (in < -1.0d){ in = -1.0d;}
        return in;
    }
    
    private static class Vector {
        
        public double angle;
        public double r;
        
        public Vector (double r, double angle)
        {
            this.r = r;
            this.angle = angle;
        }
        
        public static Vector vectorXY(double x, double y)
        {
            return new Vector(Math.sqrt(x*x + y*y),MathUtils.atan2(y, x));
        }
        
        public Vector add(Vector v)
        {
            double r, a, x, y;
            
            x = this.xdisplacement() + v.xdisplacement();
            y = this.ydisplacement() + v.ydisplacement();
            
            return vectorXY(x,y);
        }
        
        public double xdisplacement()
        {
            return r*Math.cos(angle);
        }
        public double ydisplacement()
        {
            return r*Math.sin(angle);
        }
    }
}
