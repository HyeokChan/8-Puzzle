package level2;
import java.awt.Frame;
import java.util.PriorityQueue;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
class print extends Thread
{
	int step=0;
	private JButton NumBtn[];
	ArrayList<int[][]> mylist = new ArrayList<int[][]>(); 
	print(JButton NumBtn[],ArrayList<int[][]> mylist)
	{
		this.NumBtn = NumBtn;
		this.mylist = mylist;
	}
	public void run()
	{
		while(true)
		{
			try {
				for(int i=0;i<mylist.size();i++)
				{
					Thread.sleep(500);
					step++;
					for(int j=0;j<mylist.get(i).length;j++)
					{
						for(int k=0;k<mylist.get(i).length;k++)
						{
							NumBtn[mylist.get(i).length*j+k].setText(Integer.toString(mylist.get(i)[j][k]));
							if(NumBtn[mylist.get(i).length*j+k].getText().equals("0"))
							{
								NumBtn[mylist.get(i).length*j+k].setText("");
								NumBtn[mylist.get(i).length*j+k].setEnabled(false);
							}
							else
							{
								NumBtn[mylist.get(i).length*j+k].setEnabled(true);
							}
						}
					}					
				}
				mylist.clear(); // 다음 게임의 에이스타를 위한 초기화
				NumBtn[NumBtn.length-1].setText("A:"+Integer.toString(step));
				step=0;
				break;
			}
			catch(InterruptedException e) { }
		}
	}
}
public class astar
{
	int[] row = { 1, 0, -1, 0 };
	int[] col = { 0, -1, 0, 1 };
	public boolean isSafe(int x, int y, int initial[][]) {  //안전검사  isSafe(min.x + row[i], min.y + col[i]
		return (x >= 0 && x < initial.length && y >= 0 && y < initial.length); // 빈칸의 인덱스가 범위를 벗어나는지 검사
	}
	
	ArrayList<int[][]> mylist = new ArrayList<int[][]>(); 
	
	public void printMat(int[][] matrix,JButton NumBtn[]  ) {  // 프린트매트릭스, 인자는 이차원배열 매트릭스   
		mylist.add(matrix); // 재귀적으로 프린트매트가 호출될때 그 값을 어레이리스트에 추가
	}
	public void printPath(Node root, JButton NumBtn[] ) {  // 프린트패스, 인자는 노드 루트
		if (root == null) {  
			return;
		}
		printPath(root.parent,NumBtn); // 재귀, 초기상태로 올라감
		printMat(root.matrix,NumBtn);  //결과 출력
	}
	
	public int calculateCost(int[][] initial, int[][] goal) { // 코스트 계산 , 인자는 초기상태와 목표상태
		int count = 0;
		int n = initial.length; // n은 3 2차원배열의 행의 갯수
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (initial[i][j] != 0 && initial[i][j] != goal[i][j]) {  //2차원배열 전체를 살펴볼때 0이 아닌고 목표와 같은자리가 아니면
					count++;  // 카운트 증가
				}
			}
		}
		return count;  // 카운트 반환, 
	}
	
	public void solve(int[][] initial, int[][] goal, int x, int y,JButton NumBtn[] ) { // 솔브, 인자는 이차원배열 초기,목표, x,y
		PriorityQueue<Node> pq = new PriorityQueue<Node>(1000, (a, b) -> (a.cost + a.level) - (b.cost + b.level));
		// 우선순위 큐 생성, 람다식 사용
		Node root = new Node(initial, x, y, x, y, 0, null); //노드 생성, 인자는 이차원배열,x,y,newx,newy,지금까지 이동횟수, 노드 파렌트
		root.cost = calculateCost(initial, goal);  // 칼큘레이터코스트는 목표와 같지않는 숫자 갯수,잘못배치된 타일개수
		pq.add(root); // 우선순위 큐에 삽입
		
		while (!pq.isEmpty()) { //큐가 다비어있지 않으면
			Node min = pq.poll();  // min에 큐pq에서 나온것을 넣는다, 우선순위큐 pq
			if (min.cost == 0) { // 잘못배치된 타일이 없으면, 최종상태
				printPath(min,NumBtn); // 출력을 위한 함수 호출
				print a = new print(NumBtn,mylist); // 스레드를 위한 클래스 객체 생성
				a.start(); // 스레드 시작
				return;
			}
			
			//잘못배치된 타일이 있으면
			for (int i = 0; i < 4; i++) { // i는 0,1,2,3  // 빈칸이 상하좌우 움직인 이후에 대해서 우선순위큐 삽입 
	            if (isSafe(min.x + row[i], min.y + col[i],initial)) {
	            	Node child = new Node(min.matrix, min.x, min.y, min.x + row[i], min.y + col[i], min.level + 1, min);
	            	child.cost = calculateCost(child.matrix, goal); //칼큘레이터코스트는 목표와 같지않는 숫자 갯수,잘못배치된 타일개수, 자식의 코스트계산
	            	pq.add(child); //우선순위큐에 집어넣음
	            }
	        }
		}
	}
}