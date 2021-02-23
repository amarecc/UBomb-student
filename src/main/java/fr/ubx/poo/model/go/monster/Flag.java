package fr.ubx.poo.model.go.monster;

/**
 * Flag
 */
public class Flag {
    private int value;

    public Flag(int value){
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}