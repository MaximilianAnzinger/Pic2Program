package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

/**
 * necessary to let the player win
 */
public class Win extends Command {

    public static final Parcelable.Creator<Win> CREATOR = new Parcelable.Creator<Win>(){
        @Override
        public Win createFromParcel(Parcel source) {
            return new Win();
        }

        @Override
        public Win[] newArray(int size) {
            return new Win[size];
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
        out += "Representing Win\n";
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
