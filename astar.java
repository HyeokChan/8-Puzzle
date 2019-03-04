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
				mylist.clear(); // ���� ������ ���̽�Ÿ�� ���� �ʱ�ȭ
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
	public boolean isSafe(int x, int y, int initial[][]) {  //�����˻�  isSafe(min.x + row[i], min.y + col[i]
		return (x >= 0 && x < initial.length && y >= 0 && y < initial.length); // ��ĭ�� �ε����� ������ ������� �˻�
	}
	
	ArrayList<int[][]> mylist = new ArrayList<int[][]>(); 
	
	public void printMat(int[][] matrix,JButton NumBtn[]  ) {  // ����Ʈ��Ʈ����, ���ڴ� �������迭 ��Ʈ����   
		mylist.add(matrix); // ��������� ����Ʈ��Ʈ�� ȣ��ɶ� �� ���� ��̸���Ʈ�� �߰�
	}
	public void printPath(Node root, JButton NumBtn[] ) {  // ����Ʈ�н�, ���ڴ� ��� ��Ʈ
		if (root == null) {  
			return;
		}
		printPath(root.parent,NumBtn); // ���, �ʱ���·� �ö�
		printMat(root.matrix,NumBtn);  //��� ���
	}
	
	public int calculateCost(int[][] initial, int[][] goal) { // �ڽ�Ʈ ��� , ���ڴ� �ʱ���¿� ��ǥ����
		int count = 0;
		int n = initial.length; // n�� 3 2�����迭�� ���� ����
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (initial[i][j] != 0 && initial[i][j] != goal[i][j]) {  //2�����迭 ��ü�� ���캼�� 0�� �ƴѰ� ��ǥ�� �����ڸ��� �ƴϸ�
					count++;  // ī��Ʈ ����
				}
			}
		}
		return count;  // ī��Ʈ ��ȯ, 
	}
	
	public void solve(int[][] initial, int[][] goal, int x, int y,JButton NumBtn[] ) { // �ֺ�, ���ڴ� �������迭 �ʱ�,��ǥ, x,y
		PriorityQueue<Node> pq = new PriorityQueue<Node>(1000, (a, b) -> (a.cost + a.level) - (b.cost + b.level));
		// �켱���� ť ����, ���ٽ� ���
		Node root = new Node(initial, x, y, x, y, 0, null); //��� ����, ���ڴ� �������迭,x,y,newx,newy,���ݱ��� �̵�Ƚ��, ��� �ķ�Ʈ
		root.cost = calculateCost(initial, goal);  // Įŧ�������ڽ�Ʈ�� ��ǥ�� �����ʴ� ���� ����,�߸���ġ�� Ÿ�ϰ���
		pq.add(root); // �켱���� ť�� ����
		
		while (!pq.isEmpty()) { //ť�� �ٺ������ ������
			Node min = pq.poll();  // min�� ťpq���� ���°��� �ִ´�, �켱����ť pq
			if (min.cost == 0) { // �߸���ġ�� Ÿ���� ������, ��������
				printPath(min,NumBtn); // ����� ���� �Լ� ȣ��
				print a = new print(NumBtn,mylist); // �����带 ���� Ŭ���� ��ü ����
				a.start(); // ������ ����
				return;
			}
			
			//�߸���ġ�� Ÿ���� ������
			for (int i = 0; i < 4; i++) { // i�� 0,1,2,3  // ��ĭ�� �����¿� ������ ���Ŀ� ���ؼ� �켱����ť ���� 
	            if (isSafe(min.x + row[i], min.y + col[i],initial)) {
	            	Node child = new Node(min.matrix, min.x, min.y, min.x + row[i], min.y + col[i], min.level + 1, min);
	            	child.cost = calculateCost(child.matrix, goal); //Įŧ�������ڽ�Ʈ�� ��ǥ�� �����ʴ� ���� ����,�߸���ġ�� Ÿ�ϰ���, �ڽ��� �ڽ�Ʈ���
	            	pq.add(child); //�켱����ť�� �������
	            }
	        }
		}
	}
}