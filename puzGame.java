package level2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class puzGame extends JFrame {
	JButton[] NumBtn; // ���ӽ��۽� ����� ��ư�迭 : Player1
	JButton[] NumBtn2; //2�ο� ���ӽ��۽� ����� ��ư�迭 : Player2
	TextField MapTf = new TextField(1);  //������ ũ�� ������ ���� �ؽ�Ʈ�ʵ�
	Checkbox TwoPlayer = new Checkbox("2�ο� ����");  // 2�ο� ������ ���� üũ�ڽ�
	int MapSize=0; // ��ũ�⸦ ���� ����
	int En, En2; // ��ĭ �ε���
	JButton Astar = new JButton("A* : �ΰ����� �ع�"); // �ΰ������� ������ ���� ���̽�Ÿ ��ư
	
	puzGame () {
		Container contentPane = getContentPane();  //ù��° ����â �����̳�
		setTitle("Puzzle Game");
		contentPane.setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel GameTitle = new JLabel("8 - PUZZLE GAME");
		GameTitle.setFont(new Font("����",Font.BOLD,28));
		JLabel Blank = new JLabel("                ");
		Blank.setFont(new Font("����",Font.BOLD,30));
		JLabel Size = new JLabel("N x N ũ���� ���� ����");
		JButton Input = new JButton("�Է�");
		contentPane.add(GameTitle);
		contentPane.add(Blank);
		contentPane.add(Size);
		contentPane.add(MapTf);
		contentPane.add(Input);
		contentPane.add(TwoPlayer);
		contentPane.add(Astar);
		Input.addActionListener(new MyActionListener()); // �Է¹�ư�� ���� �׼Ǹ����� ���
		Astar.addActionListener(new MyActionListener()); // �Է¹�ư�� ���� �׼Ǹ����� ���
		setSize(300,300);
		setVisible(true);
	}
	class MyActionListener implements ActionListener  {  //�ο��� ��ưŬ���� ���� �׼Ǹ�����
		int[][] initial;  // �ʱⰪ�� �������� �迭
		int[][] goal; //��ǥ �迭
		astar a = new astar(); // astar ��ü
		int x = 0, y = 0; // ��ĭ �ε���
		public void actionPerformed(ActionEvent e) { 
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("A* : �ΰ����� �ع�"))
			{
				initial = new int[MapSize][MapSize]; // �ʻ���� ���� �ʱ� ���� ����
				goal = new int[MapSize][MapSize]; // �ʻ���� ���� ��ǥ ���� ����
				for(int i=0;i<MapSize;i++) // �ʻ���� �´� ��ǥ���� ����
				{
					for(int j=0;j<MapSize;j++)
					{
						goal[i][j] = (MapSize*i+j)+1;
					}
				}
				goal[MapSize-1][MapSize-1] = 0; // ��ǥ������ ������ĭ�� 0(��ĭ)
				for(int i=0;i<MapSize;i++)  //���� ������ NumBtn�� ������ �ʱ���·� �ش�
				{
					for(int j=0;j<MapSize;j++)
					{
						if(NumBtn[(MapSize*i+j)].getText().equals(""))
						{
							initial[i][j] = 0;
							x=i;y=j;
						}
						else
						{
							initial[i][j] = Integer.parseInt(NumBtn[(MapSize*i+j)].getText());
						}
					}
				}
				
				a.solve(initial, goal, x, y,NumBtn); // astar��ü�� ���� solve �޼ҵ带 ����Ѵ�, ���ڴ� �ʱ����,��ǥ����,��ĭ��ġx,��ĭ��ġy
			}
			if(b.getText().equals("�Է�"))
			{
				Frame GamePane = new Frame("Game Start : Player1");  // ���ο� â ���� : ù��° �÷��̾� ������
				GamePane.setVisible(true);
				GamePane.addWindowListener(new WindowAdapter() { //�ι�° â�� ���� ����� ���� �̺�Ʈ������, �͸�Ŭ����
					public void windowClosing(WindowEvent e) {
						GamePane.setVisible(false);
						GamePane.dispose();
					}
				});
				
				int left,right,down,up;
				int n;
				MapSize = Integer.parseInt(MapTf.getText()); // �� ũ�⸦ ����
				En = MapSize*MapSize-1; // �ʱ��� ��ĭ�� ������ ĭ
				NumBtn = new JButton[MapSize*MapSize]; // ��ư�迭�� ��ũ�⿡ �°� ����
				GamePane.setLayout(new GridLayout(MapSize,MapSize,7,7)); //�׸��巹�̾ƿ��� ���� ��ư����
				for(int i=0;i<MapSize*MapSize;i++)
				{
					NumBtn[i] = new JButton(Integer.toString(i+1));  //�ʱ�ȭ ���� 1,2,3,4,5,6,7,8,9
					NumBtn[i].setFont(new Font("�ü�",Font.BOLD,50)); // ���ڸ� �ߺ��̰� �ϱ� ���ؼ�
					GamePane.add(NumBtn[i]);  // �����ǿ� ���ڹ�ư ����
					NumBtn[i].addKeyListener(new MyKeyListener()); // Ű�������� ���� �̵��� ���� ������ ���
				}
				NumBtn[MapSize*MapSize-1].setText(""); // 1,2,3,4,5,6,7,8,0
				NumBtn[MapSize*MapSize-1].setEnabled(false);  // ������ ��ư�� ��Ȱ��ȭ(��ĭ�� ����)
				
				for(int t=0;t<50;t++)  // ���������� ��ĭ�� ������
				{
					// ������� ������ ���ڿ��� ���Ƿ� ���ڸ� �ű��
					n = (int)(Math.random()*4)+1; 
					if(n==1)
					{
						left=En-1;
						if(left>=0 && (left+1)%MapSize!=0)
						{
							NumBtn[En].setText(NumBtn[left].getText());
							NumBtn[En].setEnabled(true);
							NumBtn[left].setText("");
							NumBtn[left].setEnabled(false);
							En=left;
						}
						else {t--;}
					}
					else if(n==2)
					{
						right=En+1;
						if(right<MapSize*MapSize && right%MapSize!=0)
						{
							NumBtn[En].setText(NumBtn[right].getText());
							NumBtn[En].setEnabled(true);
							NumBtn[right].setText("");
							NumBtn[right].setEnabled(false);
							En=right;
						}
						else {t--;}
					}
					else if(n==3)
					{
						up=En-MapSize;
						if(up>=0)
						{
							NumBtn[En].setText(NumBtn[up].getText());
							NumBtn[En].setEnabled(true);
							NumBtn[up].setText("");
							NumBtn[up].setEnabled(false);
							En=up;
						}
						else {t--;}
					}
					else if(n==4)
					{
						down=En+MapSize;
						if(down<MapSize*MapSize)
						{
							NumBtn[En].setText(NumBtn[down].getText());
							NumBtn[En].setEnabled(true);
							NumBtn[down].setText("");
							NumBtn[down].setEnabled(false);
							En=down;
						}
						else {t--;}
					}
				}
				
				GamePane.setSize(600,600);
				GamePane.setLocation(800,200);
				
				if(TwoPlayer.getState()) //2�ο� �ɼ�ó��, üũ�ڽ�
				{
					En2 = En;
					
					final Frame GamePane2 = new Frame("Game Start : Player2");  // ���ο� â ���� : �ι�° �÷��̾� ������
					GamePane2.setVisible(true);
					GamePane2.addWindowListener(new WindowAdapter() { //����° â�� ���� ����� ���� �̺�Ʈ������, �͸�Ŭ����
						public void windowClosing(WindowEvent e) {
							GamePane2.setVisible(false);
							GamePane2.dispose();
						}
					});
					NumBtn2 = new JButton[MapSize*MapSize]; // �ʻ���� �°� �迭����
					GamePane2.setLayout(new GridLayout(MapSize,MapSize,7,7)); //�׸��巹�̾ƿ��� ���� ��ư����
					for(int i=0;i<MapSize*MapSize;i++)
					{
						NumBtn2[i] = new JButton((NumBtn[i].getText()));  // ù��° �÷��̾��� �����ǰ� �Ȱ��� ��ư�迭 �ʱ�ȭ
						NumBtn2[i].setFont(new Font("�ü�",Font.BOLD,50));
						GamePane2.add(NumBtn2[i]);
						NumBtn2[i].addKeyListener(new MyKeyListener());
					}
					NumBtn2[En2].setEnabled(false);
					GamePane2.setSize(600,600);	
					GamePane2.setLocation(200,200);
				}
			}
			
			
		}
	}
	class MyKeyListener extends KeyAdapter {   // �����¿� Ű�� �̿��� �����̵� 
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode(); // �Էµ� Ű�ڵ�
			switch(keyCode) 
			{ 
			
			case KeyEvent.VK_UP: //��ĭ�� �������� ���ڰ� �ö�
				for(int index=0;index<MapSize*MapSize;index++)
				{
					if(index-MapSize>=0 && NumBtn[index-MapSize].getText().equals(""))  // �̵������� ��Ȳ�� ���� ����
					{
						NumBtn[index-MapSize].setText(NumBtn[index].getText());  // ��ư�� �ؽ�Ʈ�� ���� �ٲٰ�
						NumBtn[index-MapSize].setEnabled(true);   // Ȱ��ȭ, ��Ȱ��ȭ�� ��ȯ
						NumBtn[index].setText("");
						NumBtn[index].setEnabled(false);
						break;
					}
				}
			
				break;
			
				case KeyEvent.VK_DOWN: // ��ĭ�� �ö󰡰� ���ڰ� ������
					for(int index=0;index<MapSize*MapSize;index++)  // �ش� �̵��� ���� ���� �� �ݺ���ȯ
					{
						if(index+MapSize<MapSize*MapSize && NumBtn[index+MapSize].getText().equals(""))
						{
							NumBtn[index+MapSize].setText(NumBtn[index].getText());
							NumBtn[index+MapSize].setEnabled(true);
							NumBtn[index].setText("");
							NumBtn[index].setEnabled(false);
							break;
						}
					}
					
					break;
				case KeyEvent.VK_LEFT: // ��ĭ�� ���������� ���ڰ� ��������
					
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if((index)%MapSize!=0 && NumBtn[index-1].getText().equals(""))
						{
							
							NumBtn[index-1].setText(NumBtn[index].getText());
							NumBtn[index-1].setEnabled(true);
							NumBtn[index].setText("");
							NumBtn[index].setEnabled(false);
							break;
						}
					}
					
					break;
				case KeyEvent.VK_RIGHT: // ��ĭ�� �������� ���ڰ� ���������� En, En-1 ��ȯ
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if((index+1)%MapSize!=0 && NumBtn[index+1].getText().equals(""))
						{
							NumBtn[index+1].setText(NumBtn[index].getText());
							NumBtn[index+1].setEnabled(true);
							NumBtn[index].setText("");
							NumBtn[index].setEnabled(false);
							break;
						}
					}
					break;
			}
			if(TwoPlayer.getState()) //2�ο� �ɼ�ó��, üũ�ڽ�
			{
				switch(e.getKeyChar())   //2�ο������ ���� �ι�° �÷��̾��� Ű ����
				{ 
				case 'w':
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if(index-MapSize>=0 && NumBtn2[index-MapSize].getText().equals(""))
						{
							NumBtn2[index-MapSize].setText(NumBtn2[index].getText());
							NumBtn2[index-MapSize].setEnabled(true);
							NumBtn2[index].setText("");
							NumBtn2[index].setEnabled(false);
							break;
						}
					}
					break;
				case 's':
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if(index+MapSize<MapSize*MapSize && NumBtn2[index+MapSize].getText().equals(""))
						{
							NumBtn2[index+MapSize].setText(NumBtn2[index].getText());
							NumBtn2[index+MapSize].setEnabled(true);
							NumBtn2[index].setText("");
							NumBtn2[index].setEnabled(false);
							break;
						}
					}
					break;
				case 'a':
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if((index)%MapSize!=0 && NumBtn2[index-1].getText().equals(""))
						{
							
							NumBtn2[index-1].setText(NumBtn2[index].getText());
							NumBtn2[index-1].setEnabled(true);
							NumBtn2[index].setText("");
							NumBtn2[index].setEnabled(false);
							break;
						}
					}
					break;
				case 'd':
					for(int index=0;index<MapSize*MapSize;index++)
					{
						if((index+1)%MapSize!=0 && NumBtn2[index+1].getText().equals(""))
						{
							NumBtn2[index+1].setText(NumBtn2[index].getText());
							NumBtn2[index+1].setEnabled(true);
							NumBtn2[index].setText("");
							NumBtn2[index].setEnabled(false);
							break;
						}
					}
					break;
				}
			}
			int count=0;int count2=0;
			for(int q=0;q<MapSize*MapSize-1;q++)  // count ������ ���� ��������� ��
			{
				if(NumBtn[q].getText().equals(Integer.toString(q+1)))
				{
					count++;
				}
				if(TwoPlayer.getState())  // 2�ο������ ��쿡�� ����
				{
					if(NumBtn2[q].getText().equals(Integer.toString(q+1)))
					{
						count2++;
					}
				}
			}
			if(count == MapSize*MapSize-1)  // count�񱳸� ���� ���� �˻�, ����� �ؽ�Ʈ ���� �� ��Ȱ��ȭ
			{
				for(int k=0;k<MapSize*MapSize;k++)
				{
					NumBtn[k].setText("O");
					NumBtn[k].setEnabled(false);
					if(TwoPlayer.getState())
					{
						NumBtn2[k].setText("X");
						NumBtn2[k].setEnabled(false);
					}
					
				}

			}
			if(count2 == MapSize*MapSize-1)
			{
				for(int k=0;k<MapSize*MapSize;k++)
				{
					NumBtn2[k].setText("O");
					NumBtn2[k].setEnabled(false);
					NumBtn[k].setText("X");
					NumBtn[k].setEnabled(false);
				}
			}
		}
	}
	
	public static void main(String [] args) {
		new puzGame();
	}
}