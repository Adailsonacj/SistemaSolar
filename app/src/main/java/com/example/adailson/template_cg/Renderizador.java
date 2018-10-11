package com.example.adailson.template_cg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//essa classe implementa os métodos necessários para
//utilizar a biblioteca OPENGL no desenho gráfico que
// sera apresentado na tela pela superficie de desenho
class Renderizador implements GLSurfaceView.Renderer, View.OnTouchListener {
    
    private FloatBuffer buffer;
    private FloatBuffer coorTerra;
    private FloatBuffer coorLua;
    private FloatBuffer coorSol;
    private float posX = 0;
    private float posY = 0;
    private int alturaY = 0;
    private int larguraX = 0;
    private float angulo = 0;
    private Activity vrActivity = null;
    private int codTerra = 0;
    private int codLua = 0;
    private int codSol = 0;

    Renderizador(Activity activity) {
        this.vrActivity = activity;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Este método é chamado uma vez quando a superfície de desenho é criada.
        //inicial = System.currentTimeMillis();

        //atual = System.currentTimeMillis();

        //Configura a cor d limpeza no formato RGBA
        gl.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int largura, int altura) {
        //É chamado quando a superfície de desenho for alterada.

        larguraX = largura;
        alturaY = altura;
        //Configurando a área de cordenadas do plano cartesiano
        //MAtriz de projeção
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();

        //Define o espaço de trabalho.
        //volume (CUBO 3D) de renderização - Tudo que for configurado dentro desse volume aparece na tela.
        //Definindo X - Y - Z , LARGURA - ALTURA - PROFUNDIDADE
        gl.glOrthof(0.0f, largura, 0.0f, altura, -1.0f, 1.0f);

        //OPENGL aponta para nova matriz (De transformações geométricas)
        //Translação, Rotação e Escala
        //Matriz de câmera
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glViewport(0, 0, largura, altura);

        float[] vetorJava = {
                -100, 100,
                -100, -100,
                100, 100,
                100, -100
        };

        buffer = generateBuffer(vetorJava);

        //Coordenadas de textura para a máquna
        float[] vetCoordSol = {
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };

        coorSol = generateBuffer(vetCoordSol);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coorSol);
        codSol = carregaTextura(gl, R.mipmap.sol);

        float[] vetCoordTerra = {
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };

        coorTerra = generateBuffer(vetCoordTerra);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coorTerra);
        codTerra = carregaTextura(gl, R.mipmap.terra);

        float[] vetCoordLua = {
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };

        coorLua = generateBuffer(vetCoordTerra);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coorLua);
        codLua = carregaTextura(gl, R.mipmap.lua);


    }

    public int carregaTextura(GL10 gl, int codTextura) {
        //Carrega a imagem na memoria ram
        Bitmap imagem = BitmapFactory.decodeResource(vrActivity.getResources(), codTextura);

        //Define um array para armazenamento dos ids de textura
        int[] idTextura = new int[1];
        //gera as áreas na gpu e cria um id para cada uma
        gl.glGenTextures(1, idTextura, 0);

        //Aponta a máquina openGL para uma das áreas de memória criadas na gpu
        gl.glBindTexture(GL10.GL_TEXTURE_2D, idTextura[0]);

        //Copia a imagem da ram para a vram
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, imagem, 0);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        //Aponta a VRAM OpenGL para o nada
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        //Libera a memmória ram
        imagem.recycle();

        //retorna o
        return idTextura[0];
    }

    FloatBuffer generateBuffer(float[] coordenadas) {
        //Aloca a qtd de bytes necessárias para armazenar os dados dos vertices
        ByteBuffer vetBytes = ByteBuffer.allocateDirect(coordenadas.length * 4);

        //Usa o sistema de endereçamento de memória
        //nativo no processador em questão
        vetBytes.order(ByteOrder.nativeOrder());

        //cria o FloatBuffer a partir do byteBuffer
        FloatBuffer buffer = vetBytes.asFloatBuffer();

        //Limpa um eventual lixo de memória
        buffer.clear();

        //Encapsula o array java no objeto Float Buffer
        buffer.put(coordenadas);

        //Retira as sobras de memória
        buffer.flip();

        //Retorna o objeto de coordenadas
        return buffer;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Este método é chamado n vezes por segundo para desenhar na tela.
        //(Determina o FPS)

        //Habilita o uso de textura
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //Habilita o desenho por vertices
        //Solicita openGL permissao para usar vetores de vertices
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        /*
        //Responsáveis por deixar as sprints transparentes quando sobrepostas
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        */

        gl.glLoadIdentity();

        if (posX >= larguraX) {
            posX = larguraX;
        }
        if (posX <= 0) {
            posX = 0;
        }
        if (posY >= alturaY) {
            posY = alturaY;
        }
        if (posY <= 0) {
            posY = 0;
        }
        if (MainActivity.sensorDirecaoX < 3) {
            posX += 10;
        }
        if (MainActivity.sensorDirecaoX > -3) {
            posX += -10;
        }
        if (MainActivity.sensorDirecaoY < 3) {
            posY += 10;
        }
        if (MainActivity.sensorDirecaoY > -3) {
            posY += -10;
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, codSol);
        gl.glTranslatef(posX, posY, 0);
        //gl.glColor4f(1, 1, 1, 0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, buffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, codTerra);
        gl.glRotatef(angulo, 0, 0, 1);
        gl.glTranslatef(350, 0, 0);
        gl.glRotatef(angulo, 0, 0, 4);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, codLua);
        gl.glRotatef(angulo, 0, 0, 1);
        gl.glTranslatef(300, 0, 0);
        gl.glRotatef(angulo, 0, 0, 1);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPopMatrix();

        angulo++;


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            posX = (int) motionEvent.getX();
            posY = alturaY - (int) motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

        }
        return true;
    }
}