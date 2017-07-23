package com.example.myapp_2.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp_2.R;
import com.example.myapp_2.media.SoundPlayer;
import com.example.myapp_2.activity.GameActivity;
import com.example.myapp_2.activity.MainActivity;
import com.example.myapp_2.activity.MenuActivity;

import java.util.ArrayList;

/**
 * Created by 11141 on 2017/7/4.
 */

public class GameButton extends Button {
    private int row;
    private int column;
    private Bitmap bitmap;
    private TableLayout tableLayout;
    private GameImageView[][] imageViews;
    private Bitmap[][] bitmaps;
    private int[][] mp;
    private Dialog dialog;
    private boolean ismove;
    private ArrayList<Integer> moveList = new ArrayList<Integer>();
    private int xx;
    private int yy;

    private int type;
    private int pos;
    private int maxlevel;
    public void setRC(int row,int column){
        this.row = row;
        this.column = column;
    }
    public void setType(int type){
        this.type = type;
    }
    public GameButton(Context context) {
        super(context);
        init();
    }
    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                gameStart();
            }
        });
    }

    public void gameStart(){
        pos = MenuActivity.getMenuActivity().getPos();
        String username = MenuActivity.getMenuActivity().getUser().getUsername();
        maxlevel = MenuActivity.getMenuActivity().getMyDB().selectP(username).getLevel();
        GameActivity.getGameActivity().setType(type);
        setXY(row,column);
        ismove = true;
        if (MenuActivity.getMenuActivity().getPos() != -1){
            bitmap = BitmapFactory.decodeResource(getResources(),
                    MenuActivity.getMenuActivity().pictures[MenuActivity.getMenuActivity().getPos()]);
        }else{
            bitmap = MainActivity.getMainActivity().getBitmap();
        }
        WindowManager windowManager = GameActivity.getGameActivity().getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        bitmap = zoomBitmap(bitmap,width,width);
        tableLayout = (TableLayout) GameActivity.getGameActivity().findViewById(R.id.tl_game);
        chooseLevel();
        randMove();
        GameActivity.getGameActivity().setButtonColor();
        if (type == 3){
            GameActivity.getGameActivity().setTime();
        }else{
            GameActivity.getGameActivity().closeTime();
        }
    }

    private Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return newBitmap;
    }

    private Bitmap cutBitmap(Bitmap source,int x,int y,int width,int height){
        Bitmap newBitmap = Bitmap.createBitmap(source,x,y,width,height);
        return newBitmap;
    }

    private void chooseLevel(){
        moveList.clear();
        imageViews = new GameImageView[row+2][column+2];
        bitmaps = new Bitmap[row+2][column+2];
        mp = new int[row+2][column+2];
        for (int i=0;i<row+2;i++){
            for (int j=0;j<column+2;j++){
                mp[i][j] = 0;
            }
        }
        tableLayout.removeAllViewsInLayout();
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,5,0,0);
        int blockWidth = bitmap.getWidth()/column;
        int blockHeight = bitmap.getHeight()/row;
        for (int i=0;i<row;i++){
            TableRow tableRow = new TableRow(GameActivity.getGameActivity());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            for (int j=0;j<column;j++){
                int num = i * column + j + 1;
                mp[i+1][j+1] = num;
                bitmaps[i+1][j+1] = cutBitmap(bitmap,j*blockWidth,i*blockHeight,blockWidth,blockHeight);
                final GameImageView curView = new GameImageView(GameActivity.getGameActivity());
                curView.setLayoutParams(layoutParams);
                curView.setScaleType(ImageView.ScaleType.FIT_XY);
                curView.setImageBitmap(bitmaps[i+1][j+1]);
                curView.setIJ(i+1,j+1);
                curView.setId(num);
                curView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                        int i = curView.getI();
                        int j = curView.getJ();
                        if (ismove){
                            move(i,j);
                            check();
                        }
                    }
                });
                imageViews[i+1][j+1] = curView;
                tableRow.addView(imageViews[i+1][j+1]);
            }
            tableLayout.addView(tableRow);
        }
        imageViews[row][column].setVisibility(INVISIBLE);
    }

    private void swap(int i,int j,int ii,int jj){
        mp[i][j] = mp[ii][jj];
        mp[ii][jj] = row*column;
        int x = (imageViews[ii][jj].getId() - 1) / column + 1;
        int y = (imageViews[ii][jj].getId() - 1) % column + 1;
        imageViews[i][j].setImageBitmap(bitmaps[x][y]);
        imageViews[ii][jj].setImageBitmap(bitmaps[row][column]);
        imageViews[i][j].setVisibility(VISIBLE);
        imageViews[ii][jj].setVisibility(INVISIBLE);
        imageViews[i][j].setId(imageViews[ii][jj].getId());
        imageViews[ii][jj].setId(row*column);
    }
    private void setXY(int x,int y){
        this.xx = x;
        this.yy = y;
    }
    private void randMove(){
        int step = (int) (Math.random() * 10 * row * column + row*column);
        int i = row, j = column;
        for (int l=0;l<step;l++){
            int move = (int) (Math.random() * 4 + 1);
            moveList.add(move);
            if (move == 1 && mp[i-1][j] != 0){
                swap(i,j,i-1,j);
                i = i - 1;
            }else if (move == 2 && mp[i+1][j] != 0){
                swap(i,j,i+1,j);
                i = i + 1;
            }else if (move == 3 && mp[i][j-1] != 0){
                swap(i,j,i,j-1);
                j = j - 1;
            }else if (move == 4 && mp[i][j+1] != 0){
                swap(i,j,i,j+1);
                j = j + 1;
            }else {
                moveList.remove(moveList.size()-1);
            }
            setXY(i,j);
        }
    }
    private void check(){
        int flag = -1;
        for (int i=1;i<=row;i++){
            for (int j=1;j<=column;j++){
                int num = (i - 1) * column + j;
                if (mp[i][j] != num){
                    flag = 0;
                    break;
                }else if (i == row && j == column){
                    flag = 1;
                }
            }
            if (flag == 0){
                break;
            }
        }
        if (flag == 1 || ismove == false){
            if (pos == maxlevel){
                String username = MenuActivity.getMenuActivity().getUser().getUsername();
                MenuActivity.getMenuActivity().getMyDB().updated(username,maxlevel+1);
            }
            ismove = false;
            imageViews[row][column].setVisibility(VISIBLE);
            GameActivity.getGameActivity().closeTime();
            dialog = new Dialog(GameActivity.getGameActivity(),R.style.dialog);
            LayoutInflater inflater = LayoutInflater.from(GameActivity.getGameActivity());
            View dialogView = inflater.inflate(R.layout.dialog_game,null);
            dialog.setContentView(dialogView);
            TextView textView = (TextView)dialogView.findViewById(R.id.tv_dialog_game_message);
            textView.setText(R.string.game_successful);
            Button button1 = (Button)dialogView.findViewById(R.id.bt_dialog_game_restart);
            button1.setText(R.string.gameRestart);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                    dialog.dismiss();
                    WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    GameActivity.getGameActivity().getWindow().setAttributes(lp);
                    gameStart();
                }
            });
            Button button2 = (Button)dialogView.findViewById(R.id.bt_dialog_game_remark);
            button2.setText(R.string.gameRank);
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                    if (type ==3 && pos != -1){
                        String nickname = MenuActivity.getMenuActivity().getUser().getNickname();
                        String time = "";
                        int tt = 98 - GameActivity.getGameActivity().getRemainedTime();
                        if (tt < 10){
                            time = "0" + tt;
                        }else {
                            time = "" + tt;
                        }
                        int missionid = pos + 1;
                        MenuActivity.getMenuActivity().getMyDB().insertd(missionid,nickname,time);
                        dialog.dismiss();
                        WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
                        lp.alpha = 1f;
                        GameActivity.getGameActivity().getWindow().setAttributes(lp);
                        gameStart();
                        Toast.makeText(GameActivity.getGameActivity(),"记录排名成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(GameActivity.getGameActivity(),"该难度下不支持排名",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
            lp.alpha = 0.4f;
            GameActivity.getGameActivity().getWindow().setAttributes(lp);
            dialog.show();
            SoundPlayer.success(MenuActivity.getMenuActivity().getSoundVolume());
        }
    }
    private void move(int i,int j){
        if (ismove){
            int[] mx = {1,-1,0,0};
            int[] my = {0,0,1,-1};
            for (int k=0;k<4;k++){
                int x = i + mx[k];
                int y = j + my[k];
                if (mp[x][y] == row * column){
                    swap(x,y,i,j);
                    moveList.add(k+1);
                    setXY(i,j);
                    SoundPlayer.move(MenuActivity.getMenuActivity().getSoundVolume());
                    break;
                }
            }
        }
    }

    private int l = 0;
    private int k = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (k>=0){
                int i = xx, j = yy;
                int move = moveList.get(k);
                if (move == 1){
                    swap(i,j,i+1,j);
                    i = i + 1;
                }else if (move == 2){
                    swap(i,j,i-1,j);
                    i = i - 1;
                }else if (move == 3){
                    swap(i,j,i,j+1);
                    j = j + 1;
                }else if (move == 4){
                    swap(i,j,i,j-1);
                    j = j - 1;
                }
                setXY(i,j);
                SoundPlayer.move(MenuActivity.getMenuActivity().getSoundVolume());
                if (k == 0){
                    check();
                    GameActivity.getGameActivity().showButton();
                }
                k--;
                handler.postDelayed(this,100);
            }
        }
    };
    public void remove(){
        if (ismove){
            ismove = false;
            l = moveList.size();
            k = l - 1;
            handler.post(runnable);
        }
    }


}
