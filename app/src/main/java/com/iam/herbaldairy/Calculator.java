package com.iam.herbaldairy;

import android.util.Log;

public class Calculator {

    public static double waterVolumeForDiluteSpirit(double spiritVolume, int alcSource, int alcResult) {
        double clearSpiriteVolume = spiritVolume * (double) alcSource / 100;
        double resultVolume = clearSpiriteVolume / ((double) alcResult / 100);
        return resultVolume - spiritVolume;
    }

    public static double volumeOfDilutedSpirit(double spiritVolume, int alcSource, int alcResult) {
        double clearSpiriteVolume = spiritVolume * (double) alcSource / 100;
        return clearSpiriteVolume / ((double) alcResult / 100);
    }

    public static double spiritVolumeForSpirit(double spiritVolume, int alcSource, int alcResult) {

        double sourceResultDifferene = Math.abs(alcResult - alcSource);
        double spiritResultDifferene = Math.abs(96 - alcResult);

        return spiritVolume * (sourceResultDifferene / spiritResultDifferene);
    }



}
