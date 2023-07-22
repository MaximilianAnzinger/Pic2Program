package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public class StepForward extends Command{

    public static final Parcelable.Creator<StepForward> CREATOR = new Parcelable.Creator<StepForward>(){
        @Override
        public StepForward createFromParcel(Parcel source) {
            return new StepForward(source);
        }

        @Override
        public StepForward[] newArray(int size) {
            return new StepForward[size];
        }
    };

    public StepForward(){

	}

	public StepForward(Parcel in){

	}

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "StepForward\n";
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
