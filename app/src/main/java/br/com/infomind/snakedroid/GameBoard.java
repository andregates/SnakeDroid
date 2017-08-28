package br.com.infomind.snakedroid;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class GameBoard extends AppCompatActivity {
    MediaPlayer mpBG1;
    public final static String erros = "ErrosSnake";
    private boolean running=true;
    TextView tv;
    Context c =this;
    ImageView table[][];
    ArrayList<int[]> snake;
    int directionActive=0 ;   //0-direita  1-esquerda   2-cima   3-baixo
    ArrayList<int[]> direction;
    boolean flag =true;
    int n;
    int pontos=0;
    int statusFruta=0; //0-sem fruta na tabela   1-fruta existente na tabela
    int frutaX;
    int frutaY;
    int segundos=400;
    public int getDirectionActive(){return directionActive;}
    public int getSegundos() {
        return segundos;
    }
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //aqui a mágica
        setContentView(R.layout.activity_game_board);

        mpBG1 = MediaPlayer.create(this, R.raw.cobrabg1);
        n = 22; //grande

        atualizacaoDePontos();

        GridLayout layout = (GridLayout) findViewById(R.id.grid);
        this.table = new ImageView[n][n];

        int cont=0;

        layout.setColumnCount(n);
        layout.setRowCount(n);



        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                LayoutInflater inflater = LayoutInflater.from(this);
                ImageView cell = (ImageView) inflater.inflate(R.layout.inflate_image_view, layout, false);
                cell.setImageResource(R.drawable.grama);
                layout.addView(this.table[i][j]=cell);
            }
        }
        startInitialsParams();

    }


    public void atualizacaoDePontos(){
        tv = (TextView) findViewById(R.id.pontos_valor);
        tv.setText(""+this.pontos);
    }

    public void clickControl(View v){
        ImageView img;
        img = (ImageView) v;

        if (v.getId()==R.id.btnRight && directionActive!=1){
            directionActive=0;
            img = (ImageView) findViewById(R.id.btnLeft);
            img.setImageResource(R.drawable.btn_left_d);
        }else if (v.getId()==R.id.btnLeft && directionActive!=0){
            directionActive=1;
            img = (ImageView) findViewById(R.id.btnRight);
            img.setImageResource(R.drawable.btn_right_d);

        }else if (v.getId()==R.id.btnUp && directionActive!=3){
            directionActive=2;
            img = (ImageView) findViewById(R.id.btnDown);
            img.setImageResource(R.drawable.btn_down_d);

        }else if (v.getId()==R.id.btnDown && directionActive!=2) {
            directionActive = 3;
            img = (ImageView) findViewById(R.id.btnUp);
            img.setImageResource(R.drawable.btn_up_d);
        }

        if(directionActive==2 || directionActive==3) {
            img = (ImageView) findViewById(R.id.btnLeft);
            img.setImageResource(R.drawable.btn_left);
            img = (ImageView) findViewById(R.id.btnRight);
            img.setImageResource(R.drawable.btn_right);
        }

        if(directionActive==0 || directionActive==1) {
            img = (ImageView) findViewById(R.id.btnDown);
            img.setImageResource(R.drawable.btn_down);
            img = (ImageView) findViewById(R.id.btnUp);
            img.setImageResource(R.drawable.btn_up);
        }
    }

    public void pauseGame(View v){
        if(running){
            running=false;
            startGame();
            return;
        }else{
            running=true;
            startGame();
            return;
        }

    }

    private void startInitialsParams(){

        mpBG1.setLooping(true);
        mpBG1.start();
        //Criação da estrutura geral da Cobra
        int [] vetor = new int[3];
        this.snake = new ArrayList<>();
        //Criação da cabeça da cobra.
        snake.add(new int[]{n/2, n/2,directionActive});
        //Insere a cobra em sua posicao inicial no centro do tabuleiro
        table[snake.get(0)[0]][snake.get(0)[1] ].setImageResource(R.drawable.cabeca);

        //============================================ CONTROLE DE DIRECIONAMENTO
        this.direction = new ArrayList<>();
        //Direita
        this.direction.add(0, new int[]{0, 1});  //0 - Direita
        //Esquerda
        this.direction.add(1, new int[]{0, -1});  //1 - Esquerda
        //Para cima
        this.direction.add(2, new int[]{-1, 0});  //2 - Para Cima
        //Para baixo
        this.direction.add(3, new int[]{1, 0});  //3 - Para baixo

        //===========================================================================
       // Toast.makeText(c,"ss"+directionActive+"kkkkk"+direction.get(directionActive)[0]+"ssss"+direction.get(directionActive)[1],Toast.LENGTH_SHORT).show();
        startGame();
    }

    public void startGame(){

        final Handler handler = new Handler();

        new Thread (new Runnable() {
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(getSegundos());
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            snakeMovimentation();
                        }
                    });
                }
            }
        }).start();

    }

    public void snakeMovimentation() {
            int x=0,y=0,z=0;
            table[snake.get(0)[0]][snake.get(0)[1]].setRotation(0);
            if (snake.size()==1) table[snake.get(0)[0]][snake.get(0)[1]].setImageResource(R.drawable.grama);
            int fg=0;
            x = snake.get(0)[0];
            y = snake.get(0)[1];
            z = snake.get(0)[2];
            //Cabeça da cobra no limite da direita
            if (getDirectionActive() == 0 && (snake.get(0)[1] == n - 1)) {y=0; fg++;}
            //Cabeça da cobra no limite da esquerda
            if (getDirectionActive() == 1 && (snake.get(0)[1] == 0)) {y=n-1; fg++;}
            //Cabeça da cobra no limite superior
            if (getDirectionActive() == 2 && (snake.get(0)[0] == 0)) {x=n-1; fg++;}
            //Cabeça da cobra no limite inferior
            if (getDirectionActive() == 3 && (snake.get(0)[0] == n-1)) {x=0; fg++;}

            if(fg==0){
                x = snake.get(0)[0] + direction.get(getDirectionActive())[0];
                y = snake.get(0)[1] + direction.get(getDirectionActive())[1];
            }

            snakeEat(x,y);
            snakeMove(getDirectionActive());
            snake.get(0)[0] = x;
            snake.get(0)[1] = y;
            snake.get(0)[2]=getDirectionActive();

            switch(snake.get(0)[2]){
                case 0:
                    table[snake.get(0)[0]][snake.get(0)[1]].setRotation(0);
                    break;
                case 1:
                    table[snake.get(0)[0]][snake.get(0)[1]].setRotation(180);
                    break;
                case 2:
                    table[snake.get(0)[0]][snake.get(0)[1]].setRotation(270);
                    break;
                case 3:
                    table[snake.get(0)[0]][snake.get(0)[1]].setRotation(90);
                    break;
            }
            table[snake.get(0)[0]][snake.get(0)[1]].setImageResource(R.drawable.cabeca);
            if (!snakeDies(x, y)){
                MediaPlayer mp3 = MediaPlayer.create(this, R.raw.cobramorte);
                mp3.setLooping(true);
                mp3.start();
                mpBG1.stop();
                running=false;
            }
            if(this.statusFruta==0){generateFruit();}





    }

    public boolean snakeDies(int x, int y){
        boolean retorno=true;
        for(int i=snake.size()-1;i>0;i--) {
            if(x==snake.get(i)[0] && y==snake.get(i)[1]){
                //Cobra morreu
                retorno= false;
            }
        }
        return retorno;

    }

    public void snakeEat(int x, int y){
        if (x == this.frutaX && y == this.frutaY) {
            mp = MediaPlayer.create(this, R.raw.mordida);
            mp.setLooping(false);
            mp.start();
            this.statusFruta = 0;
            this.pontos += 50;
            if (this.segundos > 100) this.segundos -= 40;
            atualizacaoDePontos();
            snake.add(new int[]{0, 0, 0});
        }
    }

    public void snakeMove(int a){

        for(int i=snake.size()-1;i>0;i--) {
            table[snake.get(i)[0]][snake.get(i)[1]].setRotation(0);
            if(i==snake.size()-1)table[snake.get(i)[0]][snake.get(i)[1]].setImageResource(R.drawable.grama);

            snake.get(i)[0] = snake.get(i - 1)[0];
            snake.get(i)[1] = snake.get(i - 1)[1];
            snake.get(i)[2] = snake.get(i - 1)[2];

                table[snake.get(i)[0]][snake.get(i)[1]].setImageResource(R.drawable.corpo);
        }
    }


    public void generateFruit(){
        //Gera um novo local para a fruta, checando a matriz, bem como não gerando sobre a cobra.

        do {
            flag=false;
            Random r = new Random();
            this.frutaX = 0 + r.nextInt(n);
            this.frutaY = 0 + r.nextInt(n);
            for(int i=0;i<snake.size();i++){
                if(frutaX == snake.get(i)[0] && frutaY==snake.get(i)[1]){
                   this.flag=true;
                    Log.i("ErrosSnake", "Posicao da cobra");
                    break;
                }
            }
        }while(this.flag);
        Random r = new Random();
        int fruta=0;
        fruta = 0 + r.nextInt(3);
        switch(fruta){
            case 0:
                this.table[this.frutaX][this.frutaY].setImageResource(R.drawable.fruta1);
                break;
            case 1:
                this.table[this.frutaX][this.frutaY].setImageResource(R.drawable.fruta3);
                break;
            case 2:
                this.table[this.frutaX][this.frutaY].setImageResource(R.drawable.fruta2);
                break;
        }

        this.statusFruta=1;
    }

}