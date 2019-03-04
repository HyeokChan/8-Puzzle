package level2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class puzGame extends JFrame {
	JButton[] NumBtn; // 게임시작시 사용할 버튼배열 : Player1
	JButton[] NumBtn2; //2인용 게임시작시 사용할 버튼배열 : Player2
	TextField MapTf = new TextField(1);  //게임판 크기 지정을 위한 텍스트필드
	Checkbox TwoPlayer = new Checkbox("2인용 선택");  // 2인용 게임을 위한 체크박스
	int MapSize=0; // 맵크기를 위한 변수
	int En, En2; // 빈칸 인덱스
	JButton Astar = new JButton("A* : 인공지능 해법"); // 인공지능을 실행을 위한 에이스타 버튼
	
	puzGame () {
		Container contentPane = getContentPane();  //첫번째 실행창 컨테이너
		setTitle("Puzzle Game");
		contentPane.setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel GameTitle = new JLabel("8 - PUZZLE GAME");
		GameTitle.setFont(new Font("굴림",Font.BOLD,28));
		JLabel Blank = new JLabel("                ");
		Blank.setFont(new Font("굴림",Font.BOLD,30));
		JLabel Size = new JLabel("N x N 크기의 게임 생성");
		JButton Input = new JButton("입력");
		contentPane.add(GameTitle);
		contentPane.add(Blank);
		contentPane.add(Size);
		contentPane.add(MapTf);
		contentPane.add(Input);
		contentPane.add(TwoPlayer);
		contentPane.add(Astar);
		Input.addActionListener(new MyActionListener()); // 입력버튼에 대한 액션리스너 등록
		Astar.addActionListener(new MyActionListener()); // 입력버튼에 대한 액션리스너 등록
		setSize(300,300);
		setVisible(true);
	}
	class MyActionListener implements ActionListener  {  //인원수 버튼클릭에 대한 액션리스너
		int[][] initial;  // 초기값을 저장해줄 배열
		int[][] goal; //목표 배열
		astar a = new astar(); // astar 객체
		int x = 0, y = 0; // 빈칸 인덱스
		public void actionPerformed(ActionEvent e) { 
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("A* : 인공지능 해법"))
			{
				initial = new int[MapSize][MapSize]; // 맵사이즈에 따른 초기 상태 설정
				goal = new int[MapSize][MapSize]; // 맵사이즈에 따른 목표 상태 설정
				for(int i=0;i<MapSize;i++) // 맵사이즈에 맞는 목표상태 설정
				{
					for(int j=0;j<MapSize;j++)
					{
						goal[i][j] = (MapSize*i+j)+1;
					}
				}
				goal[MapSize-1][MapSize-1] = 0; // 목표상태의 마지막칸은 0(빈칸)
				for(int i=0;i<MapSize;i++)  //섞인 상태의 NumBtn의 값들을 초기상태로 준다
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
				
				a.solve(initial, goal, x, y,NumBtn); // astar객체를 통해 solve 메소드를 사용한다, 인자는 초기상태,목표상태,빈칸위치x,빈칸위치y
			}
			if(b.getText().equals("입력"))
			{
				Frame GamePane = new Frame("Game Start : Player1");  // 새로운 창 열기 : 첫번째 플레이어 게임판
				GamePane.setVisible(true);
				GamePane.addWindowListener(new WindowAdapter() { //두번째 창만 끄게 만들기 위한 이벤트리스너, 익명클래스
					public void windowClosing(WindowEvent e) {
						GamePane.setVisible(false);
						GamePane.dispose();
					}
				});
				
				int left,right,down,up;
				int n;
				MapSize = Integer.parseInt(MapTf.getText()); // 맵 크기를 받음
				En = MapSize*MapSize-1; // 초기의 빈칸은 마지막 칸
				NumBtn = new JButton[MapSize*MapSize]; // 버튼배열을 맵크기에 맞게 생성
				GamePane.setLayout(new GridLayout(MapSize,MapSize,7,7)); //그리드레이아웃을 통해 버튼배정
				for(int i=0;i<MapSize*MapSize;i++)
				{
					NumBtn[i] = new JButton(Integer.toString(i+1));  //초기화 생성 1,2,3,4,5,6,7,8,9
					NumBtn[i].setFont(new Font("궁서",Font.BOLD,50)); // 숫자를 잘보이게 하기 위해서
					GamePane.add(NumBtn[i]);  // 게임판에 숫자버튼 부착
					NumBtn[i].addKeyListener(new MyKeyListener()); // 키움직임을 통한 이동을 통해 리스너 등록
				}
				NumBtn[MapSize*MapSize-1].setText(""); // 1,2,3,4,5,6,7,8,0
				NumBtn[MapSize*MapSize-1].setEnabled(false);  // 마지막 버튼은 비활성화(빈칸을 위해)
				
				for(int t=0;t<50;t++)  // 섞을때에는 빈칸이 움직임
				{
					// 순서대로 맞춰진 숫자에서 임의로 숫자를 옮긴다
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
				
				if(TwoPlayer.getState()) //2인용 옵션처리, 체크박스
				{
					En2 = En;
					
					final Frame GamePane2 = new Frame("Game Start : Player2");  // 새로운 창 열기 : 두번째 플레이어 게임판
					GamePane2.setVisible(true);
					GamePane2.addWindowListener(new WindowAdapter() { //세번째 창만 끄게 만들기 위한 이벤트리스너, 익명클래스
						public void windowClosing(WindowEvent e) {
							GamePane2.setVisible(false);
							GamePane2.dispose();
						}
					});
					NumBtn2 = new JButton[MapSize*MapSize]; // 맵사이즈에 맞게 배열생성
					GamePane2.setLayout(new GridLayout(MapSize,MapSize,7,7)); //그리드레이아웃을 통해 버튼배정
					for(int i=0;i<MapSize*MapSize;i++)
					{
						NumBtn2[i] = new JButton((NumBtn[i].getText()));  // 첫번째 플레이어의 게임판과 똑같이 버튼배열 초기화
						NumBtn2[i].setFont(new Font("궁서",Font.BOLD,50));
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
	class MyKeyListener extends KeyAdapter {   // 상하좌우 키를 이용한 숫자이동 
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode(); // 입력된 키코드
			switch(keyCode) 
			{ 
			
			case KeyEvent.VK_UP: //빈칸이 내려가고 숫자가 올라감
				for(int index=0;index<MapSize*MapSize;index++)
				{
					if(index-MapSize>=0 && NumBtn[index-MapSize].getText().equals(""))  // 이동가능한 상황에 대한 제어
					{
						NumBtn[index-MapSize].setText(NumBtn[index].getText());  // 버튼의 텍스트를 서로 바꾸고
						NumBtn[index-MapSize].setEnabled(true);   // 활성화, 비활성화도 교환
						NumBtn[index].setText("");
						NumBtn[index].setEnabled(false);
						break;
					}
				}
			
				break;
			
				case KeyEvent.VK_DOWN: // 빈칸이 올라가고 숫자가 내려감
					for(int index=0;index<MapSize*MapSize;index++)  // 해당 이동에 대한 제어 및 반복교환
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
				case KeyEvent.VK_LEFT: // 빈칸이 오른쪽으로 숫자가 왼쪽으로
					
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
				case KeyEvent.VK_RIGHT: // 빈칸이 왼쪽으로 숫자가 오른쪽으로 En, En-1 교환
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
			if(TwoPlayer.getState()) //2인용 옵션처리, 체크박스
			{
				switch(e.getKeyChar())   //2인용게임을 위한 두번째 플레이어의 키 제어
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
			for(int q=0;q<MapSize*MapSize-1;q++)  // count 변수를 통한 퍼즐정답과 비교
			{
				if(NumBtn[q].getText().equals(Integer.toString(q+1)))
				{
					count++;
				}
				if(TwoPlayer.getState())  // 2인용게임일 경우에만 한정
				{
					if(NumBtn2[q].getText().equals(Integer.toString(q+1)))
					{
						count2++;
					}
				}
			}
			if(count == MapSize*MapSize-1)  // count비교를 통해 정답 검사, 정답시 텍스트 변경 및 비활성화
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