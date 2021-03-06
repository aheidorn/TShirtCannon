// RobotBuilder Version: 3.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Subsystem.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Cannon extends SubsystemBase {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
private Solenoid lights;
private WPI_VictorSPX cannonElevator;
private WPI_VictorSPX magazineRotator;
private DigitalInput leftAlignment;
private DigitalInput rightAlignment;
private Compressor firingMechanism;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    private Boolean _armed = false;
    private Boolean _rotateLeft = false;
    private Boolean _rotateRight = false;
    private Boolean _isNowRotating = false;
    private long _blinkLights = -1;
    /**
    *
    */
    public Cannon() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
lights = new Solenoid(0, PneumaticsModuleType.CTREPCM, 0);
 addChild("Lights", lights);
 

cannonElevator = new WPI_VictorSPX(6);
 
 

magazineRotator = new WPI_VictorSPX(5);
 
 

leftAlignment = new DigitalInput(0);
 addChild("LeftAlignment", leftAlignment);
 

rightAlignment = new DigitalInput(1);
 addChild("RightAlignment", rightAlignment);
 

firingMechanism = new Compressor(0, PneumaticsModuleType.CTREPCM);
 addChild("FiringMechanism",firingMechanism);
 


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (!_isNowRotating && (_rotateLeft || _rotateRight)) {
            _isNowRotating = !leftAlignment.get() && !rightAlignment.get();
        }
        if (_isNowRotating) {
            if (_rotateLeft && leftAlignment.get()) {
                magazineRotator.set(0.05);
            }
            if (_rotateRight && rightAlignment.get()) {
                magazineRotator.set(-0.05);
            }
            if (leftAlignment.get() && rightAlignment.get()) {
                magazineRotator.stopMotor();
                _isNowRotating = _rotateLeft = _rotateRight = false;
                if (_armed) {
                    lightsBlink();
                } else {
                    lightsOn();
                }
            } else {
                lightsOff();
            }
        }
        if (_blinkLights >= 0) {
            _blinkLights += 20;

            lights.set(_blinkLights >= 500);

            if (_blinkLights >= 1000) {
                _blinkLights = _blinkLights % 1000;
            }
        }
        if (!_armed) {
            firingMechanism.disable();
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void rotateLeft() {
        if (!_isNowRotating) {
            _rotateLeft = true;
            _rotateRight = false;
            magazineRotator.set(0.4);
        }
    }
    public void rotateRight() {
        if (!_isNowRotating) {
            _rotateLeft = false;
            _rotateRight = true;
            magazineRotator.set(-0.4);
        }
    }
    public void raiseCannon(double speed) {
        cannonElevator.set(speed);
    }
    public void arm(boolean armed) {
        _armed = armed;
    }
    public void fire() {
        if (_armed && !_isNowRotating && !_rotateLeft && !_rotateRight ) {
            firingMechanism.enableDigital();
            _armed = false;
        }
    }

    public void lightsOn() {
        _blinkLights = -1;
        lights.set(true);
    }
    public void lightsOff() {
        _blinkLights = -1;
        lights.set(false);
    }
    public void lightsBlink() {
        _blinkLights = 0;
        lights.set(true);
    }
}

