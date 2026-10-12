package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicThread extends Thread {

    private String filePath;
    private boolean loop;
    private Clip clip;

    public MusicThread(String filePath, boolean loop) {
        this.filePath = filePath;
        this.loop = loop;
    }

    @Override
    public void run() {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            System.err.println("音频文件未找到: " + filePath);
            return;
        }

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (loop) {
                // 循环播放
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

            // 等待播放结束（非循环）
            if (!loop) {
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放音乐
     */
    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}