package com.eagle.ui.demo;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class XiangQi extends JFrame {
	private static final long serialVersionUID = 1L;

	// /////////////////////常量///////////////////////////////////////
	private final boolean DEAD = false; // /////死亡的棋子

	private final boolean ALIVE = true; // /////活着的棋子

	private final boolean RED = false; // /////红方

	private final boolean BLACK = true; // /////黑方

	// ////////////////////////////////////////////////////////////////////
	private final String BING = "兵";

	private final String PAO = "炮";

	private final String MA = "马";

	private final String CHE = "车";

	private final String XIANG = "象";

	private final String SHI = "士";

	private final String SHUAI = "帅";

	// ////////////////////////////////////////////////////////////////////
	private final Color GREEN = new Color(0, 200, 0);

	// ////////////////////////////////////////////////////////////////////

	private LinkedList redChessList = new LinkedList();// 红方棋子

	private LinkedList blackChessList = new LinkedList();// 黑方棋子

	private Point2D[][] p = new Point2D[9][10];// 点位

	private Points[][] points = new Points[9][10];// 圆点

	private MainPanel mainPane = new MainPanel();

	private Line2D[] heng = new Line2D[10];

	private Line2D[] shu1 = new Line2D[9];

	private Line2D[] shu2 = new Line2D[9];

	private Rectangle2D range = new Rectangle2D.Float(25, 25, 450, 500);

	// ////////////////////////////////////////////////////////////////////////////
	private Chess drag = null;// 将要移动的棋子

	private Point2D lastLocation = null;

	private boolean player = BLACK;

	private boolean win = false;

	// ////////////////////////////////////////////////////////////////////////////

	private class Chess// 棋子
	{
		public boolean chessColor = false;

		public String name = null;

		public Point2D point = null;

		public boolean state = false;

		public Ellipse2D circle = new Ellipse2D.Float();

		public int moved = 0;

		public Chess(boolean a, String b, Point2D c, boolean d) {
			chessColor = a;
			name = b;
			point = c;
			state = d;
			float x = (float) point.getX();
			float y = (float) point.getY();
			circle.setFrameFromCenter(point, new Point2D.Float(x + 20, y + 20));
		}

		public void setPoint(Point2D c) {
			point = c;
			float x = (float) point.getX();
			float y = (float) point.getY();
			circle.setFrameFromCenter(point, new Point2D.Float(x + 20, y + 20));
		}
	}

	private void initChess()// 初试化棋子
	{
		for (int i = 0; i < 5; i++) {
			redChessList.add(new Chess(RED, BING, p[i * 2][3], ALIVE));
			blackChessList.add(new Chess(BLACK, BING, p[i * 2][6], ALIVE));
		}
		for (int i = 0; i < 2; i++) {
			redChessList.add(new Chess(RED, PAO, p[i * 6 + 1][2], ALIVE));
			redChessList.add(new Chess(RED, CHE, p[i * 8][0], ALIVE));
			redChessList.add(new Chess(RED, MA, p[i * 6 + 1][0], ALIVE));
			redChessList.add(new Chess(RED, XIANG, p[i * 4 + 2][0], ALIVE));
			redChessList.add(new Chess(RED, SHI, p[i * 2 + 3][0], ALIVE));

			blackChessList.add(new Chess(BLACK, PAO, p[i * 6 + 1][7], ALIVE));
			blackChessList.add(new Chess(BLACK, CHE, p[i * 8][9], ALIVE));
			blackChessList.add(new Chess(BLACK, MA, p[i * 6 + 1][9], ALIVE));
			blackChessList.add(new Chess(BLACK, XIANG, p[i * 4 + 2][9], ALIVE));
			blackChessList.add(new Chess(BLACK, SHI, p[i * 2 + 3][9], ALIVE));
		}
		redChessList.add(new Chess(RED, SHUAI, p[4][0], ALIVE));
		blackChessList.add(new Chess(BLACK, SHUAI, p[4][9], ALIVE));
	}

	private Chess find(Point2D p) {
		LinkedList temList = null;
		if (player == BLACK) {
			temList = blackChessList;
		} else {
			temList = redChessList;
		}
		for (int i = 0, len = temList.size(); i < len; i++) {
			if (((Chess) temList.get(i)).circle.contains(p)
					&& ((Chess) temList.get(i)).state == ALIVE) {
				return (Chess) temList.get(i);
			}
		}
		return null;
	}

	private int setLocat(Chess chess) {
		if (chess != null) {
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 10; y++) {
					if (chess.circle.contains(p[x][y])
							&& points[x][y].fillColor == true) {
						chess.setPoint(p[x][y]);
						if (p[x][y] == lastLocation) {
							return 0;
						}
						return 1;
					}
				}
			}
			return 0;
		}
		return -1;
	}

	private Pair findXY(Chess c) {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 10; y++) {
				if (c.point == p[x][y]) {
					return new Pair(x, y);
				}
			}
		}
		return null;
	}

	private class Pair {
		public int x = 0;

		public int y = 0;

		public Pair(int a, int b) {
			x = a;
			y = b;
		}
	}

	private void releaseRule() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 10; y++) {
				points[x][y].fillColor = false;
			}
		}
	}

	private void getRule(Chess chess) {
		Pair tem = findXY(chess);
		int x = tem.x;
		int y = tem.y;

		if (chess.name == MA) {
			if (x + 1 < 9 && y + 2 < 10 && find(p[x + 1][y + 2]) == null) {
				if (findAll(p[x][y + 1]) == null)
					points[x + 1][y + 2].fillColor = true;
			}
			if (x + 1 < 9 && y - 2 >= 0 && find(p[x + 1][y - 2]) == null) {
				if (findAll(p[x][y - 1]) == null)
					points[x + 1][y - 2].fillColor = true;
			}
			if (x - 1 >= 0 && y + 2 < 10 && find(p[x - 1][y + 2]) == null) {
				if (findAll(p[x][y + 1]) == null)
					points[x - 1][y + 2].fillColor = true;
			}
			if (x - 1 >= 0 && y - 2 >= 0 && find(p[x - 1][y - 2]) == null) {
				if (findAll(p[x][y - 1]) == null)
					points[x - 1][y - 2].fillColor = true;
			}
			if (x + 2 < 9 && y + 1 < 10 && find(p[x + 2][y + 1]) == null) {
				if (findAll(p[x + 1][y]) == null)
					points[x + 2][y + 1].fillColor = true;
			}
			if (x + 2 < 9 && y - 1 >= 0 && find(p[x + 2][y - 1]) == null) {
				if (findAll(p[x + 1][y]) == null)
					points[x + 2][y - 1].fillColor = true;
			}
			if (x - 2 >= 0 && y + 1 < 10 && find(p[x - 2][y + 1]) == null) {
				if (findAll(p[x - 1][y]) == null)
					points[x - 2][y + 1].fillColor = true;
			}
			if (x - 2 >= 0 && y - 1 >= 0 && find(p[x - 2][y - 1]) == null) {
				if (findAll(p[x - 1][y]) == null)
					points[x - 2][y - 1].fillColor = true;
			}
		} else if (chess.name == XIANG) {
			if (x + 2 < 9 && y + 2 < 10 && find(p[x + 2][y + 2]) == null) {
				if (findAll(p[x + 1][y + 1]) == null) {
					if (y + 2 < 5 || player == BLACK)
						points[x + 2][y + 2].fillColor = true;
				}
			}
			if (x + 2 < 9 && y - 2 >= 0 && find(p[x + 2][y - 2]) == null) {
				if (findAll(p[x + 1][y - 1]) == null) {
					if (player == RED || y - 2 > 4)
						points[x + 2][y - 2].fillColor = true;
				}
			}
			if (x - 2 >= 0 && y + 2 < 10 && find(p[x - 2][y + 2]) == null) {
				if (findAll(p[x - 1][y + 1]) == null) {
					if (player == BLACK || y + 2 < 5)
						points[x - 2][y + 2].fillColor = true;
				}
			}
			if (x - 2 >= 0 && y - 2 >= 0 && find(p[x - 2][y - 2]) == null) {
				if (findAll(p[x - 1][y - 1]) == null) {
					if (player == RED || y - 2 > 4)
						points[x - 2][y - 2].fillColor = true;
				}
			}
		} else if (chess.name == BING) {
			if (player == BLACK) {
				if (y >= 5) {
					if (find(p[x][y - 1]) == null) {
						points[x][y - 1].fillColor = true;
					}
				} else {
					if (y - 1 >= 0 && find(p[x][y - 1]) == null)
						points[x][y - 1].fillColor = true;
					if (x - 1 >= 0 && find(p[x - 1][y]) == null)
						points[x - 1][y].fillColor = true;
					if (x + 1 < 9 && find(p[x + 1][y]) == null)
						points[x + 1][y].fillColor = true;
				}
			} else {
				if (y <= 4) {
					if (find(p[x][y + 1]) == null) {
						points[x][y + 1].fillColor = true;
					}
				} else {
					if (y + 1 < 10 && find(p[x][y + 1]) == null) {
						points[x][y + 1].fillColor = true;
					}
					if (x - 1 >= 0 && find(p[x - 1][y]) == null)
						points[x - 1][y].fillColor = true;
					if (x + 1 < 9 && find(p[x + 1][y]) == null)
						points[x + 1][y].fillColor = true;
				}
			}
		} else if (chess.name == CHE) {
			for (int i = x + 1; i < 9; i++) {

				if (findAll(p[i][y]) != null) {
					if (find(p[i][y]) == null) {
						points[i][y].fillColor = true;
						break;
					} else {
						break;
					}
				}
				points[i][y].fillColor = true;
			}
			for (int i = x - 1; i >= 0; i--) {

				if (findAll(p[i][y]) != null) {
					if (find(p[i][y]) == null) {
						points[i][y].fillColor = true;
						break;
					} else {
						break;
					}
				}
				points[i][y].fillColor = true;
			}
			for (int i = y + 1; i < 10; i++) {
				if (findAll(p[x][i]) != null) {
					if (find(p[x][i]) == null) {
						points[x][i].fillColor = true;
						break;
					} else {
						break;
					}
				}
				points[x][i].fillColor = true;
			}
			for (int i = y - 1; i >= 0; i--) {
				if (findAll(p[x][i]) != null) {
					if (find(p[x][i]) == null) {
						points[x][i].fillColor = true;
						break;
					} else {
						break;
					}
				}
				points[x][i].fillColor = true;
			}
		} else if (chess.name == SHI) {
			if (player == BLACK) {
				if (x + 1 < 6 && y + 1 < 10 && find(p[x + 1][y + 1]) == null) {
					points[x + 1][y + 1].fillColor = true;
				}
				if (x + 1 < 6 && y - 1 > 6 && find(p[x + 1][y - 1]) == null) {
					points[x + 1][y - 1].fillColor = true;
				}
				if (x - 1 > 2 && y + 1 < 10 && find(p[x - 1][y + 1]) == null) {
					points[x - 1][y + 1].fillColor = true;
				}
				if (x - 1 > 2 && y - 1 > 6 && find(p[x - 1][y - 1]) == null) {
					points[x - 1][y - 1].fillColor = true;
				}
			} else if (player == RED) {
				if (x + 1 < 6 && y + 1 < 3 && find(p[x + 1][y + 1]) == null) {
					points[x + 1][y + 1].fillColor = true;
				}
				if (x + 1 < 6 && y - 1 >= 0 && find(p[x + 1][y - 1]) == null) {
					points[x + 1][y - 1].fillColor = true;
				}
				if (x - 1 > 2 && y + 1 < 3 && find(p[x - 1][y + 1]) == null) {
					points[x - 1][y + 1].fillColor = true;
				}
				if (x - 1 > 2 && y - 1 >= 0 && find(p[x - 1][y - 1]) == null) {
					points[x - 1][y - 1].fillColor = true;
				}
			}
		} else if (chess.name == SHUAI) {
			if (player == BLACK) {
				if (x + 1 < 6 && find(p[x + 1][y]) == null) {
					points[x + 1][y].fillColor = true;
				}
				if (x - 1 > 2 && find(p[x - 1][y]) == null) {
					points[x - 1][y].fillColor = true;
				}
				if (y + 1 < 10 && find(p[x][y + 1]) == null) {
					points[x][y + 1].fillColor = true;
				}
				if (y - 1 > 6 && find(p[x][y - 1]) == null) {
					points[x][y - 1].fillColor = true;
				}
			} else {
				if (x + 1 < 6 && find(p[x + 1][y]) == null) {
					points[x + 1][y].fillColor = true;
				}
				if (x - 1 > 2 && find(p[x - 1][y]) == null) {
					points[x - 1][y].fillColor = true;
				}
				if (y + 1 < 3 && find(p[x][y + 1]) == null) {
					points[x][y + 1].fillColor = true;
				}
				if (y - 1 > -1 && find(p[x][y - 1]) == null) {
					points[x][y - 1].fillColor = true;
				}
			}
		} else if (chess.name == PAO) {
			int k = 0;
			for (int i = x + 1; i < 9; i++) {
				if (findAll(p[i][y]) != null) {
					k = i + 1;
					break;
				}
				points[i][y].fillColor = true;
			}
			for (int j = k; j < 9; j++) {
				if (findOther(p[j][y]) != null) {
					points[j][y].fillColor = true;
					break;
				}
			}
			k = 0;
			for (int i = x - 1; i >= 0; i--) {
				if (findAll(p[i][y]) != null) {
					k = i - 1;
					break;
				}
				points[i][y].fillColor = true;
			}
			for (int j = k; j >= 0; j--) {
				if (findOther(p[j][y]) != null) {
					points[j][y].fillColor = true;
					break;
				}
			}
			k = 0;
			for (int i = y + 1; i < 10; i++) {
				if (findAll(p[x][i]) != null) {
					k = i + 1;
					break;
				}
				points[x][i].fillColor = true;
			}
			for (int j = k; j < 10; j++) {
				if (findOther(p[x][j]) != null) {
					points[x][j].fillColor = true;
					break;
				}
			}
			k = 0;
			for (int i = y - 1; i >= 0; i--) {
				if (findAll(p[x][i]) != null) {
					k = i - 1;
					break;
				}
				points[x][i].fillColor = true;
			}
			for (int j = k; j >= 0; j--) {
				if (findOther(p[x][j]) != null) {
					points[x][j].fillColor = true;
					break;
				}
			}
		}
	}

	private Chess findOther(Point2D p) {
		LinkedList temList = null;
		if (player == RED) {
			temList = blackChessList;
		} else {
			temList = redChessList;
		}
		for (int i = 0, len = temList.size(); i < len; i++) {
			if (((Chess) temList.get(i)).circle.contains(p)
					&& ((Chess) temList.get(i)).state == ALIVE) {
				return (Chess) temList.get(i);
			}
		}
		return null;
	}

	private Chess findAll(Point2D p) {
		Chess c = find(p);
		if (c == null) {
			c = findOther(p);
		}
		return c;
	}

	public void killChess(Chess killer) {
		Chess tem = findOther(killer.point);
		// System.out.println(tem.chessColor==RED);

		if (tem != null && tem.chessColor != player) {
			tem.state = DEAD;
		} else if (tem != null) {
			killer.setPoint(lastLocation);
			player = !player;
		}
	}

	XiangQi()// //////////////////////////////////////////////////构造方法
	{
		super("象棋");
		this.setDefaultCloseOperation(3);
		this.setBounds(200, 100, 500, 590);
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 10; y++) {
				p[x][y] = new Point2D.Float(x * 50 + 50, y * 50 + 50);
				Ellipse2D tem = new Ellipse2D.Float();
				tem.setFrameFromCenter(p[x][y], new Point2D.Float(x * 50 + 54,
						y * 50 + 54));
				points[x][y] = new Points(p[x][y], tem);
			}
		}
		for (int i = 0; i < 10; i++) {
			heng[i] = new Line2D.Float(p[0][i], p[8][i]);
		}
		for (int i = 0; i < 9; i++) {
			shu1[i] = new Line2D.Float(p[i][0], p[i][4]);
			if (i == 0 || i == 8)
				shu2[i] = new Line2D.Float(p[i][4], p[i][9]);
			else
				shu2[i] = new Line2D.Float(p[i][5], p[i][9]);
		}
		initChess();
		init();
		// System.out.println(((Chess)redChessList.get(15)).name);
		this.setVisible(true);
	}

	private void init() {
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		// jp.add(new JTextField(),"North");
		jp.add(mainPane);
		this.setContentPane(jp);
	}

	private void winLose() {
		if (((Chess) redChessList.get(15)).state == DEAD) {
			win = true;
			JOptionPane.showMessageDialog(null, "红方的主将已经阵亡!");
			for (int i = 0; i < 16; i++) {
				((Chess) redChessList.get(i)).state = DEAD;
			}
			repaint();
			JOptionPane.showMessageDialog(null, "黑方胜利!");
		} else if (((Chess) blackChessList.get(15)).state == DEAD) {
			win = true;
			JOptionPane.showMessageDialog(null, "黑方的主将已经阵亡!");
			for (int i = 0; i < 16; i++) {
				((Chess) blackChessList.get(i)).state = DEAD;
			}
			repaint();
			JOptionPane.showMessageDialog(null, "红方胜利!");
		}

	}

	class Points {
		Point2D point = null;

		Ellipse2D circle = null;

		boolean fillColor = false;

		Points(Point2D a, Ellipse2D b) {
			point = a;
			circle = b;
		}
	}

	class MainPanel extends JPanel implements MouseMotionListener,
			MouseListener {
		private static final long serialVersionUID = 1L;

		Graphics2D g = null;

		public MainPanel() {
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
		}

		public void paintComponent(Graphics e) {
			super.paintComponent(e);
			this.setBackground(Color.white);
			g = (Graphics2D) e;
			g.setColor(Color.pink);
			g.draw(range);

			for (int i = 0; i < 10; i++) {
				g.draw(heng[i]);
				if (i < 9) {
					g.draw(shu1[i]);
					g.draw(shu2[i]);
				}
			}

			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 10; y++) {
					if (points[x][y].fillColor == true) {
						if (player == BLACK)
							g.setColor(GREEN);
						else
							g.setColor(Color.blue);
						g.fill(points[x][y].circle);
						g.setColor(Color.pink);
					} else {
						g.draw(points[x][y].circle);
					}
				}
			}

			int len = redChessList.size();
			// System.out.println(len);
			for (int i = 0; i < len; i++) {

				g.setColor(Color.red);
				Chess temRed = (Chess) redChessList.get(i);
				Pair pair1 = findXY(temRed);
				if (pair1 != null && points[pair1.x][pair1.y].fillColor == true
						&& player == BLACK) {
					g.setColor(GREEN);
				}
				if (temRed.state == ALIVE) {
					g.fill(temRed.circle);
					g.setColor(Color.white);
					g.drawString(temRed.name, (float) temRed.point.getX() - 4,
							(float) temRed.point.getY() + 4);
				}
				g.setColor(Color.black);
				Chess tem = (Chess) blackChessList.get(i);
				Pair pair2 = findXY(tem);
				if (pair2 != null && points[pair2.x][pair2.y].fillColor == true
						&& player == RED) {
					g.setColor(Color.blue);
				}
				if (tem.state == ALIVE) {
					g.fill(tem.circle);
					g.setColor(Color.white);
					g.drawString(tem.name, (float) tem.point.getX() - 4,
							(float) tem.point.getY() + 4);
				}
			}
			g.setColor(Color.pink);
		}

		public void cross() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}

		public void deleteCross() {
			this.setCursor(Cursor.getDefaultCursor());
		}

		public void mouseDragged(MouseEvent e) {
			if (drag != null) {
				drag.setPoint(e.getPoint());
				repaint();
			}
		}

		public void mouseMoved(MouseEvent e) {
			Chess tem = find(e.getPoint());
			if (tem != null) {
				cross();
			} else {
				deleteCross();
			}
		}

		public void mousePressed(MouseEvent e) {
			drag = find(e.getPoint());
			if (drag != null) {
				lastLocation = drag.point;
				getRule(drag);
			} else {
				lastLocation = null;
			}
		}

		public void mouseReleased(MouseEvent e) {
			int temp = setLocat(drag);
			if (temp == 0) {
				drag.setPoint(lastLocation);
			} else if (temp == 1) {
				killChess(drag);
				player = !player;
			}
			releaseRule();

			// System.out.println("apple");
			drag = null;
			repaint();
			if (win == false) {
				winLose();
			}
		}

		// /////////////////////////////////////////////////////////////////////////////////////
		public void mouseClicked(MouseEvent e) {
		}// //

		public void mouseEntered(MouseEvent e) {
		}// //

		public void mouseExited(MouseEvent e) {
		}// //////
		// ///////////////////////////////////////////////////////////////////////////////////////
	}
	
	public static void main(String args[]) {
		// TODO Auto-generated method stub
		new XiangQi();
	}
}
