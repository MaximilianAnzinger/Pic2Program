package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public class TurnLeft extends Command{

	public static final Parcelable.Creator<TurnLeft> CREATOR = new Parcelable.Creator<TurnLeft>(){
		@Override
		public TurnLeft createFromParcel(Parcel source) {
			return new TurnLeft();
		}

		@Override
		public TurnLeft[] newArray(int size) {
			return new TurnLeft[size];
		}
	};

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "TurnLeft\n";
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
