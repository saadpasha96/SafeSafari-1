package pashainc.google.com.safesafari;

/**
 * Created by black on 3/20/2018.
 */

public class ReadData {

	public String startAddress,destAddress;

	public ReadData() {
	}

	public ReadData(String startAddress, String destAddress) {
		this.startAddress = startAddress;
		this.destAddress = destAddress;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
}
