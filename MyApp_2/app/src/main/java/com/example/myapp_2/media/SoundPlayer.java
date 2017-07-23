package com.example.myapp_2.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.myapp_2.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by 11141 on 2017/7/10.
 */

public class SoundPlayer {
    private static MediaPlayer music;
    private static SoundPool soundPool;

    private static boolean musicSwitch = true; //音乐开关
    private static boolean soundSwitch = true; //音效开关
    private static Context context;

    private static final int[] musicId = {
            R.raw.background, R.raw.background2, R.raw.background3, R.raw.background4};
    private static final int[] soundId = {
            R.raw.move, R.raw.move2, R.raw.success, R.raw.failed};
    private static Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表

    public static void init(Context c)
    {
        context = c;
        initMusic();
        initSound();
    }
    //初始化音效播放器
    private static void initSound()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,100);

        soundMap = new HashMap<Integer,Integer>();
        for (int i=0;i<soundId.length;i++){
            soundMap.put(soundId[i], soundPool.load(context, soundId[i], 1));
        }
        /*soundMap.put(R.raw.move, soundPool.load(context, R.raw.move, 1));
        soundMap.put(R.raw.move2, soundPool.load(context, R.raw.move2, 1));
        soundMap.put(R.raw.success, soundPool.load(context, R.raw.success, 1));
        soundMap.put(R.raw.failed, soundPool.load(context, R.raw.failed, 1));*/
    }

    //初始化音乐播放器,加载背景音乐(可随机)
    private static void initMusic()
    {
        int r = new Random().nextInt(musicId.length);
        music = MediaPlayer.create(context,musicId[r]);
        music.setLooping(true);
    }
    //播放音效
    public static void playSound(int resId,float volume)
    {
        if(soundSwitch == false)
            return;
        Integer soundId = soundMap.get(resId);
        if(soundId != null)
            soundPool.play(soundId, volume, volume, 1, 0, 1);
    }
    public static void setMediaPlayerVolume(float volume){
        music.setVolume(volume,volume);
    }
    /**
     * 暂停音乐
     */
    public static void pauseMusic()
    {
        if(music.isPlaying())
            music.pause();
    }
    /**
     * 播放音乐
     */
    public static void startMusic()
    {
        if(musicSwitch)
            music.start();
    }
    /**
     * 切换一首音乐并播放
     * 这是背景音乐，可以更换，添加不同的资源即可
     */
    public static void changeAndPlayMusic()
    {
        if(music != null)
            music.release();
        initMusic();
        startMusic();
    }
    /**
     * 获得音乐开关状态
     * @return
     */
    public static boolean isMusicSwitch() {
        return musicSwitch;
    }
    /**
     * 设置音乐开关
     * @param musicSwitch
     */
    public static void setMusicSt(boolean musicSwitch) {
        SoundPlayer.musicSwitch = musicSwitch;
        if(musicSwitch)
            music.start();
        else
            music.stop();
    }
    /**
     * 获得音效开关状态
     * @return
     */
    public static boolean isSoundSwitch() {
        return soundSwitch;
    }
    /**
     * 设置音效开关
     * @param soundSwitch
     */
    public static void setSoundSwitch(boolean soundSwitch) {
        SoundPlayer.soundSwitch = soundSwitch;
    }
    /**
     * 发出移动的声音
     */
    public static void move(float volume){
        playSound(R.raw.move, volume);
    }
    public static void move2(float volume){
        playSound(R.raw.move2, volume);
    }
    public static void success(float volume){
        playSound(R.raw.success, volume);
    }
    public static void failed(float volume){
        playSound(R.raw.failed, volume);
    }
}
