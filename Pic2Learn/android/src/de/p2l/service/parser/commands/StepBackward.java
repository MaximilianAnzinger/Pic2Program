package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public class StepBackward extends Command{

    public static final Parcelable.Creator<StepBackward> CREATOR = new Parcelable.Creator<StepBackward>(){
        @Override
        public StepBackward createFromParcel(Parcel source) {
            return new StepBackward();
        }

        @Override
        public StepBackward[] newArray(int size) {
            return new StepBackward[size];
        }
    };

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "StepBackward\n";
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
