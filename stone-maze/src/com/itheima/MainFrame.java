package com.itheima;

import javax.swing.*;
        import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

//自定义主窗口
public class MainFrame extends JFrame {
    //定义两个整数变量记录当前空白色块的位置
    private int row;
    private int col;
    private int count;
    //准备一个数组，用于存储数字色块的行列位置
    private int[][] imageData = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };
    //准备一个数组作为胜利条件对比
    private int[][] winData = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    public MainFrame() {
        initFrame();
        initRandomArray();//展示图片之前打乱
        initImage();
        initMenu();
        initKeyPressEvent();
        //全部东西做完之后再显示出来
        this.setVisible(true);
    }

    //初始化窗口大小
    private void initFrame() {
        //设置标题
        this.setTitle("StoneMaze");
        //设置大小
        this.setSize(465, 580);
        //设置关闭模式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置居中显示
        this.setLocationRelativeTo(null);
        //取消默认布局
        this.setLayout(null);
    }

    //初始化界面图片,展示数字色块
    private void initImage() {
        //先清空窗口上的全部图层，否则会显示重复的图片
        this.getContentPane().removeAll();
        //刷新界面时，展示最新移动步数
        JLabel countTxt = new JLabel("当前移动" + count + "步");
        countTxt.setBounds(0, 0, 100, 20);
        this.add(countTxt);
        //判断是否赢了
        if (isWin()) {
            JLabel label = new JLabel(new ImageIcon("stone-maze/src/image/win.png"));
            label.setBounds(124, 230, 266, 88);
            this.add(label);
        }
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                int number = imageData[i][j];
                //创建图片标签
                JLabel label = new JLabel();
                label.setIcon(new ImageIcon("stone-maze/src/image/" + number + ".png"));
                //设置图片标签的位置
                label.setBounds(20 + j * 100, 60 + i * 100, 100, 100);
                //添加图片标签
                this.add(label);
            }
        }
        //设置背景图片
        JLabel background = new JLabel(new ImageIcon("stone-maze/src/image/background.png"));
        background.setBounds(0, 0, 450, 484);
        this.add(background);
        //关于图片展示顺序问题，Swing中后添加的组件会显示在先添加组件的上方，背景图片是最后添加的，应该显示在最上层
        //刷新新图层，重新绘制
        this.repaint();
    }

    private boolean isWin() {
        //判断游戏二维数组和赢了以后的二维数组的内容是否一致，只要有一个位置不一致，就返回false
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                if (imageData[i][j] != winData[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    //- 初始化界面菜单：系统退出，重启游戏。
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();//创建菜单栏
        JMenu menu = new JMenu("系统");//创建菜单
        JMenuItem exitJi = new JMenuItem("退出");//创建菜单项退出
        menu.add(exitJi);
        exitJi.addActionListener(e -> System.exit(0));//退出
        JMenuItem restartJi = new JMenuItem("重启");
        menu.add(restartJi);
        restartJi.addActionListener(e -> {
            dispose();
            new MainFrame();
        });
        menuBar.add(menu);//添加到菜单栏中
        this.setJMenuBar(menuBar);//添加到窗口中
    }

    //打乱顺序
    private void initRandomArray() {
        //打乱二维数组中的元素顺序
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData.length; j++) {
                //随机两个行列位置，让这两个位置交换
                int randomRow = (int) (Math.random() * imageData.length);
                int randomCol = (int) (Math.random() * imageData.length);
                int temp = imageData[i][j];
                imageData[i][j] = imageData[randomRow][randomCol];
                imageData[randomRow][randomCol] = temp;
            }
        }
        //定位空白色块的位置
        OUT:
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                if (imageData[i][j] == 0) {
                    row = i;
                    col = j;
                    break OUT;//跳出循环
                }
            }
        }
    }

    //给当前窗口绑定上下左右按键事件
    private void initKeyPressEvent() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //获取按键的编码
                int KeyCode = e.getKeyCode();
                //判断按键
                switch (KeyCode) {
                    //上
                    case KeyEvent.VK_UP:
                        switchAndMove(Direction.UP);
                        break;
                    //下
                    case KeyEvent.VK_DOWN:
                        switchAndMove(Direction.DOWN);
                        break;
                    //左
                    case KeyEvent.VK_LEFT:
                        switchAndMove(Direction.LEFT);
                        break;
                    //右
                    case KeyEvent.VK_RIGHT:
                        switchAndMove(Direction.RIGHT);
                        break;

                }
            }
        });
    }

    //控制数据交换，和图片移动
    private void switchAndMove(Direction r) {
        //判断图片的方向，再控制图片移动
        switch (r) {
            case UP:
                //上交换条件是行必须<3
                if (row < imageData.length - 1) {
                    int temp = imageData[row][col];
                    imageData[row][col] = imageData[row + 1][col];
                    imageData[row + 1][col] = temp;
                    //更新当前空白块的位置
                    row++;
                    count++;
                }
                break;
            case DOWN:
                //下交换条件是行必须>0
                if (row > 0) {
                    int temp = imageData[row][col];
                    imageData[row][col] = imageData[row - 1][col];
                    imageData[row - 1][col] = temp;
                    row--;
                    count++;
                }
                break;
            case LEFT:
                //左交换条件是列必须<3
                if (col < imageData.length - 1) {
                    int temp = imageData[row][col];
                    imageData[row][col] = imageData[row][col + 1];
                    imageData[row][col + 1] = temp;
                    col++;
                    count++;
                }
                break;
            case RIGHT:
                //右交换条件是列必须>0
                if (col > 0) {
                    int temp = imageData[row][col];
                    imageData[row][col] = imageData[row][col - 1];
                    imageData[row][col - 1] = temp;
                    col--;
                    count++;
                }
                break;
        }
        //重新刷新界面
        initImage();
    }
}