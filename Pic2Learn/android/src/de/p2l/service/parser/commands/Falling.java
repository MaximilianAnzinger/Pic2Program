package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

/**
 * necessary for letting the player char fallin into a pit
 */
public class Falling extends Command {

    public static final Parcelable.Creator<Falling> CREATOR = new Parcelable.Creator<Falling>(){
        @Override
        public Falling createFromParcel(Parcel source) {
            return new Falling();
        }

        @Override
        public Falling[] newArray(int size) {
            return new Falling[size];
        }
    };

    @Override
    /**
     * Debugging-Methode for Parser
     */
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String out = "";
        out += "Representing Falling\n";
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //pass
    }
}
