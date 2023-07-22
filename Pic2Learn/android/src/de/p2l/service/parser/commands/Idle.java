package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

/**
 * necessary to let the player char do nothing
 */
public class Idle extends Command{

    public static final Parcelable.Creator<Idle> CREATOR = new Parcelable.Creator<Idle>(){
        public Idle createFromParcel(Parcel in){
            return new Idle();
        }

        public Idle[] newArray(int size){
            return new Idle[size];
        }
    };

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		String out = super.toString();
		out += "Idle\n";
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