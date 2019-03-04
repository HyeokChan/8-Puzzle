package level2;

public class Node {

	public Node parent;
	public int[][] matrix; //������ �迭 ��Ʈ����
	public int x, y;
	public int cost;
	public int level;
	
	//��� ������, ���ڴ� �������迭,x,y,newx,newy,���ݱ��� �̵�Ƚ��, ��� �ķ�Ʈ
	public Node(int[][] matrix, int x, int y, int newX, int newY, int level, Node parent) {
		this.parent = parent; 
		this.matrix = new int[matrix.length][];  // 
		for (int i = 0; i < matrix.length; i++) {
			this.matrix[i] = matrix[i].clone();  //�޾ƿ� �������迭 ����
		}
		this.matrix[x][y] = this.matrix[newX][newY];
		this.matrix[newX][newY] = 0;
		this.cost = Integer.MAX_VALUE;
		this.level = level;
		this.x = newX;
		this.y = newY;
	}
	
}