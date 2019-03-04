package level2;

public class Node {

	public Node parent;
	public int[][] matrix; //이차원 배열 매트릭스
	public int x, y;
	public int cost;
	public int level;
	
	//노드 생성자, 인자는 이차원배열,x,y,newx,newy,지금까지 이동횟수, 노드 파렌트
	public Node(int[][] matrix, int x, int y, int newX, int newY, int level, Node parent) {
		this.parent = parent; 
		this.matrix = new int[matrix.length][];  // 
		for (int i = 0; i < matrix.length; i++) {
			this.matrix[i] = matrix[i].clone();  //받아온 이차원배열 복사
		}
		this.matrix[x][y] = this.matrix[newX][newY];
		this.matrix[newX][newY] = 0;
		this.cost = Integer.MAX_VALUE;
		this.level = level;
		this.x = newX;
		this.y = newY;
	}
	
}