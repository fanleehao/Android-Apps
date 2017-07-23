package com.example.fanleehao.game2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    GridLayout gridLayout = null;
    GestureDetector gd = null;
    TextView scoreText = null;
    Button btReset = null;
    List<Integer> spaceList = new ArrayList<Integer>();  //空格
    NumList numList = new NumList(); //数字
    List<Integer> changeList = new ArrayList<Integer>(); //更改的临时
    //用图片储存
    private final int[] icons = {
       R.drawable.space,R.drawable.bt2,R.drawable.bt4,R.drawable.bt8,R.drawable.bt16,R.drawable.bt32,
            R.drawable.bt64,R.drawable.bt128,R.drawable.bt256,R.drawable.bt512,R.drawable.bt1024,R.drawable.bt2048
    };
    final static int LEFT = -1;
    final static int RIGHT = 1;
    final static int UP = -4;
    final static int DOWN = 4;
    boolean moved = false;          //是否移动
    int score = 0;                  //得分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        gridLayout =(GridLayout) findViewById(R.id.gridlayout1);
        scoreText = (TextView) findViewById(R.id.scoretv);
        btReset = (Button) findViewById(R.id.btRet);
        MyGesDect mg = new MyGesDect();
        gd = new GestureDetector(mg);
        gridLayout.setOnTouchListener(mg);
        gridLayout.setLongClickable(true);

        scoreText.setText("得分：0");
        for(int i = 0; i < 16; i++){
            //Log.i("ha","2333333");
            View view = View.inflate(this, R.layout.item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            image.setBackgroundResource(icons[0]);
            spaceList.add(i);
            gridLayout.addView(view);
        }
        //随机加入两个值
        addRandomItem();addRandomItem();
        //重新游戏设置
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    };
    //从空列表中获取随机值
    public int getRandomIndex(){
        Random random = new Random();
        if(spaceList.size()>0)
            return random.nextInt(spaceList.size());
        else return -1;
    }
    //从空格中随机加入2
    public void addRandomItem(){
        int index = getRandomIndex();
        if(index!=-1){
            //获取坐标
            View view = gridLayout.getChildAt(spaceList.get(index));
            ImageView image = (ImageView) view.findViewById(R.id.image);
            int i = (int) Math.round(Math.random()+1);
            //设置图片为2或4
            image.setBackgroundResource(icons[i]);
            //加入numList中
            numList.add(spaceList.get(index), i);
            //相应去掉空白列中的记录
            spaceList.remove(index);
        }
    }
    /**
     * 监听手势事件的内部类，控制游戏操作，这里是实现了两个监听器的一个类
     */
    public class MyGesDect implements OnGestureListener, OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gd.onTouchEvent(motionEvent);
        }
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }
        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float vx, float vy) {
            return false;
        }
        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float vx, float vy) {
            //Log.i("ha","8888888");
            if(event1.getX()-event2.getX()>100){
                Log.i("ha","向左");                   //测试
                move(LEFT);
                return true;
            }
            else  if(event1.getX()-event2.getX()<-100){
                Log.i("ha","向右");
                move(RIGHT);
                return true;
            }
            else  if(event1.getY()-event2.getY()>100){
                Log.i("ha","向上");
                move(UP);
                return true;
            }
            else  if(event1.getY()-event2.getY()<-100){
                Log.i("ha","向下");
                move(DOWN);
                return true;
            }
            return false;
        }
    }

    /**
     * 以下是逻辑控制 *
     */

     // 获取移动方向上下一个格子的位置
    public int getNext(int index,int direction){
        int y = index/4;
        int x = index%4;
        if(x==3 && direction==RIGHT)
            return -1;
        if(x==0 && direction==LEFT)
            return -1;
        if(y==0 && direction==UP)
            return -1;
        if(y==3 && direction==DOWN)
            return -1;
        return index+direction;
    }
    /**
     * 获取移动方向上前一个格子的位置
     */
    public int getBefore(int index,int direction){
        int y = index/4;
        int x = index%4;
        if(x==0 && direction==RIGHT)
            return -1;
        if(x==3 && direction==LEFT)
            return -1;
        if(y==3 && direction==UP)
            return -1;
        if(y==0 && direction==DOWN)
            return -1;
        return index-direction;
    }
    /**
     * 交换当前格与目标空白格的位置
     */
    public void replace(int thisIdx, int nextIdx){
        moved = true;
        View thisView = gridLayout.getChildAt(thisIdx);
        ImageView image = (ImageView) thisView.findViewById(R.id.image);
        image.setBackgroundResource(icons[0]);
        //设置下一个
        View nextView = gridLayout.getChildAt(nextIdx);
        ImageView nextImage = (ImageView) nextView.findViewById(R.id.image);
        nextImage.setBackgroundResource(icons[numList.getNumberByIndex(thisIdx)]);
        //更新不同的存储值
        spaceList.remove(spaceList.indexOf(nextIdx));
        spaceList.add(thisIdx);
        numList.changeIndex(thisIdx, nextIdx);
    }
    /**
     * 合并在移动方向上两个相同的格子
     */
    public void levelup(int thisIdx, int nextIdx){
        if(!changeList.contains(nextIdx)){
            moved = true;
            //设置当前格子
            View thisView = gridLayout.getChildAt(thisIdx);
            ImageView image = (ImageView) thisView.findViewById(R.id.image);
            image.setBackgroundResource(icons[0]);
            //设置下一个
            View nextView = gridLayout.getChildAt(nextIdx);
            ImageView nextImage = (ImageView) nextView.findViewById(R.id.image);
            nextImage.setBackgroundResource(icons[numList.getNumberByIndex(nextIdx)+1]);
            //更新存储记录
            spaceList.add(thisIdx);
            numList.remove(thisIdx);
            //将数字列表对应的内容更新
            numList.levelup(nextIdx);
            changeList.add(nextIdx);
            //改变分数值
            updateScore((int)Math.pow(2, numList.getNumberByIndex(nextIdx)));
        }
    }
    //移动操作
    public void move(int direction){
        moved = false;
        changeList.clear();
        switch(direction){
            case RIGHT:
                for(int y=0;y<4;y++){
                    for(int x=2;x>=0;x--){
                        int thisIdx = 4*y +x;
                        Change(thisIdx,direction);
                    }
                }
                break;
            case LEFT:
                for(int y=0;y<4;y++){
                    for(int x=1;x<=3;x++){
                        int thisIdx = 4*y +x;
                        Change(thisIdx,direction);
                    }
                }
                break;
            case UP:
                for(int x=0;x<4;x++){
                    for(int y=1;y<=3;y++){
                        int thisIdx = 4*y +x;
                        Change(thisIdx,direction);
                    }
                }
                break;
            case DOWN:
                for(int x=0;x<4;x++){
                    for(int y=2;y>=0;y--){
                        int thisIdx = 4*y +x;
                        Change(thisIdx,direction);
                    }
                }
                break;
        }
        //有格子移动过
        if(moved)
            addRandomItem();
        //判断结束
        if(spaceList.size()==0){
            if(!numList.hasChance())
                gameOver();
        }
    }
    //变换
    public void Change(int thisIdx,int direction){
        if(numList.contains(thisIdx)){
            int nextIdx = getLast(thisIdx, direction);
            if(nextIdx == thisIdx){
                //不能移动
                return;
            }else if(spaceList.contains(nextIdx)){
                //存在可置换的格子
                replace(thisIdx,nextIdx);
            }else{
                if(numList.getNumberByIndex(thisIdx) == numList.getNumberByIndex(nextIdx)){
                    //可以合并
                    levelup(thisIdx, nextIdx);
                }else{
                    int before = getBefore(nextIdx, direction);
                    if(before != thisIdx){
                        //存在可置换的格子
                        replace(thisIdx,before);
                    }
                }
            }
        }
    }
    //获取最后一个可移动格子坐标
    public int getLast(int thisIdx, int direction){
        int nextIdx = getNext(thisIdx, direction);
        if(nextIdx < 0)
            return thisIdx;
        else{
            if(spaceList.contains(nextIdx))
                return getLast(nextIdx, direction);
            else
                return nextIdx;
        }
    }
    //分数
    public void updateScore(int add){
        score += add;
        scoreText.setText("得分："+score);
    }
    //判断游戏结束
    //参考。。。
    public void gameOver(){
        new AlertDialog.Builder(this)
                .setTitle("AHHHHHH!")
                .setMessage("游戏结束！得分："+score)
                .setPositiveButton("重新开始",new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .setNegativeButton("不想玩了", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                }).show();
    }
    /**
     * 清空界面，重新初始化
     */
    public void reset(){
        spaceList.clear();
        numList.clear();
        score = 0;
        //重新初始化
        gridLayout.removeAllViews();
        scoreText.setText("得分："+score);
        for(int i=0;i<16;i++){
            View view = View.inflate(this, R.layout.item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            image.setBackgroundResource(icons[0]);
            spaceList.add(i);
            gridLayout.addView(view);
        }
        addRandomItem(); addRandomItem();
    }
}
