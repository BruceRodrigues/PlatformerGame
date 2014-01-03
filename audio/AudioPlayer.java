package audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {

	private Clip clip;

	public AudioPlayer(String s) {

		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass()
					.getResourceAsStream(s));

			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);

			AudioInputStream dais = AudioSystem.getAudioInputStream(
					decodeFormat, ais);

			this.clip = AudioSystem.getClip();
			this.clip.open(dais);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (this.clip != null) {
			stop();
			this.clip.setFramePosition(0);
			this.clip.start();
		}
	}

	public void stop() {
		if (this.clip.isRunning()) {
			this.clip.stop();
		}
	}

	public void close() {
		stop();
		this.clip.close();
	}
}
