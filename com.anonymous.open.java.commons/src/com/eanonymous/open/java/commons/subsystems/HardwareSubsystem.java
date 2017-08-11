package com.eanonymous.open.java.commons.subsystems;

public class HardwareSubsystem extends JavaSubsystem {

	public static final String TAG = "HARDWARE_SUBSYSTEM";

	@Override
	public String getName() {
		return "Hardware";
	}

	@Override
	public String getDescription() {
		return "Java hardware libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { JavaCoreSubsystem.TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] { "javax.sound.midi", "javax.sound.midi.spi", "javax.sound.sampled",
				"javax.sound.sampled.spi" };
	}
}
