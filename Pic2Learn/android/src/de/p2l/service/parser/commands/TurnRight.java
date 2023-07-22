package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public class TurnRight extends Command{

	public static final Parcelable.Creator<TurnRight> CREATOR = new Parcelable.Creator<TurnRight>(){
		@Override
		public TurnRight createFromParcel(Parcel source) {
			return new TurnRight();
		}

		@Override
		public TurnRight[] newArray(int size) {
			return new TurnRight[size];
		}
	};

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "TurnRight\n";
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
