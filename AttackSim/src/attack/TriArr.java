package attack;

public class TriArr<T> {

	private Object[] arr;
	public TriArr(int numObjects) {
		arr = new Object[triangle(numObjects)];
	}
	
	@SuppressWarnings("unchecked")
	public T get(int i, int j) {
		return (T) arr[triangle(Math.max(i,j)) + Math.min(i, j)];
	}
	public void set(int i, int j, T obj) {
		arr[triangle(Math.max(i,j))+Math.min(i, j)] = obj;
	}
	private int triangle(int n) {
		return (n * (n+1) / 2);
	}
}
