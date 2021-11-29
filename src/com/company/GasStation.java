package com.company;

import java.util.Random;

/**
 * @author Esben Christensen
 * @version 1.0
 * Fuel pump is an abstraction of a fuel pump at a gas station,
 * it is responsible for getting fuel from the main gas tank of the gas station.
 */
class FuelPump {

    /**
     * The fueltank to draw gas from
     */
    private FuelTank fuelTank;  // Gas tank reference

    /**
     * The speed at which the pump operates in liters per pump cycle
     */
    float pumpSpeed = 0.3f;     // liters per pump

    /**
     * Fuel counter, how much fuel has been pumped in liters
     */
    float fuelPumped = 0;       // total fuel pumped since last reset

    /**
     * Constructor of fuel pump
     * @param tank specifies which tank to draw gas from
     */
    public FuelPump(FuelTank tank) {
        fuelTank = tank;
    }

    /**
     * Resets the fuel counter of the fuel pump.
     */
    public void resetPumpCounter() {
        fuelPumped = 0;
    }

    /**
     * Returns the turnover for this specific pump.
     * @param price Price per liter.
     * @return Total turnover for this specific pump.
     */
    public int getTurnover(int price) {
        return (int)(price * fuelPumped);
    }

    /**
     * Tries to pump fuel from the gas stations gas tank.
     * @return the amount of fuel pumped.
     */
    public float pumpFuel() {
        // try to pump fuel
        if(fuelTank.tryPumpFuel(pumpSpeed)) {
            fuelPumped += pumpSpeed;
            return pumpSpeed;
        }
        // fall-through
        System.out.println("No more fuel in the gas tank!");
        return 0;
    }
}

/**
 * @author Esben Christensen
 * @version 1.0
 * The FuelTank class acts as an abstraction of a fuel tank for a gas station or similar.
 * It implements methods for adding or subtracting fuel from the tank.
 */
class FuelTank {

    /**
     * Indicator of how much fuel is in the tank
     */
    float fuelTankLevel;

    /**
     * Tries to pump fuel from the gas tank.
     * @param amount the amount to pump.
     * @return true if there is available fuel in the gas tank.
     */
    public boolean tryPumpFuel(float amount) {
        // there is fuel
        if(amount <= 1000-fuelTankLevel) {
            fuelTankLevel -= amount;
            return true;
        } // there is no fuel
        else if(amount > 1000-fuelTankLevel) {
            return false;
        }
        // Undefined behaviour
        return false;
    }

    /**
     * Tries to fill the gas stations fuel tank with specified amount of fuel.
     * @param amount the amount with which to fill up the gas tank.
     * @return true if there is room in the gas tank, otherwise false.
     */
    public boolean tryFillTank(float amount) {
        // Gas tank has room
        if(amount <= 1000-fuelTankLevel) {
            fuelTankLevel += amount;
            return true;
        } // Gas tank has no room
        else if(amount > 1000-fuelTankLevel) {
            return false;
        }
        // Undefined behaviour
        return false;
    }
}

/**
 * @author Esben Christensen
 * @version 1.0
 * The Gas station implements the setup and usage of the FuelTank and FuelPump classes.
 * We build an example gas station and make it operational.
 */
public class GasStation {

    /**
     * Entry point of the program
     * @param args program arguments
     * @throws InterruptedException for Thread.sleep()
     */
    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);

        // --- Build gas station ---
        // Put new fuel tank in the ground
        System.out.println("Installing gas tank ... ");
        Thread.sleep(2000);
        FuelTank tank = new FuelTank();
        System.out.println("Gas tank now installed.\n");

        // Install 8 new fuel pumps
        System.out.println("Installing 8 gas pumps ... ");
        Thread.sleep(1500);
        FuelPump[] fuelPumps = new FuelPump[8];
        for(int i = 0; i < 8; i++) {
            fuelPumps[i] = new FuelPump(tank);
        }
        System.out.println("8 pumps now installed.\n");

        // Fill new fuel tank with fuel
        System.out.println("Fuel delivery has been ordered.");
        Thread.sleep(5000);
        System.out.println("Fuel truck has arrived! Filling tank ...");
        Thread.sleep(2000);
        if(tank.tryFillTank(800)) {
            System.out.println("Filled the gas tank with 800 liters of fuel.");
            System.out.println("\nGas station ready for business ... !");
        }
        else
        {
            System.out.println("Failed to fill the gas tank with 800 liters of fuel...");
            System.out.println("\nClosing down, and going out of business.");
            return;
        }
        Thread.sleep(3000);

        // --- Handling customers ---
        System.out.println("Here comes first customer, a Fiat Punto, red...");
        Thread.sleep(2500);
        System.out.println("Lining up along the gas pump, aaaaaaaaand ...");
        Thread.sleep(2000);
        float totalPunto = 0;
        System.out.print("[");
        for(int i = 0; i < 60; i++) {
            if(i % 6 == 0) {
                System.out.print("#");
            }
            totalPunto += fuelPumps[1].pumpFuel();
            Thread.sleep(100);
        }
        System.out.print("]\n\n");
        Thread.sleep(1000);
        System.out.printf("and off goes the Fiat Punto, filled with %.1f liters of fuel for a price of %d,00 DKK\n", totalPunto, fuelPumps[1].getTurnover(10) );

        System.out.println("\nHere comes the second customer, a Opel Corsa, in black.");
        Thread.sleep(2500);
        System.out.println("Lining up along the gas pump, aaaaaaaaand ...");
        Thread.sleep(2000);
        float totalCorsa = 0;
        System.out.print("[");
        for(int i = 0; i < 80; i++) {
            if(i % 8 == 0) {
                System.out.print("#");
            }
            totalCorsa += fuelPumps[1].pumpFuel();
            Thread.sleep(100);
        }
        System.out.print("]\n\n");
        Thread.sleep(1000);
        System.out.printf("and off goes the Opel Corsa, filled with %.1f liters of fuel for a price of %.2f DKK\n", totalCorsa, totalCorsa*10);

        // Status
        System.out.printf("\nSo far the gas tank has lost %.1f liters of fuel, with a revenue of %d DKK", (800-tank.fuelTankLevel), fuelPumps[1].getTurnover(10)+1);


    }
}
